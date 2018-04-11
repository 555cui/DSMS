<template>
  <div>
    <el-dialog :visible.sync="$store.state.show.templateNew" title="new template">
      <el-form ref="form" :model="template" label-width="150px">
        <el-form-item label="name">
          <el-input v-model="template.name"></el-input>
        </el-form-item>
        <el-form-item label="group">
          <el-select v-model="template.group" placeholder="group">
            <el-option
              v-for="item in templateGroup"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="width">
          <el-input type="number" v-model.number="template.width"></el-input>
        </el-form-item>
        <el-form-item label="height">
          <el-input type="number" v-model.number="template.height"></el-input>
        </el-form-item>
        <el-form-item label="background color">
          <el-color-picker v-model="template.backgroundColor"></el-color-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onTemplateEdit">new</el-button>
          <el-button type="warning" @click="onCancelNew">cancel</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "template-new",
    data(){
      return {
        template: {
          id: '',
          name: '',
          group: '',
          backgroundColor: '#808080',
          width: '',
          height: '',
          media: [],
        },
        templateGroup: [
          {
            id: 'default',
            name: 'default'
          },
          {
            id: '00000000-00000000-0000-0000-00000000',
            name: 'group1',
          }
        ]
      }
    },
    methods: {
      onTemplateEdit(){
        this.$store.state.show.templateNew=false;
        this.$router.push('/templateEdit/aaaa');
      },
      onCancelNew(){
        this.resetForm('form');
        this.$store.state.show.templateNew=false;
      }
    }
  }
</script>

<style scoped>

</style>
