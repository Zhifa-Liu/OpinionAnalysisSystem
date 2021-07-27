import multiprocessing
import re
import threading

import traceback
from urllib.parse import urlencode

# import requests
import json
import random

import requests
from pyquery import PyQuery as pq
import time
import datetime
from goto import with_goto
from threadpool import makeRequests, ThreadPool
from tqdm import tqdm

from include.weibo.proxy import get_random_ip, get_ip_list

from util import kafka_util
import ssl
requests.adapters.DEFAULT_RETRIES = 5
requests.packages.urllib3.disable_warnings()
# 设置连接活跃状态为False
requests = requests.session()
requests.keep_alive = False
ssl._create_default_https_context = ssl._create_unverified_context
base_url = 'https://m.weibo.cn/api/container/getIndex?'
list1 = [
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.101 Safari/537.36 Edg/91.0.864.48",
    "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36",
    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0",
    "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Win64; x64; Trident/6.0)",
    'Mozilla/5.0 (Windows; U; Windows NT 5.1; it; rv:1.8.1.11) Gecko/20071127 Firefox/2.0.0.11',
    'Opera/9.25 (Windows NT 5.1; U; en)',
    'Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)',
    'Mozilla/5.0 (compatible; Konqueror/3.5; Linux) KHTML/3.5.5 (like Gecko) (Kubuntu)',
    'Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.0.12) Gecko/20070731 Ubuntu/dapper-security Firefox/1.5.0.12',
    'Lynx/2.8.5rel.1 libwww-FM/2.14 SSL-MM/1.4.1 GNUTLS/1.2.9',
    "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:10.0) Gecko/20100101 Firefox/10.0 "
]


# proxies = {}
urls = [
    'https://www.89ip.cn/index_1.html',
    'https://www.89ip.cn/index_2.html',
    'https://www.89ip.cn/index_3.html',
    'https://www.89ip.cn/index_4.html',
    'https://www.89ip.cn/index_5.html',
    'https://www.89ip.cn/index_6.html',
    'https://www.89ip.cn/index_7.html',
    'https://www.89ip.cn/index_8.html',
    'https://www.89ip.cn/index_9.html',
    'https://www.89ip.cn/index_10.html',
    'https://www.89ip.cn/index_11.html',
    'https://www.89ip.cn/index_12.html',
    'https://www.89ip.cn/index_13.html',
    'https://www.89ip.cn/index_14.html',
    'https://www.89ip.cn/index_15.html',
    'https://www.89ip.cn/index_16.html',
    'https://www.89ip.cn/index_17.html',
    'https://www.89ip.cn/index_18.html',
    'https://www.89ip.cn/index_19.html',
    'https://www.89ip.cn/index_20.html',
    'http://www.ip3366.net/?stype=1&page=1',
    'http://www.ip3366.net/?stype=1&page=2',
    'http://www.ip3366.net/?stype=1&page=3',
    'http://www.ip3366.net/?stype=1&page=4',
    'http://www.ip3366.net/?stype=1&page=5',
    'http://www.ip3366.net/?stype=1&page=6',
    'http://www.ip3366.net/?stype=1&page=7',
    'http://www.ip3366.net/?stype=1&page=8',
    'http://www.ip3366.net/?stype=1&page=9',
    'http://www.ip3366.net/?stype=1&page=10',
    'https://www.kuaidaili.com/free/inha/1/',
    'https://www.kuaidaili.com/free/inha/2/',
    'https://www.kuaidaili.com/free/inha/3/',
    'https://www.kuaidaili.com/free/inha/4/',
    'https://www.kuaidaili.com/free/inha/5/',
    'https://www.kuaidaili.com/free/inha/6/',
    'https://www.kuaidaili.com/free/inha/7/',
    'https://www.kuaidaili.com/free/inha/8/',
    'https://www.kuaidaili.com/free/inha/9/',
    'https://www.kuaidaili.com/free/inha/10/',
    'https://www.7yip.cn/free/?action=china&page=1',
    'https://www.7yip.cn/free/?action=china&page=2',
    'https://www.7yip.cn/free/?action=china&page=3',
    'https://www.7yip.cn/free/?action=china&page=4',
    'https://www.7yip.cn/free/?action=china&page=5',
    'https://www.7yip.cn/free/?action=china&page=6',
    'https://www.7yip.cn/free/?action=china&page=7',
    'https://www.7yip.cn/free/?action=china&page=8',
    'https://www.7yip.cn/free/?action=china&page=9',
    'https://www.7yip.cn/free/?action=china&page=10',
       ]


list2 = [
    ['102803_ctg1_4188_-_ctg1_4188?from=faxian_hot&mod=fenlei','社会'],
    ['102803_ctg1_4288_-_ctg1_4288?from=faxian_hot&mod=fenlei','明星'],
    ['102803_ctg1_1388_-_ctg1_1388?from=faxian_hot&mod=fenlei','体育'],
    ['102803_ctg1_5788_-_ctg1_5788?from=faxian_hot&mod=fenlei','政务']
]



#获取网页的json
@with_goto
def get_page(list, since_id=None):
    print(since_id)
    if since_id == None:
        para = {
            'containerid': list[0]
        }
    else:
        para = {
            'containerid': list[0],
            'since_id': since_id
        }
    url = base_url + urlencode(para)
    # print(url)
    label.page1
    try:
        # host用于指定internet主机和端口号，http1.1必须包含，不然系统返回400，
        head = random.choice(list1)
        print(head)
        headers = {
            'Referer': 'https://m.weibo.cn',
            'User-Agent': head,
            # 'Cookie': 'SCF=Ajql1mQRxs50tXTYPERywh1-diUzc2uAEuoglbVmaiXFt3gQAomQmIk9jcmhnkjRp3kn-KgyYplgj1RBleSMRK8.; SUB=_2A25NxPFJDeRhGeNI6FoY9CjNwzyIHXVvRp8BrDV6PUJbktAKLUaskW1NSGOz-BCmW_LjsKdBkVm2PgMIldR05ogv; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5kH7G5AEKa9fqaPPs-rMI85NHD95QfSoeR1KBceKn7Ws4Dqcjwi--Ni-2pi-8Fi--4iKnfi-zRi--4iK.XiKyh--8x',
            'Host': 'm.weibo.cn',
            # 'Connection': 'close',
            'X-Requested-With': 'XMLHttpRequest'
        }
        global proxies
        proxies1 = get_random_ip(proxies)
        print(proxies1)
        response = requests.get(url, headers=headers, proxies=proxies1, verify=False, timeout=30)
        print('response')
        print(response)
        if response.status_code != 200:
            print("状态码有误，微博主页获取失败，休眠重启中.......")
            # response.close()
            time.sleep(60)
            goto.page1
        res = response.json()
        time.sleep(random.uniform(0.5, 1.5))
        # print(res)
        res['field'] = list[1]
        items = res.get('data').get('cardlistInfo')
        next_since_id = items['since_id']
        # print(res)
        parse_json(res)

        return str(next_since_id)
    except Exception as e:
        traceback.print_exc()
        print("微博主页获取失败，休眠重启中.......")
        # response.close()
        time.sleep(60)
        goto.page1


#正则过滤字符
def filter(str):
    str = re.sub('[a-zA-Z0-9’!"$%&\'()*+,-./:;<=>?@，。?★、…【】🖤《》？“”‘’！[\\]^_`{|}~\s]+', "", str)
    str = re.sub(
        '[\001\002\003\004\005\006\007\x08\x09\x0a\x0b\x0c\x0d\x0e\x0f\x10\x11\x12\x13\x14\x15\x16\x17\x18\x19\x1a]+',
        '', str)
    return str
def filter_text(str):
    str = re.sub('\n', ",", str)
    return str


class Tool:
    deleteImg = re.compile('<img.*?>')
    newLine = re.compile('<tr>|<div>|</tr>|</div>')
    deleteAite = re.compile('//.*?:')
    deleteAddr = re.compile('<a.*?>.*?</a>')
    deleteTag = re.compile('<.*?>')

    @classmethod
    def replace(cls, x):
        x = re.sub(cls.deleteImg, '', x)
        x = re.sub(cls.deleteAite, '', x)
        x = re.sub(cls.deleteAddr, '', x)
        x = re.sub(cls.newLine, '', x)
        x = re.sub(cls.deleteTag, '', x)
        return x.strip()


#分析JSON格式的数据，抓取目标信息
@with_goto
def parse_json(data):
    if data:
        # print(data.get('data'))
        items = data.get('data').get('cards')
        for item in items:
            item = item.get('mblog')
            # print(item)
            #有的card没有mblog
            if(item):
                weibo = {}
                #抓取信息
                if(item.get('user')):
                    weibo['field'] = pq(data.get('field')).text()
                    weibo['user_name'] = pq(item.get('user').get('screen_name')).text()
                    weibo['user_id'] = item.get('user').get('id')
                    if item.get('user').get('verified_type') == 0:
                        weibo['user_type'] = '网民'
                    else:
                        weibo['user_type'] = '媒体'
                    weibo['gender'] = pq(item.get('user').get('gender')).text()
                    #获取用户详细信息
                    profile_url=item.get('user').get('profile_url')
                    # print(profile_url)
                    weibo['location'] = get_user_location(profile_url)
                    weibo['fans_count'] = item.get('user').get('followers_count')
                    # if(item.get('user').get('verified_reason')):
                    #     weibo['user_type'] = pq(item.get('user').get('verified_reason')).text()
                weibo['blog_id'] = pq(item.get('id')).text()
                # if (item.get('retweeted_status')):
                #     if item.get('pid'):
                #         weibo['source_blog_id'] = item.get('pid')
                #     else:
                #         weibo['source_blog_id'] = pq(item.get('retweeted_status').get('id')).text()
                if(item.get('page_info')):
                    weibo['theme'] = pq(item.get('page_info').get('page_title')).text()
                weibo['create_date'] = pq(item.get('created_at')).text()
                label.json1
                # host用于指定internet主机和端口号，http1.1必须包含，不然系统返回400，
                head = random.choice(list1)
                headers = {
                    'Referer': 'https://m.weibo.cn',
                    'User-Agent': head,
                    # 'Cookie': 'SCF=Ajql1mQRxs50tXTYPERywh1-diUzc2uAEuoglbVmaiXFt3gQAomQmIk9jcmhnkjRp3kn-KgyYplgj1RBleSMRK8.; SUB=_2A25NxPFJDeRhGeNI6FoY9CjNwzyIHXVvRp8BrDV6PUJbktAKLUaskW1NSGOz-BCmW_LjsKdBkVm2PgMIldR05ogv; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5kH7G5AEKa9fqaPPs-rMI85NHD95QfSoeR1KBceKn7Ws4Dqcjwi--Ni-2pi-8Fi--4iKnfi-zRi--4iK.XiKyh--8x',
                    'Host': 'm.weibo.cn',
                    # 'Connection': 'close',
                    'X-Requested-With': 'XMLHttpRequest'
                }
                flag1 = 0
                global proxies
                proxies1 = get_random_ip(proxies)
                # print(proxies1)
                try:
                    test_response = requests.get(
                    'https://m.weibo.cn/statuses/extend?id=' + weibo['blog_id'],
                    headers=headers, proxies=proxies1, verify=False, timeout=30)
                    test = test_response.json()
                    if test_response.status_code != 200:
                        print("状态码有误，微博文本页获取失败，休眠重启中.......")
                        # test_response.close()
                        time.sleep(60)
                        goto.json1
                    time.sleep(random.uniform(0.5, 1.5))
                except Exception as e:
                    traceback.print_exc()
                    print("微博文本页获取失败，休眠重启中.......")
                    # test_response.close()
                    time.sleep(60)
                    goto.json1
                if test:
                    if test.get('data'):
                        if test.get('data').get('longTextContent'):
                            weibo['text'] = filter_text(pq(test.get('data').get('longTextContent')).text())
                            print(weibo['text'])
                        else:
                            weibo['text'] = filter_text(pq(item.get('text')).text())
                            print(weibo['text'])
                    else:
                        weibo['text'] = filter_text(pq(item.get('text')).text())
                        print(weibo['text'])
                else:
                    weibo['text'] = filter_text(pq(item.get('text')).text())
                    print(weibo['text'])

                #主题文章获取
                topics = re.findall(r"#(.+?)#", weibo['text'])
                print(topics)
                if topics:
                    for topic in topics:
                        label.topic
                        try:
                            url = 'https://m.weibo.cn/api/container/getIndex?containerid=231522type%3D1%26q%3D%23' + topic + '%23&page_type=searchall'
                            print(url)
                            # host用于指定internet主机和端口号，http1.1必须包含，不然系统返回400，
                            head = random.choice(list1)
                            headers = {
                                'Referer': 'https://m.weibo.cn',
                                'User-Agent': head,
                                # 'Cookie': 'SCF=Ajql1mQRxs50tXTYPERywh1-diUzc2uAEuoglbVmaiXFt3gQAomQmIk9jcmhnkjRp3kn-KgyYplgj1RBleSMRK8.; SUB=_2A25NxPFJDeRhGeNI6FoY9CjNwzyIHXVvRp8BrDV6PUJbktAKLUaskW1NSGOz-BCmW_LjsKdBkVm2PgMIldR05ogv; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5kH7G5AEKa9fqaPPs-rMI85NHD95QfSoeR1KBceKn7Ws4Dqcjwi--Ni-2pi-8Fi--4iKnfi-zRi--4iK.XiKyh--8x',
                                'Host': 'm.weibo.cn',
                                # 'Connection': 'close',
                                'X-Requested-With': 'XMLHttpRequest'
                            }
                            # global proxies
                            proxies1 = get_random_ip(proxies)
                            response_topic = requests.get(url, headers=headers, proxies=proxies1, verify=False, timeout=30)
                            print('response_topic')
                            print(response_topic)
                            if response_topic.status_code != 200:
                                print("状态码有误，微博主题文章页获取失败，休眠重启中.......")
                                # response_topic.close()
                                time.sleep(60)
                                goto.topic
                            res = response_topic.json()
                            time.sleep(random.uniform(0.5, 1.5))
                            if res:
                                if res.get('ok') == 1:
                                    items2 = res.get('data').get('cards')
                                    for item2 in items2:
                                        if item2.get('card_type') == 9:
                                            print('数据存在 数据存在 数据存在 ')
                                            print('topic:'+topic)
                                            print('field:' + weibo.get('field'))
                                            item2 = item2.get('mblog')
                                            # print(item)
                                            # 有的card没有mblog
                                            if item2:
                                                weibo_topic = {}
                                                # 抓取信息
                                                if item2.get('user'):
                                                    weibo_topic['field'] = pq(weibo.get('field')).text()
                                                    weibo_topic['user_name'] = pq(item2.get('user').get('screen_name')).text()
                                                    weibo_topic['user_id'] = item2.get('user').get('id')
                                                    if item2.get('user').get('verified_type') == 0:
                                                        weibo_topic['user_type'] = '网民'
                                                    else:
                                                        weibo_topic['user_type'] = '媒体'
                                                    weibo_topic['gender'] = pq(item2.get('user').get('gender')).text()
                                                    # 获取用户详细信息
                                                    profile_url = item2.get('user').get('profile_url')
                                                    # print(profile_url)
                                                    weibo_topic['location'] = get_user_location(profile_url)
                                                    weibo_topic['fans_count'] = item2.get('user').get('followers_count')
                                                weibo_topic['blog_id'] = pq(item2.get('id')).text()
                                                # if (item2.get('retweeted_status')):
                                                #     if item2.get('pid'):
                                                #         weibo['source_blog_id'] = item2.get('pid')
                                                #     else:
                                                #         weibo['source_blog_id'] = pq(item2.get('retweeted_status').get('id')).text()
                                                if (item2.get('page_info')):
                                                    weibo_topic['theme'] = '无'
                                                weibo_topic['create_date'] = pq(item2.get('created_at')).text()
                                                label.json_topic
                                                # host用于指定internet主机和端口号，http1.1必须包含，不然系统返回400，
                                                head = random.choice(list1)
                                                headers = {
                                                    'Referer': 'https://m.weibo.cn',
                                                    'User-Agent': head,
                                                    # 'Cookie': 'SCF=Ajql1mQRxs50tXTYPERywh1-diUzc2uAEuoglbVmaiXFt3gQAomQmIk9jcmhnkjRp3kn-KgyYplgj1RBleSMRK8.; SUB=_2A25NxPFJDeRhGeNI6FoY9CjNwzyIHXVvRp8BrDV6PUJbktAKLUaskW1NSGOz-BCmW_LjsKdBkVm2PgMIldR05ogv; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5kH7G5AEKa9fqaPPs-rMI85NHD95QfSoeR1KBceKn7Ws4Dqcjwi--Ni-2pi-8Fi--4iKnfi-zRi--4iK.XiKyh--8x',
                                                    'Host': 'm.weibo.cn',
                                                    # 'Connection': 'close',
                                                    'X-Requested-With': 'XMLHttpRequest'
                                                }
                                                flag1 = 0
                                                # global proxies
                                                proxies1 = get_random_ip(proxies)
                                                # print(proxies1)
                                                try:
                                                    test_response = requests.get(
                                                        'https://m.weibo.cn/statuses/extend?id=' + weibo_topic['blog_id'],
                                                        headers=headers, proxies=proxies1, verify=False, timeout=30)
                                                    test = test_response.json()
                                                    if test_response.status_code != 200:
                                                        print("状态码有误，微博文本页获取失败，休眠重启中.......")
                                                        # test_response.close()
                                                        time.sleep(60)
                                                        goto.json_topic
                                                    time.sleep(random.uniform(0.5, 1.5))
                                                except Exception as e:
                                                    traceback.print_exc()
                                                    print("微博文本页获取失败，休眠重启中.......")
                                                    # test_response.close()
                                                    time.sleep(60)
                                                    goto.json_topic
                                                if test:
                                                    if test.get('data'):
                                                        if test.get('data').get('longTextContent'):
                                                            weibo_topic['text'] = filter_text(
                                                                pq(test.get('data').get('longTextContent')).text())
                                                            print(weibo_topic['text'])
                                                        else:
                                                            weibo_topic['text'] = filter_text(pq(item2.get('text')).text())
                                                            print(weibo_topic['text'])
                                                    else:
                                                        weibo_topic['text'] = filter_text(pq(item2.get('text')).text())
                                                        print(weibo_topic['text'])
                                                else:
                                                    weibo_topic['text'] = filter_text(pq(item2.get('text')).text())
                                                    print(weibo_topic['text'])

                                                weibo_topic['attitudes_count'] = item2.get('attitudes_count')
                                                weibo_topic['comments_count'] = item2.get('comments_count')
                                                weibo_topic['reports_count'] = item2.get('reposts_count')
                                                weibo_topic['get_time'] = str(time.asctime())
                                                array1 = []
                                                url = 'https://m.weibo.cn/comments/hotflow?id=' + weibo_topic[
                                                    'blog_id'] + '&mid=' + weibo_topic[
                                                          'blog_id'] + '&max_id_type=0'
                                                label.json_topic2
                                                head2 = random.choice(list1)
                                                headers2 = {
                                                    'Referer': 'https://m.weibo.cn',
                                                    'User-Agent': head2,
                                                    # 'Cookie': 'SCF=Ajql1mQRxs50tXTYPERywh1-diUzc2uAEuoglbVmaiXFt3gQAomQmIk9jcmhnkjRp3kn-KgyYplgj1RBleSMRK8.; SUB=_2A25NxPFJDeRhGeNI6FoY9CjNwzyIHXVvRp8BrDV6PUJbktAKLUaskW1NSGOz-BCmW_LjsKdBkVm2PgMIldR05ogv; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5kH7G5AEKa9fqaPPs-rMI85NHD95QfSoeR1KBceKn7Ws4Dqcjwi--Ni-2pi-8Fi--4iKnfi-zRi--4iK.XiKyh--8x',
                                                    'Host': 'm.weibo.cn',
                                                    # 'Connection': 'close',
                                                    'X-Requested-With': 'XMLHttpRequest'
                                                }
                                                flag2 = 0
                                                # label.json2
                                                proxies2 = get_random_ip(proxies)
                                                try:
                                                    data2_response = requests.get(
                                                        url, headers=headers2,
                                                        proxies=proxies2, verify=False, timeout=30)
                                                    print(data2_response)
                                                    data2 = data2_response.json()
                                                    if data2_response.status_code != 200:
                                                        print("状态码有误，微博主题评论页获取失败，休眠重启中.......")
                                                        # data2_response.close()
                                                        time.sleep(60)
                                                        goto.json_topic2
                                                    time.sleep(random.uniform(0.5, 1.5))
                                                except Exception as e:
                                                    traceback.print_exc()
                                                    print("微博主题评论页获取失败，休眠重启中.......")
                                                    # data2_response.close()
                                                    time.sleep(60)
                                                    goto.json_topic2

                                                if data2.get('ok') == 1:
                                                    if data2:
                                                        if data2.get('data'):
                                                            if data2.get('data').get('data'):
                                                                comments = data2.get('data').get('data')
                                                                # print(str(comments).encode("gbk", "ignore").decode("gbk"))
                                                                for comment2 in comments:
                                                                    detail = {}
                                                                    detail['comment_id'] = comment2.get('id');
                                                                    detail['commenter_id'] = comment2.get('user').get(
                                                                        'id')
                                                                    detail['commenter_name'] = pq(
                                                                        comment2.get('user').get('screen_name')).text()
                                                                    if comment2.get('user').get('gender'):
                                                                        detail['commenter_gender'] = pq(
                                                                            comment2.get('user').get('gender')).text()
                                                                    profile_url = comment2.get('user').get('profile_url')
                                                                    detail['comment_text'] = filter(
                                                                        pq(comment2.get('text')).text())
                                                                    detail['comment_reply'] = comment2.get(
                                                                        'total_number')
                                                                    detail['comment_like'] = comment2.get('like_count')
                                                                    print(str(detail).encode("gbk", "ignore").decode(
                                                                        "gbk"))
                                                                    array1.append(detail)
                                                    weibo_topic['comments'] = array1
                                                    # 保存一条数据
                                                    print('##############################')
                                                    print('##############################')
                                                    print(str(weibo_topic).encode("gbk", "ignore").decode("gbk"))
                                                    print('##############################')
                                                    print('##############################')
                                                    save_data(weibo_topic)
                        except Exception as e:
                            traceback.print_exc()
                            print("微博主页获取失败，休眠重启中.......")
                            # response_topic.close()
                            time.sleep(60)
                            goto.topic
                weibo['attitudes_count'] = item.get('attitudes_count')
                weibo['comments_count'] = item.get('comments_count')
                weibo['reports_count'] = item.get('reposts_count')
                weibo['get_time'] = str(time.asctime())
                # test=requests.get('https://m.weibo.cn/api/comments/show?id=H6AxBo0B8&page=0',headers=headers).text
                # print(test)
                count=0
                # max_id = ''
                # tmp = 'liuchangxin'
                array = []
                url = 'https://m.weibo.cn/comments/hotflow?id=' + weibo['blog_id'] + '&mid=' + weibo[
                    'blog_id'] + '&max_id_type=0'
                label.json2
                head2 = random.choice(list1)
                headers2 = {
                    'Referer': 'https://m.weibo.cn',
                    'User-Agent': head2,
                    # 'Cookie': 'SCF=Ajql1mQRxs50tXTYPERywh1-diUzc2uAEuoglbVmaiXFt3gQAomQmIk9jcmhnkjRp3kn-KgyYplgj1RBleSMRK8.; SUB=_2A25NxPFJDeRhGeNI6FoY9CjNwzyIHXVvRp8BrDV6PUJbktAKLUaskW1NSGOz-BCmW_LjsKdBkVm2PgMIldR05ogv; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5kH7G5AEKa9fqaPPs-rMI85NHD95QfSoeR1KBceKn7Ws4Dqcjwi--Ni-2pi-8Fi--4iKnfi-zRi--4iK.XiKyh--8x',
                    'Host': 'm.weibo.cn',
                    # 'Connection': 'close',
                    'X-Requested-With': 'XMLHttpRequest'
                }
                flag2 = 0
                # label.json2
                proxies2 = get_random_ip(proxies)
                try:
                    data2_response = requests.get(
                        url, headers=headers2,
                        proxies=proxies2, verify=False, timeout=30)
                    print(data2_response)
                    data2 = data2_response.json()
                    if data2_response.status_code != 200:
                        print("状态码有误，微博评论页获取失败，休眠重启中.......")
                        # data2_response.close()
                        time.sleep(60)
                        goto.json2
                    # print('data2')
                    # print(data2)
                    time.sleep(random.uniform(0.5, 1.5))
                except Exception as e:
                    traceback.print_exc()
                    print("微博评论页获取失败，休眠重启中.......")
                    # data2_response.close()
                    time.sleep(60)
                    goto.json2

                if data2.get('ok') == 1:
                    # if data2:
                    #     if data2.get('data'):
                    #         if max_id == str(data2.get('data').get('max_id')):
                    #             break
                    #         else:
                    # tmp = max_id
                    # print(data2.get('data'))
                    # max_id = str(data2.get('data').get('max_id'))
                    if data2:
                        if data2.get('data'):
                            if data2.get('data').get('data'):
                                comments = data2.get('data').get('data')
                                # print(str(comments).encode("gbk", "ignore").decode("gbk"))
                                for comment in comments:
                                    detail = {}
                                    detail['comment_id'] = comment.get('id');
                                    detail['commenter_id'] = comment.get('user').get('id')
                                    detail['commenter_name'] = pq(comment.get('user').get('screen_name')).text()
                                    if comment.get('user').get('gender'):
                                        detail['commenter_gender'] = pq(comment.get('user').get('gender')).text()
                                    profile_url = comment.get('user').get('profile_url')
                                    # detail['commenter_location'] = get_user_location(profile_url)
                                    detail['comment_text'] = filter(pq(comment.get('text')).text())
                                    detail['comment_reply'] = comment.get('total_number')
                                    detail['comment_like'] = comment.get('like_count')
                                    print(str(detail).encode("gbk", "ignore").decode("gbk"))
                                    array.append(detail)


                    weibo['comments'] = array
                    #保存一条数据
                    print('&&&&&&&&&&&&&&&&&&&&&&&&&&')
                    print('&&&&&&&&&&&&&&&&&&&&&&&&&&')
                    print(str(weibo).encode("gbk", "ignore").decode("gbk"))
                    print('&&&&&&&&&&&&&&&&&&&&&&&&&&')
                    print('&&&&&&&&&&&&&&&&&&&&&&&&&&')
                    save_data(weibo)
                    # print(weibo)
                    # 一个一个返回weibo
                else:
                    weibo['comments'] = array
                    # 保存一条数据
                    print('&&&&&&&&&&&&&&&&&&&&&&&&&&')
                    print('&&&&&&&&&&&&&&&&&&&&&&&&&&')
                    print(str(weibo).encode("gbk", "ignore").decode("gbk"))
                    print('&&&&&&&&&&&&&&&&&&&&&&&&&&')
                    print('&&&&&&&&&&&&&&&&&&&&&&&&&&')
                    save_data(weibo)
    return

@with_goto
def get_containerid(id):
    """
    获取用户的containerid
    """
    label.containerid1
    head = random.choice(list1)
    headers = {
        'Referer': 'https://m.weibo.cn',
        'User-Agent': head,
        # 'Cookie': 'SCF=Ajql1mQRxs50tXTYPERywh1-diUzc2uAEuoglbVmaiXFt3gQAomQmIk9jcmhnkjRp3kn-KgyYplgj1RBleSMRK8.; SUB=_2A25NxPFJDeRhGeNI6FoY9CjNwzyIHXVvRp8BrDV6PUJbktAKLUaskW1NSGOz-BCmW_LjsKdBkVm2PgMIldR05ogv; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5kH7G5AEKa9fqaPPs-rMI85NHD95QfSoeR1KBceKn7Ws4Dqcjwi--Ni-2pi-8Fi--4iKnfi-zRi--4iK.XiKyh--8x',
        # 'Cookie': random.choice(cookie),
        'Host': 'm.weibo.cn',
        # 'Connection': 'close',
        'X-Requested-With': 'XMLHttpRequest'
    }
    containerid_url = 'https://m.weibo.cn/api/container/getIndex?type=uid&value='+id
    flag1 = 0
    # label.containerid1
    global proxies
    proxies1 = get_random_ip(proxies)
    try:
        print('containerid_json')
        # print(requests.get(containerid_url, proxies=proxies1, headers=headers))
        containerid_json_response = requests.get(containerid_url, proxies=proxies1, headers=headers, verify=False, timeout=30)
        print(containerid_json_response)
        if containerid_json_response.status_code != 200:
            print("containerid_json状态码有误，微博containerid页获取错误，休眠重启中.......")
            # containerid_json_response.close()
            time.sleep(60)
            goto.containerid1
        containerid_json = containerid_json_response.json()
        # time.sleep(0.1)
        time.sleep(random.uniform(0.5, 1.5))
        # print('containerid_json')
        # print(containerid_json)
        if containerid_json.get('errno'):
            print("系统繁忙，微博containerid页获取错误，休眠重启中.......")
            # containerid_json_response.close()
            time.sleep(60)
            goto.containerid1
    except Exception as e:
        traceback.print_exc()
        print("微博containerid页获取失败，休眠重启中.......")
        # containerid_json_response.close()
        time.sleep(60)
        goto.containerid1
    containerid_data = containerid_json['data'] if 'data' in containerid_json else None
    if not containerid_data:
        return -1
    tabsInfo = containerid_data['tabsInfo'] if 'tabsInfo'in containerid_data else None
    if not tabsInfo:
        return -1
    tabs = tabsInfo['tabs'] if 'tabs' in tabsInfo else None
    if not tabs:
        return -1
    containerid = tabs[0]['containerid'] if 'containerid'in tabs[0] else None
    return containerid

@with_goto
def get_user_location(profile_url):
    label.location1
    head = random.choice(list1)
    headers = {
        'Referer': 'https://m.weibo.cn',
        'User-Agent': head,
        # 'Cookie': 'SCF=Ajql1mQRxs50tXTYPERywh1-diUzc2uAEuoglbVmaiXFt3gQAomQmIk9jcmhnkjRp3kn-KgyYplgj1RBleSMRK8.; SUB=_2A25NxPFJDeRhGeNI6FoY9CjNwzyIHXVvRp8BrDV6PUJbktAKLUaskW1NSGOz-BCmW_LjsKdBkVm2PgMIldR05ogv; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5kH7G5AEKa9fqaPPs-rMI85NHD95QfSoeR1KBceKn7Ws4Dqcjwi--Ni-2pi-8Fi--4iKnfi-zRi--4iK.XiKyh--8x',
        # 'Cookie': random.choice(cookie),
        'Host': 'm.weibo.cn',
        # 'Connection': 'close',
        'X-Requested-With': 'XMLHttpRequest'
    }
    left,right = profile_url.split('?')
    uid = left.split('/')[-1]
    # print(uid)
    pre = 'https://m.weibo.cn/api/container/getIndex?'
    mid = '&type=uid&value='
    containerid = get_containerid(uid)
    # print(containerid)
    if containerid == -1:
        return '其它'
    if not containerid:
        return '其它'
    final_url = pre+right+mid+uid+'&containerid='+containerid
    flag1 = 0
    # label.location1
    global proxies
    proxies1 = get_random_ip(proxies)
    try:
        local_json_response = requests.get(final_url, headers=headers, proxies=proxies1, verify=False, timeout=30)
        print('local_json')
        # print(local_json)
        print(local_json_response)
        if local_json_response.status_code != 200:
            print("状态码返回有误，微博地址信息获取错误，休眠重启中.......")
            # local_json_response.close()
            time.sleep(60)
            goto.location1
        local_json = local_json_response.json()
        time.sleep(random.uniform(0.5, 1.5))
        if local_json.get('data'):
            if local_json.get('errno'):
                print("系统繁忙，微博地址信息获取错误，休眠重启中.......")
                # local_json_response.close()
                time.sleep(60)
                goto.location1
    except Exception as e:
        traceback.print_exc()
        print("微博用户地址信息获取失败，休眠重启中.......")
        time.sleep(60)
        goto.location1
    if local_json:
        data = local_json['data'] if 'data' in local_json else None
        # print('其它1')
        if not data:
            return '其它1'
        cards = data['cards'] if 'cards' in data else None
        # print('其它2')
        if not cards:
            return '其它2'
        card_group = cards[0]['card_group'] if 'card_group' in cards[0] else None
        # print('其它3')
        if not card_group:
            return '其它3'
        location = card_group[0]['item_content'] if 'item_content' in card_group[0] else None
        # print(location)
        return location
    else:
        return '其它4'
    return '其它5'

def save_data(data):
    # pass
    # client = get_hdfs_client()
    # if client is not None:
    #     try:
    #         client.write(hdfs_config_2["root_path"] + hdfs_config_2["weibo_path"] + "weibo.txt", data=json.dumps(data, ensure_ascii=False), append=True,
    #                      encoding="utf-8")
    #     except:
    #         client.write(hdfs_config_2["root_path"] + hdfs_config_2["weibo_path"] + "weibo.txt", data=json.dumps(data, ensure_ascii=False),
    #                      encoding="utf-8")
    #     # print(json.dumps(data, ensure_ascii=False))
    # else:
    #     print("#############################")
    #     print(
    #         data
    #     )

    producer = kafka_util.get_kafka_producer()
    producer.send("weibo_ods", value=str(data).encode("utf-8"))
    # producer.close()

    filename = 'weibo_topic.txt'
    with open(filename, 'a', encoding='utf-8') as f:
        f.write(json.dumps(data, ensure_ascii=False) + '\n')
    # f.close()


threads = []
#多线程设置
def thread_build():
    pool = ThreadPool(4)
    arg = [0, 1, 2, 3]
    requests = makeRequests(getdata, arg)
    [pool.putRequest(req) for req in requests]
    pool.wait()

    # 防止内存泄漏
    # pool.dismissWorkers(4, do_join=True)

    # threads.append(threading.Thread(target=getdata,args=(0, 3)))
    # threads.append(threading.Thread(target=getdata, args=(3, 6)))
    # threads.append(threading.Thread(target=getdata, args=(6, 9)))
    # threads.append(threading.Thread(target=getdata, args=(0, 1)))
    # threads.append(threading.Thread(target=getdata, args=(1, 2)))
    # threads.append(threading.Thread(target=getdata, args=(2, 3)))
    # threads.append(threading.Thread(target=getdata, args=(3,4)))
    # print(threads)
@with_goto
def getdata(bot):
    for list in list2[bot:bot+1]:
        global proxies
        proxies = proxies_tmp
        data = ''
        for page in range(1, 31):
            time.sleep(random.uniform(2,5))
            if page == 1:
                print("{}第{}页".format(list[1], page))
                data = get_page(list)
            else:
                print("{}第{}页".format(list[1], page))
                data = get_page(list, data)


def main():
    global proxies_tmp
    print("开始抓取代理IP...")
    proxies_tmp = []
    for url in tqdm(urls):
        proxies_tmp += get_ip_list(url)
    print(proxies_tmp)
    print("最终IP列表" + str(len(proxies_tmp)))
    thread_build()
    # for thread in threads:
    #     thread.start()

if __name__ == '__main__':
    # requests.adapters.DEFAULT_RETRIES = 10
    while True:
        try:
            main()
        except Exception as e:
            continue

