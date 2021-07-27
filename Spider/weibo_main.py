# -*- coding: utf-8 -*-
import time

import schedule
from clickhouse_driver import Client

from include.weibo import weibo_spider

client = Client(host='192.168.79.128', database='poas', user='default', password='root')



def main():
    print("开始爬取微博......")
    try:
        sql = "select * from reptile_param order by modify_time desc limit 1"
        res = client.execute(sql)
        print("爬取间隔时间："+str(res[0][0])+res[0][1])
        if res[0][1] == 'minute':
            schedule.every(res[0][0]).minutes.do(weibo_spider.main())
        elif res[0][1] == 'hour':
            schedule.every(res[0][0]).hours.do(weibo_spider.main())
        elif res[0][1] == 'day':
            schedule.every(res[0][0]).days.do(weibo_spider.main())
    except Exception as e:
        print(str(e))
    print("微博爬取结束......")
    while True:
        schedule.run_pending()
        time.sleep(1)


if __name__ == "__main__":
    main()

