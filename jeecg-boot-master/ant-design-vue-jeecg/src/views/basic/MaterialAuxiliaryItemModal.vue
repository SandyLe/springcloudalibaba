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
              label="辅助属性">
              <a-select v-decorator="['suppCode', validatorRules.suppCode]" placeholder="请选择辅助属性" showSearch optionFilterProp="children"
                        notFoundContent="没有匹配的辅助属性" @change="suppChange"  >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in supplementaryList" :key="key" :value="item.code">
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
              label="属性值">
              <a-select v-decorator="['suppValueCode', validatorRules.suppValueCode]" placeholder="请选择辅助属性" showSearch optionFilterProp="children"
                        notFoundContent="没有匹配的辅助属性"  >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in supplementaryValList" :key="key" :value="item.code">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                </a-select-option>
              </a-select>
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
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {addMaterialAuxiliaryItem,editMaterialAuxiliaryItem,getSupplementaryList,getSupplementaryValListBySourceCode,duplicateCheck } from '@/api/api'
  export default {
    name: "MaterialAuxiliaryItemModal",
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
              {required: true, message: '请输入辅助属性名称!'}
            ]
          },
          content: {
            rules: [
              { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' }
            ]
          }
        },
        supplementaryList:[],
        supplementaryValList:[]
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
        getSupplementaryList().then((res) => {
          if (res.success) {
            this.supplementaryList = res.result;
          }
        });

        getSupplementaryValListBySourceCode({sourceCode:record.suppCode}).then((res) => {
          if (res.success) {
            this.supplementaryValList = res.result;
          }
        });
        this.form.resetFields();
        record.mtlId = this.$route.query.id;
        this.model = Object.assign({}, record);
        this.visible = true;

        //编辑页面禁止修改角色编码
        if(this.model.id){
          this.roleDisabled = true;
        }else{
          this.roleDisabled = false;
        }

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'suppValueCode', 'suppCode','content'))
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
            if(!this.model.id){
              obj=addMaterialAuxiliaryItem(formData);
            }else{
              obj=editMaterialAuxiliaryItem(formData);
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
      suppChange (e) {

        getSupplementaryValListBySourceCode({sourceCode:e}).then((res) => {
          if (res.success) {
            this.supplementaryValList = res.result;
          }
        });
      }
    }
  }
</script>

<style scoped>

</style>