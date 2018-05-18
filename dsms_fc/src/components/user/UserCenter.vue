<template>
    <div>
        <device-edit :device="selectDevice" :show="deviceEditShow" @edit="onEditDevice" @close="deviceEditShow=false"></device-edit>
        <el-container class="no-box">
            <el-header height="80px" class="no-box">
                <div id="header-box">
                    <div class="text-div"><h1>用户中心</h1></div>
                    <div class="button-div">
                        <el-button @click="handleReturn">返 回</el-button>
                    </div>
                </div>
            </el-header>
            <el-main class="no-box">
                <el-container>
                    <el-aside>
                        <el-menu
                                :default-active="defaultCenterAction"
                                @select="onSelectMenu">
                            <el-menu-item index="info">
                                <template slot="title">
                                    <i class="el-icon-setting"></i>
                                    <span>用户信息</span>
                                </template>
                            </el-menu-item>
                            <el-menu-item index="device">
                                <template slot="title">
                                    <i class="el-icon-menu"></i>
                                    <span>设备管理</span>
                                </template>
                            </el-menu-item>
                        </el-menu>
                    </el-aside>
                    <el-main>
                        <div style="width: 80%" v-if="centerAction === 'info'">
                            <el-form ref="userForm" :model="userFormData" label-width="200px">
                                <el-form-item label="user name">
                                    <el-input v-model="userFormData.name" :disabled="true"></el-input>
                                </el-form-item>
                                <el-form-item label="email">
                                    <el-input v-model="userFormData.email" type="email"></el-input>
                                </el-form-item>
                                <el-form-item label="new password">
                                    <el-input v-model="userFormData.password" type="password"></el-input>
                                </el-form-item>
                                <el-form-item label="password again">
                                    <el-input v-model="userFormData.cpassword" type="password"></el-input>
                                </el-form-item>
                                <el-form-item>
                                    <div style="text-align: right">
                                        <el-button type="primary" @click="onUpdateUser">保存</el-button>
                                    </div>
                                </el-form-item>
                            </el-form>
                        </div>
                        <div v-else-if="centerAction === 'device'">
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
                                                width="55">
                                        </el-table-column>
                                        <el-table-column
                                                prop="createTime"
                                                label="创建日期"
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
                                                prop="id"
                                                label="ID">
                                        </el-table-column>
                                        <el-table-column
                                                prop="width"
                                                label="width">
                                        </el-table-column>
                                        <el-table-column prop="height"
                                                         label="height">
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
                    </el-main>
                </el-container>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import DeviceEdit from "../device/DeviceEdit";
    export default {
        name: "user-center",
      components: {DeviceEdit},
      computed: {
            defaultCenterAction(){
                return this.$store.state.user.centerAction;
            }
        },
        mounted(){
            const url = '/DSMS_FE/user/';
            this.$ajax.get(url).then(
                response => {
                    if (response.data.code === 0){
                        this.userFormData.name = response.data.data.name;
                        this.userFormData.email = response.data.data.email;
                    } else {
                        this.$store.state.user.code = response.data.code;
                    }
                }
            );
            const url2 = '/DSMS_FE/deviceGroup/';
            this.$ajax.get(url2).then(response => {
                if (response.data.code === 0){
                    this.group = response.data.data;
                }
                else if (response.data.code>=10&&response.data.code<20)
                    this.$store.state.user.code=response.data.code;
                else this.$message.error('获取数据出错');
            }).catch(e=>{this.$message.error('服务器出错')});
        },
        data(){
            const emailReg=/^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\.)+(com|cn|net|org)$/;
            const passwordRule = (rule, value, callback) => {
                if (value !== '') {
                    this.$refs.register.validateField('cpassword');
                }else {
                    callback();
                }
            };
            const cpasswordRule = (rule, value, callback) => {
                if (this.userFormData.password !== ''){
                    if (value === '') {
                        callback(new Error('请再次输入密码'));
                    } else if (value !== this.userFormData.password) {
                        callback(new Error('两次输入密码不一致!'));
                    }
                }
                callback();
            };
            const emailRule = (rule, value, callback) => {
                if (value !== '' && value !== this.user.email){
                    if (!emailReg.test(value)) {
                        callback(new Error('邮箱格式不正确'));
                    } else {
                        this.$ajax.post('/DSMS_FE/check/email', {email: value}).then(response => {
                            if (response.data.code === 0){
                                callback();
                            } else {
                                callback(new Error(response.data.msg));
                            }
                        });
                    }
                } else {
                    callback();
                }
            };
            const action = this.$store.state.user.centerAction;
            return {
                centerAction: action,
                group: [],
                userFormData: {
                    name: '',
                    email: '',
                    password: '',
                    cpassword: '',
                },
                rules: {
                    password: [
                        { validator: passwordRule, trigger: 'blur' },
                    ],
                    cpassword: [
                        { validator: cpasswordRule, trigger: 'blur' },
                    ],
                    email: [
                        { validator: emailRule, trigger: 'blur'},
                    ],
                },
                deviceSelection: [],
                groupSelectionIndex: 0,
              selectDevice: {
                id: '',
                name: '',
                width: '',
                height: '',
                mac: '',
                address: '',
                group: {},
                state: '',
              },
              deviceEditShow: false,
            }
        },
        methods: {
          onSelectMenu(key, keyPath){
            this.centerAction=key;
          },
          handleReturn(){
            window.history.go(-1);
          },
          onUpdateUser(){
                const url = '/DSMS_FE/user/';
                const userData = {
                    name: this.userFormData.name,
                    password: this.userFormData.password,
                    email: this.userFormData.email,
                };
                this.$ajax.put(url, userData, {responseType: 'json'}).then(
                    response => {
                        if (response.data.code === 0){
                            this.userFormData.name=response.data.data.name;
                            this.userFormData.password='';
                            this.userFormData.cpassword='';
                            this.userFormData.email=response.data.data.email;
                            this.$message('保存成功！');
                        } else {
                            this.$store.commit('logout');
                        }
                    }
                )
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
            const index = this.group[this.groupSelectionIndex].device.findIndex(item=>item.id===device.id);
            this.group[this.groupSelectionIndex].device.splice(index, 1);
            const index2 = this.group.findIndex(item=>item.id===device.group.id);
            this.group[index2].device.push(device);
          },
        }
    }
</script>

<style scoped>
    .no-box{
        margin: 0;
        padding: 0;
    }
    #header-box{
        background-color: #8cbff8;
        display: flex;
    }
    .button-div{
        padding: 15px 5px;
        margin-left: 75%;
    }
    .text-div{
        margin: 0 50px;
    }
</style>
