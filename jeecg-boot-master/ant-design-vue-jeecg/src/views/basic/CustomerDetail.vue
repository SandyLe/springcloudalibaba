<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <detail-list title="基本信息">
        <a-row>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="客户名称">{{customer.name}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="客户编码">{{customer.code}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">{{customer.rowStsName}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="客户类型">{{customer.customerType}} </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="客户来源">{{customer.customerSource}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="性别">{{customer.genderName}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="客户昵称">{{customer.nickName}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="生日">{{customer.birthday}} </a-form-item>
          </a-col>
        </a-row>
      </detail-list>
      <a-divider style="margin-bottom: 32px"/>
      <detail-list title="联系信息">
        <a-row>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="联系电话">{{customer.tel}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="手机">{{customer.phone}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="邮箱">{{customer.email}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="传真">{{customer.fax}} </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="联系人">{{customer.linkman}} </a-form-item>
          </a-col>
        </a-row>
      </detail-list>
      <a-divider style="margin-bottom: 32px"/>
      <detail-list title="功能地址">
        <div class="table-operator" style="border-top: 5px">
          <a-button @click="handleAddAddress" type="primary" icon="plus">添加地址</a-button>
        </div>
        <a-table
          ref="table"
          bordered
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          @change="handleTableChange">
           <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical"/>
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
              <a>删除</a>
            </a-popconfirm>
          </span>
        </a-table>
      </detail-list>
      <detail-list title="开票信息">
        <a-row>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="抬头">{{customer.billTitle}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="税号">{{customer.taxNo}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="银行账号">{{customer.bankaccount}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="银行名称">{{customer.bankName}} </a-form-item>
          </a-col>
        </a-row>
      </detail-list>
      <a-divider style="margin-bottom: 32px"/>
      <detail-list title="会员信息">
        <a-row>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="会员昵称">{{customer.memberNickName}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="微商城会员">{{customer.memberNo}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="会员手机">{{customer.memberPhone}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="折扣">{{customer.discount}} / {{customer.discountType}} </a-form-item>
          </a-col>
        </a-row>
      </detail-list>
      <a-divider style="margin-bottom: 32px"/>
      <address-modal ref="addressModal" :entity = "entity" @ok="modalFormOk"></address-modal>
    </a-card>
  </page-layout>
</template>

<script>
  import PageLayout from '@/components/page/PageLayout'
  import DetailList from '@/components/tools/DetailList'
  import AddressModal from './AddressModal'
  import { deleteAction, postAction, getAction } from '@/api/manage'
  import { getFullAddress } from '@/utils/commonUtil'
  import {getCustomerOne, getAreaOne} from '@/api/api'
  import ARow from "ant-design-vue/es/grid/Row";
  const DetailListItem = DetailList.Item

  export default {
    components: {
      ARow,
      PageLayout,
      DetailList,
      DetailListItem,
      AddressModal
    },
    data () {
      return {
        entity: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        customer: {},
        /**
         * 表格大小风格，default, middle, small
         */
        size: 'default',
        loading: true,
        bordered: false,
        rowKey: 'id',
        ipagination: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        columns: [{
            title: '功能类型',
            align: 'center',
            dataIndex: 'typeName',
            width: 60
          },
          {
            title: '收件人',
            align: 'center',
            dataIndex: 'recipients',
            width: 60
          },
          {
            title: '电话',
            align: 'center',
            width: 60,
            dataIndex: 'tel'
          },
          {
            title: '详细地址',
            align: 'center',
            width: 150,
            dataIndex: 'fullAddress'
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
            align: 'center',
            width: 120
          }],
        url: {
          list: '/address/getPage',
          delete: '/address/delete',
          deleteBatch: '/address/deleteBatch',
        },
        dataSource: []
      }
    },
    computed: {
      title () {
        return this.$route.meta.title
      }
    },
    methods: {

      handleDelete: function(id) {
        if (!this.url.delete) {
          this.$message.error('请设置url.delete3属性!')
          return
        }
        var that = this
        deleteAction(that.url.delete, { id: id }).then((res) => {
          if (res.success) {
            that.$message.success(res.message)
            that.loadData();
          } else {
            that.$message.warning(res.message)
          }
        })
      },
      modalFormOk() {
        // 新增/修改 成功时，重载列表
        this.loadData()
      },
      loadData(arg) {
        if (!this.url.list) {
          this.$message.error('请设置url.list属性!')
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1
        }
        let params = this.getQueryParams()//查询条件
        params.roleId = this.currentRoleId
        this.loading = true
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total

          }
          this.loading = false
        })

      },
      getQueryParams() {
        //获取查询条件
        let param = {};
        if(this.$route.query.id){
          param.sourceId = this.$route.query.id
        }else{
          param.sourceId = -1;
        }
        param.field = this.getQueryField()
        param.pageNo = this.ipagination.current
        param.pageSize = this.ipagination.pageSize
        return param;
      },
      handleAddAddress(){
        this.$refs.addressModal.add();
        this.$refs.addressModal.title = "新增功能地址";
      },
      getQueryField() {
        var str = 'id,'
        this.columns.forEach(function(value) {
          str += ',' + value.dataIndex
        })
        return str
      },
      handleEdit: function(record) {
        this.$refs.addressModal.title = '编辑'
        this.$refs.addressModal.edit(record)
      },
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
          }
        });

        this.loadData();
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
