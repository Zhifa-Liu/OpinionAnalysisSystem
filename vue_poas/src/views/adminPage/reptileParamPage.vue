<template>
    <div>
        <el-form ref="form" :model="form" status-icon :rules="rules" label-width="80px" >
            <el-form-item label="任务周期" prop="gap">
                <el-input v-model="form.gap" style="width: 3.49rem;margin-right: 0.2rem" placeholder="请输入数字"></el-input>
                <el-select  v-model="form.time_unit" placeholder="时间单位">
                    <el-option label="分钟" value="minute"></el-option>
                    <el-option label="小时" value="hour"></el-option>
                    <el-option label="天" value="day"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit('form')" @keyup.enter="onSubmit('form')">提交</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import axios from 'axios';

    export default {
        name: 'reptileParam',
        data() {
            var checkNumber = (rule, value, callback) => {
                if(value===''){
                    callback(new Error('输入不能为空'));
                }
                if(isNaN(value)){
                    callback(new Error('请输入数字'))
                } else {
                    callback()
                }
                // alert("xyz")
            };
            return {
                form: {
                    gap:'',
                    time_unit: 'minute',
                },
                rules: {
                    gap: [
                        { validator: checkNumber, trigger: 'blur' }
                    ],
                }
            }
        },
        methods: {
            onSubmit() {
                const that = this;
                this.$refs.form.validate((valid) => {
                    if(valid){
                        axios.post("/reptile/setReptileParam", {
                            "gap": this.form.gap,
                            "time_unit": this.form.time_unit
                        }).then(function (response){
                            if(response.data){
                                that.$message({
                                    message: '设置成功',
                                    type: "success"
                                });
                                that.form.gap = '';
                                that.resetForm();
                            }
                        });
                    }
                });
            },
            resetForm() {
                this.$refs.form.resetFields();
            }
        }
    }
</script>

<style scoped>

</style>