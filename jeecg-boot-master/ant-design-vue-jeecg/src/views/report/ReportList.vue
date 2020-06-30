<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="10">
            <a-form-item label="名称">
              <a-input placeholder="请输入名称" v-model="queryParam.jobClassName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="10">
            <a-form-item label="归属页面">
              <a-select v-model="queryParam.pageType" placeholder="请选择归属页面">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in pagetypedict" :key="key" :value="item.value">
                      {{ item.title }}
                  </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="10">
            <a-form-item label="展示方式">
              <a-select v-model="queryParam.showType" placeholder="请选择展示方式">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in showtypedict" :key="key" :value="item.value">
                      {{ item.title }}
                  </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="10" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="goConfig" type="primary" icon="setting">公司配置</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('定时任务信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :loading="loading"        
        :pagination="ipagination"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}" @change="handleTableChange" >

        <!-- 字符串超长截取省略号显示-->
        <span slot="description" slot-scope="text">
          <j-ellipsis :value="text" :length="25" />
        </span>


        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
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

        <!-- 状态渲染模板 -->
        <template slot="customRenderStatus" slot-scope="status">
          <a-tag v-if="status==0" color="green">已启动</a-tag>
          <a-tag v-if="status==-1" color="orange">已暂停</a-tag>
        </template>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <report-modal ref="modalForm" @ok="modalFormOk"></report-modal>
  </a-card>
</template>

<script>
  import reportModal from './modules/ReportModal'
  import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {ajaxGetDictItems} from '@/api/api'

  export default {
    name: "ReportList",
    mixins:[JeecgListMixin],
    components: {
      reportModal,
    },
    data () {
      return {
        description: '统计报表功能列表',
        // 查询条件
        queryParam: {},        
        showtypedict:[],
        pagetypedict:[],
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'id',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title: '名称',
            align:"center",
            width: 120,
            dataIndex: 'name',
          },
          {
            title: '统计SQL语句',
            align:"center",
            dataIndex: 'querySql'
          },
          {
            title: '归属页面',
            align:"center",
            width: 100,
            sorter: true,
            dataIndex: 'pageType',
            customRender: (text) => {
                        if (!text) {
                            return ''
                        } else {
                            return filterMultiDictText(this.pagetypedict, text + "")
                        }
                    }
          },
          {
            title: '展示方式',
            align:"center",
            dataIndex: 'showType',
            width:100,
            sorter: true,
            customRender: (text) => {
                        if (!text) {
                            return ''
                        } else {
                            return filterMultiDictText(this.showtypedict, text + "")
                        }
                    }
          },
          {
            title: '接收结果Bean',
            align:"center",
            width:120,
            dataIndex: 'resultBean',
            // scopedSlots: { customRender: 'customRenderStatus' },
            // filterMultiple: false,
            // filters: [
            //   { text: '已启动', value: '0' },
            //   { text: '已暂停', value: '-1' },
            // ]
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            width:180,
            scopedSlots: { customRender: 'action' },
          }
        ],
		    url: {
          list: "/report/getPageList",
          delete: "/report/delete",
          deleteBatch: "/report/deleteBatch",
          // exportXlsUrl: "sys/quartzJob/exportXls",
          // importExcelUrl: "sys/quartzJob/importExcel",
        },
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    created () {
      //获取字典数据
      this.initDictData();
    },
    methods: {

      goConfig(id) {
        this.$router.replace({ path: "/report/config"})
      },
      initDictData() {
        //根据字典Code, 初始化字典数组
        ajaxGetDictItems('show_type', null).then((res) => {
          if (res.success) {
            this.showtypedict = res.result;
          }
        });
        //根据字典Code, 初始化字典数组
        ajaxGetDictItems('page_type', null).then((res) => {
          if (res.success) {
            this.pagetypedict = res.result;
          }
        });
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>