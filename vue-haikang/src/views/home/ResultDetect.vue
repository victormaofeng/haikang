<template>
    <div class="main">
        <el-breadcrumb separator-class="el-icon-arrow-right" class="size box bg">
        <el-breadcrumb-item>首页</el-breadcrumb-item>
        <el-breadcrumb-item>检测结果</el-breadcrumb-item>
        </el-breadcrumb>

        <div v-if="detectPage.list && detectPage.list.length > 0" class="box">
          <div v-for="item in detectPage.list" :key="item.id">
            <feed
              :title="item.title"
              :desc="item.content"
              :time="item.insertTime"
              class="pad shadow"
               @close="close(item.id)"
               @edit="getDetail(item.id)"
            ></feed>
            <!-- <el-button type="text" icon="el-icon-edit" @click="getDetail(item.id)">
            </el-button> -->
          </div>

          <el-pagination
            background
            layout="prev, pager, next"
            :total="detectPage.count"
            :current-page="detectPage.page"
            @current-change="pageChange"
          >
          </el-pagination>
        </div>
    
    </div>
    
</template>

<script>
 import feed from "@/components/feed.vue";
 export default {
     

     data(){
         return{
            detectPage: { page: 1, pageSize: 10, list: [], count: 0 },
            token: "",
         }
    },
    components:{
        feed,
    },
    created(){
        this.token = this.$store.state.token;
        this.load();
    },
    methods:{

        pageChange(currentPage) {
            this.detectPage.page = currentPage;
            this.load();
        },

       load(){  
        this.$axios
        .get("detect/list", {
            params: {
            pageSize: this.detectPage.pageSize,
            page: this.detectPage.page,
            type: 1,
            },
        })
        .then((res) => {
            res = res.data;
            if (res.status == 200) {
            this.detectPage.list = res.data.list;
            this.detectPage.count = res.data.count;
            } else {
            this.$message.error("数据加载失败");
            }
        }); 
       },

       close(id) {
        window.console.log(id);
       },

       getDetail(item){
        this.$router.push({path:'/home/ResultDetectIndex',query:{id:item}});
        window.console.log(item);
       },
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

.shadow{
    box-shadow: 1px 1px 2px #e9eef3;
}

.bg {
  background-color: #e9eef3;
}
</style>