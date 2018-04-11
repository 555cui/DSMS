<template>
  <div>
    <el-dialog
      title="new day node"
      :visible.sync="$store.state.show.scheduleEdit"
      width="50%">
      <el-form ref="form" :model="schedule" label-width="80px">
        <el-form-item label="device">
          <el-select v-model="schedule.device" placeholder="device">
            <el-option
              v-for="item in deviceGroup"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="date">
          <el-date-picker
            v-model="schedule.date"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="time">
          <el-col span="11">
            <el-time-select
              v-model="schedule.startTime"
              :picker-options="{start: '00:00',step: '01:00',end: '23:00'}"
              placeholder="选择时间">
            </el-time-select>
          </el-col>
          <el-col span="2">-</el-col>
          <el-col span="11">
            <el-time-select
              v-model="schedule.endTime"
              :picker-options="{start: '00:00',step: '01:00',end: '23:00',minTime: schedule.startTime}"
              placeholder="选择时间">
            </el-time-select>
          </el-col>
        </el-form-item>
        <el-form-item label="week">
          <el-checkbox-group v-model="schedule.week">
            <el-checkbox label="Sun"></el-checkbox>
            <el-checkbox label="Mon"></el-checkbox>
            <el-checkbox label="Tue"></el-checkbox>
            <el-checkbox label="Wen"></el-checkbox>
            <el-checkbox label="Tru"></el-checkbox>
            <el-checkbox label="Fri"></el-checkbox>
            <el-checkbox label="Sat"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="template">
          <el-select v-model="schedule.template" placeholder="template">
            <el-option
              v-for="item in templateGroup"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="warning" @click="onCancelEdit">cancel</el-button>
          <el-button type="primary" @click="onSaveEdit">save</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "schedule-edit",
    props: ['schedule'],
    data(){
      return {
        deviceGroup: [
          {
            id: '00000000-00000000-0000-0000-00000101',
            name: 'device1',
          },
          {
            id: '00000000-00000000-0000-0000-00000102',
            name: 'device2',
          },
          {
            id: '00000000-00000000-0000-0000-00000103',
            name: 'device3',
          },
        ],
        templateGroup: [
          {
            id: '00000000-00000000-0000-0000-00000101',
            name: 'device1',
          },
          {
            id: '00000000-00000000-0000-0000-00000102',
            name: 'device2',
          },
          {
            id: '00000000-00000000-0000-0000-00000103',
            name: 'device3',
          },
        ],
      }
    },
    methods: {
      onSaveEdit(){
        this.resetForm('form');
        this.$store.state.show.scheduleEdit = false;
      },
      onCancelEdit(){
        this.resetForm('form');
        this.$store.state.show.scheduleEdit = false;
      }
    }
  }
</script>

<style scoped>

</style>
