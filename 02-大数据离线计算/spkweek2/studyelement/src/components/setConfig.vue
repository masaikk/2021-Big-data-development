<template>
  <div>
    <el-form ref="hiveConfig" :model="hiveConfig"  label-width="80px">
      <el-form-item label="url:">
        <el-input v-model="hiveConfig.hiveUrl"></el-input>
      </el-form-item>
      <el-form-item label="port:">
        <el-input v-model="hiveConfig.port"></el-input>
      </el-form-item>
      <el-form-item label="baseName:">
        <el-input v-model="hiveConfig.baseName"></el-input>
      </el-form-item>
      <el-form-item label="user:">
        <el-input v-model="hiveConfig.user"></el-input>
      </el-form-item>
      <el-form-item label="password:">
        <el-input v-model="hiveConfig.password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">link!</el-button>
      </el-form-item>
    </el-form>

  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'setConfig',
  computed: {
    url() {
      return 'jdbc:'+this.hiveConfig.hiveUrl + ':' + this.hiveConfig.port + '/' + this.hiveConfig.baseName
    }
  },
  data() {
    return {
      hiveConfig: {
        hiveUrl: 'hive2://bigdata107.depts.bingosoft.net',
        port: '22107',
        baseName: 'user05_db',
        user: 'user05',
        password: 'pass@bingo5',
      }
    }
  },
  methods: {
    onSubmit() {
      // console.log(this.hiveConfig)
      axios.get('http://127.0.0.1:8000/st1/setConfig/', {
          params: {
            url: this.url,
            user: this.hiveConfig.user,
            password: this.hiveConfig.password
          }
        }
      ).then(res=>{
        console.log(res)
        this.$message({
          dangerouslyUseHTMLString:true,
          message:'<strong>配置修改成功</strong>',
          center:true,
          showClose:true,
        });
      }).catch(err=>{
        console.log(err)
        this.$message.error('修改失败请检查您的语句或者网络环境！');
      })

    }
  }
}
</script>

<style scoped>

</style>
