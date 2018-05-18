<template>
    <div @click="fullscreen">
        <register-device :show="msg==='noRegister'" @close="looping"></register-device>
        <display :template="template" v-if="msg==='playing'" id="display"></display>
        <div v-else id="msg-box">{{msg}}</div>
    </div>
</template>

<script>
    import RegisterDevice from "./RegisterDevice";
    import Display from "./Display";
    export default {
        name: "Playing",
        components: {Display, RegisterDevice},
        data(){
            return {
                id: null,
                template: {
                    id: '',
                    element:[]
                },
                msg: 'waiting',
                interval: null,
                deviceState: {
                    id: null,
                    state: null,
                }
            }
        },
        created(){
            this.id = localStorage.getItem('DevicePlayID');
            if (this.id === null){
                this.msg='noRegister';
            }
            else {
                this.getTemplate();
                this.looping();
                this.$message.success('开始定时任务');
            }
        },
        methods: {
            looping(){
              this.msg='waiting';
              this.interval = setInterval(this.getTemplate, 5*1000);
            },
            getTemplate(){
                console.info('开始获取模板');
                console.info('获取设备唯一标识');
                this.id = localStorage.getItem('DevicePlayID');
                if (this.id === null){
                    console.info('丢失唯一标识');
                    this.msg='noRegister';
                  clearInterval(this.interval);
                  this.$message.error('结束定时任务');
                }
                else {
                    console.info('获取设备唯一标识成功:'+this.id);
                    console.info('开始请求模板');
                    const url = '/DSMS_FE/program/'+this.id;
                    this.$ajax.get(url).then(response=>{
                        if (response.data.code===0){
                            console.info('获取成功');
                            this.template = response.data.data.template;
                            this.msg='playing';
                            console.info(this.msg);
                        }
                        else if (response.data.code===60){
                            console.info('当前时间没有播放任务');
                            this.template={
                                id: '',
                                element: [],
                            };
                            this.msg='ready';
                        }
                        else if (response.data.code===61){
                            console.info('设备已经被注销');
                            this.template={
                                id: '',
                                element: [],
                            };
                            localStorage.removeItem('DevicePlayID');
                            this.msg='noRegister';
                            this.$message.error('no register');
                          clearInterval(this.interval);
                          this.$message.error('结束定时任务');
                        }
                    })
                }
            },
            openFullscreen(element) {
                if (element.requestFullscreen) {
                    element.requestFullscreen();
                } else if (element.mozRequestFullScreen) {
                    element.mozRequestFullScreen();
                } else if (element.msRequestFullscreen) {
                    element.msRequestFullscreen();
                } else if (element.webkitRequestFullscreen) {
                    element.webkitRequestFullScreen();
                }
            },
            fullscreen(){
                this.id = localStorage.getItem('DevicePlayID');
                if (this.id === null){
                    this.msg='noRegister';
                }
                else this.openFullscreen(window.document.getElementById('display'));
            }
        },
        beforeDestroy(){
            clearInterval(this.interval);
            this.$message.error('结束定时任务');
        },
    }
</script>

<style scoped>
    #msg-box{
        text-align: center;
        font-size: 200px;
    }

    #display {
        background-color: blue;
    }
</style>