<template>
    <div id="comment_stat_data">
        <span class="text" style="margin-bottom: 10px;">文章正负面评论占比排名</span>
        <el-button-group>
            <el-button type="primary" size="mini" @click="replaceGraphA">Positive</el-button>
            <el-button type="primary" size="mini" @click="replaceGraphB">Negative</el-button>
        </el-button-group>
        <div id="pos_neg">
        </div>
    </div>
</template>

<script>
    export default {
        name: "eventCommentData",
        props: ['eventName'],
        mounted() {
            this.init()
        },
        methods: {
            init(){
                this.posOption = {
                    color: ['#409EFF'],
                    // grid: {
                    //     containLabel: true,
                    // },
                    xAxis: {
                        name: '',
                        type: 'category',
                        data: ["1", "3", "5", '7', '8'],
                        boundaryGap: true,
                        axisLabel: {
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
                        data: [0.98, 0.90, 0.88, 0.80, 0.77],
                        type: 'bar',
                        barWidth: 25
                    }]
                };
                this.negOption = {
                    color: ['#409EFF'],
                    // grid: {
                    //     containLabel: true,
                    // },
                    xAxis: {
                        name: '',
                        type: 'category',
                        data: ["1", "3", "5", '7', '8'],
                        boundaryGap: true,
                        axisLabel: {
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
                        data: [0.88, 0.70, 0.45, 0.30, 0.15],
                        type: 'bar',
                        barWidth: 25
                    }]
                };
                this.draw();
            },
            draw(){
                this.$echarts.init(document.getElementById("pos_neg")).setOption(this.posOption)
            },
            replaceGraphA(){
                this.$echarts.init(document.getElementById("pos_neg")).setOption(this.posOption)
            },
            replaceGraphB(){
                this.$echarts.init(document.getElementById("pos_neg")).setOption(this.negOption)
            },
        }
    }
</script>

<style lang="scss">
    #comment_stat_data {
        padding: 0.2rem 0.2rem 0;
        height: 4.8rem;
        width: 6rem;
        #pos_neg {
            height: 4.5rem;
            width: 5.5rem;
            /*background-color: white;*/
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
</style>