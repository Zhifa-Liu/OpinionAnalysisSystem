<template>
    <div id="index">
        <dv-full-screen-container class="bg">
            <dv-loading v-if="loading">Loading...</dv-loading>
            <div v-else class="host-body" style="margin-bottom: 0rem">
                <div class="d-flex jc-center">
                    <dv-decoration-10 style="width:33.3%;height:.0625rem;"/>
                    <div class="d-flex jc-center">
                        <dv-decoration-8 :color="['#568aea', '#000000']" style="width:2.5rem;height:.625rem;"/>
                        <div class="title">
                            <span class="title-text">舆情分析系统</span>
                            <dv-decoration-6
                                    class="title-bottom"
                                    :reverse="true"
                                    :color="['#50e3c2', '#67a1e5']"
                                    style="width:3.125rem;height:.1rem;"
                            />
                        </div>
                        <dv-decoration-8
                                :reverse="true"
                                :color="['#568aea', '#000000']"
                                style="width:2.5rem;height:.625rem;"
                        />
                    </div>
                    <dv-decoration-10 style="width:33.3%;height:.0625rem; transform: rotateY(180deg);"/>
                </div>

                <div class="d-flex jc-between px-2">
                    <div class="d-flex" style="width: 40%">
                        <div
                                class="react-right bg-color-blue ml-4"
                                style="width: 6.25rem; text-align: left;"
                        >
                            <span class="react-before"></span>
                            <span class="text page_name">系统管理页</span>
                        </div>
                        <div class="react-right bg-color-blue ml-3">
                            <span class="text page_name"></span>
                        </div>
                    </div>
                    <div class="d-flex" style="width: 40%">
                        <div class="react-left bg-color-blue mr-3">
                            <router-link to="/">
                                <span class="text page_name">退出</span>
                            </router-link>
                        </div>
                        <div
                                class="react-left bg-color-blue mr-4"
                                style="width: 6.25rem; text-align: right;"
                        >
                            <span class="react-after"></span>
                            <span class="text">{{dateYear}} {{dateWeek}} {{dateDay}}</span>
                        </div>
                    </div>
                </div>

                <div class="body-box" >
                    <el-container>
                        <el-main>
                            <el-tabs tab-position="left" style="height: 22rem;align-items: center">
                                <el-tab-pane label="系统运行情况">
                                    <system-page></system-page>
                                </el-tab-pane>
                                <el-tab-pane label="TopN 配置">
                                    <rank-value-set-page></rank-value-set-page>
                                </el-tab-pane>
                                <el-tab-pane label="事件预警阈值设置">
                                    <warn-min-value-page></warn-min-value-page>
                                </el-tab-pane>
                                <el-tab-pane label="爬虫参数配置">
                                    <reptile-param-page></reptile-param-page>
                                </el-tab-pane>
<!--                                <el-tab-pane label="Kafka参数配置">-->
<!--                                    <kafka-param-page></kafka-param-page>-->
<!--                                </el-tab-pane>-->
<!--                                <el-tab-pane label="Flume参数配置">-->
<!--                                    <flume-param-page></flume-param-page>-->
<!--                                </el-tab-pane>-->
<!--                                <el-tab-pane label="Flink参数配置">-->
<!--                                    <flink-param-page></flink-param-page>-->
<!--                                </el-tab-pane>-->
                            </el-tabs>
                        </el-main>
                    </el-container>
                </div>
            </div>
        </dv-full-screen-container>
    </div>
</template>

<script>
    import {formatTime} from "../utils";
    import reptileParamPage from "./adminPage/reptileParamPage";
    // import flinkParamPage from "./adminPage/flinkParamPage";
    // import flumeParamPage from "./adminPage/flumeParamPage";
    // import kafkaParamPage from "./adminPage/kafkaParamPage";
    import systemPage from "./adminPage/systemPage";
    import warnMinValuePage from "./adminPage/warnMinValuePage";
    import rankValueSetPage from "./adminPage/rankValueSetPage";

    export default {
        name:'adminMainPage',
        components: {
            rankValueSetPage,
            warnMinValuePage,
            reptileParamPage,
            // flinkParamPage,
            // flumeParamPage,
            // kafkaParamPage,
            systemPage
        },
        data() {
            return {
                loading: true,
                dateDay: null,
                dateYear: null,
                dateWeek: null,
                weekday: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
            };
        },
        mounted() {
            this.timeFn();
            this.cancelLoading();
        },
        methods: {
            timeFn() {
                setInterval(() => {
                    this.dateDay = formatTime(new Date(), 'HH: mm: ss');
                    this.dateYear = formatTime(new Date(), 'yyyy-MM-dd');
                    this.dateWeek = this.weekday[new Date().getDay()];
                }, 1000)
            },
            cancelLoading() {
                setTimeout(() => {
                    this.loading = false;
                }, 500);
            },
        }
    };
</script>

<style lang="scss">
    @import '../assets/scss/globel';

    #main {
        display: block;
    }

    #event {
        display: none;
    }

    .page_name {
        color: white;
    }
</style>