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
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="仓库">
              <a-select v-decorator="['warehouseId', {}]" placeholder="请选择仓库" showSearch optionFilterProp="children"
                        @change="mtlChange" notFoundContent="没有匹配的仓库"  >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in warehouseList" :key="key" :value="item.id">
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
              label="产品">
              <a-select v-decorator="['mtlId', {}]" placeholder="请选择产品" showSearch optionFilterProp="children"
                        @change="mtlChange" notFoundContent="没有匹配的产品"  >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in mtlList" :key="key" :value="item.id">
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
              label="规格">
              <a-input placeholder="请输入规格" v-decorator="[ 'specification', {}]" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="编码">
              <a-input placeholder="请输入代码" v-decorator="[ 'mtlCode', {}]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="条形码">
              <a-input placeholder="请输入条形码" v-decorator="[ 'barCode', {}]" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="盘点数量">
              <a-input placeholder="请输入数量" v-decorator="[ 'stockAmount', {}]" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="盘点前数量">
              <a-input placeholder="请输入数量" v-decorator="[ 'beforeAmount', {}]" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="单位">
              <a-select v-decorator="['unitId', {}]" placeholder="单位">
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in unitList" :key="key" :value="item.unitId">
                    <span style="display: inline-block;width: 100%" :title=" item.unit">
                      {{ item.unit }}
                    </span>
                </a-select-option>
              </a-select>
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
  import {getWarehouseList, addStocking,editStocking,getMaterialList,duplicateCheck,getMaterialSelfUnitList,getMaterialOne } from '@/api/api'

  export default {
    name: "StockingModal",
    props: {
      saleOrder: {
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
        unitId: '',
        mtlList:[],
        unitList: [],
        warehouseList: []
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
        console.log(this.$route.query.id)
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        let that = this;
        getWarehouseList().then((res) => {
          if (res.success) {
            that.warehouseList = res.result;
          }
        })
        getMaterialList().then((res) => {
          if (res.success) {
            that.mtlList = res.result;
          }
        })
        if (record.mtlId){
          getMaterialSelfUnitList({addSelf:true,sourceId:record.mtlId}).then((res) => {
            if (res.success) {
              this.unitList = res.result;
            }
          })
        } else {
          this.unitList = [];
        }
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'warehouseId','mtlId' ,'mtlCode','barCode','specification','unitId','beforeAmount','stockAmount'))
        });

      },
      mtlChange (val) {
        getMaterialSelfUnitList({addSelf:true,sourceId:val}).then((res) => {
          if (res.success) {
            this.unitList = res.result;
          }
        })

        getMaterialOne({id:val}).then((res) => {
          if (res.success) {
            const material = res.result;
            this.model.mtlCode = material.code;
            this.model.specification = material.specification;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'mtlCode','specification'))
            });
          }
        })
      },
      unitChange (val) {
        this.unitId = val;
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
              obj=addStocking(formData);
            }else{
              obj=editStocking(formData);
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
      }
    }
  }
</script>

<style scoped>

</style>