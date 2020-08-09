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
    <!--<div class="table-operator" style="border-top: 5px">
      <a-button @click="handleAdd" type="primary" icon="plus">添加退款单</a-button>

      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作 <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>-->

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
          <span v-if="record.billStatusId != 2"><a @click="gathering(record)">退款</a></span>
          <a-divider type="vertical"/>
          <a href="javascript:;" @click="handleDetail(record)">退款明细</a>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <refund-modal ref="modalForm" @ok="modalFormOk"></refund-modal>
    <refund-order-dtl-modal ref="refundOrderDtlModal" :sourceOrder = "selectedOrder" @ok="modalFormOk4"></refund-order-dtl-modal>
    <refund-dtl-log-modal ref="refundDtlLogModal" :refundDtlLogs="refundDtlLogs" @ok="modalFormOk"></refund-dtl-log-modal>
  </a-card>
</template>

<script>
  import RefundModal from './RefundOrderModal'
  import RefundOrderDtlModal from './RefundOrderDtlModal'
  import RefundDtlLogModal from './RefundDtlLogModal'
  import JInput from '@/components/jeecg/JInput'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getAction } from '@/api/manage'
  import {getRefundOrderDtlList} from '@/api/api'
  export default {
    name: "RefundList",
    mixins: [JeecgListMixin],
    components: {
      JInput,
      RefundModal,
      RefundOrderDtlModal,
      RefundDtlLogModal
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
            title: '退款单编号',
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
            title: '收款方',
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
          list: "/refundOrder/getPage",
          delete: "/refundOrder/delete",
          deleteBatch: "/refundOrder/deleteBatch"
        },
        isorter: {
          column: 'createTime',
          order: 'desc'
        },
        selectedOrder: null,
        refundDtlLogs: []
      }
    },
    methods: {
      gathering (record) {
        this.selectedOrder = record;
        this.$refs.refundOrderDtlModal.title = '新增退款明细'
        const dtl = {
          payDate : new Date(),
          sourceCode : record.code,
          sourceBillCode : record.sourceBillCode,
          sourceBillId : record.sourceId,
          sourceBillType : record.sourceBillType,
          sourceId : record.id
        }
        this.$refs.refundOrderDtlModal.edit(dtl)
      },
      handleDetail (record) {
        this.refundDtlLogs = [];
        if (record.id && record.sourceId) {
          getRefundOrderDtlList({sourceId: record.id}).then((res) => {
            if (res.success) {
              this.refundDtlLogs = res.result;
            }
          })
        }
        this.$refs.refundDtlLogModal.edit(record);
        this.$refs.refundDtlLogModal.title = "退款记录";
      },
      modalFormOk4 () {
        this.loadData()
      }
    },
    mounted() {
    }
  }
</script>

<style scoped>

</style>