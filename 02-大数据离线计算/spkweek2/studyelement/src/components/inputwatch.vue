<template>
  <div>
    <input v-model="input" placeholder="请输入内容"></input>
    <button @click="addarray">添加</button>
  </div>
</template>

<script>
export default {
name: "inputwatch",
  data() {
    return {
      input: '',
      array:[1,2,3,4]
    }
  },
  methods:{
    addarray(){
      this.array.push(parseInt(this.input));
    },
    print(){
      console.log(this.array)
      console.log("push成功")
    }
  },
  watch:{
    array(){
    this.print();
    }
  }
}


</script>

<style scoped>

</style>
