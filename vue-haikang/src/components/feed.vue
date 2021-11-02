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
        <!-- <el-button type="text"  icon="el-icon-edit" 
          ></el-button> -->
          <!-- <el-button type="text" class="edit"  @click="dialogFormVisible = true">
            <i class="el-icon-deit"></i>
          </el-button> -->
          <!-- <el-button type="text" @click="dialogFormVisible = true">打开嵌套表单的 Dialog</el-button> -->
        
      </div>
    </div>
  </div>
</template>
<script>
// import image from "@/assets/logo.png" 
// import Dialog from "@/components/Dialog.vue";

// 信息流
export default {
  name: "feed",
  data() {
    return {};
  },
  props: {
    // 封面地址 生成的图片   暂时先先写上这样的备注，写完业务后再删除
    src: {
      type: String,
      default: "",
    },
    //原来的图片
    src1: {
      type: String,
      // 默认监控流
      default: "",
    },
    //目标任务的图片
    src2: {
      type: String,
      // 默认监控流
      default: "",
    },
    title: {
      type: String,
      // 默认监控流
      default: "",
    },
    desc: {
      type: String,
      // 默认监控流 内容
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
  created(){
    // this.src=image;
  },
  methods: {
    close() {
      this.$emit("close");
    },

  },
  components: {
    // Dialog,
    },
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
.edit{
  padding: 0 0;
  margin: 0 0;
}
</style>