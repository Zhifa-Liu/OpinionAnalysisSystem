<template>
    <div id="hotField">
        <el-tabs type="border-card" style="color: transparent; border-radius: 15px">
            <el-tab-pane label="全部">
                <el-table
                        v-loading="loading"
                        element-loading-text="拼命加载中"
                        element-loading-spinner="el-icon-loading"
                        element-loading-background="rgba(0, 0, 0, 0.8)"
                        :data="tableData"
                        height="4rem"
                        border
                        style="border-radius: 15px; width: 96%; margin-left: 2%;cursor: pointer"
                        @row-click="openDetails"
                >
<!--                    <el-table-column-->
<!--                        prop="rank"-->
<!--                        label="排名">-->
<!--                    </el-table-column>-->
                    <el-table-column
                            type="index"
                            :index="indexMethod">
                    </el-table-column>
                    <el-table-column
                        prop="eventName"
                        label="事件名称"
                        width="500">
                    </el-table-column>
                    <el-table-column
                        prop="eventHot"
                        label="热度"
                        width="">
                    </el-table-column>
                    <el-table-column
                        prop="eventType"
                        label="事件类型"
                        width="">
                    </el-table-column>
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="社会">
                <el-table
                    :data="socialData"
                    height="4rem"
                    border
                    style="border-radius: 15px; width: 96%; margin-left: 2%;cursor: pointer"
                    @row-click="openDetails"
                >
<!--                    <el-table-column-->
<!--                        prop="rank"-->
<!--                        label="排名">-->
<!--                    </el-table-column>-->
                    <el-table-column
                            type="index"
                            :index="indexMethod">
                    </el-table-column>
                    <el-table-column
                        prop="eventName"
                        label="事件名称"
                        width="500">
                    </el-table-column>
                    <el-table-column
                        prop="eventHot"
                        label="热度指数"
                        width="">
                    </el-table-column>
                </el-table>
            </el-tab-pane >
            <el-tab-pane label="政务">
                <el-table
                    :data="affairData"
                    height="4rem"
                    border
                    style="border-radius: 15px; width: 96%; margin-left: 2%;cursor: pointer"
                    @row-click="openDetails"
                >
<!--                    <el-table-column-->
<!--                        prop="rank"-->
<!--                        label="排名">-->
<!--                    </el-table-column>-->
                    <el-table-column
                            type="index"
                            :index="indexMethod">
                    </el-table-column>
                    <el-table-column
                        prop="eventName"
                        label="事件名称"
                        width="500">
                    </el-table-column>
                    <el-table-column
                        prop="eventHot"
                        label="热度指数"
                        width="">
                    </el-table-column>
                </el-table>
            </el-tab-pane >
            <el-tab-pane label="明星">
                <el-table
                    :data="idolData"
                    height="4rem"
                    border
                    style="border-radius: 15px; width: 96%; margin-left: 2%;cursor: pointer"
                    @row-click="openDetails"
                >
<!--                    <el-table-column-->
<!--                        prop="rank"-->
<!--                        label="排名">-->
<!--                    </el-table-column>-->
                    <el-table-column
                            type="index"
                            :index="indexMethod">
                    </el-table-column>
                    <el-table-column
                        prop="eventName"
                        label="事件名称"
                        width="500">
                    </el-table-column>
                    <el-table-column
                        prop="eventHot"
                        label="热度指数"
                        width="">
                    </el-table-column>
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="体育">
                <el-table
                    :data="sportsData"
                    height="4rem"
                    border
                    style="border-radius: 15px; width: 96%; margin-left: 2%;cursor: pointer"
                    @row-click="openDetails"
                >
<!--                    <el-table-column-->
<!--                        prop="rank"-->
<!--                        label="排名">-->
<!--                    </el-table-column>-->
                    <el-table-column
                            type="index"
                            :index="indexMethod">
                    </el-table-column>
                    <el-table-column
                        prop="eventName"
                        label="事件名称"
                        width="500">
                    </el-table-column>
                    <el-table-column
                        prop="eventHot"
                        label="热度指数"
                        width="">
                    </el-table-column>
                </el-table>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "hotField",
        mounted () {
            this.getData()
        },
        data() {
            return {
                tableData: [],
                socialData: [],
                affairData: [],
                idolData: [],
                sportsData: [],
                loading:true
            }
        },
        methods: {
            init () {
            },
            indexMethod(index) {
                return index +1;
            },
            openDetails (row) {
                this.$router.push({
                    path:"/eventPage",
                    query:{
                        eventName: row.eventName
                    }});
            },
            async getData(){
                const that = this;
                await axios.get('/event/eventList').then(function (response) {
                    if (response.data) {
                        // alert(response.data)
                        // console.log("Hello Runoob!");
                        that.tableData=response.data
                        that.loading=false
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                await axios.get('/event/eventSubList?eventType=社会').then(function (response) {
                    if (response.data) {
                        // alert(response.data)
                        // console.log("Hello Runoob!");
                        console.log(response.data)
                        that.socialData=response.data
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                await axios.get('/event/eventSubList?eventType=政务').then(function (response) {
                    if (response.data) {
                        // alert(response.data)
                        // console.log("Hello Runoob!");
                        that.affairData=response.data
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                await axios.get('/event/eventSubList?eventType=明星').then(function (response) {
                    if (response.data) {
                        // alert(response.data)
                        // console.log("Hello Runoob!");
                        that.idolData=response.data
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                await axios.get('/event/eventSubList?eventType=体育').then(function (response) {
                    if (response.data) {
                        // alert(response.data)
                        // console.log("Hello Runoob!");
                        that.sportsData=response.data
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                this.init();
            }
        }
    }
</script>

<style lang="scss">
    #hotField{
        padding: 0.2rem;
        height: 5.6rem;
        width: 23.6rem;
        border-radius: 0.0625rem;
    }
    ::-webkit-scrollbar{width:0.1rem;}
    ::-webkit-scrollbar-track{background-color: #909393;}
    ::-webkit-scrollbar-thumb{background-color: #161617;}
    ::-webkit-scrollbar-thumb:hover {background-color:#9c3}
    ::-webkit-scrollbar-thumb:active {background-color: #0f0f10}

    /* 表格透明 */
    .el-table, .el-table__expanded-cell {
        background-color: transparent !important;
    }
    .el-table th,
    .el-table tr,
    .el-table td {
        background-color: transparent !important;
    }

    .el-table thead {
        color: #ffffff;
        font-weight: normal;
    }

    .el-table tbody {
        color: white;
        font-weight: lighter;
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

    .el-tab-pane {

    }
</style>
