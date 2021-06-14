<template>
  <div>
    <el-table :data="tableListData">
      <el-table-column
        prop="name"
        label="name"

      ></el-table-column>
    </el-table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "showTables",
  data() {
    return {
      tableListData: []
    }
  },
  mounted() {
    axios.get('http://127.0.0.1:8000/st1/fetchTables/',).then(res=>{
      console.log(res)
      // this.tableListData=list(res.data);
      this.tableListData = res.data.tableListData
    }).catch(err=>{
      console.log(err)
    })

  }
}
</script>

<style scoped>

</style>
