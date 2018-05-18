<template>
    <div>
        <schedule-edit :schedule="schedule" :action="editAction" @finish="refreshData"></schedule-edit>
        <el-row>
            <div class="row">
                <el-input v-model="searchData">
                    <el-button slot="append" icon="el-icon-search"></el-button>
                </el-input>
            </div>
        </el-row>
        <el-row>
            <div class="row">
                <el-button icon="el-icon-plus" @click="handleNew"></el-button>
                <el-button type="danger" icon="el-icon-delete" @click="handleDeleteList"></el-button>
            </div>
        </el-row>
        <el-row>
            <div class="row">
                <el-table
                        :data="scheduleList"
                        @selection-change="handleSelectionChange"
                        style="width: 100%">
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column label="开始日期" sortable>
                        <template slot-scope="scope">
                            {{ scope.row.startDate.substring(0, 10) }} </template>
                    </el-table-column>
                    <el-table-column label="结束日期" sortable>
                        <template slot-scope="scope">
                            {{ scope.row.endDate.substring(0, 10) }} </template>
                    </el-table-column>
                    <el-table-column label="设备">
                        <template slot-scope="scope">
                            {{ scope.row.device === null ? "none" : scope.row.device.name }} </template>
                    </el-table-column>
                    <el-table-column label="设备组">
                        <template slot-scope="scope">
                            {{ scope.row.deviceGroup === null ? "none" : scope.row.deviceGroup.name }} </template>
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
            </div>
        </el-row>
    </div>
</template>

<script>
  import ScheduleEdit from "./ScheduleEdit";
  export default {
    name: "ScheduleList",
    components: {ScheduleEdit},
    created(){
      this.refreshData();
    },
    data(){
      return {
        schedule: {},
        editAction: 'new',
        searchData: '',
        scheduleList: [],
        selection: [],
      }
    },
    methods: {
      handleNew(){
        this.editAction = 'new';
        this.$store.state.schedule.show=true;
      },
      handleEdit(row){
        this.schedule = row;
        this.editAction = 'edit';
        this.$store.state.schedule.show=true;
      },
      handleDelete(row){
        this.$confirm('你确定删除该日程?', 'tips', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const url = '/DSMS_FE/schedule/'+row.id;
          this.$ajax.delete(url).then(response=>{
            if (response.data.code === 0){
              const index = this.scheduleList.findIndex(item=>item.id===row.id);
              this.scheduleList.splice(index, 1);
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
      handleDeleteList(){
        if (this.selection.length>0){
          this.$confirm('此操作将永久删除选中日程, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            const url = '/DSMS_FE/schedule/delete';
            this.$ajax.post(url,this.selection,{responseType: 'json'}).then(response=>{
              if (response.data.code === 0){
                for(let schedule of this.selection){
                  const i = this.scheduleList.findIndex(
                    item => item.id === schedule.id);
                  this.scheduleList.splice(i, 1);
                }
                this.selection=[];
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                });
              } else if (response.data.code >=10 && response.data.code<20)
                this.$store.state.user.code=response.data.code;
            });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });
        }
        else this.$message({type: 'info', message: '请选择'})
      },
      handleSelectionChange(val){
        this.selection = val;
      },
      refreshData(){
        const url = '/DSMS_FE/schedule/';
        this.$ajax.get(url).then(response=>{
          if (response.data.code === 0){
            this.scheduleList = response.data.data;
          }
          else if (response.data.code >=10 && response.data.code < 20){
            this.$store.state.user.code = response.data.code;
          }
        });
      }
    }
  }
</script>

<style scoped>
    .row{
        margin: 5px 20px;
    }
</style>