<template>
  <div>
    <a-form style="margin: 40px auto 0;">
      <result title="操作成功" :is-success="true" description="销售订单录入完成，请在库存管理中销售出库完成出库操作">
        <div class="information">
          <a-row>
            <a-col :sm="8" :xs="24">销售单号：</a-col>
            <a-col :sm="16" :xs="24">{{saleOrder.code}}</a-col>
          </a-row>
          <a-row>
            <a-col :sm="8" :xs="24">客户名称：</a-col>
            <a-col :sm="16" :xs="24">{{saleOrder.customer}}</a-col>
          </a-row>
          <a-row>
            <a-col :sm="8" :xs="24">出货仓库：</a-col>
            <a-col :sm="16" :xs="24">{{saleOrder.warehouse}}</a-col>
          </a-row>
          <a-row>
            <a-col :sm="8" :xs="24">订单金额：</a-col>
            <a-col :sm="16" :xs="24"><span class="money">{{saleOrder.totalamount}}</span> 元</a-col>
          </a-row>
        </div>
        <div slot="action">
          <a-button type="primary" @click="finish">完成</a-button>
          <a-button style="margin-left: 8px" @click="toOrderList">订单列表</a-button>
        </div>
      </result>
    </a-form>
  </div>
</template>

<script>
  import Result from '../result/Result'
  import {getSaleOrderOne } from '@/api/api'

  export default {
    name: "Step3",
    components: {
      Result
    },
    data () {
      return {
        saleOrder: {},
        loading: false
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
</style>