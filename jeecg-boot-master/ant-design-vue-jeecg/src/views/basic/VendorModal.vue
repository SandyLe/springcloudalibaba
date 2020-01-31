<template>
  <a-modal
    :title="title"
    :width="modelStyle.width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :bodyStyle ="bodyStyle"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    wrapClassName="ant-modal-cust-warp">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-row>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="名称">
              <a-input placeholder="请输入名称" v-decorator="[ 'name', validatorRules.name]" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="代码">
              <a-input placeholder="后台编号器自动生成代码" v-decorator="[ 'code', validatorRules.code]" :readOnly="true" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="联系人">
              <a-input placeholder="请输入联系人" v-decorator="[ 'linkman', validatorRules.linkman]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="手机">
              <a-input placeholder="请输入手机" v-decorator="[ 'phone', validatorRules.phone]" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="电话">
              <a-input placeholder="请输入电话" v-decorator="[ 'tel', validatorRules.tel]" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="邮箱">
              <a-input placeholder="请输入邮箱" v-decorator="[ 'email', validatorRules.email]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="开票信息">
              <a-input placeholder="请输入开票信息" v-decorator="[ 'billingInfo', validatorRules.billingInfo]" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="折扣">
              <a-input placeholder="请输入折扣" v-decorator="[ 'discount', validatorRules.discount]" style="width: 40%" />
              <j-dict-select-tag v-decorator="['discountTypeId', {}]" placeholder="折扣类型" :type="'select'" style="width: 60%" :triggerChange="true" dictCode="discount_type"/>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="网站">
              <a-input placeholder="请输入网站" v-decorator="[ 'web', validatorRules.web]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="银行账号">
              <a-input placeholder="请输入银行账号" v-decorator="[ 'bankaccount', validatorRules.bankaccount]" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="银行账户">
              <a-input placeholder="请输入银行账户" v-decorator="[ 'bankacctName', validatorRules.bankacctName]" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="银行名称">
              <a-input placeholder="请输入银行名称" v-decorator="[ 'bankName', validatorRules.bankName]" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="传真">
              <a-input placeholder="请输入传真" v-decorator="[ 'fax', validatorRules.fax]" />
            </a-form-item>
          </a-col>
          <a-col :md="16" :sm="16">
            <a-form-item
              :labelCol="hlabelCol"
              :wrapperCol="hwrapperCol"
              label="地址">
              <a-select v-decorator="['province', {}]" placeholder="省" style="width: 15%" @change="areaChange" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in provinceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                </a-select-option>
              </a-select>
              <a-select v-decorator="['city', {}]" placeholder="市" style="width: 15%" @change="areaChange" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in cityList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                </a-select-option>
              </a-select>
              <a-select v-decorator="['district', {}]" placeholder="区、县" style="width: 15%">
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in districtList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                </a-select-option>
              </a-select>
              <a-input placeholder="请输入详细地址" v-decorator="[ 'address', validatorRules.address]" style="width: 55%"  />
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
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {addVendor,editVendor,getAreaList,duplicateCheck } from '@/api/api'
  export default {
    name: "VendorModal",
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
              { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' }
            ]
          }
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
        provinceList: [],
        cityList: [],
        districtList:[]
      }
    },
    components: {AFormItem,JDictSelectTag},
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

        getAreaList({parentId:'100000'}).then((res) => {
          if (res.success) {
            this.provinceList = res.result
          }
        })
        if (record.province){
          getAreaList({parentId:record.province}).then((res) => {
            if (res.success) {
              if(res.result && res.result.length>0){
                if(res.result[0].levelType==2){
                  this.cityList = res.result;
                }
              }
            }
          })
        }
        if (record.city){
          getAreaList({parentId:record.city}).then((res) => {
            if (res.success) {
              if(res.result && res.result.length>0){
                if(res.result[0].levelType==3){
                  this.districtList = res.result;
                }
              }
            }
          })
        }
        //编辑页面禁止修改角色编码
        if(this.model.id){
          this.roleDisabled = true;
        }else{
          this.roleDisabled = false;
        }

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name', 'code','linkman','phone','tel','email','fax','bankaccount','bankacctName','bankName','discount','discountTypeId'
            ,'billingInfo','web','province','city','district','address'))
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
              obj=addVendor(formData);
            }else{
              obj=editVendor(formData);
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
      areaChange(val){
        getAreaList({parentId:val}).then((res) => {
          if (res.success) {
            if(res.result && res.result.length>0){
              if(res.result[0].levelType==2){
                this.cityList = res.result;
                this.model.city = '';
                this.model.district = '';
                this.$nextTick(() => {
                  this.form.setFieldsValue(pick(this.model,'city', 'district'))
                });
              }else if(res.result[0].levelType==3){
                this.districtList = res.result;
                this.model.district = '';
                this.$nextTick(() => {
                  this.form.setFieldsValue(pick(this.model, 'district'))
                });
              }
            }
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>