<template>
  <div>
    <a-form style="max-width: 700px; margin: 40px auto 0;" :form="form">
      <a-alert
        :closable="true"
        message="请确认订单信息及费用信息。"
        style="margin-bottom: 24px;"
      />
      <a-form-item
        label="退货单号"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText"
      >
        {{this.saleOrderReturn.code}}
      </a-form-item>
      <a-form-item
        label="销售单号"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText"
      >
        {{this.saleOrderReturn.sourceCode}}
      </a-form-item>
      <a-form-item
        label="客户"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText"
      >
        {{this.saleOrderReturn.customer}}
      </a-form-item>
      <a-form-item
        label="单据时间"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText"
      >
        {{this.saleOrderReturn.billDate}}
      </a-form-item>
      <a-form-item
        label="金额"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText"
      >
        {{this.saleOrderReturn.totalamount}}
      </a-form-item>
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
          :rowSelection="{selectedRowKeys: selectedRowKeys1, onChange: onSelectChange1}"
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
        <a-card class="card" title="收款" :bordered="true">
          <!-- 操作按钮区域 -->
          <div class="table-operator" style="border-top: 5px" v-if = "editType==1">
            <a-button @click="handleAddRefundDtl" type="primary" icon="plus">添加退款明细</a-button>
            <a-dropdown v-if="selectedRowKeys.length > 0">
              <a-menu slot="overlay">
                <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
              </a-menu>
              <a-button style="margin-left: 8px">
                批量操作 <a-icon type="down" />
              </a-button>
            </a-dropdown>
          </div>
          <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
            <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
            selectedRowKeys2.length }}</a>项
            <a style="margin-left: 24px" @click="onClearSelected2">清空</a>
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
            :rowSelection="{selectedRowKeys: selectedRowKeys2, onChange: onSelectChange2}"
            @change="handleTableChange2">
           <span slot="action" slot-scope="text, record" v-if = "editType==1">
            <a @click="handleEdit2(record)">编辑</a>
            <a-divider type="vertical"/>
            <a-dropdown>
              <a class="ant-dropdown-link">
                更多 <a-icon type="down"/>
              </a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete2(record.id)">
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </span>
          </a-table>
        </a-card>
      </div>
      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <!--<a-button :loading="loading" @click="finish">提交</a-button>-->
        <a-button style="margin-left: 8px" @click="prevStep">上一步</a-button>
        <a-button style="margin-left: 8px" type="primary" @click="finish">完成</a-button>
        <a-button v-if="returnTypeSum > 0 && editType ==1" style="margin-left: 8px" type="primary" @click="finishStockIn">完成并通知入库</a-button>
      </a-form-item>
    </a-form>

    <refund-order-dtl-modal ref="refundOrderDtlModal" :sourceBillOrder = "saleOrderReturn" @ok="modalFormOk2" v-on:listenToTotalamont="showTotalMount"></refund-order-dtl-modal>
  </div>
</template>

<script>
  import pick from 'lodash.pick'
  import RefundOrderDtlModal from '../refund/RefundOrderDtlModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { deleteAction, postAction, getAction } from '@/api/manage'
  import {editSaleOrderReturn, getSaleOrderReturnOne, getSaleOrderReturnMtlList, inventoryInSave } from '@/api/api'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  export default {
    name: "Return2",
    mixins: [JeecgListMixin],
    components: {
      RefundOrderDtlModal,
      JDictSelectTag
    },
    data() {
      return {
        saleOrderReturn: {},
        model1: {},
        model2: {},
        currentRoleId: '',
        queryParam1: {},
        queryParam2: {},
        dataSource1: [],
        dataSource2: [],
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
        isorter1: {
          column: 'createTime',
          order: 'desc'
        },
        isorter2: {
          column: 'createTime',
          order: 'desc'
        },
        filters1: {},
        filters2: {},
        loading1: false,
        loading2: false,
        selectedRowKeys1: [],
        selectedRowKeys2: [],
        selectionRows1: [],
        selectionRows2: [],
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
            },
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
              title: '退货方式',
              dataIndex: 'returnTypeName',
              key: 'returnTypeName'
            },
            {
              title: '金额',
              align:"center",
              dataIndex: 'amount'
            }
          ],
        columns2: [{
          title: '退款单号',
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
          list: "/saleOrderReturnMtl/getPage",
          list2: '/refundOrderDtl/getPage',
          delete2: '/refundOrderDtl/delete',
          deleteBatch2: '/refundOrderDtl/deleteBatch',
        },
        returnTypeSum: 0,
        editType: 0
      }
    },
    methods: {
      onSelectChange2(selectedRowKeys, selectionRows) {
        this.selectedRowKeys2 = selectedRowKeys
        this.selectionRows2 = selectionRows
      },
      onClearSelected2() {
        this.selectedRowKeys2 = []
        this.selectionRows2 = []
      },
      onClearSelected1() {
        this.selectedRowKeys1 = []
        this.selectionRows1 = []
      },
      onSelectChange1(selectedRowKeys, selectionRows) {
        this.selectedRowKeys1 = selectedRowKeys
        this.selectionRows1 = selectionRows
        this.model1 = Object.assign({}, selectionRows[0])
        console.log(this.model1)
        this.currentRoleId = selectedRowKeys[0]
        this.loadData2()
      },
      onClearSelected() {
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
          param2.sourceBillId = this.$route.query.id
        }else{
          param2.sourceBillId = -1;
        }
        param2.field = this.getQueryField2()
        param2.pageNo = this.ipagination2.current
        param2.pageSize = this.ipagination2.pageSize
        return param2;
      },
      getQueryField2() {
        var str = 'id,'
        this.columns2.forEach(function(value) {
          str += ',' + value.dataIndex
        })
        return str
      },
      handleEdit2: function(record) {
        this.$refs.refundOrderDtlModal.title = '编辑'
        this.$refs.refundOrderDtlModal.roleDisabled = true
        this.$refs.refundOrderDtlModal.edit(record)
      },
      modalFormOk2() {
        // 新增/修改 成功时，重载列表
        this.loadData2()
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
      handleAddRefundDtl(){
        this.$refs.refundOrderDtlModal.add();
        this.$refs.refundOrderDtlModal.title = "新增";
      },
      showTotalMount(data){
        this.saleOrderReturn.totalamount = data;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            values.sourceId = this.$route.query.id;
            let formData = Object.assign(this.model, values);
            let obj;
            console.log(formData)
            if(this.model.id){
              obj=editSaleOrderReturn(formData);
            }
            obj.then((res)=>{
              if(res.success){
                that.saleOrderReturn.totalamount = res.result;
                console.log(that.saleOrderReturn.totalamount)
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
        })
      },
      finish () {
        /*if (!this.unEditable) {
          this.handleOk();
        }*/
        this.$emit('finish')
      },
      prevStep () {
        this.$emit('prevStep')
      },
      finishStockIn () {
        let formData = {
          "sourceId": this.saleOrderReturn.id,
          "sourceCode": this.saleOrderReturn.code,
          "sourceBillType": this.saleOrderReturn.billType
        };
        inventoryInSave(formData).then((res)=>{
          if(res.success){
            this.$message.success(res.message);
          }else{
            this.$message.warning(res.message);
          }
        }).finally(() => {
        })
        this.$emit('finish')
      },
    },
    mounted() {
      this.editType = this.$route.query.editType;
      if (this.$route.query.id) {
        getSaleOrderReturnOne({id:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.saleOrderReturn = res.result;
            this.model = Object.assign({}, this.saleOrderReturn);
            // this.model.payamount = this.model.totalamount; // 默认填充
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'payamount','receiptType'))
            });
          }
        })
      }
      this.loadData2();
    },
    watch: {
      // 如果 `data` 发生改变，这个函数就会运行
      dataSource: function () {
        debugger
        let typesum = 0;
        this.dataSource.forEach(function(target){
          if (target.returnTypeId) {
            typesum += parseFloat(target.returnTypeId);
          }
        });
        this.returnTypeSum = typesum;
      },
      '$route' (to, from) {
        if (this.$route.query.id) {
          getSaleOrderReturnOne({id:this.$route.query.id}).then((res) => {
            if (res.success) {
              this.saleOrderReturn = res.result;
              this.model = Object.assign({}, this.saleOrderReturn);
            }
          })
        }
        this.loadData2();
        this.editType = this.$route.query.editType;
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