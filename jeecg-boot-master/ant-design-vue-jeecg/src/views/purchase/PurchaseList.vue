<template>
<a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
                <a-col :md="6" :sm="8">
                    <a-form-item label="供应商">
                        <a-select v-model="queryParam.vendorId" placeholder="请输入供应商">
                            <a-select-option value="">请选择</a-select-option>
                            <a-select-option v-for="(item, key) in dictOptions.vendorId" :key="key" :value="item.id">
                                {{ item.name }}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :md="12" :sm="16">
                    <a-form-item label="仓库">
                        <a-select v-model="queryParam.warehouseId" placeholder="请输入仓库">
                            <a-select-option value="">请选择</a-select-option>
                            <a-select-option v-for="(item, key) in dictOptions.warehouse" :key="key" :value="item.id">
                                {{ item.name }}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
                <template v-if="toggleSearchStatus">
                    <a-col :md="12" :sm="16">
                        <a-form-item label="业务时间">
                            <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.billdate_begin"></j-date>
                            <span class="query-group-split-cust"></span>
                            <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.billdate_end"></j-date>
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
        <a-button type="primary" icon="download" @click="handleExportXls('采购列表')">导出</a-button>
        <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
            <a-button type="primary" icon="import">导入</a-button>
        </a-upload>
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
            <template slot="htmlSlot" slot-scope="text">
                <div v-html="text"></div>
            </template>
            <template slot="imgSlot" slot-scope="text">
                <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
                <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;" />
            </template>
            <template slot="fileSlot" slot-scope="text">
                <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
                <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)">
                    下载
                </a-button>
            </template>

            <span slot="action" slot-scope="text, record">
                <a v-if="record.inventoryin!=null&&record.inventoryin.billStatus==1" @click="handleinventoryout(record)" :data-id="record.id">退货</a>
                <a-divider type="vertical" v-if="record.inventoryin!=null&&record.inventoryin.billStatus==1"/>

                <a v-if="record.inventoryin!=null" @click="handleinventoryin(record.inventoryin)" :data-id="record.id">入库单</a>
                <a-divider type="vertical" v-if="record.inventoryin!=null"/>

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
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
import JDate from '@/components/jeecg/JDate.vue'
import { getVendorList, ajaxGetDictItems, getWarehouseList } from '@/api/api'

export default {
    name: "PurchaseList",
    mixins: [JeecgListMixin],
    components: {
        JDictSelectTag,
        JDate
    },
    data() {
        return {
            queryParam: {},
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
                  title: '批次号',
                  align: "center",
                  dataIndex: 'batchNo'
                },
                {
                  title: '采购单号',
                  align: "center",
                  dataIndex: '',
                  scopedSlots: { customRender: 'nameAction' }
                },
                {
                    title: '供应商',
                    align: "center",
                    dataIndex: 'vendorId',
                    customRender: (text) => {
                        if (!text) {
                            return ''
                        } else {
                            return filterMultiDictText(this.dictOptions['vendorId'], text + "")
                        }
                    }
                },
                {
                    title: '仓库',
                    align: "center",
                    dataIndex: 'warehouseId',
                    customRender: (text) => {
                        if (!text) {
                            return ''
                        } else {
                            return filterMultiDictText(this.dictOptions['warehouse'], text + "")
                        }
                    }
                },
                {
                    title: '实付金额',
                    align: "center",
                    dataIndex: 'payamount'
                },
                {
                    title: '总金额',
                    align: "center",
                    dataIndex: 'totalamount'
                },
                {
                    title: '订单日期',
                    align: "center",
                    dataIndex: 'billdate',
                    // customRender:(text)=>{
                    //   if(!text){
                    //     return ''
                    //   }else{
                    //     return filterMultiDictText(this.dictOptions['purchasestype'], text+"")
                    //   }
                    // }
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
                list: "/purchase/getPage",
                delete: "/purchase/delete",
                deleteBatch: "/purchase/deleteBatch",
                exportXlsUrl: "/purchase/exportXls",
                importExcelUrl: "purchase/importExcel",
            },
            dictOptions: {
                vendorId: [],
                purchasestype: [],
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
        initDictConfig() {
            getVendorList('').then((res) => {
                if (res.success) {
                    if (res.result && res.result.length > 0) {
                        res.result.forEach(function (option) {
                            option.value = option.id;
                            option.text = option.name;
                        })
                    }
                    this.$set(this.dictOptions, 'vendorId', res.result)
                }
            });
            getWarehouseList('').then((res) => {
                if (res.success) {
                    if (res.result && res.result.length > 0) {
                        res.result.forEach(function (option) {
                            option.value = option.id;
                            option.text = option.name;
                        })
                    }
                    this.$set(this.dictOptions, 'warehouse', res.result)
                }
            });
        },
        diyhandleEdit(e){
            if(e.target.dataset.id)
                this.$router.replace({ path:'/purchase/PurchaseModal/' + e.target.dataset.id, query: {"editType": 1}});
            else
                this.$router.replace({ path:'/purchase/PurchaseModal/', query: {"editType": 1}  });
        },
        goDetail(id) {
          this.$router.replace({ path:'/purchase/PurchaseModal/' + id, query: {"editType": 0} });
        },
        handleinventoryin(data){
            var that = this;
            that.$refs.inventorymodalForm.edit(data);
            that.$refs.inventorymodalForm.disableSubmit = true;
        },
        handleinventoryout(data){
            var that = this;
            let model = {};
            model.sourceId = data.id;
            if(data.inventoryOut && data.inventoryOut.putOutTime)
            {
                model.id = data.inventoryOut.id;
                model.putOutTime = data.inventoryOut.putOutTime;
            }

            that.$refs.inventoryOutmodalForm.edit(model);
            that.$refs.inventoryOutmodalForm.disableSubmit = true;
        }
    }
}
</script>

<style scoped>
@import '~@assets/less/common.less'
</style>
