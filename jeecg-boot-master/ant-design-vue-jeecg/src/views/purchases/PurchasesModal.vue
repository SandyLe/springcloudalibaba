<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <!-- <j-dict-select-tag type="list" v-decorator="['vendorId']" :trigger-change="true" dictCode="" placeholder="请选择供应商"/> -->
          <a-select v-decorator="['vendorId', {}]" placeholder="请选择供应商">
            <a-select-option v-for="(item, key) in vendorIddictOptions" :key="key" :value="item.id">
                  {{ item.name }}
            </a-select-option>
          </a-select> 
        </a-form-item>
        <a-form-item label="业务时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择业务时间" v-decorator="[ 'businessTime', validatorRules.businessTime]" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'amount', validatorRules.amount]" placeholder="请输入金额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="排序值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sort', validatorRules.sort]" placeholder="请输入排序值"></a-input>
        </a-form-item>
        <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <!-- <a-input v-decorator="[ 'states', validatorRules.states]" placeholder="请输入状态"></a-input> -->
          <a-select v-decorator="['states', {}]" placeholder="请选择状态">
            <a-select-option v-for="(item, key) in purchasestypedict" :key="key" :value="item.value">
                  {{ item.text }}
            </a-select-option>
          </a-select> 
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'  
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: "PurchasesModal",
    components: { 
      JDate,
      JDictSelectTag,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        validatorRules:{
        vendorId:{},
        businessDate:{rules: [{ required: true, message: '请输入业务时间!' }]},
        amount:{},
        sort:{},
        states:{},
        },
        url: {
          add: "/purchases/add",
          edit: "/purchases/edit",
        },
        vendorIddictOptions:[],
        purchasestypedict:[]
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.vendorIddictOptions = this.$parent.$parent.dictOptions.vendorId;
        this.purchasestypedict = this.$parent.$parent.dictOptions.purchasestype;
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'vendorId','businessDate','amount','updateDate','createDate','sort','states'))
        })
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
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
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
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'vendorId','businessDate','amount','updateDate','createDate','sort','states'))
      },

      
    }
  }
</script>