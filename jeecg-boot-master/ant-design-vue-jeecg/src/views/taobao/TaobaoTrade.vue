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
      <a-button @click="getTaobaoOrder(0)" type="primary" icon="plus">更新淘宝订单</a-button>
      <a-button @click="subscribe()" type="primary" icon="plus">订阅消息</a-button>

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
        @expand="expandSubmenu"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="nameAction" slot-scope="text, record">
          <a-tooltip placement="topLeft">
            <template slot="title">
              <span>{{record.tid}}</span>
            </template>
            <a @click="goDetail(record.id)">{{record.tid}}</a>
          </a-tooltip>
        </span>
        <span slot="mtlImgAction" slot-scope="text, record">
          <a-tooltip placement="topLeft">
            <template slot="title">
              <span>{{record.title}}</span>
            </template>
            <img :src="record.picPath" width="100px" />
          </a-tooltip>
        </span>
        <span slot="action" slot-scope="text, record">
          <span v-if="record.billStatus == 0">
            <a @click="handleEditSaleOrder(record.id)">编辑</a>
            <a-divider type="vertical" />
          </span>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="popDetail(record)">详情打印</a>
              </a-menu-item>
              <a-menu-item  v-if="record.billStatus > -1">
                <a @click="addWorkOrder(record)">安排工单</a>
              </a-menu-item>
              <a-menu-item v-if="record.billStatus < 3 && record.billStatus > -1">
                <a @click="addStockOut(record)">通知出库</a>
              </a-menu-item>
              <a-menu-item v-if="record.billStatus > 0">
                <a @click="addInvoice(record)">发票开具</a>
              </a-menu-item>
              <a-menu-item v-if="record.billStatus > 0">
                <a @click="addSaleReturn(record)">退货</a>
              </a-menu-item>
              <a-menu-item  v-if="record.billStatus < 4 && record.billStatus > -1">
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

  </a-card>
</template>

<script>
  import JInput from '@/components/jeecg/JInput'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {disableSaleOrder, searchCustomer, inventoryOutSave, getTaobaoOrder, subscribe,getOneTrade, getSlTradeOrderList} from '@/api/api'
  export default {
    name: "",
    mixins: [JeecgListMixin],
    components: {
      JInput
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
            title: '交易单号',
            align:"center",
            dataIndex: '',
            scopedSlots: { customRender: 'nameAction' }
          },
          {
            title: '买家昵称',
            align:"center",
            dataIndex: 'buyerNick'
          },
          {
            title: '产品',
            align:"center",
            dataIndex: '',
            scopedSlots: { customRender: 'mtlImgAction' }
          },
          {
            title: '下单时间',
            align:"center",
            dataIndex: 'created'
          },
          {
            title: '付款时间',
            dataIndex: 'payTime',
            align:"center"
          },
          {
            title: '发货方式',
            align:"center",
            customRender:function (r) {
              let shippingTypeStr = r.shippingType;
              if (r.shippingType == 'express') {
                shippingTypeStr = '快递';
              }
              return shippingTypeStr;
            }
          },
          {
            title: '商品价格',
            align:"center",
            dataIndex: 'price'
          },
          {
            title: '商品金额',
            dataIndex: 'totalFee',
            align:"center"
          },
          {
            title: '折扣  ',
            align:"center",
            dataIndex: 'discountFee'
          },
          {
            title: '实付金额',
            dataIndex: 'payment',
            align:"center"
          },
          {
            title: '订单状态',
            align:"center",
            customRender:function (r) {
              let statusStr = r.status;
              if (r.status == 'WAIT_BUYER_PAY') {
                statusStr = '等待买家付款';
              } else if (r.status == 'WAIT_SELLER_SEND_GOODS') {
                statusStr = '等待卖家发货';
              } else if (r.status == 'SELLER_CONSIGNED_PART') {
                statusStr = '卖家部分发货';
              } else if (r.status == 'WAIT_BUYER_CONFIRM_GOODS') {
                statusStr = '等待买家确认收货';
              } else if (r.status == 'TRADE_BUYER_SIGNED') {
                statusStr = '买家已签收（货到付款专用）';
              } else if (r.status == 'TRADE_FINISHED') {
                statusStr = '交易成功';
              } else if (r.status == 'TRADE_CLOSED') {
                statusStr = '交易关闭';
              } else if (r.status == 'TRADE_CLOSED_BY_TAOBAO') {
                statusStr = '交易被淘宝关闭';
              } else if (r.status == 'TRADE_NO_CREATE_PAY') {
                statusStr = '没有创建外部交易（支付宝交易）';
              } else if (r.status == 'WAIT_PRE_AUTH_CONFIRM') {
                statusStr = '余额宝0元购合约中';
              } else if (r.status == 'PAY_PENDING') {
                statusStr = '外卡支付付款确认中';
              } else if (r.status == 'ALL_WAIT_PAY') {
                statusStr = '所有买家未付款的交易';
              } else if (r.status == 'ALL_CLOSED') {
                statusStr = '所有关闭的交易';
              } else if (r.status == 'PAID_FORBID_CONSIGN') {
                statusStr = '禁止发货';
              }
              return statusStr;
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/slTrade/getPage",
          delete: "/slTrade/delete",
          deleteBatch: "/slTrade/deleteBatch",
          exportXlsUrl: "/slTrade/exportXls",
          importExcelUrl: "/slTrade/importExcel",
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
        this.$router.push({ name: "saleorder-slTradeEdit", query: {"id": id, "editType": 0}})
      },
      popDetail: function(record) {
        this.$refs.slTradeDetail.title = '订单详情'
        this.$refs.slTradeDetail.view(record)
      },
      handleEditSaleOrder (id) {
        this.$router.push({ name: "saleorder-slTradeEdit", query: {"id": id, "editType": 1}})
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
      getTaobaoOrder(id) {
        getOneTrade().then((res)=>{
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
      subscribe(){
        subscribe().then((res)=>{
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

        this.$router.replace({ path:'/workorder/workOrderDetail/',  query: {"sourceBillType": 0, "sourceId": record.id, "sourceCode": record.code, "editType":1} });
      },
      addStockOut (record) {
        let formData = {
          "sourceId": record.id,
          "sourceCode": record.code,
          "sourceBillType": record.billType
        };
        inventoryOutSave(formData).then((res)=>{
          if(res.success){
            this.$message.success(res.message);
          }else{
            this.$message.warning(res.message);
          }
        }).finally(() => {
          this.loadData();
        })
      },
      addInvoice (record) {
        this.$router.replace({ path:'/invoice/invoiceModal/',  query: {"sourceBillType": 0, "sourceId": record.id, "sourceCode": record.code, "editType":1} });
      },
      addSaleReturn (record) {
        this.$router.replace({ path: "/saleorder/slTradeReturnEdit",  query: {"sourceBillType": 0, "sourceId": record.id, "sourceCode": record.code, "editType":1,
        'customerId': record.customerId, 'channelId': record.channelId} });
      },
      expandSubmenu(expanded, record){
        if(expanded){
          getSlTradeOrderList({oid:record.tid}).then((res) => {
            if (res.success) {
              record.children = res.result
            }
          })
        }

      },
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
