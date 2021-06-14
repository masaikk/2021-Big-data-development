<template>
  <div>
    <div>
      <el-input
        type="textarea"
        autosize
        placeholder="查询语句内容"

        v-model="querySentence">
      </el-input>
<!--      <div>
        {{querySentence}}
      </div>-->

    </div>
    <div>
      <el-button @click="getTableData">query</el-button>
    </div>
    <div>
      <el-table
        :data="dataInfo"
        stripe
        height="600"
        >
        <el-table-column v-for="(key, i) in propList"
                         :prop="key"
                         :label="key"
                         sortable
        ></el-table-column>
      </el-table>
      <query-fields></query-fields>


    </div>
  </div>

</template>

<script>
import axios from "axios";
import queryFields from './queryFields'

export default {
  name: "fetchTableInfo",
  data() {
    return {
      querySentence: "select * from t_rk_jbxx limit 10",
      dataInfo: [
        {p1: 1, p2: 2},
        {p1: 2, p2: 4}
      ]
    }
  },
  computed:{
    propList(){
      return Object.keys(this.dataInfo[0])
    }
  },
  methods: {
    getTableData() {
      this.$message({
        dangerouslyUseHTMLString:true,
        message:'<strong>执行语句：'+this.querySentence+'</strong>',
        center:true,
        showClose:true,
      });
      axios.get('http://127.0.0.1:8000/st1/query', {
        params: {
          sen: this.querySentence
        }
      }).then(res => {
        console.log(res)
        // this.tableListData=list(res.data);
        this.dataInfo = res.data.info_data;
        this.$message({
          message:'查询成功！',
          showClose:true,
          center:true,
          type:'success'
        });
      }).catch(err => {
        console.log(err)
        this.$message.error('查询失败请检查您的语句或者网络环境！');
      })
    },
  },
  mounted() {
    console.log(this.propList);
  },
  components:{
    queryFields
  }


}
</script>

<style scoped>

</style>
