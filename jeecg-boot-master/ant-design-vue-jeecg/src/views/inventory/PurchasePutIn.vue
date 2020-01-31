<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="账号">
              <!--<a-input placeholder="请输入账号查询" v-model="queryParam.username"></a-input>-->
              <j-input placeholder="输入账号模糊查询" v-model="queryParam.username"></j-input>
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
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
        :row-class-name="status_change"
        @change="handleTableChange">

        <span slot="nameAction" slot-scope="text, record">
          <a @click="goDetail(record.mtlId)">{{record.material}}</a>
        </span>
        <span slot="action" v-if="record.rowSts !== 6" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a v-if="record.rowSts !== 6" @click="handleStocking(record)">盘点</a>
        </span>
      </a-table>
    </div>

    <stocking-modal ref="modalForm" @ok="modalFormOk"></stocking-modal>
  </a-card>
</template>

<script>
  import StockingModal from './StockingModal'
  import JInput from '@/components/jeecg/JInput'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {handleStocking} from '@/api/api'
  export default {
    name: "PurchasePutIn",
    mixins: [JeecgListMixin],
    components: {
      JInput,
      StockingModal
    },
    data () {
      return {
        queryParam:{},
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
            title: '仓库',
            align:"center",
            dataIndex: 'warehouse'
          },
          {
            title: '入库单号',
            align:"center",
            dataIndex: 'code'
          },
          {
            title: '原单类型',
            align:"center",
            dataIndex: 'sourceBillTypeName'
          },
          {
            title: '原单编号',
            align:"center",
            dataIndex: 'sourceCode'
          },
          {
            title: '入库时间',
            align:"center",
            dataIndex: 'putInTime'
          },
          {
            title: '状态',
            align:"center",
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
          list: "/inventoryIn/getPage",
          delete: "/inventoryIn/delete",
          deleteBatch: "/inventoryIn/deleteBatch",
          exportXlsUrl: "/inventoryIn/exportXls",
          importExcelUrl: "/inventoryIn/importExcel",
        },
      }
    },
    methods: {
      status_change:function (row) {
        if(row.rowSts===6){
          return 'demo-table-info-row';
        }
      },
      handleStocking (record) {
        let that = this;
        let content = "是否确认盘点 " + record.warehouse + " 中 " + record.material + " 库存数量？"
        if(record.beforeAmount !== record.stockAmount){
          content = "❌ 盘点数量与库存数量有差距，" + content;
        } else {
          content = "✅" + content;
        }
        this.$confirm({
          title: "确认盘点",
          content: content,
          onOk: function () {
            handleStocking({id:record.id}).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.loadData();
                that.onClearSelected();
              } else {
                that.$message.warning(res.message);
              }
            })
          }
        });

      },
      searchQuery () {

      },
      searchReset () {

      }
    }
  }
</script>
<style lang="scss">
  .demo-table-info-row td {
    background-color: #fafafa ;
  }
</style>