# OpinionAnalysisSystem
- Spider 舆情数据爬虫
- POAS 舆情分析与服务
- vue_poas 前端

# 需求规格说明书

### 一、引言
#### 1.1 目的
&emsp;&emsp;编写此文档的目的是确认舆情分析系统的需求及系统边界，指导系统的设计。
#### 1.2 项目信息
- 项目名称：舆情分析系统
- 项目提出者：指导教师
- 开发者：东北大学软件学院大数据班T09实训项目组(lzf、lcx)
- 用户：舆情分析员、系统管理员

#### 1.3 缩写说明
#### 1.4 术语定义
![在这里插入图片描述](https://img-blog.csdnimg.cn/2896bf595c8440079c500901d4f938c0.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)
#### 1.5 参考资料
- 新浪舆情通：https://yqt.mdata.net/

### 二、舆情分析系统概述
#### 2.1 舆情分析系统介绍
&emsp;&emsp;我们的舆情分析系统主要包括舆情总缆分析、舆情搜索、文章分析、文章评论分析、事件舆情分析、事件舆情预警六大功能模块以及管理员系统配置模块。针对舆情总览分析、舆情搜索、文章分析、文章评论分析、事件舆情分析、事件舆情预警我们的分析数据来源于多个网站关于某一事件的报道文章的爬取，如微博、今日头条、知乎等，但主要集中于微博。管理员配置模块配置的是爬虫的爬虫间隔、舆情事件的展示参数以及系统日志查看。
#### 2.2 舆情分析系统价值主张与愿景
&emsp;&emsp;不论是热点新闻还是娱乐八卦，传播速度远超我们的想象。可以在短短数分钟内，有数万计转发，数百万的阅读。如此海量的信息可以得到爆炸式的传播，如何能够实时的把握民情并作出对应的处理对很多企业来说都是至关重要的。我们的舆情分析系统的目的是通过大数据技术实时获取民众舆论并分析舆论变化情况，同时能够提供舆情预警使得可以引导舆情向好的方向发展。
#### 2.3 舆情分析系统功能架构
&emsp;&emsp;下图为舆情分析系统整体功能架构图:
![在这里插入图片描述](https://img-blog.csdnimg.cn/6e9c72593df2406d96596b1141c9e21a.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)

#### 2.4 系统数据描述
&emsp;&emsp;系统的数据来源于微博博文与今日头条新闻文章舆情数据的实时爬取，爬取的数据包括文章内容、文章作者、文章点赞量、文章评论量、文章转发量、文章时间、文章评论、文章评论对应的评论者性别、文章评论的点赞量、文章评论的回复量等。
爬虫爬取到的数据为JSON串(表示的是文章对象)，文章对象的属性及其说明如下：
- field
	- 文章所属领域
- user_name
	- 用户名(即文章作者名)
- user_id
	- 用户ID(即文章作者ID)，文章作者为某一事件的传播人
- user_type
	-用户类型(即作者类型)
- gender
	- 作者性别
- location
	- 作者地域，由于或取不到文章发表时的IP，采用文章作者的地域作为文章发表时的地址
- fans_count
	- 作者粉丝数
- blog_id
	- 文章(如博客等)ID
- create_date
	- 创建时间，即文章时间
- text
	- 文章内容(文章文本)
- attitudes_count
	- 文章点赞量
- comments_count
	- 文章评论量
- reports_count
	- 文章转发量
- get_time
	- 文章爬取时间
- comments
	- 文章的各个评论组成的JSON数组，数组的元素为JSON字符串(表示的是评论对象)

&emsp;&emsp;评论对象的属性及其说明如下表所示：
- comment_id
	- 评论ID
- commenter_id
	- 评论者ID
- commenter_name
	- 评论者名称
- commenter_gender
	- 评论者性别
- comment_text
	- 评论文本
- comment_reply
	- 评论回复量
- comment_like
	- 评论点赞量

### 三、功能性需求
&emsp;&emsp;系统用户中舆情观察分析员与系统管理员的用例图为：
![在这里插入图片描述](https://img-blog.csdnimg.cn/f651be53289945bf9bc7261afa310750.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)


#### 3.1	舆情首页需求
![在这里插入图片描述](https://img-blog.csdnimg.cn/7ecd7c15443c4db0a5ea3f5b68bf7ee2.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)


##### 3.1.1	领域舆情热度
&emsp;&emsp;用表格展示不同领域下近七天事件的热度排名，表格展示的字段有事件名称，时间热度，事件类型，热度排名，点击事件名称可跳转到对于事件的详情页。
 
##### 3.1.2	领域舆情热度时间变化
&emsp;&emsp;用折线图展示不同领域最近七天的舆情热度变化。
 
##### 3.1.3	地域舆情分布
&emsp;&emsp;用热度地图展示中国范围内所有舆情文章的地域数量分布情况。
 
#### 3.2	舆情搜索页需求
![在这里插入图片描述](https://img-blog.csdnimg.cn/c9849e27c5e042159cc565f86884bf20.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)


##### 3.2.1	舆情事件搜索
&emsp;&emsp;提供搜索框，输入事件关键词，将对应的事件名称，事件热度，事件类型用表格的显示展示在搜索框下，点击事件名称可跳转到对应事件详情页。
 
#### 3.3	舆情预警页需求
![在这里插入图片描述](https://img-blog.csdnimg.cn/a1f0b927071346ccbfa831804f708404.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)

##### 3.3.1	事件负面评论预警列表
&emsp;&emsp;用表格展示近七天内事件负面评论占比大于预警阈值的事件，表格包括事件名称，事件负面评论占比，负面舆论环比增长率（由最近一天的数据和前一天的数据计算而来），表格可根据事件负面评论占比和环比增长率进行排序，其中正增长用红色字体和增长箭头标识，负增长由绿色字体和下降箭头标识，点击事件名称可跳转至对应事件详情页。
 
##### 3.3.2	事件情感占比排名
&emsp;&emsp;用饼图展示事件负面评论占比排名 TopN 的事件，以及展示事件负面评论环比增长排名 TopN 的事件。
 
##### 3.3.3	事件热度增长列表
&emsp;&emsp;用表格展示近七天事件的热度增长率，表格字段有事件名称、时间热度、增长率，其中正增长用红色字体和增长箭头标识，负增长由绿色字体和下降箭头标识，点击事件名称可跳转至对应事件详情页。
 
##### 3.3.4	事件热度增长排名
&emsp;&emsp;用柱形图标识事件增长率排名靠前的事件，包括事件名称和事件热度增长率。
 
#### 3.4	舆情事件总览页需求
![在这里插入图片描述](https://img-blog.csdnimg.cn/6684b08c0d8b4890829ba07d8fe561d6.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)

##### 3.4.1	事件关键词词云
&emsp;&emsp;用词云图展示事件关键词。

##### 3.4.2	事件传播趋势
&emsp;&emsp;用折线图展示事件关注度走势和事件信息量走势，关注度是事件所有文章阅读量(由于无法爬取到阅读量，采用点赞量代替)之和，信息量事件所有文章的数量。
##### 3.4.3	时间核心传播人
&emsp;&emsp;用饼图展示事件核心传播人占比，可设置展示媒体或网民，展示的数据主要有传播人名称和传播量，传播量指文章的转发量。
 
##### 3.4.4	时间关注度增长趋势
&emsp;&emsp;用折线面积图展示事件关注度环比增长率随时间的变化曲线。
 
#### 3.5	舆情事件文章页需求
![在这里插入图片描述](https://img-blog.csdnimg.cn/b5f04fb50310464caeaed76ad98f949c.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)

##### 3.5.1	事件文章排名
&emsp;&emsp;用柱状图展示相关事件文章排名，横坐标是文章作者的名称，纵坐标数据可由按钮切换成点赞数，转发数，评论数。
 
##### 3.5.2	事件文章地域分布
&emsp;&emsp;用地图展示事件文章发表的地域分布(关于某事件的所有文章在全国的数量分布)。
 
##### 3.5.3	事件热度TopN文章
&emsp;&emsp;卡片展示热度排名TopN文章的具体内容，作者名称，文章热度，可用走马灯组件进行切换展示。
 
##### 3.5.4	事件文章时间线列表
&emsp;&emsp;用时间线按时间展示当前事件文章的发表，展示字段文章时间，文章作者，文章内容。
 
#### 3.6	舆情事件评论页需求
![在这里插入图片描述](https://img-blog.csdnimg.cn/c26aa13b5e8a4885bb83476a69874c8b.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/3fdffa6e5a0c4864926e8cfd582e4a48.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)

##### 3.6.1	事件评论关键词词云
&emsp;&emsp;用词云图展示当前事件下所用评论的关键词。
 
##### 3.6.2	事件评论情感走势
&emsp;&emsp;折线图展示当前事件下所有评论的情感趋势变化，图像中标识出最大值和最小值，横坐标为事件，纵坐标分别为负面评论和正面评论的占比，点击折现右边显示当前横坐标时间之前的所有评论，按照正面，负面，中立进行分类。
 
##### 3.6.3	事件评论者性别占比分析
&emsp;&emsp;用饼状图展示当前事件下评论者的性别占比。
 
##### 3.6.4	事件高赞评论
&emsp;&emsp;饼图展示当前事件下的高赞评论。
 
##### 3.6.5	事件高回复评论
&emsp;&emsp;饼图展示当前事件下的高回复评论。
 
#### 3.7	管理员页需求
##### 3.7.1	爬虫参数配置
&emsp;&emsp;设置爬虫爬取事件间隔。
![在这里插入图片描述](https://img-blog.csdnimg.cn/00765f543e904105a1753851afa4d7c0.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)

##### 3.7.2	TopN参数配置
&emsp;&emsp;设置文章排名、文章热度排名、事件高赞与高回复评论排名。
![在这里插入图片描述](https://img-blog.csdnimg.cn/3837d0c842b24ec9acf4a536b88256af.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)


 
##### 3.7.3 事件负面评论占比预警阈值配置
&emsp;&emsp;在事件负面评论占比输入框内输入事件负面评论占比阈值，点击设置事件负面评论占比预警阈值。
![在这里插入图片描述](https://img-blog.csdnimg.cn/1e83ab81f2ae4896ab84c36ff64cffdd.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)



##### 3.7.4	系统日志页
&emsp;&emsp;按时间线显示系统操作的记录。
![在这里插入图片描述](https://img-blog.csdnimg.cn/d6624c046d8a42c09bf2f1684780ea98.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0OTkyNTU5,size_16,color_FFFFFF,t_70)


### 4.	非功能性需求
#### 4.1	可交互性
&emsp;&emsp;系统的人机交互符合人的认知心理学基本原理，并且需要降低系统工作人员的学习成本，必要的话还要提供系统使用的帮助文档。
#### 4.2	可维护性与可扩展性
&emsp;&emsp;系统基于大数据生态组件构建，鉴于大数据组件的横向扩展能力，系统的可扩展性有一定保证。系统代码的开发需要满足代码开发规范，需要做好充分的注释、注意代码的可复用性、注意功能模块之间解耦能力，使得系统能够以较低成本进行二次开发、进行功能扩展、进行系统维护。
#### 4.3	可适应性
&emsp;&emsp;网页需要支持可视化图表在主流浏览器的正常加载显示，以及在浏览器窗口大小变化时它们也能够适应窗口大小正常加载显示。
#### 4.4	响应性
&emsp;&emsp;在网络正常的情况下用户点击网页后页面的跳转时间<=3s；若页面的数据量较大而导致的页面加载时间长的话，页面必须提供网页加载提示。
#### 4.5	可靠性
&emsp;&emsp;在系统发生故障后，需要保证系统可以在较短时间内重建其性能水平并恢复直接受影响数据的能力，并且使系统故障率保持在一定的水平下。
#### 4.6	安全性
&emsp;&emsp;系统需要保证数据的安全，防止数据的泄漏等。
