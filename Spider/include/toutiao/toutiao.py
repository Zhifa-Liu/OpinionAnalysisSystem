import time

from selenium import webdriver
from include.toutiao.get_event_article import get_event_article
import sys
import os

# from concurrent.futures import ThreadPoolExecutor

# for window's edge
from config.config import edge_capabilities


class Toutiao:
    def __init__(self):
        self.url = 'https://www.toutiao.com/'

    def get_browser(self, headless):
        # https://npm.taobao.org/mirrors/geckodriver
        # https://github.com/mozilla/geckodriver/releases   火狐版本与对应的驱动版本
        if headless:
            if sys.platform == 'win32' or sys.platform == 'win64':
                browser = webdriver.Edge(r"C:\Program Files (x86)\Microsoft\Edge\Application\msedgedriver.exe", edge_capabilities)
            # elif sys.platform == 'linux':
            else:
                opts = webdriver.FirefoxOptions()
                opts.add_argument("--headless")
                browser = webdriver.Firefox(executable_path=os.path.join(os.path.dirname(os.path.abspath(__file__)), "geckodriver"), options=opts)
        else:
            if sys.platform == 'win32' or sys.platform == 'win64':
                browser = webdriver.Edge(r"C:\Program Files (x86)\Microsoft\Edge\Application\msedgedriver.exe")
            else:
                browser = webdriver.Firefox(executable_path=os.path.join(os.path.dirname(os.path.abspath(__file__)), "geckodriver"))
        browser.get(self.url)
        browser.maximize_window()
        time.sleep(2)
        return browser

    def spider_hot_chart(self):
        browser = self.get_browser(headless=False)
        browser.execute_script("var q=document.documentElement.scrollTop=500")
        time.sleep(2)

        urls = []
        titles = []
        click_count = 0
        while True:
            hots = browser.find_elements_by_xpath("//div[@class='hot-list']/div/ul/*")
            for hot in hots:
                url = hot.find_element_by_xpath('a').get_attribute("href")
                title = hot.find_element_by_xpath('a/p').get_attribute("title")
                if title not in titles:
                    urls.append(url)
                    titles.append(title)
                    # print(title, len(titles))
                else:
                    continue

            # pool = ThreadPoolExecutor(max_workers=5)
            if click_count == 5:
                browser.close()

                for url in urls:
                    get_event_article(url)

                # for i in range(len(urls)):
                #     get_event_article(urls[len(urls)-2], client)

                # tasks = []
                # for url in urls:
                #     task = pool.submit(get_event_article, *(url, client))
                #     tasks.append(task)
                #
                # wait(tasks, return_when=ALL_COMPLETED)

                exit(0)

            ele = browser.find_element_by_xpath("//div[@class='hot-list']/div/div/button")
            browser.execute_script("arguments[0].click();", ele)
            click_count += 1
            time.sleep(5)


def main():
    toutiao = Toutiao()
    toutiao.spider_hot_chart()


if __name__ == '__main__':
    main()

