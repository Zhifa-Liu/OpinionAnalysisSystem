<template>
    <div class="block" style="height:11rem;overflow-y:auto;overflow-x:hidden">
        <el-timeline>
            <el-timeline-item
                    v-for="(log,index) in data"
                    :key="index"
                    :timestamp="log.times"
                    placement="top">
                <el-card style="color:#ffffff;border-color:rgba(113,107,107,0.4);background-color:rgba(153,146,146,0.4);">
                    <h4>{{log.type}}</h4><br>
                    <p>{{log.operator}}</p>
                </el-card>
            </el-timeline-item>
        </el-timeline>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "systemPage",
        data(){
            return{
                data:[]
            }
        },
        mounted () {
            this.getData()
        },
        methods:{
            async getData(){
                const that = this;
                await axios.get("/log/getLog").then(function (response){
                    if(response.data){
                        that.data=response.data
                        console.log(response.data)
                    }
                });
            }
        }
    }
</script>


<style>
    ::-webkit-scrollbar{width:0.1rem;}
    ::-webkit-scrollbar-track{background-color: #909393;}
    ::-webkit-scrollbar-thumb{background-color: #161617;}
    ::-webkit-scrollbar-thumb:hover {background-color:#9c3}
    ::-webkit-scrollbar-thumb:active {background-color: #0f0f10
    }
</style>