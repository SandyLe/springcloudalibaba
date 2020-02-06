<template>
  <a-modal
    :width="modelStyle.width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    wrapClassName="ant-modal-cust-warp"
    :bodyStyle ="bodyStyle">

    <template slot="title">
      <div style="width: 100%;height:20px;padding-right:32px;">
        <div style="float: left;">{{ title }}</div>
        <div style="float: right;">
          <a-button
            icon="fullscreen"
            style="width:56px;height:100%;border:0"
            @click="handleClickToggleFullScreen"/>
        </div>
      </div>
    </template>

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="费用名称">
          <a-select v-decorator="['expenseId', {}]" placeholder="请选择费用名称" showSearch optionFilterProp="children"
                    @change="expenseChange" notFoundContent="没有匹配的费用名称"  >
            <a-select-option value="">请选择</a-select-option>
            <a-select-option v-for="(item, key) in expenseList" :key="key" :value="item.expenseId">
                    <span style="display: inline-block;width: 100%" :title=" item.expense ">
                      {{ item.expense}}
                    </span>
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="费用">
          <a-input placeholder="请输入费用" v-decorator="[ 'amount', {}]" :readOnly="true"/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import {addSaleOrderReturnExpense,editSaleOrderReturnExpense,getSaleOrderExpenseList,getSaleOrderExpenseOne } from '@/api/api'

  export default {
    name: "SaleOrderReturnExpenseModal",
    props: {
      saleOrderReturn: {
        type: Object,
        default: () => {}
      }
    },
    data() {
      return {
        title: "操作",
        visible: false,
        confirmLoading: false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        bodyStyle:{
          padding: "0",
          height:(window.innerHeight*0.8)+"px",
          "overflow-y":"auto"
        },
        modelStyle:{
          width: '80%',
          style: { top: '20px' },
          fullScreen: false
        },
        model: {},
        form: this.$form.createForm(this),
        validatorRules: {
          rcontent: {
            rules: [
              { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' }
            ]
          }
        },
        expenseList:[],
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
        debugger
        this.form.resetFields();
        record.sourceId = this.$route.query.id;
        this.model = Object.assign({}, record);
        this.visible = true;
        let that = this;
        getSaleOrderExpenseList({sourceId:this.saleOrderReturn.sourceId}).then((res) => {
          if (res.success) {
            that.expenseList = res.result;
          }
        })
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'expenseId', 'amount'))
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
            values.sourceId = this.$route.query.id;
            let formData = Object.assign(this.model, values);
            let obj;
            console.log(formData)
            if(!this.model.id){
              obj=addSaleOrderReturnExpense(formData);
            }else{
              obj=editSaleOrderReturnExpense(formData);
            }
            obj.then((res)=>{
              if(res.success){
                that.saleOrderReturn.totalamount = res.result;
                console.log(that.saleOrderReturn.totalamount)
                debugger
                this.$emit("listenToTotalamont",that.saleOrderReturn.totalamount);
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
      },
      /** 切换全屏显示 */
      handleClickToggleFullScreen() {
        let mode = !this.modelStyle.fullScreen
        if (mode) {
          this.modelStyle.width = '100%'
          this.modelStyle.style.top = '20px'
        } else {
          this.modelStyle.width = '80%'
          this.modelStyle.style.top = '50px'
        }
        this.modelStyle.fullScreen = mode
      },
      expenseChange(expenseId) {
        getSaleOrderExpenseOne({sourceId:this.saleOrderReturn.sourceId, expenseId:expenseId}).then((res) => {
          if (res.success) {
            const record = res.result;
            record.id = null;
            record.createBy = null;
            record.createTime = null;
            this.model = Object.assign({}, record);
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'expenseId', 'amount'))
            });
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>