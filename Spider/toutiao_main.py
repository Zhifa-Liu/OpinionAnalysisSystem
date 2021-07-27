# -*- coding: utf-8 -*-
import sys
sys.path.append("/home/hadoop/poas/Spider/include/toutiao")

from include.toutiao import toutiao

def main():
    print("开始爬取头条......")
    toutiao.main()
    print("头条爬取结束......")


if __name__ == "__main__":
    main()

