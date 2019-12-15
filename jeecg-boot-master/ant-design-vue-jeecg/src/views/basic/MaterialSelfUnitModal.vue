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
              label="单位">
              <a-input style="display: none" v-decorator="[ 'sourceId', validatorRules.sourceId]" />
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
          <a-col :md="12" :sm="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="单位类型">
              <j-dict-select-tag v-decorator="['unitType', {}]" placeholder="单位类型" :type="'radio'" :triggerChange="true" dictCode="unitType"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="与主单位比例">
              <a-input placeholder="请输入与主单位比例" v-decorator="[ 'qty', validatorRules.qty]" />
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
  import {addMaterialSelfUnit,editMaterialSelfUnit,getMaterialUnitList,duplicateCheck } from '@/api/api'
  export default {
    name: "MaterialSelfUnitModal",
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
        unitList:[]
      }
    },
    components: {AFormItem},
    created () {
    },
    methods: {
      add (sourceId) {
        this.edit({sourceId: sourceId, unitType: '0'});
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

        getMaterialUnitList().then((res) => {
          if (res.success) {
            this.unitList = res.result;
          }
        })

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name', 'code','sourceId', 'unitType', 'qty', 'unitId'))
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
              obj=addMaterialSelfUnit(formData);
            }else{
              obj=editMaterialSelfUnit(formData);
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
    }
  }
</script>

<style scoped>

</style>