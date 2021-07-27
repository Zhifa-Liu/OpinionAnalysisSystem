import schedule
import time

import toutiao_main
import weibo_main


def job():
    print("I'm working...")


# schedule.every(1).seconds.do(job)
# schedule.every().hour.do(job)
# schedule.every().day.at("10:30").do(job)
# schedule.every(5).to(10).days.do(job)
# schedule.every().monday.do(job)
# schedule.every().wednesday.at("13:15").do(job)

if __name__ == '__main__':
    schedule.every().day.at("00:00").do(weibo_main.main)
    schedule.every().day.at("00:00").do(toutiao_main.main)
    while True:
        schedule.run_pending()
        time.sleep(1)