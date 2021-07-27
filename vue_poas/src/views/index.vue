<template>
    <div id="index">
        <dv-full-screen-container class="bg">
            <dv-loading v-if="loading">Loading...</dv-loading>
            <div v-else class="host-body">
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
                            @click="changeStyle(0)"
                        >
                            <span class="react-before"></span>
                            <router-link to="/mainPage" active-class="active" >
                                <div class="originalStyle">
                                    <span :class="{changeStyle:changeSelectStyle == 0}" >舆情首页</span>
                                </div>
<!--                                <span :class="{changeStyle:changeSelectStyle == 0}" @click="changeStyle(0)">舆情首页</span>-->
                            </router-link>
                        </div>
                        <div class="react-right bg-color-blue ml-3"
                             @click="changeStyle(1)">
                            <router-link to="/mainPage/opinionSearchPage" active-class="active">
                                <div class="originalStyle">
                                    <span :class="{changeStyle:changeSelectStyle == 1}">舆情搜索</span>
                                </div>
                            </router-link>
                        </div>
                        <div class="react-right bg-color-blue ml-3"
                             @click="changeStyle(2)">
                            <router-link to="/warnPage">
                                <div class="originalStyle">
                                    <span :class="{changeStyle:changeSelectStyle == 2}">舆情预警</span>
                                </div>
                            </router-link>
                        </div>
                    </div>
                    <div class="d-flex" style="width: 40%">
                        <div class="react-left bg-color-blue mr-3">
                            <router-link to="/">
                                <span class="text page_name">退出系统</span>
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

                <div class="body-box">
                    <router-view></router-view>
                </div>
            </div>
        </dv-full-screen-container>
    </div>
</template>

<script>
    import {formatTime} from "../utils";

    export default {
        components: {
        },
        data() {
            return {
                changeSelectStyle:0,
                loading: true,
                dateDay: null,
                dateYear: null,
                dateWeek: null,
                weekday: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
            };
        },
        created() {
            if(sessionStorage.getItem('data')){
                this.changeSelectStyle = JSON.parse(sessionStorage.getItem('data'))
            }else{
                this.changeSelectStyle = 0
            }

        },
        mounted() {
            this.timeFn();
            this.cancelLoading();
        },
        methods: {
            changeStyle(index){
                sessionStorage.clear()
                this.changeSelectStyle = index;
                sessionStorage.setItem('data', JSON.stringify(this.changeSelectStyle))
                // alert(JSON.stringify(this.changeSelectStyle))
            },
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
    .changeStyle{
        text-align: center;		//水平居中
        text-shadow: 0 0 10px yellow,0 0 20px yellow,0 0 30px yellow,0 0 40px yellow;	//设置发光效果
    }
    .originalStyle{
        color: white;		//水平居中
        display: inline-block;
        transform: skewX(-45deg);
    }

    #event {
        display: none;
    }

    .page_name {
        color: white;
    }
</style>