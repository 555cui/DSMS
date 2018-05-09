<template>
    <div>
        <el-card :body-style="{ padding: '0px' }">
            <div slot="header" class="clearfix">
                <el-checkbox style="float: left; padding: 0" @change="onCheck"></el-checkbox>
                <el-button class="button" icon="el-icon-close" @click="onDelete"></el-button>
            </div>
            <div>
                <div v-if="media._type === 'row' || media._type === 'col'" class="text-box">
                    {{media.url}}
                </div>
                <div v-else-if="media._type === 'dynamic'" class="text-box">
                    path:{{media.url}}<br>
                    value:{{media.imageUrl}}<br>
                </div>
                <img :src="media.imageUrl" class="image" v-else>
            </div>
            <div style="padding: 14px;">
                <span>{{media.name}}</span>
                <div class="bottom clearfix">
                    <time class="time">{{ media.updateTime }}</time>
                    <el-button class="button" icon="el-icon-setting" @click="onEdit"></el-button>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "MediaCard",
        props: ['media'],
        methods: {
            onDelete(){
                this.$emit('action', this.media, 'delete');
            },
            onCheck(check){
                this.$emit('action', this.media, check);
            },
            onEdit(){
                this.$emit('action', this.media, 'edit');
            },
        },
    }
</script>

<style scoped>
    .time {
        font-size: 13px;
        color: #999;
    }

    .bottom {
        margin-top: 13px;
        line-height: 12px;
    }

    .button {
        padding: 0;
        float: right;
    }

    .image {
        width: 100%;
        display: block;
    }

    .text-box {
        overflow : hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 5;
        -webkit-box-orient: vertical;
    }

    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }

    .clearfix:after {
        clear: both
    }
</style>