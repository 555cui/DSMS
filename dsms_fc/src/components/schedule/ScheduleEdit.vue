<template>
    <div>
        <el-dialog :title="title" :visible.sync="$store.state.schedule.show" @open="onOpenEdit" width="70%">
            <el-form ref="scheduleForm" :model="scheduleD" label-width="80px" :rules="rules">
                <el-form-item label="type">
                    <el-radio-group v-model="scheduleD.type">
                        <el-radio label="device">device</el-radio>
                        <el-radio label="group">group</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="device" v-if="scheduleD.type==='device'" prop="device">
                    <el-select v-model="scheduleD.device"
                               value-key="id">
                        <el-option-group v-for="item in deviceGroup" :key="item.id" :label="item.name">
                            <el-option v-for="device1 in item.device" :key="device1.id"
                                       :label="device1.name" :value="device1"></el-option>
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="group" v-else-if="scheduleD.type==='group'" prop="deviceGroup">
                    <el-select v-model="scheduleD.deviceGroup" value-key="id">
                        <el-option
                                v-for="item in deviceGroup"
                                :key="item.id"
                                :label="item.name"
                                :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="date" prop="date">
                    <el-date-picker
                            v-model="scheduleD.date"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            format="yyyy 年 MM 月 dd 日"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="week" prop="week">
                    <el-checkbox-group v-model="scheduleD.week" size="small">
                        <el-checkbox-button label="64">Sun</el-checkbox-button>
                        <el-checkbox-button label="1">Mon</el-checkbox-button>
                        <el-checkbox-button label="2">Tue</el-checkbox-button>
                        <el-checkbox-button label="4">Wed</el-checkbox-button>
                        <el-checkbox-button label="8">Thu</el-checkbox-button>
                        <el-checkbox-button label="16">Fri</el-checkbox-button>
                        <el-checkbox-button label="32">Sat</el-checkbox-button>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item v-for="(item,index) in scheduleD.program"
                              :key="index"
                              :label="'时间段'+index" :error="item.error">
                    <el-time-select @blur="programValidator(index)" v-model="item.startTime" :picker-options="getStartOptions(item)">
                    </el-time-select>
                    -
                    <el-time-select @blur="programValidator(index)" v-model="item.endTime" :picker-options="getEndOptions(item)">
                    </el-time-select>
                    <el-select @blur="programValidator(index)" v-model="item.template" value-key="id">
                        <el-option-group v-for="group1 in templateGroup" :key="group1.id" :label="group1.name">
                            <el-option v-for="item1 in group1.template"
                                       :key="item1.id"
                                       :label="item1.name"
                                       :value="item1">
                            </el-option>
                        </el-option-group>
                    </el-select>
                    <el-button circle size="mini" icon="el-icon-circle-plus" @click="insertProgram(item)"></el-button>
                    <el-button circle size="mini" icon="el-icon-remove" @click="removeProgram(item)"></el-button>
                </el-form-item>
                <el-form-item>
                    <el-button @click="onCancelEdit">取 消</el-button>
                    <el-button type="primary" @click="onSaveEdit">保 存</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "schedule-edit",
        props: ['schedule', 'action'],
        data(){
            return {
                deviceGroup: [],
                templateGroup: [],
                title: '新建日程',
                scheduleD: {
                    id: null,
                    device: null,
                    deviceGroup: null,
                    date: [],
                    program: [
                        {
                            startTime: null,
                            endTime: null,
                            template: null,
                            error: '',
                        }
                    ],
                    week: [],
                    type: 'device',
                },
                rules: {
                    device: [{ required: true, message: '请选择设备', trigger: 'blur'},],
                    deviceGroup: [{ required: true, message: '请选择设备组', trigger: 'blur'},],
                    date: [{ required: true, message: '请选择日期', trigger: 'blur'},],
                    week: [{ required: true, message: '请选择星期', trigger: 'blur'},],
                },
                scheduleForm: null,
            }
        },
        created(){
            const url = '/DSMS_FE/deviceGroup/';
            this.$ajax.get(url).then(response=>{
                if (response.data.code===0){
                    this.deviceGroup=response.data.data;
                }
                else if (response.data.code>=10&&response.data.code<20)
                    this.$store.state.user.code=response.data.code;
                else this.$message.error('获取数据出错');
            }).catch(e=>{this.$message.error('服务器出错')});

            const url2 = '/DSMS_FE/templateGroup/';
            this.$ajax.get(url2).then(response=>{
                if (response.data.code===0){
                    this.templateGroup=response.data.data;
                }
                else if (response.data.code>=10&&response.data.code<20)
                    this.$store.state.user.code=response.data.code;
                else this.$message.error('获取数据出错');
            }).catch(e=>{this.$message.error('服务器出错')});
        },
        methods: {
            getStartOptions(item){
                const length = this.scheduleD.program.length;
                let index = this.scheduleD.program.indexOf(item);
                const a = {
                    start: '00:00',
                    step: '01:00',
                    end: '23:00',
                };
                if (index >0){
                    a.minTime = this.scheduleD.program[index-1].endTime;
                }
                if (index < length-1){
                    a.maxTime = this.scheduleD.program[index+1].startTime;
                }
                return a;
            },
            getEndOptions(item){
                const length = this.scheduleD.program.length;
                let index = this.scheduleD.program.indexOf(item);
                const a = {
                    start: '00:59',
                    step: '01:00',
                    end: '23:59',
                    minTime: item.startTime,
                };
                if (index < length-1){
                    a.maxTime = this.scheduleD.program[index+1].startTime;
                }
                return a;
            },
            insertProgram(item){
                const i = this.scheduleD.program.indexOf(item);
                if (i!==-1){
                    const a = {
                        startTime: null,
                        endTime: null,
                        template: null,
                        error: '',
                    };
                    this.scheduleD.program.splice(i+1, 0, a);
                }
            },
            removeProgram(item){
                const i = this.scheduleD.program.indexOf(item);
                if (i!==-1 && i!==0)
                    this.scheduleD.program.splice(i,1);
            },
            onOpenEdit(){
                if (this.action === 'edit'){
                    this.title = '修改日程';
                    this.scheduleD.id = this.schedule.id;
                    if (this.schedule.device !== null){
                        this.scheduleD.type = 'device';
                        this.scheduleD.device = this.schedule.device;
                    }
                    else if (this.schedule.deviceGroup !== null){
                        this.scheduleD.type = 'group';
                        this.scheduleD.deviceGroup = this.schedule.deviceGroup;
                    }
                    this.scheduleD.program = this.schedule.program;
                    console.info(this.schedule.startDate);
                    this.scheduleD.date=[this.schedule.startDate, this.schedule.endDate];
                    let week = this.schedule.week;
                    for (let i = 64; i>=1; i/=2){
                        if (week-i>=0){
                            this.scheduleD.week.push(''+i);
                            week-=i;
                        }
                    }
                }
                else {
                    this.title = '新建日程';
                    this.scheduleD = {
                        id: null,
                        device: null,
                        deviceGroup: null,
                        date: [],
                        program: [
                            {
                                startTime: null,
                                endTime: null,
                                template: null,
                                error: '',
                            }
                        ],
                        week: [],
                        type: 'device',
                    };
                }
            },
            onSaveEdit(){
                for (const program of this.scheduleD.program){
                    this.checkProgram(program, res=>{
                        if (res!=='success'){
                            program.error=res;
                        }
                        else{
                            program.error='';
                            this.saveToServer();
                        }
                    });
                }
            },
            saveToServer(){
                const url = '/DSMS_FE/schedule/';
                const scheduleData = {
                    startDate: null,
                    endDate: null,
                    week: 0,
                    program: [],
                    device: null,
                    deviceGroup: null,
                };
                scheduleData.startDate=this.scheduleD.date[0];
                scheduleData.endDate=this.scheduleD.date[1];
                for (let num of this.scheduleD.week)
                    scheduleData.week+=parseInt(num);
                for (let item of this.scheduleD.program){
                    scheduleData.program.push({
                        startTime: item.startTime,
                        endTime: item.endTime,
                        template: {
                            id: item.template.id,
                        }
                    })
                }
                if (this.scheduleD.type === 'group'){
                  scheduleData.deviceGroup=this.scheduleD.deviceGroup;
                }
                if (this.scheduleD.type === 'device'){
                  scheduleData.device=this.scheduleD.device;
                }
                if (this.action==='new'){
                    this.$ajax.post(url,scheduleData).then(response=>{
                        if (response.data.code===0){
                            console.info('success');
                            this.$emit('finish');
                        }
                    });
                }
                else {
                    this.$ajax.put(url+this.schedule.id, scheduleData).then(response=>{
                        if (response.data.code===0){
                            console.info('success');
                            this.$emit('finish');
                        }
                    })
                }
                this.resetForm('scheduleForm');
                this.$store.state.schedule.show = false;
            },
            onCancelEdit(){
                this.resetForm('scheduleForm');
                this.$store.state.schedule.show = false;
            },
            resetForm(formName){
                this.$refs[formName].resetFields();
                this.scheduleD.program=[{startTime:'',endTime:'',template: null, error: ''}]
            },
            checkProgram(program, callback){
                this.$refs['scheduleForm'].validate(valid=>{
                    if (valid){
                        const scheduleData = {
                            startDate: null,
                            endDate: null,
                            week: 0,
                            program: [],
                            device: null,
                            deviceGroup: null,
                        };
                        scheduleData.startDate=this.scheduleD.date[0];
                        scheduleData.endDate=this.scheduleD.date[1];
                        for (let num of this.scheduleD.week)
                            scheduleData.week+=parseInt(num);
                        if (program.startTime===null||program.startTime===null||program.template===null){
                            callback('请选择数据');
                            return;
                        }
                        scheduleData.program.push({
                            startTime: program.startTime,
                            endTime: program.endTime,
                            template: program.template,
                        });
                        if (this.scheduleD.type === 'group'){
                          scheduleData.deviceGroup=this.scheduleD.deviceGroup;
                          scheduleData.device=null;
                        }
                        if (this.scheduleD.type === 'device'){
                          scheduleData.device=this.scheduleD.device;
                          scheduleData.deviceGroup=null;
                        }
                        this.$ajax.post('/DSMS_FE/schedule/check', scheduleData).then(response=>{
                            if (response.data.code===0){
                                if (this.action==='new'){
                                    if (response.data.data.length===0)
                                        callback('success');
                                    else callback('日程冲突');
                                }
                                else {
                                    if (response.data.data.length===1&&response.data.data[0].id===this.scheduleD.id)
                                        callback('success');
                                    else callback('日程冲突');
                                }
                            }
                        });
                    }
                });
            },
            programValidator(index){
                this.checkProgram(this.scheduleD.program[index], flag1=>{
                    if (flag1!=='success'){
                        this.scheduleD.program[index].error=flag1;
                        return false;
                    }
                    else {
                        this.scheduleD.program[index].error='';
                        return true;
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>
