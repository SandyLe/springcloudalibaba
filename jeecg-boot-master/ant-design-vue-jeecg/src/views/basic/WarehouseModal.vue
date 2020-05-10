<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    wrapClassName="ant-modal-cust-warp"
    style="top:5%;height: 65%;overflow-y: hidden">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="名称">
              <a-input placeholder="请输入名称" v-decorator="[ 'name', validatorRules.name]" />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="代码">
              <a-input placeholder="请输入代码" v-decorator="[ 'code', validatorRules.code]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="所属门店">
              <a-select v-decorator="['belongsToId', {}]" placeholder="请选择门店">
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in shopList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.departName || item.departNameEn ">
                      {{ item.departName || item.departNameEn }}
                    </span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="负责人">
              <a-select v-decorator="['principalId', {}]" placeholder="请选择负责人">
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in userList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.realname || item.username ">
                      {{ item.realname || item.username }}
                    </span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-item
              :labelCol="hlabelCol"
              :wrapperCol="hwrapperCol"
              label="备注"
              label-width="4">
              <a-input placeholder="请输入备注" v-decorator="[ 'content', validatorRules.content]" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import pick from 'lodash.pick'
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import {addWarehouse,editWarehouse,duplicateCheck,loadShopData, getAllUser } from '@/api/api'
  export default {
    name: "MaterialUnitModal",
    data() {
      return {
        title: "操作",
        visible: false,
        roleDisabled: false,
        confirmLoading: false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        hlabelCol: {
          xs: { span: 24 },
          sm: { span: 4 },
        },
        hwrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        model: {},
        form: this.$form.createForm(this),
        validatorRules: {
          name: {
            rules: [
              { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' },
              {required: true, message: '名称不能为空!'}
            ]
          }
        },
        shopList: [],
        userList: []
      }
    },
    components: {AFormItem},
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;

        loadShopData({orgType : '1'}).then((res) => {
          if (res.success) {
            this.shopList = res.result;
          }
        })
        getAllUser().then((res) => {
          if (res.success) {
            this.userList = res.result;
          }
        })

        //编辑页面禁止修改角色编码
        if(this.model.id){
          this.roleDisabled = true;
        }else{
          this.roleDisabled = false;
        }

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name', 'code','content','belongsToId','principalId'))
        });
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let formData = Object.assign(this.model, values);
            let obj;
            console.log(formData)
            if(!this.model.id){
              obj=addWarehouse(formData);
            }else{
              obj=editWarehouse(formData);
            }
            obj.then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
        })
      },
      handleCancel () {
        this.close()
      }
    },
    mounted() {
    }
  }
</script>

<style scoped>

</style>