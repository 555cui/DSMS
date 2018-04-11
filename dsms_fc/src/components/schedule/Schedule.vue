<template>
  <div>
    <schedule-edit :schedule="schedule"></schedule-edit>
    <el-container>
      <el-aside width="250px">
        <div class="aside-div">
          <el-container>
            <el-header height="50px">
              <div id="menu-title-div">device</div>
            </el-header>
            <el-main>
              <div id="menu-list">
                <el-menu class="el-menu-vertical-demo" unique-opened="true">
                  <el-submenu
                    :index="''+index"
                    v-for="(group, index) in menuData.data"
                    :key="index">

                    <template slot="title">
                      <i class="el-icon-location"></i>
                      <span>{{group.name}}</span>
                    </template>

                    <el-menu-item-group>

                      <el-menu-item
                        v-for="(item, dataIndex) in group.list"
                        :key="index+'-'+dataIndex"
                        :index="index+'-'+dataIndex">
                        <div :id="item.id" @click="handleClickDevice(item)">{{item.name}}</div>
                      </el-menu-item>
                    </el-menu-item-group>
                  </el-submenu>
                </el-menu>
              </div>
            </el-main>
            <el-footer height="50px">
              <div class="button-div">
                <el-button round type="primary" @click="toDeviceManager">device manager</el-button>
              </div>
            </el-footer>
          </el-container>
        </div>
      </el-aside>
      <el-main>
        <el-row>
          <el-col>
            <div id="sample-template">
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <div id="device-info">
              <div v-for="(value, key) in info">
                <el-row>
                  <el-col span="8">
                    {{key}}
                  </el-col>
                  <el-col span="16">
                    {{value}}
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-main>
      <el-aside>
        <div class="aside-div">
          <el-container>
            <el-header>
              <div id="date-div">
                <el-date-picker
                  v-model="selectDate"
                  type="date"
                  :placeholder="new Date()">
                </el-date-picker>
              </div>
            </el-header>
            <el-main>
              <div id="program-list">
                <program-list></program-list>
              </div>
            </el-main>
            <el-footer height="55px">
              <div class="button-div">
                <el-button type="primary" round @click="onScheduleEdit">new day node</el-button>
              </div>
            </el-footer>
          </el-container>
        </div>
      </el-aside>
    </el-container>
  </div>
</template>

<script>
  import ScheduleEdit from './ScheduleEdit'
  import ProgramList from '../utils/ProgramList'
  export default {
    name: "schedule",
    components:{
      ProgramList,
      ScheduleEdit,
    },
    data(){
      return {
        schedule: {
          device: '',
          date: '',
          startTime: '',
          endTime: '',
          week: [],
          template: '',
        },
        selectDate:'',
        info:{
          ID: '',
          MAC: '',
          statue: '',
          address: '',
        },
        menuData:{
          name: 'device menu',
          data: [
            {
              name:'default',
              list:[
                {name:'device1'},
                {name:'device2'},
                {name:'device3'},
              ],
            },
            {
              name:'group1',
              list:[
                {name:'device4'},
                {name:'device5'},
                {name:'device6'},
              ],
            },
            {
              name:'group2',
              list:[
                {name:'device7'},
                {name:'device8'},
                {name:'device9'},
              ],
            },
          ],
        },
      }
    },
    methods: {
      toDeviceManager(){
        this.$store.state.centerSelect='device';
        this.$router.push('userCenter');
      },
      onScheduleEdit(){
        this.$store.state.show.scheduleEdit=true;
      },
      handleClickDevice(device){
        this.info=device;
      }
    }
  }
</script>

<style scoped>
  #menu-list{
    height: 400px;
    overflow: auto;
  }
  #program-list{
    height: 400px;
    overflow :auto;
  }
  #menu-title-div{
    font-size: 30px;
  }
  #sample-template{
    width: 100%;
    height: 300px;
    background-color: black;
    margin: 0 10px;
  }
  #device-info{
    margin: 10px;
    width: 100%;
    min-height: 200px;
    background-color: gray;
  }
</style>
