<template>
  <a-card :bordered="false" class="card-area">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="12">
            <a-form-item label="原单编号">
              <j-input placeholder="输入原单编号查询" v-model="queryParam.sourceBillCode"></j-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="12">
            <a-form-item label="编号">
              <j-input placeholder="输入单号模糊查询" v-model="queryParam.code"></j-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator" style="border-top: 5px">
      <a-button @click="handleAdd" type="primary" icon="plus">添加收款单</a-button>

      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作 <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;<a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项&nbsp;&nbsp;
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
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
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <template slot="avatarslot" slot-scope="text, record, index">
          <div class="anty-img-wrap">
            <a-avatar shape="square" :src="getAvatarView(record.avatar)" icon="user"/>
          </div>
        </template>

        <span slot="nameAction" slot-scope="text, record">
              <a-tooltip placement="topLeft">
                <template slot="title">
                  <span>{{record.code}}</span>
                </template>
                <a @click="goDetail(record.id)">{{record.code}}</a>
              </a-tooltip>
            </span>
        <span slot="sourceAction" slot-scope="text, record">
              <a-tooltip placement="topLeft">
                <template slot="title">
                  <span>{{record.sourceBillCode}}</span>
                </template>
                <a @click="goSourceDetail(record.sourceBillType, record.sourceId)">{{record.sourceBillCode}}</a>
              </a-tooltip>
        </span>
        <span slot="action" slot-scope="text, record">
          <span v-if="record.billStatusId != 2"><a @click="gathering(record)">收款</a></span>
          <a-divider type="vertical"/>
          <a href="javascript:;" @click="handleDetail(record)">收款明细</a>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <receipt-modal ref="modalForm" @ok="modalFormOk"></receipt-modal>
    <receipt-order-dtl-modal ref="receiptOrderDtlModal" :sourceOrder = "selectedOrder" @ok="modalFormOk4"></receipt-order-dtl-modal>
    <receipt-dtl-log-modal ref="receiptDtlLogModal" :receiptDtlLogs="receiptDtlLogs" @ok="modalFormOk"></receipt-dtl-log-modal>
  </a-card>
</template>

<script>
  import ReceiptModal from './ReceiptOrderModal'
  import ReceiptOrderDtlModal from './ReceiptOrderDtlModal'
  import ReceiptDtlLogModal from './ReceiptDtlLogModal'
  import JInput from '@/components/jeecg/JInput'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getAction } from '@/api/manage'
  import {getReceiptOrderDtlList} from '@/api/api'
  export default {
    name: "ReceiptList",
    mixins: [JeecgListMixin],
    components: {
      JInput,
      ReceiptModal,
      ReceiptOrderDtlModal,
      ReceiptDtlLogModal
    },
    data() {
      return {
        queryParam: {},
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title: '收款单编号',
            align: "center",
            dataIndex: '',
            scopedSlots: { customRender: 'nameAction' }
          },
          {
            title: '原单类型',
            align: "center",
            dataIndex: 'sourceBillTypeName'
          },
          {
            title: '原单编号',
            align: "center",
            dataIndex: '',
            scopedSlots: { customRender: 'sourceAction' }
          },
          {
            title: '付款方',
            align:"center",
            dataIndex: 'payerName'
          },
          {
            title: '金额',
            align: "center",
            dataIndex: 'amount'
          },
          {
            title: '单据时间',
            dataIndex: 'billDate',
            align:"center",
            sorter: true
          },
          {
            title: '业务员',
            dataIndex: 'salemanName',
            align:"center",
            sorter: true
          },
          {
            title: '状态',
            align: "center",
            dataIndex: 'billStatusName'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/receiptOrder/getPage",
          delete: "/receiptOrder/delete",
          deleteBatch: "/receiptOrder/deleteBatch"
        },
        selectedOrder: null,
        receiptDtlLogs: []
      }
    },
    methods: {
      gathering (record) {
        this.selectedOrder = record;
        this.$refs.receiptOrderDtlModal.title = '新增收款明细'
        const dtl = {
          payDate : new Date(),
          sourceCode : record.code,
          sourceBillCode : record.sourceBillCode,
          sourceBillId : record.sourceId,
          sourceBillType : record.sourceBillType,
          sourceId : record.id
        }
        this.$refs.receiptOrderDtlModal.edit(dtl)
      },
      handleDetail (record) {
        this.receiptDtlLogs = [];
        if (record.id && record.sourceId) {
          getReceiptOrderDtlList({sourceId: record.id}).then((res) => {
            if (res.success) {
              this.receiptDtlLogs = res.result;
            }
          })
        }
        this.$refs.receiptDtlLogModal.edit(record);
        this.$refs.receiptDtlLogModal.title = "收款记录";
      },
      modalFormOk4 () {
        this.loadData()
      },
      getQueryParams() {
        //获取查询条件
        let param = {};
        /*if(this.$route.query.id){
          param.sourceBillId = this.$route.query.id
        }else{
          param.sourceBillId = -1;
        }*/
        param.field = this.getQueryField()
        param.pageNo = this.ipagination.current
        param.pageSize = this.ipagination.pageSize
        return param;
      },
      getQueryField() {
        var str = 'id,'
        this.columns.forEach(function(value) {
          str += ',' + value.dataIndex
        })
        return str
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
          if (res.success) {debugger
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
          }
          this.loading = false
        })

      }
    },
    mounted() {
    }
  }
</script>

<style scoped>

</style>