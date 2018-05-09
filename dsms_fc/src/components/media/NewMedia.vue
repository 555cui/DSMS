<template>
  <div>
    <el-dialog :visible.sync="show" top="50px" title="新建媒体" :before-close="onCancel" @open="flag=true">
      <el-tabs v-model="$store.state.media.action" type="card" @tab-click="cleanAllForm">
        <el-tab-pane label="video" name="video">
          <el-upload
            accept="video/*"
            action=""
            :file-list="videoUpLoadList"
            :http-request="onUploadFiles"
            list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传mp4/ogg/webm文件，且不超过2GB
            </div>
          </el-upload>
        </el-tab-pane>
        <el-tab-pane label="image" name="image">
          <el-upload
            accept="image/*"
            action=""
            :file-list="imageUpLoadList"
            :http-request="onUploadFiles"
            list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png文件，且不超过500kb
            </div>
          </el-upload>
        </el-tab-pane>
        <el-tab-pane label="text" name="text">
          <el-form ref="text" label-width="120px" :model="media" :rules="rules">
            <el-form-item label="name" prop="name">
              <el-input v-model="media.name"></el-input>
            </el-form-item>
            <el-form-item label="width" prop="width">
              <el-input type="number" v-model.number="media.width"></el-input>
            </el-form-item>
            <el-form-item label="height" prop="height">
              <el-input type="number" v-model.number="media.height"></el-input>
            </el-form-item>
            <el-form-item label="type" prop="_type">
                <el-radio-group v-model="media._type" >
                    <el-radio label="static">静态</el-radio>
                    <el-radio label="dynamic">动态</el-radio>
                </el-radio-group>
            </el-form-item>
            <div v-if="media._type === 'static'">
                <el-form-item label="滚动方式" prop="_type">
                    <el-radio-group v-model="media.ctype">
                        <el-radio label="row">横向</el-radio>
                        <el-radio label="col">纵向</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="字体大小">
                    <el-input type="number" v-model.number="media.etcData"></el-input>
                </el-form-item>
              <el-form-item label="内容" prop="url">
                <el-input type="textarea" rows="5" v-model="media.url"></el-input>
              </el-form-item>
            </div>
            <div v-if="media._type === 'dynamic'">
              <el-form-item label="请求路径" prop="url">
                <el-input v-model="media.url" placeholder="https://example.org/data"></el-input>
              </el-form-item>
              <el-form-item label="返回数据格式" prop="url">
                <el-input v-model="media.imageUrl" placeholder="example: data.user.name"></el-input>
              </el-form-item>
              <el-form-item label="刷新时间(ms)">
                <el-input type="number" v-model.number="media.etcData"></el-input>
              </el-form-item>
            </div>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="group" name="group">
          <el-form ref="group" label-width="100px" :model="media" :rules="rules">
            <el-form-item label="name" prop="name">
              <el-input v-model="media.name"></el-input>
            </el-form-item>
            <el-form-item label="type" prop="_type">
              <el-radio v-model="media._type" @change="onMediaTypeChange" label="video">video</el-radio>
              <el-radio v-model="media._type" @change="onMediaTypeChange" label="image">image</el-radio>
            </el-form-item>
            <el-form-item label="播放间隔(ms)" prop="etcData">
              <el-input type="number" v-model.number="media.etcData"></el-input>
            </el-form-item>
            <el-form-item label="media group" prop="group">
              <el-transfer
                v-model="media.group"
                :titles="['media', 'group']"
                :props="{key:'id',label:'name'}"
                target-order="push"
                @change="onDataTransferChange"
                :data="mediaList">
              </el-transfer>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      <span slot="footer" class="dialog-footer">
        <el-button @click="onCancel">取 消</el-button>
        <el-button type="primary" @click="onNewMedia">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
    export default {
        name: "new-media",
        props: ['videoList', 'imageList'],
        data(){
            const nameRule = (rule, value, callback) => {
                this.$ajax.post('/media/name', {name: value}, {responseType: 'json'}).then(response => {
                    if (response.data.code >= 10 && response.data.code < 20)
                        this.$store.state.user.code=response.data.code;
                    else if (response.data.code !== 0)callback(new Error(response.data.msg));
                    else callback();
                }).catch(e=>{
                    callback(new Error('请求失败'));
                })
            };
            const groupRule = (rule, value, callback) => {
                if (value.length === 0)
                    callback(new Error("请添加素材"));
                else callback();
            };
            const numberRule = (rule, value, callback) => {
                if (value>0) callback();
                else callback(new Error("请输入大于零的数"));
            };

            return {
                flag: false,
                rules: {
                    name: [
                        { required: true, message: '请输入素材名称', trigger: 'blur' },
                        { validator: nameRule, trigger: 'blur' },
                    ],
                    width: [{ required: true, message: '请输入宽度', trigger: 'blur' },
                        { validator: numberRule, trigger: 'blur'}],
                    height: [{required: true, message: '请输入高度', trigger: 'blur'},
                        { validator: numberRule, trigger: 'blur'}],
                    _type: [{required: true, message: '请选择类型', trigger: 'blur'}],
                    url:[{required: true, message: '请输入内容', trigger: 'blur'}],
                    group: [{validator: groupRule, trigger:'blur'}],
                },
                media: {
                    name: '',
                    width: 0,
                    height: 0,
                    type: '',
                    _type: '',
                    ctype: '',
                    etcData: 0,
                    group: [],
                    url: '',
                    imageUrl: '',
                },
                mediaList: [],
                videoUpLoadList: [],
                imageUpLoadList: [],
            }
        },
        computed: {
            show(){
                return this.$store.state.media.show;
            },
            action(){
                return this.$store.state.media.action;
            }
        },
        methods: {
            onDataTransferChange(){
                console.info(this.media.group);
            },
            onUploadFiles(param){
                this.flag=false;
                const form = new FormData();
                const url = '/upload/'+this.$store.state.media.action;
                form.append('file', param.file);
                this.$ajax.post(url, form, {'Content-Type': 'multipart/form-data'}).then(
                    response => {
                        if (response.data.code === 0){
                            param.onSuccess();
                            this.$emit('OnNewMedia', response.data.data);
                        } else param.onError();
                        this.flag = true;
                    }
                ).catch(
                    error => {
                        param.onError();
                        this.flag = true;
                    }
                );
            },
            onNewMedia(){
                if (this.action === 'image' || this.action === 'video'){
                    if (this.flag)
                        this.onCancel();
                    else this.$message('请等待上传完成');
                }
                else {
                    this.$refs[this.action].validate(valid => {
                        if(valid){
                            let media = {
                                name: this.media.name,
                                type: this.action,
                                _type: this.media._type==='static'?this.media.ctype:this.media._type,
                                width: this.media.width,
                                height: this.media.height,
                                etcData: this.media.etcData,
                                group: this.media.group,
                                url: this.media.url,
                                imageUrl: this.media.imageUrl
                            };
                            if (media.type === 'group'){
                                const list = [];
                                for(let id of media.group){
                                    const item = this.mediaList.find(i => i.id===id);
                                    list.push(item);
                                }
                                media.group=list;
                            }
                            this.$ajax.post('/media/',media,{responseType:'json'}).then(
                                response => {
                                    if (response.data.code === 0){
                                        this.$emit('OnNewMedia', response.data.data);
                                        this.cleanAllForm();
                                        this.$store.state.media.show=false;
                                    }
                                    else if (response>=10&&response<20){
                                        this.$store.state.user.code=response.data.code;
                                    }
                                }
                            );
                        }
                    });
                    this.flag = true;
                }
            },
            onCancel(){
                if (this.flag){
                    this.cleanAllForm();
                    this.$store.state.media.show=false;
                } else {
                    this.$message.info('请等待上传完成');
                }
            },
            onMediaTypeChange(label){
                this.media.group = [];
                if(label==='image'){this.mediaList=this.imageList;}
                else if(label==='video'){this.mediaList=this.videoList;}
            },
            resetForm(formName) {this.$refs[formName].resetFields();},
            cleanAllForm(){
                this.resetForm('text');
                this.resetForm('group');
                this.mediaList=[];
                this.media.group=[];
                this.videoUpLoadList=[];
                this.imageUpLoadList=[];
            }
        }
    }
</script>

<style scoped>

</style>
