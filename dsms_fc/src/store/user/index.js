export default {
    state: {
        show: false,
        action: 'login',
        code: 0,
        centerAction: 'device',
        login: false,
    },
    mutations: {
        openLogin(state){
            state.action='login';
            state.show=true;
        },
        openRegister(state){
            state.action='register';
            state.show=true;
        },
        logout(state){
            localStorage.removeItem("dsms_token");
            state.code = 10;
            state.login = false;
        },
        login(state){
            localStorage.setItem('dsms_token', (new Date()).valueOf()+'');
            state.code = 0;
            state.login = true;
        }
    },
}
