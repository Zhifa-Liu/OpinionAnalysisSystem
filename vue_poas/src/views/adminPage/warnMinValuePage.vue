<template>
    <div>
        <el-form :model="ruleForm" ref="ruleForm" status-icon :rules="rules" label-width="3rem" >
            <el-form-item label="事件负面评论占比" prop="negCommentProportionThreshold">
                <el-input v-model="ruleForm.negCommentProportionThreshold" placeholder="事件负面评论占比阈值" style="width: 3.49rem;margin-right: 0.5rem"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit('ruleForm')">提交</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "warnMinValuePage",
        data() {
            var checkNumber = (rule, value, callback) => {
                if(value===''){
                    callback(new Error('阈值不能为空'));
                }
                if(!isNaN(value)){
                    // alert(Number.parseFloat(value));
                    if (value>1 || value<0) {
                        callback(new Error('请输入[0,1]之间的数字'))
                    } else {
                        callback()
                    }
                }else{
                    callback(new Error('请输入数字'))
                }
            };
            return {
                ruleForm: {
                    negCommentProportionThreshold: '',
                },
                rules: {
                    negCommentProportionThreshold: [
                        { validator: checkNumber, trigger: 'blur' }
                    ],
                }
            }
        },
        methods: {
            onSubmit() {
                const that = this;
                this.$refs.ruleForm.validate((valid) => {
                    if(valid){
                        axios.post("/threshold/setThreshold", {
                            "negCommentProportionThreshold": this.ruleForm.negCommentProportionThreshold,
                        }).then(function (response){
                            if(response.data){
                                that.$message.success({
                                    message: '设置成功',
                                });
                                that.ruleForm.negCommentProportionThreshold = '';
                                that.resetForm()
                            }
                        });
                    }
                });
            },
            resetForm() {
                this.$refs.ruleForm.resetFields();
            }
        }
    }
</script>

<style lang="scss">

</style>