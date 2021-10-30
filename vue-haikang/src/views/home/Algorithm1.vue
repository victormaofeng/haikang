<template>
  <el-table
    :data="tableData4"
    style="width: 100%"
    max-height="250">
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