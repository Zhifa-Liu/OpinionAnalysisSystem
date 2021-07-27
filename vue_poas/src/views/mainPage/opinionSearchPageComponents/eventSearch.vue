<template>
    <div id="eventSearch">
        <div id="query_box">
            <el-input placeholder="输入事件关键词" style="width: 7rem" v-model="input"></el-input>
            <el-button type="primary" plain @click="eventQuery()" @keyup.enter="eventQuery()" style="margin-left: 30px">查询</el-button>
            <el-button type="danger" plain @click="clear()">清空</el-button>
        </div>
        <span class="text" style="display: block; font-size: medium; margin-top: 0.1rem; margin-bottom: 0.1rem">
            搜索结果
        </span>
        <div id="event_result">
            <el-table
                :data="eventSearchResult"
                width="100%"
                height="7rem"
                border
                style="border-radius: 15px;cursor: pointer"
                @row-click="openDetails">
                <el-table-column
                    prop="eventName"
                    label="事件名称"
                    width="">
                </el-table-column>
                <el-table-column
                    prop="eventHot"
                    label="事件热度"
                    sortable
                    width="">
                </el-table-column>
                <el-table-column
                    prop="eventType"
                    label="事件类型"
                    width="">
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "eventSearch",
        data() {
            return {
                input: '',
                eventSearchResult: [],
                // eventSearchResult: [
                //     {
                //         eventId: '1',
                //         eventName: '甘肃马拉松事件',
                //         eventHotDegree: 87.9
                //     },
                //     {
                //         eventId: '2',
                //         eventName: '重庆坠江事件',
                //         eventHotDegree: 81.9
                //     },
                //     {
                //         eventId: '3',
                //         eventName: 'XXX事件',
                //         eventHotDegree: 76.9
                //     },
                //     {
                //         eventId: '4',
                //         eventName: 'XXX事件',
                //         eventHotDegree: 65.1
                //     },
                //     {
                //         eventId: '5',
                //         eventName: 'XXX事件',
                //         eventHotDegree: 56.9
                //     },
                // ],
            }
        },
        mounted() {
            window.addEventListener("keydown", this.keyDown);
        },
        destroyed() {
            // 销毁事件
            window.removeEventListener("keydown", this.keyDown, false);
        },
        methods: {
            // 点击回车键登录
            keyDown(e) {
                // 回车则执行登录方法 enter键的ASCII是13
                if (e.keyCode === 13) {
                    this.eventQuery(); // 定义的登录方法
                }
            },
            openDetails (row) {
                this.$router.push({
                    path:"/eventPage",
                    query:{
                        eventName: row.eventName
                    }});
            },
            async eventQuery(){
                const that = this;
                await axios.get('/eventES/eventSearch', {
                    params: {
                        searchInput: this.input
                    }
                }).then(function (response) {
                    console.log(response.data);
                    if (response.data) {
                        that.eventSearchResult = response.data;
                        console.log(that.eventSearchResult)
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
            },
            clear(){

            }
        }
    }
</script>

<style lang="scss">
    #eventSearch {
        display: block;
    }

    #query_box {
        margin-left: 0.1rem;
        margin-bottom: 0.2rem;
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
</style>