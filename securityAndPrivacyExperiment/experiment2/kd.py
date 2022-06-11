import random
import sys
import numpy as np


class Node:  # 结点
    def __init__(self, data, lchild=None, rchild=None):
        self.data = data
        self.lchild = lchild
        self.rchild = rchild


class KdTree:  # kd树
    def __init__(self):
        self.nearestValue = None
        self.nearestPoint = None
        self.kdTree = None

    def create(self, dataSet, depth):  # 创建kd树，返回根结点
        if len(dataSet) > 0:
            m, n = np.shape(dataSet)  # 求出样本行，列
            axis = depth % n  # 判断以哪个轴划分数据
            leftDataSet = []
            rightDataSet = []
            node = Node(dataSet[0])  # 将节点数据设置为第一个数据
            i = 1
            while i < m:  # 以轴来比较，比第一个数据的小的放在左孩子序列，比第一个数据大的放在右孩子序列
                if dataSet[i][axis] < dataSet[0][axis]:
                    leftDataSet.append(dataSet[i])
                else:
                    rightDataSet.append(dataSet[i])
                i += 1
            node.lchild = self.create(leftDataSet, depth + 1)  # 将中位数左边样本传入来递归创建树
            node.rchild = self.create(rightDataSet, depth + 1)
            return node
        else:
            return None

    def preOrder(self, node):  # 前序遍历
        if node is not None:
            print("node->%s" % node.data)
            self.preOrder(node.lchild)
            self.preOrder(node.rchild)

    def search(self, tree, x):  # 搜索
        self.nearestPoint = None  # 保存最近的点
        self.nearestValue = 0  # 保存最近的值

        def travel(node, depth=0):  # 递归搜索，先寻找最近邻的叶子节点作为目标数据的近似最近点，然后再回溯查找
            if node is not None:  # 递归终止条件
                n = 2
                axis = depth % n  # 计算轴
                if x[axis] < node.data[axis]:  # 如果数据小于当前结点，则往左结点找
                    travel(node.lchild, depth + 1)
                else:
                    travel(node.rchild, depth + 1)

                # 递归完毕后，往父结点方向回朔
                distance = self.dist(x, node.data)  # 目标和节点的距离判断
                if self.nearestPoint is None or self.nearestValue > distance:  # 更新最近的点和最近的值
                    self.nearestPoint = node.data
                    self.nearestValue = distance

                print(node.data, '\t', depth, '\t', self.nearestValue, '\t', distance, '\t')
                if abs(x[axis] - node.data[
                    axis]) <= self.nearestValue:  # 确定是否需要去子节点的区域去找（圆的判断），以当前最近距离为半径，判断圆是否和另一个孩子节点的轴相交，如果相交则需要搜索
                    if x[axis] < node.data[axis]:
                        travel(node.rchild, depth + 1)
                    else:
                        travel(node.lchild, depth + 1)

        print('坐标\t\t树深度\t\t当前最近距离\t\t当前点与目标点距离\t\t')
        travel(tree)
        return self.nearestPoint

    def dist(self, x1, x2):  # 计算欧式距离
        return ((np.array(x1) - np.array(x2)) ** 2).sum() ** 0.5


if __name__ == '__main__':
    a = 0.5
    b = 0.5
    x = [a, b]
    datas = []
    factor = random.randint(1, 10000)
    with open(r"D:\DownLoad\NE.txt") as f:
        for line in f:
            data = [0, 0]
            tmp = line.strip().split(' ')
            data[0] = float(tmp[0]) * factor
            data[1] = float(tmp[1].strip()) * factor
            datas.append(data)
    kdtree = KdTree()
    tree = kdtree.create(datas, 0)  # 创建KD树
    kdtree.preOrder(tree)  # 先序遍历
    x[0] *= factor
    x[1] *= factor
    res = kdtree.search(tree, x)
    print("\n本次使用加密因子factor:", factor)
    print("给定的目标数据为:", [a, b], "\nkd树搜索得到的最近邻为:", res)
    print("\n遍历寻找最近邻ing......")
    min = sys.maxsize
    res1 = []
    for data in datas:
        distance = KdTree.dist(None, x, data)
        if distance < min:
            min = distance
            res1 = data
    print("遍历搜索的结果和KD树搜索的结果是否相等:", res1 == res)
    res[0] /= factor
    res[1] /= factor
    print("解密后的最近邻:", res)