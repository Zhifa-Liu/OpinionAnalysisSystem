<template>
    <div id="comment_emotion">
        <span class="text" style="margin-bottom: 10px;">评论情感趋势分析</span>
        <div id="graph_2"></div>
        <el-drawer
                :title="commenttime"
                :visible.sync="drawer"
                :direction="direction"
                :before-close="handleClose"
                z-index = "1"
        >
            <el-tabs v-model="activeName" type="card">
                <el-tab-pane label="正面评论" name="first">
                    <el-card class="box-card">
                        <div v-for="o in this.poscomment" :key="o" class="text item">
                            {{o }}
                            <el-divider></el-divider>
                        </div>
                    </el-card>
                </el-tab-pane>
                <el-tab-pane label="负面评论" name="second">
                    <el-card class="box-card">
                        <div v-for="o in this.negcomment" :key="o" class="text item">
                            {{o }}
                            <el-divider></el-divider>
                        </div>
                    </el-card>
                </el-tab-pane>
                <el-tab-pane label="中立评论" name="third">
                    <el-card class="box-card">
                        <div v-for="o in this.midcomment" :key="o" class="text item">
                            {{o }}
                            <el-divider></el-divider>
                        </div>
                    </el-card>
                </el-tab-pane>
            </el-tabs>
        </el-drawer>
    </div>
</template>

<script>


    import axios from "axios";

    export default {
        name: "eventCommentEmotion",
        props: ['eventName'],
        mounted () {
            this.getEmotionTrend()
        },
        data() {
            return {
                poscomment:[],
                midcomment:[],
                activeName: 'first',
                negcomment:[],
                drawer: false,
                commenttime:'',
                direction: 'rtl',
                emotionTrendData: [],
                timer:null
            }
        },
        created() {
            this.timer = setInterval(this.getEmotionTrend, 30000)
        },
        beforeDestroy() {
            clearInterval(this.timer);
            this.timer = null
        },
        methods: {
            handleClose(done) {
                // this.$confirm('确认关闭？')
                //     .then(_ => {
                //         done();
                //     })
                //     .catch(_ => {});
                done()
            },
            async getEmotionTrend(){
                const that = this;
                await axios.get('/eventComment/eventCommentEmotion', {
                    params: {
                        eventName: this.eventName
                    }
                }).then(function (response) {
                    console.log(response.data);
                    if (response.data) {
                        that.emotionTrendData = response.data;
                        console.log("@@@@@@@@"+JSON.stringify(that.emotionTrendData))
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                this.draw();
            },
            draw(){
                this.lineOption={
                    // grid: {
                    //     containLabel: true,
                    // },
                    xAxis: {
                        type: 'category',
                        // data: ['2021-05-21', '2021-05-22', '2021-05-23', '2021-05-24', '2021-05-25', '2021-05-26', '2021-05-27', ],
                        data: this.emotionTrendData.times,
                        axisLabel: {
                            rotate: 30,
                            fontSize: 11,
                            color: "white",
                        },
                        axisLine: {
                            symbol: ['none', 'arrow'],
                            symbolSize: [10, 10],
                            symbolOffset: [0, 5],
                            lineStyle: {
                                color: 'lightblue'
                            }
                        }
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    color: [
                        "#867fde",
                        "#fac6ad",
                        "#a4ee90",
                    ],
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            fontSize: 11,
                            color: "white",
                        },
                        axisLine: {
                            symbol: ['none', 'arrow'],
                            symbolSize: [10, 10],
                            symbolOffset: [0, 5],
                            lineStyle: {
                                color: 'lightblue'
                            }
                        }
                    },
                    series: [
                        {
                            name: "正面评论占比",
                            // data: [0.52, 0.54, 0.66, 0.78, 0.89, 0.92, 0.93],
                            data: this.emotionTrendData.poss,
                            type: 'line',
                            markPoint: {
                                data: [
                                    {type: 'max', name: '最大值'},
                                    {type: 'min', name: '最小值'}
                                ]
                            },
                            // markLine: {
                            //     data: [
                            //         {type: 'average', name: '平均值'}
                            //     ]
                            // }
                        },
                        {
                            name: "负面评论占比",
                            // data: [0.48, 0.42, 0.31, 0.18, 0.10, 0.06, 0.02],
                            data: this.emotionTrendData.negs,
                            type: 'line',
                            markPoint: {
                                data: [
                                    {type: 'max', name: '最大值'},
                                    {type: 'min', name: '最小值'}
                                ]
                            },
                            // markLine: {
                            //     data: [
                            //         {type: 'average', name: '平均值'}
                            //     ]
                            // }
                        }
                    ]
                };
                var myChart = this.$echarts.init(document.getElementById('graph_2'))
                var emotionTrendData=this.emotionTrendData
                var that = this
                myChart.on('click',  function(param) {
                    //param.name x轴值,param.data y轴值
                    that.drawer = true
                    that.commenttime=param.name+"前的所有评论"
                    that.poscomment=[]
                    that.negcomment=[]
                    that.midcomment=[]
                    for(let i=0;i<emotionTrendData.comments[0].length;i++){
                        if(emotionTrendData.comments[0][i].timetmp <= param.name){
                            that.poscomment.push(emotionTrendData.comments[0][i].commentText)
                        }
                    }
                    for(let j=0;j<emotionTrendData.comments[1].length;j++){
                        if(emotionTrendData.comments[1][j].timetmp <= param.name){
                            that.negcomment.push(emotionTrendData.comments[1][j].commentText)
                        }
                    }
                    for(let k=0;k<emotionTrendData.comments[2].length;k++){
                        if(emotionTrendData.comments[2][k].timetmp <= param.name){
                            that.midcomment.push(emotionTrendData.comments[2][k].commentText)
                        }
                    }
                    // alert(that.poscomment)
                    console.log(param.name);
                });
                myChart.setOption(this.lineOption);
            }
        }
    }
</script>

<style scoped>
    #comment_emotion {
        padding: 0.2rem 0.36rem 0;
    }

    #graph_2 {
        border-color: white;
        height: 5rem;
        width: 10.5rem;
    }
</style>