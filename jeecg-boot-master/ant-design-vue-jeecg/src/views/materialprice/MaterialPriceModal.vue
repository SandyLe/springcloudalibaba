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
              :wrapperCol="wrapperCol" label="产品">
              <a-select  v-decorator="['materialId', validatorRules.materialId]" placeholder="请选择产品"  showSearch
                        optionFilterProp="children" @change="mtlChange"
                        notFoundContent="无法找到，输入关键词Enter搜索" @keyup.enter.native="searchMtl" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in mtlList" :key="key" :value="item.id">
                  {{ item.info }}
                </a-select-option>
              </a-select>
            </a-form-item>
        </a-col><!--
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="客户类型">
              <a-select v-decorator="['customerTypeId', {}]" placeholder="请选择类型列表">
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in typeList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>-->
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
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="价格">
              <a-input placeholder="请输入价格" v-decorator="[ 'price', validatorRules.price]" />
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
  import {searchMaterial, addMaterialPrice,editMaterialPrice,getCustomerTypeList,getMaterialUnitList,getMaterialList,duplicateCheck,getMaterialOne } from '@/api/api'
  export default {
    name: "MaterialPriceModal",
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
        mtlList: [],
        unitList: [],
        typeList: [],
        model: {},
        form: this.$form.createForm(this),
        validatorRules: {
          materialId: {
            rules: [
              {required: true, message: '请选择产品!'}
            ]
          },
          content: {
            rules: [
              { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' }
            ]
          },
          price: {
            rules: [
              {required: true, message: '请输入价格!'}
            ]
          }
        }
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
        getMaterialList().then((res) => {
          if (res.success) {
            this.materialList = res.result;
          }
        })
        getCustomerTypeList().then((res) => {
          if (res.success) {
            this.typeList = res.result;
          }
        })
        getMaterialUnitList().then((res) => {
          if (res.success) {
            this.unitList = res.result;
          }
        })
        this.model = Object.assign({}, record);
        this.visible = true;

        //编辑页面禁止修改角色编码
        if(this.model.id){
          this.roleDisabled = true;
        }else{
          this.roleDisabled = false;
        }

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'materialId', 'customerTypeId','unitId','price','content'))
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
              obj=addMaterialPrice(formData);
            }else{
              obj=editMaterialPrice(formData);
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
      searchMtl (e) {
        searchMaterial({"keyword":e.target.valueOf().value}).then((res) => {
          if (res.success) {
            this.mtlList = res.result;
          }
        })
      },
      handleCancel () {
        this.close()
      },
      mtlChange (val) {
        getMaterialOne({id:val}).then((res) => {
          if (res.success) {
            const material = res.result;
            this.model.unitId = material.unitId;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'unitId'))
            });
          }
        })
      }
    },
    mounted() {
      searchMaterial().then((res) => {
        if (res.success) {
          this.mtlList = res.result;
        }
      })
    }
  }
</script>

<style scoped>

</style>