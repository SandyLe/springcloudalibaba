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
    style="top:5%;height: 55%;overflow-y: hidden">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item label="费用名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'sourceCode', validatorRules.sourceCode]" type="hidden" />
              <a-input v-decorator="[ 'sourceId', {}]" placeholder="原单ID" type="hidden" />
              <a-select v-decorator="['expenseId', validatorRules.expenseId]" placeholder="请选择费用名称">
                <a-select-option v-for="(item, key) in expenseList" :key="key" :value="item.id">
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="金额">
              <a-input placeholder="请输入金额" v-decorator="[ 'payAmount', {}]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item label="付款方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="['payType', validatorRules.payType]" placeholder="请选择付款方式">
                <a-select-option v-for="(item, key) in payTypeList" :key="key" :value="item.id">
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="退款时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'payDate', {}]"/>
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
  import {addRefundOrderDtl,editRefundOrderDtl,getExpenseList, getPayModeTypeList } from '@/api/api'
  export default {
    name: "RefundOrderDtlModal",
    props: {
      sourceBillOrder: {
        type: Object,
        default: () => {}
      },
      sourceOrder: {
        type: Object,
        default: () => {}
      }
    },
    data() {
      return {
        dateFormat:"YYYY-MM-DD HH:mm:ss",
        title: "操作",
        visible: false,
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
          expenseId: {
            rules: [
              {required: true, message: '请选择费用名称!'}
            ]
          },
          customerId: {
            rules: [
              {payType: true, message: '请选择付款方式!'}
            ]
          },
          payAmount: {
            rules: [
              {payType: true, message: '请输入付款金额!'}
            ]
          }
        },
        expenseList: [],
        payTypeList: []
      }
    },
    components: {AFormItem},
    created () {
    },
    methods: {
      moment,
      add () {
        this.edit({payDate:new Date()});
      },
      edit (record) {
        this.form.resetFields();
        if (this.sourceBillOrder) {
          record.sourceBillId = this.$route.query.id;
          record.sourceBillCode = this.sourceBillOrder.code;
          record.sourceBillType = this.sourceBillOrder.billType;
        }
        if (this.sourceOrder) {
          record.sourceId = this.sourceOrder.id;
          record.sourceBillId = this.sourceOrder.sourceId;
          record.sourceBillCode = this.sourceOrder.sourceBillCode;
          record.sourceBillType = this.sourceOrder.sourceBillType;
        }
        this.model = Object.assign({}, record);
        this.visible = true;
        getExpenseList().then((res) => {
          if (res.success) {
            this.expenseList = res.result;
          }
        })
        getPayModeTypeList().then((res) => {
          if (res.success) {
            this.payTypeList = res.result;
          }
        })

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'sourceId', 'sourceCode', 'sourceBillType', 'sourceBillId', 'sourceBillCode','payAmount','payType','expenseId','content'))
          this.form.setFieldsValue({payDate: this.model.payDate ? moment(this.model.payDate) : null})
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
            if(!values.payDate){
              values.payDate = '';
            }else{
              values.payDate = values.payDate.format(this.dateFormat);
            }
            let formData = Object.assign(this.model, values);
            let obj;
            console.log(formData)
            if(!this.model.id){
              obj=addRefundOrderDtl(formData);
            }else{
              obj=editRefundOrderDtl(formData);
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