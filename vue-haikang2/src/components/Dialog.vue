<template>
    <el-dialog title="检测内容" :visible.sync="dialogFormVisible">
        <h3>原图片：</h3><img :src="src" class="img" v-if="src != ''" />
        <h3>生成图片：</h3><img :src="src1" class="img" v-if="src != ''" />
        <h3>目标人物图片：</h3><img :src="src2" class="img" v-if="src != ''" />
        <div class="content">
        <div class="title">检测标题：  {{ title }}</div>
        <div class="desc">
        <div>检测简介：   {{ desc }}</div>
        <div>插入时间：   {{ time }}</div>
        </div>
        </div>
        <el-button @click="dialogFormVisible = false">关 闭</el-button>
    </el-dialog>
</template>

<script>

// 信息流 这是专门给列表展示做的一个模板
export default {
  name: "Dialog",
  data() {
    return {
      dialogFormVisible: false,
    };
  },
  props: {
    // 封面地址 生成的图片
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
    this.src=image;
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
.edit{
  padding: 0 0;
  margin: 0 0;
}
</style>