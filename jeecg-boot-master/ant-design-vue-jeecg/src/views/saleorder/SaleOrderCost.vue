<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <!--<a-form layout="inline" @keyup.enter.native="searchQuery">-->
        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="原单编号">
              <!--<a-input placeholder="请输入账号查询" v-model="queryParam.username"></a-input>-->
              <j-input placeholder="输入单号模糊查询" v-model="queryParam.sourceCode"></j-input>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="12">
            <a-form-item label="收款方">
              <a-select style="" v-model="queryParam.payeeId" placeholder="请选择收款方"  showSearch
                        optionFilterProp="children"
                        notFoundContent="无法找到，输入名称、编号、手机号回车搜索" @keyup.enter.native="searchInstitution" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in institutionList" :key="key" :value="item.id">
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
    <!--<div class="table-operator"  style="margin-top: 5px">
      <a-button @click="handleEditSaleOrder(0)" type="primary" icon="plus">新增</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作 <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>-->
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
              <span>{{record.sourceCode}}</span>
            </template>
            <a @click="goDetail(record.sourceId)">{{record.sourceCode}}</a>
          </a-tooltip>
        </span>
        <span slot="action" slot-scope="text, record">
          <span v-if="record.billStatusId != 6">
            <a-popconfirm title="确定标记吗?" @confirm="() => handleSaleOrderCost(record.id)">
                  <a>标记已处理</a>
                </a-popconfirm>
            <a-divider type="vertical" />
          </span>
        </span>
      </a-table>
    </div>
  </a-card>
</template>

<script>
  import JInput from '@/components/jeecg/JInput'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {handleSaleOrderCost, searchInstitution} from '@/api/api'
  export default {
    name: "SaleOrderCost",
    mixins: [JeecgListMixin],
    components: {
      JInput
    },
    data () {
      return {
        queryParam:{},
        institutionList: [],
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
            title: '原单编号',
            align:"center",
            dataIndex: '',
            scopedSlots: { customRender: 'nameAction' }
          },
          {
            title: '收款方',
            align:"center",
            dataIndex: 'payee'
          },
          {
            title: '费用  ',
            align:"center",
            dataIndex: 'expense'
          },
          {
            title: '金额',
            align:"center",
            dataIndex: 'amount'
          },
          {
            title: '订单状态',
            dataIndex: 'billStatus',
            align:"center"
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/saleOrderCost/getPage",
          delete: "/saleOrderCost/delete",
          deleteBatch: "/saleOrderCost/deleteBatch",
          exportXlsUrl: "/saleOrderCost/exportXls",
          importExcelUrl: "/saleOrderCost/importExcel",
        },
      }
    },
    methods: {
      goDetail (id) {
        this.$router.push({ name: "saleorder-saleOrderEdit", query: {"id": id, "unEditable": true}})
      },
      searchInstitution (e) {
        searchInstitution({"keyword":e.target.valueOf().value}).then((res) => {
          if (res.success) {
            this.institutionList = res.result;
          }
        })
      },
      handleSaleOrderCost (id) {
        handleSaleOrderCost({id:id}).then((res)=>{
          if(res.success){
            this.$message.success(res.message);
            this.$emit('ok');
          }else{
            this.$message.warning(res.message);
          }
        }).finally(() => {
          this.loadData();
        })
      }
    },
    mounted() {
      searchInstitution({}).then((res) => {
        if (res.success) {
          this.institutionList = res.result;
        }
      })
    }
  }
</script>
