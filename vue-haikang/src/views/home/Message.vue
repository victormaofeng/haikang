<template>
  <div class="main">
    <el-breadcrumb separator-class="el-icon-arrow-right" class="size box bg">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>我的消息</el-breadcrumb-item>
    </el-breadcrumb>

    <el-tabs v-model="activeName" @tab-click="tabChange" class="box">
      <el-tab-pane name="detect">
        <span slot="label" class="size">行人检测</span>

        <div v-if="detectPage.list && detectPage.list.length > 0">
          <div v-for="item in detectPage.list" :key="item.id">
            <feed
              :src="item.path"
              :title="item.title"
              :desc="item.content"
              :time="item.insertTime1"
              class="pad shadow"
              @close="close(item.id)"
              @edit="getDetail(item)"
            ></feed>
            <!-- <el-button type="text" icon="el-icon-edit" @click="getDetail(item)">
            </el-button> -->
          </div>

          <!-- <el-pagination
            background
            layout="prev, pager, next"
          >
          </el-pagination> -->
        </div>
      </el-tab-pane>

      <el-tab-pane name="reid">
        <span slot="label" class="size">行人重识别</span>
        <div v-if="reidPage.list && reidPage.list.length > 0">
          <div v-for="item in reidPage.list" v-bind:key="item.id">
            <feed
              :title="item.title"
              :desc="item.content"
              :src="item.path"
              :time="item.insertTime1"
              @close="close"
              @edit="getDetail(item)"
            >
            </feed>
          </div>

          <!-- <el-pagination
            background
            layout="prev, pager, next"
          >
          </el-pagination> -->
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import feed from "@/components/feed.vue";
// 视频
export default {
  name: "Message",
  data() {
    return {
      activeName: "detect",
      detectPage: { list: [] },
      reidPage: { list: [] },
      token: "",
    };
  },
  components: {
    feed,
  },
  created() {
    this.$store.commit("setMessageCount", 0);

    this.token = this.$store.state.token;
    this.load();
  },
  methods: {
    close(id) {
      window.console.log(id);
    },

    tabChange(tab, event) {
      this.load();
    },
    load() {
      // 初始化
      this.reidPage.list = [];
      this.detectPage.list = [];
      let messages = this.$store.state.messages;
      console.info(
        "this.$store.state.messages",
        this.$store.state.messagesages
      );
      console.info("messages", messages);

      for (var i = 0; i < messages.length; i++) {
        if (messages[i].style) {

          this.detectPage.list.unshift(messages[i]);
        } else {
          this.reidPage.list.unshift(messages[i]);
        }
      }
    },

    getDetail(item) {
      this.$router.push({
        path: "/home/messageIndex",
        query: { message: item },
      });
    },
  },
};
</script>
<style scoped>
.size {
  font-size: 15px;
}

.box {
  padding: 10px 15px;
  margin: 10px 15px;
  border-radius: 5px 5px;
}

.pad {
  padding: 10px 10px;
  border-radius: 5px 5px;
  margin: 10px 0px;
}

.shadow {
  box-shadow: 1px 1px 2px #e9eef3;
}

.bg {
  background-color: #e9eef3;
}

.close {
  padding: 0 0;
  margin: 0 0;
}
</style>