<template>
    <div id="heatMap" >
        <span class="text" style="">全国实时舆情热力图</span>
        <div id="map1"></div>
    </div>
</template>

<script>
    import 'echarts/map/js/china'
    import echarts from "echarts";
    import axios from "axios";
    // var resdata=[]
    export default {
        name: "heatMap",
        mounted () {
            this.getData()
        },
        data(){
            return {
                tableData: []
            }
        },
    methods: {
        init() {
            var region_container = document.getElementById('map1');
            var comment_region = echarts.init(region_container);
            var optionMap = {
                tooltip: {},
                visualMap: {
                    show: false,
                    min: 0,
                    max: 200,
                    left: '10%',
                    top: 'bottom',
                    text: ['高', '低'],
                    calculable: true,
                    color: ['#0c3d92', '#c3e2f4']
                },
                selectedMode: 'single',
                series: [
                    {
                        name: '领域热力值',
                        zoom: 1.2,
                        type: 'map',
                        mapType: 'china',
                        itemStyle: {
                            normal: {
                                borderColor: 'rgba(0, 0, 0, 0.2)'
                            },
                            emphasis: {
                                areaColor: 'blue',
                                shadowOffsetX: 0,
                                shadowOffsetY: 0,
                                shadowBlur: 20,
                                borderWidth: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        },
                        showLegendSymbol: true,
                        label: {
                            // normal: {
                            //     show: true
                            // },
                            emphasis: {
                                show: true
                            }
                        },
                        data: [
                            {name: '北京',value: this.tableData["北京"]},
                            {name: '天津',value: this.tableData["天津"]},
                            {name: '上海',value: this.tableData["上海"]},
                            {name: '重庆',value: this.tableData["重庆"]},
                            {name: '河北',value: this.tableData["河北"]},
                            {name: '河南',value: this.tableData["河南"]},
                            {name: '云南',value: this.tableData["云南"]},
                            {name: '辽宁',value: this.tableData["辽宁"]},
                            {name: '黑龙江',value: this.tableData["黑龙江"]},
                            {name: '湖南',value: this.tableData["湖南"]},
                            {name: '安徽',value: this.tableData["安徽"]},
                            {name: '山东',value: this.tableData["山东"]},
                            {name: '新疆',value: this.tableData["新疆"]},
                            {name: '江苏',value: this.tableData["江苏"]},
                            {name: '浙江',value: this.tableData["浙江"]},
                            {name: '江西',value: this.tableData["江西"]},
                            {name: '湖北',value: this.tableData["湖北"]},
                            {name: '广西',value: this.tableData["广西"]},
                            {name: '甘肃',value: this.tableData["甘肃"]},
                            {name: '山西',value: this.tableData["山西"]},
                            {name: '内蒙古',value: this.tableData["内蒙古"]},
                            {name: '陕西',value: this.tableData["陕西"]},
                            {name: '吉林',value: this.tableData["吉林"]},
                            {name: '福建',value: this.tableData["福建"]},
                            {name: '贵州',value: this.tableData["贵州"]},
                            {name: '广东',value: this.tableData["广东"]},
                            {name: '青海',value: this.tableData["青海"]},
                            {name: '西藏',value: this.tableData["西藏"]},
                            {name: '四川',value: this.tableData["四川"]},
                            {name: '宁夏',value: this.tableData["宁夏"]},
                            {name: '海南',value: this.tableData["海南"]},
                            {name: '台湾',value: this.tableData["台湾"]},
                            {name: '香港',value: this.tableData["香港"]},
                            {name: '澳门',value: this.tableData["澳门"]}
                        ]
                    }
                ]
            };
            comment_region.setOption(optionMap);
        },
        randomData () {
            return Math.round(Math.random() * 500)
        },
        async getData() {
            const that = this;
            await axios.get('/event/allEventArticleRegion').then(function (response) {
                if (response.data) {
                    // alert(response.data)
                    // console.log("Hello Runoob!");
                    that.tableData = response.data
                    // resdata=JSON.parse(JSON.stringify(that.tableData));
                    console.log("运行记录组件接到的数据",that.tableData);
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
    #heatMap{
        padding: 0.2rem 0.36rem 0;
        height: 5.5rem;
        width: 11.7rem;
        border-radius: 0.0625rem;
    }
    #map1 {
        background-color: transparent;
        display: block;
        width: 11.5rem;
        height: 5rem;
    }
</style>