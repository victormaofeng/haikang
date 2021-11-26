<template>
  <div>
    <div class="main">
      <div class="main1">
      
      <d-player
        :src="src1"
        :live="true"
        :autoplay="true"
        :type="type"
        class="img"
        v-show="play3"
      />
	  <h3 v-show="play3">原视频</h3>
      
      <d-player
        :src="src"
        :live="true"
        :autoplay="true"
        :type="type"
        class="img"
        v-show="play4"
      />
	  <h3 v-show="play4">生成视频</h3>
	  
	  
      <img :src="src1" class="img" v-show="play1" />
      <img :src="src" class="img" v-show="play"  />
     <img :src="src2" class="img" v-show="play2" /> 
	 
      </div>
      <div class="wenzi">
          <h3 class="pword" v-show="play1">原图片</h3>
          <h3 class="pword" v-show="play">生成图片</h3>
          <h3 v-show="play2">目标人物</h3>
      </div>
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

import EZUIKit from "@/components/EZUIKitJs";
import player from "@/components/player.vue";
import dPlayer from "@/components/dplayer.vue";

//这是一个专门针对检测结果，重识别结果界面做的一个组件。
// 信息流
export default {
  name: "feed1",
  data() {
    return {
      play: true,
      play1: true,
      play2: true,
      play3: true,
      play4: true,
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
	
	  this.src="http://127.0.0.1"+this.src
  	this.src1="http://127.0.0.1"+this.src1
	  this.src2="http://127.0.0.1"+this.src2
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
      //如果格式为图片，则采取img显示；如果格式不是图片，则采取dplayer显示。
      if(this.type == 'jpg' || this.type == 'png' || this.type== 'jpeg'){
         this.play4 = ! this.play4
         this.play3 = ! this.play3
        if(this.src == '' || this.src == null ){
                this.play = ! this.play
        } 
        if(this.src1 == '' || this.src1 == null){  
                this.play1 = ! this.play1
        }
      }else{
        this.play1 = ! this.play1
        this.play = ! this.play
        if(this.src == '' || this.src == null ){
                this.play4 = ! this.play4
        } 
        if(this.src1 == '' || this.src1 == null){  
                this.play3 = ! this.play3
        }
      }
      // 如果不是重识别，则不显示目标图像
      if(this.src2 == '' || this.src2 == null){
                this.play2 = ! this.play2
        }        
    }

  },
  components: {
    EZUIKit,
    player,
    dPlayer,
    },
};
</script>
<style scoped>
.main {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.main1 {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.wenzi{
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
.pword{
  /* display: flex;
  align-items: center;
  align-self: auto; */
}
.img {
  /* height: 75px;
  width: 400px; */
  height: 300px;
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