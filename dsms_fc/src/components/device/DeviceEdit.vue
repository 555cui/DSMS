<template>
    <div>
        <el-dialog :visible.sync="show" :before-close="onCloseEnd" @open="onOpenStart" title="设备详情">
            <el-form :model="deviceData" ref="deviceForm" :rules="rules">
                <el-form-item label="id">
                    <el-input v-model="deviceData.id" disabled></el-input>
                </el-form-item>
                <el-form-item label="name" prop="name">
                    <el-input v-model="deviceData.name"></el-input>
                </el-form-item>
                <el-form-item label="group">
                    <el-select v-model="deviceData.group" value-key="id">
                        <el-option v-for="group in deviceGroup"
                                   :key="group.id"
                                   :value="group"
                                   :label="group.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="width">
                    <el-input v-model.number="deviceData.width" disabled></el-input>
                </el-form-item>
                <el-form-item label="height">
                    <el-input v-model.number="deviceData.height" disabled></el-input>
                </el-form-item>
                <el-form-item label="mac" prop="mac">
                    <el-input v-model="deviceData.mac"></el-input>
                </el-form-item>
                <el-form-item label="address">
                    <el-input v-model="deviceData.address"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="onSaveToServer" type="primary">保存</el-button>
                    <el-button @click="onCloseEnd" type="warning">返回</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
  export default {
    name: "DeviceEdit",
    props: ['device', 'show'],
    data(){
      const nameRule = (rule, value, callback) => {
        if (value === this.device.name)callback();
        else {
          this.$ajax.post('/DSMS_FE/device/name', {name: value}, {responseType: 'json'}).then(response => {
            if (response.data.code >= 10 && response.data.code < 20)
              this.$store.state.user.code=response.data.code;
            else if (response.data.code !== 0)callback(new Error(response.data.msg));
            else callback();
          }).catch(e=>{
            callback(new Error('请求失败'));
          });
        }
      };
      const macRule = (rule, value, callback) => {
        if (value === '')callback();
        else if (value === this.device.mac)callback();
        else {
          this.$ajax.post('/DSMS_FE/device/mac', {mac: value}).then(response => {
            if (response.data.code === 0)
              callback();
            else if (response.data.code >= 10 && response.data.code < 20)
              this.$store.state.user.code=response.data.code;
            else callback(new Error(response.data.msg));
          })
        }
      };
      return {
        deviceData: {
          id: '',
          name: '',
          group: null,
          width: 0,
          height: 0,
          mac: '',
          address: '',
        },
        rules: {
          name: [
            { required: true, message: '请输入设备名称', trigger: 'blur' },
            { validator: nameRule, trigger: 'blur' },
          ],
          mac: [
            { validator: macRule, trigger: 'blur'},
          ]
        },
        deviceGroup: [],
      }
    },
    methods: {
      onOpenStart(){
        const url = '/DSMS_FE/deviceGroup/';
        this.$ajax.get(url).then(e=>{
          if (e.data.code===0){
            this.deviceGroup = e.data.data;
          }
        });
        this.deviceData = this.device;
      },
      onSaveToServer(){
        const url = '/DSMS_FE/device/'+this.device.id;
        this.$refs['deviceForm'].validate(value => {
          if (value){
            this.$ajax.put(url, this.deviceData).then(response=>{
              if (response.data.code===0){
                this.$emit('edit', response.data.data);
                console.info('finish');
                this.onCloseEnd();
              }
              else if (response.data.code>=10&&response.data.code<20){
                this.$store.state.user.code = response.data.code;
              }
            });
          }
        });
      },
      onCloseEnd(){
        this.$refs['deviceForm'].resetFields();
        this.deviceData = {
          id: '',
          name: '',
          group: null,
          width: 0,
          height: 0,
          mac: '',
          address: '',
        };
        this.$emit('close');
      }
    }
  }
</script>

<style scoped>

</style>