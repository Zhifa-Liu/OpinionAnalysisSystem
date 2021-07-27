<template>
    <div>
        <el-form ref="rankForm" :model="rankForm" :rules="theRules" label-width="4rem" >
            <el-form-item label="文章排名 TopN" prop="v1">
                <el-select v-model="rankForm.v1">
                    <el-option
                        v-for="item in nOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="文章热度 TopN" prop="v2">
                <el-select v-model="rankForm.v2">
                    <el-option
                        v-for="item in nOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="事件高赞与高回复评论 TopN" prop="v3">
                <el-select v-model="rankForm.v3">
                    <el-option
                        v-for="item in nOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit('')">提交</el-button>
                <el-button @click="resetForm">重置</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import axios from 'axios';

    export default {
        name: "rankValueSetPage",
        data() {
            var checkSelect = (rule, value, callback) => {
                if(value===''){
                    callback(new Error('请选择'));
                } else{
                    callback()
                }
            };
            return {
                rankForm: {
                    v1: '',
                    v2: '',
                    v3: '',
                },
                nOptions: [
                    {
                        value: 5,
                        label: 5
                    },
                    {
                        value: 7,
                        label: 7
                    },
                    {
                        value: 10,
                        label: 10
                    }
                ],
                theRules: {
                    v1: [
                        { validator: checkSelect, trigger: 'blur' }
                    ],
                    v2: [
                        { validator: checkSelect, trigger: 'blur' }
                    ],
                    v3: [
                        { validator: checkSelect, trigger: 'blur' }
                    ],
                }
            }
        },
        methods: {
            onSubmit() {
                const that = this;
                this.$refs.rankForm.validate((valid) => {
                    console.log(valid);
                    if(valid){
                        axios.post("/topN/setTopN", {
                            "articleRank": this.rankForm.v1,
                            "articleHotRank": this.rankForm.v2,
                            "eventCommentRank": this.rankForm.v3
                        }).then(function (response){
                            if(response.data){
                                that.$message.success({
                                    message: '设置成功',
                                });
                            }
                        });
                        that.rankForm.v1 = '';
                        that.rankForm.v2 = '';
                        that.rankForm.v3 = '';
                        that.resetForm();
                        axios.post("/log/setLog", {
                            "type": "文章排名设置",
                            "operator": "设置文章排名Top"+this.rankForm.v1+"+文章热度排名Top"+this.rankForm.v2+"+事件评论排名Top"+this.rankForm.v3
                        }).then(function (response){
                            if(response.data){
                                console.log("1")
                            }
                        });
                    }
                });
            },
            resetForm() {
                this.$refs.rankForm.resetFields();
            }
        }
    }
</script>

<style scoped>

</style>