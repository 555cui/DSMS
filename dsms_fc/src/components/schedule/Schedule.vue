<template>
  <div>
    <schedule-edit :schedule="schedule" :action="editAction" @finish="reflashPrograms"></schedule-edit>
    <el-container>
      <el-aside width="250px">
        <div class="side-div">
          <div class="title-div">设备</div>
          <div class="list-div">
            <el-menu background-color="#8cbff8" default-active="0-0">
              <el-submenu
                      :index="''+index"
                      v-for="(item, index) in group"
                      :key="item.id">
                <template slot="title">
                  <i class="el-icon-location"></i>
                  <span>{{item.name}}</span>
                </template>

                <el-menu-item-group>

                  <el-menu-item
                          v-for="(deviceItem, dataIndex) in item.device"
                          :key="deviceItem.id"
                          :index="index+'-'+dataIndex">
                    <div @click="handleClickDevice(deviceItem)">{{deviceItem.name}}</div>
                  </el-menu-item>
                </el-menu-item-group>
              </el-submenu>
            </el-menu>
          </div>
          <div class="button-div">
            <el-button round type="primary" @click="toDeviceManager">设备管理</el-button>
          </div>
        </div>
      </el-aside>
      <el-main>
          <div class="main-div">
            <div class="display-div"><canvas v-draw="deviceState"></canvas></div>
            <div class="device-div">
                <el-row>
                    <el-col :span="4">id</el-col>
                    <el-col :span="20">{{device.id}}</el-col>
                </el-row>
                <el-row>
                    <el-col :span="4">
                        名称
                    </el-col>
                    <el-col :span="8">{{device.name}}</el-col>
                    <el-col :span="4">
                        mac
                    </el-col>
                    <el-col :span="8">{{device.mac}}</el-col>
                </el-row>
                <el-row>
                    <el-col :span="4">
                        状态
                    </el-col>
                    <el-col :span="20">{{deviceState.type}}</el-col>
                </el-row>
            </div>
          </div>
      </el-main>
      <el-aside width="250px">
        <div class="side-div">
          <div class="title-div">
            <el-date-picker
                    v-model="selectDate"
                    type="date"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    format="yyyy 年 MM 月 dd 日"
                    :placeholder="(new Date()).toDateString()"
                    @change="reflashPrograms">
            </el-date-picker>
          </div>
          <div class="list-div">
            <program-list :programs="program" @dbClick="setSchedule"></program-list>
          </div>
          <div class="button-div">
            <el-button type="primary" round @click="onScheduleEdit('new')">新建日程</el-button>
          </div>
        </div>
      </el-aside>
    </el-container>
  </div>
</template>

<script>
    import ScheduleEdit from "./ScheduleEdit";
    import ProgramList from "../utils/ProgramList";
    export default {
        name: "schedule",
        components: {ProgramList, ScheduleEdit},
        created(){
            const url = '/DSMS_FE/deviceGroup/';
            this.$ajax.get(url).then(response=>{
                if (response.data.code===0){
                    this.group=response.data.data;
                    //this.device=this.group[0].device[0];
                    this.reflashPrograms();
                }
                else if (response.data.code>=10&&response.data.code<20){
                  this.$store.state.user.code=response.data.code;
                  console.info('set code'+this.$store.state.user.code);
                }
                else this.$message.error('获取数据出错');
            }).catch(e=>{this.$message.error('服务器出错')});
        },
        mounted(){
            this.intervalId = setInterval(()=>{
                this.getState(()=>{
                    console.info('获取状态成功');
                });
            }, 5000);
        },
        beforeDestroy(){
            clearInterval(this.intervalId);
        },
      watch: {
        login(){
          if (this.login){
            const url = '/DSMS_FE/deviceGroup/';
            this.$ajax.get(url).then(response=>{
              if (response.data.code===0){
                this.group=response.data.data;
                //this.device=this.group[0].device[0];
                this.reflashPrograms();
              }
              else if (response.data.code>=10&&response.data.code<20){
                this.$store.state.user.code=response.data.code;
                console.info('set code'+this.$store.state.user.code);
              }
              else this.$message.error('获取数据出错');
            }).catch(e=>{this.$message.error('服务器出错')});
          }
        }
      },
        computed: {
          program(){
                const p = [];
                for (let i=0;i<24;i++){
                    if (i<10)i='0'+i;
                    else i=''+i;
                    p.push({
                        time: i+':00-'+i+':59',
                        name: '',
                        schedule: null,
                    })
                }
                for (const program of this.programs){
                    for (let i = parseInt(program.startTime); i<= parseInt(program.endTime); i++){
                        p[i].name = program.template.name;
                        p[i].schedule = program.schedule;
                    }
                }
                return p;
            },
          login(){
            return this.$store.state.user.login;
          },
        },
        data(){
            const d = new Date();
            return {
                intervalId: null,
                schedule: {},
                selectDate:d.getFullYear()+'-'+(d.getMonth()+1)+'-'+(d.getDate())+' 00:00:00',
                device: {
                    id: null,
                    mac: '',
                    name: '',
                    address: '',
                    width: '',
                    height: '',
                },
                group:[],
                programs: [],
                editAction: 'new',
                deviceState: {
                    device: null,
                    url: '',
                    type: '',
                },
            }
        },
        methods: {
            reflashPrograms(){
                const url2 = '/DSMS_FE/program/';
                const today = {
                    device: this.device,
                    startDate: this.selectDate,
                };
                this.$ajax.post(url2, today).then(response=>{
                    if (response.data.code===0){
                        this.programs = response.data.data;
                    }
                    else if (response.data.code>=10&&response.data.code<20)
                        this.$store.state.user.code=response.data.code;
                    else this.$message.error('获取数据出错');
                }).catch(e=>{this.$message.error('服务器出错')});
            },
            toDeviceManager(){
                this.$router.push('/device');
            },
            setSchedule(schedule){
                this.schedule=schedule;
                if (schedule===null){
                    this.onScheduleEdit('new');
                }
                else {
                    this.onScheduleEdit('edit');
                }
            },
            onScheduleEdit(action){
                this.editAction = action;
                this.$store.state.schedule.show=true;
            },
            handleClickDevice(device){
                this.device=device;
                this.reflashPrograms();
            },
            getState(callback){
                if (this.device.id!==null){
                    const url = '/DSMS_FE/device/'+this.device.id+'/state';
                    this.$ajax.get(url).then(response=>{
                        if (response.data.code===0){
                            this.deviceState = response.data.data;
                            callback();
                        }
                        else if (response.data.code===69){
                            this.deviceState.type = '未连接';
                            this.deviceState.url=null;
                        }
                    });
                }
            },
        },
        directives: {
            draw(el, binding){
                el.width=768;
                el.height=349;

                if (binding.value.url!==null){
                    const pan = el.getContext("2d");
                    const image = new Image();
                    image.onload=ev=>{
                        pan.drawImage(image, 0, 0, 768, 349);
                    };
                    image.src = binding.value.url;
                }
                else {
                    el.width=0;
                    el.height=0;
                }
            }
        }
    }
</script>

<style scoped>
  .side-div{
    background-color: #8cbff8;
  }
  .title-div{
    text-align: center;
    font-size: 30px;
    padding: 5px;
  }
  .list-div{
    text-align: center;
    overflow-y: auto;
    height: 400px;
  }
  .button-div{
    text-align: center;
    padding: 5px;
  }

  .main-div{
    background-color: gray;
    width: 100%;
    height: 100%;
  }

  .display-div{
    background-color: black;
    height: 75%;
  }
</style>
