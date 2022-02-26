<template>
<div>  
  <div class="background">
    <img :src="imgSrc" width="100%" height="100%" alt="" />
  </div>
  <div class="title">
    <h2>欢迎登录</h2>
  </div>
  <div class="login">
    <el-row>
      <!-- span 宽度 ； offset 左侧空多少份 -->
      <el-col :span="6" :offset="14">
        <el-form
          :model="ruleForm"
          status-icon
          ref="ruleForm"
          :rules="rules"
          label-width="100px"
          class="demo-ruleForm"
        >
          <el-form-item label="用户名" prop="username" required>
            <el-input
              type="text"
              v-model="ruleForm.username"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password" required>
            <el-input
              type="password"
              v-model="ruleForm.password"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')"
              >登录</el-button
            >
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</div>
</template>
<script>


export default {
  name: "login",
  data() {
    return {
      ruleForm: {
        password: "",
        username: "",
      },
      rules: {
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
        ],
      },
      imgSrc:require('../assets/bj.jpg')
    };
  },
  methods: {
    submitForm(formName) {
      let that = this;
      this.$refs[formName].validate((valid, object) => {
        if (valid) {
          window.console.log(that.ruleForm);
          that.$axios
            .get("/login/in", {
              params: {
                username: that.ruleForm.username,
                password: that.ruleForm.password,
              },
            })
            .then((res) => {
              window.console.log(res);
              res = res.data;
              if (res.status == 200) {
                // 纪录token
               // window.sessionStorage.setItem("token", res.data);
                that.$store.commit('changeToken',res.data);
                that.$router.push("/home");
              } else {
                that.$message.error("登录失败,用户名或密码有问题");
              }
            });
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
};
</script>
<style scoped>
.login {

  padding-top: 200px;
}
.background{
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}
.title{
  position: absolute;
  padding-left: 62%;
  padding-top: 10%;
}
</style>
