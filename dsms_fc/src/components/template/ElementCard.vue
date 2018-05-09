<template>
    <div id="padding">
        <div class="box" id="close-button"><i class="el-icon-circle-close" @click="onRemove"></i></div>
        <div class="box" id="show-box"><canvas v-draw="{element:element,base:base}"></canvas></div>
    </div>
</template>

<script>
    export default {
        name: "ElementCard",
        props: ['element', 'base'],
        methods: {
            onRemove(){
                this.$emit('remove', this.element);
            }
        },
        directives: {
            draw: (el, binding)=>{
                const element = binding.value.element;
                const base = binding.value.base;
                el.width = Math.round(element.width * base);
                el.height = Math.round(element.height * base);

                const pan = el.getContext("2d");

                if (element.media.type === 'text'){
                    if (element.media._type === 'row'){
                        const textSize = Math.ceil(element.media.etcData * base);
                        pan.font=textSize+'px Georgia';
                        pan.fillText(element.media.url, 0, textSize);
                    }
                    else if(element.media._type === 'col'){
                        const pan = el.getContext("2d");
                        const textSize = Math.ceil(element.media.etcData * base);
                        pan.font=textSize+"px Georgia";
                        let lineWidth = 0;
                        let initHeight= textSize;
                        let lastSubStrIndex = 0;
                        for(let i=0;i<element.media.url.length;i++){//循环绘制多行文本
                            lineWidth=pan.measureText(element.media.url.substring(lastSubStrIndex, i)).width;
                            if(lineWidth>element.width*base){
                                pan.fillText(element.media.url.substring(lastSubStrIndex,i-1),0,initHeight);
                                lineWidth=0;//回车
                                initHeight+=textSize;//换行
                                lastSubStrIndex=i-1;
                            }
                            if(i===element.media.url.length-1){//绘制最后一行
                                pan.fillText(element.media.url.substring(lastSubStrIndex),0,initHeight);
                                lineWidth=0;//回车
                                initHeight+=textSize;//换行
                            }
                        }
                    }
                    else {
                        const textSize = Math.ceil(element.media.etcData * base);
                        pan.font=textSize+'px Georgia';
                        const content = element.media.name + ':' + element.media.imageUrl;
                        pan.fillText(content, 0, textSize);
                    }
                }
                else {
                    const image = new Image();
                    image.onload = ev =>{
                        pan.drawImage(image, 0, 0, el.width, el.height);
                    };
                    image.src = element.media.imageUrl;
                }
            },
        }

    }
</script>

<style scoped>
    #padding {
        position: relative;
    }
    #close-button {
        top: 0;
        right: 0;
        z-index: 1;
    }
    #show-box {
        z-index: 0;
    }
    .box {
        position: absolute;
    }
</style>