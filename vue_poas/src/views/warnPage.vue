<template>
    <div>
        <el-row>
            <el-col :span="12" style="width: 40%">
                <div class="grid-content bg-purple">
                    <div id="box_11">
                        <span class="text" style="display: block; font-size: medium; margin-top: 0.1rem; margin-bottom: 0.1rem">
                            七天内预警事件
<!--                            <i class="el-icon-question"></i>-->
                        </span>
                        <div class="event_box" style="width: 100%">
                            <el-table
                                    :data="eventWarnData"
                                    :row-class-name="tableRowClassName"
                                    width="100%"
                                    @row-click="openDetails1"
                                    style="cursor: pointer"
                                    height="5.4rem"
                                    border>
                                <el-table-column
                                        prop="eventName"
                                        label="事件名称"
                                        width="">
                                </el-table-column>
                                <!--                    <el-table-column-->
                                <!--                        prop="negArticleProportion"-->
                                <!--                        label="事件负面文章占比"-->
                                <!--                        width="">-->
                                <!--                    </el-table-column>-->
                                <el-table-column
                                        prop="negCommentProportion"
                                        sortable
                                        label="事件负面评论占比"
                                        width="">
                                </el-table-column>
                                <el-table-column
                                        prop="rate"
                                        sortable
                                        label="负面舆论环比增长率"
                                        width="">
                                    <template slot-scope="{row}">
                                        <p v-if="row.rate>0" v-text="((row.rate*1000)*(100*1000))/1000000+'%'" style="display: inline; color: red"></p>
                                        <p v-else-if="row.rate<0" v-text="((row.rate*1000)*(100*1000))/1000000+'%'" style="display: inline; color: green"></p>
                                        <p v-else-if="row.rate===0"></p>
                                        <i v-if="row.rate>0" class="el-icon-top"></i>
                                        <i v-else-if="row.rate<0" class="el-icon-bottom"></i>
                                        <i v-else-if="row.rate===0"> - </i>
                                        </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                    <div id="box_12">
                        <span class="text" style="display: block; font-size: medium; margin-top: 0.1rem; margin-bottom: 0.1rem">
                            事件热度及热度增长率
<!--                            <i class="el-icon-question"></i>-->
                        </span>
                        <div class="event_box" style="width: 100%">
                            <el-table
                                    :data="eventRate"
                                    width="100%"
                                    height="5.4rem"
                                    style="cursor: pointer"
                                    @row-click="openDetails"
                                    border>
                                <el-table-column
                                        prop="name"
                                        sortable
                                        label="事件名称"
                                        width="">
                                </el-table-column>
                                <el-table-column
                                        prop="eventHot"
                                        sortable
                                        label="事件热度"
                                        width="">
                                </el-table-column>
                                <!--                    <el-table-column-->
                                <!--                        prop="negArticleProportion"-->
                                <!--                        label="事件负面文章占比"-->
                                <!--                        width="">-->
                                <!--                    </el-table-column>-->
                                <el-table-column
                                        prop="value"
                                        sortable
                                        label="增长率"
                                        width="">
                                    <template slot-scope="{row}">
                                        <p v-if="row.value>0" v-text="((row.value*1000)*(100*1000))/1000000+'%'" style="display: inline; color: red"></p>
                                        <p v-else-if="row.value<0" v-text="((row.value*1000)*(100*1000))/1000000+'%'" style="display: inline; color: green"></p>
                                        <p v-else-if="row.value===0"></p>
                                        <i v-if="row.value>0" class="el-icon-top"></i>
                                        <i v-else-if="row.value<0" class="el-icon-bottom"></i>
                                        <i v-else-if="row.value===0"> - </i>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                </div>
            </el-col>
            <el-col :span="12" style="width: 60%">
                <div class="grid-content bg-purple-light">
                    <div id="box_2" style="width: 100%;margin-top: 0.1rem">
                        <span class="text" style="display: block; font-size: medium">
                            事件负面评论占比TopN与事件负面评论占比环比增长TopN
<!--                            <i class="el-icon-question" ></i>-->
                        </span>
                        <div style="border:1px solid #ffffff; margin-top: 0.1rem; height: 5.41rem">
                            <div id="box_22" ></div>
                            <div id="box_23" ></div>
                        </div>
                    </div>
                    <div id="box_3" style="width:100%;margin: 0.1rem;margin-top: 0.44rem">
                        <span class="text" style="display: block; font-size: medium">
                            事件热度增长预警TopN
<!--                            <i class="el-icon-question" ></i>-->
                        </span>
                        <div style=" border:1px solid #ffffff; height: 5.41rem; margin-top: 0.1rem">
                            <div id="box_31" ></div>
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import * as echarts from "echarts";
    import axios from "axios";

    export default {
        name: "eventWarn",
        mounted() {
            this.getWarnEvent()
        },
        data() {
            return {
                eventWarnData: [],
                eventWarnTopN: [],
                eventWarnRateTopN:[],
                yeventRate:[],
                eventRate:[],
                xeventRate:[],
                timer: null
            }
        },
        created() {
            this.timer = setInterval(this.getWarnEvent, 30000)
        },
        beforeDestroy() {
            clearInterval(this.timer);
            this.timer = null
        },
        methods: {
            openDetails1 (row) {
                this.$router.push({
                    path:"/eventPage",
                    query:{
                        eventName: row.eventName
                    }});
            },
            openDetails (row) {
                this.$router.push({
                    path:"/eventPage",
                    query:{
                        eventName: row.name
                    }});
            },
            tableRowClassName(rowIndex) {
                if (rowIndex === 1) {
                    return {
                        backgroundColor: 'red'
                    }
                } else if (rowIndex === 2) {
                    return {
                        backgroundColor: 'red'
                    }
                } else if (rowIndex === 3) {
                    return {
                        backgroundColor: 'red'
                    }
                }
                return '';
            },
            async getWarnEvent(){
                const that = this;
                await axios.get('/eventComment/warnEvent').then(function (response) {
                    console.log(response.data);
                    if (response.data) {
                        that.eventWarnData = response.data;
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                await axios.get('/eventComment/warnEventTopN').then(function (response) {
                    console.log(response.data);
                    if (response.data) {
                        that.eventWarnTopN = response.data[0];
                        that.eventWarnRateTopN = response.data[1];
                        // console.log(that.eventWarnRateTopN)
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                await axios.get('/event/eventHotWarning').then(function (response) {
                    console.log(response.data);
                    if (response.data) {
                        that.eventRate = response.data;
                        that.xeventRate = []
                        that.yeventRate = []
                        for(var i=6;i>=0;i--){
                            that.xeventRate.push(that.eventRate[i].name);
                            that.yeventRate.push(that.eventRate[i]);
                        }
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                this.draw();
            },
            draw() {
                this.pieOptionB={
                    tooltip: {
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    legend: {
                        top: '10%',
                        type: 'scroll',
                        icon: 'circle',
                        textStyle: {
                            color: 'white',
                            fontSize: 10,
                            fontWeight: 'lighter',
                            fontFamily: 'MicroSoft YaHei'
                        }
                    },
                    grid: {
                        top:'0%',
                    },
                    color: [
                        "#5470c6",
                        "#91cc75",
                        "#fac858",
                        "#ee6666",
                        "#73c0de",
                        "#3ba272",
                        "#fc8452",
                        "#9a60b4",
                        "#ea7ccc"],
                    series: [
                        {
                            name: '',
                            type: 'pie',
                            radius: '30%',
                            center: ['50%', '60%'],
                            roseType: 'radius', // area;
                            itemStyle: {
                                borderRadius: 20,
                                normal:{
                                    label:{
                                        show: true,
                                        formatter: '{b}\n{c}',
                                        textStyle: {
                                            fontSize: 9
                                        }
                                    },
                                    labelLine :{show:true}
                                }
                            },
                            data: this.eventWarnTopN,
                            // data: [
                            //     {value: 98.1, name: 'A事件'},
                            //     {value: 91.3, name: 'B事件'},
                            //     {value: 87.1, name: 'C事件'},
                            //     {value: 77.2, name: 'X事件'},
                            //     {value: 65.2, name: 'Y事件'},
                            //     {value: 54.1, name: 'Z事件'},
                            // ]
                        }
                    ]
                };
                this.pieOptionA={
                    grid: {
                        top:'0%'
                    },
                    legend: {
                        top: '10%',
                        type: 'scroll',
                        icon: 'circle',
                        textStyle: {
                            color: 'white',
                            fontSize: 10,
                            fontWeight: 'lighter',
                            fontFamily: 'MicroSoft YaHei'
                        }
                    },
                    tooltip: {
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    color: [
                        "#5470c6",
                        "#91cc75",
                        "#fac858",
                        "#ee6666",
                        "#73c0de",
                        "#3ba272",
                        "#fc8452",
                        "#9a60b4",
                        "#ea7ccc"],
                    series: [
                        {
                            name: '',
                            type: 'pie',
                            radius: '30%',
                            center: ['50%', '60%'],
                            roseType: 'radius', // area;
                            itemStyle: {
                                borderRadius: 20,
                                normal:{
                                    label:{
                                        show: true,
                                        formatter: '{b}\n{c}',
                                        textStyle: {
                                            fontSize: 9
                                        }
                                    },
                                    labelLine :{show:true}
                                }
                            },
                            data: this.eventWarnRateTopN,
                            // data: [
                            //     {value: 98.1, name: 'A事件'},
                            //     {value: 91.3, name: 'B事件'},
                            //     {value: 87.1, name: 'C事件'},
                            //     {value: 77.2, name: 'X事件'},
                            //     {value: 65.2, name: 'Y事件'},
                            //     {value: 54.1, name: 'Z事件'},
                            // ]
                        }
                    ]
                };
                this.barOptionB={
                    grid: {
                        top: '2%',
                        // left: '2%',
                        // right: '2%',
                        bottom: '50%',
                        containLabel: true
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    xAxis: {
                        type: 'value'
                    },
                    yAxis: {
                        name: '',
                        type: 'category',
                        data: this.xeventRate,
                        axisLabel: {
                            color: "#ee6666"
                        }
                    },
                    color: ["#f80202"],
                    series: [
                        {
                            name: '',
                            type: 'bar',
                            data: this.yeventRate,
                            barWidth:20,
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true, //开启显示
                                        position: 'right', //在上方显示
                                        textStyle: { //数值样式
                                            color: 'white',
                                            fontSize: 16
                                        }
                                    }
                                }
                            }
                            // data: [
                            //     {value: 98.1, name: 'A事件'},
                            //     {value: 91.3, name: 'B事件'},
                            //     {value: 87.1, name: 'C事件'},
                            //     {value: 77.2, name: 'X事件'},
                            //     {value: 65.2, name: 'Y事件'},
                            //     {value: 54.1, name: 'Z事件'},
                            // ]
                        }
                    ]
                };
                echarts.init(document.getElementById("box_23")).setOption(this.pieOptionA);
                echarts.init(document.getElementById("box_22")).setOption(this.pieOptionB);
                echarts.init(document.getElementById("box_31")).setOption(this.barOptionB);
            },
        }
    }
</script>

<style lang="scss">
    ::-webkit-scrollbar{width:0.1rem;}
    ::-webkit-scrollbar-track{background-color: #909393;}
    ::-webkit-scrollbar-thumb{background-color: #161617;}
    ::-webkit-scrollbar-thumb:hover {background-color:#9c3}
    ::-webkit-scrollbar-thumb:active {background-color: #0f0f10
    }
    .down-arrow {
        color: #47da2a;
    }
    .up-arrow {
        color: #d64e18;
        transform: rotate(180deg);
    }
    #top_box {
        display: flex;
    }

    #box_11, #box_12 {
        width: 100%;
    }
    /*#query_box {*/
    /*    margin-left: 2.5rem;*/
    /*    margin-bottom: 0.2rem;*/
    /*}*/
    .event_box {
        margin-top: 0.1rem;
    }

    #box_2 {
        height: 5.4rem;
        width: 10rem;
        margin: 0.1rem;
    }
    #box_21 {
        /*background-color: #8cc5ff;*/
        height: 10rem;
        width: 10rem;
    }
    #box_22 {
        /*background-color: #8cc5ff;*/
        margin-top: 0.1rem;
        height: 5.3rem;
        width: 48%;
        float:left;
        margin-right: 0.4rem;
    }
    #box_23 {
        /*background-color: #8cc5ff;*/
        margin-top: 0.1rem;
        height: 5.3rem;
        width: 48%;
        float:left;
    }
    #box_31 {
        /*background-color: #8cc5ff;*/
        margin-left: 1rem;
        height: 10rem;
        width: 12rem;
    }

    .el-input {
        background-color: transparent !important;
    }
    .el-input__inner {
        background-color: transparent !important;
    }

    /* 表格透明 */
    .el-table, .el-table__expanded-cell {
        background-color: transparent !important;
    }
    .el-table th,
    .el-table tr,
    .el-table td {
        background-color: transparent !important;
    }

    .el-tabs {
        background-color: transparent !important;
    }
    .el-tabs__header {
        background-color: transparent !important;
    }
    .el-tabs__nav-warp {
        background-color: transparent !important;
    }
    .el-tabs__active-bar {
        background-color: transparent !important;
    }
    .el-tabs__item {
        background-color: transparent !important;
    }

    .el-icon-top{
        color: red;
    }
    .el-icon-bottom{
        color: green;
    }
</style>
