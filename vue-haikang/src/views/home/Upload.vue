<template>
  <div>
    <el-row class="row">
      <el-col :span="20" :offset="2">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>开始检测</el-breadcrumb-item>
        </el-breadcrumb>

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

           <el-form-item label="算法选择">
              <el-checkbox-group 
                v-model="checkedAlgorithms"
                :min="0"
                :max="1">
                <el-checkbox v-for="algorithm in algorithms" :label="algorithm" :key="algorithm">{{algorithm}}</el-checkbox>
              </el-checkbox-group>
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
//这里可以遍历 算法表，暂时没有接口。最后返回的是ID
const algorithmOptions = ['yolo3', 'reid'];
export default {
  name: "Upload",
  data() {
    return {
      is_auto: false,
      is_mult: true,
      form: {
        title: "",
        content: "",
        algorithmId: 1,
        token: "",
      },
      checkedAlgorithms: ['yolo3'],
      algorithms: algorithmOptions,
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
  },
};
</script>
<style scope>
.row {
  margin-top: 20px;
}
</style>