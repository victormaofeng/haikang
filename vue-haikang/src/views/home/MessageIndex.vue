<template>
    <div class="main">
        <el-breadcrumb separator-class="el-icon-arrow-right" class="size box bg">
        <el-breadcrumb-item>首页</el-breadcrumb-item>
        <el-breadcrumb-item>消息管理</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="box">
          
            <div><h1>{{title}}</h1></div>
            <feed
              :title="title"
              :src1="src1"
              :src="src"
              :src2="src2"
              :desc="insertTime1"
              :time="insertTime"
              :type="type"
            ></feed>
        </div>


          <!-- <el-pagination
            background
            layout="prev, pager, next"
            :total="detectPage.count"
            :current-page="detectPage.page"
            @current-change="pageChange"
          >
          </el-pagination> -->
       

    </div>
</template>

<script>
import feed from "@/components/feed1.vue";
export default {
  data() {
    return {
      title: this.$route.query.message.title,
      content: this.$route.query.message.content,
      //生成文件
      src: this.$route.query.message.path,
      //源文件
      src1: this.$route.query.message.path1,
      //目标人物
      src2: this.$route.query.message.path2,
      //完成检测插入的时间
      insterTime: this.$route.query.message.insertTime,
      //上传的时间
      insterTime1: this.$route.query.message.insertTime1,
      //文件类型
      type: this.$route.query.message.type,

    };
  },
  components:{
        feed,
  },
  created() {
    console.log(this.$route.query);
    
  },
  methods: {
  }
}
</script>

<style scoped>


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

.shadow {
  box-shadow: 1px 1px 2px #e9eef3;
}

.bg {
  background-color: #e9eef3;
}

</style>
