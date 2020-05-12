<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <!--<a-form layout="inline" @keyup.enter.native="searchQuery">-->
        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="单号">
              <!--<a-input placeholder="请输入账号查询" v-model="queryParam.username"></a-input>-->
              <j-input placeholder="输入单号模糊查询" v-model="queryParam.code"></j-input>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="12">
            <a-form-item label="客户">
              <a-select style="" v-model="queryParam.customerId" placeholder="请选择客户"  showSearch
                        optionFilterProp="children"
                        notFoundContent="无法找到，输入名称、编号、手机号回车搜索" @keyup.enter.native="searchCustomer" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in customerList" :key="key" :value="item.id">
                  {{ item.info }}
                </a-select-option>
              </a-select>
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
    <div class="table-operator"  style="margin-top: 5px">
      <a-button @click="handleEditSaleOrder(0)" type="primary" icon="plus">新增</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作 <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="nameAction" slot-scope="text, record">
          <a-tooltip placement="topLeft">
            <template slot="title">
              <span>{{record.code}}</span>
            </template>
            <a @click="goDetail(record.id)">{{record.code}}</a>
          </a-tooltip>
        </span>
        <span slot="action" slot-scope="text, record">
          <span v-if="record.billStatus < 1 && record.billStatus != -1">
            <a @click="handleEditSaleOrder(record.id)">编辑</a>
            <a-divider type="vertical" />
          </span>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item  v-if="record.billStatus != -1">
                <a @click="popDetail(record)">详情打印</a>
              </a-menu-item>
              <a-menu-item  v-if="record.billStatus != -1">
                <a @click="addWorkOrder(record)">安排工单</a>
              </a-menu-item>
              <a-menu-item  v-if="record.billStatus < 4 && record.billStatus != -1">
                <a-popconfirm title="确定作废吗?" @confirm="() => handleInvalid(record)">
                  <a>作废</a>
                </a-popconfirm>
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

    <sale-order-detail ref="saleOrderDetail" @ok="modalFormOk"></sale-order-detail>
  </a-card>
</template>

<script>
  import SaleOrderDetail from './SaleOrderDetail'
  import JInput from '@/components/jeecg/JInput'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {disableSaleOrder, searchCustomer} from '@/api/api'
  export default {
    name: "",
    mixins: [JeecgListMixin],
    components: {
      JInput,
      SaleOrderDetail
    },
    data () {
      return {
        queryParam:{},
        customerList: [],
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
            title: '销售单号',
            align:"center",
            dataIndex: '',
            scopedSlots: { customRender: 'nameAction' }
          },
          {
            title: '客户',
            align:"center",
            dataIndex: 'customer'
          },
          {
            title: '出货仓库  ',
            align:"center",
            dataIndex: 'warehouse'
          },
          {
            title: '渠道',
            align:"center",
            dataIndex: 'channel'
          },
          {
            title: '发货时间',
            align:"center",
            dataIndex: 'deliveryTime'
          },
          {
            title: '安装时间',
            dataIndex: 'installTime',
            align:"center"
          },
          {
            title: '订单时间',
            dataIndex: 'billDate',
            align:"center"
          },
          {
            title: '订单状态',
            dataIndex: 'billStatusName',
            align:"center"
          },
          {
            title: '收款方式',
            dataIndex: 'receiptTypeName',
            align:"center"
          },
          {
            title: '总金额',
            dataIndex: 'totalamount',
            align:"center"
          },
          {
            title: '已付金额',
            align:"center",
            dataIndex: 'payamount'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/saleOrder/getPage",
          delete: "/saleOrder/delete",
          deleteBatch: "/saleOrder/deleteBatch",
          exportXlsUrl: "/saleOrder/exportXls",
          importExcelUrl: "/saleOrder/importExcel",
        },
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
      goDetail (id) {
        this.$router.push({ name: "saleorder-saleOrderEdit", query: {"id": id, "unEditable": true}})
      },
      popDetail: function(record) {
        this.$refs.saleOrderDetail.title = '订单详情'
        this.$refs.saleOrderDetail.view(record)
      },
      handleEditSaleOrder (id) {
        this.$router.push({ name: "saleorder-saleOrderEdit", query: {"id": id, "unEditable": false}})
      },
      handleInvalid (record) {
        disableSaleOrder(record).then((res)=>{
          if(res.success){
            this.$message.success(res.message);
            this.$emit('ok');
          }else{
            this.$message.warning(res.message);
          }
        }).finally(() => {
          this.loadData();
        })
      },
      addWorkOrder (record) {

        this.$router.replace({ path:'/workorder/workOrderDetail/',  query: {"sourceBillType": 0, "sourceId": record.id, "sourceCode": record.code} });
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
