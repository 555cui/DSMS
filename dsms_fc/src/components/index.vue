<template>
    <div>
      <login-page></login-page>
      <el-container class="no-box">
        <el-header class="no-box" height="80px">
            <div id="header-box">
                <div id="menu-box"><el-menu :default-active="$route.path"
                                            background-color="#8cbff8"
                                            text-color="#ffffff"
                                            mode="horizontal"
                                            @select="handleSelect">
                    <el-menu-item index="/first">首页</el-menu-item>
                    <el-menu-item index="/schedule">日程</el-menu-item>
                    <el-menu-item index="/template">模板</el-menu-item>
                    <el-menu-item index="/media">媒体</el-menu-item>
                </el-menu></div>
                <div id="text-box"><h1>DSMS</h1></div>
                <div id="button-box">
                    <div v-if="$store.state.user.login">
                        <el-dropdown split-button
                                     type="primary"
                                     @click="toUserCenter('info')"
                                     @command="userMenuCommand">
                            用户中心
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item command="info">用户信息</el-dropdown-item>
                                <el-dropdown-item command="device">设备管理</el-dropdown-item>
                                <el-dropdown-item command="logout">登出</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </div>
                    <div v-else>
                        <el-button round type="primary" @click="$store.commit('openLogin')">登陆</el-button>
                        <el-button round type="warning" @click="$store.commit('openRegister')">注册</el-button>
                    </div>
                </div>
            </div>
        </el-header>

        <el-main>
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
        methods: {
            handleSelect(key){
                this.$router.push(key);
            },
            userMenuCommand(command){
                if (command !== 'logout'){
                    this.toUserCenter(command);
                } else {
                    if (!this.$store.state.user.logout){
                        const url = '/check/logout';
                        this.$ajax.delete(url).then(response => {
                            if(response.data.code === 0)this.$store.commit(command);
                        });
                    }
                }
            },
            toUserCenter(command){
                this.$store.state.user.centerAction=command;
                this.$router.push('/userCenter');
            },
        },
        created(){
            if (sessionStorage.getItem('dsms_token')!==null)
                this.$store.state.user.login=true;
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
        justify-content: space-between;
    }

    #menu-box{
        margin: 10px;
        width: 300px;
    }
    #button-box{
        padding: 20px 10px;
    }
    #text-box{
        width: 200px;
    }
</style>
