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
        <a-row>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="功能类型">
              <a-select v-decorator="['typeId', {}]" placeholder="请选择收功能类型" showSearch optionFilterProp="children"
                        notFoundContent="没有匹配的收款方"  >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in typeList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="收件人">
              <a-input placeholder="请输入收件人" v-decorator="[ 'recipients', validatorRules.recipients]" />
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
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-item
              :labelCol="hlabelCol"
              :wrapperCol="hwrapperCol"
              label="地址">
              <a-select v-decorator="['province', {}]" placeholder="省" @change="areaChange" style="width: 10%">
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in provinceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                </a-select-option>
              </a-select>
              <a-select v-decorator="['city', {}]" placeholder="市" @change="areaChange" style="width: 10%">
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in cityList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                </a-select-option>
              </a-select>
              <a-select v-decorator="['district', {}]" placeholder="区、县" style="width: 10%">
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in districtList" :key="key" :value="item.id">
                      <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                        {{ item.name || item.code }}
                      </span>
                </a-select-option>
              </a-select>
              <a-input placeholder="请输入详细地址" v-decorator="[ 'name', validatorRules.name]" style="width: 70%"  />
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
  import {addAddress,editAddress,getAddressTypeList,getAreaList,duplicateCheck } from '@/api/api'
  import ARow from "ant-design-vue/es/grid/Row";

  export default {
    name: "AddressModal",
    props: {
      entity: {
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
        hlabelCol: {
          xs: { span: 24 },
          sm: { span: 2 },
        },
        hwrapperCol: {
          xs: { span: 24 },
          sm: { span: 20 },
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
        typeList:[],
        provinceList: [],
        cityList: [],
        districtList: []
      }
    },
    components: {ARow, AFormItem},
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        console.log(this.$route.query.id)
        this.form.resetFields();
        record.sourceId = this.$route.query.id;
        this.model = Object.assign({}, record);
        this.visible = true;
        let that = this;
        getAddressTypeList().then((res) => {
          if (res.success) {
            that.typeList = res.result;
          }
        });
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
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'sourceId', 'typeId', 'province', 'city', 'district', 'name', 'recipients', 'tel'))
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
              obj=addAddress(formData);
            }else{
              obj=editAddress(formData);
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
          this.modelStyle.width = '80%'
          this.modelStyle.style.top = '50px'
        }
        this.modelStyle.fullScreen = mode
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