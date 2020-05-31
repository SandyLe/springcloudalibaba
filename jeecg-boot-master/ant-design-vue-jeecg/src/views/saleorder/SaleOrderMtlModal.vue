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
              label="产品">

              <a-select v-decorator="['mtlId', {}]" placeholder="请选择产品"  showSearch
                        optionFilterProp="children" @change="mtlChange"
                        notFoundContent="无法找到，输入关键词Enter搜索" @keyup.enter.native="searchMtl" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in mtlList" :key="key" :value="item.id">
                  {{ item.info }}
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
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="指导价">
              <a-input placeholder="请输入销售指导价" ref="priceInput" :disabled="true" v-decorator="[ 'price', {}]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="数量">
              <a-input placeholder="请输入数量" v-decorator="[ 'quantity', {}]" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="单位">
              <a-select v-decorator="['unitId', {}]" placeholder="单位" @change="unitChange" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in unitList" :key="key" :value="item.unitId">
                    <span style="display: inline-block;width: 100%" :title=" item.unit">
                      {{ item.unit }}
                    </span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="折扣%">
              <a-input placeholder="请输入折扣%" ref="discountInput" @blur.native.capture="handleDiscountBlur" v-decorator="[ 'discount', {}]" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="成交价">
              <a-input placeholder="请输入成交价" ref="transactionPriceInput" @blur.native.capture="handleTransactionPriceBlur" v-decorator="[ 'transactionPrice', {}]" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              :hidden = true
              label="折扣方式">
              <a-select v-decorator="['discountType', {}]" placeholder="折扣方式" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in discountTypeList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
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
  import {addSaleMtlOrder,editSaleMtlOrder,searchMaterial,duplicateCheck,getDiscountTypeList,getMaterialSelfUnitList,getMtlPrice } from '@/api/api'

  export default {
    name: "SaleOrderMtlModal",
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
        discountTypeList: []
      }
    },
    components: {AFormItem},
    created () {
    },
    methods: {
      handleDiscountBlur(e){
        const price = this.$refs.priceInput.value;
        const discount = e.target.valueOf().value;
        if (price) {
          this.model.transactionPrice = price * discount * 0.01;
          this.$nextTick(() => {
            this.form.setFieldsValue(pick(this.model,'transactionPrice'))
          });
        } else {
          this.$message.warning('没有获取到销售指导价！请联系管理员！');
        }
      },
      handleTransactionPriceBlur(e){
        const price = this.$refs.priceInput.value;
        const transactionPriceInput = e.target.valueOf().value;
        if (price) {
          this.model.discount = (transactionPriceInput / price).toFixed(4) * 100 ;
          this.$nextTick(() => {
            this.form.setFieldsValue(pick(this.model,'discount'))
          });
        } else {
          this.$message.warning('没有获取到销售指导价！请联系管理员！');
        }
      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();

        record.sourceId = this.$route.query.id;
        record.discountType = '0';//比例折扣
        this.model = Object.assign({}, record);
        this.visible = true;
        let that = this;
        searchMaterial().then((res) => {
          if (res.success) {
            that.mtlList = res.result;
          }
        })
        getDiscountTypeList().then((res) => {
          if (res.success) {
            that.discountTypeList = res.result;
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
          this.form.setFieldsValue(pick(this.model,'mtlId', 'mtlCode','specification','unitId','price','transactionPrice','discount','quantity','discountType'))
        });

      },
      mtlChange (val) {
        getMaterialSelfUnitList({addSelf:true,sourceId:val}).then((res) => {
          if (res.success) {
            this.unitList = res.result;
          }
        })

        getMtlPrice({customerId:this.saleOrder.customerId, mtlId:val, unitId:this.unitId}).then((res) => {
          if (res.success) {
            const mtlPrice = res.result;
            this.model.mtlCode = mtlPrice.mtlCode;
            this.model.unitId = mtlPrice.unitId;
            this.model.specification = mtlPrice.specification;
            this.model.discount = 100;
            this.model.price = mtlPrice.price;
            this.model.transactionPrice = mtlPrice.price;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'mtlCode','specification','unitId','price','discount','transactionPrice'))
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
            values.sourceId = this.$route.query.id;
            let formData = Object.assign(this.model, values);
            let obj;
            console.log(formData)
            if(!this.model.id){
              obj=addSaleMtlOrder(formData);
            }else{
              obj=editSaleMtlOrder(formData);
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