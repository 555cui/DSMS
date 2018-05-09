<template>
    <el-table :data="programs" stripe style="width: 100%" @row-dblclick="handleRowDblClick">
        <el-table-column prop="time" label="时间" width="115"></el-table-column>
        <el-table-column prop="name" label="模板" width="115"></el-table-column>
    </el-table>
</template>

<script>
    export default {
        name: "program-list",
        props: ['programs'],
        methods: {
            handleRowDblClick(row){
                this.$emit('dbClick', row.schedule);
            }
        }
    }
</script>

<style scoped>

</style>
