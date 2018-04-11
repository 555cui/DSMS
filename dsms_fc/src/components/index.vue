<template>
    <div>
      <login-page :action="btnAction"></login-page>
      <el-container class="no-box">
        <el-header id="main-header" height="80px">
            <el-col :span="8">
              <el-menu id="menulist"
                       :default-active="$route.path"
                       background-color="aqua"
                       mode="horizontal"
                       @select="handleSelect">
                <el-menu-item index="/first">first</el-menu-item>
                <el-menu-item index="/schedule">schedule</el-menu-item>
                <el-menu-item index="/template">template</el-menu-item>
                <el-menu-item index="/media">media</el-menu-item>
              </el-menu>
            </el-col>
            <el-col :span="8">
              <div id="dsmsmsg">DSMS</div>
            </el-col>
            <el-col :span="8">
              <div id="usercenter" v-if="$store.state.loginState">
                <el-dropdown split-button
                             type="primary"
                             @click="toUserCenter('info')"
                             @command="userMenuCommand">
                  user center
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="info">user info</el-dropdown-item>
                    <el-dropdown-item command="device">device manager</el-dropdown-item>
                    <el-dropdown-item command="logout">logout</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
              <div id="bottons" v-else>
                <el-button round type="primary" @click="onLogin">login</el-button>
                <el-button round type="warning" @click="onRegister">register</el-button>
              </div>
            </el-col>
        </el-header>

        <el-main id="main-main">
          <router-view></router-view>
        </el-main>
      </el-container>
    </div>
</template>

<script>
  import LoginPage from "./user/LoginPage";
  export default {
    components: {LoginPage},
    name: "index",
    comments: {LoginPage},
    data(){
      return {
        btnAction: 'login',
      }
    },
    methods: {
      handleSelect(key, keyPath){
        console.log(key, keyPath);
        this.$router.push(key);
      },
      onLogin(){
        this.btnAction='login';
        this.$store.state.show.login=true;
      },
      onRegister(){
        this.btnAction='register';
        this.$store.state.show.login=true;
      },
      userMenuCommand(command){
        if (command !== 'logout'){
          this.toUserCenter(command);
        } else {
          if (this.$store.state.loginState){
            const url = '/user/'+this.$store.state.userId;
            this.$ajax.delete(url).then(response => {
              if(response.data.code === 0)this.$store.state.loginState=false;
            });
          }
        }
      },
      toUserCenter(command){
        this.$store.state.centerSelect=command;
        this.$router.push('userCenter');
      },
    }
  }
</script>

<style scoped>
  .no-box{
    margin: 0;
    padding: 0;
  }
  #main-header{
    background-color: gray;
  }
  #bottons{
    margin: 15px 5px;
    text-align: right;
  }
  #usercenter{
    margin: 15px;
    text-align: right;
  }
  #dsmsmsg{
    font-size: 55px;
    margin-left: 120px;
  }
</style>
