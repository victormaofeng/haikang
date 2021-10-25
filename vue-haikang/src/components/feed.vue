<template>
  <div>
    <div class="main">
      <img :src="src" class="img" v-if="src != ''" />
      <div class="content">
        <div class="title">{{ title }}</div>
        <div class="desc">
          <div>{{ desc }}</div>
          <div>{{ time }}</div>
        </div>
      </div>
      <div class="close" v-if="closeale">
        <el-button type="text" class="close" @click="close"
          ><i class="el-icon-close"></i
        ></el-button>
      </div>
    </div>
  </div>
</template>
<script>
// 信息流
export default {
  name: "feed",
  data() {
    return {};
  },
  props: {
    // 封面地址
    src: {
      type: String,
      default: "",
    },
    title: {
      type: String,
      // 默认监控流
      default: "",
    },
    desc: {
      type: String,
      // 默认监控流
      default: "",
    },
    time: {
      type: String,
      // 默认监控流
      default: "",
    },
    // 是否可关闭
    closeale: {
      type: Boolean,
      // 默认监控流
      default: true,
    },
  },
  methods: {
    close() {
      this.$emit("close");
    },
  },
  components: {},
};
</script>
<style scoped>
.main {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.img {
  height: 75px;
  width: 100px;
}
.content {
  height: 75px;
  flex: 1;
  display: flex;
  flex-direction: column;
}
.title {
  font-size: 15px;
  flex: 3;
  color: #606266;
  text-overflow: ellipsis;
}

.desc {
  font-size: 14px;
  flex: 2;
  color: #909399;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  text-overflow: ellipsis;
}

.close {
  padding: 0 0;
  margin: 0 0;
}
</style>