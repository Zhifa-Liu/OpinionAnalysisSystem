# coding: utf-8
import time
import datetime
import os
import sys
import json
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions

from util import kafka_util

# for window's edge
from config.config import edge_capabilities
# from msedge.selenium_tools import EdgeOptions
# from msedge.selenium_tools import Edge

from include.toutiao.get_article_comment import get_article_comment


def get_event_article(url, client=None):
    if sys.platform == 'win32' or sys.platform == 'win64':
        browser = webdriver.Edge(r"C:\Program Files (x86)\Microsoft\Edge\Application\msedgedriver.exe", edge_capabilities)
    else:
        opts = webdriver.FirefoxOptions()
        opts.add_argument("--headless")
        browser = webdriver.Firefox(executable_path=os.path.join(os.path.dirname(os.path.abspath(__file__)), "geckodriver"), options=opts)

    browser.get(url)
    time.sleep(5)

    js = "return action=document.body.scrollHeight"
    # 初始化现在滚动条所在高度为0
    height = 0
    # 当前窗口总高度
    new_height = browser.execute_script(js)

    # Solution 3: success
    # thanks for: https://blog.csdn.net/Stybill_LV_/article/details/111183750
    while height < new_height:
        # 将滚动条调整至页面底部
        for i in range(height, new_height, 100):
            browser.execute_script('window.scrollTo(0, {})'.format(i))
            time.sleep(0.2)
        height = new_height
        new_height = browser.execute_script(js)
        time.sleep(0.2)

    try:
        WebDriverWait(browser, 10).until(
            expected_conditions.presence_of_element_located((By.CLASS_NAME, "feed-m-loadall")))
    except Exception:
        print("Didn't get all article of current event!")

    # Solution 1: fail
    # for i in range(20):
    #     js = "window.scrollTo(0, document.body.scrollHeight)"
    #     browser.execute_script(js)
    #     time.sleep(2)

    # Solution 2: fail
    # https://blog.csdn.net/xu_cxiang/article/details/104539223

    articles = browser.find_elements_by_class_name("feed-wtt-wrapper")
    for article in articles:
        article_header = article.find_element_by_class_name("feed-card-wtt-header")
        article_content = article.find_element_by_class_name("feed-wtt-content-wrapper")
        article_action = article.find_element_by_class_name("feed-card-wtt-action-wrapper")

        article_author = article_header.find_element_by_class_name("feed-card-wtt-user-info").find_element_by_tag_name(
            "a").get_attribute("title")

        # Only day is accuracy
        # article_time = ""
        # article_time = article_header.find_element_by_class_name("time").text
        # if "分钟" in article_time:
        #     article_time = (datetime.datetime.now() - datetime.timedelta(minutes=int(article_time.split("分钟")[0]))).strftime("%Y-%m-%d %H:%M")
        # elif "小时" in article_time:
        #     article_time = (datetime.datetime.now() - datetime.timedelta(hours=int(article_time.split("小时")[0]))).strftime("%Y-%m-%d %H:%M")
        # elif "天" in article_time:
        #     article_time = (datetime.datetime.now() - datetime.timedelta(days=int(article_time.split("天")[0]))).strftime("%Y-%m-%d %H:%M")

        try:
            article_author_desc = article_header.find_element_by_class_name("user-auth-desc").get_attribute("title")
        except Exception:
            article_author_desc = ""

        article_content_text = article_content.find_element_by_class_name("abstract-text").text
        article_content_url = article_content.find_element_by_tag_name("a").get_attribute("href")

        article_like = article_action.find_element_by_class_name("feed-card-footer-like-cmp").find_element_by_tag_name(
            "span").text
        article_comment = article_action.find_element_by_class_name(
            "feed-card-footer-comment-cmp").find_element_by_tag_name("a").text

        if sys.platform == 'win32' or sys.platform == 'win64':
            driver = webdriver.Edge(r"C:\Program Files (x86)\Microsoft\Edge\Application\msedgedriver.exe")
        else:
            opts = webdriver.FirefoxOptions()
            opts.add_argument("--headless")
            driver = webdriver.Firefox(executable_path=os.path.join(os.path.dirname(os.path.abspath(__file__)), "geckodriver"), options=opts)

        driver.set_window_position(1300, 0)
        driver.get(article_content_url)
        time.sleep(6)

        timestamp = article_content_url[article_content_url.find("timestamp"):].split("&")[0].split("=")[1]
        spider_time = datetime.datetime.fromtimestamp(int(timestamp)).strftime("%Y-%m-%d %H:%M")

        comments = []
        article_time = []
        try:
            try:
                article_time = driver.find_element_by_class_name("time").text
                # print(article_time)
                if article_comment != '0':
                    comments = get_article_comment(driver, "1")
            except Exception:
                article_time = driver.find_element_by_class_name("publish-time").text
                if article_comment != '0':
                    comments = get_article_comment(driver, "2")
        except Exception:
            pass

        driver.close()

        data = {
            "field": "",
            "user_name": article_author,
            "user_id": "",
            "gender": "",
            "location": "",
            "fans_count": "",
            "user_type": article_author_desc,
            "theme": "",
            "create_time": article_time,
            "text": article_content_text,
            "attitudes_count": article_like,
            "comments_count": article_comment,
            "reports_count": "",
            "get_time": spider_time,
            "comments": comments
        }
        if client is not None:
            pass
            # try:
            #     client.write(hdfs_config["root_path"] + hdfs_config["toutiao_path"] + "toutiao.txt", data=str(data)+"\n",
            #                  append=True, encoding="utf-8")
            # except:
            #     client.write(hdfs_config["root_path"] + hdfs_config["toutiao_path"] + "toutiao.txt", data=str(data)+"\n",
            #                  encoding="utf-8")
            # print(json.dumps(data, ensure_ascii=False))

        filename = 'toutiao.txt'
        with open(filename, 'a', encoding='utf-8') as f:
            f.write(json.dumps(data, ensure_ascii=False) + '\n')
        f.close()

        print("#############################")
        print(
            data
        )

        try:
            producer = kafka_util.get_kafka_producer()
            producer.send("toutiao", value=str(data).encode("utf-8"))
            producer.close()
        except Exception:
            print("kafka producer get failed!")

    browser.close()


if __name__ == '__main__':
    if sys.platform == 'win32' or sys.platform == 'win64':
        driver = webdriver.Edge(r"C:\Program Files (x86)\Microsoft\Edge\Application\msedgedriver.exe")
    else:
        options = webdriver.FirefoxOptions()
        options.add_argument("--headless")
        options.add_argument("blink-settings=imagesEnabled=false")
        browser = webdriver.Firefox(executable_path=os.path.join(os.path.dirname(os.path.abspath(__file__)), "geckodriver"), options=options)
    # headless 1:
    # edge_options = EdgeOptions()
    # edge_options.use_chromium = True
    # edge_options.add_argument("headless")
    # browser = Edge(r"C:\Program Files (x86)\Microsoft\Edge\Application\msedgedriver.exe", options=edge_options)
    # headless 2:
    # browser = webdriver.Edge(r"C:\Program Files (x86)\Microsoft\Edge\Application\msedgedriver.exe", edge_capabilities)

    get_event_article(
        ""
    )

