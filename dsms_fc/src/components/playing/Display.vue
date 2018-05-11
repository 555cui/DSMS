<template>
    <div id="playing" :style="style" ref="playing">
        <div v-for="element in template.element" :key="element.id" v-style="element" class="box">
            <Element :element="element"></Element>
        </div>
    </div>
</template>

<script>
    import Element from "./Element";
    import html2canvas from 'html2canvas';
    export default {
        name: "Display",
        components: {Element},
        props: ['template'],
        computed: {
            style(){
                return {
                    backgroundColor: this.template.backgroundColor,
                    width: this.template.width+'px',
                    height: this.template.height+'px',
                }
            }
        },
        directives: {
            style(el, binding){
                el.style.width = binding.value.width + 'px';
                el.style.height = binding.value.height + 'px';
                el.style.top = binding.value.top + 'px';
                el.style.left = binding.value.left + 'px';
                el.style.zIndex = binding.value.zIndex;
            }
        },
        data(){
            return {
                id: '',
            }
        },
        mounted(){
            html2canvas(this.$refs['playing']).then(canvas=>{
                const url = '/DSMS_FE/device/state';
                const state = {
                    device: {
                        id: localStorage.getItem('DevicePlayID')
                    },
                    type: 'playing',
                    url: canvas.toDataURL('image/png')
                };
                this.$ajax.post(url,state);
            });
            let count = 0;
            this.id = setInterval(()=>{
                if (count >= 60000){
                    count = 0;
                    html2canvas(this.$refs['playing']).then(canvas=>{
                        const url = '/DSMS_FE/device/state';
                        const state = {
                            device: {
                                id: localStorage.getItem('DevicePlayID')
                            },
                            type: 'playing',
                            url: canvas.toDataURL('image/png')
                        };
                        this.$ajax.post(url,state);
                    });
                }
                else {
                    const url = '/DSMS_FE/device/state';
                    const state = {
                        device: {
                            id: localStorage.getItem('DevicePlayID')
                        },
                        type: 'playing',
                        url: null,
                    };
                    this.$ajax.post(url,state);
                    count += 5000;
                }
            }, 5000);
        },
        beforeDestroy(){
            clearInterval(this.id);
        }
    }
</script>

<style scoped>
    #playing {
        position: relative;
    }
    .box{
        position: absolute;
    }
</style>