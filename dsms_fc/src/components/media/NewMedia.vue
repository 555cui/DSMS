<template>
  <div>
    <el-dialog :visible.sync="$store.state.show.mediaNew" top="50px" title="new media">
      <el-tabs v-model="tab" type="card" @tab-click="cleanAllForm">
        <el-tab-pane label="video" name="video">
          <el-upload
            class="upload-demo"
            action="/upload/video"
            accept="video/*"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :file-list="videoUpLoadList"
            list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传mp4/ogg/webm文件，且不超过2GB
            </div>
          </el-upload>
        </el-tab-pane>
        <el-tab-pane label="image" name="image">
          <el-upload
            class="upload-demo"
            action="/upload/image"
            accept="image/*"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :file-list="imageUpLoadList"
            list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png文件，且不超过500kb
            </div>
          </el-upload>
        </el-tab-pane>
        <el-tab-pane label="text" name="text">
          <el-form ref="TextForm" label-width="100px" :model="media">
            <el-form-item label="name">
              <el-input v-model="media.name"></el-input>
            </el-form-item>
            <el-form-item label="width">
              <el-input type="number" v-model.number="media.width"></el-input>
            </el-form-item>
            <el-form-item label="size">
              <el-input type="number" v-model.number="media.height"></el-input>
            </el-form-item>
            <el-form-item label="type">
              <div>
                <el-radio v-model="media.type" label="static">static</el-radio>
                <el-radio v-model="media.type" label="dynamic">dynamic</el-radio>
              </div>
            </el-form-item>
            <el-form-item label="content" v-if="media.type === 'static'">
              <el-input type="textarea" rows="5" v-model="media.content"></el-input>
            </el-form-item>
            <div v-if="media.type === 'dynamic'">
              <el-form-item label="path">
                <el-input v-model="media.path"></el-input>
              </el-form-item>
              <el-form-item label="value">
                <el-input v-model="media.value"></el-input>
              </el-form-item>
              <el-form-item label="refresh time(ms)">
                <el-input type="number" v-model.number="media.refresh"></el-input>
              </el-form-item>
            </div>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="group" name="group">
          <el-form ref="GroupForm" label-width="100px" :model="media">
            <el-form-item label="name">
              <el-input v-model="media.name"></el-input>
            </el-form-item>
            <el-form-item label="type">
              <el-radio v-model="media.type" @change="onMediaTypeChange" label="video">video</el-radio>
              <el-radio v-model="media.type" @change="onMediaTypeChange" label="image">image</el-radio>
            </el-form-item>
            <el-form-item label="refresh(ms)">
              <el-input type="number" v-model.number="media.refresh"></el-input>
            </el-form-item>
            <el-form-item label="media group">
              <el-transfer
                v-model="media.group"
                :titles="['media', 'group']"
                target-order="push"
                :data="mediaList">
              </el-transfer>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      <span slot="footer" class="dialog-footer">
        <el-button @click="onCancel">取 消</el-button>
        <el-button type="primary" @click="onNewMedia">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "new-media",
    props: ['tab'],
    data(){
      return {
        media: {
          id: '',
          name: '',
          width: '',
          height: '',
          type: '',
          refresh: '',
          value: '',
          group: [],
          path: '',
        },
        mediaList: [],
        imageList: [
          {
            key: '00000000-00000000-0000-0000-00000000',
            label: 'picture1',
            width: 800,
            height: 600,
          },
          {
            key: '00000000-00000000-0000-0000-00000001',
            label: 'picture2',
            width: 800,
            height: 600,
          },
          {
            key: '00000000-00000000-0000-0000-00000002',
            label: 'picture3',
            width: 800,
            height: 600,
          },
          {
            key: '00000000-00000000-0000-0000-00000003',
            label: 'picture4',
            width: 800,
            height: 600,
          },
        ],
        videoList: [
          {
            key: '00000000-00000000-0000-0000-00000001',
            label: 'video1',
            width: 800,
            height: 600,
          },
          {
            key: '00000000-00000000-0000-0000-00000002',
            label: 'video2',
            width: 800,
            height: 600,
          },
          {
            key: '00000000-00000000-0000-0000-00000003',
            label: 'video3',
            width: 800,
            height: 600,
          },
          {
            key: '00000000-00000000-0000-0000-00000004',
            label: 'video4',
            width: 800,
            height: 600,
          },
          {
            key: '00000000-00000000-0000-0000-00000005',
            label: 'video5',
            width: 800,
            height: 600,
          },
        ],
        videoUpLoadList: [],
        imageUpLoadList: [],
      }
    },
    methods: {
      onNewMedia(){
        this.cleanAllForm();
        this.$store.state.show.mediaNew=false;
      },
      onCancel(){
        this.cleanAllForm();
        this.$store.state.show.mediaNew=false;
      },
      onMediaTypeChange(label){
        this.media.group = [];
        if(label==='image'){
          this.mediaList=this.imageList;
        }
        else if(label==='video'){
          this.mediaList=this.videoList;
        }
      },
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      cleanAllForm(){
        this.resetForm('TextForm');
        this.resetForm('GroupForm');
      }
    }
  }
</script>

<style scoped>

</style>
