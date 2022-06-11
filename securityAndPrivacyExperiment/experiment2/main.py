# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
import random


def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press Ctrl+F8 to toggle the breakpoint.


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    data = [[0,0],[1,1],[2,2],[3,3]]
    d1 = []
    d2 = []
    for d in data:
        if d[0] == 1:
            d1 = d
            d1 = d.copy()

    print("d1===", d1)
    d1[0] *= 3
    d1[1] *= 3
    print("d1===", d1)
    print(data)


# See PyCharm help at https://www.jetbrains.com/help/pycharm/
