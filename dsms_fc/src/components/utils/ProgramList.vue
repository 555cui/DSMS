<template>
  <div>
    <el-row  v-for="time in times" :key="time">
      <el-col :span="8">
        {{time}}
      </el-col>
      <el-col :span="16">
        template
      </el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: "program-list",
    data(){
      return {
        times:[
          '00:00','01:00','02:00','03:00','04:00','05:00',
          '06:00','07:00','08:00','09:00','10:00','11:00',
          '12:00','13:00','14:00','15:00','16:00','17:00',
          '18:00','19:00','20:00','21:00','22:00','23:00',
        ],
        isTest:true,
      }
    }
  }
</script>

<style scoped>

</style>
