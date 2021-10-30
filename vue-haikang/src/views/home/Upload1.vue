<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right" class="size box bg">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>检测管理</el-breadcrumb-item>
    </el-breadcrumb>
    <el-row class="row">
      <el-col :span="20" :offset="2">
        <!-- <el-breadcrumb separator="/">
          <el-breadcrumb-item>开始检测</el-breadcrumb-item>
        </el-breadcrumb> -->

        <el-form
          ref="form"
          :model="form"
          label-width="100px"
          size="medium"
          action=""
        >
          <el-form-item label="标题">
            <el-input v-model="form.title"></el-input>
          </el-form-item>

          <el-form-item label="介绍">
            <el-input type="textarea" v-model="form.content"></el-input>
          </el-form-item>

          <el-form-item label="上传">
            <el-upload
              ref="upload"
              :auto-upload="is_auto"
              :data="form"
              :multiple="is_mult"
              :action="getUrl()"
              name="files"
              :on-success="success"
            >
              <el-button slot="trigger" size="small" type="primary"
                >选取文件</el-button
              >
            </el-upload>
          </el-form-item>



           <el-form-item label="选择模型">
            <el-select v-model="form.algorithmId" placeholder="请选择模型">
            <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
            </el-select>
          
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="detect" native-type="button"
              >开始检测</el-button
            >
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>

export default {
  name: "Upload",
  created() {
        this.token = this.$store.state.token;
        this.load();
    },
  data() {
    
    return {
      is_auto: false,
      is_mult: true,
      options: [{}],
      form: {
        title: "",
        content: "",
        algorithmId: '',
        token: "",
      },
    };
  },
  methods: {
    getUrl: function () {
      return this.$location + "/detect";
    },
    detect: function () {
      window.console.log(this.form);
      this.form.token = window.sessionStorage.getItem("token");
      this.$refs["upload"].submit();
    },
    success: function (res, file, fileList) {
      window.console.log(res);
      if (res.status == 200) {
        this.$message({
          message: "成功",
          type: "success",
        });
      } else {
        this.$message.error("失败");
      }
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
              this.options = res.data;

            } else {
              this.$message.error("数据加载失败");
            }
          });
      },

      
  },
};
</script>
<style scope>
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
</style>