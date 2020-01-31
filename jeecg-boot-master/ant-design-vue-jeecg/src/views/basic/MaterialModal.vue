<template>
  <a-modal
    :width="modelStyle.width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :bodyStyle ="bodyStyle"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    wrapClassName="ant-modal-cust-warp">

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
              label="规格">
              <a-input placeholder="请输入规格" v-decorator="[ 'specification', validatorRules.specification]" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="类型">
              <a-select v-decorator="['typeId', {}]" placeholder="类型" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in typeList" :key="key" :value="item.id">
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
              label="品牌">
              <a-select v-decorator="['brandId', {}]" placeholder="品牌" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in brandList" :key="key" :value="item.id">
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
              label="库存上限">
              <a-input placeholder="请输入库存上限" v-decorator="[ 'uplimit', validatorRules.uplimit]" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="库存下线">
              <a-input placeholder="请输入库存下限" v-decorator="[ 'downlimit', validatorRules.downlimit]" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="单位">
              <a-select v-decorator="['unitId', {}]" placeholder="单位" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in unitList" :key="key" :value="item.id">
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
              label="条形码">
              <a-input placeholder="请输入条形码" v-decorator="[ 'barCode', validatorRules.barCode]" />
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
  import {addMaterial,editMaterial,getMaterialBrandList,getMaterialTypeList,getMaterialUnitList,duplicateCheck } from '@/api/api'
  export default {
    name: "MaterialModal",
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
        typeList: [],
        brandList: [],
        unitList:[]
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

        getMaterialTypeList().then((res) => {
          if (res.success) {
            this.typeList = res.result;
          }
        })
        getMaterialUnitList().then((res) => {
          if (res.success) {
            this.unitList = res.result;
          }
        })
        getMaterialBrandList().then((res) => {
          if (res.success) {
            this.brandList = res.result;
          }
        })
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
          this.form.setFieldsValue(pick(this.model,'name', 'code','specification','brandId','typeId','uplimit','downlimit','unitId','barCode'))
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
              obj=addMaterial(formData);
            }else{
              obj=editMaterial(formData);
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

</style>