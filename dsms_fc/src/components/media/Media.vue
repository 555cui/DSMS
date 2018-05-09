<template>
  <div>
    <new-media
            :videoList="mediaList.video"
            :imageList="mediaList.image"
            @OnNewMedia="onInsertMedia"></new-media>
      <edit-media
              :media="editMedia"
              :mediaList="mediaList"
              @onEdit="onEditMedia"></edit-media>
    <el-row>
      <div class="row">
        <el-input v-model="searchData">
          <el-button slot="append" icon="el-icon-search"></el-button>
        </el-input>
      </div>
    </el-row>
    <el-row>
      <div class="row">
        <el-button icon="el-icon-plus" @click="$store.state.media.show = true"></el-button>
        <el-button type="danger" icon="el-icon-delete" @click="onDeleteList"></el-button>
      </div>
    </el-row>
    <el-row>
      <div class="row">
        <el-tabs type="border-card" v-model="$store.state.media.action">
            <el-tab-pane :label="k" :name="k" v-for="(v,k) in mediaList" :key="k">
                <div class="media-box">
                    <div class="item" v-for="item in v" :key="item.id">
                        <media-card :media="item" @action="onCardAction"></media-card>
                    </div>
                </div>
            </el-tab-pane>
        </el-tabs>
      </div>
    </el-row>
  </div>
</template>

<script>
    import NewMedia from "./NewMedia";
    import MediaCard from "./MediaCard";
    import EditMedia from "./EditMedia";
    export default {
        components: {EditMedia, MediaCard, NewMedia},
        name: "media",
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
        },
        data(){
            return {
                searchData: '',
                mediaList: {
                    video: [],
                    image: [],
                    text: [],
                    group: [],
                },
                checkList:{
                    video: [],
                    image: [],
                    text: [],
                    group: [],
                },
                editMedia: {
                    id: '',
                    name: '',
                    width: '',
                    height: '',
                    type: '',
                    _type: '',
                    etcData: '',
                    group: [],
                    url: '',
                    imageUrl: '',
                },
            }
        },
        methods: {
            onInsertMedia(media){
                this.mediaList[media.type].push(media);
            },
            onEditMedia(media){
                console.info('save:'+media.id);
                const i = this.mediaList[media.type].findIndex(item=> item.id===media.id);
                this.mediaList[media.type][i].name=media.name;
                this.mediaList[media.type][i]._type=media._type;
                this.mediaList[media.type][i].group=media.group;
                this.mediaList[media.type][i].width=media.width;
                this.mediaList[media.type][i].height=media.height;
                this.mediaList[media.type][i].url=media.url;
                this.mediaList[media.type][i].imageUrl=media.imageUrl;
                this.mediaList[media.type][i].etcData=media.etcData;
            },
            onCardAction(media, action){
                if (action === 'delete'){
                    this.$confirm('此操作将永久删除该素材, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        const url = '/media/'+media.id;
                        this.$ajax.delete(url).then(response=>{
                            if (response.data.code === 0){
                                const i = this.mediaList[media.type].findIndex(item => item.id===media.id);
                                console.info(i);
                                if (i !== -1)
                                    this.mediaList[media.type].splice(i,1);
                                this.$message({
                                    type: 'success',
                                    message: '删除成功!'
                                });
                            } else if (response.data.code >=10 && response.data.code<20)
                                this.$store.state.user.code=response.data.code;
                        });
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消删除'
                        });
                    });
                }
                else if (action === 'edit'){
                    console.info('mediaCard:'+media.id);
                    this.editMedia=media;
                    this.$store.state.media.showEdit=true;
                }
                else {
                    if (action){
                        this.checkList[media.type].push(media);
                    } else{
                        const i = this.checkList[media.type].findIndex(item => item.id === media.id);
                        if (i !== -1)
                            this.checkList[media.type].splice(i,1);
                    }
                }
            },
            onDeleteList(){
                if (this.checkList[this.action].length>0){
                    this.$confirm('此操作将永久删除选中素材, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        const url = '/media/delete';
                        this.$ajax.post(url,this.checkList[this.action],{responseType: 'json'}).then(response=>{
                            if (response.data.code === 0){
                                for(let media of this.checkList[this.action]){
                                    const i = this.mediaList[this.action].findIndex(
                                        item => item.id === media.id);
                                    this.mediaList[this.action].splice(i, 1);
                                }
                                this.mediaList[this.action]=[];
                                this.$message({
                                    type: 'success',
                                    message: '删除成功!'
                                });
                            } else if (response.data.code >=10 && response.data.code<20)
                                this.$store.state.user.code=response.data.code;
                        });
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消删除'
                        });
                    });
                }
                else this.$message({type: 'info', message: '请选择'})
            }
        },
        computed: {
            action(){
                return this.$store.state.media.action;
            }
        }
    }
</script>

<style scoped>
  .row{
    margin: 5px 20px;
  }
    .media-box{
        min-height: 60%;
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
    }
    .item {
        width: 20%;
        margin: 5px;
    }
</style>
