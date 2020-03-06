<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <detail-list title="基本信息">
        <detail-list-item term="客户名称">{{customer.name}}</detail-list-item>
        <detail-list-item term="客户编码">{{customer.code}}</detail-list-item>
        <detail-list-item term="状态">{{customer.rowStsName}}</detail-list-item>
        <detail-list-item term="客户类型">{{customer.customerType}}</detail-list-item>
        <detail-list-item term="客户来源">{{customer.customerSource}}</detail-list-item>
        <detail-list-item term="性别">{{customer.genderName}}</detail-list-item>
      </detail-list>
      <a-divider style="margin-bottom: 32px"/>
      <detail-list title="其他信息">
        <detail-list-item term="客户昵称">{{customer.nickName}}</detail-list-item>
        <detail-list-item term="生日">{{customer.birthday}}</detail-list-item>
        <detail-list-item term="联系人">{{customer.linkman}}</detail-list-item>
        <detail-list-item term="联系电话">{{customer.tel}}</detail-list-item>
        <detail-list-item term="手机">	{{customer.phone}}</detail-list-item>
        <detail-list-item term="邮箱">	{{customer.email}}</detail-list-item>
        <detail-list-item term="传真">	{{customer.fax}}</detail-list-item>
        <detail-list-item term="地址">	{{customer.fullAddress}}</detail-list-item>
      </detail-list>
      <a-divider style="margin-bottom: 32px"/>
      <detail-list title="会员信息">
        <detail-list-item term="会员昵称">{{customer.memberNickName}}</detail-list-item>
        <detail-list-item term="微商城会员">{{customer.memberNo}}</detail-list-item>
        <detail-list-item term="会员昵称">{{customer.memberNickName}}</detail-list-item>
        <detail-list-item term="会员手机">{{customer.memberPhone}}</detail-list-item>
        <detail-list-item term="折扣">	{{customer.discount}} / {{customer.discountType}}</detail-list-item>
        <detail-list-item term="银行账号">	{{customer.bankaccount}}</detail-list-item>
        <detail-list-item term="银行账户">	{{customer.bankacctName}}</detail-list-item>
        <detail-list-item term="银行名称">	{{customer.bankName}}</detail-list-item>
        <detail-list-item term="开票信息">	{{customer.billingInfo}}</detail-list-item>
      </detail-list>
      <a-divider style="margin-bottom: 32px"/>


      <div class="title">收货信息</div>

      <detail-list :title="cdiDeliveryInfo.cdiDefaultTypeName" v-if="cdiDeliveryInfo.cdiDefaultType === 'ZHIDINGDIAN'">
        <detail-list-item term="说明">	{{cdiDeliveryInfo.cdiDescription}}</detail-list-item>
        <detail-list-item term="联系人">	{{cdiDeliveryInfo.cdiLinkman}}</detail-list-item>
        <detail-list-item term="联系电话">	{{cdiDeliveryInfo.cdiPhone}}</detail-list-item>
      </detail-list>
      <detail-list :title="cdiDeliveryInfo.cdiDefaultTypeName" v-if="cdiDeliveryInfo.cdiDefaultType === 'SONGCHE'">
        <detail-list-item term="车牌号">	{{cdiDeliveryInfo.cdiCarLicense}}</detail-list-item>
        <detail-list-item term="司机姓名">	{{cdiDeliveryInfo.cdiLinkman}}</detail-list-item>
        <detail-list-item term="司机电话">	{{cdiDeliveryInfo.cdiPhone}}</detail-list-item>
        <detail-list-item term="发货地址">	{{cdiDeliveryInfo.cdiDeliveryAddress}}</detail-list-item>
        <detail-list-item term="收件人">	{{cdiDeliveryInfo.cdiRecipients}}</detail-list-item>
        <detail-list-item term="联系电话">	{{cdiDeliveryInfo.cdiRecipientsPhone}}</detail-list-item>
        <detail-list-item term="详细地址">	{{cdiDeliveryInfo.cdiFullAddress}}</detail-list-item>
      </detail-list>
      <detail-list :title="cdiDeliveryInfo.cdiDefaultTypeName" v-if="cdiDeliveryInfo.cdiDefaultType === 'WULIU'">
        <detail-list-item term="物流">	{{cdiDeliveryInfo.cdiLogisticsName}}</detail-list-item>
        <detail-list-item term="网点">	{{cdiDeliveryInfo.cdiBranch}}</detail-list-item>
        <detail-list-item term="电话">	{{cdiDeliveryInfo.cdiTel}}</detail-list-item>
        <detail-list-item term="收件人">	{{cdiDeliveryInfo.cdiRecipients}}</detail-list-item>
        <detail-list-item term="联系电话">	{{cdiDeliveryInfo.cdiRecipientsPhone}}</detail-list-item>
        <detail-list-item term="详细地址">	{{cdiDeliveryInfo.cdiFullAddress}}</detail-list-item>
      </detail-list>
      <detail-list :title="cdiDeliveryInfo.cdiDefaultTypeName" v-if="cdiDeliveryInfo.cdiDefaultType === 'ZITI'">
      </detail-list>
      <detail-list :title="cdiDeliveryInfo.cdiDefaultTypeName" v-if="cdiDeliveryInfo.cdiDefaultType === 'DIANZIMIANDAN'">
      </detail-list>

      <!--<div class="title">退货进度</div>-->
    </a-card>
  </page-layout>
</template>

<script>
  import PageLayout from '@/components/page/PageLayout'
  import DetailList from '@/components/tools/DetailList'
  import { getFullAddress } from '@/utils/commonUtil'
  import {getDeliveryInfo, getCustomerOne, getAreaOne} from '@/api/api'
  const DetailListItem = DetailList.Item

  export default {
    components: {
      PageLayout,
      DetailList,
      DetailListItem
    },
    data () {
      return {
        customer: {},
        /**
         * 表格大小风格，default, middle, small
         */
        size: 'default',
        loading: true,
        bordered: false,
        rowKey: 'id',
        pagination: {},
        columns: [
          {
            title: '商品编号',
            dataIndex: 'id',
            key: 'id'
          }
        ],
        current:[],
        cdiDeliveryInfo: {}
      }
    },
    computed: {
      title () {
        return this.$route.meta.title
      }
    },
    methods: {
      getFullAddress1 (provinceId, cityId, districtId, address) {
        return getFullAddress(provinceId, cityId, districtId, address)
      }
    },
    watch: {

      '$route' (to, from) {
        console.log(this.$route.query.id)
        if (this.$route.query.id) {
          getCustomerOne({id:this.$route.query.id}).then((res) => {
            if (res.success) {
              this.customer = res.result;
              this.customer.address = this.getFullAddress(this.customer.province,this.customer.city,this.customer.district,this.customer.address);
            }
          })
          getDeliveryInfo({cdiSourceId:this.model.id}).then((res) => {
            if (res.success) {
              this.cdiDeliveryInfo = res.result;
            }
          })
        }
      }
    },
    create: {
      '$route' (to, from) {
        console.log(this.$route.query.id)

        if (this.$route.query.id) {
          getCustomerOne({id:this.$route.query.id}).then((res) => {
            if (res.success) {
              this.customer = res.result;
              this.customer.address = this.getFullAddress(this.customer.province,this.customer.city,this.customer.district,this.customer.address);
            }
          })

          getDeliveryInfo({cdiSourceId:this.$route.query.id}).then((res) => {
            if (res.success) {
              this.cdiDeliveryInfo = res.result;
            }
          })
        }
      }
    },
    mounted() {
      if (this.$route.query.id) {
        getCustomerOne({id:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.customer = res.result;
            const address = this.getFullAddress1(this.customer.province,this.customer.city,this.customer.district,this.customer.address);
            console.log(address)
          }
        })

        getDeliveryInfo({cdiSourceId:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.cdiDeliveryInfo = res.result;
          }
        })
      }
    }

  }
</script>

<style lang="scss" scoped>
  .title {
    color: rgba(0,0,0,.85);
    font-size: 16px;
    font-weight: 500;
    margin-bottom: 16px;
  }
</style>
