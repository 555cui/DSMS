<template>
  <div>
    <el-container>
      <el-header height="30px">
        <el-form ref="formTemplate" :inline="true" :model="template" :rules="rules">
          <el-form-item label="group">
            <el-select v-model="template.group" placeholder="group" value-key="id">
              <el-option
                v-for="item in group"
                :key="item.id"
                :label="item.name"
                :value="item">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="name" prop="name">
            <el-input v-model="template.name"></el-input>
          </el-form-item>
          <el-form-item label="width">
            <div style="width: 100px">
              <el-input type="number" v-model.number="template.width" disabled></el-input>
            </div>
          </el-form-item>
          <el-form-item label="height">
            <div style="width: 100px">
              <el-input type="number" v-model.number="template.height" disabled></el-input>
            </div>
          </el-form-item>
          <el-form-item label="background color">
            <el-color-picker v-model="template.backgroundColor"></el-color-picker>
          </el-form-item>
        </el-form>
      </el-header>
      <el-main>
        <el-container>
          <el-aside width="210px">
            <div id="menu-list">
              <el-menu :unique-opened="true">
                <el-submenu index="0">
                  <template slot="title">
                    <i class="el-icon-document"></i>
                    <span>text</span>
                  </template>
                  <el-menu-item-group>
                    <el-menu-item
                      v-for="(item, index) in mediaList.text"
                      :key="item.id"
                      :index="'0-'+index">
                      <div :id="item.id" v-drag="item">
                        {{item.name}}
                      </div>
                    </el-menu-item>
                  </el-menu-item-group>
                </el-submenu>

                  <el-submenu index="1">
                      <template slot="title">
                          <i class="el-icon-picture"></i>
                          <span>image</span>
                      </template>
                      <el-menu-item-group>
                          <el-menu-item
                                  v-for="(item, index) in mediaList.image"
                                  :key="item.id"
                                  :index="'1-'+index">
                              <div :id="item.id" v-drag="item">
                                  {{item.name}}
                              </div>
                          </el-menu-item>
                      </el-menu-item-group>
                  </el-submenu>

                  <el-submenu index="2">
                      <template slot="title">
                          <i class="el-icon-caret-right"></i>
                          <span>video</span>
                      </template>
                      <el-menu-item-group>
                          <el-menu-item
                                  v-for="(item, index) in mediaList.video"
                                  :key="item.id"
                                  :index="'2-'+index">
                              <div :id="item.id" v-drag="item">
                                  {{item.name}}
                              </div>
                          </el-menu-item>
                      </el-menu-item-group>
                  </el-submenu>

                  <el-submenu index="3">
                      <template slot="title">
                          <i class="el-icon-menu"></i>
                          <span>group</span>
                      </template>
                      <el-menu-item-group>
                          <el-menu-item
                                  v-for="(item, index) in mediaList.group"
                                  :key="item.id"
                                  :index="'3-'+index">
                              <div :id="item.id" v-drag="item">
                                  {{item.name}}
                              </div>
                          </el-menu-item>
                      </el-menu-item-group>
                  </el-submenu>
              </el-menu>
            </div>
          </el-aside>
          <el-main style="padding: 0;">
            <el-container>
              <el-main style="padding: 0; min-height: 405px">
                <div :style="padStyle" id="pad" v-drop="template">
                  <div class="item" v-move="item" data-id=""
                       v-for="item in template.element" @click="onFouces(item)"
                       :style="toStyle(item)">
                      <element-card :element="item" :base="base" @remove="removeElement"></element-card>
                  </div>
                </div>
              </el-main>
              <el-footer style="padding: 10px 20px">
                <el-button type="primary" @click="onSaveTemplate">保存</el-button>
                <el-button type="warning" @click="onReturn">返回</el-button>
              </el-footer>
            </el-container>
          </el-main>
          <el-aside width="200px">
            <div>
                <el-form :model="selectElement">
                    <el-form-item label="media" size="mini">
                        <el-input disabled v-model="selectElement.media.name"></el-input>
                    </el-form-item>
                    <el-form-item label="width" size="mini">
                        <el-input v-model="selectElement.width"></el-input>
                    </el-form-item>
                    <el-form-item label="height" size="mini">
                        <el-input v-model="selectElement.height"></el-input>
                    </el-form-item>
                    <el-form-item label="left" size="mini">
                        <el-input v-model="selectElement.left"></el-input>
                    </el-form-item>
                    <el-form-item label="top" size="mini">
                        <el-input v-model="selectElement.top"></el-input>
                    </el-form-item>
                    <el-form-item label="zIndex" size="mini">
                        <el-input v-model="selectElement.zIndex"></el-input>
                    </el-form-item>
                </el-form>
            </div>
          </el-aside>
        </el-container>
      </el-main>
    </el-container>
  </div>
</template>

<script>
    import ElementCard from "./ElementCard";
    import UUID from 'es6-uuid';
    export default {
        name: "template-edit",
        components: {ElementCard},
        created(){
            this.$ajax.get('/media/').then(response => {
                if (response.data.code === 0){
                    this.mediaList.video = response.data.data.filter(item => item.type === 'video');
                    this.mediaList.image = response.data.data.filter(item => item.type === 'image');
                    this.mediaList.text = response.data.data.filter(item => item.type === 'text');
                    this.mediaList.group = response.data.data.filter(item => item.type === 'group');
                }
                else if (response.data.code>=10&&response.data.code<20)
                    this.$store.state.user.code = response.data.code;
            });
            const url = '/template/'+this.$route.params.id;
            this.$ajax.get(url).then(response => {
                if (response.data.code === 0){
                    this.template = response.data.data;
                    this.templateOld = {
                        name: response.data.data.name,
                    }
                }
                else if (response.data.code>=10&&response.data.code<20)
                    this.$store.state.user.code = response.data.code;
            });
            this.$ajax.get("/templateGroup/").then(response=>{
                if (response.data.code === 0){
                    this.group = response.data.data;
                }
                else if (response.data.code >=10 && response.data.code <20){
                    this.$store.state.user.code = response.data.code;
                }
                else {
                    this.$message.error('获取数据失败');
                }
            }).catch(e=>{
                this.$message.error('服务器出错');
            });
        },
        data(){
            const nameRule = (rule, value, callback) => {
                console.info(value);
                console.info(this.templateOld.name);
                if (value !== this.templateOld.name){
                    this.$ajax.post('/template/name', {name: value}).then(response => {
                        if (response.data.code >= 10 && response.data.code < 20)
                            this.$store.state.user.code=response.data.code;
                        else if (response.data.code !== 0)callback(new Error(response.data.msg));
                        else callback();
                    }).catch(e=>{
                        callback(new Error('请求失败'));
                    })
                }
                else callback();
            };
            return {
                templateOld: {},
                template: {
                    id: '',
                    name: '',
                    group: {},
                    width: 0,
                    height: 0,
                    backgroundColor: '',
                    element: [],
                },
                rules: {
                    name: [{ required: true, message: '请输入模板名称', trigger: 'blur' },
                        { validator: nameRule, trigger: 'blur' },]
                },
                selectElement: {
                    id: '',
                    media: {name: ''},
                    width: 0,
                    height: 0,
                    top: 0,
                    left: 0,
                    zIndex: 0,
                },
                lock: {
                    width: false,
                    height: false,
                },
                mediaList: {
                    video: [],
                    image: [],
                    text: [],
                    group: [],
                },
                group: [],
            }
        },
        computed: {
            base(){
                const width = this.template.width;
                const height = this.template.height;
                let baseHeight = 0;
                if (height !== 0)baseHeight = 400 / height;
                const showWidth = width * baseHeight;
                if (showWidth <= 850)return baseHeight;
                let baseWidth = 0;
                if (width !== 0)baseWidth = 850 / width;
                return baseWidth;
            },
            padStyle(){
                return {
                    height: Math.round(this.template.height*this.base)+'px',
                    width: Math.round(this.template.width*this.base)+'px',
                    backgroundColor: this.template.backgroundColor
                };
            },
            swidth(){
                return this.selectElement.width;
            },
            sheight(){
                return this.selectElement.height;
            },
        },
        watch: {
            swidth(newVal, oldVal){
                if (this.lock.width){
                    if (this.selectElement.media.type!=='text'){
                        const base = this.selectElement.media.width / this.selectElement.media.height;
                        this.selectElement.height = Math.round(newVal / base);
                        this.lock.height=false;
                    }
                }
                this.lock.width=true;
            },
            sheight(newVal, oldVal) {
                if (this.lock.height){
                    if (this.selectElement.media.type!=='text'){
                        const base = this.selectElement.media.width / this.selectElement.media.height;
                        this.selectElement.width = Math.round(newVal * base);
                        this.lock.width = false;
                    }
                }
                this.lock.height = true;
            }
        },

        methods: {
            onFouces(element){
                if (this.selectElement.width!==element.width)
                    this.lock.width = false;
                if (this.selectElement.height!==element.height)
                    this.lock.height=false;
                this.selectElement = element;
            },
            onReturn(){
                window.history.go(-1);
            },
            onSaveTemplate(){
                this.$refs['formTemplate'].validate(valid => {
                    if (valid) {
                        const url = '/template/'+this.$route.params.id;
                        this.$ajax.put(url, this.template).then(response=>{
                            if (response.data.code === 0){
                                this.template = response.data.data;
                                this.$message.success('保存成功!');
                            }
                            else if (response.data.code>=10&&response.data.code<20)
                                this.$store.state.user.code = response.data.code;
                            else this.$message.error('保存失败!');
                        }).catch(e=>{
                            this.$message.error('服务器出错!');
                        });
                    }
                });
            },
            removeElement(element){
                const i = this.template.element.findIndex(item => item.id===element.id);
                this.template.element.splice(i,1);
            },
            toStyle(item){
                return {
                    width: Math.round(item.width*this.base) + 'px',
                    height: Math.round(item.height*this.base) + 'px',
                    left : Math.round(item.left*this.base) + 'px',
                    top : Math.round(item.top*this.base) + 'px',
                    zIndex: item.zIndex
                };
            }
        },
        directives: {
            drag: (el, binding) => {
                el.ondragstart = ev => {
                    ev.dataTransfer.setData('id', binding.value.id);
                    ev.dataTransfer.setData('name', binding.value.name);
                    ev.dataTransfer.setData('width', binding.value.width);
                    ev.dataTransfer.setData('height', binding.value.height);
                    ev.dataTransfer.setData('type', binding.value.type);
                    ev.dataTransfer.setData('_type', binding.value._type);
                    ev.dataTransfer.setData('imageUrl', binding.value.imageUrl);
                    ev.dataTransfer.setData('url1', binding.value.url);
                    ev.dataTransfer.setData('etcData', binding.value.etcData);
                };
                el.draggable = true;
            },
            drop: (el, binding) => {
                el.ondrop = ev => {
                    ev.preventDefault();

                    const padWidth = binding.value.width;
                    const padHeight = binding.value.height;

                    const width = parseInt(ev.dataTransfer.getData('width'));
                    const height = parseInt(ev.dataTransfer.getData('height'));

                    const showPadWidth = el.clientWidth;
                    const base = showPadWidth / padWidth;

                    const showLeft = ev.clientX - el.offsetLeft;
                    const showTop = ev.clientY - el.offsetTop;

                    let left = Math.round(showLeft / base);
                    let top = Math.round(showTop / base);

                    if (left > padWidth - width) left = padWidth - width;
                    if (top > padHeight - height) top = padHeight - height;
                    if (left < 0) left = 0;
                    if (top < 0) top = 0;

                    const divItem = {
                        id: UUID(32),
                        left: left,
                        top: top,
                        height: height,
                        width: width,
                        zIndex: 0,
                        media: {
                            id: ev.dataTransfer.getData('id'),
                            name: ev.dataTransfer.getData('name'),
                            type: ev.dataTransfer.getData('type'),
                            _type: ev.dataTransfer.getData('_type'),
                            imageUrl: ev.dataTransfer.getData('imageUrl'),
                            url: ev.dataTransfer.getData('url1'),
                            height: height,
                            width: width,
                            etcData: ev.dataTransfer.getData('etcData'),
                        },
                    };
                    console.info(divItem);
                    binding.value.element.push(divItem);
                };
                el.ondragover = ev => {
                    ev.preventDefault();
                };
            },
            move: (el, binding) => {
                el.onmousedown = ev => {
                    const maxLeft = el.parentElement.clientWidth - el.clientWidth;
                    const maxTop = el.parentElement.clientHeight - el.clientHeight;
                    const startLeft = ev.clientX - el.offsetLeft;
                    const startTop = ev.clientY - el.offsetTop;

                    document.onmousemove = ev => {
                        let left = ev.clientX - startLeft;
                        let top = ev.clientY - startTop;

                        if (left > maxLeft) left = maxLeft;
                        if (left < 0) left = 0;

                        if (top > maxTop) top = maxTop;
                        if (top < 0) top = 0;

                        const boxWidth = el.clientWidth;
                        const width = binding.value.width;
                        const base = boxWidth / width;
                        binding.value.left = Math.round(left / base);
                        binding.value.top = Math.round(top / base);
                    };

                    document.onmouseup = ev => {
                        document.onmousemove = null;
                        document.onmouseup = null;
                    };
                };
            },
        }
    }
</script>

<style scoped>
    #pad {
        position: relative;
    }
    .item {
        position: absolute;
    }
</style>
