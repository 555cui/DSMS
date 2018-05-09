<template>
  <div>
    <el-dialog :visible.sync="show" :before-close="closeLoginPage">
      <el-tabs v-model="$store.state.user.action" type="card" @tab-click="handleClick">
        <el-tab-pane label="login" name="login">
          <el-form ref="login" :model="user" :rules="loginRules" label-width="150px">
            <el-form-item label="name" prop="name">
              <el-input v-model="user.name"></el-input>
            </el-form-item>
            <el-form-item label="password" prop="password">
              <el-input type="password" v-model="user.password"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleLogin">login</el-button>
              <el-button type="warning" @click="closeLoginPage">cancel</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="register" name="register">
          <el-form status-icon ref="register" :model="user" label-width="160px" :rules="rules">
            <el-form-item label="name" prop="name">
              <el-input v-model="user.name"></el-input>
            </el-form-item>
            <el-form-item label="password" prop="password">
              <el-input type="password" v-model="user.password"></el-input>
            </el-form-item>
            <el-form-item label="check password" prop="cpassword">
              <el-input type="password" v-model="user.cpassword"></el-input>
            </el-form-item>
            <el-form-item label="email" prop="email">
              <el-input v-model="user.email"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleRegister">register</el-button>
              <el-button type="warning" @click="closeLoginPage">cancle</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
    export default {
        name: "login-page",
        data(){
            const emailReg=/^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\.)+(com|cn|net|org)$/;
            const nameRule = (rule, value, callback) => {
                this.$ajax.post('/check/name', {name: value}, {responseType: 'json'}).then(response => {
                    if (response.data.code !== 0)callback(new Error(response.data.msg));
                    else callback();
                });
            };
            const passwordRule = (rule, value, callback) => {
                if (this.user.password !== '') this.$refs.register.validateField('cpassword');
                callback();
            };
            const cpasswordRule = (rule, value, callback) => {
                if (value === '') callback(new Error('请再次输入密码'));
                else if (value !== this.user.password) callback(new Error('两次输入密码不一致!'));
                else callback();
            };
            const emailRule = (rule, value, callback) => {
                if (value === '') callback(new Error('请输入邮箱'));
                else if (!emailReg.test(value)) callback(new Error('邮箱格式不正确'));
                else {
                    this.$ajax.post('/check/email', {email: value}, {responseType: 'json'}).then(
                        response => {
                            if (response.data.code !== 0)callback(new Error(response.data.msg));
                            else callback();
                        });
                    }
                };
            return {
                user: {
                    name: '',
                    password: '',
                    email: '',
                    cpassword: '',
                },
                rules: {
                    name: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
                        { validator: nameRule, trigger: 'blur' },
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
                        { validator: passwordRule, trigger: 'blur' },
                    ],
                    cpassword: [
                        { required: true, message: '请再次输入密码', trigger: 'blur' },
                        { validator: cpasswordRule, trigger: 'blur' },],
                    email: [
                        { required: true, message: '请输入邮箱', trigger: 'blur'},
                        { validator: emailRule, trigger: 'blur'},],
                },
                loginRules: {
                    name: [{ required: true, message: '请输入用户名', trigger: 'blur'},],
                    password: [{ required: true, message: '请输入密码', trigger: 'blur'}]
                }
            }
        },
        computed: {
            show(){
                return this.$store.state.user.show;
            },
        },
        methods: {
            closeLoginPage(){
                this.handleClick();
                this.$store.state.user.show=false;
            },
            handleLogin(){
                this.$refs['login'].validate(valid => {
                    if (valid) {
                        const user = {
                            name: this.user.name,
                            password: this.user.password,
                        };
                        this.$ajax.post('/check/login', user).then(response => {
                            if (response.data.code === 0){
                                this.$store.commit("login");
                                this.closeLoginPage();
                            }else {
                                this.$message.error(response.data.msg);
                            }
                        });
                    }
                });
            },
            handleRegister() {
                this.$refs['register'].validate(valid => {
                    if (valid) {
                        const user = {
                            name: this.user.name,
                            password: this.user.password,
                            email: this.user.email,
                        };
                        this.$ajax.post('/check/register', user).then(response => {
                            if (response.data.code === 0){
                                this.$store.commit("login");
                                this.closeLoginPage();
                            } else {
                                this.$message.error(response.data.msg);
                            }
                        });
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            handleClick(){
                this.resetForm('login');
                this.resetForm('register');
            },
        },
    }
</script>

<style scoped>

</style>
