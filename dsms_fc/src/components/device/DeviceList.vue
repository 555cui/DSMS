<template>
    <div>
        <device-edit :device="selectDevice"
                     :show="deviceEditShow"
                     @edit="onEditDevice"
                     @close="deviceEditShow=false"></device-edit>
        <el-row>
            <div class="row">
                <el-input v-model="searchData">
                    <el-button slot="append" icon="el-icon-search"></el-button>
                </el-input>
            </div>
        </el-row>
        <el-row>
            <div class="row">
                <el-button type="danger" icon="el-icon-delete" @click="onDeleteDevice"></el-button>
            </div>
        </el-row>
        <el-row>
            <el-tabs value="default" type="card" editable @edit="handleTabsEdit" @tab-click="toTabClick">
                <el-tab-pane
                        v-for="item in group"
                        :key="item.id"
                        :label="item.name"
                        :name="item.name">
                    <el-table
                            :data="item.device"
                            @selection-change="handleSelectionChange"
                            style="width: 100%">
                        <el-table-column
                                type="selection"
                                width="50">
                        </el-table-column>
                        <el-table-column
                                prop="createTime"
                                label="创建日期"
                                sortable
                                width="200">
                        </el-table-column>
                        <el-table-column
                                prop="name"
                                label="名称"
                                sortable
                                width="200">
                        </el-table-column>
                        <el-table-column label="状态" widht="100">
                            <template slot-scope="scope">
                                {{getState(scope.row.id)}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="id" label="ID" width="350">
                        </el-table-column>
                        <el-table-column prop="width" label="width" width="100">
                        </el-table-column>
                        <el-table-column prop="height" label="height" width="100">
                        </el-table-column>
                        <el-table-column label="操作" width="200">
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
        </el-row>
    </div>
</template>

<script>
  import DeviceEdit from "../device/DeviceEdit";
  export default {
    name: "DeviceList",
    components: {DeviceEdit},
    data(){
      return {
        group: [],
        deviceSelection: [],
        groupSelectionIndex: 0,
        selectDevice: null,
        deviceEditShow: false,
        searchData: '',
        states: [],
        runTimeId: -1,
      }
    },
    created(){
      const url2 = '/DSMS_FE/deviceGroup/';
      this.$ajax.get(url2).then(response => {
        if (response.data.code === 0){
          this.group = response.data.data;
        }
        else if (response.data.code>=10&&response.data.code<20)
          this.$store.state.user.code=response.data.code;
        else this.$message.error('获取数据出错');
      }).catch(e=>{this.$message.error('服务器出错')});
      this.runTimeId = setInterval(()=>{
        const url1 = '/DSMS_FE/device/state/group/'+this.group[this.groupSelectionIndex].id;
        this.$ajax.get(url1).then(e=>{
          if (e.data.code===0){
            this.states = e.data.data;
          }
          else if (e.data.code===10){
            this.$store.state.user.code=e.data.code;
          }
        });
      }, 5000);
    },
    beforeDestroy(){
      clearInterval(this.runTimeId);
    },
    methods: {
      getState(id){
        const index = this.states.findIndex(item=>item.device.id===id);
        if (index>=0)
            return this.states[index].type;
        else return '';
      },
      handleTabsEdit(targetName, action) {
        if (action === 'add') {
          this.$prompt('请输入分组名', 'tips', {
            confirmButtonText: '新建',
            cancelButtonText: '取消',
            inputErrorMessage: '分组已存在'
          }).then(value => {
            const url = '/DSMS_FE/deviceGroup/';
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
              else if (response.data.code>=50&&response.data.code<60)
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
          this.$confirm('你将会删除该组内所有设备，你确定删除?', 'tips', {
            confirmButtonText: '删除',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            const tabs = this.group;
            const i = tabs.findIndex(item=>item.name===targetName);
            const url = '/DSMS_FE/deviceGroup/'+tabs[i].id;
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
      toTabClick(tab){
        this.deviceSelection = [];
        this.states = [];
        this.groupSelectionIndex = this.group.findIndex(item=>item.name === tab.name);
      },
      handleEdit(row){
        this.selectDevice = row;
        this.deviceEditShow = true;
      },
      handleDelete(row){
        this.$confirm('你确定删除该设备?', 'tips', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const url = '/DSMS_FE/device/'+row.id;
          this.$ajax.delete(url).then(response=>{
            if (response.data.code === 0){
              const groupIndex = this.group.findIndex(item => item.id === row.group.id);
              const group = this.group[groupIndex];
              const index = group.device.findIndex(item1 => item1.id === row.id);
              this.group[groupIndex].device.splice(index, 1);
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
        this.deviceSelection = val;
      },
      onEditDevice(device){
        const index = this.group[this.groupSelectionIndex].device.findIndex(item => item.id === device.id);
        this.group[this.groupSelectionIndex].device.splice(index, 1);
        const index2 = this.group.findIndex(item=>item.id===device.group.id);
        this.group[index2].device.push(device);
      },
      onDeleteDevice(){},
    },
  }
</script>

<style scoped>
    .row{
        margin: 5px 20px;
    }
</style>