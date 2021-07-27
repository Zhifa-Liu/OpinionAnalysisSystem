<!--<template>-->
<!--    <div id="comment_rank">-->
<!--        <span class="text" style="margin-bottom: 10px;">评论排名</span>-->
<!--        <el-button-group>-->
<!--            <el-button type="primary" size="mini" @click="replaceGraphA">高赞评论</el-button>-->
<!--            <el-button type="primary" size="mini" @click="replaceGraphB">高回复评论</el-button>-->
<!--        </el-button-group>-->
<!--        <div id="rank_div">-->
<!--        </div>-->
<!--    </div>-->
<!--</template>-->

<!--<script>-->
<!--    export default {-->
<!--        name: "eventCommentRank",-->
<!--        mounted() {-->
<!--            this.init()-->
<!--        },-->
<!--        methods: {-->
<!--            init() {-->
<!--                this.likeOption={-->
<!--                    legend: {-->
<!--                        top: '10%',-->
<!--                        type: 'scroll',-->
<!--                        icon: 'circle',-->
<!--                        textStyle: {-->
<!--                            color: 'white',-->
<!--                            fontSize: 11,-->
<!--                            fontWeight: 'lighter',-->
<!--                            fontFamily: 'MicroSoft YaHei'-->
<!--                        }-->
<!--                    },-->
<!--                    color: [-->
<!--                        "#5470c6",-->
<!--                        "#91cc75",-->
<!--                        "#fac858",-->
<!--                        "#ee6666",-->
<!--                        "#73c0de",-->
<!--                        "#3ba272",-->
<!--                        "#fc8452",-->
<!--                        "#9a60b4",-->
<!--                        "#ea7ccc"],-->
<!--                    series: [-->
<!--                        {-->
<!--                            name: '文章声量排名',-->
<!--                            type: 'pie',-->
<!--                            radius: [20, 30],-->
<!--                            center: ['50%', '50%'],-->
<!--                            roseType: 'radius', // area;-->
<!--                            itemStyle: {-->
<!--                                borderRadius: 20,-->
<!--                                normal:{-->
<!--                                    // label:{-->
<!--                                    //     show: true,-->
<!--                                    //     formatter: '{b}@{c}'-->
<!--                                    // },-->
<!--                                    labelLine :{show:true}-->
<!--                                }-->
<!--                            },-->
<!--                            data: [-->
<!--                                {value: 112, name: 'XYZ'},-->
<!--                                {value: 201, name: 'ABC'},-->
<!--                                {value: 78, name: 'CBA'},-->
<!--                                {value: 102, name: 'NBA'},-->
<!--                                {value: 301, name: 'COCO'},-->
<!--                                {value: 87, name: 'MN'},-->
<!--                            ]-->
<!--                        }-->
<!--                    ]-->
<!--                };-->

<!--                this.replyOption={-->
<!--                    legend: {-->
<!--                        top: '10%',-->
<!--                        type: 'scroll',-->
<!--                        icon: 'circle',-->
<!--                        textStyle: {-->
<!--                            color: 'white',-->
<!--                            fontSize: 11,-->
<!--                            fontWeight: 'lighter',-->
<!--                            fontFamily: 'MicroSoft YaHei'-->
<!--                        }-->
<!--                    },-->
<!--                    color: [-->
<!--                        "#5470c6",-->
<!--                        "#91cc75",-->
<!--                        "#fac858",-->
<!--                        "#ee6666",-->
<!--                        "#73c0de",-->
<!--                        "#3ba272",-->
<!--                        "#fc8452",-->
<!--                        "#9a60b4",-->
<!--                        "#ea7ccc"],-->
<!--                    series: [-->
<!--                        {-->
<!--                            name: '文章声量排名',-->
<!--                            type: 'pie',-->
<!--                            radius: [20, 30],-->
<!--                            center: ['50%', '50%'],-->
<!--                            roseType: 'radius', // area;-->
<!--                            itemStyle: {-->
<!--                                borderRadius: 20,-->
<!--                                normal:{-->
<!--                                    // label:{-->
<!--                                    //     show: true,-->
<!--                                    //     formatter: '{b}@{c}'-->
<!--                                    // },-->
<!--                                    labelLine :{show:true}-->
<!--                                }-->
<!--                            },-->
<!--                            data: [-->
<!--                                {value: 112, name: 'abc'},-->
<!--                                {value: 201, name: 'xyz'},-->
<!--                                {value: 78, name: 'nba'},-->
<!--                                {value: 102, name: 'cba'},-->
<!--                                {value: 301, name: 'nb'},-->
<!--                                {value: 87, name: 'mhb'},-->
<!--                            ]-->
<!--                        }-->
<!--                    ]-->
<!--                };-->

<!--                this.draw();-->
<!--            },-->
<!--            draw(){-->
<!--                this.$echarts.init(document.getElementById("rank_div")).setOption(this.likeOption)-->
<!--            },-->
<!--            replaceGraphA(){-->
<!--                this.$echarts.init(document.getElementById("rank_div")).setOption(this.likeOption)-->
<!--            },-->
<!--            replaceGraphB(){-->
<!--                this.$echarts.init(document.getElementById("rank_div")).setOption(this.replyOption)-->
<!--            },-->
<!--        }-->
<!--    }-->
<!--</script>-->

<!--<style lang="scss">-->
<!--    #comment_rank {-->
<!--        padding: 0.2rem 0.2rem 0;-->
<!--        height: 4.8rem;-->
<!--        width: 6rem;-->
<!--        #rank_div {-->
<!--            height: 4.5rem;-->
<!--            width: 5.5rem;-->
<!--            /*background-color: white;*/-->
<!--        }-->
<!--    }-->

<!--    .el-button-group {-->
<!--        margin-left: 10px;-->
<!--        margin-top: 10px;-->
<!--        background-color: transparent;-->
<!--    }-->
<!--    .el-button {-->
<!--        background-color: transparent;-->
<!--        border-color: white;-->
<!--    }-->
<!--</style>-->