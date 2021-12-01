<template>
    <div class="main">
        <el-breadcrumb separator-class="el-icon-arrow-right" class="size box bg">
        <el-breadcrumb-item>首页</el-breadcrumb-item>
        <el-breadcrumb-item>重识别结果</el-breadcrumb-item>
        </el-breadcrumb>

        <div v-if="reidPage.list && reidPage.list.length > 0" class="box">
          <div v-for="item in reidPage.list" v-bind:key="item.id">
            <feed
              :title="item.title"
              :src1="item.path1"
              :src="item.path"
              :src2="item.path2"
              :desc="item.insertTime"
              :time="item.insertTime1"
              :type="item.type"
              @close="close"
            ></feed>
          </div>


          <!-- <el-pagination
            background
            layout="prev, pager, next"
            :total="reidPage.count"
            :current-page="reidPage.page"
            @current-change="pageChange"
          >
          </el-pagination> -->
        </div>

    </div>
</template>


<script>
 import feed from "@/components/feed1.vue";

export default {
    data(){
        return{
            reidPage: { page: 1, pageSize: 10, list: [], count: 0 },
            token: "",
        }
    },
    components:{
        feed,
    },
    methods:{
        load(){
            this.$axios
            .get("detect/listDetails", {
                params: {
                pageSize: this.reidPage.pageSize,
                page: this.reidPage.page,
                type: 2,
                id:this.$route.query.id,
                },
            })
            .then((res) => {
                res = res.data;
                if (res.status == 200) {
                this.reidPage.list = res.data.list;
                this.reidPage.count = res.data.count;
                } else {
                this.$message.error("数据加载失败");
                }
            });

        },
        pageChange(currentPage) {
            this.reidPage.page = currentPage;
            this.load();
        },
    },
    created(){
        this.token = this.$store.state.token;
        this.load();
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