<template>
<div>
    <a-card :bordered="false">
      <a-form :form="form">
        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item label="原单类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-if = "editType == 1" v-decorator="['sourceBillType', {}]" placeholder="请选择单据类型" showSearch optionFilterProp="children"
                        notFoundContent="没有匹配的单据类型"  >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in billTypeList" :key="key" :value="item.id">
                  {{ item.name || item.code }}
                </a-select-option>
              </a-select>
              <span v-if="editType == 0">{{invoice.sourceBillTypeName}}</span>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="12">
            <a-form-item label="原单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-if = "editType == 1" v-decorator="[ 'sourceCode', validatorRules.sourceCode]" placeholder="请输入原单编号" />
              <a-input v-decorator="[ 'sourceId', {}]" placeholder="原销售单ID" type="hidden" />
              <span v-if="editType == 0">{{invoice.sourceCode}}</span>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item label="发票类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-if = "editType == 1" v-decorator="['invoiceTypeId', {}]" placeholder="请选择发票类型" showSearch optionFilterProp="children"
                        notFoundContent="没有匹配的发票类型" @change="invoiceTypeChange" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in invoiceTypeList" :key="key" :value="item.id">
                  {{ item.name || item.code }}
                </a-select-option>
              </a-select>
              <span v-if="editType == 0">{{invoice.invoiceTypeName}}</span>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="代码">
              <a-input v-if = "editType == 1" placeholder="后台自动生成单号" :readOnly="true" v-decorator="[ 'code', validatorRules.code]" />
              <span v-if="editType == 0">{{invoice.code}}</span>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item label="材质" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-if = "editType == 1" v-decorator="['invoiceTextureId', {}]" placeholder="请选择发票类型" showSearch optionFilterProp="children"
                        notFoundContent="没有匹配的发票类型"  >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in invoiceTextureList" :key="key" :value="item.id">
                  {{ item.name || item.code }}
                </a-select-option>
              </a-select>
              <span v-if="editType == 0">{{invoice.invoiceTextureName}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="单据时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker v-if = "editType == 1" showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'billDate', {}]"/>
              <span v-if="editType == 0">{{invoice.billDate}}</span>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item label="抬头" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-if = "editType == 1" v-decorator="[ 'billTitle', validatorRules.billTitle]" placeholder="请输入抬头" />
              <span v-if="editType == 0">{{invoice.billTitle}}</span>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="12">
            <a-form-item label="金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-if = "editType == 1" v-decorator="[ 'amount', validatorRules.amount]" placeholder="请输入金额" />
              <span v-if="editType == 0">{{invoice.amount}}</span>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="税号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-if = "editType == 1" v-decorator="[ 'taxNo', validatorRules.taxNo]" placeholder="请输入税号" />
              <span v-if="editType == 0">{{invoice.taxNo}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-if = "editType == 1" v-decorator="[ 'email', validatorRules.email]" placeholder="请输入邮箱" />
              <span v-if="editType == 0">{{invoice.email}}</span>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row v-if="invoiceTypeId==1">
          <a-col :span="12">
            <a-form-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-if = "editType == 1" v-decorator="[ 'address', validatorRules.address]" placeholder="请输入地址" />
              <span v-if="editType == 0">{{invoice.address}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-if = "editType == 1" v-decorator="[ 'tel', validatorRules.tel]" placeholder="请输入电话" />
              <span v-if="editType == 0">{{invoice.tel}}</span>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row v-if="invoiceTypeId==1">
          <a-col :span="12">
            <a-form-item label="开户行" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-if = "editType == 1" v-decorator="[ 'openningBank', validatorRules.openningBank]" placeholder="请输入开户行" />
              <span v-if="editType == 0">{{invoice.openningBank}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="账号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-if = "editType == 1" v-decorator="[ 'bankNo', validatorRules.bankNo]" placeholder="请输入账号" />
              <span v-if="editType == 0">{{invoice.bankNo}}</span>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="发票号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-if = "makeInvoice == 1" v-decorator="[ 'invoiceNo', validatorRules.invoiceNo]" placeholder="请输入发票号" />
              <span v-if="editType == 0">{{invoice.invoiceNo}}</span>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-card class="card" title="寄送地址" :bordered="true">
              寄送地址：<span style="font-weight: bold">{{invoiceAddress.recipients}} &nbsp;</span> <a>{{invoiceAddress.tel}}</a> {{invoiceAddress.fullAddress}}
              <a v-if = "editType == 1" @click="editAddressItem(invoice)"><a-icon type="setting"/> 请选择收获地址</a>
            </a-card>
          </a-col>
        </a-row>
      </a-form>
      <sale-address-list ref="saleAddressList" v-on:addressFlag="addressOK"></sale-address-list>
    </a-card>
    <footer-tool-bar>
        <a-button v-if = "makeInvoice == 1" type="primary" @click="createInvoice">登记开票</a-button>
        <a-button v-if = "editType == 1" type="primary" @click="handleOk">保存</a-button>
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
  addInvoice,
  createInvoice
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
      makeInvoice: 0,
      editType: 0,
      dateFormat:"YYYY-MM-DD HH:mm:ss",
      invoiceAddress: {},
      invoice: {},
      billTypeList: [],
      invoiceTextureList: [],
      invoiceTypeList: [],
      invoiceTypeId: 0,
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
        invoiceNo: {
          rules: [{
            required: true,
            message: '请输入发票号!'
          }]
        },
        amount: {},
        description: {},
        states: {}
      },
      url: {
        add: '/invoice/add',
        edit: '/invoice/edit',
        save: '/invoice/save'
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

      const that = this
      this.form.validateFields((err, values) => {
        if (!err) {
          let httpurl = this.url.save
          let method = 'post'
          let formData = Object.assign(this.model, values)
          formData.billDate = values.billDate.format(this.dateFormat)
          httpAction(httpurl, formData, method)
            .then(res => {
              console.log(res);
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
                that.model = res.result;
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {/*
              that.confirmLoading = false
              that.close()
              that.$parent.closeRouteViewTab(this.$route.path)
              that.$router.push({ path:'/invoice/invoiceList' });*/
            })
        }
      })

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
      delete this.validatorRules.invoiceNo;
      const that = this
      // 触发表单验证

      this.form.validateFields((err, values) => {
          if (!err) {
            let httpurl = this.url.save
            let method = 'post'
            let formData = Object.assign(this.model, values)
            formData.billDate = values.billDate.format(this.dateFormat)
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
                    that.$router.push({ path:'/saleorder/SaleOrder' });
                })
          }
      })
    },
    createInvoice() {
      const that = this
      this.form.validateFields((err, values) => {
        if (!err) {
          let formData = Object.assign(this.model, values)
          createInvoice(formData)
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
              that.$router.push({ path:'/invoice/list' });
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
        this.$router.replace({ path:'/saleorder/SaleOrder' });
    },
    invoiceTypeChange (val) {
      this.invoiceTypeId = val;
    }
  },
  mounted() {
    this.editType = this.$route.query.editType;
    this.makeInvoice = this.$route.query.makeInvoice;
    if (this.$route.query.unEditable === false) {
      this.unEditable = this.$route.query.unEditable;
    }
    if (this.$route.query.sourceId) {
      this.invoice.sourceId = this.$route.query.sourceId;
      this.invoice.sourceBillType = this.$route.query.sourceBillType;
      this.invoice.sourceCode = this.$route.query.sourceCode;

      if (this.$route.query.sourceBillType == 0) {
        const that = this;
        getSaleOrderOne({id:this.$route.query.sourceId}).then((res) => {
          if (res.success) {
            that.addressSourceId = res.result.customerId;
            that.form.setFieldsValue({sourceId:that.$route.query.sourceId, sourceBillType:that.$route.query.sourceBillType, sourceCode: that.$route.query.sourceCode})
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
        this.model.id = this.invoice.id;
        this.invoiceTypeId = this.invoice.invoiceTypeId;
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
