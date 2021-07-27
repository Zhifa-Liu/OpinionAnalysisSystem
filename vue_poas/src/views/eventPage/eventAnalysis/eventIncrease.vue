<template>

    <div id="eventIncrease">
        <span class="text" style="display: block">事件关注度趋势增长曲线</span>
        <div id="increase">111</div>
    </div>
</template>

<script>
    import axios from "axios";
    export default {
        name: "eventIncrease",
        props: ['eventName'],
        mounted () {
            this.getData()
        },
        data(){
            return {
                time:[],
                eventAttentionIncrease:[],
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
                this.option = {
                    grid:{
                      left:"5%",
                      right:"5%"

                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: this.time,
                        axisLabel: {
                            color: "white",
                            fontWeight: "lighter",
                        },
                    },
                    yAxis: {
                        name:"增长率",
                        color: "white",
                        fontWeight: "lighter",
                        type: 'value',
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
                    series: [{
                        data: this.eventAttentionIncrease,
                        type: 'line',
                        areaStyle: {}
                    }]
                };
                this.draw();
            },
            draw(){
                this.$echarts.init(document.getElementById('increase')).setOption(this.option);
            },
            async getData() {
                const that = this;
                await axios.get('/event/eventTrendIncrease',{
                    params: {
                        eventName: this.eventName
                    }
                }).then(function (response) {
                    if (response.data) {
                        var eventTrend = response.data;
                        that.time=[];
                        that.eventAttentionIncrease=[];
                        for(let i=0;i<eventTrend.length;i++){
                            that.time.push(eventTrend[i].time)
                            that.eventAttentionIncrease.push(eventTrend[i].eventAttentionIncrease)
                        }
                        console.log("xyz"+that.eventAttentionIncrease)
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
    #eventIncrease {
        background-color: transparent !important;
        padding: 0.2rem 0.3rem 0;
        height: 4.2rem;
        width: 23.5rem;
        border-radius: 0.0625rem;
    }
    #increase {
        height: 4.2rem;
        width: 23.5rem;
    }

</style>