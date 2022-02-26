<template>
  <div class="main">
    <div style="height: 30px; line-height: 30px; font-size: 20px">
      {{ msg }}
    </div>
    <div id="video-container" style="width: 640px; height: 660px"></div>
  </div>
</template>

<script>

// 海康提供的组件
import EZUIKit from "ezuikit-js";

export default {
  name: "EZUIKit",
  data() {
    return {
      // 开发者和摄像头信息
      cameraDeveloper: null
    };
  },
  props: {
    msg: String,
    // 监控模式
    mode: {
      type: String,
      default: "standard",
    },
    // 摄像头id
    camera: Number
  },
  methods: {
    loadLive: function () {
      let that = this;
      let player = new EZUIKit.EZUIKitPlayer({
        autoplay: true,
        id: "video-container",
        accessToken: that.cameraDeveloper.developer.accessToken,
        url:
          "ezopen://open.ys7.com/" + that.cameraDeveloper.camera.serialNumber + "/1.hd.live",
        template: that.mode, // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
        // 视频上方头部控件
        header: ["capturePicture", "save", "zoom"], // 如果templete参数不为simple,该字段将被覆盖
        plugin: ["talk"], // 加载插件，talk-对讲
        // 视频下方底部控件
        footer: ["talk", "broadcast", "hd", "fullScreen"], // 如果template参数不为simple,该字段将被覆盖
        audio: 1, // 是否默认开启声音 0 - 关闭 1 - 开启
        openSoundCallBack: (data) => window.console.log("开启声音回调", data),
        closeSoundCallBack: (data) => window.console.log("关闭声音回调", data),
        startSaveCallBack: (data) => window.console.log("开始录像回调", data),
        stopSaveCallBack: (data) => window.console.log("录像回调", data),
        capturePictureCallBack: (data) =>
          window.console.log("截图成功回调", data),
        fullScreenCallBack: (data) => window.console.log("全屏回调", data),
        getOSDTimeCallBack: (data) =>
          window.console.log("获取OSDTime回调", data),
        width: 600,
        height: 400,
      });
      window.console.log("player", player);
      //   player.stop();
    },
  },
  mounted: function () {
    let that = this;
    this.$axios.get("haikang/" + this.camera)
    .then((res) => {
      let result = res.data;
      window.console.log(result);
      if (result.status == 200) {
        that.cameraDeveloper = result.data;
        that.loadLive();
      }
    });
  }
};
</script>
<style scoped>
.main {
  width: 100%;
  height: 100%;
}
</style>
