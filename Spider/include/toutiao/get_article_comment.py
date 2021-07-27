from selenium import webdriver
import time
import re


def get_article_comment(browser, page):
    js = "var q=document.documentElement.scrollTop=500"
    browser.execute_script(js)
    comments = []
    if page == '1':
        # comment_list = browser.find_elements_by_xpath("//ul[@class='comment-list']/li")
        # for comment in comment_list:
        #     print(comment.find_element_by_css_selector("div.comment-info>div.body>p").text)
        #     # print(comment.get_attribute("innerHTML"))
        #     # print(comment.find_element_by_xpath("//div[@class='comment-info']//div[2]/p").text)  # duplicate why?
        #
        #     print(comment.find_element_by_css_selector("div.ttp-comment-like>div.inner>span").text)
        #     try:
        #         print(comment.find_element_by_css_selector("div.comment-info>div.check-more-reply").text.split(" ")[1])
        #     except Exception:
        #         print("0")
        #
        #     comments.append(comment.find_element_by_css_selector("div.comment-info>div.body>p").text)
        try:
            browser.find_element_by_xpath("//div[@class='side-drawer-btn']").click()
        except Exception:
            pass
        time.sleep(3)
        try:
            browser.find_element_by_xpath("//div[@class='load-more-btn']").click()
        except Exception:
            pass
        time.sleep(2)
        comment_list = browser.find_elements_by_xpath("//div[@class='ttp-comment-wrapper']/ul/li")
        for comment in comment_list:
            # print(comment.find_element_by_css_selector("div.comment-info>div.body>p").text)
            comment_text = comment.find_element_by_css_selector("div.comment-info>div.body>p").text
            # print(comment.get_attribute("innerHTML"))
            # print(comment.find_element_by_xpath("//div[@class='comment-info']//div[2]/p").text)  # duplicate why?

            # print(comment.find_element_by_css_selector("div.ttp-comment-like>div.inner>span").text)
            comment_like = comment.find_element_by_css_selector("div.ttp-comment-like>div.inner>span").text.strip()
            try:
                # print(comment.find_element_by_css_selector("div.comment-info>div.check-more-reply").text.split(" ")[1])
                comment_reply = comment.find_element_by_css_selector("div.comment-info>div.check-more-reply").text.split(" ")[1]
            except Exception:
                # print("0")
                comment_reply = "0"

            comments.append({"comment_id": "", "commenter_id": "", "commenter_name": "", "commenter_gender": "", "comment_location": "", "comment_text": comment_text, "comment_like": comment_like, "comment_reply": comment_reply})

    else:
        try:
            browser.find_element_by_xpath("//div[@class='load-more-comment']/button").click()
        except Exception:
            pass
        time.sleep(2)
        comment_list = browser.find_elements_by_xpath("//div[@class='comment-list']/ul/*")
        for comment in comment_list:
            # print(comment.find_element_by_css_selector("div.comment-detail>p").text)
            comment_text = comment.find_element_by_css_selector("div.comment-detail>p").text
            # print(comment.get_attribute("innerHTML"))
            # print(comment.find_element_by_xpath("//div[@class='comment-detail']/p").text)  # duplicate why?

            # print(comment.find_element_by_css_selector("div.comment-detail>div.footer>span.digg").text.split("条")[0])
            comment_like = comment.find_element_by_css_selector("div.comment-detail>div.footer>span.digg").text.split("条")[0].strip()
            comment_reply = ""
            try:
                # print(comment.find_element_by_css_selector("div.comment-detail>div.footer>span.reply-num").text)
                # comment_reply = comment.find_element_by_css_selector("div.comment-detail>div.footer>span.reply-num").text
                string = comment.find_element_by_css_selector("div.comment-detail>div.footer>span.reply-num").text
                # print(string)
                comment_reply = re.findall(r"\d+", string)[0]
            except Exception:
                # print("0")
                comment_reply = "0"

            comments.append({"comment_id": "", "commenter_id": "", "commenter_name": "", "commenter_gender": "", "comment_location": "", "comment_text": comment_text, "comment_like": comment_like, "comment_reply": comment_reply})
    # print(comments)
    return comments


if __name__ == '__main__':
    # options = webdriver.FirefoxOptions()
    # options.add_argument("headless")
    browser = webdriver.Edge(r"C:\Program Files (x86)\Microsoft\Edge\Application\msedgedriver.exe")
    browser.maximize_window()
    browser.get(
        "https://www.toutiao.com/w/i1701982721997827/?timestamp=1623210644&app=toutiao_web&use_new_style=1&wid=1623210652697"
    )
    time.sleep(6)
    try:
        print(browser.find_element_by_class_name("time"))
        get_article_comment(browser, "1")
    except Exception:
        print(browser.find_element_by_class_name("publish-time"))
        get_article_comment(browser, "2")
