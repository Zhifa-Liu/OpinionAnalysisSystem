<template>
    <!-- 事件文章分析 -->
    <div id="eventArticleTopN">
        <div id="rank_div">
            <div style="display: flex">
                <div style="margin-right: 1.5rem">
                    <span class="text" style="margin-top: 10px">事件文章排名</span>
                </div>
                <div>
                    <el-button-group>
                        <el-button type="primary" size="mini" @click="replaceGraphA()" class="btns">点赞</el-button>
                        <el-button type="primary" size="mini" @click="replaceGraphB()" class="btns">转发</el-button>
                        <el-button type="primary" size="mini" @click="replaceGraphC()" class="btns">评论</el-button>
<!--                        <el-button type="primary" size="mini" @click="replaceGraphD()" class="btns">声量</el-button>-->
                    </el-button-group>
                </div>
            </div>
            <div id="graph_div">
            </div>
        </div>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "eventArticleAnalysis",
        props: ['eventName'],
        mounted () {
            this.getData()
        },
        data(){
            return{
                tableData:[],
                attitudeData:[],
                commentData:[],
                reportData:[],
                attitudeId:[],
                commentId:[],
                reportId:[],
                rank:[],
                top:[],
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
                this.likeOption = {
                    color: ['#409EFF'],
                    // grid: {
                    //     containLabel: true,
                    // },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },

                    xAxis: {
                        name: '',
                        type: 'category',
                        data: this.attitudeId,
                        // data: ['点赞','评论','转发'],
                        boundaryGap: true,
                        axisLabel: {
                            rotate: 30,
                            color: "white",
                            fontWeight: "lighter",
                            fontFamily: 'Microsoft YaHei, Arial, Verdana, sans-serif'
                        },
                        axisLine: {
                            symbol: ['none', 'arrow'],
                            symbolSize: [10, 10],
                            symbolOffset: [0, 5],
                            lineStyle: {
                                color: 'lightblue'
                            }
                        },
                        axisTick: {
                            inside: true
                        }
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            color: "white",
                            fontWeight: "lighter",
                            fontFamily: 'Microsoft YaHei'
                        },
                        axisLine: {
                            symbol: ['none', 'arrow'],
                            symbolSize: [10, 10],
                            symbolOffset: [0, 5],
                            lineStyle: {
                                color: 'lightblue'
                            }
                        },
                        axisTick: {
                            inside: true,
                            length: 5
                        },
                        splitLine: {
                            show: true,
                            lineStyle: 'lightblue'
                        }
                    },
                    series: [
                        {
                        data: this.attitudeData,
                        stack: 'total',
                        type: 'bar',
                        barWidth: 25,
                            itemStyle:{
                                barBorderRadius:10,
                            },
                    }
                    ]
                };

                this.forwardOption = {
                    color: ['#409EFF'],
                    // grid: {
                    //     containLabel: true,
                    // },
                    xAxis: {
                        name: '',
                        type: 'category',
                        data: this.reportId,
                        boundaryGap: true,
                        axisLabel: {
                            rotate: 30,
                            color: "white",
                            fontWeight: "lighter",
                            fontFamily: 'Microsoft YaHei, Arial, Verdana, sans-serif'
                        },
                        axisLine: {
                            symbol: ['none', 'arrow'],
                            symbolSize: [10, 10],
                            symbolOffset: [0, 5],
                            lineStyle: {
                                color: 'lightblue'
                            }
                        },
                        axisTick: {
                            inside: true
                        }
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            color: "white",
                            fontWeight: "lighter",
                            fontFamily: 'Microsoft YaHei'
                        },
                        axisLine: {
                            symbol: ['none', 'arrow'],
                            symbolSize: [10, 10],
                            symbolOffset: [0, 5],
                            lineStyle: {
                                color: 'lightblue'
                            }
                        },
                        axisTick: {
                            inside: true,
                            length: 5
                        },
                        splitLine: {
                            show: true,
                            lineStyle: 'lightblue'
                        }
                    },
                    series: [{
                        data: this.reportData,
                        type: 'bar',
                        barWidth: 25
                    }]
                };

                this.commentOption = {
                    color: ['#409EFF'],
                    // grid: {
                    //     containLabel: true,
                    // },
                    xAxis: {
                        name: '',
                        type: 'category',
                        data: this.commentId,
                        boundaryGap: true,
                        axisLabel: {
                            rotate: 30,
                            color: "white",
                            fontWeight: "lighter",
                            fontFamily: 'Microsoft YaHei, Arial, Verdana, sans-serif'
                        },
                        axisLine: {
                            symbol: ['none', 'arrow'],
                            symbolSize: [10, 10],
                            symbolOffset: [0, 5],
                            lineStyle: {
                                color: 'lightblue'
                            }
                        },
                        axisTick: {
                            inside: true
                        }
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            color: "white",
                            fontWeight: "lighter",
                            fontFamily: 'Microsoft YaHei'
                        },
                        axisLine: {
                            symbol: ['none', 'arrow'],
                            symbolSize: [10, 10],
                            symbolOffset: [0, 5],
                            lineStyle: {
                                color: 'lightblue'
                            }
                        },
                        axisTick: {
                            inside: true,
                            length: 5
                        },
                        splitLine: {
                            show: true,
                            lineStyle: 'lightblue'
                        }
                    },
                    series: [{
                        data: this.commentData,
                        type: 'bar',
                        barWidth: 25
                    }]
                };

                this.draw();
            },
            draw(){
                this.$echarts.init(document.getElementById("graph_div")).setOption(this.likeOption)
            },
            replaceGraphA(){
                this.$echarts.init(document.getElementById("graph_div")).setOption(this.likeOption)
            },
            replaceGraphB(){
                this.$echarts.init(document.getElementById("graph_div")).setOption(this.forwardOption)
            },
            replaceGraphC(){
                this.$echarts.init(document.getElementById("graph_div")).setOption(this.commentOption)
            },
            async getData(){
                const that = this;
                await axios.get('/eventArticle/eventArticleRank',{
                    params: {
                        eventName: this.eventName
                    }
                }).then(function (response) {
                    if (response.data) {
                        var data=response.data
                        that.attitudeId=data[0].articleAuthor
                        that.attitudeData=data[0].articleLikeCount
                        that.commentId=data[1].articleAuthor
                        that.commentData=data[1].articleCommentCount
                        that.reportId=data[2].articleAuthor
                        that.reportData=data[2].articleReportCount

                        console.log(that.attitudeId)

                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                this.init();
            }
            // replaceGraphD(){
            //     this.$echarts.init(document.getElementById("graph_div")).setOption(this.volumeOption)
            // }
        }
    }
</script>

<style lang="scss">
    #eventArticleTopN {
        /*display: flex;*/
        background-color: transparent !important;
        padding: 0.2rem 0.3rem 0;
        height: 6rem;
        width: 8rem;
        border-radius: 0.0625rem;
        #rank_div {
            display: block;
            width: 8rem;
            height: 5.8rem;
            .text {
                display: block;
                color: white;
            }

            #graph_div {
                margin-top: -10px;
                /*background-color: white;*/
                background-color: transparent;
                width: 8rem;
                height: 5.2rem;
            }
        }
    }

    .el-button-group {
        margin-left: 10px;
        margin-top: 10px;
        background-color: transparent;
    }
    .el-button {
        background-color: transparent;
        border-color: white;
    }
    .el-button.btns:active{
        background-color: lightblue;
    }
</style>