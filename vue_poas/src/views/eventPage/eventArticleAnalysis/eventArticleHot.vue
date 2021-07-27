<template>
    <div id="eventArticleHot">
        <span class="text" style="margin-top: 10px; display: block">当前事件文章热度Top</span>
<!--        <dv-scroll-ranking-board :config="config" style="width: 7.8rem; height: 5.2rem; border-radius: 10px" />-->
        <el-carousel indicator-position="outside">
            <el-carousel-item v-for="item in this.article" :key="item.index">
                <div style="padding: 0.2rem">
                    <el-card class="box-card" style="font-size: small;height: 4.5rem;width: 100%;color:#ffffff;border-color:rgba(17,17,17,0.4);background-color:rgba(17,17,17,0.4);">
                        <i class="el-icon-user-solid"> </i>
                        {{item.name}}
                        <i class="el-icon-s-flag"></i>
                        {{item.value}}<br><br><br><br><br><br>
                        {{item.articleText }}
                    </el-card>
                </div>
            </el-carousel-item>
        </el-carousel>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "eventArticleHot",
        props: ['eventName'],
        mounted () {
            this.getData()
        },
        data() {
            return {
                article:[],
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
        methods:{
            init(){

            },
            async getData(){
                const that = this;
                console.log("首页点击参数" + that.eventName)
                await axios.get('/eventArticle/eventArticleRank',{
                    params: {
                        eventName: this.eventName
                    }
                }).then(function (response) {
                    if (response.data) {
                        that.article=response.data[3]
                        console.log("$$$$$$"+JSON.stringify(that.article))

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
    #eventArticleHot {
        /*background-color: white;*/
        padding: 0.2rem 0.3rem 0;
        height: 5.8rem;
        width: 8.4rem;
    }
</style>