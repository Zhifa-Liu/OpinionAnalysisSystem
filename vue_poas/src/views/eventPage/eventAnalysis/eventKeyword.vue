<template>
    <div id="word_count_div">
        <span class="text">事件关键词</span>
        <div id="event_keyword"></div>
    </div>
</template>

<script>
    import 'echarts-wordcloud';
    import axios from "axios";

    export default {
        name: "eventArticleKeyword",
        props: ['eventName'],
        mounted() {
            this.getData()
            // this.init()
        },
        data() {
            return {
                mydata: [],
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
        methods: {
                init() {
                    this.option = {
                        shape: 'circle',

                        series: [
                            {
                                type: 'wordCloud',
                                sizeRange: [6, 32],
                                rotationRange: [-60, 90],
                                rotationStep: 36,
                                textStyle: {
                                    normal: {
                                        color: function () {
                                            return 'rgb(' + [
                                                Math.round(Math.random() * 160 + 45),
                                                Math.round(Math.random() * 160 + 45),
                                                Math.round(Math.random() * 160 + 45)
                                            ].join(',') + ')';
                                        }
                                    }
                                },
                                data: this.mydata
                            }
                        ]
                    };
                    this.$echarts.init(document.getElementById("event_keyword")).setOption(this.option);
                },
                async getData() {
                    const that = this;

                    console.log("首页点击参数" + that.eventName)
                    await axios.get('/event/eventKeyword', {
                        params: {
                            eventName: this.eventName
                        }
                    }).then(function (response) {
                        if (response.data) {
                            console.log("Hello Runoob!   this.eventName");
                            console.log(response.data);
                            that.mydata = response.data
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
    #word_count_div {
        width: 6rem;
        height: 6rem;
        padding: 0.2rem 0.36rem 0;
    }

    #event_keyword {
        width: 5.5rem;
        height: 5.5rem;
    }
</style>