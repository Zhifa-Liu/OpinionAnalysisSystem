mysql_config = {
    "host": "192.168.79.128",
    "user": "root",
    "password": "Hive@2020",
    "database": "poas"
}

spider_config = {
    "table": "spider",
}

hdfs_config_1 = {
    "user": "hadoop",
    "url": "http://192.168.79.128:9870",
    "root_path": "/spider/",
    "toutiao_path": "toutiao/",
    "weibo_path": "weibo/"
}

hdfs_config_2 = {
    "user": "Liu",
    "url": "http://192.168.2.5:9870",
    "root_path": "/spider/",
    "toutiao_path": "toutiao/",
    "weibo_path": "weibo/"
}

kafka_config_1 = {
    "server": "master:9092",
}
kafka_config_2 = {
    "server": "wasabi:9092",
}


edge_capabilities = {"browserName": "MicrosoftEdge",
                     "version": "",
                     "platform": "WINDOWS",
                     "ms:edgeOptions": {
                         'extensions': [],
                         'args': ['--headless',
                                  '--disable-gpu']
                     }
                     }

