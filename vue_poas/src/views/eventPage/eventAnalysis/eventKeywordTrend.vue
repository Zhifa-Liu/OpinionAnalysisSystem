<!--<template>-->
<!--    <div id="event_keyword_trend">-->
<!--        <span class="text">关键词趋势</span>-->
<!--&lt;!&ndash;        <div style="display: flex">&ndash;&gt;-->
<!--&lt;!&ndash;            <div style="margin-right: 13rem">&ndash;&gt;-->
<!--&lt;!&ndash;                <el-button-group>&ndash;&gt;-->
<!--&lt;!&ndash;                    <el-button type="primary" size="mini">近一日</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;                    <el-button type="primary" size="mini">近一周</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;                    <el-button type="primary" size="mini">近一月</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;                </el-button-group>&ndash;&gt;-->
<!--&lt;!&ndash;            </div>&ndash;&gt;-->
<!--&lt;!&ndash;            <div style="margin-top: 0.14rem">&ndash;&gt;-->
<!--&lt;!&ndash;                <el-date-picker&ndash;&gt;-->
<!--&lt;!&ndash;                    v-model="v"&ndash;&gt;-->
<!--&lt;!&ndash;                    type="daterange"&ndash;&gt;-->
<!--&lt;!&ndash;                    size="mini"&ndash;&gt;-->
<!--&lt;!&ndash;                    range-separator="至"&ndash;&gt;-->
<!--&lt;!&ndash;                    start-placeholder="开始日期"&ndash;&gt;-->
<!--&lt;!&ndash;                    end-placeholder="结束日期">&ndash;&gt;-->
<!--&lt;!&ndash;                </el-date-picker>&ndash;&gt;-->
<!--&lt;!&ndash;            </div>&ndash;&gt;-->
<!--&lt;!&ndash;        </div>&ndash;&gt;-->
<!--        <div id="keyword_trend">-->

<!--        </div>-->
<!--    </div>-->
<!--</template>-->

<!--<script>-->
<!--    export default {-->
<!--        name: "eventKeywordTrend",-->
<!--        mounted() {-->
<!--            this.init()-->
<!--        },-->
<!--        methods: {-->
<!--            init(){-->
<!--                this.option={-->
<!--                    xAxis: {-->
<!--                        type: 'category',-->
<!--                        data: ['2021-05-21', '2021-05-22', '2021-05-23', '2021-05-24', '2021-05-25', '2021-05-26', '2021-05-27', ],-->
<!--                        axisLabel: {-->
<!--                            color: "white",-->
<!--                            rotate: 25,-->
<!--                            fontWeight: "lighter",-->
<!--                        },-->
<!--                        axisLine: {-->
<!--                            symbol: ['none', 'arrow'],-->
<!--                            symbolSize: [10, 10],-->
<!--                            symbolOffset: [0, 5],-->
<!--                            lineStyle: {-->
<!--                                color: 'lightblue'-->
<!--                            }-->
<!--                        }-->
<!--                    },-->
<!--                    legend: {-->
<!--                        data: ['甘肃', '马拉松', '事故', '运动员', '救援']-->
<!--                    },-->
<!--                    tooltip: {-->
<!--                        trigger: 'axis'-->
<!--                    },-->
<!--                    color: [-->
<!--                        "#867fde",-->
<!--                        "#fac6ad",-->
<!--                        "#a4ee90",-->
<!--                    ],-->
<!--                    yAxis: {-->
<!--                        type: 'value',-->
<!--                        axisLabel: {-->
<!--                            color: "white",-->
<!--                            fontWeight: "lighter",-->
<!--                        },-->
<!--                        axisLine: {-->
<!--                            symbol: ['none', 'arrow'],-->
<!--                            symbolSize: [10, 10],-->
<!--                            symbolOffset: [0, 5],-->
<!--                            lineStyle: {-->
<!--                                color: 'lightblue'-->
<!--                            }-->
<!--                        }-->
<!--                    },-->
<!--                    series: [-->
<!--                        {-->
<!--                            name: '甘肃',-->
<!--                            data: [34, 76, 420, 1080, 990, 870, 450],-->
<!--                            type: 'line'-->
<!--                        },-->
<!--                        {-->
<!--                            name: '马拉松',-->
<!--                            data: [50, 108, 560, 980, 850, 440, 120],-->
<!--                            type: 'line'-->
<!--                        },-->
<!--                        {-->
<!--                            name: '事故',-->
<!--                            data: [23, 79, 230, 680, 460, 220, 20],-->
<!--                            type: 'line'-->
<!--                        },-->
<!--                        {-->
<!--                            name: '运动员',-->
<!--                            data: [21, 36, 129, 780, 365, 205, 107],-->
<!--                            type: 'line'-->
<!--                        },-->
<!--                        {-->
<!--                            name: '救援',-->
<!--                            data: [10, 46, 128, 680, 460, 320, 100],-->
<!--                            type: 'line'-->
<!--                        },-->
<!--                    ]-->
<!--                };-->
<!--                this.$echarts.init(document.getElementById('keyword_trend')).setOption(this.option);-->
<!--            }-->
<!--        }-->
<!--    }-->
<!--</script>-->

<!--<style scoped>-->
<!--    #event_keyword_trend {-->
<!--        padding: 0.2rem 0.36rem 0;-->
<!--        height: 5rem;-->
<!--        width: 23.8rem;-->
<!--    }-->
<!--    #keyword_trend {-->
<!--        height: 4.9rem;-->
<!--        width: 23.5rem;-->
<!--    }-->
<!--</style>-->