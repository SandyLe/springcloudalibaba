<template>
  <div>
    <a-form style="margin: 40px auto 0;">
      <result title="操作成功" :is-success="true" description="销售订单录入完成，请在库存管理中销售出库完成出库操作">
        <div class="information" id="information">
          <a-alert
            :closable="false"
            message="产品销售订单"
            style="margin-bottom: 24px; text-align: center; font-weight: bolder"
          />
          <table class="saleordertableclass">
            <a-row>
              <a-col :sm="4" :xs="4">销售单号：</a-col>
              <a-col :sm="8" :xs="8">{{saleOrder.code}}</a-col>
              <a-col :sm="4" :xs="4">单据时间：</a-col>
              <a-col :sm="8" :xs="8">{{this.saleOrder.billDate}}</a-col>
            </a-row>
            <a-row>
              <a-col :sm="4" :xs="4">客户名称：</a-col>
              <a-col :sm="8" :xs="8">{{saleOrder.customer}}</a-col>
              <a-col :sm="4" :xs="4">渠道：</a-col>
              <a-col :sm="8" :xs="8">{{this.saleOrder.channel}}</a-col>
            </a-row>
            <a-row>
              <a-col :sm="4" :xs="4">出货仓库：</a-col>
              <a-col :sm="8" :xs="8">{{saleOrder.warehouse}}</a-col>
              <a-col :sm="4" :xs="4">发货时间：</a-col>
              <a-col :sm="8" :xs="8">{{this.saleOrder.deliveryTime}}</a-col>
            </a-row>
            <a-row>
              <a-col :sm="4" :xs="4">安装时间：</a-col>
              <a-col :sm="8" :xs="8">{{this.saleOrder.installTime}}</a-col>
              <a-col :sm="4" :xs="4">订单金额：</a-col>
              <a-col :sm="8" :xs="8"><span class="money">{{saleOrder.totalamount}}</span> 元</a-col>
            </a-row>
          </table>

          <div style="margin-top: 15px">
            <div>订单产品</div>
            <table class="saleordertableclass">
              <thead>
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
            <div>其他费用</div>
            <table class="saleordertableclass">
              <thead>
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
            <div style="border-bottom: dotted 1px #888888; ">收货信息</div>
            <div><label>默认发货方式：</label>{{deliveryInfo.cdiDefaultTypeName}}</div>
            <div v-if="'ZHIDINGDIAN' === deliveryInfo.cdiDefaultType">
              <label>说明：</label>{{deliveryInfo.cdiDescription}}<br/>
              <label>联系人：</label>{{deliveryInfo.cdiLinkman}}<br/>
              <label>联系电话：</label>{{deliveryInfo.cdiPhone}}<br/>
            </div>
            <div v-if="'SONGCHE' === deliveryInfo.cdiDefaultType">
              <label>车牌：</label>{{deliveryInfo.cdiCarLicense}}<br/>
              <label>司机姓名：</label>{{deliveryInfo.cdiLinkman}}<br/>
              <label>司机电话：</label>{{deliveryInfo.cdiPhone}}<br/>
              <label>发货地址：</label>{{deliveryInfo.cdiDeliveryAddress}}<br/>
              <label>收件人：</label>{{deliveryInfo.cdiRecipients}}<br/>
              <label>联系电话：</label>{{deliveryInfo.cdiRecipientsPhone}}<br/>>
              <label>地址：</label>{{deliveryInfo.cdiProvince + deliveryInfo.cdiCity + deliveryInfo.cdiDistrict + deliveryInfo.cdiAddress}}<br/>
            </div>
            <div v-if="'WULIU' === deliveryInfo.cdiDefaultType">
              <label>物流：</label>{{deliveryInfo.cdiLogisticsName}}<br/>
              <label>网点：</label>{{deliveryInfo.cdiBranch}}<br/>
              <label>电话：</label>{{deliveryInfo.cdiTel}}<br/>
              <label>收件人：</label>{{deliveryInfo.cdiRecipients}}<br/>
              <label>联系电话：</label>{{deliveryInfo.cdiRecipientsPhone}}<br/>
              <label>地址：</label>{{deliveryInfo.cdiProvince + deliveryInfo.cdiCity + deliveryInfo.cdiDistrict + deliveryInfo.cdiAddress}}<br/>
            </div>
          </div>
        </div>
        <div slot="action">
          <a-button type="primary" @click="finish">完成</a-button>
          <a-button style="margin-left: 8px" @click="toOrderList">订单列表</a-button>
          <a-button style="margin-left: 8px" v-print="'#information'" ghost type="primary">打印</a-button>
        </div>
      </result>
    </a-form>
  </div>
</template>

<script>
  import Result from '../result/Result'
  import {getSaleOrderOne, getlSaleMtlList, getSaleOrderExpenseList, getDeliveryInfoBySourceId} from '@/api/api'

  export default {
    name: "Step3",
    components: {
      Result
    },
    data () {
      return {
        saleOrder: {},
        loading: false,
        expenseDatas: [],
        mtlDatas: [],
        deliveryInfo: {}
      }
    },
    methods: {
      finish () {
        this.$emit('finish')
      },
      toOrderList () {
        this.$router.push('/saleorder/SaleOrder')
        this.$parent.$parent.closeRouteViewTab(this.$route.fullPath)
      }
    },
    mounted() {
      if (this.$route.query.id) {
        getSaleOrderOne({id:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.saleOrder = res.result;
          }
        })
        getlSaleMtlList({sourceId:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.mtlDatas = res.result;
          }
        })
        getSaleOrderExpenseList({sourceId:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.expenseDatas = res.result;
          }
        })
        getDeliveryInfoBySourceId({sourceId:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.deliveryInfo = res.result;
          }
        })
      }
    }
  }
</script>
<style lang="scss" scoped>
  .information {
    line-height: 22px;

    .ant-row:not(:last-child) {
      margin-bottom: 24px;
    }
  }
  .money {
    font-family: "Helvetica Neue",sans-serif;
    font-weight: 500;
    font-size: 20px;
    line-height: 14px;
  }
  .saleordertableclass{
    width: 100%;
  }
</style>