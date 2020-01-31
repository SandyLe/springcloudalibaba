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
              label="原单编号">
              <a-input type="text" :readOnly="true" v-decorator="[ 'sourceCode', {}]" />
              <a-input type="hidden" placeholder="请输入规格" v-decorator="[ 'sourceId', {}]" />
            </a-form-item>
          </a-col>
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
              label="入库时间">
              <a-date-picker format="YYYY-MM-DD HH:mm:ss"
                            placeholder="请输入入库时间" showTime
                            style="width: 100%" @change="onDateChange"
                            :defaultValue="!model.putInTime ? null :moment( model.putInTime, 'YYYY-MM-DD HH:mm:ss')" name="putInTime" id="putInTime"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
 import moment from 'moment';
import 'moment/locale/zh-cn';
moment.locale('zh-cn');
  import pick from 'lodash.pick'
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import {getWarehouseList, inventoryInedit } from '@/api/api'

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
        title: "入库单",
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
      moment,
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        let that = this;
        getWarehouseList().then((res) => {
          if (res.success) {
            that.warehouseList = res.result;
          }
        })
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'id','sourceId' ,'sourceCode' ,'sourceBillType' ,'billStatus','putInTime','warehouseId','billType'))
        });

      },
      mtlChange (val) {
        this.model.warehouseId = val;
        this.form.setFieldsValue({ 'warehouseId' : val })
      },
      onDateChange(date, dateString) {
        this.model.putInTime = dateString;
        this.form.setFieldsValue({ 'putInTime' : dateString })
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
            inventoryInedit(formData).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
              if (formData.sourceBillType === 4) {
                that.$router.replace({ path:'/purchase/PurchaseList' });
              }
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