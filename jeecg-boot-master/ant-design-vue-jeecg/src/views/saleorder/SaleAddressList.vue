<template>
  <a-card :bordered="false">
    <!-- 抽屉 -->
    <a-drawer
      title="地址列表"
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
                <a-form-item label="收件人">
                  <a-input style="width: 120px;" placeholder="请输入收件人" v-model="queryParam.recipients"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="9" :sm="24">

                <a-form-item label="类型" style="width: 170px" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select v-model="queryParam.typeId" placeholder="请选择收功能类型" showSearch optionFilterProp="children"
                            notFoundContent="没有匹配的类型"  >
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in typeList" :key="key" :value="item.id">
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
    <address-modal ref="addressModal" :entity = "customer" @ok="modalFormOk"></address-modal>
  </a-card>
</template>

<script>
  import pick from 'lodash.pick'
  import {filterObj} from '@/utils/util';
  import AddressModal from '../basic/AddressModal'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getAddressTypeList, saveOrderAddress, saveInvoiceAddress} from '@/api/api'

  export default {
    name: "SaleAddressList",
    mixins: [JeecgListMixin],
    components: {AddressModal},
    data() {
      return {
        selectedRowKeys1: [],
        columns: [{
          title: '功能类型',
          align: 'center',
          dataIndex: 'typeName',
          width: 60
        },
          {
            title: '收件人',
            align: 'center',
            dataIndex: 'recipients',
            width: 60
          },
          {
            title: '电话',
            align: 'center',
            width: 60,
            dataIndex: 'tel'
          },
          {
            title: '详细地址',
            align: 'center',
            width: 150,
            dataIndex: 'fullAddress'
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
          list: '/address/getPage',
          delete: '/address/delete',
          deleteBatch: '/address/deleteBatch',
        },
        typeList: [],
        customer: {},
        entityOrder: {}
      }
    },
    created() {
      // 当页面初始化时,根据屏幕大小来给抽屉设置宽度
      this.resetScreenSize();
      getAddressTypeList().then((res) => {
        if (res.success) {
          this.typeList = res.result;
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
        this.customer.id = record.customerId;
        this.entityOrder = record;
        this.selectedRowKeys1[0] = record.sourceAddId;

        this.queryParam = {}
        this.form.resetFields();
        this.model = Object.assign({}, record);
        /*this.model.dictId = this.dictId;
        this.model.status = this.status;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'itemText', 'itemValue'))
        });*/
        // 当其它模块调用该模块时,调用此方法加载字典数据
        this.loadData();
      },

      getQueryParams() {
        var param = Object.assign({}, this.queryParam);
        param.sourceId = this.customer.id
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        return filterObj(param);
      },

      // 添加字典数据
      handleAdd() {
        this.$refs.addressModal.add({});
        this.$refs.addressModal.title = "新增";
      },
      handleEdit: function(record) {
        this.$refs.addressModal.title = '编辑'
        this.$refs.addressModal.edit(record)
      },
      showDrawer() {
        this.visible = true
      },
      handleOK () {
        let data = {};
        this.dataSource.forEach(o=>{
          if (o.id == this.selectedRowKeys1[0]) {
            data = o;
            return ;
          }
        })
        data.sourceId = this.entityOrder.id;
        data.sourceAddId = data.id;
        data.id = null;
        if (this.entityOrder.billType == 0) {
          saveOrderAddress(data).then((res) => {
            if (res.success) {
              this.$emit("addressFlag", new Date().getTime());
            }
          });
        } else if (this.entityOrder.billType == 35) {
          saveInvoiceAddress(data).then((res) => {
            if (res.success) {
              this.$emit("addressFlag", new Date().getTime());
            }
          });
        }
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
    }
  }
</script>
<style scoped>
</style>