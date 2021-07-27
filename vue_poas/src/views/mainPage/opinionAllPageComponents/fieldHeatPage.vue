<template>
    <div id="fieldHeat">
        <span class="text" style="">领域热度时间变化曲线</span>
        <div id="heatChange" ></div>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "fieldHeat",
        mounted () {
            this.getData()
        },
        data(){
            return {
                tableData: [],
                socialData: [],
                affairData: [],
                idolData: [],
                sportsData: [],
                timeData: []
            }
        },
        methods: {
            init() {
                this.optionA = {
                    xAxis: {
                        type: 'category',
                        data: this.timeData,
                        axisLabel: {
                            color: "white",
                            rotate: 30,
                            fontWeight: "lighter",
                        }
                    },
                    tooltip: {
                        show: true, // 是否显示
                        trigger: 'axis', // 触发类型，默认数据触发，见下图，可选为：'item' | 'axis'
                    },
                    legend: {
                        data: ['社会', '政务','明星','体育'],
                        textStyle:{
                            fontSize: 12,//字体大小
                            color: '#ffffff'//字体颜色
                        }
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            color: "white",
                            fontWeight: "lighter",
                        }
                    },
                    series: [
                        {
                            name: '社会', // 系列名称
                            type: 'line', // 类型：线
                            symbol: 'emptydiamond',
                            data: this.socialData, // 数据
                        },
                        {
                            name: '政务', // 系列名称
                            type: 'line', // 类型：线
                            symbol: 'emptydiamond',
                            data: this.affairData, // 数据
                        },{
                            name: '明星', // 系列名称
                            type: 'line', // 类型：线
                            symbol: 'emptydiamond',
                            data: this.idolData, // 数据
                        },{
                            name: '体育', // 系列名称
                            type: 'line', // 类型：线
                            symbol: 'emptydiamond',
                            data: this.sportsData, // 数据
                        }
                    ]
                };
                this.$echarts.init(document.getElementById('heatChange')).setOption(this.optionA);
            },
            async getData(){
                const that = this;
                await axios.get('/event/eventTypeHot').then(function (response) {
                    if (response.data) {
                        var data=response.data
                        console.log("##############"+JSON.stringify(data))
                        for(var o in data){
                            if(that.timeData.indexOf(data[o].time) == -1){
                                that.timeData.push(data[o].time)

                            }
                            if(data[o].eventType == '社会'){
                                that.socialData.push(data[o].hot)
                            }else if(data[o].eventType == '政务'){
                                that.affairData.push(data[o].hot)
                            }else if(data[o].eventType == '明星'){
                                that.idolData.push(data[o].hot)
                            }else if(data[o].eventType == '体育'){
                                that.sportsData.push(data[o].hot)
                            }
                        }
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
    #fieldHeat{
        padding: 0.2rem 0.36rem 0;
        height: 5.5rem;
        width: 11.8rem;
        border-radius: 0.0625rem;
    }
    #heatChange {
        background-color: transparent;
        display: block;
        width: 11.5rem;
        height: 5rem;
    }
</style>