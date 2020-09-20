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
              <a-col :sm="4" :xs="4">出货方式：</a-col>
              <a-col :sm="8" :xs="8">{{saleOrder.deliveryTypeName}}</a-col>
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
              <th>辅助属性</th>
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
                <td>{{item.suppValueMap}}</td>
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
            <div>成本</div>
            <table class="saleordertableclass">
              <thead>
              <th>编号</th>
              <th>收款方</th>
              <th>费用名称</th>
              <th>费用金额</th>
              </thead>
              <tr v-for="(item, index) in costDatas" style="border: dotted 1px #888888; ">
                <td>{{index +1 }}</td>
                <td>{{item.payee}}</td>
                <td>{{item.expense}}</td>
                <td>{{item.amount}}</td>
              </tr>
            </table>
          </div>
          <div style="margin-top: 15px">
            <div style="border-bottom: solid 1px #888888; ">地址信息</div>
            <div><span style="font-weight: bold">收货地址：{{saleOrderAddress.recipients}} &nbsp;</span> <a>{{saleOrderAddress.tel}}</a> {{saleOrderAddress.fullAddress}}</div>
            <div><span style="font-weight: bold">安装地址：{{workAddress.recipients}} &nbsp;</span> <a>{{workAddress.tel}}</a> {{workAddress.fullAddress}}</div>
          </div>
        </div>
        <div slot="action">
          <a-button type="primary" @click="finish">完成并关闭</a-button>
          <a-button v-if="editType==1" type="primary" style="margin-left: 8px" @click="finishStockOut">完成并通知出库</a-button>
          <a-button style="margin-left: 8px" @click="toOrderList">订单列表</a-button>
          <a-button style="margin-left: 8px" v-print="'#information'" ghost type="primary">打印</a-button>
        </div>
      </result>
    </a-form>
  </div>
</template>

<script>
  import Result from '../result/Result'
  import {getSaleOrderOne, getlSaleMtlList, getSaleOrderExpenseList, getSaleOrderCostList, getDeliveryInfoBySourceId, inventoryOutSave, getOrderAddress, getInstallAddress} from '@/api/api'

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
        costDatas: [],
        mtlDatas: [],
        deliveryInfo: {},
        saleOrderAddress: {},
        workAddress: {},
        editType: 0
      }
    },
    methods: {
      finish () {
        this.$emit('finish')
      },
      finishStockOut () {
        let formData = {
          "sourceId": this.saleOrder.id,
          "sourceCode": this.saleOrder.code,
          "sourceBillType": this.saleOrder.billType
        };
        inventoryOutSave(formData).then((res)=>{
          if(res.success){
            this.$message.success(res.message);
          }else{
            this.$message.warning(res.message);
          }
        }).finally(() => {
        })
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
        getSaleOrderCostList({sourceId:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.costDatas = res.result;
          }
        })
        getDeliveryInfoBySourceId({sourceId:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.deliveryInfo = res.result;
          }
        })
        getOrderAddress({sourceId:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.saleOrderAddress = res.result;
          }
        })
        getInstallAddress({saleId:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.workAddress = res.result;
          }
        })
        this.editType = this.$route.query.editType;
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