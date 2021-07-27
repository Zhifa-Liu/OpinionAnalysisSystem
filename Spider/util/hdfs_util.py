# # coding: utf-8
# from hdfs.client import Client
# from hdfs.client import InsecureClient
# # https://hdfscli.readthedocs.io/en/latest/api.html
# from config.config import hdfs_config_1 as hdfs_config
#
#
# def get_hdfs_client(insecure=True):
#     if insecure:
#         return InsecureClient(url=hdfs_config["url"], user=hdfs_config["user"])
#     return Client(url=hdfs_config["url"], proxy=hdfs_config["user"], root="/")
#
#
# def close_hdfs_client(cli):
#     cli.close()
#
#
# if __name__ == '__main__':
#
#     client = get_hdfs_client(True)
#     print(client.list('/'))
#     data = {"article_author": "财经无忌", "article_author_desc": "财经评论员 优质财经领域创作者", "article_time": "2021-06-08 13:07", "article_content_text": "#湖北考生考试时拍数学题传上网被查#【武汉考生考试时拍照搜题被举报】前有考生因为带牙套需要出具加盖医院公章的证明信才能进入考场，后有考生考试时拍照上传小猿搜题被举报作弊。按理说高考应该是最严也最谨慎，怎么会出现电子设备带入考场这种情况。在众人熟知的高考，进门时需过安检，金属探测仪，信号屏蔽，这些是必备的，还有老师监考，场外还有巡考的。在这么严的监考制度下出现了拍照上传搜题，也只能说是艺高人胆大了，监考的老师也有责任。大家最大的疑问都是他是怎么把电子设备带进去且还有信号拍照上传搜题真的让人百思不得其解。根据最新的通报已经确认该考生是携带手机拍摄照片的，监考人员也进行了处罚。可是信号这个问题还是没有得到解答。", "article_like_count": "12", "article_comment_count": "5", "article_comments": [{"comment_text": "也许是团伙作案也许警方派来卧底考场的", "comment_like": "2", "comment_reply": "0"}, {"comment_text": "手机能带进考场？还能有机会拍照？还有网络能上传？这就不仅仅是考生的作蔽问题了，必须严查！", "comment_like": "3", "comment_reply": "0"}, {"comment_text": "说不定手机在布置考场前就已经在学校的某个角落了", "comment_like": "赞", "comment_reply": "0"}, {"comment_text": "也许是团伙作案也许警方派来卧底考场的", "comment_like": "2", "comment_reply": "0"}, {"comment_text": "手机能带进考场？还能有机会拍照？还有网络能上传？这就不仅仅是考生的作蔽问题了，必须严查！", "comment_like": "3", "comment_reply": "0"}, {"comment_text": "说不定手机在布置考场前就已经在学校的某个角落了", "comment_like": "赞", "comment_reply": "0"}, {"comment_text": "这条作弊链条里面有很多人参与其中，水很深，单凭考生根本做不到。", "comment_like": "1", "comment_reply": "0"}, {"comment_text": "也许只要一个人就可以做成这事。手机是进场后才给的；信号屏蔽只需要一个拔插座的动作；至于没看见，只需要一个转身。", "comment_like": "6", "comment_reply": "0"}], "spider_time": "2021-06-10 16:25"}
#     client.write("/file.txt", data=str(data)+"\n", encoding="utf-8", append=True)

