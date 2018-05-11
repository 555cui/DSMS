<template>
    <div>
        <el-dialog :visible.sync="$store.state.user.code !== 0" title="你已经登出" :before-close="onClose">
            <span>{{msg}}</span>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="onClose">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "logoutPage",
        computed: {
            show(){
                return this.$store.state.user.code !== 0;
            },
            msg(){
                if (this.$store.state.user.code === 10)return '你还没登录，请先登录';
                else if (this.$store.state.user.code === 11)return '登录信息已过期，请从新登陆';
                else if (this.$store.state.user.code === 12)return '你的账号已在其他地方登陆，如非本人操作，请修改密码';
            }
        },
        methods: {
            onClose(){
                this.$store.commit("logout");
                this.$store.state.user.code=0;
                this.$router.push('/first');
            }
        }
    }
</script>

<style scoped>

</style>