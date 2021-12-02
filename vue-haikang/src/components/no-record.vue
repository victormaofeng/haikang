<template>
  <div class="main">
    <img :src="path" class="img" />
    <div class="text">暂无记录</div>
  </div>
</template>
<script>
import img from "@/assets/no_record.png";

export default {
  data() {
    return {
      path: img,
    };
  },
  mounted() {},
};
</script>
<style scoped>
.main {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.img {
  width: 200px;
  height: 200px;
}
.text {
  color: #409eff;
  font-size: 20px;
  text-align: center;
}
</style>