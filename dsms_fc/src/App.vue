<template>
    <div id="app">
        <logout-page></logout-page>
        <router-view></router-view>
    </div>
</template>

<script>
import LogoutPage from "./components/user/LogoutPage";
export default {
    name: 'App',
    components: {LogoutPage},
    created(){
        const login = localStorage.getItem('dsms_token');
        if (login!==null){
            if (parseInt(login)+60*60*1000>(new Date()).valueOf()){
                this.$store.commit('login');
            }
            else localStorage.removeItem('dsms_token');
        }
    }
}
</script>

<style>
#app {
}
</style>
