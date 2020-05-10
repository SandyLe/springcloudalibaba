<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="8" :sm="18">
            <a-form-item label="产品">
              <a-select v-model="queryParam.mtlId" placeholder="请选择产品"  showSearch
                        optionFilterProp="children"
                        notFoundContent="无法找到，输入关键词Enter搜索" @keyup.enter.native="searchMtl" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in mtlList" :key="key" :value="item.id">
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
  import {handleStocking, searchMaterial} from '@/api/api'
  export default {
    name: "",
    mixins: [JeecgListMixin],
    components: {
      JInput,
      StockingModal
    },
    data () {
      return {
        mtlList:[],
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
            title: '盘点数量',
            align:"center",
            dataIndex: 'stockAmount'
          },
          {
            title: '盘点前数量',
            align:"center",
            dataIndex: 'beforeAmount'
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
          list: "/stocking/getPage",
          delete: "/stocking/delete",
          deleteBatch: "/stocking/deleteBatch",
          exportXlsUrl: "/stocking/exportXls",
          importExcelUrl: "/stocking/importExcel",
        },
      }
    },
    methods: {
      searchMtl (e) {
        searchMaterial({"keyword":e.target.valueOf().value}).then((res) => {
          if (res.success) {
            this.mtlList = res.result;
          }
        })
      },
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
              } else {
                that.$message.warning(res.message);
              }
              that.loadData();
              that.onClearSelected();
            })
          }
        });

      }
    },
    mounted() {
      searchMaterial().then((res) => {
        if (res.success) {
          this.mtlList = res.result;
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