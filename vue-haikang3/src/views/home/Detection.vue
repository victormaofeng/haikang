<template>
  <div class="main">
    <el-breadcrumb separator-class="el-icon-arrow-right" class="size box bg">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>我的历史</el-breadcrumb-item>
    </el-breadcrumb>

    <el-tabs v-model="activeName" @tab-click="tabChange" class="box">
      <el-tab-pane name="detect">
        <span slot="label" class="size">行人检测</span>

        <div v-if="detectPage.list && detectPage.list.length > 0">
          <div v-for="item in detectPage.list" :key="item.id">
            <feed
              :title="item.title"
              :desc="item.content"
              :time="item.insertTime"
              class="pad shadow"
               @close="close(item.id)"
            ></feed>
          </div>


          <el-pagination
            background
            layout="prev, pager, next"
            :total="detectPage.count"
            :current-page="detectPage.page"
            @current-change="pageChange"
          >
          </el-pagination>
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
              @close="close"
            ></feed>
          </div>


          <el-pagination
            background
            layout="prev, pager, next"
            :total="reidPage.count"
            :current-page="reidPage.page"
            @current-change="pageChange"
          >
          </el-pagination>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>

import feed from "@/components/feed.vue";
// 视频
export default {
  name: "Detection",
  data() {
    return {
      activeName: "detect",
      detectPage: { page: 1, pageSize: 10, list: [], count: 0 },
      reidPage: { page: 1, pageSize: 10, list: [], count: 0 },
      token: "",
    };
  },

  components: {
    feed,
  },
  created() {
    this.token = this.$store.state.token;
    this.load();
  },
  methods: {

    close(id) {
      window.console.log(id);
    },
    pageChange(currentPage) {
      if (this.activeName == "detect") {
        this.detectPage.page = currentPage;
      } else if (this.activeName == "reid") {
        this.reidPage.page = currentPage;
      }
      this.load();
    },
    tabChange(tab, event) {
      this.load();
    },
    load() {
      if (this.activeName == "detect") {
        this.$axios
          .get("detect/list", {
            params: {
              pageSize: this.detectPage.pageSize,
              page: this.detectPage.page,
              type: 1,
            },
          })
          .then((res) => {
            res = res.data;
            if (res.status == 200) {
              this.detectPage.list = res.data.list;
              this.detectPage.count = res.data.count;
            } else {
              this.$message.error("数据加载失败");
            }
          });
      } else if (this.activeName == "reid") {
        this.$axios
          .get("detect/list", {
            params: {
             // token: this.$store.state.token,
              pageSize: this.reidPage.pageSize,
              page: this.reidPage.page,
              type: 2,
            },
          })
          .then((res) => {
            res = res.data;
            if (res.status == 200) {
              this.reidPage.list = res.data.list;
              this.reidPage.count = res.data.count;
            } else {
              this.$message.error("数据加载失败");
            }
          });
      }
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

.shadow{
    box-shadow: 1px 1px 2px #e9eef3;
}

.bg {
  background-color: #e9eef3;
}
</style>