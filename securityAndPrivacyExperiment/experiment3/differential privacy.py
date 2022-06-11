import numpy as np
import pandas as pd


# 加噪声
def add_laplace_noise(data_list, μ=0, b=1):
    laplace_noise = np.random.laplace(μ, b, len(data_list))  # 为原始数据添加μ为0，b为1的噪声
    res = np.append((laplace_noise + data_list.T[0]), data_list.T[1]).T
    return res.reshape(2, len(data_list)).T


def age_mean(a):
    sage = 0
    scnt = 0
    for row in a:
        sage += row[0] * row[1]
        scnt += row[1]
    return sage / scnt


if __name__ == "__main__":
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
    dfn = pd.read_csv("data/out.csv", sep=",", index_col=False, engine='python')
    dfn = dfn.iloc[:, [1, -1]]
    data = dfn.to_numpy()
    data1 = data
    x1 = age_mean(data)
    print("原始无噪声数据|均值：" + str(x1))

    noise_list = add_laplace_noise(data)
    y1 = age_mean(noise_list)
    print("加噪声后的数据|均值：" + str(y1))

    print("删除第一条合集")
    deleted = data[0]
    print(deleted)

    data = np.delete(data, 0, axis=0)
    x2 = age_mean(data)
    print("原始无噪声数据|均值：" + str(x2))

    noise_list = add_laplace_noise(data)
    y2 = age_mean(noise_list)
    print("加噪声后的数据|均值：" + str(y2))

    # 差分隐私分析
    ans1 = (x1 * 32561 - x2 * (32561 - deleted[1])) / deleted[1]
    ans2 = (y1 * 32561 - y2 * (32561 - deleted[1])) / deleted[1]
    print("根据K匿名数据集推出删除的数据:", ans1)
    print("根据加入噪声后K匿名数据集推出删除的数据:", ans2)