<template>
    <div>
        <video :id="element.id"
               :src="element.media.url"
               hidden playsinline :muted="true"
               v-if="element.media.type==='video'">
        </video>
        <img hidden :id="element.id"
             :src="element.media.url"
             v-else-if="element.media.type==='image'"/>
        <div v-else-if="element.media.type==='group'">
            <video :id="element.id"
                   :src="src"
                   hidden playsinline autoplay :muted="true"
                   v-if="element.media._type==='video'"></video>
            <img hidden :id="element.id"
                 :src="src"
                 v-else-if="element.media._type==='image'"/>
        </div>
        <canvas v-draw="element" :width="element.width" :height="element.height">你的浏览器不支持canvas</canvas>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        name: "Element",
        props: ['element'],
        created(){
            if (this.element.media.type==='group'){
                this.src = this.element.media.group[0].url;
            }
        },
        mounted(){
            if (this.element.media.type==='group') {
              if (this.element.media._type === 'video') {
                const video = document.getElementById(this.element.id);
                video.onended = ev => {
                  let index = this.element.media.group.findIndex(item => item.url = this.src);
                  index = (index + 1) % this.element.media.group.length;
                  this.src = this.element.media.group[index].url;
                }
              }
              else if (this.element.media._type === 'image') {
                this.id = setInterval(() => {
                  if (this.etc >= this.element.media.etcData) {
                    const image = document.getElementById(this.element.id);
                    this.etc = 0;
                    let index = this.element.media.group.findIndex(item => item.url = this.src);
                    index = (index + 1) % this.element.media.group[index].url;
                    this.src = this.element.media.group[index].url;
                  }
                  this.etc += 20;
                }, 20);
              }
            }
        },
        beforeDestroy(){
            clearInterval(this.id);
        },
        data(){
            return {
                src: '',
                id: '',
                etc: 0,
            }
        },
        directives: {
            draw: {
                inserted(el, binding){
                  if (binding.value.media.type==='video'){
                    const video = document.getElementById(binding.value.id);
                    video.onended = ev => {
                      video.play();
                    };
                    video.play();
                  }
                    if (binding.value.media.type!=='text'){
                        el.dataset.id = setInterval(()=>{
                            const pan = el.getContext("2d");
                            const element = document.getElementById(binding.value.id);
                            pan.drawImage(element, 0, 0, binding.value.width, binding.value.height);
                        }, 20);
                    }
                    else {
                        if (binding.value.media._type==='row'){
                            let x = 0;
                            el.dataset.id = setInterval(()=>{
                                el.width = el.width;
                                el.height = el.height;
                                const pan = el.getContext("2d");
                                const content = binding.value.media.url;
                                const textSize = binding.value.media.etcData;
                                pan.font=textSize+"px Georgia";
                                const textShow = pan.measureText(content);
                                pan.fillText(content, -x, textSize);
                                if (textShow.width>binding.value.width){
                                    const spe = Math.ceil(textSize / 50);
                                    x = (x+spe)%textShow.width;
                                }
                            }, 20);
                        }
                        else if (binding.value.media._type==='col'){
                            let y = 0;
                            el.dataset.id = setInterval(()=>{
                                el.width = el.width;
                                el.height = el.height;
                                const pan = el.getContext("2d");
                                const content = binding.value.media.url;
                                const textSize = binding.value.media.etcData;
                                pan.font=textSize+"px Georgia";
                                let lineWidth = 0;
                                let initHeight= textSize;
                                let lastSubStrIndex = 0;
                                for(let ia=0;ia<content.length;ia++){//循环绘制多行文本
                                    lineWidth=pan.measureText(content.substring(lastSubStrIndex, ia)).width;
                                    if(lineWidth>binding.value.width){
                                        pan.fillText(content.substring(lastSubStrIndex, ia-1),0,initHeight-y);
                                        lineWidth=0;//回车
                                        initHeight+=textSize;//换行
                                        lastSubStrIndex=ia-1;
                                    }
                                    if(ia===content.length-1){//绘制最后一行
                                        pan.fillText(content.substring(lastSubStrIndex),0,initHeight-y);
                                        lineWidth=0;//回车
                                        initHeight+=textSize;//换行
                                    }
                                }
                                if (initHeight>binding.value.height){//行高超出时滚动.
                                    const spe = Math.ceil(textSize / 50);//下移速度
                                    y = (y+spe)%initHeight;
                                }
                            }, 20);
                        }
                        else {
                            let content="等待数据...";
                            let y = 0;
                            let etc = 0;
                            el.dataset.id = setInterval(()=>{
                                el.width = el.width;
                                el.height = el.height;
                                if (etc >= binding.value.media.etcData){
                                    etc = 0;
                                    axios.get('/cross/'+binding.value.media.url).then(response=>{
                                        const struct = binding.value.media.imageUrl.splice('.');
                                        let data = response.data;
                                        for (const index of struct){
                                            data = data[index];
                                        }
                                        content = data;
                                    });
                                }
                                etc = etc + 20;
                                const pan = el.getContext("2d");
                                const textSize = binding.value.media.etcData;
                                pan.font=textSize+"px Georgia";
                                let lineWidth = 0;
                                let initHeight= y + textSize;
                                let lastSubStrIndex = 0;
                                for(let i=0;i<content.length;i++){//循环绘制多行文本
                                    lineWidth+=pan.measureText(content[i]).width;
                                    if(lineWidth>binding.value.width){
                                        pan.fillText(content.substring(lastSubStrIndex,i),0,initHeight-y);
                                        lineWidth=0;//回车
                                        initHeight+=textSize;//换行
                                        lastSubStrIndex=i;
                                    }
                                    if(i===content.length-1){//绘制最后一行
                                        pan.fillText(content.substring(lastSubStrIndex,i+1),0,initHeight-y);
                                        lineWidth=0;//回车
                                        initHeight+=textSize;//换行
                                    }
                                }
                                if (initHeight>binding.value.height){//行高超出时滚动.
                                    const spe = Math.ceil(textSize / 50);//下移速度
                                    y = (y+spe)%initHeight;
                                }
                            }, 20);
                        }
                    }
                },
                unbind(el, binding){
                    clearInterval(el.dataset.id);
                },
            }
        }
    }
</script>

<style scoped>

</style>