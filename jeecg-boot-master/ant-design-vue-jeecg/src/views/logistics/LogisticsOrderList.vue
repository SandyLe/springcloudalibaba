<template>
<a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
                <a-col :md="6" :sm="8">
                  <a-form-item label="单号">
                    <!--<a-input placeholder="请输入账号查询" v-model="queryParam.username"></a-input>-->
                    <j-input placeholder="输入单号模糊查询" v-model="queryParam.code"></j-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                  <a-form-item label="原单编号">
                    <j-input placeholder="输入原单单号模糊查询" v-model="queryParam.sourceCode"></j-input>
                  </a-form-item>
                </a-col>
                <template v-if="toggleSearchStatus">
                  <a-col :md="8" :sm="8">
                    <a-form-item label="物流单号">
                      <j-input placeholder="输入物流单号模糊查询" v-model="queryParam.logisticsNo"></j-input>
                    </a-form-item>
                  </a-col>
                </template>
                <a-col :md="6" :sm="8">
                    <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                        <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                        <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                        <a @click="handleToggleSearch" style="margin-left: 8px">
                            {{ toggleSearchStatus ? '收起' : '展开' }}
                            <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
                        </a>
                    </span>
                </a-col>

            </a-row>
        </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
        <a-button @click="diyhandleEdit" type="primary" icon="plus">新增</a-button>
        <!--<a-button type="primary" icon="download" @click="handleExportXls('换货列表')">导出</a-button>
        <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
            <a-button type="primary" icon="import">导入</a-button>
        </a-upload>-->
        <a-dropdown v-if="selectedRowKeys.length > 0">
            <a-menu slot="overlay">
                <a-menu-item key="1" @click="batchDel">
                    <a-icon type="delete" />删除</a-menu-item>
            </a-menu>
            <a-button style="margin-left: 8px"> 批量操作
                <a-icon type="down" />
            </a-button>
        </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
        <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
            <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
            <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </div>

        <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}" @change="handleTableChange">

            <span slot="nameAction" slot-scope="text, record">
              <a-tooltip placement="topLeft">
                <template slot="title">
                  <span>{{record.code}}</span>
                </template>
                <a @click="goDetail(record.id)">{{record.code}}</a>
              </a-tooltip>
            </span>

            <span slot="action" slot-scope="text, record">
                <a v-if="record.billStatus<11" @click="diyhandleEdit" :data-id="record.id">编辑</a>
                <a-divider type="vertical" v-if="!(record.inventoryin!=null&&record.inventoryin.billStatus==1)"/>

                <a-dropdown>
                    <a class="ant-dropdown-link">更多
                        <a-icon type="down" /></a>
                    <a-menu slot="overlay">
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
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
import JDate from '@/components/jeecg/JDate.vue'
import {  ajaxGetDictItems, getWarehouseList } from '@/api/api'

export default {
    name: "LogisticsOrderList",
    mixins: [JeecgListMixin],
    components: {
        JDictSelectTag,
        JDate,
        JInput
    },
    data() {
        return {
            customerList:[],
            queryParam: {},
            description: '换货列表管理页面',
            // 表头
            columns: [{
                    title: '#',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: 60,
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                  title: '单据单号',
                  align: "center",
                  dataIndex: '',
                  scopedSlots: { customRender: 'nameAction' }
                },
                {
                  title: '原单编号',
                  align: "center",
                  dataIndex: 'sourceCode'
                },
                {
                  title: '物流公司',
                  align: "center",
                  dataIndex: 'logisticsName'
                },
                {
                  title: '物流单号',
                  align: "center",
                  dataIndex: 'logisticsNo'
                },
                {
                  title: '收件人',
                  align: "center",
                  dataIndex: 'recipients'
                },
                {
                    title: '收件人电话',
                    align: "center",
                    dataIndex: 'recipientsPhone'
                },
                {
                    title: '订单日期',
                    align: "center",
                    dataIndex: 'billDate'
                },
                {
                  title: '件数',
                  align: "center",
                  dataIndex: 'number'
                },
                {
                  title: '总重量',
                  align: "center",
                  dataIndex: 'totalWeight'
                },
                {
                  title: '总费用',
                  align: "center",
                  dataIndex: 'totalCharge'
                },
                {
                    title: '状态',
                    align: "center",
                    dataIndex: 'billStatusName'
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: "center",
                    scopedSlots: {
                        customRender: 'action'
                    }
                }
            ],
            url: {
                list: "/logisticsOrder/getPage",
                delete: "/logisticsOrder/delete",
                deleteBatch: "/logisticsOrder/deleteBatch",
                exportXlsUrl: "/logisticsOrder/exportXls",
                importExcelUrl: "logisticsOrder/importExcel",
            },
            dictOptions: {
                vendorId: [],
                logisticsOrderstype: [],
                warehouse: []
            },
        }
    },
    computed: {
        importExcelUrl: function () {
            return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
        }
    },
    methods: {
        diyhandleEdit(e){
            if(e.target.dataset.id)
                this.$router.replace({ path:'/logistics/LogisticsOrderModal/' + e.target.dataset.id });
            else
                this.$router.replace({ path:'/logistics/LogisticsOrderModal/' });
        },
        goDetail(id) {
          this.$router.replace({ path:'/logisticsOrder/LogisticsOrderModal/' + id, query: {"unEditable": false} });
        }
    }
}
</script>

<style scoped>
@import '~@assets/less/common.less'
</style>
