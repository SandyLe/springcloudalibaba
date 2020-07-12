<template>
  <a-modal
    class="customModal"
    :width="modelStyle.width"
    :visible="visible"
    :bodyStyle ="bodyStyle"
    @ok="handleOk"
    @cancel="handleCancel"
    destroyOnClose
    :confirmLoading="confirmLoading"
    cancelText="关闭">
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
        <a-card class="card" title="基础信息" :bordered="true">
          <a-row>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="名称">
                <a-input placeholder="请输入名称" v-decorator="[ 'name', validatorRules.name]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="代码">
                <a-input placeholder="后台自动生成" :readOnly="true" v-decorator="[ 'code', validatorRules.code]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="客户类型">
                <a-select v-decorator="['customerTypeId', validatorRules.customerTypeId]" placeholder="请选择类型列表">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in typeList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="客户来源">
                <a-select v-decorator="['customerSourceId', {}]" placeholder="请选择来源">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in sourceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="手机">
                <a-input placeholder="请输入手机号码" v-decorator="[ 'phone', validatorRules.phone]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="微商城会员">
                <a-input placeholder="微商城会员" v-decorator="[ 'memberNo', validatorRules.memberNo]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="会员手机">
                <a-input placeholder="请输入会员手机号" v-decorator="[ 'memberPhone', validatorRules.memberPhone]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="会员昵称">
                <a-input placeholder="请输入代码" v-decorator="[ 'memberNickName', validatorRules.memberNickName]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="客户昵称">
                <a-input placeholder="请输入客户昵称" v-decorator="[ 'nickName', validatorRules.nickName]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="性别">
                <j-dict-select-tag v-decorator="['gender', {}]" placeholder="性别" :type="'radio'" :triggerChange="true" dictCode="sex"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="生日">
                <a-date-picker
                  style="width: 100%"
                  placeholder="请选择生日"
                  v-decorator="['birthday', {initialValue:!model.birthday?null:moment(model.birthday,dateFormat)}]"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="联系人">
                <a-input placeholder="请输入联系人" v-decorator="[ 'linkman', validatorRules.linkman]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="电话">
                <a-input placeholder="请输入电话号码" v-decorator="[ 'tel', validatorRules.tel]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="邮箱">
                <a-input placeholder="请输入邮箱" v-decorator="[ 'email', validatorRules.email]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="传真">
                <a-input placeholder="请输入传真号码" v-decorator="[ 'fax', validatorRules.fax]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="折扣">
                <a-input placeholder="请输入折扣" v-decorator="[ 'discount', validatorRules.discount]" style="width: 40%" />
                <j-dict-select-tag v-decorator="['discountTypeId', {}]" placeholder="折扣类型" :type="'select'" style="width: 60%" :triggerChange="true" dictCode="discount_type"/>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>

        <a-card class="card" title="开票信息" :bordered="true">
          <a-row>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="抬头">
                <a-input placeholder="请输入抬头" v-decorator="[ 'billTitle', validatorRules.billTitle]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="税号">
                <a-input placeholder="请输入税号" v-decorator="[ 'taxNo', validatorRules.taxNo]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="银行账号">
                <a-input placeholder="请输入银行账号" v-decorator="[ 'bankaccount', validatorRules.bankaccount]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="银行名称">
                <a-input placeholder="请输入银行名称" v-decorator="[ 'bankName', validatorRules.bankName]" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import pick from 'lodash.pick'
  import moment from 'moment'
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {saveCustomer,duplicateCheck, getCustomerTypeList, getCustomerSourceList} from '@/api/api'
  import ARow from "ant-design-vue/es/grid/Row";
  export default {
    name: "Customer",
    data() {
      return {
        description: '高级表单常见于一次性输入和提交大批量数据的场景。',
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
        model: {},
        form: this.$form.createForm(this),
        validatorRules: {
          name: {
            rules: [
              { min: 1, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' },
              {required: true, message: '名称不能为空!'}
            ]
          },
          customerTypeId: {
            rules: [
              {required: true, message: '请选择客户类型!'}
            ]
          }
        },
        bodyStyle:{
          padding: "0",
          height:(window.innerHeight*0.8)+"px",
          "overflow-y":"auto",

        },
        modelStyle:{
          width: '80%',
          style: { top: '20px' },
          fullScreen: false
        },
        dateFormat:"YYYY-MM-DD",
        sourceList: [],
        typeList: []
      }
    },
    components: {ARow, AFormItem,JDictSelectTag},
    created () {
    },
    methods: {
      add () {
        this.edit({'gender':'1','cdiDefaultType':''});
      },
      edit (record) {
        getCustomerTypeList().then((res) => {
          if (res.success) {
            this.typeList = res.result;
          }
        })
        getCustomerSourceList().then((res) => {
          if (res.success) {
            this.sourceList = res.result;
          }
        })
        if (record.birthday) {
          record.birthday = moment(new Date(record.birthday),this.dateFormat);
        }

        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name', 'code','content','customerTypeId','customerSourceId','phone','memberNo','memberPhone',
          'memberNickName','nickName','gender','birthday','linkman','tel','email','fax','discount','discountTypeId','bankaccount','billTitle',
            'bankName','province','city','district','address','taxNo'))
        });
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      moment,
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            if(!values.birthday){
              values.birthday = '';
            }else{
              values.birthday = values.birthday.format(this.dateFormat);
            }
            let formData = Object.assign(this.model, values);
            let obj;
            if(this.model){
              obj=saveCustomer(formData);
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
      },
      /** 切换全屏显示 */
      handleClickToggleFullScreen() {
        let mode = !this.modelStyle.fullScreen
        if (mode) {
          this.modelStyle.width = '100%'
          this.modelStyle.style.top = '20px'
        } else {
          this.modelStyle.width = '60%'
          this.modelStyle.style.top = '50px'
        }
        this.modelStyle.fullScreen = mode
      }
    }
  }
</script>

<style scoped>

  .customModal {
    .ant-modal-header {
      border: none;
      display: inline-block;
      position: absolute;
      z-index: 1;
      right: 56px;
      padding: 0;
      .ant-modal-title {
        .custom-btn{
          width: 56px;
          height: 56px;
          border: none;
          box-shadow: none;
        }
      }
    }
    .daily-article{
      border-bottom: 0;
    }
  }
</style>