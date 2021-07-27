# coding: utf-8
from selenium import webdriver
import datetime
import time
import re
import json

string = {"article_author": "邵永灵", "article_author_desc": "2020百大人气创作者 军事专家 优质军事领域创作者", "article_time": "2021-06-15 13:34", "article_content_text": "#美媒：G7领导人和拜登发生严重分歧#【为了美国的利益去对抗中国？G7峰会注定充满争吵】据美国媒体报道，拜登试图说服七国集团领导人在谴责中国的联合声明上签字，但七国集团领导人在此问题上存在严重分歧。 其实关于此次G7峰会的矛盾与分歧，我们也不用将其想得过于深奥，从一个简单的道理和角度来说，那就是“资本是无情的，不论是面对敌人还是盟友”。不难发现，美国自以为对抗中国是美国乃至西方世界的利益所在，但真实情况与实际操作却没有那么简单。面对中国这个深不可测的世界级市场与工厂，任何资本家都希望能在此赚得盆满钵满。 对于资本主义国家来说，政府和国家领导人往往并非是真正意义上的掌权者，他们更像是资本的代言人，要为资本的增长与发展创造机会。而美国目前提出的要求则是：西方世界团结起来“整垮”中国。于是乎问题就来了：“整垮”中国需要一个过程，在这个过程中，各国付出的损失和代价由谁来买单？失去了世界级市场和工厂后，长期的损失由谁买单？这些都由美国来进行补偿吗？对于如今深陷美债危机和通货膨胀危机的美国来说，显然不可能…… 因此，当西方各国看清了现实，发现给美国“打工”却没有工钱，甚至自己还要“倒贴钱”之后，谁还愿意真正地卖力气呢？美国所谓的“对抗中国”，是为了保住自己的霸主地位，但对于其他国家来说，则完全没有为了美国的利益而让自己搭上性命的必要性。所以说，此次G7注定充满了争吵。", "article_like_count": "16", "article_comment_count": "1", "article_comments": [{"comment_text": "所谓分歧是做出来的成分更多，放大的分歧可以赚取更多的利益。", "comment_like": "赞", "comment_reply": "0"}], "spider_time": "2021-06-15 14:23"}
string["field"] = "zyz"
print(json.dumps(string, encoding='UTF-8', ensure_ascii=False))

string = "https://www.toutiao.com/w/i1701986574536704/?timestamp=1623144009&app=toutiao_web&use_new_style=1"
timestamp = string[string.find("timestamp"):].split("&")[0].split("=")[1]
print(timestamp)
timestamp = string.split("=")[1].split("&")[0]
print(timestamp)

print(datetime.datetime.now().strftime("%Y-%m-%d %H:%M"), " ", datetime.datetime.now().timestamp())

print(datetime.datetime.fromtimestamp(int(1623161927)))

# browser = webdriver.Edge(r"C:\Program Files (x86)\Microsoft\Edge\Application\msedgedriver.exe")
# browser.get(
#     "https://www.toutiao.com/w/i1701971973920776/?timestamp=1623142883&app=toutiao_web&use_new_style=1&log_from=88056f929f04c_1623142895424"
# )
# browser.maximize_window()
# time.sleep(2)
# comments_list = browser.find_element_by_class_name("comment-list").get_attribute("innerHTML")
# print(comments_list)

print(re.findall(r"\d+", "回复 ⋅ 9条回复"))


