<template>
  <div>
    <template-new :group="group" @new="onInsertTemplate"></template-new>
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
        <el-tabs value="default" type="card" editable @edit="handleTabsEdit" @tab-click="toTabClick">
          <el-tab-pane
            v-for="item in group"
            :key="item.id"
            :label="item.name"
            :name="item.name">
              <el-table
                      :data="item.template"
                      @selection-change="handleSelectionChange"
                      style="width: 100%">
                  <el-table-column
                          type="selection"
                          width="55">
                  </el-table-column>
                  <el-table-column
                          prop="createTime"
                          label="日期"
                          sortable
                          width="180">
                  </el-table-column>
                  <el-table-column
                          prop="name"
                          label="名称"
                          sortable
                          width="180">
                  </el-table-column>
                  <el-table-column
                          prop="width"
                          label="width">
                  </el-table-column>
                  <el-table-column prop="height"
                                   label="height">
                  </el-table-column>
                  <el-table-column label="background color" prop="backgroundColor">
                  </el-table-column>
                  <el-table-column label="操作">
                      <template slot-scope="scope">
                          <el-button
                                  size="mini"
                                  @click="handleEdit(scope.row)">编辑</el-button>
                          <el-button
                                  size="mini"
                                  type="danger"
                                  @click="handleDelete(scope.row)">删除</el-button>
                      </template>
                  </el-table-column>
              </el-table>
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
        name: "template-show",
        created(){
            const url = "/templateGroup/";
            this.$ajax.get(url).then(response=>{
                if (response.data.code === 0){
                    this.group = response.data.data;
                }
                else if (response.data.code >=10 && response.data.code <20){
                    this.$store.state.user.code = response.data.code;
                }
                else {
                    this.$message.error('获取数据失败');
                }
            }).catch(e=>{
                this.$message.error('服务器出错');
            });
        },
        data(){
            return {
                searchData: '',
                group: [],
                templateSelection: [],
                groupSelectionIndex: 0,
            }
        },
        methods: {
            onInsertTemplate(template){
                const groupIndex = this.group.findIndex(item => item.id===template.group.id);
                this.group[groupIndex].template.push(template);
            },
            handleTabsEdit(targetName, action) {
                if (action === 'add') {
                    this.$prompt('请输入分组名', 'tips', {
                        confirmButtonText: '新建',
                        cancelButtonText: '取消',
                        inputErrorMessage: '分组已存在'
                    }).then(value => {
                        const url = '/templateGroup/';
                        this.$ajax.post(url,{name:value.value}).then(response=>{
                            if (response.data.code===0){
                                this.group.push(response.data.data);
                                this.$message({
                                    type: 'success',
                                    message: '成功新建分组'
                                });
                            }
                            else if (response.data.code>=0&&response.data.code<20)
                                this.$store.state.user.code=response.data.code;
                            else if (response.data.code>=40&&response.data.code<50)
                                this.$message.error(response.data.msg);
                            else
                                this.$message.error('请求失败');
                        }).catch(e=>{
                            this.$message.error('服务器出错');
                        })
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '取消输入'
                        });
                    });
                }
                if (action === 'remove') {
                    if(targetName === 'default'){
                        this.$message("你不能删除默认分组");
                        return;
                    }
                    this.$confirm('你将会删除该组内所有模板，你确定删除?', 'tips', {
                        confirmButtonText: '删除',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        const tabs = this.group;
                        const i = tabs.findIndex(item=>item.name===targetName);
                        const url = '/templateGroup/'+tabs[i].id;
                        this.$ajax.delete(url).then(response=>{
                            if (response.data.code===0){
                                tabs.splice(i,1);
                                this.$message({
                                    type: 'success',
                                    message: '删除成功'
                                });
                            }
                            else if (response.data.code>=0&&response.data.code<20){
                                this.$store.state.user.code=response.data.code;
                            }
                            else {
                                this.$message.error('请求失败');
                            }
                        }).catch(e=>{
                            this.$message.error('服务器出错');
                        });
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '取消删除'
                        });
                    });
                }
            },
            onNewTemplate() {
                this.$store.state.template.show = true;
            },
            toTabClick(tab){
                this.templateSelection = [];
                this.groupSelectionIndex = this.group.findIndex(item=>item.name === tab.name);
            },
            onDeleteTemplate() {
                this.$confirm('你确定删除选中模板?', 'tips', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const url = '/template/delete';
                    this.$ajax.post(url, this.templateSelection).then(response=>{
                        if (response.data.code===0){
                            for(const item of this.templateSelection){
                                const index = this.group[this.groupSelectionIndex].template.findIndex(
                                    ite=>ite.id===item.id);
                                this.group[this.groupSelectionIndex].template.splice(index, 1);
                            }
                        }
                    })
                });
            },
            handleEdit(row){
                //console.info(row);
                this.$router.push('/template/'+row.id);
            },
            handleDelete(row){
                this.$confirm('你确定删除该模板?', 'tips', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const url = '/template/'+row.id;
                    this.$ajax.delete(url).then(response=>{
                        if (response.data.code === 0){
                            const groupIndex = this.group.findIndex(item => item.id === row.group.id);
                            const group = this.group[groupIndex];
                            const index = group.template.findIndex(item1 => item1.id === row.id);
                            this.group[groupIndex].template.splice(index, 1);
                            this.$message.success('删除成功!');
                        }
                        else if (response.data.code >=10 && response.data.code <20){
                            this.$store.state.user.code = response.data.code;
                        }
                        else {
                            this.$message.error('获取数据失败');
                        }
                    }).catch(e=>{
                        this.$message.error('服务器出错');
                    })
                });
            },
            handleSelectionChange(val){
                this.templateSelection = val;
                //console.info(this.templateSelection);
            }
        },
    }
</script>

<style scoped>
  .row{
    margin: 5px 20px;
  }
</style>
