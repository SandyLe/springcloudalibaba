<template>
  <div>
    <a-form style="max-width: 700px; margin: 40px auto 0;" :form="form">
      <a-alert
        :closable="true"
        message="请确认订单信息及费用信息。"
        style="margin-bottom: 24px;"
      />
      <a-form-item
        label="销售单号"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText"
      >
        {{this.saleOrder.code}}
      </a-form-item>
      <a-form-item
        label="客户"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText"
      >
        {{this.saleOrder.customer}}
      </a-form-item>
      <a-form-item
        label="单据时间"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText"
      >
        {{this.saleOrder.billDate}}
      </a-form-item>
      <a-form-item
        label="金额"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText"
      >
        {{this.saleOrder.totalamount}}
      </a-form-item>
      <div style="margin-top: 15px">
        <a-card class="card" title="收货信息" :bordered="true">
        收货地址：<span style="font-weight: bold">{{saleOrderAddress.recipients}} &nbsp;</span> <a>{{saleOrderAddress.tel}}</a> {{saleOrderAddress.fullAddress}}
          <a v-if = "!unEditable" @click="editAddressItem(saleOrder)"><a-icon type="setting"/> 请选择收获地址</a>
        </a-card>
      </div>
      <div style="margin-top: 15px">
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          @change="handleTableChange">
          <span slot="nameAction" slot-scope="text, record">
            <a @click="goDetail(record.mtlId)">{{record.mtl}}</a>
          </span>
          <span slot="action" slot-scope="text, record">
          <a @click="handleOpen(record)">用户</a>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete1(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
        </a-table>
      </div>
      <div>
        <a-card class="card" title="其他费用" :bordered="true">
          <!-- 操作按钮区域 -->
          <div class="table-operator" style="border-top: 5px" v-if = "!unEditable">
            <a-button @click="handleAddExpense" type="primary" icon="plus">添加费用</a-button>
          </div>
          <a-table
            ref="table2"
            bordered
            size="middle"
            rowKey="id"
            :columns="columns2"
            :dataSource="dataSource2"
            :pagination="ipagination2"
            :loading="loading2"
            @change="handleTableChange2">
           <span slot="action" slot-scope="text, record" v-if = "!unEditable">
            <a @click="handleEdit2(record)">编辑</a>
            <a-divider type="vertical"/>
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete2(record.id)">
              <a>删除</a>
            </a-popconfirm>
          </span>
          </a-table>
        </a-card>
      </div>
      <div>
        <a-card class="card" title="成本" :bordered="true">
          <!-- 操作按钮区域 -->
          <div class="table-operator" style="border-top: 5px" v-if = "!unEditable">
            <a-button @click="handleAddCost" type="primary" icon="plus">添加成本</a-button>
          </div>
          <a-table
            ref="table3"
            bordered
            size="middle"
            rowKey="id"
            :columns="columns3"
            :dataSource="dataSource3"
            :pagination="ipagination3"
            :loading="loading3"
            @change="handleTableChange3">
           <span slot="action" slot-scope="text, record" v-if = "!unEditable">
            <a @click="handleEdit3(record)">编辑</a>
            <a-divider type="vertical"/>
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete3(record.id)">
              <a>删除</a>
            </a-popconfirm>
          </span>
          </a-table>
        </a-card>
      </div>
      <div>
        <a-card class="card" title="收款" :bordered="true">
          <div class="table-operator" style="border-top: 5px" v-if = "!unEditable">
            <a-button @click="handleAddReceiptDtl" type="primary" icon="plus">添加收款明细</a-button>
          </div>
          <a-table
            ref="table4"
            bordered
            size="middle"
            rowKey="id"
            :columns="columns4"
            :dataSource="dataSource4"
            :pagination="ipagination4"
            :loading="loading4"
            @change="handleTableChange4">
           <span slot="action" slot-scope="text, record" v-if = "!unEditable">
              <a @click="handleEdit4(record)">编辑</a>
              <a-divider type="vertical"/>
              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete4(record.id)">
                <a>删除</a>
              </a-popconfirm>
            </span>
          </a-table>
        </a-card>
      </div>
      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <!--<a-button :loading="loading" @click="nextStep">提交</a-button>-->
        <a-button style="margin-left: 8px" @click="prevStep">上一步</a-button>
        <a-button style="margin-left: 8px" type="primary" @click="nextStep">下一步</a-button>
      </a-form-item>
    </a-form>

    <sale-order-expense-modal ref="saleOrderExpenseModal" :saleOrder = "saleOrder" @ok="modalFormOk2" v-on:listenToTotalamont="showTotalMount"></sale-order-expense-modal>
    <sale-order-cost-modal ref="saleOrderCostModal" :saleOrder = "saleOrder" @ok="modalFormOk3"></sale-order-cost-modal>
    <receipt-order-dtl-modal ref="receiptOrderDtlModal" :sourceBillOrder = "saleOrder" @ok="modalFormOk4"></receipt-order-dtl-modal>
    <sale-address-list ref="saleAddressList" v-on:addressFlag="modalFormOk5"></sale-address-list>
  </div>
</template>

<script>
  import pick from 'lodash.pick'
  import SaleOrderExpenseModal from './SaleOrderExpenseModal'
  import SaleOrderCostModal from './SaleOrderCostModal'
  import ReceiptOrderDtlModal from '../receipt/ReceiptOrderDtlModal'
  import SaleAddressList from './SaleAddressList'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { deleteAction, postAction, getAction } from '@/api/manage'
  import { getSaleOrderOne, getSaleOrderMtlList, getOrderAddress } from '@/api/api'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  export default {
    name: "Step2",
    mixins: [JeecgListMixin],
    components: {
      SaleOrderExpenseModal,
      SaleOrderCostModal,
      ReceiptOrderDtlModal,
      JDictSelectTag,
      SaleAddressList
    },
    data() {
      return {
        saleOrder: {},
        model1: {},
        model2: {},
        model3: {},
        model4: {},
        currentRoleId: '',
        queryParam1: {},
        queryParam2: {},
        queryParam3: {},
        queryParam4: {},
        dataSource1: [],
        dataSource2: [],
        dataSource3: [],
        dataSource4: [],
        ipagination1: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        ipagination2: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        ipagination3: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        ipagination4: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        isorter1: {
          column: 'createTime',
          order: 'desc'
        },
        isorter2: {
          column: 'createTime',
          order: 'desc'
        },
        isorter3: {
          column: 'createTime',
          order: 'desc'
        },
        isorter4: {
          column: 'createTime',
          order: 'desc'
        },
        filters1: {},
        filters2: {},
        filters3: {},
        filters4: {},
        loading1: false,
        loading2: false,
        loading3: false,
        loading4: false,
        columns:
          [
            {
              title: '#',
              dataIndex: '',
              key:'rowIndex',
              width:60,
              align:"center",
              customRender:function (t,r,index) {
                return parseInt(index)+1;
              }
            },,
            {
              title: '产品名称',
              align:"center",
              dataIndex: '',
              scopedSlots: { customRender: 'nameAction' }
            },
            {
              title: '产品编码',
              align:"center",
              dataIndex: 'mtlCode'
            },
            {
              title: '数量',
              align:"center",
              dataIndex: 'quantity'
            },
            {
              title: '单位',
              align:"center",
              dataIndex: 'unit'
            },
            {
              title: '单价',
              align:"center",
              dataIndex: 'price'
            },
            {
              title: '折扣',
              align:"center",
              dataIndex: 'discount'
            },
            {
              title: '折扣类型',
              dataIndex: 'discountTypeName',
              align:"center"
            },
            {
              title: '金额',
              align:"center",
              dataIndex: 'amount'
            }
          ],
        columns2: [{
          title: '费用名称',
          align: 'center',
          dataIndex: 'expense',
          width: 120
        },
          {
            title: '费用',
            align: 'center',
            width: 100,
            dataIndex: 'amount'
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
            align: 'center',
            width: 120
          }],
        columns3: [{
            title: '收款方',
            align: 'center',
            dataIndex: 'payee',
            width: 120
          },
          {
            title: '费用名称',
            align: 'center',
            dataIndex: 'expense',
            width: 120
          },
          {
            title: '费用',
            align: 'center',
            width: 100,
            dataIndex: 'amount'
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
            align: 'center',
            width: 120
          }],
        columns4: [{
            title: '销售单号',
            align: 'center',
            dataIndex: 'sourceBillCode',
            width: 120
          },{
            title: '收款单号',
            align: 'center',
            dataIndex: 'sourceCode',
            width: 120
          },
          {
            title: '费用名称',
            align: 'center',
            dataIndex: 'expenseName',
            width: 120
          },
          {
            title: '金额',
            align: 'center',
            width: 100,
            dataIndex: 'payAmount'
          },
          {
            title: '付款方式',
            align: 'center',
            dataIndex: 'payTypeName',
            width: 120
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
            align: 'center',
            width: 120
          }],
        form: this.$form.createForm(this),
        url: {
          list: "/saleOrderMtl/getPage",
          list2: '/saleOrderExpense/getPage',
          delete2: '/saleOrderExpense/delete',
          deleteBatch2: '/saleOrderExpense/deleteBatch',
          list3: '/saleOrderCost/getPage',
          delete3: '/saleOrderCost/delete',
          deleteBatch3: '/saleOrderCost/deleteBatch',
          list4: '/receiptOrderDtl/getPage',
          delete4: '/receiptOrderDtl/delete',
          deleteBatch4: '/receiptOrderDtl/deleteBatch',
        },
        unEditable: true,
        saleOrderAddress: {}
      }
    },
    methods: {
      editAddressItem (record) {
        record.sourceAddId = this.saleOrderAddress.sourceAddId;
        this.$refs.saleAddressList.edit(record);
      },
      getQueryParams(){
        let param = {};
        if(this.$route.query.id){
          param.sourceId = this.$route.query.id
        }else{
          param.sourceId = -1;
        }
        return param;
      },
      getQueryParams2() {
        //获取查询条件
        let param2 = {};
        if(this.$route.query.id){
          param2.sourceId = this.$route.query.id
        }else{
          param2.sourceId = -1;
        }
        param2.field = this.getQueryField2()
        param2.pageNo = this.ipagination2.current
        param2.pageSize = this.ipagination2.pageSize
        return param2;
      },
      getQueryParams3() {
        //获取查询条件
        let param3 = {};
        if(this.$route.query.id){
          param3.sourceId = this.$route.query.id
        }else{
          param3.sourceId = -1;
        }
        param3.field = this.getQueryField3()
        param3.pageNo = this.ipagination3.current
        param3.pageSize = this.ipagination3.pageSize
        return param3;
      },
      getQueryParams4() {
        //获取查询条件
        let param4 = {};
        if(this.$route.query.id){
          param4.sourceBillId = this.$route.query.id
        }else{
          param4.sourceBillId = -1;
        }
        param4.field = this.getQueryField4()
        param4.pageNo = this.ipagination4.current
        param4.pageSize = this.ipagination4.pageSize
        return param4;
      },
      getQueryField2() {
        var str = 'id,'
        this.columns2.forEach(function(value) {
          str += ',' + value.dataIndex
        })
        return str
      },
      getQueryField3() {
        var str = 'id,'
        this.columns3.forEach(function(value) {
          str += ',' + value.dataIndex
        })
        return str
      },
      getQueryField4() {
        var str = 'id,'
        this.columns4.forEach(function(value) {
          str += ',' + value.dataIndex
        })
        return str
      },
      handleEdit2: function(record) {
        this.$refs.saleOrderExpenseModal.title = '编辑'
        this.$refs.saleOrderExpenseModal.roleDisabled = true
        this.$refs.saleOrderExpenseModal.edit(record)
      },
      handleEdit3: function(record) {
        this.$refs.saleOrderCostModal.title = '编辑'
        this.$refs.saleOrderCostModal.roleDisabled = true
        this.$refs.saleOrderCostModal.edit(record)
      },
      handleEdit4: function(record) {
        this.$refs.receiptOrderDtlModal.title = '编辑'
        this.$refs.receiptOrderDtlModal.edit(record)
      },
      modalFormOk2() {
        // 新增/修改 成功时，重载列表
        this.loadData2()
      },
      modalFormOk3() {
        // 新增/修改 成功时，重载列表
        debugger
        this.loadData3()
      },
      modalFormOk4() {
        // 新增/修改 成功时，重载列表
        this.loadData4()
      },
      loadData2(arg) {
        if (!this.url.list2) {
          this.$message.error('请设置url.list2属性!')
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination2.current = 1
        }
        let params = this.getQueryParams2()//查询条件
        params.roleId = this.currentRoleId
        this.loading2 = true
        getAction(this.url.list2, params).then((res) => {
          if (res.success) {
            this.dataSource2 = res.result.records
            this.ipagination2.total = res.result.total

          }
          this.loading2 = false
        })

      },
      loadData3(arg) {
        if (!this.url.list2) {
          this.$message.error('请设置url.list3属性!')
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination3.current = 1
        }
        let params = this.getQueryParams3()//查询条件
        params.roleId = this.currentRoleId
        this.loading3 = true
        getAction(this.url.list3, params).then((res) => {
          if (res.success) {
            this.dataSource3 = res.result.records
            this.ipagination3.total = res.result.total

          }
          this.loading3 = false
        })

      },
      loadData4(arg) {
        if (!this.url.list4) {
          this.$message.error('请设置url.list3属性!')
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination4.current = 1
        }
        let params = this.getQueryParams4()//查询条件
        params.roleId = this.currentRoleId
        this.loading4 = true
        getAction(this.url.list4, params).then((res) => {
          if (res.success) {
            this.dataSource4 = res.result.records
            this.ipagination4.total = res.result.total

          }
          this.loading4 = false
        })

      },
      handleDelete1: function(id) {
        this.handleDelete(id)
        this.dataSource2 = []
        this.currentRoleId = ''
      },
      handleDelete2: function(id) {
        if (!this.url.delete2) {
          this.$message.error('请设置url.delete2属性!')
          return
        }
        var that = this
        deleteAction(that.url.delete2, { id: id }).then((res) => {
          if (res.success) {
            that.$message.success(res.message)
            that.loadData2()
            that.saleOrder.totalamount = res.result;
            that.model.payamount = res.result; // 默认填充
            that.model.totalamount = res.result;
            that.$nextTick(() => {
              that.form.setFieldsValue(pick(this.model,'payamount', 'totalamount'))
            });
          } else {
            that.$message.warning(res.message)
          }
        })
      },
      handleDelete3: function(id) {
        if (!this.url.delete3) {
          this.$message.error('请设置url.delete3属性!')
          return
        }
        var that = this
        deleteAction(that.url.delete3, { id: id }).then((res) => {
          if (res.success) {
            that.$message.success(res.message)
            that.loadData3();
          } else {
            that.$message.warning(res.message)
          }
        })
      },
      handleDelete4: function(id) {
        if (!this.url.delete4) {
          this.$message.error('请设置url.delete4属性!')
          return
        }
        var that = this
        deleteAction(that.url.delete4, { id: id }).then((res) => {
          if (res.success) {
            that.$message.success(res.message)
            that.loadData4();
          } else {
            that.$message.warning(res.message)
          }
        })
      },
      batchDel2: function() {

        if (!this.url.deleteBatch2) {
          this.$message.error('请设置url.deleteBatch2属性!')
          return
        }
        if (this.selectedRowKeys2.length <= 0) {
          this.$message.warning('请选择一条记录！')
          return
        } else {
          var ids = ''
          for (var a = 0; a < this.selectedRowKeys2.length; a++) {
            ids += this.selectedRowKeys2[a] + ','
          }
          var that = this
          console.log(this.currentDeptId)
          this.$confirm({
            title: '确认删除',
            content: '是否删除选中数据?',
            onOk: function() {
              deleteAction(that.url.deleteBatch2, { id: ids }).then((res) => {
                if (res.success) {
                  that.$message.success(res.message)
                  that.loadData2()
                  that.onClearSelected()
                  that.saleOrder.totalamount = res.result;
                  that.model.payamount = res.result; // 默认填充
                  that.model.totalamount = res.result;
                  that.$nextTick(() => {
                    that.form.setFieldsValue(pick(this.model,'payamount', 'totalamount'))
                  });
                } else {
                  that.$message.warning(res.message)
                }
              })
            }
          })
        }
      },
      batchDel3: function() {

        if (!this.url.deleteBatch3) {
          this.$message.error('请设置url.deleteBatch3属性!')
          return
        }
        if (this.selectedRowKeys3.length <= 0) {
          this.$message.warning('请选择一条记录！')
          return
        } else {
          var ids = ''
          for (var a = 0; a < this.selectedRowKeys3.length; a++) {
            ids += this.selectedRowKeys3[a] + ','
          }
          var that = this
          console.log(this.currentDeptId)
          this.$confirm({
            title: '确认删除',
            content: '是否删除选中数据?',
            onOk: function() {
              deleteAction(that.url.deleteBatch3, { id: ids }).then((res) => {
                if (res.success) {
                  that.$message.success(res.message)
                  that.loadData2()
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message)
                }
              })
            }
          })
        }
      },
      batchDel4: function() {

        if (!this.url.deleteBatch4) {
          this.$message.error('请设置url.deleteBatch4属性!')
          return
        }
        if (this.selectedRowKeys4.length <= 0) {
          this.$message.warning('请选择一条记录！')
          return
        } else {
          var ids = ''
          for (var a = 0; a < this.selectedRowKeys43.length; a++) {
            ids += this.selectedRowKeys4[a] + ','
          }
          var that = this
          console.log(this.currentDeptId)
          this.$confirm({
            title: '确认删除',
            content: '是否删除选中数据?',
            onOk: function() {
              deleteAction(that.url.deleteBatch4, { id: ids }).then((res) => {
                if (res.success) {
                  that.$message.success(res.message)
                  that.loadData4()
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message)
                }
              })
            }
          })
        }
      },
      handleTableChange2(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        if (Object.keys(sorter).length > 0) {
          this.isorter2.column = sorter.field
          this.isorter2.order = 'ascend' == sorter.order ? 'asc' : 'desc'
        }
        this.ipagination2 = pagination
        this.loadData2()
      },
      handleTableChange3(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        if (Object.keys(sorter).length > 0) {
          this.isorter3.column = sorter.field
          this.isorter3.order = 'ascend' == sorter.order ? 'asc' : 'desc'
        }
        this.ipagination3 = pagination
        this.loadData3()
      },
      handleTableChange4(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        if (Object.keys(sorter).length > 0) {
          this.isorter4.column = sorter.field
          this.isorter4.order = 'ascend' == sorter.order ? 'asc' : 'desc'
        }
        this.ipagination4 = pagination
        this.loadData4()
      },
      handleAddExpense(){
        this.$refs.saleOrderExpenseModal.add();
        this.$refs.saleOrderExpenseModal.title = "新增费用";
      },
      handleAddCost(){
        this.$refs.saleOrderCostModal.add();
        this.$refs.saleOrderCostModal.title = "新增成本";
      },
      handleAddReceiptDtl(){
        this.$refs.receiptOrderDtlModal.add();
        this.$refs.receiptOrderDtlModal.title = "新增收款明细";
      },
      showTotalMount(data){
        this.saleOrder.totalamount = data;
        this.model.payamount = data; // 默认填充
        this.model.totalamount = data;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'payamount', 'totalamount'))
        });
      },
      nextStep () {
        this.$emit('nextStep')
      },
      prevStep () {
        this.$emit('prevStep')
      },
      modalFormOk5() {
        getOrderAddress({sourceId:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.saleOrderAddress = res.result;
          }
        })
      }
    },
    mounted() {
      if (this.$route.query.id) {
        getSaleOrderOne({id:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.saleOrder = res.result;
            this.model = Object.assign({}, this.saleOrder);
            this.model.payamount = this.model.totalamount; // 默认填充
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'payamount','receiptType'))
            });
          }
        })
        getOrderAddress({sourceId:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.saleOrderAddress = res.result;
          }
        })
      }
      this.loadData2();
      this.loadData3();
      this.loadData4();
      this.unEditable = this.$route.query.unEditable;
    },
    watch: {
      '$route' (to, from) {
      }
    }
  }
</script>

<style lang="scss" scoped>
  .stepFormText {
    margin-bottom: 24px;

    .ant-form-item-label,
    .ant-form-item-control {
      line-height: 22px;
    }
  }

</style>