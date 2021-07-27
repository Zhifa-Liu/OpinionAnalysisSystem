<template>
    <div id="eventArticleTimeline">
        <span class="text" style="margin-top: 10px; display: block">文章时间线</span>
        <div id="articleTimeline" style="height:3.5rem;overflow-y:auto;overflow-x:hidden">
            <el-timeline style="margin-top: 0.1rem;width: 22rem">
                <el-timeline-item
                        v-for="(article, index) in articleTimeline"
                        :key="index"
                        :timestamp="article.articleTime"
                        placement="top"
                        color="white"
                >
                    <el-card style="color:#ffffff;border-color:rgba(113,107,107,0.4);background-color:rgba(153,146,146,0.4);">
                        {{ article.articleAuthor+"："+article.articleText }}
                    </el-card>

                </el-timeline-item>
<!--                <el-timeline-item timestamp="2018/4/12" placement="top">-->
<!--                    <el-card>-->
<!--                        <h4>更新 Github 模板</h4>-->
<!--                        <p>王小虎 提交于 2018/4/12 20:46</p>-->
<!--                    </el-card>-->
<!--                </el-timeline-item>-->
<!--                <el-timeline-item timestamp="2018/4/3" placement="top">-->
<!--                    <el-card>-->
<!--                        <h4>更新 Github 模板</h4>-->
<!--                        <p>王小虎 提交于 2018/4/3 20:46</p>-->
<!--                    </el-card>-->
<!--                </el-timeline-item>-->
<!--                <el-timeline-item timestamp="2018/4/2" placement="top">-->
<!--                    <el-card>-->
<!--                        <h4>更新 Github 模板</h4>-->
<!--                        <p>王小虎 提交于 2018/4/2 20:46</p>-->
<!--                    </el-card>-->
<!--                </el-timeline-item>-->
            </el-timeline>
        </div>
    </div>
</template>

<script>
    // import * as echarts from "echarts";
    // import 'echarts/map/js/china'
    import axios from "axios";

    export default {
        name: "eventArticleTimeline",
        props: ['eventName'],
        mounted () {
            this.getData()
        },
        data(){
            return {
                tableData: [],
                articleTimeline:[],
                timer:null
            }
        },
        created() {
            this.timer = setInterval(this.getData, 30000)
        },
        beforeDestroy() {
            clearInterval(this.timer);
            this.timer = null
        },
        methods: {
            init(){
                // var region_container = document.getElementById('articleTimeline');
                // var comment_region = echarts.init(region_container);
                // var optionMap = {
                // };
                // comment_region.setOption(optionMap);
            },
            async getData() {
                const that = this;
                await axios.get('/eventArticle/articleTimeline',{
                    params: {
                        eventName: this.eventName
                    }
                }).then(function (response) {
                    if (response.data) {
                        // alert(response.data)
                        // console.log("Hello Runoob!");
                        that.articleTimeline = response.data
                        // resdata=JSON.parse(JSON.stringify(that.tableData));
                        console.log("运行记录组件接到的数据2", JSON.stringify(that.articleTimeline));
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                this.init();
            }
        }
    }
</script>

<style scoped>
    #eventArticleTimeline {
        background-color: transparent !important;
        padding: 0.2rem 0.3rem 0;
        height: 4.2rem;
        width: 23.5rem;
        border-radius: 0.0625rem;
    }
    ::-webkit-scrollbar{width:0.1rem;}
    ::-webkit-scrollbar-track{background-color: #909393;}
    ::-webkit-scrollbar-thumb{background-color: #161617;}
    ::-webkit-scrollbar-thumb:hover {background-color:#9c3}
    ::-webkit-scrollbar-thumb:active {background-color: #0f0f10
    }

    #articleTimeline {
        height: 4.2rem;
        width: 23.5rem;
    }

    /*#graph {*/
    /*    background-color: white;*/
    /*}*/
</style>