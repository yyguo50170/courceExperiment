import numpy as np
import random

# 交叉概率
pc = 0.8
# 变异概率
pm = 0.15
# 进化数
evolution = 2000
# 城市数
city_num = 21
# 种群数
size = 500

# 生成距离矩阵
Distance = np.zeros([city_num, city_num])


def read():
    i = 0
    j = 0
    with open("21.txt", 'r') as f:
        for line in f.readlines():
            linestr = line.strip()
            linestrlist = linestr.split(" ")
            for num in linestrlist:
                if len(num) == 0 or num == '' or num == ' ':
                    continue
                if j <= i:
                    Distance[i][j] = int(num)
                    j += 1
                else:
                    i += 1
                    j = 0
                    Distance[i][j] = int(num)
                    j += 1
    i = 0
    j = i + 1
    while i < city_num:
        if j < city_num:
            Distance[i][j] = Distance[j][i]
            j += 1
        else:
            i += 1
            j = i + 1
    print(Distance)


# 适应度函数
def get_distance(path):
    distance = 0
    for i in range(len(path) - 1):
        distance += Distance[path[i], path[i + 1]]
    distance = distance + Distance[path[len(path) - 1], path[0]]
    return distance


# 初始化种群
def get_races(size):
    races = []
    for i in range(size):
        races.append(np.random.permutation(city_num))
    return races


# 选择
def tournament(old_races, size):
    pool = []
    for _ in range(size):
        i, j = np.random.randint(0, size, 2)
        if get_distance(old_races[i]) < get_distance(old_races[j]):
            pool.append(old_races[i])
        else:
            pool.append(old_races[j])
    return pool


# 交配
def crossover(pool, city_num):
    new_races = []
    while len(new_races) < len(pool):
        i1, i2 = np.random.randint(0, len(pool), 2)
        p1 = pool[i1].copy()
        p2 = pool[i2].copy()
        # 概率大于pc就直接加入新种群，小于就进行交叉
        if np.random.random() > pc:
            new_races.extend([p1, p2])
        else:
            # 交叉
            p1, p2 = cross(p1, p2, city_num)
            # 概率小于pm就进行变异
            if np.random.random() < pm:
                # 变异
                p1 = variation(p1, city_num)
                p2 = variation(p2, city_num)
            new_races.extend([p1, p2])
    return new_races


# 变异
def variation(path, city_num):
    i1, i2 = random.sample(range(0, city_num - 1), 2)
    path[i1], path[i2] = path[i2], path[i1]
    return path


# 交叉
def cross(p1, p2, city_num):
    i1 = random.randint(0, city_num - 2)
    i2 = random.randint(i1 + 1, city_num - 1)
    temp1 = p2[i1:i2]
    temp2 = p1[i1:i2]
    p1len = 0
    p2len = 0
    newp1 = []
    newp2 = []
    for i in p1:
        if p1len == i1:
            newp1.extend(temp1)
            p1len += 1
        if i not in temp1:
            newp1.append(i)
            p1len += 1
    for j in p2:
        if p2len == i1:
            newp2.extend(temp2)
            p2len += 1
        if j not in temp2:
            newp2.append(j)
            p2len += 1
    return newp1, newp2


def main():
    races = get_races(size)
    minlen = 100000
    minrace = []
    for i in range(evolution):
        pool = tournament(races, size)  # 选择
        races = crossover(pool, city_num)  # 交配
        for race in races:
            len = get_distance(race)
            if len < minlen:
                minlen = len
                minrace = race.copy()
        print("第%d次" % i)
        print('路线为:', minrace)
        print('距离为:', minlen)
    print('最终路线为:', minrace)
    print('最终距离为:', minlen)


if __name__ == '__main__':
    read()
    main()
