<template>
  <a-card :bordered="false" class="card-area">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="12" :sm="18">
            <a-form-item label="产品">
              <a-select v-model="queryParam.materialId" placeholder="请选择产品"  showSearch
                        optionFilterProp="children"
                        notFoundContent="无法找到，输入关键词Enter搜索" @keyup.enter.native="searchMtl" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in mtlList" :key="key" :value="item.id">
                  {{ item.info }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="6">
            <a-form-item label="代码">
              <j-input placeholder="输入代码模糊查询" v-model="queryParam.code"></j-input>
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
      <a-button @click="handleAdd" type="primary" icon="plus">添加产品定价</a-button>

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

      <a-button type="link" @click="downloadtemplate">下载模板</a-button>
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
    <material-price-modal ref="modalForm" @ok="modalFormOk"></material-price-modal>

  </a-card>
</template>

<script>
  import MaterialPriceModal from './MaterialPriceModal'
  import JInput from '@/components/jeecg/JInput'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {searchMaterial} from '@/api/api'
  export default {
    name: "MaterialPrice",
    mixins: [JeecgListMixin],
    components: {
      JInput,
      MaterialPriceModal
    },
    data() {
      return {
        mtlList: [],
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
            title: '产品名称',
            align:"center",
            dataIndex: 'material'
          },/*
          {
            title: '客户类型',
            align:"center",
            dataIndex: 'customerType'
          },*/
          {
            title: '计量单位',
            align:"center",
            dataIndex: 'unit'
          },
          {
            title: '价格',
            align:"center",
            dataIndex: 'price'
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
          list: "/materialPrice/getPage",
          delete: "/materialPrice/delete",
          deleteBatch: "/materialPrice/deleteBatch",
          importExcelUrl: "/materialPrice/importExcel",
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
        location.href =`${window._CONFIG['domianURL']}/sys/common/download/systemplate/MaterialPrice.xlsx`;
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