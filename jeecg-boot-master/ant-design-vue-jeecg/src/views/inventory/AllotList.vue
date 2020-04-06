<template>
<a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
                <a-col :md="6" :sm="8">
                  <a-form-item label="代码">
                    <j-input placeholder="输入单号模糊查询" v-model="queryParam.code"></j-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                    <a-form-item label="供应商">
                        <a-select v-model="queryParam.vendorId" placeholder="请输入供应商">
                            <a-select-option value="">请选择</a-select-option>
                            <a-select-option v-for="(item, key) in vendorList" :key="key" :value="item.id">
                                {{ item.name }}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :md="6" :sm="16">
                    <a-form-item label="仓库">
                        <a-select v-model="queryParam.warehouseId" placeholder="请输入仓库">
                            <a-select-option value="">请选择</a-select-option>
                            <a-select-option v-for="(item, key) in warehouseList" :key="key" :value="item.id">
                                {{ item.name }}
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
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
        <a-button @click="diyhandleEdit" type="primary" icon="plus">新增</a-button>
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
import { getVendorList, ajaxGetDictItems, getWarehouseList } from '@/api/api'

export default {
    name: "AllotList",
    mixins: [JeecgListMixin],
    components: {
        JDictSelectTag,
        JInput,
        JDate
    },
    data() {
        return {
            queryParam: {},
            vendorList: [],
            warehouseList: [],
            description: '采购列表管理页面',
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
                  title: '调拨单号',
                  align: "center",
                  dataIndex: 'code'
                },
                {
                  title: '调出仓库',
                  align: "center",
                  dataIndex: 'fromWarehouse'
                },
                {
                    title: '掉入仓库',
                    align: "center",
                    dataIndex: 'toWarehouse'
                },
                {
                    title: '单据日期',
                    align: "center",
                    dataIndex: 'billdate'
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
                list: "/allot/getPage",
                delete: "/allot/delete",
                deleteBatch: "/allot/deleteBatch",
                exportXlsUrl: "/allot/exportXls",
                importExcelUrl: "/allot/importExcel",
            }
        }
    },
    computed: {
        importExcelUrl: function () {
            return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
        }
    },
    methods: {
        initDictConfig() {
            getVendorList('').then((res) => {
                if (res.success) {
                    if (res.result && res.result.length > 0) {
                        this.vendorList =res.result;
                    }
                    // this.$set(this.dictOptions, 'vendorId', res.result)
                }
            });
            getWarehouseList('').then((res) => {
                if (res.success) {
                    if (res.result && res.result.length > 0) {
                        this.warehouseList = res.result;
                    }
                    // this.$set(this.dictOptions, 'warehouse', res.result)
                }
            });
        },
        diyhandleEdit(e){
            if(e.target.dataset.id)
                this.$router.replace({ path:'/inventory/AllotModal/' + e.target.dataset.id });
            else
                this.$router.replace({ path:'/inventory/AllotModal/' });
        },
        goDetail(id) {
          this.$router.replace({ path:'/inventory/AllotModal/' + id, query: {"unEditable": false} });
        }
    }
}
</script>

