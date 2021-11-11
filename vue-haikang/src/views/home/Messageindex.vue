<template>
<div>
  <el-breadcrumb separator-class="el-icon-arrow-right" class="size box bg">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>我的消息</el-breadcrumb-item>
    </el-breadcrumb>
  <div class="main">
    
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
    <div class="flexbox1">
      <h3 style="width:10%" v-show="play1">原图片：</h3><img :src="src1" class="img1" v-if="src != ''" />
      <h3 style="width:10%" v-show="play">生成图片：</h3><img :src="src" class="img1" v-if="src != ''" /> 
    </div>
    <div class="flexbox2" v-show="play2" >
      <h3 style="width:10%" >目标人物：</h3><img :src="src2" class="img2" v-if="src != ''" />
    </div>
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
  flex-direction: row;
  /* item太多，一行放不下的时候，换行 */
  flex-wrap: wrap;
  /* 纵向平均分布*/
  align-content: space-between;
}
.flexbox1{
  background-color: blueviolet;
  display: flex;
  flex-direction: row;
  /* 横向平均分布*/
  justify-content: space-between;
  width: 100%;
}
.flexbox2{
  background-color: blueviolet;
  display: flex;
  flex-direction: row;
  width: 100%;
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
   background-color:brown;
   text-align: center;
}
.img2{
   height: 225px;
   width: 300px;
   background-color:brown;
   text-align: center;
}
</style>
