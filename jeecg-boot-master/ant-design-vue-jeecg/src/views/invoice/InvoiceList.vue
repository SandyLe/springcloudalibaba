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
              <j-input placeholder="输入单号模糊查询" v-model="queryParam.invoiceNo"></j-input>
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
      <a-button @click="handleEdit" type="primary" icon="plus">添加发票</a-button>

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
                  <span>{{record.sourceCode}}</span>
                </template>
                <a @click="goSourceDetail(record.sourceBillType, record.sourceId)">{{record.sourceCode}}</a>
              </a-tooltip>
        </span>
        <span slot="action" slot-scope="text, record">
          <span v-if="record.billStatusId != 1"><a @click="createInvoice(record)">登记开票</a></span>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

  </a-card>
</template>

<script>
  import JInput from '@/components/jeecg/JInput'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getAction } from '@/api/manage'
  import {getinvoiceDtlList} from '@/api/api'
  export default {
    name: "InvoiceList",
    mixins: [JeecgListMixin],
    components: {
      JInput
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
            title: '发票单编号',
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
            title: '抬头',
            align:"center",
            dataIndex: 'billTitle'
          },
          {
            title: '发票号',
            align:"center",
            dataIndex: 'invoiceNo'
          },
          {
            title: '发票类型',
            dataIndex: 'invoiceTypeName',
            align:"center",
            sorter: true
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
          list: "/invoice/getPage",
          delete: "/invoice/delete",
          deleteBatch: "/invoice/deleteBatch"
        },
        isorter: {
          column: 'createTime',
          order: 'desc'
        },
        selectedOrder: null,
        invoiceDtlLogs: []
      }
    },
    methods: {
      createInvoice (record) {
        this.$router.replace({ path:'/invoice/invoiceModal/' + record.id, query: {"editType":0, "makeInvoice":1} });
      },
      handleEdit(e){
        if(e.target.dataset.id)
          this.$router.replace({ path:'/invoice/invoiceModal/' + e.target.dataset.id });
        else
          this.$router.replace({ path:'/invoice/invoiceModal/' });
      },
      goDetail(id){
        this.$router.replace({ path:'/invoice/invoiceModal/' + id, query: {"editType":0} });
      }
    },
    mounted() {
    }
  }
</script>

<style scoped>

</style>