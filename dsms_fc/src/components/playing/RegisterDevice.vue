<template>
    <div>
        <el-dialog
                title="登记设备"
                :visible.sync="show"
                :before-close="beforeClose"
                width="30%">
            <div v-if="step===0" class="step-action">
                <div>你的设备还没登记或登记信息已丢失。</div>
                <div>请输入设备的管理用户名和密码</div>
                <el-form :model="user" :rules="rules" ref="userF">
                    <el-form-item label="name" prop="name">
                        <el-input v-model="user.name"></el-input>
                    </el-form-item>
                    <el-form-item label="password" prop="password">
                        <el-input v-model="user.password" type="password"></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <div v-else-if="step===1" class="step-action">
                <div>
                    <el-radio v-model.number="action" :label="1">登记为新设备</el-radio>
                    <el-radio v-model.number="action" :label="2">输入设备ID</el-radio>
                </div>
            </div>
            <div v-else-if="step===2" class="step-action">
                <div v-if="action===1">
                    <div>请输入设备名称</div>
                    <el-form :model="device" :rules="rulesD" ref="deviceF">
                        <el-form-item label="group" prop="group">
                            <el-select v-model="device.group" value-key="id">
                                <el-option v-for="item in group"
                                           :key="item.id" :label="item.name" :value="item"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="name" prop="name">
                            <el-input v-model="device.name"></el-input>
                        </el-form-item>
                    </el-form>
                </div>
                <div v-else>
                    <div>请输入登记设备的ID</div>
                    <div>设备ID可以登陆管理端，在<span style="color: blue">用户中心=>设备管理</span>处查看</div>
                    <el-input v-model="DeviceId"></el-input>
                </div>
            </div>
            <div v-else class="step-action">
                你已经完成设备登记
            </div>
            <el-steps :active="step" finish-status="success">
                <el-step title="步骤 1"></el-step>
                <el-step title="步骤 2"></el-step>
                <el-step title="步骤 3"></el-step>
            </el-steps>
            <div>
                <el-button @click="step=--step<0?0:step" v-if="step>0 && step<3">上一步</el-button>
                <el-button @click="toNext" :loading="isLoad" v-if="step>=0 && step<3">下一步</el-button>
                <el-button @click="beforeClose" v-if="step===3">完成</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "register-device",
        props: ['show'],
        data(){
            const nameRule = (rule, value, callback) => {
                this.device.user = this.user;
                this.$ajax.post('/device/name', this.device).then(
                    response => {
                        if (response.data.code !== 0) callback(new Error(response.data.msg));
                        else callback();
                    });
            };
            return {
                device: {
                    name: '',
                    user: {
                        name: '',
                        password: '',
                    },
                    group: {
                        id: '',
                        name: '',
                    }
                },
                group: [],
                rules: {
                    name: [{ required: true, message: '请输入用户名', trigger: 'blur' },],
                    password: [{ required: true, message: '请输入密码', trigger: 'blur' },],
                },
                rulesD: {
                    name: [
                        { required: true, message: '请输入设备名称', trigger: 'blur' },
                        {validator: nameRule, trigger: 'blur'}
                    ],
                    group: [{ required: true, message: '请选择设备组', trigger: 'blur' },]
                },
                step:0,
                action: 1,
                DeviceId: '',
                user: {
                    name: '',
                    password: '',
                },
                isLoad: false,
            }
        },
        methods: {
            beforeClose(){
                this.$emit('close');
            },
            toNext(){
                switch (this.step) {
                    case 0:
                        this.isLoad=true;
                        this.$refs['userF'].validate(val=>{
                            if (val){
                                const url = '/deviceGroup/pass';
                                const u = {
                                    name: '',
                                    password: '',
                                };
                                u.name=this.user.name;
                                u.password=this.user.password;
                                console.info(u);
                                this.$ajax.post(url, u).then(response=>{
                                    if (response.data.code===0){
                                        this.group = response.data.data;
                                        this.step=1;
                                    }
                                    else this.$message.error(response.data.msg);
                                    this.isLoad=false;
                                });
                            }
                        });
                        break;
                    case 1:
                        this.step=2;
                        break;
                    case 2:
                        this.isLoad=true;
                        if (this.action===1){
                            this.$refs['deviceF'].validate(val=>{
                                if (val){
                                    const url = '/device/';
                                    const dev = {
                                        name: '',
                                        user: {
                                            name: '',
                                            password: '',
                                        },
                                        group: {
                                            id: ''
                                        },
                                        width: 0,
                                        height: 0,
                                    };
                                    dev.name=this.device.name;
                                    dev.user = this.user;
                                    dev.group.id = this.device.group.id;
                                    dev.width = window.screen.width;
                                    dev.height = window.screen.height;
                                    this.$ajax.post(url, dev).then(response=>{
                                        if (response.data.code===0){
                                            localStorage.setItem("DevicePlayID", response.data.data.id);
                                            this.step=3;
                                        }
                                        else this.$message.error(response.data.msg);
                                        this.isLoad=false;
                                    })
                                }
                            });
                        }
                        else {
                            const url = '/device/check';
                            const dev = {
                                id : '',
                                user: {
                                    name: '',
                                    password: '',
                                }
                            };
                            dev.id=this.DeviceId;
                            dev.user = this.user;
                            this.$ajax.post(url, dev).then(response=>{
                                if (response.data.code===0){
                                    localStorage.setItem("DevicePlayID", this.DeviceId);
                                    this.step=3;
                                }
                                else this.$message.error(response.data.msg);
                                this.isLoad=false;
                            });
                        }
                        break;
                }
            }
        }
    }
</script>

<style scoped>
    .step-action{
        min-height: 30%;
        text-align: center;
    }
</style>