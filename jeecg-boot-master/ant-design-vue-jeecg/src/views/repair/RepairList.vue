<template>
  <a-card :bordered="false" class="card-area">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="12">
            <a-form-item label="原单编号">
              <j-input placeholder="输入原单编号查询" v-model="queryParam.sourceCode"></j-input>
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
      <a-button @click="handleAdd" type="primary" icon="plus">添加维修单</a-button>

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
                  <span>{{record.sourceCode}}</span>
                </template>
                <a @click="goSourceDetail(record.sourceBillType, record.sourceId)">{{record.sourceCode}}</a>
              </a-tooltip>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>

          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="addWorkOrder(record)">安排工单</a>
              </a-menu-item>

              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <repair-modal ref="modalForm" @ok="modalFormOk"></repair-modal>

  </a-card>
</template>

<script>
  import RepairModal from './RepairModal'
  import JInput from '@/components/jeecg/JInput'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {searchCustomer} from '@/api/api'
  export default {
    name: "Repair",
    mixins: [JeecgListMixin],
    components: {
      JInput,
      RepairModal
    },
    data() {
      return {
        queryParam: {},
        customerList: [],
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
            title: '工单编号',
            align: "center",
            dataIndex: '',
            scopedSlots: { customRender: 'nameAction' }
          },
          {
            title: '原单编号',
            align: "center",
            dataIndex: '',
            scopedSlots: { customRender: 'sourceAction' }
          },
          {
            title: '客户',
            align:"center",
            dataIndex: 'customer'
          },
          {
            title: '施工时间',
            align: "center",
            dataIndex: 'operateDate'
          },
          {
            title: '订单日期',
            align: "center",
            dataIndex: 'billDate'
          },
          {
            title: '完工时间',
            dataIndex: 'finishedDate',
            align:"center",
            sorter: true
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/repairOrder/getPage",
          delete: "/repairOrder/delete",
          deleteBatch: "/repairOrder/deleteBatch"
        }
      }
    },
    methods: {
      searchCustomer (e) {
        searchCustomer({"keyword":e.target.valueOf().value}).then((res) => {
          if (res.success) {
            this.customerList = res.result;
          }
        })
      },
      addWorkOrder (record) {

        this.$router.replace({ path:'/workorder/workOrderDetail/',  query: {"sourceBillType": 17, "sourceId": record.id, "sourceCode": record.code} });
      }
    },
    mounted() {
      searchCustomer({}).then((res) => {
        if (res.success) {
          this.customerList = res.result;
        }
      })
    }
  }
</script>

<style scoped>

</style>