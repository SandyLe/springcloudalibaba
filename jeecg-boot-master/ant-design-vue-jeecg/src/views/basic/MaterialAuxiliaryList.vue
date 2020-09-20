<template>
  <a-card :bordered="false">
    <!-- 抽屉 -->
    <a-drawer
      title="辅助属性组合"
      :width="screenWidth"
      @close="onClose"
      :visible="visible"
    >
      <!-- 抽屉内容的border -->
      <div
        :style="{
          padding:'10px',
          border: '1px solid #e9e9e9',
          background: '#fff',
        }">

        <div class="table-page-search-wrapper">
          <a-form layout="inline" :form="form" @keyup.enter.native="searchQuery">
            <a-row :gutter="10">
              <a-col :md="8" :sm="12">
                <a-form-item label="辅助属性">
                  <a-select v-model="queryParam.suppCode" placeholder="请选择辅助属性" showSearch optionFilterProp="children"
                            notFoundContent="没有匹配的辅助属性" @change="suppChange">
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in supplementaryList" :key="key" :value="item.code">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="9" :sm="24">

                <a-form-item label="值" style="width: 170px" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select v-model="queryParam.suppValueCode" placeholder="请选择辅助属性值" showSearch optionFilterProp="children"
                            notFoundContent="没有匹配的辅助属性值"  >
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in supplementaryValList" :key="key" :value="item.code">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="7" :sm="24">
              <span style="float: left;" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery">搜索</a-button>
                <a-button type="primary" @click="searchReset" style="margin-left: 8px">重置</a-button>
              </span>
              </a-col>
            </a-row>
            <a-row>
              <a-col :md="2" :sm="24">
                <a-button style="margin-bottom: 10px" type="primary" @click="handleAdd">新增</a-button>
              </a-col>
            </a-row>
          </a-form>
        </div>
        <div>
          <a-table
            ref="table"
            rowKey="id"
            size="middle"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="ipagination"
            :loading="loading"
            :rowSelection="{selectedRowKeys: selectedRowKeys1, onChange: onSelectChange1, type:'radio'}"
            @change="handleTableChange"
          >

          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical"/>
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
              <a>删除</a>
            </a-popconfirm>
          </span>

          </a-table>
        </div>

        <div style=" width: 100px; margin-left: auto"><a-button style="margin-bottom: 10px;" type="primary" @click="handleOK">确定</a-button></div>
        <div></div>
      </div>
    </a-drawer>
    <material-auxiliary-item-modal ref="materialAuxiliaryItemModal" :entity = "materialAuxiliary" @ok="modalFormOk"></material-auxiliary-item-modal>
  </a-card>
</template>

<script>
  import pick from 'lodash.pick'
  import {filterObj} from '@/utils/util';
  import MaterialAuxiliaryItemModal from './MaterialAuxiliaryItemModal'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getSupplementaryList, saveOrderMaterialAuxiliary, saveInvoiceMaterialAuxiliary, saveWorkMaterialAuxiliary,getSupplementaryValListBySourceCode} from '@/api/api'

  export default {
    name: "MaterialAuxiliaryList",
    mixins: [JeecgListMixin],
    components: {MaterialAuxiliaryItemModal},
    data() {
      return {
        selectedRowKeys1: [],
        columns: [{
          title: '辅助属性',
          align: 'center',
          dataIndex: 'suppName',
          width: 60
        },
        {
          title: '辅助属性值',
          align: 'center',
          dataIndex: 'suppValueName',
          width: 60
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 120
        }],
        queryParam: {
          dictId: "",
          dictName: "",
          itemText: "",
          delFlag: "1",
          status: [],
        },
        title: "操作",
        visible: false,
        screenWidth: 800,
        model: {},
        dictId: "",
        status: 1,
        labelCol: {
          xs: {span: 5},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 12},
          sm: {span: 12},
        },
        form: this.$form.createForm(this),
        validatorRules: {
          itemText: {rules: [{required: true, message: '请输入名称!'}]},
          itemValue: {rules: [{required: true, message: '请输入数据值!'}]},
        },
        url: {
          list: '/materialAuxiliaryItem/getPage',
          delete: '/materialAuxiliaryItem/delete',
          deleteBatch: '/materialAuxiliaryItem/deleteBatch',
        },
        supplementaryList: [],
        supplementaryValList: [],
        materialAuxiliary: {},
        entityOrder: {}
      }
    },
    created() {
      // 当页面初始化时,根据屏幕大小来给抽屉设置宽度
      this.resetScreenSize();
      getSupplementaryList().then((res) => {
        if (res.success) {
          this.supplementaryList = res.result;
        }
      });
      this.selectedRowKeys1 = new Array();
      this.selectedRowKeys1[0]='1286871926753574914';
    },
    methods: {
      add(dictId) {
        this.dictId = dictId;
        this.edit({});
      },
      edit(record) {
        this.visible = true;
        this.materialAuxiliary = record;

        this.queryParam = {}
        this.form.resetFields();
        this.model = Object.assign({}, record);
        // 当其它模块调用该模块时,调用此方法加载字典数据
        this.loadData();
      },

      getQueryParams() {
        var param = Object.assign({}, this.queryParam);
        param.sourceId = this.materialAuxiliary.id;
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        return filterObj(param);
      },

      // 添加字典数据
      handleAdd() {
        this.$refs.materialAuxiliaryItemModal.edit({sourceId: this.materialAuxiliary.id});
        this.$refs.materialAuxiliaryItemModal.title = "新增";
      },
      handleEdit: function(record) {
        this.$refs.materialAuxiliaryItemModal.title = '编辑'
        this.$refs.materialAuxiliaryItemModal.edit(record)
      },
      showDrawer() {
        this.visible = true
      },
      handleOK () {
        this.$emit("auxiliaryFlag", new Date().getTime());
        this.onClose();
      },
      onClose() {
        this.visible = false
        this.form.resetFields();
        this.dataSource = [];
      },
      // 抽屉的宽度随着屏幕大小来改变
      resetScreenSize() {
        let screenWidth = document.body.clientWidth;
        if (screenWidth < 600) {
          this.screenWidth = screenWidth;
        } else {
          this.screenWidth = 600;
        }
      },
      onSelectChange1(selectedRowKeys, selectionRows) {
        this.selectedRowKeys1 = selectedRowKeys
        this.selectionRows1 = selectionRows
        this.currentRoleId = selectedRowKeys[0]
      },
      suppChange (e) {

        getSupplementaryValListBySourceCode({sourceCode:e}).then((res) => {
          if (res.success) {
            this.supplementaryValList = res.result;
          }
        });
      }
    }
  }
</script>
<style scoped>
</style>