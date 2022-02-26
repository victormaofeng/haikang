<template>
  <div>
    <video class="demo-video" ref="player"></video>
  </div>
</template>
<script>
// b站开源播放器
import flvjs from "flv.js";

export default {
  data() {
    return {
      player: null,
    };
  },
  props: {
    src: {
      type: String,
      // 默认播放源
      default: "",
    },
  },
  mounted() {
    if (flvjs.isSupported()) {
      let video = this.$refs.player;
      if (video) {
        this.player = flvjs.createPlayer({
          type: "flv",
          url: this.src,
        });
        this.player.attachMediaElement(video);
        try {
          this.player.load();
          this.player.play();
        } catch (error) {
          console.log(error);
          this.player.destory();
        }
      }
    }
  },
  beforeDestroy() {
    this.player.destory();
  },
};
</script>
<style>
</style>