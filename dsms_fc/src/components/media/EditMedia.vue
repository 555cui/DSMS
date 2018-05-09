<template>
    <div>
        <el-dialog
                title="修改信息"
                :visible.sync="$store.state.media.showEdit"
                width="50%"
                :before-close="handleClose"
                @open="onOpen">
            <el-form ref="mediaF" :model="mediaForm" :rules="rules" label-width="100px">
                <el-form-item label="name" prop="name">
                    <el-input v-model="mediaForm.name"></el-input>
                </el-form-item>
                <div v-if="mediaForm.type === 'video' || mediaForm.type === 'image'">
                    <el-form-item label="width">
                        <el-input type="number" v-model.number="mediaForm.width" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="height">
                        <el-input type="number" v-model.number="mediaForm.height" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="type">
                        <el-input v-model="mediaForm._type" disabled></el-input>
                    </el-form-item>
                </div>
                <div v-else-if="mediaForm.type === 'group'">
                    <el-form-item label="width">
                        <el-input type="number" v-model.number="mediaForm.width" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="height">
                        <el-input type="number" v-model.number="mediaForm.height" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="type">
                        <el-input v-model="mediaForm._type" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="播放间隔" prop="etcData">
                        <el-input type="number" v-model.number="mediaForm.etcData"></el-input>
                    </el-form-item>
                    <el-form-item label="media" prop="group">
                        <el-transfer
                                v-model="mediaForm.group"
                                :titles="['media', 'group']"
                                :props="{key:'id',label:'name'}"
                                target-order="push"
                                @change="onDataTransferChange"
                                :data="list">
                        </el-transfer>
                    </el-form-item>
                </div>
                <div v-else>
                    <el-form-item label="width" prop="width">
                        <el-input type="number" v-model="mediaForm.width"></el-input>
                    </el-form-item>
                    <el-form-item label="height" prop="height">
                        <el-input type="number" v-model="mediaForm.height"></el-input>
                    </el-form-item>
                    <div v-if="mediaForm._type === 'dynamic'">
                        <el-form-item label="刷新速度" prop="etcData">
                            <el-input type="number" v-model.number="mediaForm.etcData"></el-input>
                        </el-form-item>
                        <el-form-item label="请求地址" prop="url">
                            <el-input v-model="mediaForm.url"></el-input>
                        </el-form-item>
                        <el-form-item label="数据结构" prop="imageUrl">
                            <el-input v-model="mediaForm.imageUrl"></el-input>
                        </el-form-item>
                    </div>
                    <div v-else>
                        <el-form-item label="字体大小" prop="etcData">
                            <el-input type="number" v-model.number="mediaForm.etcData"></el-input>
                        </el-form-item>
                        <el-form-item label="滚动方向">
                            <el-radio-group v-model="mediaForm._type">
                                <el-radio label="row">横向</el-radio>
                                <el-radio label="col">纵向</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item label="正文" prop="url">
                            <el-input type="textarea" rows="5" v-model="mediaForm.url"></el-input>
                        </el-form-item>
                    </div>
                </div>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="$store.state.media.showEdit = false">取 消</el-button>
                <el-button type="primary" @click="onUpdateMedia">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "EditMedia",
        props: ['media','mediaList'],
        methods: {
            onDataTransferChange(){
                let width = 0;
                let height = 0;
                for (const id of this.mediaForm.group){
                    const item = this.list.find(i => i.id===id);
                    if (item.width>width){
                        width = item.width;
                    }
                    if (item.height>height){
                        height = item.height;
                    }
                }
                this.mediaForm.width=width;
                this.mediaForm.height=height;
            },
            onUpdateMedia(){
                console.info('update:'+this.mediaForm.id);
                this.$refs['mediaF'].validate(valid => {
                    if (valid){
                        const url = '/media/'+this.mediaForm.id;
                        if (this.mediaForm.type==='group'){
                            console.info(this.mediaForm.group);
                            const group = [];
                            for (const id of this.mediaForm.group){
                                const item = this.list.find(i=> i.id===id);
                                group.push(item);
                            }
                            this.mediaForm.group = group;
                            this.mediaForm.imageUrl = group[0].imageUrl;
                        }
                        this.$ajax.put(url,this.mediaForm).then(response=>{
                            if (response.data.code>=10 &&response.data.code<20)
                                this.$store.state.user.code=response.data.code;
                            else if (response.data.code === 0){
                                this.mediaForm=response.data.data;
                                this.$emit('onEdit', this.mediaForm);
                                this.handleClose();
                            }
                            else this.$message.error('update error');
                        }).catch(e=>{
                            this.$message.error('服务器出错');
                        });
                    }
                });
            },
            handleClose(){
                this.$refs['mediaF'].resetFields();
                this.$store.state.media.showEdit = false;
            },
            onOpen(){
                this.mediaForm.group=[];
                for (const item of this.media.group)
                    this.mediaForm.group.push(item.id);
                this.mediaForm.id=this.media.id;
                this.mediaForm.name=this.media.name;
                this.mediaForm.type=this.media.type;
                this.mediaForm._type=this.media._type;
                this.mediaForm.width=this.media.width;
                this.mediaForm.height=this.media.height;
                this.mediaForm.etcData=this.media.etcData;
                this.mediaForm.url=this.media.url;
                this.mediaForm.imageUrl=this.media.imageUrl;
                this.list = this.mediaList[this.media._type];
                console.info('open:'+this.mediaForm.id);
            }
        },
        data(){
            const nameRule = (rule, value, callback) => {
                if (value === this.media.name)callback();
                else {
                    this.$ajax.post('/media/name', {name: value}, {responseType: 'json'}).then(response => {
                        if (response.data.code >= 10 && response.data.code < 20)
                            this.$store.state.user.code=response.data.code;
                        else if (response.data.code !== 0)callback(new Error(response.data.msg));
                        else callback();
                    }).catch(e=>{
                        callback(new Error('请求失败'));
                    });
                }
            };
            const groupRule = (rule, value, callback) => {
                if (value.length === 0)
                    callback(new Error("请添加素材"));
                else callback();
            };
            const numberRule = (rule, value, callback) => {
                if (value>0)
                    callback();
                else callback(new Error("请输入大于零的数"))
            };
            const etcRule = (rule, value, callback) => {
                if (value>0)
                    if (this.mediaForm._type === 'dynamic')
                        callback();
                    else {
                        if (value > this.mediaForm.height)
                            callback(new Error("字体大小不能超过高度"));
                        else callback();
                    }
                else callback(new Error("请输入大于零的数"));
            };
            return{
                list: [],
                mediaForm: {
                    id: '',
                    name: '',
                    type: '',
                    _type: '',
                    width: 0,
                    height: 0,
                    etcData: 0,
                    group: [],
                    url: '',
                    imageUrl: '',
                },
                rules: {
                    name: [
                        { required: true, message: '请输入素材名称', trigger: 'blur' },
                        { validator: nameRule, trigger: 'blur' },
                    ],
                    width: [
                        { required: true, message: '请输入宽度', trigger: 'blur'},
                        { validator: numberRule, trigger: 'blur'}
                    ],
                    height: [
                        { required: true, message: '请输入高度', trigger: 'blur'},
                        { validator: numberRule, trigger: 'blur'}
                    ],
                    _type: [{required: true, message: '请选择类型', trigger: 'blur'}],
                    url:[{required: true, message: '请输入内容', trigger: 'blur'}],
                    imageUrl:[{required: true, message: '请输入内容', trigger: 'blur'}],
                    group: [{validator: groupRule, trigger:'blur'}],
                    etcData: [
                        { required: true, message: '请输入数字', trigger: 'blur'},
                        { validator: etcRule, trigger: 'blur'}
                    ],
                },
            }
        }
    }
</script>

<style scoped>

</style>