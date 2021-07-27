<template>
    <div id="spread_div">
        <span class="text" style="display: block">事件核心传播人</span>
        <el-button-group>
            <el-button type="primary" size="mini" @click="replaceGraphMedia()" class="btn_group">媒体</el-button>
            <el-button type="primary" size="mini" @click="replaceGraphPeople()" class="btn_group">网民</el-button>
        </el-button-group>
        <div id="graph_div">
            <div id="graph"></div>
        </div>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "eventSpreadPeople",
        props: ['eventName'],
        mounted () {
            this.getData()
        },
        data() {
            return {
                mediaData: [],
                peopleData: [],
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
            init(){
                this.mediaOption={
                    legend: {
                        top: '10%',
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
                            name: '文章声量排名',
                            type: 'pie',
                            radius: [20, 30],
                            center: ['50%', '50%'],
                            roseType: 'radius', // area;
                            itemStyle: {
                                borderRadius: 20,
                                normal:{
                                    label:{
                                        show: true,
                                        formatter: '{b} {c}'
                                    },
                                    labelLine :{show:true}
                                }
                            },
                            data: this.mediaData
                        }
                    ]
                };

                this.netizenOption={
                    legend: {
                        top: '10%',
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
                            name: '文章声量排名',
                            type: 'pie',
                            radius: [20, 30],
                            center: ['50%', '50%'],
                            roseType: 'radius', // area;
                            itemStyle: {
                                borderRadius: 20,
                                normal:{
                                    label:{
                                        show: true,
                                        formatter: '{b} {c}'
                                    },
                                    labelLine :{show:true}
                                }
                            },
                            data: this.peopleData
                        }
                    ]
                };

                this.draw();
            },
            draw(){
                this.$echarts.init(document.getElementById("graph")).setOption(this.mediaOption)
            },
            replaceGraphMedia(){
                this.$echarts.init(document.getElementById("graph")).setOption(this.mediaOption)
            },
            replaceGraphPeople(){
                this.$echarts.init(document.getElementById("graph")).setOption(this.netizenOption)
            },
            async getData() {
                const that = this;
                await axios.get('/event/eventSpread', {
                    params: {
                        eventName: this.eventName
                    }
                }).then(function (response) {
                    if (response.data) {

                        // console.log("&&&&&&&&&&&&"+response.data[1].spreadPeopleType)
                        // console.log("&&&&&&&&&&&&"+response.data[0].spreadPeopleType)
                        var data=response.data;
                        that.peopleData = []
                        that.mediaData = []
                        for(var o in data){
                            if(data[o].spreadPeopleType === '网民'){
                                that.peopleData.push({value: data[o].spreadCount,name: data[o].spreadPeople});
                            }else if(data[o].spreadPeopleType === '媒体'){
                                that.mediaData.push({value: data[o].spreadCount,name: data[o].spreadPeople});
                            }
                        }
                        console.log("&&&&&&&&&&&&"+that.mediaData)
                        console.log("&&&&&&&&&&&&"+that.peopleData)
                    }
                }).catch(function (error) {
                    console.log(error.message)
                });
                this.init();
            }
        }
    }
</script>

<style lang="scss">
    #spread_div {
        display: block;
        width: 6rem;
        height: 6rem;
        padding: 0.2rem 0.36rem 0;
        #graph_div {
            background-color: transparent;
            width: 5.4rem;
            height: 5.6rem;
            /*background-color: #4b67af;*/
            margin: 0.2rem;
            #graph {
                /*background-color: white;*/
                width: 5.4rem;
                height: 5.4rem;
            }
        }
    }

    .el-button-group {
        margin-left: 10px;
        margin-top: 10px;
        background-color: transparent;
    }
    .el-button {
        background-color: transparent;
        border-color: lightblue;
    }
    .el-button.btn_group:active {
        background-color: lightblue;
    }
</style>