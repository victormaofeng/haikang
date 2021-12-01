<template>
  <div class="player-container">
    <video-player
      v-if="activePlayer == 0"
      class="video-player vjs-custom-skin"
      ref="videoPlayer"
      :playsinline="true"
      :options="playerOption"
      @play="onPlayerPlay($event)"
      @pause="onPlayerPause($event)"
      @ended="onPlayerEnded($event)"
      @waiting="onPlayerWaiting($event)"
      @playing="onPlayerPlaying($event)"
      @loadeddata="onPlayerLoadeddata($event)"
      @timeupdate="onPlayerTimeupdate($event)"
      @canplay="onPlayerCanplay($event)"
      @canplaythrough="onPlayerCanplaythrough($event)"
      @statechanged="playerStateChanged($event)"
      @ready="playerReadied"
    >
    </video-player>

    <video
      class="demo-video"
      ref="flvPlayer"
      v-show="activePlayer == 1"
      autoplay="false"
      controls
    ></video>
  </div>
</template>

<script>
//引入video样式
import "video.js/dist/video-js.css";
import "vue-video-player/src/custom-theme.css";
// flash
import "videojs-flash";

import "videojs-hotkeys";

//引入hls.js
import "videojs-contrib-hls";

import flvjs from "flv.js";

export default {
  name: "player",
  props: {
    // 视频封面
    coverImage: {
      type: String,
      default: "",
    },
    // 视频源
    src: {
      type: String,
      // 默认监控流
      default: "http://60.30.52.41:80/live/zxldd.stream/playlist.m3u8",
    },
    // 视频类型
    type: { type: String, default: "hls" },
  },
  data() {
    return {
      activePlayer: 0, // 0  使用video.js播放器, 1  使用flv.js播放器, 2  使用DPlayer播放器

      player: null,

      playerOption: {
        playbackRates: [0.5, 1.0, 1.5, 2.0, 3.0], // 可选的播放速度
        autoplay: false, // 如果为true,浏览器准备好时开始回放。
        muted: true, // 默认情况下将会消除任何音频。
        loop: false, // 是否视频一结束就重新开始。
        preload: "auto", // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
        language: "zh-CN",
        aspectRatio: "16:9", // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        fluid: true, // 当true时，video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        sources: [
          {
            type: "", // 类型
            src: "", // url地址
          },
        ],
        // techOrder: ["flash"],
        poster: "", // 封面地址
        notSupportedMessage: "此视频暂无法播放，请稍后再试", // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
        controlBar: {
          timeDivider: true, // 当前时间和持续时间的分隔符
          durationDisplay: true, // 显示持续时间
          remainingTimeDisplay: false, // 是否显示剩余时间功能
          fullscreenToggle: true, // 是否显示全屏按钮
        },
      },
    };
  },
  mounted() {
    /**
    type："video/webm" // 可以播放，用ogg也可打开
    type："video/ogg" // 可以播放，用webm也可打开
    type："video/3gp" // 可以播放
    type："video/mp4" // 可以播放
    type："video/avi" // 打不开
    type："video/flv" // 打不开
    type："video/mkv" // 打不开
    type："video/mov" // 打不开
    type："video/mpg" // 打不开
    type："video/swf" // 打不开
    type："video/ts"  // 打不开
    type："video/wmv" // 打不开
    type："video/vob" // 没转化
    type："video/mxf" // 转化出错
    type: "video/rm"  // 转化出错 
    type: "rtmp/mp4"
    type: "rtmp/hls"
  */

    this.playerOption.poster = this.coverImage;

    // mp4视频
    if (this.type == "mp4") {
      this.playerOption.sources[0].src = this.src;
      this.playerOption.sources[0].type = "video/mp4";
    }
    // flv视频,viedo不支持,改用flv.js
    else if (this.type == "flv") {
      // video.js 播放器设置,在浏览器禁用flash的情况下无法播放
      // this.playerOption.sources[0].src = this.src;
      // this.playerOption.sources[0].type = "video/x-flv";
      // this.playerOption.techOrder = ["flash", "html5"];

      // 修改播放器为flv.js
      this.activePlayer = 1;

      // flv.js播放flv格式视频
      if (flvjs.isSupported()) {
        let flvPlayer = this.$refs.flvPlayer;

        if (flvPlayer) {
          this.player = flvjs.createPlayer({
            type: "flv",
            url: this.src,
          });

          this.player.attachMediaElement(flvPlayer);

          try {
            this.player.load();
            this.player.play();
          } catch (error) {
            console.log(error);
            this.player.destory();
          }
        }
      }
    }
    // avi视频
    else if (this.type == "avi") {
      this.playerOption.sources[0].src = this.src;
      this.playerOption.sources[0].type = "video/avi";
    }
    // mkv视频,不支持
    else if (this.type == "mkv") {
      this.playerOption.sources[0].src = this.src;
      this.playerOption.sources[0].type = "video/mkv";
    }
    // hls
    else if (this.type == "hls") {
      this.playerOption.sources[0].src = this.src;
      // this.playerOption.sources[0].withCredentials = true;
      this.playerOption.sources[0].type = "application/x-mpegURL";
      this.playerOption.techOrder = ["html5"];
    }
  },
  beforeDestroy() {
    if (this.activePlayer == 1 && this.player) {
      this.player.destory();
    }
  },
  methods: {
    // 播放回调
    onPlayerPlay(player) {
      // console.log("player play!", player);
      // this.$refs.videoPlayer.player.play()
    },
    // 暂停回调
    onPlayerPause(player) {
      console.log("player pause!", player);
      // this.$refs.videoPlayer.player.pause()
    },
    // 视频播完回调
    onPlayerEnded($event) {
      //console.log(player);
    },
    // DOM元素上的readyState更改导致播放停止
    onPlayerWaiting($event) {
      //console.log(player);
    },
    // 已开始播放回调
    onPlayerPlaying($event) {
      //console.log(player);
    },
    // 当播放器在当前播放位置下载数据时触发
    onPlayerLoadeddata($event) {
      // console.log(player);
    },
    // 当前播放位置发生变化时触发。
    onPlayerTimeupdate($event) {
      //console.log(player);
    },
    //媒体的readyState为HAVE_FUTURE_DATA或更高
    onPlayerCanplay(player) {
      // console.log('player Canplay!', player)
    },
    //媒体的readyState为HAVE_ENOUGH_DATA或更高。这意味着可以在不缓冲的情况下播放整个媒体文件。
    onPlayerCanplaythrough(player) {
      // console.log('player Canplaythrough!', player)
    },
    //播放状态改变回调
    playerStateChanged(playerCurrentState) {
      // console.log("player current update state", playerCurrentState);
    },
    //将侦听器绑定到组件的就绪状态。与事件监听器的不同之处在于，如果ready事件已经发生，它将立即触发该函数。。
    playerReadied(player) {
      // console.log("example player 1 readied", player);
    },
  },
};
</script>
<style scoped>
.video-js .vjs-icon-placeholder {
  width: 100%;
  height: 100%;
  display: block;
}

.demo-video {
  width: 100%;
  height: 100%;
  display: block;
}
</style>