<template>
<div>
  <el-breadcrumb separator-class="el-icon-arrow-right" class="size box bg">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>我的消息</el-breadcrumb-item>
    </el-breadcrumb>
  <div class="main box">
    
    <!-- <el-row class="row">
      <el-col :span="20" :offset="2"> -->
          <!-- <el-form
          ref="form"
          :model="form"
          label-width="100px"
          size="medium"
          action=""
        >
          <el-form-item label="标题">
            <div>{{title}}title</div>
          </el-form-item>

          <el-form-item label="介绍">
            <div>{{content}}content</div>
          </el-form-item>

          <el-form-item  v-show="play1" label="原图片">
            <div>图片</div>
            <img :src="src1" class="img" v-if="src != ''" />
          </el-form-item>

          <el-form-item v-show="play" label="生成图片">
            <div>图片</div>
            <img :src="src" class="img" v-if="src != ''" />
          </el-form-item>

          <el-form-item v-show="play2" label="目标人物">
            <div>图片</div>
            <img :src="src2" class="img" v-if="src != ''" />
          </el-form-item>
          
    
        </el-form> -->
    <el-row>
      <el-col :span="8" class="flexbox1" >
        <el-card :body-style="{ padding: '0px' }" v-show="play1">
          <img src="https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png" class="image">
          <div style="padding: 14px;">
            <span>原图片</span>
            <div class="bottom clearfix">
              <time class="time">{{ insterTime1 }}</time>
              <!-- <el-button type="text" class="button">操作按钮</el-button> -->
            </div>
          </div>
        </el-card>
        <el-card :body-style="{ padding: '0px' }" v-show="play">
          <img src="https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png" class="image">
          <div style="padding: 14px;">
            <span>生成图片</span>
            <div class="bottom clearfix">
              <time class="time">{{ insterTime }}</time>
              <!-- <el-button type="text" class="button">操作按钮</el-button> -->
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8"  class="flexbox2">
        <el-card :body-style="{ padding: '0px' }" v-show="play2">
          <img src="https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png" class="image">
          <div style="padding: 14px;">
            <span>目标人物</span>
            <div class="bottom clearfix">
              <time class="time">{{ insterTime1 }}</time>
              <!-- <el-button type="text" class="button">操作按钮</el-button> -->
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- <div style="width: 100%;">
    </div> -->
    <!-- <div class="flexbox1">
      <h3 style="width:10%" v-show="play1">原图片：</h3><img :src="src1" class="img1" v-if="src != ''" />
      <h3 style="width:10%" v-show="play">生成图片：</h3><img :src="src" class="img1" v-if="src != ''" /> 
    </div> -->
    <!-- <div class="flexbox2" v-show="play2" >
      <h3 style="width:10%" >目标人物：</h3><img :src="src2" class="img2" v-if="src != ''" />
    </div> -->
    <div class="flexbox1">
      <h3  style="width:10%">标题：</h3><div class="item2">{{title}}</div>
    </div>
    <div class="flexbox1">
      <h3  style="width:10%">介绍：</h3><div class="item2">{{content}}</div>
    </div>
    
     
      
      <!-- <h3 v-show="play2">目标人物：</h3> <img :src="src2" class="img" v-if="src != ''" />
      <h3>标题：</h3> <div class="item2">item4</div>
      <h3>介绍：</h3> <div class="item2">item5</div> 
      <div class="item1">item1</div>
      <div class="item1">item2</div>
      <div class="item2">item3</div>   -->

      <!-- </el-col>
    </el-row> -->

  </div>
</div>  
</template>

<script>
    export default{
        data(){
            return{
                title: this.$route.query.message.title,
                content: this.$route.query.message.content,
                src: this.$route.query.message.path,
                src1: this.$route.query.message.path1,
                src2: this.$route.query.message.path2,
                insterTime: this.$route.query.message.insertTime,
                insterTime1: this.$route.query.message.insertTime1,
                play: true,
                play1: true,
                play2: true,
            };
        },
        created(){
            console.log(this.$route.query);
            this.load();
        },
        methods:{
            load(){
                if(this.src == '' || this.src == null){
                        this.play = ! this.play
                } 
                if(this.src1 == '' || this.src1 == null){
                        this.play1 = ! this.play1
                }
                if(this.src2 == '' || this.src2 == null){
                        this.play2 = ! this.play2
                }
                
            }
        }

    }
</script>

<style scoped>

.row {
  margin-top: 20px;
}


.size {
  font-size: 15px;
}

.box {
  padding: 10px 15px;
  margin: 10px 15px;
  border-radius: 5px 5px;
}

.pad {
  padding: 10px 10px;
  border-radius: 5px 5px;
  margin: 10px 0px;
}

.shadow{
    box-shadow: 1px 1px 2px #e9eef3;
}

.bg {
  background-color: #e9eef3;
}
.main{
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  /* item太多，一行放不下的时候，换行 */
  flex-wrap: wrap;
  /* 纵向平均分布*/
  align-content: space-between;
}
.flexbox1{
  /* background-color: blueviolet; */
  display: flex;
  flex-direction: row;
  /* 横向平均分布*/
  justify-content: space-between;
  width: 100%;
}
.flexbox2{
  /* background-color: blueviolet; */
  display: flex;
  flex-direction: row;
  width: 100%;
  /* 竖轴居中  */
  align-items: center;
}
.img1{
  width:40%;
  background-color:brown;
  text-align: center;
  height: 225px;
  width: 300px;
}
.item2{ 
   width: 100%;
   /* background-color:brown; */
   text-align: center;
}
.img2{
   height: 225px;
   width: 300px;
   background-color:brown;
   text-align: center;
}

.time {
    font-size: 13px;
    color: #999;
  }
  
  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }
  
  .clearfix:after {
      clear: both
  }
</style>
