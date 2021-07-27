<template>
    <div id="index">
        <dv-full-screen-container class="bg">
<!--            <dv-loading v-if="loading">Loading...</dv-loading>-->
            <div class="host-body">
                <div class="d-flex jc-center">
                    <dv-decoration-10 style="width:33.3%;height:.0625rem;"/>
                    <div class="d-flex jc-center">
                        <dv-decoration-8 :color="['#568aea', '#000000']" style="width:2.5rem;height:.625rem;"/>
                        <div class="title">
                            <span class="title-text">舆情分析系统</span>
                            <dv-decoration-5
                                    class="title-bottom"
                                    :reverse="true"
                                    :color="['#50e3c2', '#67a1e5']"
                                    style="width:3.6rem;height:.5rem;"
                            />
                        </div>
                        <dv-decoration-8
                                :reverse="true"
                                :color="['#568aea', '#000000']"
                                style="width:2.5rem;height:.625rem;"
                        />
                    </div>
                    <dv-decoration-10 style="width:33.3%;height:.0625rem; transform: rotateY(180deg);"/>
                </div>
            </div>
            <div style="margin-top: 3.6rem; width: 6rem; margin-left: 7.5rem; text-align: center">
                <el-form :model="loginForm" ref="loginForm" :rules="rules" label-position="right" label-width="100px" >
                    <el-form-item label="账号" prop="username">
                        <el-input prefix-icon="iconfont icon-user" v-model="loginForm.username" placeholder="请输入账号" style="width: 5rem"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input prefix-icon="iconfont icon-mima" type="password" v-model="loginForm.password" autocomplete="off" placeholder="请输入密码" style="width: 5rem"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm()" @keyup.enter="submitForm()">登录</el-button>
                        <el-button @click="resetForm()">重置</el-button>
<!--                        <el-button type="primary" disabled>注册</el-button>-->
                    </el-form-item>
                </el-form>
            </div>
        </dv-full-screen-container>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        name: "loginPage",
        mounted() {
            // 绑定监听事件
            window.addEventListener("keydown", this.keyDown);
        },
        destroyed() {
            // 销毁事件
            window.removeEventListener("keydown", this.keyDown, false);
        },
        data () {
            var checkUserName = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入账号'))
                } else {
                    callback()
                }
            };
            var validatePassWord = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'))
                } else {
                    callback()
                }
            };
            return {
                loginForm: {
                    username: '',
                    password: ''
                },
                rules: {
                    username: [
                        { validator: checkUserName, trigger: 'blur' }
                    ],
                    password: [
                        { validator: validatePassWord, trigger: 'blur' }
                    ]
                }
            }
        },
        methods: {
            // 点击回车键登录
            keyDown(e) {
                // 回车则执行登录方法 enter键的ASCII是13
                if (e.keyCode === 13) {
                    this.submitForm(); // 定义的登录方法
                }
            },
            submitForm () {
                this.$refs.loginForm.validate((valid) => {
                    console.log(valid);
                    const that = this;
                    axios.get('/user/login', {
                        params: {
                            username: this.loginForm.password,
                            password: this.loginForm.password
                        }
                    }).then(function (response) {
                        if (response.data) {
                            if(response.data.userType === 'admin'){
                                that.$router.push({ name : 'adminMainPage' })
                            } else if (response.data.userType === 'analysis'){
                                that.$router.push({ name : 'index' })
                            }
                        } else {
                            alert("账号或密码有误")
                        }
                    }).catch(function (error) {
                        console.log(error.message)
                    });
                })
            },
            resetForm () {
                this.$refs.loginForm.resetFields()
            }
        }
        // data(){
        //     return {
        //         user: '',
        //         pwd : '',
        //         error : {
        //             user: '',
        //             pwd : ''
        //         }
        //     }
        // },
        // methods:{
        //     register(){},
        //     login(){
        //         // eslint-disable-next-line no-unused-vars
        //         const { user, pwd} = this;
        //
        //         if(user === "admin" && pwd === "123456"){
        //             this.$router.push({ name : 'adminMainPage' })
        //         } else if(user === "analysis" && pwd === "123456"){
        //             this.$router.push({ name : 'index' })
        //         } else{
        //             alert("账号密码有误")
        //         }
        //     }
        // }
    }
</script>

<style lang="scss">
    @import '../assets/scss/globel';
</style>


