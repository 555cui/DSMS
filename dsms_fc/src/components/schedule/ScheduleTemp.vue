<template>
    <div>
        <el-row type="flex">
                <el-button icon="el-icon-plus" @click="onNewTemplate"></el-button>
                <el-button type="danger" icon="el-icon-delete" @click="onDeleteTemplate"></el-button>
        </el-row>
        <el-row>
            <el-table :data="scheduleList">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="createTime"
                        label="创建时间"
                        sortable>
                </el-table-column>
                <el-table-column
                        prop="date"
                        label="日期"
                        sortable>
                </el-table-column>
                <el-table-column
                        prop="startTime"
                        label="开始时间">
                </el-table-column>
                <el-table-column prop="endTime"
                                 label="结束时间">
                </el-table-column>
                <el-table-column label="设备" prop="device">
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "ScheduleTemp",
        data(){
            return {
                scheduleList: [],
            }
        }
    }
</script>

<style scoped>

</style>