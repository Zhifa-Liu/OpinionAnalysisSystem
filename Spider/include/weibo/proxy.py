# IP地址取自国内髙匿代理IP网站：http://www.xicidaili.com/nn/
# 仅仅爬取首页IP地址就足够一般使用
from urllib.request import urlopen
import re
from bs4 import BeautifulSoup
import requests
import random
from google.auth.transport import urllib3
list1 = [
    "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36",
    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.75.14 (KHTML, like Gecko) Version/7.0.3 Safari/537.75.14",
    "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Win64; x64; Trident/6.0)",
    'Mozilla/5.0 (Windows; U; Windows NT 5.1; it; rv:1.8.1.11) Gecko/20071127 Firefox/2.0.0.11',
    'Opera/9.25 (Windows NT 5.1; U; en)',
    'Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)',
    'Mozilla/5.0 (compatible; Konqueror/3.5; Linux) KHTML/3.5.5 (like Gecko) (Kubuntu)',
    'Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.0.12) Gecko/20070731 Ubuntu/dapper-security Firefox/1.5.0.12',
    'Lynx/2.8.5rel.1 libwww-FM/2.14 SSL-MM/1.4.1 GNUTLS/1.2.9',
    "Mozilla/5.0 (X11; Linux i686) AppleWebKit/535.7 (KHTML, like Gecko) Ubuntu/11.04 Chromium/16.0.912.77 Chrome/16.0.912.77 Safari/535.7",
    "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:10.0) Gecko/20100101 Firefox/10.0 "
]

headers = {
    'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36'}
# headers = {
#             'Referer': 'https://m.weibo.cn',
#             'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0',
#             'Host': 'm.weibo.cn',
#             'X-Requested-With': 'XMLHttpRequest'
#         }

# 获取网页内容函数
def getHTMLText(url,proxies):
    headers = {
        'User-Agent': random.choice(list1)}
    try:
        r = requests.get(url, headers=headers)
        r.raise_for_status()
        r.encoding = r.apparent_encoding
    except:
        return 0
    else:
        return r.text

def filter(str):
    str = re.sub('\nt', "", str)
    str = re.sub(
        '[\001\002\003\004\005\006\007\x08\x09\x0a\x0b\x0c\x0d\x0e\x0f\x10\x11\x12\x13\x14\x15\x16\x17\x18\x19\x1a]+',
        '', str)
    return str

# 从代理ip网站获取代理ip列表函数，并检测可用性，返回ip列表
def get_ip_list(url):
    headers = {
        'User-Agent': random.choice(list1)}
    ip_list = []
    try:
        web_data = requests.get(url,headers=headers)
        # print(web_data)
        soup = BeautifulSoup(web_data.text, 'html5lib')
        ips = soup.find_all('tr')
        for i in range(1, len(ips)):
            ip_info = ips[i]
            tds = ip_info.find_all('td')
            if filter(tds[1].text) == 'HTTP代理':
                ip_list.append(filter(tds[0].text))
            else:
                ip_list.append(filter(tds[0].text) + ':' + filter(tds[1].text))
    #检测ip可用性，移除不可用ip：（这里其实总会出问题，你移除的ip可能只是暂时不能用，剩下的ip使用一次后可能之后也未必能用）
        # print(ip_list)
        # print("可用IP列表"+str(len(ip_list)))
        for ip in ip_list:
            try:
              proxy_host = "https://" + ip
              proxy_temp = {"https": proxy_host}
              res = urlopen(url, proxies=proxy_temp, headers=headers).read()
            except Exception as e:
              ip_list.remove(ip)
              continue
        # print(ip_list)
        # print("可用IP列表" + str(len(ip_list)))
        return ip_list
    except:
        return ip_list

# 从ip池中随机获取ip列表
def get_random_ip(ip_list):
    proxy_list = []
    for ip in ip_list:
        proxy_list.append('https://' + ip)
    proxy_ip = random.choice(proxy_list)
    proxies = {'http': proxy_ip}
    return proxies

# 调用代理
if __name__ == '__main__':
    url = 'https://www.kuaidaili.com/free/inha/2/'
    ip_list = get_ip_list(url)
    proxies = get_random_ip(ip_list)
    print(proxies)