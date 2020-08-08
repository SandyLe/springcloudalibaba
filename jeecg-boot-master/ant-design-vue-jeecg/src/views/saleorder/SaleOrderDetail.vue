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
          <a-button v-print="'#printContent'" ghost type="primary">打印</a-button>
          <a-button
            icon="fullscreen"
            title="全屏"
            style="width:56px;height:100%;border:0"
            @click="handleClickToggleFullScreen"/>
        </div>
      </div>
    </template>
    <div id="printContent">

      <a-form style="margin: 0" :form="form">
        <a-alert
          :closable="false"
          message="产品销售订单"
          style="margin-bottom: 24px; text-align: center; font-weight: bolder"
        />
        <table style="width: 100%;">
          <a-row>
            <a-col :sm="4" :xs="4" class="titletd">订单号：</a-col><a-col :sm="8" :xs="8">{{this.saleOrder.code}}</a-col>
            <a-col :sm="4" :xs="4" class="titletd">客户：</a-col><a-col :sm="8" :xs="8">{{this.saleOrder.customer}}</a-col>
            <a-col :sm="4" :xs="4" class="titletd">单据时间：</a-col><a-col :sm="8" :xs="8">{{this.saleOrder.billDate}}</a-col>
            <a-col :sm="4" :xs="4" class="titletd">金额：</a-col><a-col :sm="8" :xs="8">{{this.saleOrder.totalamount}}</a-col>
          </a-row>
          <a-row>
            <a-col :sm="4" :xs="4" class="titletd">渠道：</a-col><a-col :sm="8" :xs="8">{{this.saleOrder.channel}}</a-col>
            <a-col :sm="4" :xs="4" class="titletd">发货时间：</a-col><a-col :sm="8" :xs="8">{{this.saleOrder.deliveryTime}}</a-col>
            <a-col :sm="4" :xs="4" class="titletd">安装时间：</a-col><a-col :sm="8" :xs="8">{{this.saleOrder.installTime}}</a-col>
            <a-col :sm="4" :xs="4" class="titletd">出库方式：</a-col><a-col :sm="8" :xs="8">{{this.saleOrder.deliveryTypeName}}</a-col>
          </a-row>
        </table>
        <div style="margin-top: 15px">
          <div class="title">订单产品</div>
          <table class="tableclass">
            <thead style="border: solid 1px #888888; ">
              <th>编号</th>
              <th>产品名称</th>
              <th>产品编码</th>
              <th>数量</th>
              <th>单位</th>
              <th>单价</th>
              <th>折扣</th>
              <th>折扣类型</th>
              <th>金额</th>
            </thead>
            <tr v-for="(item, index) in mtlDatas" style="border: dotted 1px #888888; ">
              <td>{{index +1 }}</td>
              <td>{{item.mtl}}</td>
              <td>{{item.mtlCode}}</td>
              <td>{{item.quantity}}</td>
              <td>{{item.unit}}</td>
              <td>{{item.price}}</td>
              <td>{{item.discount}}</td>
              <td>{{item.discountTypeName}}</td>
              <td>{{item.amount}}</td>
            </tr>
          </table>
        </div>
        <div style="margin-top: 15px">
          <div class="title">其他费用</div>
          <table class="tableclass">
            <thead style="border: solid 1px #888888; ">
            <th>编号</th>
            <th>费用名称</th>
            <th>费用金额</th>
            </thead>
            <tr v-for="(item, index) in expenseDatas" style="border: dotted 1px #888888; ">
              <td>{{index +1 }}</td>
              <td>{{item.expense}}</td>
              <td>{{item.amount}}</td>
            </tr>
          </table>
        </div>
        <div style="margin-top: 15px">
          <div class="title">销售成本</div>
          <table class="tableclass">
            <thead style="border: solid 1px #888888; ">
            <th>编号</th>
            <th>收款方</th>
            <th>费用名称</th>
            <th>费用金额</th>
            </thead>
            <tr v-for="(item, index) in costsDatas" style="border: dotted 1px #888888; ">
              <td>{{index +1 }}</td>
              <td>{{item.payee}}</td>
              <td>{{item.expense}}</td>
              <td>{{item.amount}}</td>
            </tr>
          </table>
        </div>
        <div style="margin-top: 15px">
          <div class="title" style="border-bottom: solid 1px #888888; ">地址信息</div>
          <div><span style="font-weight: bold">收货地址：{{saleOrderAddress.recipients}} &nbsp;</span> <a>{{saleOrderAddress.tel}}</a> {{saleOrderAddress.fullAddress}}</div>
          <div><span style="font-weight: bold">安装地址：{{workAddress.recipients}} &nbsp;</span> <a>{{workAddress.tel}}</a> {{workAddress.fullAddress}}</div>
        </div>
      </a-form>
    </div>
  </a-modal>
</template>

<script>
  import {checkout, getSaleOrderOne, getlSaleMtlList, getSaleOrderExpenseList, getDeliveryInfoBySourceId, getSaleOrderCostList, getOrderAddress, getInstallAddress } from '@/api/api'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  export default {
    name: "SaleOrderDetail",
    components: {
      JDictSelectTag,
    },
    data() {
      return {
        mtlDatas: [],
        expenseDatas: [],
        costsDatas: [],
        deliveryInfo: {},
        title: "订单详情",
        visible: false,
        form: this.$form.createForm(this),
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 18 },
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
        confirmLoading: false,
        saleOrder: {},
        saleOrderAddress: {},
        workAddress: {}
      }
    },
    methods: {
      view (record) {
        this.visible = true;
        this.saleOrder = record;
        getlSaleMtlList({sourceId:this.saleOrder.id}).then((res) => {
          if (res.success) {
            this.mtlDatas = res.result;
          }
        })
        getSaleOrderExpenseList({sourceId:this.saleOrder.id}).then((res) => {
          if (res.success) {
            this.expenseDatas = res.result;
          }
        })
        getDeliveryInfoBySourceId({sourceId:this.saleOrder.id}).then((res) => {
          if (res.success) {
            this.deliveryInfo = res.result;
          }
        })
        getSaleOrderCostList({sourceId:this.saleOrder.id}).then((res) => {
          if (res.success) {
            this.costsDatas = res.result;
          }
        })
        getOrderAddress({sourceId:this.saleOrder.id}).then((res) => {
          if (res.success) {
            this.saleOrderAddress = res.result;
          }
        })
        getInstallAddress({saleId:this.saleOrder.id}).then((res) => {
          if (res.success) {
            this.workAddress = res.result;
          }
        })
      },
      handleOk () {
      },
      handleCancel () {
        this.close()
      },
      close () {
        this.$emit('close');
        this.visible = false;
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
    },
    mounted() {
    }
  }
</script>
<style rel="stylesheet">
  .tableclass {border: solid 1px #888888; width: 100%;}
  .titletd {font-weight: bolder; text-align: right}
  .title {font-weight: bolder; padding-left: 2%;}
</style>