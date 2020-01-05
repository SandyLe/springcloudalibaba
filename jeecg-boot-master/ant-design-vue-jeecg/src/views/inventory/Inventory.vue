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
        @change="handleTableChange">

        <span slot="nameAction" slot-scope="text, record">
          <a @click="goDetail(record.mtlId)">{{record.material}}</a>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record.id)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handlePerssion(record.id)">授权</a>
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

    <inventory-modal ref="modalForm" @ok="modalFormOk"></inventory-modal>
  </a-card>
</template>

<script>
  import InventoryModal from './StockingModal'
  import JInput from '@/components/jeecg/JInput'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  export default {
    name: "",
    mixins: [JeecgListMixin],
    components: {
      JInput,
      InventoryModal
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
            title: '产品',
            align:"center",
            dataIndex: '',
            scopedSlots: { customRender: 'nameAction' }
          },
          {
            title: '库存',
            align:"center",
            dataIndex: 'stockAmount'
          },
          {
            title: '库存下限  ',
            align:"center",
            dataIndex: 'warningstart'
          },
          {
            title: '库存上限',
            align:"center",
            dataIndex: 'warningend'
          },
          {
            title: '单位',
            align:"center",
            dataIndex: 'unit'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/inventory/getPage",
          delete: "/inventory/delete",
          deleteBatch: "/inventory/deleteBatch",
          exportXlsUrl: "/inventory/exportXls",
          importExcelUrl: "/inventory/importExcel",
        },
      }
    },
    methods: {
      searchQuery () {

      },
      searchReset () {

      }
    }
  }
</script>
