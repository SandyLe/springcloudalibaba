<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="单据类型">
              <a-select placeholder="请选择单据类型" v-model="queryParam.billType" showSearch optionFilterProp="children"
                        notFoundContent="没有匹配的单据类型"  >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in billTypeList" :key="key" :value="item.id">
                  {{ item.name || item.code}}
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
        </span>
      </a-table>
    </div>

    <billCodeBuilder-modal ref="modalForm" @ok="modalFormOk"></billCodeBuilder-modal>
  </a-card>
</template>

<script>
  import BillCodeBuilderModal from './modules/BillCodeBuilderModal'
  import JInput from '@/components/jeecg/JInput'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getBillTypeList} from '@/api/api'
  export default {
    name: "BillCodeBuilder",
    mixins: [JeecgListMixin],
    components: {
      JInput,
      BillCodeBuilderModal
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
            title: '单据名称',
            align:"center",
            dataIndex: 'billTypeName'
          },
          {
            title: '前缀',
            align:"center",
            dataIndex: 'prefix'
          },
          {
            title: '编号位数',
            align:"center",
            dataIndex: 'zeroCount'
          },
          {
            title: '日期格式',
            align:"center",
            dataIndex: 'dateFormatName'
          },
          {
            title: '含日期',
            align:"center",
            customRender:function (t,r,index) {
              return t.hasDate ? '是' : '否';
            }
          },
          {
            title: '预览',
            align:"center",
            dataIndex: 'preview'
          },
          {
            title: '当前段位',
            align:"center",
            dataIndex: 'currentLevel'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/billCodeBuilder/getPage",
          delete: "/billCodeBuilder/delete",
          deleteBatch: "/billCodeBuilder/deleteBatch",
          exportXlsUrl: "/billCodeBuilder/exportXls",
          importExcelUrl: "/billCodeBuilder/importExcel",
        },
        billTypeList: []
      }
    },
    methods: {
      status_change:function (row) {
        if(row.rowSts===6){
          return 'demo-table-info-row';
        }
      }
    },
    mounted() {
      getBillTypeList().then((res) => {
        if (res.success) {
          this.billTypeList = res.result;
        }
      })
    }
  }
</script>
<style lang="scss">
  .demo-table-info-row td {
    background-color: #fafafa ;
  }
</style>