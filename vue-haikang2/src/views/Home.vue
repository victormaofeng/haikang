<template>
  <div class="home">
    <!-- 左边栏 菜单-->
    <div class="left">
      <!-- 头部栏 -->
      <div class="header">
        <img :src="logo" class="img" />
        <div class="name">行人重识别系统</div>
      </div>

      <!-- <el-radio-group v-model="isCollapse" style="margin-bottom: 20px">
        <el-radio-button :label="false">展开</el-radio-button>
        <el-radio-button :label="true">收起</el-radio-button>
      </el-radio-group> -->

      <el-menu
        default-active="index"
        @open="handleOpen"
        @close="handleClose"
        :collapse="isCollapse"
        :router="true"
      >
        <el-menu-item index="index">
          <i class="el-icon-menu"></i>
          <span slot="title">监控</span>
        </el-menu-item>

        <el-menu-item index="upload2">
          <i class="el-icon-menu"></i>
          <span slot="title">重识别管理</span></el-menu-item
        >
        <el-menu-item index="algorithm2">
          <i class="el-icon-menu"></i>
          <span slot="title">模型管理</span></el-menu-item
        >
        <el-menu-item index="resultReid">
          <i class="el-icon-menu"></i>
          <span slot="title">结果管理</span></el-menu-item
        >
      </el-menu>
    </div>

    <!-- 内容栏 -->
    <div class="container">
      <div class="top">
        <el-row type="flex" justify="end">
          <!-- <el-col :span="2">
            <span class="item" @click="goDetect()">检测</span>
          </el-col>

          <el-col :span="2">
            <span class="item" @click="goDetect()">历史</span>
          </el-col> -->

          <el-col :span="2">
            <el-badge :value="messageCount" v-if="messageCount > 0">
              <span class="item" @click="goMessage()"
                >消息<i class="el-icon-message el-icon--right"></i
              ></span>
            </el-badge>

            <el-tooltip
              effect="dark"
              content="暂无新消息"
              placement="bottom"
              v-if="messageCount == 0"
            >
              <span class="item" @click="goMessage()"
                >消息<i class="el-icon-message el-icon--right"></i
              ></span>
            </el-tooltip>
          </el-col>

          <el-col :span="2">
            <el-dropdown :hide-on-click="false" @command="handleCommand">
              <span class="item">
                {{ user.nickname
                }}<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="pwd">修改密码</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>

            <el-dialog title="修改密码" :visible.sync="pwdVisible">
              <el-form :model="pwdForm">
                <el-form-item label="密码">
                  <el-input
                    v-model="pwdForm.oldPwd"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
                <el-form-item label="新密码">
                  <el-input
                    v-model="pwdForm.newPwd"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
                <el-form-item label="确认密码">
                  <el-input
                    v-model="pwdForm.checkPwd"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="cancelPwd()">取 消</el-button>
                <el-button type="primary" @click="submitPwd()">确 定</el-button>
              </div>
            </el-dialog>
          </el-col>
        </el-row>
      </div>

      <!-- 主栏 -->
      <div class="main">
        <router-view />
      </div>

    </div>
  </div>
</template>

<script>
import norecord from "@/components/no-record.vue";
import nepu from "@/assets/nepu.png";

export default {
  name: "Home",
  data() {
    return {
      logo: nepu,
      isCollapse: false,
      user: {},
      token: "",
      // 消息条数,当后台处理完图片或视频后,会通过websocket 通知 vue
      messageCount: 0,
      // websocket 对象
      socket: null,
      pwdVisible: false,
      pwdForm: {
        oldPwd: "",
        newPwd: "",
        checkPwd: "",
      },
    };
  },
  components: {
    norecord,
  },

  created() {
    // 请求个人信息
    this.$axios
      .get("user", {
        params: {
          //token: this.$store.state.token,
        },
      })
      .then((res) => {
        res = res.data;
        if (res.status == 200) {
          this.user = res.data;
          this.$store.commit("setUser", res.data);
        } else {
          this.$message.error("个人信息加载失败");
        }
      });

    this.initSocket();

    this.messageCount = this.$store.state.messageCount;
  },

  methods: {
    goDetect() {
      this.$router.push("/home/detection");
    },
    goMessage() {
      this.$router.push("/home/message");
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    handleCommand(command) {
      // 退出登录
      if (command == "logout") {
        // window.sessionStorage.removeItem("token");
        this.$store.commit("changeToken", "");
        this.$store.commit("setUser", null);
        this.$router.push("/");
      } else if ((command = "pwd")) {
        this.pwdVisible = true;
      }
    },
    // 关闭修改密码窗口
    cancelPwd() {
      this.pwdForm = {
        oldPwd: "",
        newPwd: "",
        checkPwd: "",
      };
      this.pwdVisible = false;
    },
    // 提交修改密码信息
    submitPwd() {
      if (
        this.pwdForm.newPwd == "" &&
        this.pwdForm.checkPwd == "" &&
        this.pwdForm.oldPwd == ""
      ) {
        this.$message.error("密码不能为空");
      }
      if (this.pwdForm.newPwd != this.pwdForm.checkPwd) {
        this.$message.error("新密码和确认密码不一致");
      }

      // 请求个人信息
      this.$axios
        .get("user/updatePwd", {
          params: {
            token: this.$store.state.token,
            password: this.pwdForm.oldPwd,
            newPassword: this.pwdForm.newPwd,
          },
        })
        .then((res) => {
          res = res.data;
          if (res.status == 200) {
            this.$message({
              message: "成功",
              type: "success",
            });
            this.cancelPwd();
          } else {
            this.$message.error("修改失败");
          }
        });
    },
    // 初始化socket
    initSocket: function () {
      if (typeof WebSocket === "undefined") {
        alert("您的浏览器不支持socket");
      } else {
        // 实例化socket
        this.socket = new WebSocket(
          `ws://127.0.0.1/webSocket/` + this.$store.state.token
        );
        // 监听socket连接
        this.socket.onopen = this.open;
        // 监听socket错误信息
        this.socket.onerror = this.error;
        // 监听socket消息
        this.socket.onmessage = this.getMessage;
      }
    },
    open: function () {
      console.log("socket连接成功");
    },
    error: function () {
      console.log("socket连接错误");
    },
    getMessage: function (msg) {
      console.log("websocket:", msg.data);
      // this.messageCount = this.messageCount + 1;
      let obj = JSON.parse(msg.data);
      this.$store.commit("addMessage", obj);
      this.$store.commit("addMessageCount");
    },
    send: function (params) {
      this.socket.send(params);
    },
    close: function () {
      console.log("socket已经关闭");
    },
  },
  destroyed() {
    // 销毁监听
    this.socket.onclose = this.close;
  },
  //监听messagecount的数值,使this.messagecount的数值与vuex保持一致
  watch: {
    "$store.state.messageCount": {
      handler: function (newVal, oldVal) {
        console.log("监听messagecount的数值" + newVal);
        this.messageCount = newVal;
      },
    },
  },
};
</script>

<style scoped>
.home {
  height: 100%;
  display: flex;
  flex-direction: row;
}

.left {
  background-color: #f2fcf2;
  color: #333;
  width: 200px;
  overflow-y: auto;
  overflow-x: hidden;
  text-align: center;
}


.container {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #e9eef3;
}

.header {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
}



.img {
  width: 100px;
  height: 100px;
  border-radius: 50px 50px;
}

.name {
  color: #333;
}


.top {
  background-color: #e9eef3;
  height: 55px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.main {
  flex: 1;
  overflow-y: auto;
  background-color: white;
}

.footer {
  height: 25px;
  text-align: center;
  line-height: 25px;
  background-color: #b3c0d1;
  color: #333;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}

.top .item {
  color: #409eff;
  font-size: 16px;
  cursor: pointer;
  text-align: center;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
  font-size: 18px;
}
</style>
