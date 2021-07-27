<template>
    <div id="comment_keyword">
        <span class="text" style="margin-bottom: 10px;">评论关键词</span>
        <div id="graph_3"></div>
    </div>
</template>

<script>
    import 'echarts-wordcloud';
    import axios from "axios";

    export default {
        name: "eventCommentKeyword",
        props: ['eventName'],
        mounted () {
            this.getCommentKeyword()
        },
        data () {
            return {
                commentKeywordData: [],
                timer: null
            }
        },
        created() {
            this.timer = setInterval(this.getCommentKeyword, 30000)
        },
        beforeDestroy() {
            clearInterval(this.timer);
            this.timer = null
        },
        methods: {
            async getCommentKeyword (){
                const that = this;
                await axios.get('/eventComment/eventCommentKeyword', {
                    params: {
                        eventName: this.eventName
                    }
                }).then(function (response) {
                    if (response.data) {
                        that.commentKeywordData = response.data;
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                this.draw();
            },
            draw(){
                this.wordOption ={
                    shape: 'circle',

                    series:[
                        {
                            type: 'wordCloud',
                            sizeRange: [6, 32],
                            rotationRange: [-60, 90],
                            rotationStep: 30,
                            textStyle: {
                                normal:{
                                    color:function () {
                                        return 'rgb(' + [
                                            Math.round(Math.random() * 160+40),
                                            Math.round(Math.random() * 160+40),
                                            Math.round(Math.random() * 160+40)
                                        ].join(',')+')';

                                    }
                                }
                            },
                            data: this.commentKeywordData,
                            // data: [
                            //     {
                            //         "name": "奢侈品",
                            //         "value": 137
                            //     },
                            //     {
                            //         "name": "母婴社区",
                            //         "value": 299
                            //     },
                            //     {
                            //         "name": "早教",
                            //         "value": 103
                            //     },
                            //     {
                            //         "name": "童车童床",
                            //         "value": 41
                            //     },
                            //     {
                            //         "name": "关注品牌",
                            //         "value": 271
                            //     },
                            //     {
                            //         "name": "宝宝玩乐",
                            //         "value": 30
                            //     },
                            //     {
                            //         "name": "软件应用",
                            //         "value": 1018
                            //     },
                            //     {
                            //         "name": "系统工具",
                            //         "value": 896
                            //     },
                            //     {
                            //         "name": "理财购物",
                            //         "value": 440
                            //     },
                            //     {
                            //         "name": "生活实用",
                            //         "value": 365
                            //     },
                            //     {
                            //         "name": "影音图像",
                            //         "value": 256
                            //     },
                            //     {
                            //         "name": "社交通讯",
                            //         "value": 214
                            //     },
                            //     {
                            //         "name": "手机美化",
                            //         "value": 39
                            //     },
                            //     {
                            //         "name": "办公学习",
                            //         "value": 28
                            //     },
                            //     {
                            //         "name": "应用市场",
                            //         "value": 23
                            //     },
                            //     {
                            //         "name": "母婴育儿",
                            //         "value": 14
                            //     },
                            //     {
                            //         "name": "游戏",
                            //         "value": 946
                            //     },
                            //     {
                            //         "name": "手机游戏",
                            //         "value": 565
                            //     },
                            //     {
                            //         "name": "模拟辅助",
                            //         "value": 166
                            //     },
                            //     {
                            //         "name": "个护美容",
                            //         "value": 942
                            //     },
                            //     {
                            //         "name": "护肤品",
                            //         "value": 177
                            //     },
                            // ]
                        }
                    ]
                };
                this.$echarts.init(document.getElementById("graph_3")).setOption(this.wordOption)
            }
        }
    }
</script>

<style scoped>
    #comment_keyword {
        padding: 0.2rem 0.36rem 0;
    }

    #graph_3 {
        /*background-color: white;*/
        border-color: white;
        height: 5rem;
        width: 5.5rem;
    }
</style>