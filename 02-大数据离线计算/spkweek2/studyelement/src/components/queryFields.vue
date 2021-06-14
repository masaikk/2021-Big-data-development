<template>
  <div>
    <div>
      <el-button @click="drawer = true" type="primary" icon="el-icon-circle-plus" style="margin-left: 16px;">
        点我打开
      </el-button>
    </div>
    <el-drawer
      title="当前库下的表和字段"
      :visible.sync="drawer"
      :with-header="true"
      show-close="true"
    >
      <div>
        <el-button @click="getQueryTableFields">查询字段</el-button>
        <div>
          <ol>
            <li v-for="(val , i) in tableFields">
              <div>
                <div>
                  <i class="el-icon-d-caret" style="display: inline-block"></i>
                  <p style="display: inline-block">
                    表：{{ val.tableName }}
                  </p>

                </div>
                <div>
                  <ul>
                    <li v-for="(miniVal,miniIndex) in val.fields">
                      <div>
                        <i class="el-icon-crop" style="display: inline-block"></i>
                        <p style="display: inline-block">
                          {{ miniVal }}
                        </p>
                      </div>
                    </li>
                  </ul>

                </div>
              </div>
            </li>
          </ol>

        </div>
      </div>
    </el-drawer>

  </div>


</template>

<script>
import axios from "axios";
export default {
  name: 'queryFields',
  data () {
    return {
      tableFields: [
        {
          tableName: 'tab1',
          fields: [
            '点击上方的按钮查询',
            '测试字段',
            '测试字段3'
          ]
        }, {
          tableName: 'tab2name',
          fields: [
            '点击上方的按钮查询',
            '测试字段',
            '测试字段3'
          ]
        }, {
          tableName: 'tab3Name',
          fields: [
            '点击上方的按钮查询',
            '测试字段',
            '测试字段3'
          ]
        }
      ],
      drawer: false,
    }
  },
  methods: {
    getQueryTableFields(){
      axios.get('http://127.0.0.1:8000/st1/tableFields', {
      }).then(res => {
        console.log(res)
        // this.tableListData=list(res.data);
        this.tableFields = res.data.tableFields;
        this.$message({
          message:'查询成功！',
          showClose:true,
          center:true,
          type:'success'
        });
      }).catch(err => {
        console.log(err)
        this.$message.error('查询失败请检查您的网络环境！');
      })
    }
  }

}
</script>

<style scoped>

</style>
