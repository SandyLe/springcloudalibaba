<template>
  <a-card :bordered="false" class="card-area">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="12">
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

          <a-col :md="6" :sm="12">
            <a-form-item label="代码">
              <j-input placeholder="输入批次号模糊查询" v-model="queryParam.batchNo"></j-input>
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
      <!--<a-button @click="handleAdd" type="primary" icon="plus">添加产品成本</a-button>

      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作 <a-icon type="down" />
        </a-button>
      </a-dropdown>

      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel" style="margin-left: 8px">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>

      <a-button type="link" @click="downloadtemplate">下载模板</a-button>-->
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

        <span slot="action" slot-scope="text, record">
          <a href="javascript:;" @click="handleDetail(record)">详情</a>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域
    <material-price-modal ref="modalForm" @ok="modalFormOk"></material-price-modal>-->

  </a-card>
</template>

<script>
  //import MaterialPriceModal from './MaterialPriceModal'
  import JInput from '@/components/jeecg/JInput'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {searchMaterial} from '@/api/api'
  export default {
    name: "MaterialPrice",
    mixins: [JeecgListMixin],
    components: {
      JInput,
     // MaterialPriceModal
    },
    data() {
      return {
        queryParam: {},
        mtlList:[],
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
            title: '产品名称',
            align:"center",
            dataIndex: 'material'
          },
          {
            title: '采购批次号',
            align:"center",
            dataIndex: 'batchNo'
          },
          {
            title: '计量单位',
            align:"center",
            dataIndex: 'unit'
          },
          {
            title: '平均价格',
            align:"center",
            dataIndex: 'averagePrice'
          },
          {
            title: '备注',
            align:"center",
            dataIndex: 'content'
          },
          {
            title: '创建时间',
            dataIndex: 'createTime',
            align:"center",
            sorter: true
          },
          {
            title: '更新时间',
            dataIndex: 'updateTime',
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
          list: "/purchaseCost/getPage",
          delete: "/purchaseCost/delete",
          deleteBatch: "/purchaseCost/deleteBatch",
          importExcelUrl: "/purchaseCost/importExcel",
        }
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      searchMtl (e) {
        searchMaterial({"keyword":e.target.valueOf().value}).then((res) => {
          if (res.success) {
            this.mtlList = res.result;
          }
        })
      },
      downloadtemplate(){
        location.href =`${window._CONFIG['domianURL']}/sys/common/sysdownload/systemplate/materialprice.xlsx`;
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

<style scoped>

</style>