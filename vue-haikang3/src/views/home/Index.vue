<template>
  <div class="main">
    <el-breadcrumb separator-class="el-icon-arrow-right" class="size box bg">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>监控</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="list">
      <d-player
        src="http://60.30.52.41:80/live/zxldd.stream/playlist.m3u8"
        :live="true"
        :autoplay="true"
        type="hls"
        class="item"
      />


      <d-player v-if="src!=''"
        :src="src"
        type="hls"
        :live="true"
        class="item"
      />

      <d-player
        :src="src" v-if="src!=''"
        type="hls"
        :live="true"
        class="item"
      />
    </div>
  </div>
</template>
<script>
import EZUIKit from "@/components/EZUIKitJs";
import player from "@/components/player.vue";
import dPlayer from "@/components/dplayer.vue";

export default {
  name: "index",
  data() {
    return {
      list: [],
      src: "",
    };
  },
  components: {
    EZUIKit,
    player,
    dPlayer,
  },
  created(){
    this.getUrl();
  },
  methods: {
    getDetectedFile: function () {
      this.$axios("detectedFile/gets", {
        params: {
          page: 1,
          pageSize: 10,
        },
      }).then((res) => {
        res = res.data;

        if (res.status == 200) {
          this.list = res.data;
        } else {
          this.$message.error("请求错误");
        }
      });
    },
    getUrl(){
      this.$axios("haikang/1/2/2",{

      }).then((res) =>{
        res = res.data;
        if(res.status == 200){
           console.log("+++++++++++++++++++++++++++++=");
          // console.log(res);
          this.src = res.data.url;
           console.log(this.src);
        }else{
          this.$message.error("请求错误");
        }
      });
    },
  },
};
</script>
<style scoped>
.main {
  display: flex;
  flex-direction: column;
}

.list {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: space-around;
}

.item {
  width: 400px;
  height: 300px;
  margin: 10px 10px;
}

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
</style>