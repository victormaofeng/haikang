<template>
  <div class="player-container">
    <div ref="dplayer"></div>
  </div>
</template>

<script>
import Hls from "hls.js";

import flvjs from "flv.js";

import logoImg from "@/assets/logo.png";

import DPlayer from "dplayer";

export default {
  name: "dplayer",
  props: {
    // 视频封面
    coverImage: {
      type: String,
      default: "",
    },

    autoplay: {
      type: Boolean,
      default: false,
    },

    live: {
      type: Boolean,
      default: false,
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
      dp: null,
      playType: {
        flv: "customFlv",
        hls: "customHls",
        mp4: "mp4",
        avi: "avi",
      },
    };
  },
  mounted() {
    this.dp = new DPlayer({
      container: this.$refs.dplayer,
      autoplay: this.autoplay,
      theme: "#FADFA3",
      loop: false,
      live: this.live,
      lang: "zh-cn",
      screenshot: true,
      playbackSpeed: [0.5, 0.75, 1, 1.25, 1.5, 2],
      hotkey: true,
      preload: "auto",
      logo: logoImg,
      volume: 0.5, // 音量大小  0~1
      mutex: false, // 默认true , 互斥，阻止多个播放器同时播放，当前播放器播放时暂停其他播放器
      video: {
        url: this.src,
        pic: this.coverImage,
        // thumbnails: "",
        type: this.playType[this.type],
        customType: {
          customFlv: function (video, player) {
            const flvPlayer = flvjs.createPlayer({
              type: "flv",
              url: video.src,
            });
            flvPlayer.attachMediaElement(video);
            flvPlayer.load();
          },
          customHls: function (video, player) {
            const hls = new Hls();
            hls.loadSource(video.src);
            hls.attachMedia(video);
          },
        },
      },
      // 插件
      //   pluginOptions: {
      //     flv: {
      //       // refer to https://github.com/bilibili/flv.js/blob/master/docs/api.md#flvjscreateplayer
      //       mediaDataSource: {
      //         // mediaDataSource config
      //       },
      //       config: {
      //         // config
      //       },
      //     },
      //     hls: {
      //       // hls config
      //     },
      //   },

      // 视频字幕
      //   subtitle: {
      //     url: "dplayer.vtt",
      //     type: "webvtt",
      //     fontSize: "25px",
      //     bottom: "10%",
      //     color: "#b7daff",
      //   },

      //弹幕
      // danmaku: {
      //   id: "9E2E3368B56CDBB4",
      //   api: "https://api.prprpr.me/dplayer/",
      //   token: "tokendemo",
      //   maximum: 1000,
      //   addition: ["https://api.prprpr.me/dplayer/v3/bilibili?aid=4157142"],
      //   user: "DIYgod",
      //   bottom: "15%",
      //   unlimited: true,
      // },

      //   danmaku: true,

      // 右键菜单
      //   contextmenu: [
      //     {
      //       text: "custom1",
      //       link: "https://www.baidu.com",
      //     },
      //     {
      //       text: "custom2",
      //       click: (player) => {
      //         this.sendDanmu();
      //       },
      //     },
      //   ],

      highlight: [
        {
          text: "marker for 20s",
          time: 20,
        },
        {
          text: "marker for 2mins",
          time: 120,
        },
      ],
    });
  },
  beforeDestroy() {
    if (this.dp) {
      this.dp.destroy();
    }
  },
  methods: {
    sendDanmu: function (text, color, type) {
      this.dp.danmaku.send(
        {
          text: "dplayer is amazing",
          color: "#ff0000",
          type: "bottom", // should be `top` `bottom` or `right`
        },
        function () {
          console.log("success");
        }
      );
    },
  },
};
</script>
<style scoped>
</style>