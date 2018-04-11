<template>
  <div>
    <el-container>
      <el-header height="30px">
        <el-form ref="formTemplate" :inline="true" :model="template">
          <el-form-item label="group">
            <el-select v-model="template.group" placeholder="group">
              <el-option
                v-for="item in templateGroup"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="name">
            <el-input v-model="template.name"></el-input>
          </el-form-item>
          <el-form-item label="width">
            <div style="width: 100px">
              <el-input type="number" v-model.number="template.width" disabled="true"></el-input>
            </div>
          </el-form-item>
          <el-form-item label="height">
            <div style="width: 100px">
              <el-input type="number" v-model.number="template.height" disabled="true"></el-input>
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
                <el-submenu v-for="(media, mediaIndex) in medias"
                            :index="''+mediaIndex"
                            :key="media.id">
                  <template slot="title">
                    <i :class="media.icon"></i>
                    <span>{{media.name}}</span>
                  </template>
                  <el-menu-item-group>
                    <el-menu-item
                      v-for="(item, index) in media.list"
                      :key="item.id"
                      :index="mediaIndex+'-'+index">
                      <div :id="item.id" v-drag="item">
                        {{item.name}}
                      </div>
                    </el-menu-item>
                  </el-menu-item-group>
                </el-submenu>
              </el-menu>
            </div>
          </el-aside>
          <el-main>
            <el-container>
              <el-main>
                <div :style="padStyle()" id="padding" v-drop="template">
                  <div class="item" v-move
                       v-for="item in template.media"
                       :style="item.style">
                  </div>
                </div>
              </el-main>
              <el-footer>
                <el-button type="primary" @click="onSaveTemplate">save</el-button>
                <el-button type="warning" @click="onReturn">return</el-button>
              </el-footer>
            </el-container>
          </el-main>
          <el-aside width="100px">
            <div>

            </div>
          </el-aside>
        </el-container>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  export default {
    name: "template-edit",
    data(){
      return {
        templateGroup: [
          {id: 'default', name: 'default'},
          {id: '00000000-00000000-0000-0000-00000000', name: 'group1'}
        ],
        template: {
          id: '',
          name: '',
          group: '',
          width: '',
          height: '',
          media: [],
        },
        selectMedia: {
          id: '',
          name: '',
          type: '',
          style: '',
          width: '',
          height: '',
          top: '',
          left: '',
          zIndex: '',
        },
        medias: [
          {
            id: 'text',
            name: 'text',
            icon: 'el-icon-document',
            list: [
              {
                id: '1',
                name: 'text1',
                type: 'text',
                width: 200,
                height: 100,
                top: '',
                left: '',
                zIndex: 0,
              },
            ]
          },
          {
            id: 'image',
            name: 'image',
            icon: 'el-icon-picture',
            list: [
              {
                id: '1',
                name: 'image1',
                type: 'image',
                width: 400,
                height: 300,
                top: '',
                left: '',
                zIndex: 0,
              },
            ],
          },
          {
            id: 'video',
            name: 'video',
            icon: 'el-icon-caret-right',
            list: [
              {
                id: '1',
                name: 'video1',
                type: 'video',
                width: 800,
                height: 600,
                top: '',
                left: '',
                zIndex: 0,
              },
            ],
          },
          {
            id: 'group',
            name: 'group',
            icon: 'el-icon-menu',
            list: [
              {
                id: '1',
                name: 'group1',
                type: 'group',
                width: 800,
                height: 600,
                top: '',
                left: '',
                zIndex: 0,
              },
            ]
          },
        ],
      }
    },
    methods: {
      padStyle(){
        let base = this.template.height;
        if(base!== 0)base = this.template.width / this.template.height;
        let height = 400;
        let width = height * base;
        if (width > 850){
          width = 850;
          height = width / base;
        }
        const backgroundColor = this.template.backgroundColor;
        let style = '';
        style += 'height:' + Math.round(height) + 'px;';
        style += 'width:' + Math.round(width) + 'px;';
        style += 'background-color:' + backgroundColor;
        return style;
      },
      onReturn(){
        window.history.go(-1);
      },
      onSaveTemplate(){

      },
    },
    directives: {
      drag: (el, binding) => {
        el.ondragstart = ev => {
          ev.dataTransfer.setData('id', binding.value.id);
          ev.dataTransfer.setData('name', binding.value.name);
          ev.dataTransfer.setData('width', binding.value.width);
          ev.dataTransfer.setData('height', binding.value.height);
          ev.dataTransfer.setData('type', binding.value.type);
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


          if(width > padWidth || height > padHeight){
            alert('media was too big to input');
          }
          else {
            const showPadWidth = el.clientWidth;
            const showPadHeight = el.clientHeight;

            const base = showPadWidth / padWidth;

            const showWidth = width * base;
            const showHeight = height * base;

            let showLeft = ev.clientX - el.offsetLeft;
            let showTop = ev.clientY - el.offsetTop;

            if(showLeft > showPadWidth - showWidth)showLeft = showPadWidth - showWidth;
            if(showTop > showPadHeight - showHeight)showTop = showPadHeight - showHeight;

            const left = Math.round(showLeft / base);
            const top = Math.round(showTop / base);

            let style = '';
            style += 'left:' + Math.round(showLeft) + 'px;';
            style += 'top:' + Math.round(showTop) + 'px;';
            style += 'width:' + Math.round(showWidth) + 'px;';
            style += 'height:' + Math.round(showHeight) + 'px;';

            const divItem = {
              id: ev.dataTransfer.getData('id'),
              name: ev.dataTransfer.getData('name'),
              type: ev.dataTransfer.getData('type'),
              left: left,
              top: top,
              height: height,
              width: width,
              style: style,
            };

            binding.value.media.push(divItem);
          }
        };
        el.ondragover = ev => {
          ev.preventDefault();
        };
      } ,
      move: el => {
        el.onmousedown = ev => {
          const maxLeft = el.parentElement.clientWidth - el.clientWidth;
          const maxTop = el.parentElement.clientHeight - el.clientHeight;
          const startLeft = ev.clientX - el.offsetLeft;
          const startTop = ev.clientY - el.offsetTop;

          document.onmousemove = ev => {
            let left = ev.clientX - startLeft;
            let top = ev.clientY - startTop;

            if(left > maxLeft)left = maxLeft;
            if(left < 0)left = 0;

            if(top > maxTop)top = maxTop;
            if(top < 0)top = 0;

            el.style.left = left + 'px';
            el.style.top = top + 'px';
          };

          document.onmouseup = ev => {
            document.onmousemove = null;
            document.onmouseup = null;
          };
        }
      },
    }
  }
</script>

<style scoped>
  #padding {
    position: relative;
  }
  .item {
    position: absolute;
    background-color: blue;
  }
</style>
