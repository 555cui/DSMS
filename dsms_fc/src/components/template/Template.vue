<template>
  <div>
    <template-new></template-new>
    <el-row>
      <div class="row">
        <el-input v-model="searchData">
          <el-button slot="append" icon="el-icon-search"></el-button>
        </el-input>
      </div>
    </el-row>
    <el-row>
      <div class="row">
        <el-button icon="el-icon-plus" @click="onNewTemplate"></el-button>
        <el-button type="danger" icon="el-icon-delete" @click="onDeleteTemplate"></el-button>
      </div>
    </el-row>
    <el-row>
      <div class="row">
        <el-tabs value="default" type="card" editable @edit="handleTabsEdit">
          <el-tab-pane
            v-for="item in tabsData"
            :key="item.id"
            :label="item.name"
            :name="item.name">
            {{item.name}}
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-row>
  </div>
</template>

<script>
  import TemplateNew from "./TemplateNew";

  export default {
    components: {TemplateNew},
    name: "template",
    data(){
      return {
        searchData: '',
        tabsData: [
          {
            id: '00000000-00000000-0000-0000-00000000',
            name: 'default',
            dataList: [
              {
                id: '000000000-00000000-0000-0000-00000000',
                name: 'template1',
                width: 800,
                height: 600
              },
              {
                id: '000000000-00000000-0000-0000-00000001',
                name: 'template2',
                width: 800,
                height: 600
              },
              {
                id: '000000000-00000000-0000-0000-00000003',
                name: 'template3',
                width: 800,
                height: 600
              },
            ]
          },
          {
            id: '00000000-00000000-0000-0000-00000002',
            name: 'group1',
            dataList: [
              {
                id: '000000000-00000000-0000-0000-00000005',
                name: 'template5',
                content: '<div></div>',
                width: 800,
                height: 600
              },
              {
                id: '000000000-00000000-0000-0000-00000006',
                name: 'template6',
                content: '<div></div>',
                width: 800,
                height: 600
              },
              {
                id: '000000000-00000000-0000-0000-00000007',
                name: 'template7',
                content: '<div></div>',
                width: 800,
                height: 600
              },
            ]
          },
        ],
      }
    },
    methods: {
      handleTabsEdit(targetName, action) {
        if (action === 'add') {
          this.$prompt('input group name please', 'tips', {
            confirmButtonText: 'ok',
            cancelButtonText: 'cancel',
            inputErrorMessage: 'the group already exis!'
          }).then(value => {
            let resData ={
              id: '00000000-00000000-0000-0000-00aa',
              name: value,
              dataList: [],
            };
            this.tabsData.push(resData);
            this.$message({
              type: 'success',
              message: 'success add group'
            });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '取消输入'
            });
          });
        }
        if (action === 'remove') {
          if(targetName === 'default'){
            this.$message("you can't delete the default group!");
            return;
          }
          this.$confirm('this action will delete all template which in the group, if you do it?', 'tips', {
            confirmButtonText: 'ok',
            cancelButtonText: 'cancel',
            type: 'warning'
          }).then(() => {
            let tabs = this.tabsData;
            this.tabsData = tabs.filter(tab => tab.name !== targetName);
            this.$message({
              type: 'success',
              message: 'delete action success!'
            });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: 'delete action cancel'
            });
          });
        }
      },
      onNewTemplate() {
        this.$store.state.show.templateNew=true;
      },
      onDeleteTemplate(){

      },
    },
  }
</script>

<style scoped>
  .row{
    margin: 5px 20px;
  }
</style>
