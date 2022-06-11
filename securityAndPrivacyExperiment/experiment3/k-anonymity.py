import pandas as pd

# 计算分区中所有列的跨度，如果给定了规模，那么每列的跨度值会被该列的规模除，得到的就是相对跨度
def get_spans(df, partition, scale=None):
    spans = {}
    for column in df.columns:
        if column in categorical:
            span = len(df[column][partition].unique())
        else:
            span = df[column][partition].max() - df[column][partition].min()
        if scale is not None:
            span = span / scale[column]
        spans[column] = span
    return spans


def split(df, partition, column):
    dfp = df[column][partition]
    if column in categorical:
        values = dfp.unique()
        lv = set(values[:len(values) // 2])
        rv = set(values[len(values) // 2:])
        return dfp.index[dfp.isin(lv)], dfp.index[dfp.isin(rv)]
    else:
        median = dfp.median()
        dfl = dfp.index[dfp < median]
        dfr = dfp.index[dfp >= median]
        return (dfl, dfr)


def is_k_anonymous(df, partition, sensitive_column, k=5):
    if len(partition) < k:
        return False
    return True


def partition_dataset(df, feature_columns, sensitive_column, scale, is_valid, k=5):
    finished_partitions = []
    partitions = [df.index]
    while partitions:
        # 获得一个分区
        partition = partitions.pop(0)
        # 获得该分区的跨度
        spans = get_spans(df[feature_columns], partition, scale)
        for column, span in sorted(spans.items(), key=lambda x: -x[1]):
            # 将该分区分为2个分区
            lp, rp = split(df, partition, column)
            if not is_valid(df, lp, sensitive_column, k=k) or not is_valid(df, rp, sensitive_column, k=k):
                continue
            partitions.extend((lp, rp))
            break
        else:
            finished_partitions.append(partition)
    return finished_partitions


# 非数值型，聚合时使用逗号连接
def agg_categorical_column(series):
    return [','.join(set(series))]


# 对于数字型，聚合时使用均值
def agg_numerical_column(series):
    return series.mean()


def build_anonymized_dataset(df, partitions, feature_columns, sensitive_column, max_partitions=None):
    aggregations = {}
    for column in feature_columns:
        if column in categorical:
            aggregations[column] = agg_categorical_column
        else:
            aggregations[column] = agg_numerical_column
    rows = []
    for i, partition in enumerate(partitions):
        if i % 100 == 1:
            print("Finished {} partitions...".format(i))
        if max_partitions is not None and i > max_partitions:
            break
        grouped_columns = df.loc[partition].agg(aggregations, squeeze=False)
        sensitive_counts = df.loc[partition].groupby(sensitive_column).agg({sensitive_column: 'count'})
        df2 = grouped_columns.to_frame()
        grouped_columns = pd.DataFrame(df2.values.T, columns=df2.index)
        values = grouped_columns.iloc[0].to_dict()
        for sensitive_value, count in sensitive_counts[sensitive_column].items():
            if count == 0:
                continue
            values.update({
                sensitive_column: sensitive_value,
                'count': count,
            })
            rows.append(values.copy())
    return pd.DataFrame(rows)


if __name__ == '__main__':
    # 属性列名
    names = (
        'age',
        'workclass',
        'fnlwgt',
        'education',
        'education-num',
        'marital-status',
        'occupation',
        'relationship',
        'race',
        'sex',
        'capital-gain',
        'capital-loss',
        'hours-per-week',
        'native-country',
        'income',
    )
    # 分类数据，不是数字的数据
    categorical = set((
        'workclass',
        'education',
        'marital-status',
        'occupation',
        'relationship',
        'sex',
        'native-country',
        'race',
        'income',
    ))
    k = 6
    df = pd.read_csv("data/adult.data.txt", sep=", ", header=None, names=names, index_col=False, engine='python')
    # astype()方法显式地把一种数据类型转换为另一种
    for c in categorical:
        df[c] = df[c].astype('category')

    full_spans = get_spans(df, df.index)

    # 将数据分区
    feature_columns = [
        'age',
        'workclass',
        'fnlwgt',
        'education',
        'education-num',
        'marital-status',
        'occupation',
        'relationship',
        'race',
        'sex',
        'capital-gain',
        'capital-loss',
        'hours-per-week',
        'native-country'
    ]
    sensitive_column = 'income'
    finished_partitions = partition_dataset(df, feature_columns, sensitive_column, full_spans, is_k_anonymous, k)
    # 将每个分区的数据聚合（匿名化，数字型取平均值，非数值型变为联合）
    dfn = build_anonymized_dataset(df, finished_partitions, feature_columns, sensitive_column)
    # 将数据中的额外字符去掉，并排序输出
    dfn = dfn.apply(lambda x: x.explode().astype(str).groupby(level=0).agg(", ".join))
    dfn = dfn.sort_values(feature_columns + [sensitive_column])
    dfn.to_csv('data/out.csv', sep=',')
