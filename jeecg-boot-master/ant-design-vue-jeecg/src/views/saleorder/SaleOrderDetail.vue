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
          <tr>
            <td class="titletd">订单号：</td><td>{{this.saleOrder.code}}</td>
            <td class="titletd">客户：</td><td>{{this.saleOrder.customer}}</td>
            <td class="titletd">单据时间：</td><td>{{this.saleOrder.billDate}}</td>
            <td class="titletd">金额：</td><td>{{this.saleOrder.totalamount}}</td>
          </tr>
          <tr>
            <td class="titletd">渠道：</td><td>{{this.saleOrder.channel}}</td>
            <td class="titletd">发货时间：</td><td>{{this.saleOrder.deliveryTime}}</td>
            <td class="titletd">安装时间：</td><td>{{this.saleOrder.installTime}}</td>
            <td class="titletd">出货仓库：</td><td>{{this.saleOrder.warehouse}}</td>
          </tr>
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
          <div class="title" style="border-bottom: solid 1px #888888; ">收货信息</div>
          <div><label class="title">默认发货方式：</label>{{deliveryInfo.cdiDefaultTypeName}}</div>
          <div v-if="'ZHIDINGDIAN' === deliveryInfo.cdiDefaultType">
            <label class="title">说明：</label>{{deliveryInfo.cdiDescription}}<br/>
            <label class="title">联系人：</label>{{deliveryInfo.cdiLinkman}}<br/>
            <label class="title">联系电话：</label>{{deliveryInfo.cdiPhone}}<br/>
          </div>
          <div v-if="'SONGCHE' === deliveryInfo.cdiDefaultType">
            <label class="title">车牌：</label>{{deliveryInfo.cdiCarLicense}}<br/>
            <label class="title">司机姓名：</label>{{deliveryInfo.cdiLinkman}}<br/>
            <label class="title">司机电话：</label>{{deliveryInfo.cdiPhone}}<br/>
            <label class="title">发货地址：</label>{{deliveryInfo.cdiDeliveryAddress}}<br/>
            <label class="title">收件人：</label>{{deliveryInfo.cdiRecipients}}<br/>
            <label class="title">联系电话：</label>{{deliveryInfo.cdiRecipientsPhone}}<br/>>
            <label class="title">地址：</label>{{deliveryInfo.cdiProvince + deliveryInfo.cdiCity + deliveryInfo.cdiDistrict + deliveryInfo.cdiAddress}}<br/>
          </div>
          <div v-if="'WULIU' === deliveryInfo.cdiDefaultType">
            <label class="title">物流：</label>{{deliveryInfo.cdiLogistics}}<br/>
            <label class="title">网点：</label>{{deliveryInfo.cdiBranch}}<br/>
            <label class="title">电话：</label>{{deliveryInfo.cdiTel}}<br/>
            <label class="title">收件人：</label>{{deliveryInfo.cdiRecipients}}<br/>
            <label class="title">联系电话：</label>{{deliveryInfo.cdiRecipientsPhone}}<br/>
            <label class="title">地址：</label>{{deliveryInfo.cdiProvince + deliveryInfo.cdiCity + deliveryInfo.cdiDistrict + deliveryInfo.cdiAddress}}<br/>
          </div>
        </div>
      </a-form>
    </div>
  </a-modal>
</template>

<script>
  import {checkout, getSaleOrderOne, getlSaleMtlList, getSaleOrderExpenseList, getDeliveryInfoBySourceId } from '@/api/api'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  export default {
    name: "Step2",
    components: {
      JDictSelectTag,
    },
    data() {
      return {
        mtlDatas: [],
        expenseDatas: [],
        deliveryInfo: {},
        title: "订单详情",
        visible: false,
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
        cdiDefaultType: ''
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