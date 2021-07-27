<template>
    <div>
        <div id="event_div">
            <div class="react-left ml-4" style="background-color: #0f1325;" @click="openDetails1(0)">
<!--                <router-link :to="{path:'/eventPage/eventAnalysis',params:{'eventName':this.eventName}}" active-class="active">-->
                <div class="originalStyle1">
                    <span :class="{changeStyle1:changeSelectStyle1 == 0}">舆情事件总缆</span>
                </div>
<!--                </router-link>-->
            </div>
            <div class="react-left ml-4" style="background-color: #0f1325;" @click="openDetails2(1)">
<!--                <router-link :to="{path:'/eventPage/eventArticleAnalysis',params:{'eventName':this.eventName}}" active-class="active">-->
                <div class="originalStyle1">
                    <span :class="{changeStyle1:changeSelectStyle1 == 1}">舆情事件文章分析</span>
                </div>
<!--                </router-link>-->
            </div>
            <div class="react-left ml-4" style="background-color: #0f1325;" @click="openDetails3(2)">
<!--                <router-link to="/eventPage/eventArticleCommentAnalysis" active-class="active">-->
                <div class="originalStyle1">
                    <span :class="{changeStyle1:changeSelectStyle1 == 2}">舆情事件评论分析</span>
                </div>
<!--                </router-link>-->
            </div>
<!--            <div class="bottom_box" style="font-size: xx-large">-->
<!--                <p id="p" v-text="eventName" style="font-size: xx-large;padding-left: 2rem"></p>-->
<!--            </div>-->
        </div>
<!--        <div class="bottom_box" style="font-size: xx-large;padding: 1rem 0;padding-left: 3rem">-->
<!--            ###################-->
<!--        </div>-->
        <div class="body_box">
                <router-view></router-view>
        </div>
    </div>
</template>

<script>
    export default {
        name: "eventPage",
        // props: ['eventName'],
        created() {
            if(sessionStorage.getItem('data1')){
                this.changeSelectStyle1 = JSON.parse(sessionStorage.getItem('data1'))
            }else{
                this.changeSelectStyle1=0
            }

        },

        data () {
            return {
                changeSelectStyle1:0,
                eventName: this.$route.query.eventName
            }
        },
        components: {

        },
        mounted() {


        },
        methods: {
            openDetails1 (index) {
                this.changeSelectStyle1 = index;
                sessionStorage.setItem('data1', JSON.stringify(this.changeSelectStyle1))
                this.$router.push({
                    path:"/eventPage/eventAnalysis",
                    query:{
                        eventName: this.eventName
                    }});
            },
            openDetails2 (index) {
                this.changeSelectStyle1 = index;
                sessionStorage.setItem('data1', JSON.stringify(this.changeSelectStyle1))
                this.$router.push({
                    path:"/eventPage/eventArticleAnalysis",
                    query:{
                        eventName: this.eventName
                    }});
            },
            openDetails3 (index) {
                this.changeSelectStyle1 = index;
                sessionStorage.setItem('data1', JSON.stringify(this.changeSelectStyle1))
                this.$router.push({
                    path:"/eventPage/eventArticleCommentAnalysis",
                    query:{
                        eventName: this.eventName
                    }});
            }

        }
    }
</script>

<style lang="scss">
    @import 'src/assets/scss/globel';

    #event_div {
        display: flex;
        align-items: center;
        margin-bottom: 0.2rem;
    }
    .changeStyle1{
        color: #eae371;
        text-shadow: 0 0 10px #f6f6b5,0 0 20px #f6f6b6,0 0 30px #f5f59d,0 0 40px #ffffa0;	//设置发光效果
    }
    .originalStyle1{
        color: white;		//水平居中
        display: inline-block;
        transform: skewX(45deg);
    }
</style>