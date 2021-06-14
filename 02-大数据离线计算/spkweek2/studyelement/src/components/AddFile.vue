<template>
  <div class="man-container">
    <div class="el-card">
      <div class="el-card__header">
        <slot name="header">
          <p>文件上传</p>
        </slot>
      </div>
      <div class="el-card__body">
        <el-row style="margin: 10px 0 0 0">
          <span>上传目录：</span>
          <ul style="margin: 10px 0;display: inline-block;">
            <li>
              <el-select v-model="theme" placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </li>
          </ul>
        </el-row>
        <el-row style="margin: 0px">
          <ul style="margin: 0;display: inline-block;">
            <li>
              <el-upload
                class="upload"
                ref="upload"
                action="string"
                :file-list="fileList"
                :auto-upload="false"
                :http-request="uploadFile"
                :on-change="handleChange"
                :on-preview="handlePreview"
                :on-remove="handleRemove"
                multiple="multiple"
              >
                <el-button slot="trigger" size="small" type="primary" @click="delFile">选取文件</el-button>
                <el-button
                  style="margin-left: 10px;"
                  size="small"
                  type="success"
                  @click="submitUpload"
                >上传到服务器</el-button>
              </el-upload>
            </li>
          </ul>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
var that = null;
export default {
  name: 'AddFile',
  data() {
    return {
      user: {},
      fileList: [],
      multiple: true,
      formData: '',
      options: [
        {
          value: "工作报告",
          label: "工作报告"
        },
        {
          value: "会议记录",
          label: "会议记录"
        }
      ],
      theme: ""
    };
  },
  mounted() {
    that = this;
    let user = window.localStorage.getItem("access-user");
    if (user) {
      user = JSON.parse(user);
      this.user = user;
    }
  },
  methods: {
    delFile() {
      this.fileList = [];
    },
    handleChange(file, fileList) {
      this.fileList = fileList;
    },
    uploadFile(file) {
      this.formData.append('file', file.file)
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    submitUpload() {
      let formData = new FormData();
      formData.append("theme", this.theme);
      formData.append("file", this.fileList[0].raw);

      axios({
        url: this.HOST + "/thematicfile/upload",
        method: "post",
        data: formData,
        headers: {
          "Content-Type": "multipart/form-data;charset=utf-8"
        }
      })
        .then(res => {
          if (res.data.success) {
            // alert("导入成功!");
          } else {
            alert(res.data.message + "," + res.data.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    }
  }
};
</script>

<style scoped>
.man-container {
  margin: 0px;
  overflow: hidden;
  bottom: 0px;
  right: 0px;
  width: 100%;
  padding: 50px 20px 0px 20px;
}

.el-card {
  text-align: start;
  margin-left: auto;
  margin-right: auto;
  margin-top: 8px;
  border: 1px solid #ddd;
  width: 98%;
}

.el-card__header {
  border-top: 1px solid #373d41;
  border-bottom: 1px solid #ddd;
  padding: 8px 10px !important;
}

.el-card__body {
  padding: 15px !important;
}

.upload {
  min-width: 400px;
  width: 40%;
}
</style>
