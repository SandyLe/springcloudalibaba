<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <detail-list title="基本信息">
        <detail-list-item term="客户名称">{{customer.name}}</detail-list-item>
        <detail-list-item term="客户编码">{{customer.code}}</detail-list-item>
        <detail-list-item term="状态">{{customer.rowSts}}2</detail-list-item>
        <detail-list-item term="客户类型">{{customer.customerTypeId}}</detail-list-item>
        <detail-list-item term="客户来源">{{customer.customerSourceId}}</detail-list-item>
        <detail-list-item term="性别">{{customer.gender}}</detail-list-item>
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
      </detail-list>
      <a-divider style="margin-bottom: 32px"/>


      <div class="title">退货商品</div>

      <a-table
        :size="size"
        :bordered="bordered"
        :loading="loading"
        :columns="columns"
        :dataSource="current"
        :rowKey="rowKey"
        :pagination="pagination">
        <!--:rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: updateSelect }"-->
      <!--&gt;-->
      </a-table>

      <div class="title">退货进度</div>
    </a-card>
  </page-layout>
</template>

<script>
  import PageLayout from '@/components/page/PageLayout'
  import DetailList from '@/components/tools/DetailList'
  import {getCustomerTypeList, getCustomerOne} from '@/api/api'
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
        current:[]
      }
    },
    computed: {
      title () {
        return this.$route.meta.title
      }
    },
    watch: {

      '$route' (to, from) {
        console.log(this.$route.query.id)
        if (this.$route.query.id) {
          getCustomerOne({id:this.$route.query.id}).then((res) => {
            if (res.success) {
              this.customer = res.result;
              debugger
            }
          })
          getCustomerTypeList().then((res) => {
            if (res.success) {
              this.current = res.result;
            }
          })
        }
      }
    },
    create: {
      '$route' (to, from) {
        console.log(this.$route.query.id)
        debugger
        if (this.$route.query.id) {
          getCustomerOne({id:this.$route.query.id}).then((res) => {
            if (res.success) {
              this.customer = res.result;
              debugger
            }
          })

          getCustomerTypeList().then((res) => {
            if (res.success) {
              this.current = res.result;
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
            debugger
          }
        })

        getCustomerTypeList().then((res) => {
          if (res.success) {
            this.current = res.result;
            this.loading = false;
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
