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
              :src ="item.path"
              :title="item.title"
              :desc="item.content"
              :time="item.insertTime"
              class="pad shadow"
               @close="close(item.id)"
            ></feed>
            <el-button type="text" icon="el-icon-edit" @click="getDetail(item)">
            </el-button>
          </div>


          <el-pagination
            background
            layout="prev, pager, next"
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
              :time="item.insertTime"
              @close="close"
            ></feed>
            <el-button type="text" icon="el-icon-edit" @click="getDetail(item)">
            </el-button>
          </div>


          <el-pagination
            background
            layout="prev, pager, next"
          >
          </el-pagination>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
      
      let detectList = [];
      let reidList = [];
import feed from "@/components/feed.vue";
// 视频
export default {
  
  name: "Detection",
  data() {
    return {
      activeName: "detect",
      detectPage: { list: detectList, },
      reidPage: { list: reidList, },
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
    // pageChange(currentPage) {
    //   if (this.activeName == "detect") {
    //     this.detectPage.page = currentPage;
    //   } else if (this.activeName == "reid") {
    //     this.reidPage.page = currentPage;
    //   }
    //   // this.load();
    // },
    tabChange(tab, event) {
      this.load();
    },
    load(){
      let a = this.$store.state.messages;
      detectList.length = 0;
      reidList.length = 0;
      for (var i = 0; i < a.length; i ++) { 
        if(a[i].path2 == '' || a[i].path2 == null){
          detectList.unshift(a[i]);
        }else{
          reidList.unshift(a[i]);
        }
      }
      console.log(detectList);
      console.log(reidList);
    },
    // load() {
    //   if (this.activeName == "detect") {
    //     this.$axios
    //       .get("detect/list", {
    //         params: {
    //           pageSize: this.detectPage.pageSize,
    //           page: this.detectPage.page,
    //           type: 1,
    //         },
    //       })
    //       .then((res) => {
    //         res = res.data;
    //         if (res.status == 200) {
    //           this.detectPage.list = res.data.list;
    //           this.detectPage.count = res.data.count;
    //         } else {
    //           this.$message.error("数据加载失败");
    //         }
    //       });
    //   } else if (this.activeName == "reid") {
    //     this.$axios
    //       .get("detect/list", {
    //         params: {
    //          // token: this.$store.state.token,
    //           pageSize: this.reidPage.pageSize,
    //           page: this.reidPage.page,
    //           type: 2,
    //         },
    //       })
    //       .then((res) => {
    //         res = res.data;
    //         if (res.status == 200) {
    //           this.reidPage.list = res.data.list;
    //           this.reidPage.count = res.data.count;
    //         } else {
    //           this.$message.error("数据加载失败");
    //         }
    //       });
    //   }
    // },
       getDetail(item){
       this.$router.push({path:'/home/messageIndex',query:{message:item}});
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