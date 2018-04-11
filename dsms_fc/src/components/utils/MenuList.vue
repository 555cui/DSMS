<template>
  <div>
    <el-menu
      default-active="0-0"
      class="el-menu-vertical-demo"
      unique-opened="true">
      <el-submenu
        :index="''+index"
        v-for="(group, index) in menuData.data"
        :key="index">

        <template slot="title">
          <i class="el-icon-location"></i>
          <span>{{group.name}}</span>
        </template>

        <el-menu-item-group>

          <el-menu-item
            v-for="(item, dataIndex) in group.list"
            :key="index+'-'+dataIndex"
            :index="index+'-'+dataIndex">
            <div :id="item.id" :class="group.name">{{item.name}}</div>
          </el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
  </div>
</template>

<script>
  export default {
    name: "menu-list",
    props: ['menuData']
  }
</script>

<style scoped>

</style>
