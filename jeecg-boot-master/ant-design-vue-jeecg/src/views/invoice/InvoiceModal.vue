<template>
<div>
    <a-card :bordered="false">
      <a-form :form="form">
        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item label="原单类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="['sourceBillType', {}]" placeholder="请选择单据类型" showSearch optionFilterProp="children"
                        notFoundContent="没有匹配的单据类型"  >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in billTypeList" :key="key" :value="item.id">
                  {{ item.name || item.code }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="12">
            <a-form-item label="原单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'sourceCode', validatorRules.sourceCode]" placeholder="请输入原单编号" />
              <a-input v-decorator="[ 'sourceId', {}]" placeholder="原销售单ID" type="hidden" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item label="发票类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="['invoiceTypeId', {}]" placeholder="请选择发票类型" showSearch optionFilterProp="children"
                        notFoundContent="没有匹配的发票类型"  >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in invoiceTypeList" :key="key" :value="item.id">
                  {{ item.name || item.code }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="代码">
              <a-input placeholder="后台自动生成单号" :readOnly="true" v-decorator="[ 'code', validatorRules.code]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item label="材质" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="['invoiceTextureId', {}]" placeholder="请选择发票类型" showSearch optionFilterProp="children"
                        notFoundContent="没有匹配的发票类型"  >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in invoiceTextureList" :key="key" :value="item.id">
                  {{ item.name || item.code }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="单据时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'billDate', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-item
              :labelCol="hlabelCol"
              :wrapperCol="hwrapperCol"
              label="抬头"
              label-width="4">
              <a-input placeholder="请输入抬头" v-decorator="[ 'billTitle', validatorRules.billTitle]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="税号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'taxNumber', validatorRules.taxNumber]" placeholder="请输入税号" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'email', validatorRules.email]" placeholder="请输入邮箱" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'address', validatorRules.address]" placeholder="请输入地址" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'tel', validatorRules.tel]" placeholder="请输入电话" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="开户行" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'openningBank', validatorRules.openningBank]" placeholder="请输入开户行" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="账号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'bankNo', validatorRules.bankNo]" placeholder="请输入账号" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-card class="card" title="收货信息" :bordered="true">
              收货地址：<span style="font-weight: bold">{{invoiceAddress.recipients}} &nbsp;</span> <a>{{invoiceAddress.tel}}</a> {{invoiceAddress.fullAddress}}
              <a @click="editAddressItem(invoice)"><a-icon type="setting"/> 请选择收获地址</a>
            </a-card>
          </a-col>
        </a-row>
      </a-form>
      <sale-address-list ref="saleAddressList" v-on:addressFlag="addressOK"></sale-address-list>
    </a-card>
    <footer-tool-bar>
        <a-button v-if="unEditable" type="primary" @click="handleOk">保存</a-button>
        <router-view :key="this.$route.path"></router-view>
        <a-button :style="{marginLeft:'20px'}" @click="backToList">返回</a-button>
    </footer-tool-bar>

</div>
</template>

<script>
import {httpAction} from '@/api/manage'
import pick from 'lodash.pick'
import JDate from '@/components/jeecg/JDate'
import JDictSelectTag from '@/components/dict/JDictSelectTag'
import FooterToolBar from '@/components/tools/FooterToolBar'
import SaleAddressList from '../saleorder/SaleAddressList'
import {
  getBillTypeList,
  getInvoiceTypeList,
  getInvoiceTextureList,
  getInvoiceById,
  getInvoiceAddress,
  getSaleOrderOne,
  addInvoice
} from '@/api/api'
export default {
  name: 'InvoiceModal',
  components: {
    JDate,
    FooterToolBar,
    JDictSelectTag,
    SaleAddressList
  },
  data() {
    return {
      invoiceAddress: {},
      invoice: {},
      billTypeList: [],
      invoiceTextureList: [],
      invoiceTypeList: [],
      sourceId: null,
      sourceCode: null,
      sourceBillType: null,
      addressSourceId: null,
      form: this.$form.createForm(this),
      title: '操作',
      width: '80%',
      model: {},
      labelCol: {
          xs: {
              span: 24
          },
          sm: {
              span: 5
          }
      },
      wrapperCol: {
          xs: {
              span: 24
          },
          sm: {
              span: 16
          }
      },
      hlabelCol: {
        xs: { span: 24 },
        sm: { span: 2 },
      },
      hwrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      validatorRules: {
        vendorId: {
          rules: [{
            required: true,
            message: '请选择供应商!'
          }]
        },
        batchNo: {
              rules: [{
                  required: true,
                  message: '请选择批次编号!'
              }]
          },
          amount: {},
          description: {},
          states: {}
      },
      url: {
          save: '/invoice/add',
          edit: '/invoice/edit'
      },
      unEditable: true
    }
  },
  created() {

  },
  watch: {

  },
  methods: {
    addressOK (){
      getInvoiceAddress({"sourceId": this.invoice.id}).then((res) => {
        if (res.success) {
          this.invoiceAddress = res.result;
        }
      });
    },
    editAddressItem (record) {
      record.customerId = this.addressSourceId;
      record.sourceAddId = this.invoiceAddress.sourceAddId;
      this.$refs.saleAddressList.edit(record);
    },
    add() {
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      debugger
      this.$nextTick(() => {
          this.form.setFieldsValue(
              pick(this.model, 'id', 'code', 'sourceId', 'sourceCode', 'sourceBillType', 'account', 'payamount', 'totalamount', 'batchNo')
          )
      })
    },
    close() {
        this.$emit('close')
        this.visible = false
    },
    handleOk() {
      const that = this
      // 触发表单验证

      this.form.validateFields((err, values) => {
          if (!err) {
              let httpurl = ''
              let method = ''
              if (!this.model.id) {
                  httpurl += this.url.add
                  method = 'post'
              } else {
                  httpurl += this.url.edit
                  method = 'put'
              }
              let formData = Object.assign(this.model, values)
              formData.detaillist = that.tabledata;
              console.log('表单提交数据', formData)
              httpAction(httpurl, formData, method)
                  .then(res => {
                      console.log(res);
                      if (res.success) {
                          that.$message.success(res.message)
                          that.$emit('ok')
                          that.hasaddmain = true;
                          that.$emit('close')
                      } else {
                          that.$message.warning(res.message)
                      }
                  })
                  .finally(() => {
                      that.confirmLoading = false
                      that.close()
                      that.$parent.closeRouteViewTab(this.$route.path)
                      that.$router.push({ path:'/invoice/InvoiceList' });
                  })
          }
      })
    },
    handleCancel() {
        this.close()
    },
    popupCallback(row) {
        this.form.setFieldsValue(
            pick(row, 'code', 'vendorId', 'content', 'warehouseId', 'account', 'payamount', 'totalamount')
        )
    },
    backToList() {
        // console.log(this.$route.matched);
        this.$route.matched.splice(this.$route.matched.length-1 ,1);
        this.$parent.closeRouteViewTab(this.$route.fullPath)
        this.$router.replace({ path:'/purchase/PurchaseList' });
    }
  },
  mounted() {
    if (this.$route.query.unEditable === false) {
      this.unEditable = this.$route.query.unEditable;
    }
    if (this.$route.query.sourceId) {
      this.invoice.sourceId = this.$route.query.sourceId;
      this.invoice.sourceBillType = this.$route.query.sourceBillType;
      this.invoice.sourceCode = this.$route.query.sourceCode;
      this.form.setFieldsValue({sourceId:this.$route.query.sourceId, sourceBillType:this.$route.query.sourceBillType, sourceCode: this.$route.query.sourceCode, workTypeId: this.$route.query.workTypeId})
      if (this.$route.query.sourceBillType == 0) {
        const that = this;
        getSaleOrderOne({id:this.$route.query.sourceId}).then((res) => {
          if (res.success) {
            that.addressSourceId = res.result.customerId;
          }
        })
      }
    }

    // 单据类型
    getBillTypeList().then((res) => {
      if (res.success) {
        this.billTypeList = res.result;
      }
    });
    getInvoiceTextureList().then((res) => {
      if (res.success) {
        this.invoiceTextureList = res.result;
      }
    });
    getInvoiceTypeList().then((res) => {
      if (res.success) {
        this.invoiceTypeList = res.result;
      }
    })

    const that = this;
    getInvoiceById({"id":this.$route.params.id}).then((res)=>{
      if (res.result) {
        // 收获地址
        if (this.$route.params.id) {
          getInvoiceAddress({"sourceId": this.$route.params.id}).then((res) => {
            if (res.success) {
              that.invoiceAddress = res.result;
            }
          });
        }
        this.invoice = res.result;
      }
    });
  }
}
</script>
<style scoped>
.hide{
    display: none;
}
</style>
