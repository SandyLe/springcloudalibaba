<template>
  <a-modal
    :title="title"
    width="80%"
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
            <a-form-item label="原单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'sourceCode', validatorRules.sourceCode]" placeholder="请输入原单编号" />
              <a-input v-decorator="[ 'sourceId', {}]" placeholder="原单ID" type="hidden" />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="代码">
              <a-input placeholder="后台自动生成单号" :readOnly="true" v-decorator="[ 'code', validatorRules.code]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item label="客户" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="['customerId', validatorRules.customerId]" placeholder="请选择客户"  showSearch
                        optionFilterProp="children"
                        notFoundContent="无法找到，输入名称、编号、手机号回车搜索" @keyup.enter.native="searchCustomer"
                        :disabled="unEditable" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in customerList" :key="key" :value="item.id">
                  {{ item.info }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="单据时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'billDate', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="维修时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'operateDate', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="完工时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'finishedDate', {}]"/>
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
  import JDate from '@/components/jeecg/JDate'
  import moment from 'moment'
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import {addRepair,editRepair,duplicateCheck, searchCustomer } from '@/api/api'
  export default {
    name: "RepairModal",
    data() {
      return {
        dateFormat:"YYYY-MM-DD HH:mm:ss",
        title: "操作",
        visible: false,
        roleDisabled: false,
        confirmLoading: false,
        customerList: [],
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
          sourceCode: {
            rules: [
              {required: true, message: '请输入原单编号!'}
            ]
          },
          customerId: {
            rules: [
              {required: true, message: '请选择客户!'}
            ]
          }
        }
      }
    },
    components: {AFormItem},
    created () {
    },
    methods: {
      moment,
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;

        //编辑页面禁止修改角色编码
        if(this.model.id){
          this.roleDisabled = true;
        }else{
          this.roleDisabled = false;
        }

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name', 'sourceCode', 'sourceId', 'customerId', 'code','content'))
          this.form.setFieldsValue({billDate: this.model.billDate ? moment(this.model.billDate) : null})
          this.form.setFieldsValue({finishedDate: this.model.finishedDate ? moment(this.model.finishedDate) : null})
          this.form.setFieldsValue({operateDate: this.model.operateDate ? moment(this.model.operateDate) : null})
        });
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      searchCustomer (e) {
        searchCustomer({"keyword":e.target.valueOf().value}).then((res) => {
          if (res.success) {
            this.customerList = res.result;
          }
        })
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            if(!values.billDate){
              values.billDate = '';
            }else{
              values.billDate = values.billDate.format(this.dateFormat);
            }
            if(!values.operateDate){
              values.operateDate = '';
            }else{
              values.operateDate = values.operateDate.format(this.dateFormat);
            }
            if(!values.finishedDate){
              values.finishedDate = '';
            }else{
              values.finishedDate = values.finishedDate.format(this.dateFormat);
            }
            let formData = Object.assign(this.model, values);
            let obj;
            console.log(formData)
            if(!this.model.id){
              obj=addRepair(formData);
            }else{
              obj=editRepair(formData);
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
      searchCustomer({}).then((res) => {
        if (res.success) {
          this.customerList = res.result;
        }
      })
    }
  }
</script>

<style scoped>

</style>