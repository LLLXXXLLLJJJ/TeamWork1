# -*- coding:utf-8 -*-
import sys

import numpy as np
from collections import Counter
import pymysql
import datetime

db = pymysql.connect('localhost', 'root', '123456', 'shop')
user_set = {}

def also_buy_recommendation(goodsID):
    '''
    输入商品ID，计算商品相似度，将最相似的10件商品插入（更新）数据库
    输入：商品ID，int
    '''
    listt = recommendation_1(goodsID)
    listt = popular_recommendation(list(listt))
    cur = db.cursor()
    if cur.execute(f'select * from alsobuy where goodsid={goodsID}'):
        cur.execute(f'update alsobuy set list="{listt}",createtime=now() where goodsid={goodsID}')
    else:
        cur.execute(f'insert into alsobuy (goodsid,list,createtime) value ({goodsID},"{listt}",now())')


def also_buy(user_IDs, item_IDs, item_labels, purchasing_date, item_ID, alpha=1 / 24 / 3600):
    '''
    实现够买了该商品的人还买了函数
    输入：
    user_IDs:用户ID，数据类型：列表或者int
    item_IDs:商品ID，数据类型：列表或者int
    purchasing_date:购买时间，数据类型：列表或者int
    alpha：时间衰减速率，alpha越大，则时间差对用户影响越大，数据类型：int 或者 float
    item_ID:用户购买的商品ID，数据类型：int
    输出：
    推荐列表，数据类型：列表
    '''
    W = ItemSimilarity(user_IDs, item_IDs, item_labels, purchasing_date, alpha)  # W[a][b]: 同时购买了a和b商品 0.8
    recommendation_list = most_similarity_items(W, item_ID)
    return recommendation_list


def ItemSimilarity(user_IDs, item_IDs, item_labels, purchasing_date, alpha):
    '''
    生成商品相似度矩阵，更新用户倒排表
    使用条件：
    每当用户产生购买行为时调用，函数会更新user_set（用户倒排表），并产生商品相似度字典W
    user_set：用来存储用户的行为数据。key是用户ID，value同样是一个字典，key是商品ID，value是[购买时间,评分，物品所属label]
    W（物品相似度字典）：key是商品名称（如'a'），value同样是一个字典，key是商品名称(如'b')，则W[a][b]是商品a和b的相似度
    输入：
    user_IDs:用户ID，列表或者int
    item_IDs:商品ID，列表或者int
    purchasing_date:购买时间，元组
    alpha:时间衰减速率，alpha越大，则时间差对用户影响越大
    输出：
    物品相似度字典W：W[i][j]表示商品i和商品j的相似度
    '''
    # 构建用户倒排表user_set，对于同一件商品的多次购买记录，倒排表中只记录最近一次的购买行为
    # 设置user_set是全局变量
    # user_set是一个字典，key是用户名称，value是一个字典，key是物品名称，value是列表[购买时间，label]
    # 由于user_set是全局变量所以更新时可以直接调用ItemSimilarity函数，会自动更新user_set
    global user_set
    if type(user_IDs) == int:
        user_ID = user_IDs
        item_ID = item_IDs
        time = purchasing_date
        item_label = item_labels
        if user_ID not in user_set:
            user_set[user_ID] = {}
        user_set[user_ID][item_ID] = [time, item_label]

    else:
        for i in range(len(user_IDs)):
            user_ID = user_IDs[i]
            item_ID = item_IDs[i]
            time = purchasing_date[i]
            label = item_labels[i]
            if user_ID not in user_set:
                user_set[user_ID] = {}
            user_set[user_ID][item_ID] = [time, label]
    C = {}
    N = {}
    for user in user_set.keys():
        for item_i in user_set[user].keys():
            time_i, label_i = user_set[user][item_i]
            if item_i not in N.keys():
                N[item_i] = 0
                C[item_i] = {}
            N[item_i] += 1
            for item_j in user_set[user].keys():
                time_j, label_j = user_set[user][item_j]
                if item_i == item_j:
                    continue
                if item_j not in C[item_i].keys():
                    C[item_i][item_j] = 0
                C[item_i][item_j] += 1 / (1 + alpha * abs(time_i - time_j))
    W = {}
    for item_i in C.keys():
        W[item_i] = Counter()
        for item_j in C[item_i].keys():
            W[item_i][item_j] = C[item_i][item_j] / np.sqrt(N[item_i] * N[item_j])
    return W


def most_similarity_items(W, item_ID):
    '''
    根据W和item_ID生成和商品最相似的商品ID列表
    使用条件：
    当用户产生购买行为后调用。
    输入：
    相似度字典:W,由ItemSimilarity函数生成，数据类型：字典
    商品ID:目标商品ID，数据类型：int
    输出：
    商品排序列表，数据类型：list
    W = {
        a : {
            b: 0.3,
            c: 0.4
            ...
        },
        b : {

        }
    }
    '''
    print(W[item_ID])
    print(W[item_ID].most_common())
    listt = np.array(W[item_ID].most_common())  # W[a][b]: 被同时购买的概率  w[a] = [{b:0.8},{c:0.6},{e:0.3},{x:0.00000001}]
    if len(listt) == 0:  # 其实：买了x商品的人还购买了，原理：与物品x类似的物品有哪些
        return []  # 推荐：方便用户购买？ 最根本原因：希望用户消费 网站赚钱
    else:
        return listt[:, 0]


def calculate_time(t):
    '''
    时间转换函数，将datetime.datetime数值转换成以1970年1月1日开始计时，到t时刻的为止，一共经过了多少秒
    输入：
    t:当前时间,数据类型：元组
    输出：
    listt,时间转换后的列表，数据类型：列表
    '''
    listt = []
    for i in t:
        listt.append(abs((i - datetime.datetime(1970, 1, 1)).total_seconds()))
    return listt


def recommendation_1(X):
    '''
    实现购买了X商品的人还购买了什么商品算法
    输入：X，商品ID，数据结构：int
    输出：推荐列表，数据结构：list
    '''
    cur = db.cursor()
    cur.execute(
        'SELECT a.user_id, a.goods_id, c.parent_id, a.create_time FROM goodscart a INNER JOIN category c INNER JOIN buygoods b ON a.goods_id = b.id AND b.categoryid = c.id WHERE 	a.STATUS =1 ')
    results = cur.fetchall()
    data = np.array(results)
    user_IDs = data[:, 0]
    item_IDs = data[:, 1]
    item_labels = data[:, 2]
    purchasing_date = calculate_time(data[:, 3])
    item_ID = X
    recommendation_list = also_buy(user_IDs, item_IDs, item_labels, purchasing_date, item_ID, alpha=(1 / 24 / 3600))
    return recommendation_list

def popular_recommendation(initial_list):
    '''
    输入推荐列表，若推荐列表中的商品个数小于10，则随机选择商品进行填充，若大于10，则选择前10个商品
    输入：
    user_id:用户ID，数据类型：int
    initial_list:初始推荐列表，数据类型：list
    输出：
    最终推荐列表，数据类型：list
    '''
    listt = initial_list
    while len(listt) < 10:
        cur = db.cursor()
        cur.execute('select id from buygoods order by rand() limit 1 ')
        result = cur.fetchall()[0][0]
        if result not in listt:
            listt.append(result)
    listt = listt[:10]
    return listt


if __name__ == '__main__':
    goodsID = sys.argv[1]
    # goodsID = 3451023
    also_buy_recommendation(goodsID) # 根据goodsId来获取 推荐列表（和goodsId相似的商品）
    cur = db.cursor()
    cur.execute('select list from alsobuy where goodsid=3451023')
    print(cur.fetchall())
    l = [1.0,2,3,4]
    print(l)
    print(','.join([str(int(x)) for x in l]))