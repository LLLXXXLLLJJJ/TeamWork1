# -*- coding:utf-8 -*-


'''
个性化推荐方案，由三个部分组成 基于标签的推荐+基于时间上下文的itemCF的推荐+基于购买记录的推荐
直接调用personal_recommendation(userID)函数就可以实现，将个性化推荐列表存入（更新）数据库
'''
import os, sys
import numpy as np
from collections import Counter
import pymysql
import datetime
import random

user_set = {}
db = pymysql.connect('localhost', 'root', '123456', 'shop')


##########################################################################
# 基于标签的推荐
##########################################################################
def LBR(user_IDs, item_IDs, item_labels, purchasing_date, user_ID, selected_list, T, alpha=1 / 24 / 3600,
        beta=1 / 24 / 3600, initial_value=1):
    '''
    实现了基于标签推荐的函数
    输入：
    user_IDs:用户ID，数据类型：列表或者int。
    item_IDs:商品ID，数据类型：列表或者int。
    item_labels：商品对应的标签，数据类型：列表或者int
    purchasing_date:购买时间，数据类型：列表或者datetime.datetime。
    user_ID：用户ID，数据类型：int。
    selected_list:用户注册时选择的商品标签，数据类型：int或者list。
    alpha:默认为1/24/3600，时间衰减速率，alpha越大，则时间差对用户影响越大，数据类型：int 或者 float。
    beta:默认为1/24/3600，时间衰减速率，beta越大，则时间差对用户影响越大，数据类型：int 或者 float。
    initial_value：对用户注册时选择的商品标签，进行用户标签喜好初始赋值，默认值为1，数据类型：int或float。
    输出：
    推荐列表，数据类型：列表
    '''
    W = ItemSimilarity(user_IDs, item_IDs, item_labels, purchasing_date, alpha)  # W[A][B] ...
    label_rank = label_based_recommendation_rank(user_ID, selected_list, T, initial_value, beta)  # A B C
    if len(label_rank) == 0:
        return []
    else:
        label = select_label_from_label_rank(label_rank)
        return label


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
    item_labels：商品对应的标签，列表或者int。
    purchasing_date:购买时间，列表或者int
    alpha:时间衰减速率，alpha越大，则时间差对用户影响越大，int或者float
    输出：
    物品相似度字典W：W[i][j]表示商品i和商品j的相似度
    '''
    '''
        A  a  b  d
        B  a  c
        
        python 
        A : {   C[a][b] = 1  +  C[a][b] = 1  C[a][a]  C[a][b]
            a: [购买时间，分类],
            b: [购买时间，分类],
            c: [购买时间，分类],
        },
        B : {
            a: [购买时间，分类],
            b: [购买时间，分类]
        }
        
        
        C: {
            a : {
                b: 2,
                c: 1
            }
        }
        
        M : a  <--  b, c
        
        # 相似性
        C : {
            a:  {
                b: 0,
                c: 0,
                e: 0
            },
            b: {
                a: 1,
                c: 1,
                d: 1
            }
        }
        
    '''
    # 构建用户倒排表user_set，对于同一件商品的多次购买记录，倒排表中只记录最近一次的购买行为
    # 设置user_set是全局变量
    # user_set是一个字典，key是用户名称，value是一个字典，key是物品名称，value是列表[购买时间，label]
    # 由于user_set是全局变量所以更新时可以直接调用ItemSimilarity函数，会自动更新user_set
    global user_set
    if type(user_IDs) == int:    # 保证代码的健壮性
        user_ID = user_IDs
        item_ID = item_IDs
        time = int(purchasing_date)
        item_label = item_labels
        if user_ID not in user_set:
            user_set[user_ID] = {}
        user_set[user_ID][item_ID] = [time, item_label]

    else:
        for i in range(len(user_IDs)):
            user_ID = user_IDs[i]
            item_ID = item_IDs[i]
            time = int(purchasing_date[i])
            label = item_labels[i]
            if user_ID not in user_set:
                user_set[user_ID] = {}
            user_set[user_ID][item_ID] = [time, label]  # [{userid:[{item_id:[time,label]},{item_id:[time,label]}]},{userid2:[]}]
    C = {}  # 标识相似性结构体
    N = {} # N: { i:1, j: 1 } 表示的是喜欢某个的商品的人数
    for user in user_set.keys():  # [userid,userid2, ...]
        for item_i in user_set[user].keys():  # [{item_id:[time,label]},{item_id:[time,label]}]   [item_id,item_id]
            time_i, label_i = user_set[user][item_i]
            if item_i not in N.keys():
                N[item_i] = 0  # 存取的是商品的数量  T恤
                C[item_i] = {}
            N[item_i] += 1  # A { a :[] , b: []}
            for item_j in user_set[user].keys():  # [{item_id:[time,label]},{item_id:[time,label]}]  [item_id,item_id]
                time_j, label_j = user_set[user][item_j]
                if item_i == item_j:
                    continue
                if item_j not in C[item_i].keys():
                    C[item_i][item_j] = 0
                C[item_i][item_j] += 1 / (1 + alpha * abs(time_i - time_j))
    W = {}

    # N[userId][item_i] += 1   一维的维度  （x1）  sqrt(N[userId][item_i] ^2)
    #
    for item_i in C.keys():
        W[item_i] = Counter()
        for item_j in C[item_i].keys():
            W[item_i][item_j] = C[item_i][item_j] / np.sqrt(N[item_i] * N[item_j])
    return W


def label_based_recommendation_rank(user_ID, initial_list, T, initial_value, beta):
    '''
    根据用户行为和初始标签选择，计算用户对标签的偏好
    使用条件：
    进行基于label的推荐时调用
    输入：
    user_ID：用户ID，数据类型：int
    initial_list：用户初注册时选择的标签列表，数据类型：int或者list
    T:当前时间，数据类型：datetime.datetime
    initial_value：初始标签列表中元素的权重，默认为1，越大，则初始选择标签对未来影响越大，数据类型：int或者float
    beta：时间对用户标签喜好程度的影响，默认是1/24/3600，越大，则时间影响越大，数据类型：int或者float
    输出：
    一个字典：key是标签，value是用户对该标签的喜欢程度，数据类型：字典
    '''
    # 设置初始值为initial_value
    labels_rank = Counter()
    if isinstance(initial_list, int):  #initial_list 标签（分类）
        labels_rank[initial_list] += initial_value
    else:
        for i in initial_list:
            labels_rank[i] += initial_value
    if user_ID in user_set:  # user_set 是用户购买了哪些商品的一个关系表
        for item_ID in user_set[user_ID].keys():  # 我喜欢的商品
            item_label = user_set[user_ID][item_ID][1]   # 分类
            purchasing_time = user_set[user_ID][item_ID][0] # 时间
            labels_rank[item_label] += 1 / (abs(T - purchasing_time) * beta)  # label基于时间的推荐
    return labels_rank


def select_label_from_label_rank(labels_rank):
    '''
    按用户对标签的喜欢程度的比例大小，从标签中抽样，选择要推荐的标签
    使用条件：
    进行基于label的推荐时调用
    输入：
    labels_rank：一个字典，key是标签，value是用户对该标签的喜欢程度，数据类型：字典
    输出：
    label：标签，数据类型：int
    '''
    labels = list(labels_rank.keys())
    p = []  # p[] = [2,3,4]  / 3  =  1/3, 1/5, 1/10  label 用到的是 用户和商品的关系表 W 相似表
    for label in labels:
        p.append(labels_rank[label])
    p = p / np.sum(p)
    return np.random.choice(labels, 1, p=p)


def label_based_recommendation(item_list):
    '''
    根据选择的标签，输入标签下的所有商品，进行随机推荐
    使用条件：
    进行基于label的推荐时调用
    输入：
    item_list：某类标签下的商品ID列表
    输出：
    推荐商品ID的list，数据类型：list
    '''
    np.random.shuffle(item_list)
    return item_list


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


def recommendation_4(userid):
    '''
    实现基于标签推荐算法
    输入：用户ID，数据结构：int
    输出：推荐列表，数据结构：list
    '''
    '''
    1. 考虑购物车里面的数据是最全的
        下单的时候，会将购物车中的数据清空（改了一个标识位 status 0 1默认是1）
    2. 直接下单，需要将数据保存一份在购物车中（状态是 0），这是一种方式
       
    目前实现的方式：
        查询购物车表和购买表 做了inner join （查询出来 购物车中存在的并且被下过单的数据，拿出来进行分析） 数据多，没有加入购物车，直接购买的（分析：数据量比较小）
    
    以购物车为基准（所有的数据都走购物车）：数据来源于购物车（能够被分析的数据比较少）
    
    userid   goodsid   categoryid
    A         a
    A         b
    A         c
    B         a
    B         b
    C         d
    
    A  a , b, c
    B  a, b
    C  d
    '''
    cur = db.cursor()
    cur.execute(
        'select a.user_id,a.goods_id,c.parent_id,a.create_time from goodscart a inner join category c inner join buygoods b on a.goods_id = b.id and b.categoryid=c.id where a.status=1 ')
    results = cur.fetchall()
    data = np.array(results)
    user_IDs = data[:, 0]     #  用户id
    item_IDs = data[:, 1]     #  商品id
    item_labels = data[:, 2]  #  分类id
    purchasing_date = calculate_time(data[:, 3])
    user_ID = userid
    cur.execute('select categoryid from prefer where userid=%s' % user_ID)  #这个是第一次登录后，选择的标签

    results = cur.fetchall()
    selected_list = []
    for i in results:
        selected_list.append(i[0])
    T = calculate_time((datetime.datetime.now(),))[0]
    recommendation_list = LBR(user_IDs, item_IDs, item_labels, purchasing_date, user_ID, selected_list, T,
                              alpha=1 / 24 / 3600, beta=1, initial_value=1)
    listt = []
    for j in range(10):
        for i in recommendation_list:
            parent_id = i
            cur.execute(
                f'select a.id from buygoods a inner join category b on a.categoryid = b.id where b.parent_id={parent_id} order by rand() limit 1')
            results = cur.fetchall()[0][0]
            listt.append(results)
    return listt  # A  B  C  将这些商品做一个排序   W[A][B] W[A][C] W[B][C]


##########################################################################################
# 基于时间上下文的itemCF的推荐
##########################################################################################
# 最新商品 --> 这些商品并没有被购买过 基于时间的
def recommendation_3(userid):
    '''
    实现购买了基于上下文的itemCF算法
    输入：
    用户ID，数据结构：int
    输出：
    推荐列表，数据结构：list
    '''
    cur = db.cursor()
    cur.execute(
        'select a.user_id,a.goods_id,c.parent_id,a.create_time from goodscart a inner join category c inner join buygoods b on a.goods_id = b.id and b.categoryid=c.id where a.status=1 ')
    results = cur.fetchall()
    if results == ():
        return []
    else:
        data = np.array(results)
        user_IDs = data[:, 0]
        item_IDs = data[:, 1]
        item_labels = data[:, 2]
        purchasing_date = calculate_time(data[:, 3])
        user_id = userid
        cur.execute(f'select goods_id from goodscart where status=1 and user_id={user_id} ')
        results = cur.fetchall()
        purchased_list = []
        for i in results:
            purchased_list.append(i[0])
        rec_list = TICF(user_IDs, item_IDs, item_labels, purchasing_date, purchased_list)
        return rec_list


def time_based_item_CF(purchased_list, W):
    '''
    根据物品相似度矩阵，进行推荐
    使用条件：
    生成基于时间上下文的itemCF推荐
    输入：
    purchased_list：用户购买历史，数据类型：list
    W：商品相似度字典，数据类型
    输出：
    推荐商品ID列表，数据类型：list
    '''
    # 已有了相似度矩阵，对用户A，只需要计算所有商品和A用户的所有商品的相似度之和
    '''
    张三： a , b , c 
    W: {
        a : {
            b: [],
            c: [],
            d: 0.5
        },
        b : {
            a: [],
            c: [],
            e: []
        },
        c : {
            d: 0.1,
            e: 0.4
        }
    }
    
    d : 0.6, e : 0.7
    
    e: 0.7, d: 0.6
    
    [: ,0]
    
    A --> 爱情a 
    C --> 爱情a , 爱情b
    
    '''

    rank = Counter()
    if isinstance(purchased_list, float):
        purchased_list = [purchased_list]
    for i in W.keys():
        if i not in purchased_list:
            for j in purchased_list:
                rank[i] += W[i][j]  # 没有业务  W[j][i] == W[i][j]
    if rank.most_common() == []:
        return []
    else:
        return np.array(rank.most_common())[:, 0]

# purchased_list 是购物车的商品
def TICF(user_IDs, item_IDs, item_labels, purchasing_date, purchased_list, alpha=1 / 24 / 3600):
    '''
    实现了基于时间上下文的itemCF算法
    输入：
    user_IDs:用户ID，数据类型：列表或者int
    item_IDs:商品ID，数据类型：列表或者int
    purchasing_date:购买时间，数据类型：列表或者float,int
    alpha，时间衰减速率，alpha越大，则时间差对用户影响越大，数据类型：int 或者 float
    purchased_list:用户已购买的商品ID，数据类型：int或者list
    输出：
    推荐列表，数据类型：列表
    '''

    '''
    W:物品与物品的相似矩阵
    用户喜爱：数码产品--> categoryId --> 所有商品随机找出前K个商品 --> 找与这些商品相似的商品 进行推荐
    
    W：物品与物品的相似矩阵
    查询自己的订单（自己购买的商品） --> 根据 购买商品 找到与购买商品相似的商品 --> 精排 --> 进行推荐
    '''
    if purchased_list:
        W = ItemSimilarity(user_IDs, item_IDs, item_labels, purchasing_date, alpha)
        TICF_list = time_based_item_CF(purchased_list, W)
    else:
        TICF_list = []
    return TICF_list


##########################################################################
# 基于上次搜索的推荐
##########################################################################
def search_goods(X):
    '''
    实现用户X基于搜索的推荐函数
    输入：
    X；用户ID，数据类型：int
    输出：
    results：输出列表
    '''

    cur = db.cursor()
    cur.execute(
        'select a.user_id,a.goods_id,c.parent_id,a.create_time from goodscart a inner join category c inner join buygoods b on a.goods_id = b.id and b.categoryid=c.id where a.status=1 ')
    results = cur.fetchall()
    data = np.array(results)
    user_IDs = data[:, 0]
    item_IDs = data[:, 1]
    item_labels = data[:, 2]
    purchasing_date = calculate_time(data[:, 3])

    # 物品与物品的相似关系
    W = ItemSimilarity(user_IDs, item_IDs, item_labels, purchasing_date, alpha=1 / 24 / 3600)

    cur = db.cursor()
    cur.execute(f'select lastsearch from login where id={X}')
    results = cur.fetchall()
    # 若无查询记录，直接返回空tuple
    if results == ():
        return []
    else:
        results = results[0][0]
        cur.execute(f'select id from buygoods where goodsname like "%{results}%"')  # 建模 基于 用户A  购买a b   a, b一定要在搜索结果中存在  a, d, f
        results = cur.fetchall()             # 根据购物车里面的商品 找到所有可以被推荐的商品  Counter()  a, b, c, d, e, f, g   a, d, f
        purchased_list = np.array(results)[:, 0]
        rank = Counter()
        for i in W.keys():
            if i in purchased_list:
                for j in purchased_list:
                    if i == j:
                        continue
                    rank[i] += W[i][j]  # 物品与物品的相似关系  并没有涵盖所有的物品
        if rank.most_common() == []:
            return purchased_list
        else:
            return np.array(rank.most_common())[:, 0]


def recommendation_2(X):
    '''
    实现基于用户搜索的推荐
    输入：X，用户ID，数据结构：int
    输出：推荐列表，数据结构：list

    思路：
    1. 查询购物车表（根据 x 获取搜索的值 lastsearch，来进行查询购物车表找到可以进行相似对比的商品）
    2. 使用基于物品的推荐算法进行推荐 W:相似矩阵 描述了商品与商品之间的相似关系
    3. 提出问题?
        如何去寻找对比商品：建模这个是非常重要的   拔高：要求大家在学会一般推荐算法 我能不能自己建模完成推荐功能
    '''
    results = search_goods(X)
    listt = []
    for i in results:
        listt.append(int(i))   # 设计复杂：从购物车表 查询（result搜索记录） 找到一些商品，参照物：购物车中物品
    random.shuffle(listt)          # 根据这些物品 进行 基于物品的协同过滤算法，可以找到 和购物车中物品相似的商品，将这些商品取出 推荐给你
    return listt


#####################################################################
# 过滤重排
#####################################################################

def personnal_recommendation_list(user_id, initial_list):
    '''
    对于给定好的推荐列表，进行过滤重排，过滤出用户已经购买过的商品，对剩下的商品进行随机排列，输出前10个。
    输入:
    user_id:用户ID，数据类型，int
    initial_list:未过滤的初始列表，数据类型，list
    输出：
    过滤重排后的列表，数据类型，list
    '''
    listt = []
    cur = db.cursor()
    cur.execute(f'select goods_id from goodscart where status=1 and user_id={user_id}')
    results = cur.fetchall()
    if results == ():
        purchased_list = []
    else:
        purchased_list = np.array(results)
    for i in (initial_list):
        if i not in results:
            listt.append(i)
    listt = list(set(listt))
    if len(listt) < 10:
        t = 10 - len(listt)
        for i in range(t):
            cur.execute('select id from buygoods order by rand() limit 1 ')
            result = cur.fetchall()[0][0]
            if result in purchased_list:
                continue
            if result in listt:
                continue
            listt.append(result)
    else:
        np.random.shuffle(listt)
        listt = listt[:10]
    return listt

# 最终函数
def personal_recommendation(userID):
    '''
    实现一个函数，输入用户ID，自动计算用户个性化推荐列表，并且以json(array)格式存入（更新）到数据库中
    输入：用户ID，int

    张三： a , b 就过滤 a,b 并找到物品a和物品b相似的商品
      c: 0.5, d:0.3, e:0.2
      c: 0.7, d:0.8

      -----------------------
      c 、d 、e 有顺序的

    W = {
        a : {
            b: 1,
            c: 0.5,
            d:0.3,
            e:0.2
        },
        b: {
            a: 1,
            c: 0.7,
            d: 0.8
        }
    }

    '''
    list4 = recommendation_4(userID)  # 基于标签的推荐算法
    # list3 = recommendation_3(userID)  # 基于时间  购买商品的时间
    # list2 = recommendation_2(userID)  # 基于搜索
    # listt = personnal_recommendation_list(userID, list(list2) + list(list3) + list(list4))
    # listt = personal_recommendation(list4)
    listt = list4
    personal_list = f"{listt}"
    # print(listt)  [1,2,3]  1,2,3
    cur = db.cursor()
    if cur.execute(f'select * from recommendation where userid={userID}'):
       cur.execute(f'update recommendation set list="{listt}",createtime=now() where userid={userID}')
    else:
       cur.execute(f'insert into recommendation (userid,list,createtime) value ({userID},"{listt}",now())')
    db.commit()


if __name__ == '__main__':
    # l = len(sys.argv)
    # print(l)
    # a1 = sys.argv[0]
    # print(a1)
    userid = sys.argv[1]  # 弱类型  10001.0  double float
    # print(int(userid))

    # userid = 11
    userid = int(userid)
    personal_recommendation(userid)
    list3 = recommendation_3(1)
    print(list3)  # 商品
    cur = db.cursor()
    cur.execute(f'select list from recommendation where userid = 11 ')
    print(cur.fetchall())

"""
W 物品与物品的相似矩阵

用户u  购买了  i, j, ... , k
i 相似矩阵中取到和i商品相似的商品    a(), b(), c() ,d()
j                               a()， i
.
.
.
k

a (a+a) 新的相似值

a(评分就会加起来) , b, c, d  有新的排序规则    a, b, c, d ... a10
"""