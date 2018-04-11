<template>
  <div>
    <new-media :tab="selectTab"></new-media>
    <el-row>
      <div class="row">
        <el-input v-model="searchData">
          <el-button slot="append" icon="el-icon-search"></el-button>
        </el-input>
      </div>
    </el-row>
    <el-row>
      <div class="row">
        <el-button icon="el-icon-plus" @click="onNewMedia"></el-button>
        <el-button type="danger" icon="el-icon-delete"></el-button>
      </div>
    </el-row>
    <el-row>
      <div class="row">
        <el-tabs type="border-card" v-model="selectTab">
          <el-tab-pane label="video" name="video">video</el-tab-pane>
          <el-tab-pane label="image" name="image">image</el-tab-pane>
          <el-tab-pane label="text" name="text">text</el-tab-pane>
          <el-tab-pane label="group" name="group">group</el-tab-pane>
        </el-tabs>
      </div>
    </el-row>
  </div>
</template>

<script>
  import NewMedia from "./NewMedia";

  export default {
    components: {NewMedia},
    name: "media",
    data(){
      return {
        selectTab: 'video',
        searchData: '',
      }
    },
    methods: {
      onNewMedia(){
        this.$store.state.show.mediaNew=true;
      }
    }
  }
</script>

<style scoped>
  .row{
    margin: 5px 20px;
  }
</style>
