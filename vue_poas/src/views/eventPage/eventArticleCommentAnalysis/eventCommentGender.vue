<template>
    <div id="gender_graph">
        <span class="text" style="margin-bottom: 10px">评论者性别分析</span>
        <div id="gender_div"></div>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "eventCommentGender",
        props: ['eventName'],
        mounted() {
            this.getGenderProportion()
        },
        data() {
            return {
                genderData: [],
                timer:null
            }
        },
        created() {
            this.timer = setInterval(this.getGenderProportion, 30000)
        },
        beforeDestroy() {
            clearInterval(this.timer);
            this.timer = null
        },
        methods: {
            async getGenderProportion(){
                const that = this;
                await axios.get('/eventComment/eventCommentGender', {
                    params: {
                        eventName: this.eventName
                    }
                }).then(function (response) {
                    console.log(response.data);
                    if (response.data) {
                        that.genderData = response.data;
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                this.draw();
            },
            draw(){
                this.pieOption={
                    legend: {
                        top: '80%'
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
                            radius: [30, 50],
                            center: ['50%', '50%'],
                            roseType: 'radius', // area;
                            itemStyle: {
                                borderRadius: 20,
                                normal:{
                                    label:{
                                        show: true,
                                        formatter: '{b}:{d}%'
                                    },
                                    labelLine :{show:true}
                                }
                            },
                            data: this.genderData
                            // data: [
                            //     {value: 120, name: '男'},
                            //     {value: 101, name: '女'},
                            // ]
                        }
                    ]
                };
                this.$echarts.init(document.getElementById("gender_div")).setOption(this.pieOption);
            }
        }
    }
</script>

<style scoped>
    #gender_graph {
        /*padding: 0.2rem 0.2rem 0;*/
        /*width: 5rem;*/
        /*height: 4.8rem;*/

        padding: 0.2rem 0.36rem 0;
    }

    #gender_div {
        /*background-color: white;*/
        /*width: 5rem;*/
        /*height: 4.4rem;*/

        height: 5rem;
        width: 5.5rem;
    }
</style>


