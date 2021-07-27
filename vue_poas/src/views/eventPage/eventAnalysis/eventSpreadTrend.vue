<template>
    <div id="div_box">
        <span class="text" style="display: block">事件传播趋势曲线</span>
        <div style="display: flex;">
<!--            <div style="margin-right: 160px; margin-left: 25px">-->
<!--                <el-button-group>-->
<!--                    <el-button type="primary" size="mini" @click="changeToInfo">事件信息量走势</el-button>-->
<!--                    <el-button type="primary" size="mini" @click="changeToAttention">事件关注度走势</el-button>-->
<!--                </el-button-group>-->
<!--            </div>-->
            <div style="margin-right: 1rem">
                <el-button-group>
<!--                    <el-button type="primary" size="mini">近一日</el-button>-->
                    <el-button type="primary" size="mini" @click="replaceGraphA()">按小时</el-button>
                    <el-button type="primary" size="mini" @click="replaceGraphB()">按天</el-button>
                </el-button-group>
            </div>
<!--            <div style="margin-top: 0.14rem">-->
<!--                <el-date-picker-->
<!--                    v-model="v"-->
<!--                    type="daterange"-->
<!--                    size="mini"-->
<!--                    range-separator="至"-->
<!--                    start-placeholder="开始日期"-->
<!--                    end-placeholder="结束日期">-->
<!--                </el-date-picker>-->
<!--            </div>-->
        </div>
        <div>
            <div id="trend">
            </div>
<!--            <div id="info_div">-->
<!--            </div>-->
<!--            <div id="attention_div">-->
<!--            </div>-->
        </div>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "eventArticleSpread",
        props: ['eventName'],
        mounted () {
            this.getData()
        },
        data() {
            return {
                timeData:[],
                attentionData:[],
                infoData:[],
                timeDayData:[],
                attentionDayData:[],
                infoDayData:[],
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
            // changeToInfo(){
            //     document.getElementById("info_div").style.display = "block";
            //     document.getElementById("attention_div").style.display = "none";
            // },
            // changeToAttention(){
            //     document.getElementById("info_div").style.display = "none";
            //     document.getElementById("attention_div").style.display = "block";
            // },
            init () {
                this.optionA={
                    xAxis: {
                        type: 'category',
                        data: this.timeData,
                        splitLine:{show: false},
                        axisLabel: {
                            color: "white",
                            rotate: 25,
                            fontWeight: "lighter",
                        },

                        axisLine: {
                            symbol: ['none', 'arrow'],
                            symbolSize: [10, 10],
                            symbolOffset: [0, 5],
                            lineStyle: {
                                color: 'lightblue'
                            }
                        }
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['信息量走势', '关注度走势']
                    },
                    color: [
                        "#867fde",
                        "#fac6ad",
                        "#a4ee90",
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            name: '信息量走势',
                            splitLine:{show: false},
                            axisLabel: {
                                color: "white",
                                fontWeight: "lighter",
                        },
                            axisLine: {
                                symbol: ['none', 'arrow'],
                                symbolSize: [10, 10],
                                symbolOffset: [0, 5],
                                lineStyle: {
                                    color: 'lightblue'
                                }
                            }
                    },
                        {
                            type: 'value',
                            name: '关注度走势',
                            splitLine:{show: false},
                            axisLabel: {
                                color: "white",
                                fontWeight: "lighter",
                            },
                            axisLine: {
                                symbol: ['none', 'arrow'],
                                symbolSize: [10, 10],
                                symbolOffset: [0, 5],
                                lineStyle: {
                                    color: 'lightblue'
                                }
                            }
                        }
                    ],
                    series: [
                        {
                            name: '信息量走势',
                            data: this.infoData,
                            type: 'line',
                            yAxisIndex: 0,//相对应的坐标轴
                        },
                        {
                            name: '关注度走势',
                            data: this.attentionData,
                            type: 'line',
                            yAxisIndex: 1,//相对应的坐标轴
                        }
                    ]
                };
                this.optionB={
                    xAxis: {
                        type: 'category',
                        data: this.timeDayData,
                        splitLine:{show: false},
                        axisLabel: {
                            color: "white",
                            rotate: 25,
                            fontWeight: "lighter",
                        },

                        axisLine: {
                            symbol: ['none', 'arrow'],
                            symbolSize: [10, 10],
                            symbolOffset: [0, 5],
                            lineStyle: {
                                color: 'lightblue'
                            }
                        }
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['信息量走势', '关注度走势']
                    },
                    color: [
                        "#867fde",
                        "#fac6ad",
                        "#a4ee90",
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            name: '信息量走势',
                            splitLine:{show: false},
                            axisLabel: {
                                color: "white",
                                fontWeight: "lighter",
                            },
                            axisLine: {
                                symbol: ['none', 'arrow'],
                                symbolSize: [10, 10],
                                symbolOffset: [0, 5],
                                lineStyle: {
                                    color: 'lightblue'
                                }
                            }
                        },
                        {
                            type: 'value',
                            name: '关注度走势',
                            splitLine:{show: false},
                            axisLabel: {
                                color: "white",
                                fontWeight: "lighter",
                            },
                            axisLine: {
                                symbol: ['none', 'arrow'],
                                symbolSize: [10, 10],
                                symbolOffset: [0, 5],
                                lineStyle: {
                                    color: 'lightblue'
                                }
                            }
                        }
                    ],
                    series: [
                        {
                            name: '信息量走势',
                            data: this.infoDayData,
                            type: 'line',
                            yAxisIndex: 0,//相对应的坐标轴
                        },
                        {
                            name: '关注度走势',
                            data: this.attentionDayData,
                            type: 'line',
                            yAxisIndex: 1,//相对应的坐标轴
                        }
                    ]
                };
                this.draw();
            },
            draw(){
                this.$echarts.init(document.getElementById('trend')).setOption(this.optionA);
            },
            replaceGraphA(){
                this.$echarts.init(document.getElementById("trend")).setOption(this.optionA)
            },
            replaceGraphB(){
                this.$echarts.init(document.getElementById("trend")).setOption(this.optionB)
            },
            async getData(){
                const that = this;
                await axios.get('/event/eventTrend',{
                    params: {
                        eventName: this.eventName
                    }
                }).then(function (response) {
                    if (response.data) {
                        var data=response.data
                        that.timeData = data["times"]
                        that.attentionData = data["attentions"]
                        that.infoData = data["infos"]
                        that.timeDayData = data["times_day"]
                        that.attentionDayData = data["attentions_day"]
                        that.infoDayData = data["infos_day"]
                        console.log("信息量走势"+JSON.stringify(data))

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
    #div_box {
        display: block;
        padding: 0.2rem 0.2rem 0;
        width: 11.8rem;
        height: 6rem;
    }

    #trend {
        display: block;
        width: 11.2rem;
        height: 5rem;
    }

    /*#info_div {*/
    /*    !*background-color: #4b67af;*!*/
    /*    display: block;*/
    /*    width: 11.5rem;*/
    /*    height: 5.5rem;*/
    /*}*/
    /*#attention_div {*/
    /*    !*background-color: lightgreen;*!*/
    /*    display: none;*/
    /*    width: 11.5rem;*/
    /*    height: 5.5rem;*/
    /*}*/

    /*/deep/ .el-option {*/
    /*    font-weight: lighter;*/
    /*    font-family: "Microsoft YaHei UI", sans-serif;*/
    /*}*/

    /*/deep/ .el-select-dropdown {*/
    /*    background-color: white;*/
    /*}*/
    /*/deep/ .el-select-dropdown__item {*/
    /*    background-color: transparent;*/
    /*}*/
    /*/deep/ .el-select-dropdown__item:hover{*/
    /*    background-color: lightblue;*/
    /*}*/
</style>