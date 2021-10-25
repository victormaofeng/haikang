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

      <d-player
        src="https://open.ys7.com/v3/openlive/F02718370_1_1.m3u8?expire=1664766417&id=366541977629523968&t=ad2f4f09cdf97eacfb27302049a1e6f62f3ba8c38016f3cc94b965ab686ab30e&ev=100"
        type="hls"
        :live="true"
        class="item"
      />

      <d-player
        src="https://rtmp01open.ys7.com:9188/v3/openlive/F02718370_1_1.flv?expire=1664766619&id=366542826972753920&t=ed9ce2a12708e2e74c3bbc3f8d0b7f55ea4d491cf73fc8580838409350eb8d6c&ev=100"
        type="flv"
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
    };
  },
  components: {
    EZUIKit,
    player,
    dPlayer,
  },
  onShow() {},
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