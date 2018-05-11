<template>
  <div>
    <el-dialog :visible.sync="$store.state.template.show" title="新建模板">
      <el-form ref="form" :model="template" label-width="150px" :rules="rules">
        <el-form-item label="name" prop="name">
          <el-input v-model="template.name"></el-input>
        </el-form-item>
        <el-form-item label="group" prop="group">
          <el-select v-model="template.group" placeholder="group" value-key="id">
            <el-option
              v-for="item in group"
              :key="item.id"
              :label="item.name"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="width" prop="width">
          <el-input type="number" v-model.number="template.width"></el-input>
        </el-form-item>
        <el-form-item label="height" prop="height">
          <el-input type="number" v-model.number="template.height"></el-input>
        </el-form-item>
        <el-form-item label="background color" prop="backgroundColor">
          <el-color-picker v-model="template.backgroundColor"></el-color-picker>
        </el-form-item>
        <el-form-item>
          <el-button @click="onCancelNew">取 消</el-button>
          <el-button type="primary" @click="onTemplateEdit">新 建</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
    export default {
        name: "template-new",
        props: ['group'],
        data(){
            const nameRule = (rule, value, callback) => {
                this.$ajax.post('/DSMS_FE/template/name', {name: value}).then(response => {
                    if (response.data.code >= 10 && response.data.code < 20)
                        this.$store.state.user.code=response.data.code;
                    else if (response.data.code !== 0)callback(new Error(response.data.msg));
                    else callback();
                }).catch(e=>{
                    callback(new Error('请求失败'));
                })
            };
            const numberRule = (rule, value, callback) => {
                if (value<=0)
                    callback(new Error('请输入大于零的数字'));
                else callback();
            };
            return {
                rules: {
                    name: [
                        { required: true, message: '请输入模板名称', trigger: 'blur' },
                        { validator: nameRule, trigger: 'blur' },
                    ],
                    width: [{ required: true, message: '请输入宽度', trigger: 'blur' },
                        { validator: numberRule, trigger: 'blur'}],
                    height: [{required: true, message: '请输入高度', trigger: 'blur'},
                        { validator: numberRule, trigger: 'blur'}],
                    group: [{required: true, message: '请选择分组'}],
                    backgroundColor: [{required: true, message: '请选择背景颜色'}],
                },
                template: {
                    name: '',
                    group: {},
                    backgroundColor: '#808080',
                    width: 0,
                    height: 0,
                }
            }
        },
        methods: {
            onTemplateEdit(){
                this.$refs['form'].validate(valid => {
                    if (valid){
                        const url = '/DSMS_FE/template/';
                        this.$ajax.post(url, this.template).then(response=>{
                            if (response.data.code===0){
                                this.$emit('new', response.data.data);
                                this.resetForm('form');
                                this.$store.state.template.show=false;
                                this.$router.push('/template/'+response.data.data.id);
                            }
                            else if (response.data.code>=0&&response.data.code<20){
                                this.$store.state.user.code=response.data.code;
                            }
                            else {
                                this.$message.error('请求失败');
                            }
                        }).catch(e=>{
                            this.$message.error('服务器出错');
                        });
                    }
                });
            },
            onCancelNew(){
                this.resetForm('form');
                this.$store.state.template.show=false;
            },
            resetForm(formName){
                this.$refs[formName].resetFields();
            }
        }
  }
</script>

<style scoped>

</style>
