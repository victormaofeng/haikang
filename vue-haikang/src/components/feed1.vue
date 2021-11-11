<template>
  <div>
    <div class="main">
      <h3 v-show="play1">原图片</h3><img :src="src1" class="img" v-show="play1" />
      <h3 v-show="play">生成图片</h3><img :src="src" class="img" v-show="play"  />
      <h3 v-show="play2">目标人物</h3><img :src="src2" class="img" v-show="play2"  />

        <div class="content">
        <div class="title">{{ title }}</div>
        <div class="desc">
          <div>上传时间:{{ desc }}</div>
          <div>生成时间:{{ time }}</div>
        </div>
      </div>
      <div class="close" v-if="closeale">
        <!-- <el-button type="text" class="close" @click="close"
          ><i class="el-icon-close"></i
        ></el-button>        -->
      </div>
    </div>
  </div>
</template>
<script>

//这是一个专门针对检测结果，重识别结果界面做的一个组件。
// 信息流
export default {
  name: "feed1",
  data() {
    return {
      play: true,
      play1: true,
      play2: true,
    };
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
    //目标人物的图片
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
    type:{
      type: String,
      default:"",
    },
    // 是否可关闭
    closeale: {
      type: Boolean,
      // 默认监控流
      default: true,
    },
  },

  created(){
    this.load();
  },
  methods: {
    // close() {
    //   this.$emit("close");
    // },
    // edit(item){
    //   this.$emit("edit");
    // },
    load(){
      window.console.log(this.type);
      if(this.type == 'jpg' || this.type == 'png' || this.type== 'jpeg'){
        if(this.src == '' || this.src == null ){
                this.play = ! this.play
        } 
        if(this.src1 == '' || this.src1 == null){
                this.play1 = ! this.play1
        }
        if(this.src2 == '' || this.src2 == null){
                this.play2 = ! this.play2
        }
      }else{
        this.play = ! this.play
        this.play1 = ! this.play1
        this.play2 = ! this.play2
      }        
    }

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