<!--<template>-->
<!--    <div id="age_graph">-->
<!--        <span class="text" style="margin-bottom: 10px">评论者年龄分布</span>-->
<!--        <div id="age_div"></div>-->
<!--    </div>-->
<!--</template>-->

<!--<script>-->
<!--    export default {-->
<!--        name: "eventCommentAge",-->
<!--        mounted() {-->
<!--            this.init()-->
<!--        },-->
<!--        methods: {-->
<!--            init(){-->
<!--                this.ageOption = {-->
<!--                    color: ['#409EFF'],-->
<!--                    // grid: {-->
<!--                    //     containLabel: true,-->
<!--                    // },-->
<!--                    xAxis: {-->
<!--                        name: '',-->
<!--                        type: 'category',-->
<!--                        data: ["0-18", "18-36", "36-54", '54-72', '72+'],-->
<!--                        boundaryGap: true,-->
<!--                        axisLabel: {-->
<!--                            color: "white",-->
<!--                            fontWeight: "lighter",-->
<!--                            fontFamily: 'Microsoft YaHei, Arial, Verdana, sans-serif'-->
<!--                        },-->
<!--                        axisLine: {-->
<!--                            symbol: ['none', 'arrow'],-->
<!--                            symbolSize: [10, 10],-->
<!--                            symbolOffset: [0, 5],-->
<!--                            lineStyle: {-->
<!--                                color: 'lightblue'-->
<!--                            }-->
<!--                        },-->
<!--                        axisTick: {-->
<!--                            inside: true-->
<!--                        }-->
<!--                    },-->
<!--                    yAxis: {-->
<!--                        type: 'value',-->
<!--                        axisLabel: {-->
<!--                            color: "white",-->
<!--                            fontWeight: "lighter",-->
<!--                            fontFamily: 'Microsoft YaHei'-->
<!--                        },-->
<!--                        axisLine: {-->
<!--                            symbol: ['none', 'arrow'],-->
<!--                            symbolSize: [10, 10],-->
<!--                            symbolOffset: [0, 5],-->
<!--                            lineStyle: {-->
<!--                                color: 'lightblue'-->
<!--                            }-->
<!--                        },-->
<!--                        axisTick: {-->
<!--                            inside: true,-->
<!--                            length: 5-->
<!--                        },-->
<!--                        splitLine: {-->
<!--                            show: true,-->
<!--                            lineStyle: 'lightblue'-->
<!--                        }-->
<!--                    },-->
<!--                    series: [{-->
<!--                        data: [512, 1421, 2078, 1012, 231],-->
<!--                        type: 'bar',-->
<!--                        barWidth: 25-->
<!--                    }]-->
<!--                };-->
<!--                this.$echarts.init(document.getElementById("age_div")).setOption(this.ageOption);-->
<!--            }-->
<!--        }-->
<!--    }-->
<!--</script>-->

<!--<style scoped>-->
<!--    #age_graph {-->
<!--        padding: 0.2rem 0.2rem 0;-->
<!--        width: 5rem;-->
<!--        height: 4.8rem;-->
<!--    }-->
<!--    #age_div {-->
<!--        background-color: white;-->
<!--        width: 5rem;-->
<!--        height: 4.4rem;-->
<!--    }-->
<!--</style>-->