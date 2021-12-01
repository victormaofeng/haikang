<template>
  <div class="main">
        <el-breadcrumb separator-class="el-icon-arrow-right" class="size box bg">
        <el-breadcrumb-item>首页</el-breadcrumb-item>
        <el-breadcrumb-item>模型结果</el-breadcrumb-item>
        </el-breadcrumb>
  <div class="box">
  <el-table
    :data="tableData4"
    
    style="width: 100%"
    max-height="250"
    >
    <el-table-column
      fixed
      prop="id"
      label="模型ID"
      width="150">
    </el-table-column>
    <el-table-column
      prop="name"
      label="模型名字"
      width="120">
    </el-table-column>
    <el-table-column
      prop="synopsis"
      label="模型介绍"
      width="120">
    </el-table-column>
    <el-table-column
      fixed="right"
      label="操作"
      width="120">
      <template slot-scope="scope">
        <el-button
          @click.native.prevent="deleteRow(scope.$index, tableData4)"
          type="text"
          size="small">
          移除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  </div>
  </div>
</template>

<script>

  export default {
   

    created() {
        this.token = this.$store.state.token;
        this.load();
    },

    methods: {
      deleteRow(index, rows) {
        rows.splice(index, 1);
      },
      load() {
        this.$axios
          .get("algorithm-rest/get-algorithms-by-type", {
                params:{
                    type: 1,
                },
          })
          .then((res) => {
            res = res.data;
            if (res.status == 200) {
              console.log("===================================================");
              console.log("data："+JSON.stringify(res.data));
              this.tableData4 = res.data;

            } else {
              this.$message.error("数据加载失败");
            }
          });
      },
      
    },
    
    data() {
      return {
        // tableData4: [{
        //   id: 1,
        //   name: 'yolo3',
        //   synopsis: '行人重拾别',
        // }]
        tableData4: [{

        }]
      }
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