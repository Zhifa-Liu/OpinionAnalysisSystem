<!--<template>-->
<!--    <div id="region_graph">-->
<!--        <span class="text" style="margin-bottom: 10px">评论者地域分析</span>-->
<!--        <div id="graph_1"></div>-->
<!--    </div>-->
<!--</template>-->

<!--<script>-->
<!--    import * as echarts from "echarts";-->
<!--    import 'echarts/map/js/china'-->

<!--    export default {-->
<!--        name: "eventCommentRegion",-->
<!--        mounted () {-->
<!--            this.init()-->
<!--        },-->
<!--        methods: {-->
<!--            init(){-->
<!--                var region_container = document.getElementById('graph_1');-->
<!--                var comment_region = echarts.init(region_container);-->
<!--                var optionMap = {-->
<!--                    tooltip: {},-->
<!--                    visualMap: {-->
<!--                        show: false,-->
<!--                        min: 0,-->
<!--                        max: 500,-->
<!--                        left: '10%',-->
<!--                        top: 'bottom',-->
<!--                        text: ['高', '低'],-->
<!--                        calculable: true,-->
<!--                        color: ['#0b50b9', '#c3e2f4']-->
<!--                    },-->
<!--                    selectedMode: 'single',-->
<!--                    series: [-->
<!--                        {-->
<!--                            name: '',-->
<!--                            zoom: 0.8,-->
<!--                            type: 'map',-->
<!--                            mapType: 'china',-->
<!--                            itemStyle: {-->
<!--                                normal: {-->
<!--                                    borderColor: 'rgba(0, 0, 0, 0.2)'-->
<!--                                },-->
<!--                                emphasis: {-->
<!--                                    areaColor: 'blue',-->
<!--                                    shadowOffsetX: 0,-->
<!--                                    shadowOffsetY: 0,-->
<!--                                    shadowBlur: 20,-->
<!--                                    borderWidth: 0,-->
<!--                                    shadowColor: 'rgba(0, 0, 0, 0.5)'-->
<!--                                }-->
<!--                            },-->
<!--                            showLegendSymbol: true,-->
<!--                            label: {-->
<!--                                // normal: {-->
<!--                                //     show: true-->
<!--                                // },-->
<!--                                emphasis: {-->
<!--                                    show: true-->
<!--                                }-->
<!--                            },-->
<!--                            data: [-->
<!--                                { name: '北京', value: this.randomData() },-->
<!--                                { name: '天津', value: this.randomData() },-->
<!--                                { name: '上海', value: this.randomData() },-->
<!--                                { name: '重庆', value: this.randomData() },-->
<!--                                { name: '河北', value: this.randomData() },-->
<!--                                { name: '河南', value: this.randomData() },-->
<!--                                { name: '云南', value: this.randomData() },-->
<!--                                { name: '辽宁', value: this.randomData() },-->
<!--                                { name: '黑龙江', value: this.randomData() },-->
<!--                                { name: '湖南', value: this.randomData() },-->
<!--                                { name: '安徽', value: this.randomData() },-->
<!--                                { name: '山东', value: this.randomData() },-->
<!--                                { name: '新疆', value: this.randomData() },-->
<!--                                { name: '江苏', value: this.randomData() },-->
<!--                                { name: '浙江', value: this.randomData() },-->
<!--                                { name: '江西', value: this.randomData() },-->
<!--                                { name: '湖北', value: this.randomData() },-->
<!--                                { name: '广西', value: this.randomData() },-->
<!--                                { name: '甘肃', value: this.randomData() },-->
<!--                                { name: '山西', value: this.randomData() },-->
<!--                                { name: '内蒙古', value: this.randomData() },-->
<!--                                { name: '陕西', value: this.randomData() },-->
<!--                                { name: '吉林', value: this.randomData() },-->
<!--                                { name: '福建', value: this.randomData() },-->
<!--                                { name: '贵州', value: this.randomData() },-->
<!--                                { name: '广东', value: this.randomData() },-->
<!--                                { name: '青海', value: this.randomData() },-->
<!--                                { name: '西藏', value: this.randomData() },-->
<!--                                { name: '四川', value: this.randomData() },-->
<!--                                { name: '宁夏', value: this.randomData() },-->
<!--                                { name: '海南', value: this.randomData() },-->
<!--                                { name: '台湾', value: this.randomData() },-->
<!--                                { name: '香港', value: this.randomData() },-->
<!--                                { name: '澳门', value: this.randomData() }-->
<!--                            ]-->
<!--                        }-->
<!--                    ]-->
<!--                };-->
<!--                comment_region.setOption(optionMap);-->
<!--            },-->
<!--            randomData () {-->
<!--                return Math.round(Math.random() * 500)-->
<!--            }-->
<!--        }-->
<!--    }-->
<!--</script>-->

<!--<style scoped>-->
<!--    #region_graph {-->
<!--        padding: 0.2rem 0.36rem 0;-->
<!--    }-->

<!--    #graph_1 {-->
<!--        border-color: white;-->
<!--        height: 5.8rem;-->
<!--        width: 6rem;-->
<!--    }-->
<!--</style>-->