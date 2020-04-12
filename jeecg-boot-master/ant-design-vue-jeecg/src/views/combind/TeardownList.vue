<template>
<a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
                <a-col :md="6" :sm="8">
                  <a-form-item label="产品">
                    <a-select v-decorator="['mtlId', {}]" v-model="queryParam.mtlId" placeholder="请选择产品"  showSearch
                              optionFilterProp="children"
                              notFoundContent="无法找到，输入关键词Enter搜索" @keyup.enter.native="searchMtl" >
                      <a-select-option value="">请选择</a-select-option>
                      <a-select-option v-for="(item, key) in mtlList" :key="key" :value="item.id">
                        {{ item.info }}
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
        <a-button @click="diyhandleEdit" type="primary" icon="plus">新增</a-button><!--
        <a-button type="primary" icon="download" @click="handleExportXls('拆卸单')">导出</a-button>
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
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
import JDate from '@/components/jeecg/JDate.vue'
import { searchMaterial, ajaxGetDictItems, getWarehouseList } from '@/api/api'

export default {
    name: "TeardownList",
    mixins: [JeecgListMixin],
    components: {
        JDictSelectTag,
        JDate
    },
    data() {
        return {
            mtlList:[],
            queryParam: {},
            description: '拆卸单管理页面',
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
                  title: '拆卸单号',
                  align: "center",
                  dataIndex: '',
                  scopedSlots: { customRender: 'nameAction' }
                },
                {
                    title: '产品',
                    align: "center",
                    dataIndex: 'material'
                },
                {
                  title: '数量',
                  align: "center",
                  dataIndex: 'quantity'
                },
                {
                  title: '单位',
                  align: "center",
                  dataIndex: 'unit'
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
                    title: '订单日期',
                    align: "center",
                    dataIndex: 'billDate',
                    // customRender:(text)=>{
                    //   if(!text){
                    //     return ''
                    //   }else{
                    //     return filterMultiDictText(this.dictOptions['teardownstype'], text+"")
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
                list: "/teardown/getPage",
                delete: "/teardown/delete",
                deleteBatch: "/teardown/deleteBatch",
                exportXlsUrl: "/teardown/exportXls",
                importExcelUrl: "teardown/importExcel",
            },
            dictOptions: {
                vendorId: [],
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
          searchMaterial('').then((res) => {
            if (res.success) {
              this.mtlList = res.result;
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
        searchMtl (e) {
          searchMaterial({"keyword":e.target.valueOf().value}).then((res) => {
            if (res.success) {
              this.mtlList = res.result;
            }
          })
        },
        diyhandleEdit(e){
            if(e.target.dataset.id)
                this.$router.replace({ path:'/combind/TeardownModal/' + e.target.dataset.id });
            else
                this.$router.replace({ path:'/combind/TeardownModal/' });
        },
        goDetail(id) {
          this.$router.replace({ path:'/combind/TeardownModal/' + id, query: {"unEditable": false} });
        }
    }
}
</script>
