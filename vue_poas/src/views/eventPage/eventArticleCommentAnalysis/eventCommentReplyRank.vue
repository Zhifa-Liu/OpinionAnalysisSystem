<template>
    <div id="comment_rank_2">
        <span class="text" style="margin-bottom: 10px;">事件高回复评论</span>
        <div id="rank_div_2">
        </div>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "eventCommentReplyRank",
        props: ['eventName'],

        mounted() {
            this.getReplyRank();
        },
        data() {
            return {
                replyRank: [],
                timer: null
            }
        },
        created() {
            this.timer = setInterval(this.getReplyRank, 30000)
        },
        beforeDestroy() {
            clearInterval(this.timer);
            this.timer = null
        },
        methods: {
            async getReplyRank() {
                const that = this;
                await axios.get('/eventComment/eventCommentReplyRank', {
                    params: {
                        eventName: this.eventName
                    }
                }).then(function (response) {
                    console.log(response.data);
                    if (response.data) {
                        that.replyRank = response.data;
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                this.draw();
            },
            draw() {
                this.replyOption={

                    legend: {
                        top: '5%',
                        type: 'scroll',
                        icon: 'circle',
                        textStyle: {
                            color: 'white',
                            fontSize: 11,
                            fontWeight: 'lighter',
                            fontFamily: 'MicroSoft YaHei'
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
                            name: '评论回复排名',
                            type: 'pie',
                            radius: [20, 50],
                            center: ['50%', '60%'],
                            roseType: 'radius', // area;
                            itemStyle: {
                                borderRadius: 20,
                                normal:{
                                    label:{
                                        show: true,
                                        formatter: '{b}\n{c}'
                                    },
                                    labelLine :{show:true}
                                }
                            },
                            data: this.replyRank,
                            // data: [
                            //     {value: 112, name: 'abc'},
                            //     {value: 201, name: 'xyz'},
                            //     {value: 78, name: 'nba'},
                            //     {value: 102, name: 'cba'},
                            //     {value: 301, name: 'nb'},
                            //     {value: 87, name: 'mhb'},
                            // ]
                        }
                    ]
                };
                this.$echarts.init(document.getElementById("rank_div_2")).setOption(this.replyOption);
            },
        }
    }
</script>

<style lang="scss">
    #comment_rank_2 {
        padding: 0.2rem 0.2rem 0;
        height: 4.8rem;
        width: 11.8rem;
        #rank_div_2 {
            height: 4.4rem;
            width: 11rem;
            /*background-color: white;*/
        }
    }
</style>