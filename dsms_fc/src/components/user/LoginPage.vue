<template>
  <div>
    <el-dialog :visible.sync="$store.state.show.login" :before-close="closeLoginPage">
      <el-tabs v-model="action" type="card" @tab-click="handleClick">
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
    props: ['action'],
    data(){
      const emailReg=/^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\.)+(com|cn|net|org)$/;
      const nameRule = (rule, value, callback) => {
        this.$ajax.post('/user/checkName', {name: value}, {responseType: 'json'}).then(response => {
          if (response.data.code !== 0){
            callback(new Error(response.data.msg));
          }
          callback();
        });
      };
      const passwordRule = (rule, value, callback) => {
        if (this.user.password !== '') {
          this.$refs.register.validateField('cpassword');
        }
        callback();
      };
      const cpasswordRule = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.user.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      const emailRule = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('please input an email address'));
        } else if (!emailReg.test(value)) {
          callback(new Error('please input a right email address'));
        } else {
          this.$ajax.post('/user/checkEmail', {email: value}, {responseType: 'json'}).then(response => {
            if (response.data.code !== 0){
              callback(new Error(response.data.msg));
            }
            callback();
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
            { required: true, message: 'please input user name', trigger: 'blur' },
            { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
            { validator: nameRule, trigger: 'blur' },
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
            { validator: passwordRule, trigger: 'blur' },
          ],
          cpassword: [
            { validator: cpasswordRule, trigger: 'blur' },
          ],
          email: [
            { validator: emailRule, trigger: 'blur'},
          ],
        },
        loginRules: {
          name: [
            { required: true, message: 'please input user name', trigger: 'blur'},
          ],
          password: [
            { required: true, message: 'please input password', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      closeLoginPage(){
        this.resetForm('login');
        this.resetForm('register');
        this.$store.state.show.login=false;
      },
      handleLogin(){
        this.$refs['login'].validate(valid => {
          if (valid) {
            const user = {
              name: this.user.name,
              password: this.user.password,
            };
            this.$ajax.post(
              '/user/login',
              user,
              {
                responseType: 'json',
              }
            ).then(response => {
              if (response.data.code === 0){
                this.$store.state.userId=response.data.data;
                this.$store.state.loginState=true;
                this.closeLoginPage();
              }else {
                this.$message({
                  showClose: true,
                  message: response.data.msg,
                  type: 'error'
                });
              }
            });
          } else {
            console.log('error submit!!');
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
            this.$ajax.post('/user/register', user, {responseType: 'json',}).then(
              response => {
                if (response.data.code === 0){
                  this.$store.state.userId=response.data.data;
                  this.$store.state.show.login=true;
                  this.closeLoginPage();
                } else {
                  this.$message({
                    showClose: true,
                    message: response.data.msg,
                    type: 'error'
                  });
                }
              });
          } else {
            console.log('error submit!!');
          }
        })
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
