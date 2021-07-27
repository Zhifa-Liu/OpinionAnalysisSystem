<template>
    <div id="comment_rank_1">
        <span class="text" style="margin-bottom: 10px;">事件高赞评论</span>
        <div id="rank_div_1">
        </div>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "eventCommentLikeRank",
        props: ['eventName'],
        mounted() {
            this.getLikeRank();
        },
        data() {
            return {
                likeRank: [],
                timer: null
            }
        },
        created() {
            this.timer = setInterval(this.getLikeRank, 30000)
        },
        beforeDestroy() {
            clearInterval(this.timer);
            this.timer = null
        },
        methods: {
            async getLikeRank() {
                const that = this;
                await axios.get('/eventComment/eventCommentLikeRank', {
                    params: {
                        eventName: this.eventName
                    }
                }).then(function (response) {
                    console.log(response.data);
                    if (response.data) {
                        that.likeRank = response.data;
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                this.draw();
            },
            draw() {
                this.likeOption={
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
                            name: '评论点赞排名',
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
                            data: this.likeRank
                            // data: [
                            //     {value: 112, name: 'XYZ'},
                            //     {value: 201, name: 'ABC'},
                            //     {value: 78, name: 'CBA'},
                            //     {value: 102, name: 'NBA'},
                            //     {value: 301, name: 'COCO'},
                            //     {value: 87, name: 'MN'},
                            // ]
                        }
                    ]
                };
                this.$echarts.init(document.getElementById("rank_div_1")).setOption(this.likeOption);
            },
        }
    }
</script>

<style lang="scss">
    #comment_rank_1 {
        padding: 0.2rem 0.2rem 0;
        height: 4.8rem;
        width: 11.8rem;
        #rank_div_1 {
            /*background-color: white;*/
            height: 4.4rem;
            width: 11rem;
        }
    }
</style>