<template>
  <el-form :rules="rules" class="login-container" label-position="left" label-width="0px" v-loading="loading">
    <h3 class="login_title">用户注册</h3>
    <el-form-item prop="account">
      <el-input type="text" v-model="regForm.username" auto-complete="off" placeholder="用户名"></el-input>
    </el-form-item>
    <el-form-item prop="email">
      <el-input type="text" v-model="regForm.email" auto-complete="off" placeholder="邮箱 (可选)"></el-input>
    </el-form-item>
    <el-form-item prop="checkPass">
      <el-input type="password" v-model="regForm.password" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
    <el-form-item style="width: 100%">
      <el-button type="primary" @click.native.prevent="submitClick" style="width: 100%">注册</el-button>
    </el-form-item>
    <div style="text-align:center;margin-top:8px">
      <el-button type="text" @click="$router.replace({path: '/'})">已有账号，去登录</el-button>
    </div>
  </el-form>
</template>
<script>
  import {postRequest} from '../utils/api'

  export default {
    data() {
      return {
        rules: {
          account: [{required: true, message: '请输入用户名', trigger: 'blur'}],
          checkPass: [{required: true, message: '请输入密码', trigger: 'blur'}]
        },
        regForm: {
          username: '',
          password: '',
          email: ''
        },
        loading: false
      }
    },
    methods: {
      submitClick() {
        var _this = this;
        _this.loading = true;
        postRequest('/auth/register', {
          username: _this.regForm.username,
          password: _this.regForm.password,
          email: _this.regForm.email
        }).then(resp => {
          _this.loading = false;
          if (resp.status == 200 && resp.data.status == 'success') {
            _this.$message({type: 'success', message: '注册成功，请登录'});
            _this.$router.replace({path: '/'});
          } else {
            _this.$message({type: 'error', message: resp.data ? resp.data.msg : '注册失败'});
          }
        }, () => {
          _this.loading = false;
          _this.$message({type: 'error', message: '服务器未响应'});
        })
      }
    }
  }
</script>

<style>
  .login-container { width: 360px; margin: 120px auto; padding: 30px; background: #fff; border-radius: 8px }
  .login_title { text-align: center; margin-bottom: 20px }
</style>
