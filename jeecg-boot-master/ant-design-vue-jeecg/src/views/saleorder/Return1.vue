<template>
  <div>
  <a-card :bordered="false">
    <a-form :form="form">
      <a-row :gutter="24">
        <a-col :md="6" :sm="6">
          <a-form-item
            :labelCol="{span: 8}"
            :wrapperCol="{span: 16}"
            label="原单编号">
            <!--<a-input placeholder="请输入账号查询" v-model="queryParam.username"></a-input>-->
            <a-input placeholder="输入原销售单号" ref=saleOrderNo v-decorator="[ 'sourceCode', validatorRules.sourceCode]" ></a-input>
            <a-input v-decorator="[ 'sourceId', {}]" ref=saleOrderId placeholder="原单ID" type="hidden" />
          </a-form-item>
        </a-col>
        <a-col :md="2" :sm="12">
          <a-form-item
            :labelCol="{span: 5}"
            :wrapperCol="{span: 19}"
            label="">
            <a-button type="primary" @click="searchSaleOrder" icon="search">查询</a-button>
          </a-form-item>
        </a-col>
        <a-col :md="6" :sm="12">
          <a-form-item
            :labelCol="{span: 5}"
            :wrapperCol="{span: 19}"
            label="客户">
            <a-select v-decorator="['customerId', validatorRules.customerId]" placeholder="请选择客户"  showSearch
                      optionFilterProp="children"
                      notFoundContent="无法找到">
              <a-select-option value="">请选择</a-select-option>
              <a-select-option v-for="(item, key) in customerList" :key="key" :value="item.id">
                {{ item.name || item.code }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :md="6" :sm="6">
          <a-form-item
            :labelCol="{span: 5}"
            :wrapperCol="{span: 19}"
            label="单号">
            <a-input placeholder="自动生成单号" :readOnly="true" v-decorator="[ 'code', {}]" :disabled="false" />
          </a-form-item>
        </a-col>
        <a-col :md="4" :sm="6">
          <a-form-item
            :labelCol="{span: 5}"
            :wrapperCol="{span: 19}"
            label="渠道">
            <a-tree-select
              style="width:100%"
              :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
              :treeData="treeData"
              v-model="model.channelId"
              placeholder="请选择销售渠道"
              @change="handleParentIdChange">
            </a-tree-select>
            <!-- <j-dict-select-tag v-decorator="['channelId', {}]" placeholder="请选择销售渠道" :type="'select'" style="width: 60%" :triggerChange="true" dictCode="channel" :disabled="unEditable" :readOnly = "unEditable" /> -->
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :md="6" :sm="6">
          <a-form-item
            :labelCol="{span: 5}"
            :wrapperCol="{span: 19}"
            label="订单日期">
            <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'billDate', {}]" />
          </a-form-item>
        </a-col>
        <a-col :md="18" :sm="18">
          <a-form-item
            label="备注"
            :labelCol="{span: 2}"
            :wrapperCol="{span: 22}">
            <a-input placeholder="请输入备注" v-decorator="[ 'content', {}]" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="24">
          <a-card>
            <form :autoFormCreate="(form) => this.form = form">
              <a-table :columns="columns" :dataSource="tabledata" :pagination="false" rowKey="id" ref="mtltable">
                <template v-for="(col, i) in ['mtlId', 'unitId','quantity', 'price', 'discount', 'amount', 'returnTypeId', 'action']" :slot="col" clearable slot-scope="text, record, index">
                  <a-select :style="['mtlId'].indexOf(columns[i].dataIndex) > -1 ? 'width: 250px;' : ''" v-if="['mtlId','unitId','returnTypeId'].indexOf(columns[i].dataIndex) > -1" v-decorator="[record[columns[i].dataIndex], {}]" showSearch
                            optionFilterProp="children" notFoundContent="无法找到，输入关键词回车[Enter]搜索试试" @keyup.enter.native="e => searchData(e, col, record.key)"
                            @change="e => handleChange(e, record.key, col)" :placeholder="'请选择'+columns[i].title" :value="record[columns[i].dataIndex]" ref="sel">
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in columns[i].list" :key="key" :value="item.id" :title="item.info">
                      {{ item.info || item.name }}
                    </a-select-option>
                  </a-select>
                  <a-input :key="col" v-else style="margin: -5px 0" :value="text" :placeholder="columns[i].title" @change="e => handleChange(e.target.value, record.key, col)" />
                  <!-- <template v-else>{{ text }}</template> -->
                </template>

                <template slot="operation" slot-scope="text, record, index">
                  <template v-if="record.editable">
                    <span v-if="record.isNew">
                        <!-- <a @click="saveRow(record.key)">添加</a>
                        <a-divider type="vertical" /> -->
                        <a-popconfirm title="是否要删除此行？" @confirm="remove(record.key,'')">
                            <a>删除</a>
                        </a-popconfirm>
                    </span>
                    <span v-else>
                        <a @click="saveRow(record.key)">保存</a>
                        <a-divider type="vertical" />
                        <a @click="cancel(record.key)">取消</a>
                    </span>
                  </template>
                  <span v-else>
                      <!-- <a @click="toggle(record.key)">编辑</a>
                      <a-divider type="vertical" /> -->
                      <a-popconfirm v-if="editType==1" title="是否要删除此行？" :data-id="record.id" @confirm="remove(record.key,record.id)">
                          <a>删除</a>
                      </a-popconfirm>
                      <div style="display:none;">
                          <a-input v-decorator="[ 'id', {}]" placeholder="实体主键" type="hidden"/>
                          <a-input v-decorator="[ 'code', {}]" placeholder="代码" type="hidden"/>
                      </div>
                  </span>
                </template>
              </a-table>
              <a-button v-if="editType==1" style="width: 100%; margin-top: 16px; margin-bottom: 8px" type="dashed" icon="plus" @click="newMember">新增产品</a-button>
            </form>
          </a-card>
        </a-col>
      </a-row>
    </a-form>


  </a-card>
  <footer-tool-bar>
    <a-button v-if="editType == 1" type="primary" @click="handleOk">保存下一步</a-button>
    <a-button v-if="editType == 0" type="primary" @click="nextStep">下一步</a-button>
    <router-view :key="this.$route.path"></router-view>
    <a-button :style="{marginLeft:'20px'}" @click="backToList">返回</a-button>
  </footer-tool-bar>

  </div>
</template>

<script>

  import {
    httpAction
  } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from 'moment'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import FooterToolBar from '@/components/tools/FooterToolBar'
  import {
    ajaxGetDictItems,
    searchMaterial,
    getMaterialUnitList,
    findSaleOrderReturnQueryDto,
    purchasedetailDelete,

    searchCustomer,
    getReturnTypeList,
    getSaleOrderByCode,
    getMaterialListByIds,
    queryChannelTreeList
  } from '@/api/api'
  export default {
    name: 'PurchasesModal',
    components: {
      JDate,
      FooterToolBar,
      JDictSelectTag
    },
    data() {
      return {
        treeData:[],
        editType: 0,
        dateFormat:"YYYY-MM-DD HH:mm:ss",
        form: this.$form.createForm(this),
        title: '操作',
        width: '80%',
        visible: false,
        model: {},
        labelCol: {
          xs: {
            span: 24
          },
          sm: {
            span: 5
          }
        },
        wrapperCol: {
          xs: {
            span: 24
          },
          sm: {
            span: 16
          }
        },
        hlabelCol: {
          xs: { span: 24 },
          sm: { span: 2 },
        },
        hwrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          vendorId: {
            rules: [{
              required: true,
              message: '请选择供应商!'
            }]
          },
          batchNo: {
            rules: [{
              required: true,
              message: '请选择批次编号!'
            }]
          },
          amount: {},
          description: {},
          states: {}
        },
        url: {
          add: '/saleOrderReturn/add',
          edit: '/saleOrderReturn/edit',
          save: '/saleOrderReturn/save'
        },
        customerList: [],
        dictOptions: {
        },
        columns: [{
            title: '产品', //顺序不要调整，getMaterialList中有用
            dataIndex: 'mtlId',
            key: 'mtlId',
            width: '20%',
            scopedSlots: {
              customRender: 'mtlId'
            }
          },
          {
            title: '单位', //顺序不要调整，getMaterialUnitList中有用
            dataIndex: 'unitId',
            key: 'unitId',
            width: '10%',
            scopedSlots: {
              customRender: 'unitId'
            }
          },
          {
            title: '数量',
            dataIndex: 'quantity',
            key: 'quantity',
            width: '10%',
            scopedSlots: {
              customRender: 'quantity'
            }
          },
          {
            title: '单价',
            dataIndex: 'price',
            key: 'price',
            width: '10%',
            scopedSlots: {
              customRender: 'price'
            }
          },
          {
            title: '折扣%',
            dataIndex: 'discount',
            key: 'discount',
            width: '10%',
            scopedSlots: {
              customRender: 'discount'
            }
          },
          {
            title: '金额',
            dataIndex: 'amount',
            key: 'amount',
            width: '10%',
            scopedSlots: {
              customRender: 'amount'
            }
          },
          {
            title: '退货方式', //
            dataIndex: 'returnTypeId',
            key: 'returnTypeId',
            width: '15%',
            scopedSlots: {
              customRender: 'returnTypeId'
            }
          },
          {
            title: '操作',
            key: 'action',
            width: '10%',
            scopedSlots: {
              customRender: 'operation'
            }
          }
        ],
        tabledata: [],
        mtlIds: [],
        editType: 0
      }
    },
    created() {
      this.initDictConfig();
      this.newMember();
      this.add();

    },
    watch: {
      // 如果 `data` 发生改变，这个函数就会运行
      tabledata: function () {
        // console.log(newdata);
        let datatotalamount = 0;
        this.tabledata.forEach(function(target){
          if (target.quantity && target.price) {
            if (target.discount){
              datatotalamount += parseFloat(target.quantity).toFixed(2) * parseFloat(target.price).toFixed(2);
              datatotalamount = datatotalamount - datatotalamount * (1-(parseFloat(target.discount)/100).toFixed(2));
            }
            else
              datatotalamount += parseFloat(target.quantity) * parseFloat(target.price).toFixed(2);
          }
        });
        datatotalamount = Math.round(datatotalamount*100)/100;
        this.model.totalamount = datatotalamount;
        this.form.setFieldsValue({ 'totalamount' :datatotalamount })
      }
    },
    methods: {

      loadTree(){
        var that = this;
        queryChannelTreeList().then((res)=>{
          if(res.success){
            console.log('----queryChannelTreeList1---')
            console.log(res)
            that.treeData = [];
            let treeList = res.result.treeList
            for(let a=0;a<treeList.length;a++){
              let temp = treeList[a];
              temp.isLeaf = temp.leaf;
              that.treeData.push(temp);
            }
          }
        });
      },
      initDictConfig() {
        //客户
        searchCustomer('').then((res) => {
          if (res.success) {
            if (res.result && res.result.length > 0) {
              res.result.forEach(function (option) {
                option.value = option.id;
                option.text = option.name;
              })
            }
            this.customerList = res.result;
          }
        });
        //仓库
        getReturnTypeList('').then((res) => {
          if (res.success) {
            if (res.result && res.result.length > 0) {
              res.result.forEach(function (option) {
                option.value = option.id;
                option.text = option.name;
              })
            }
            this.columns[6].list = res.result;
            this.$set(this.dictOptions, 6, this.columns[6])
          }
        });
        //单位
        getMaterialUnitList('').then((res) => {
          if (res.success) {
            if (res.result && res.result.length > 0) {
              res.result.forEach(function (option) {
                option.value = option.id;
                option.text = option.name;
              })
            }
            this.columns[1].list = res.result;
            this.$set(this.dictOptions, 1, this.columns[1])
            // this.$set(this.dictOptions, 'materialunitlist', res.result)
          }
        });
      },
      add() {
        if(this.$route.query.id || this.$route.query.sourceId){
          findSaleOrderReturnQueryDto({"id":this.$route.query.id,"sourceId":this.$route.query.sourceId}).then((res)=>{
            if (res.result) {
              if (!res.result.id) {
                delete res.result.billDate;
              }

              searchCustomer({'id': res.result.customerId}).then((res) => {
                if (res.success) {
                  if (res.result && res.result.length > 0) {
                    res.result.forEach(function (option) {
                      option.value = option.id;
                      option.text = option.name;
                    })
                  }
                  this.customerList = res.result;
                }
              });

              if(res.result.detaillist){
                this.tabledata = res.result.detaillist;
                for(let i=0;i < this.tabledata.length ; i++){
                  this.tabledata[i].key = i;
                  this.mtlIds[i] = this.tabledata[i].mtlId;
                }
                //产品
                getMaterialListByIds({"ids": this.mtlIds.join(",")}).then((res) => {
                  if (res.success) {
                    if (res.result && res.result.length > 0) {
                      res.result.forEach(function (option) {
                        option.value = option.id;
                        option.text = option.name;
                      })
                    }
                    this.columns[0].list = res.result;
                    this.$set(this.dictOptions, 0, this.columns[0])

                    const newData = [...this.tabledata]
                    this.tabledata = newData;
                    // this.$set(this.dictOptions, 'materiallist', res.result)
                  }
                });
              }
              this.edit(res.result);
            }
          });
        }
        else
          this.edit({})
      },
      edit(record) {

        if (!record.billDate) {
          record.billDate = new Date();
        }
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(
            pick(this.model, 'id', 'code', 'customerId', 'content', 'sourceId', 'sourceCode', 'channelId')
          )
        })

        this.loadTree();
        this.form.setFieldsValue({billDate: this.model.billDate ? moment(this.model.billDate) : null})
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
        const that = this
        // 触发表单验证

        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            let httpurl = this.url.save
            let method = 'post'
            let formData = Object.assign(this.model, values)
            formData.billDate = values.billDate.format(this.dateFormat);
            formData.detaillist = that.tabledata;
            console.log('表单提交数据', formData)
            httpAction(httpurl, formData, method)
              .then(res => {
                if (res.success) {
                  this.$route.query.id = res.result.id;
                  this.$emit('nextStep')
                } else {
                  that.$message.warning(res.message)
                }
              })
              .finally(() => {
                this.$emit('nextStep')
              })
          }
        })
      },
      nextStep () {
        this.$emit('nextStep')
      },
      handleCancel() {
        this.close()
      },
      newMember() {
        this.tabledata.push({
          key: this.tabledata.length,
          name: '',
          workId: '',
          department: '',
          editable: true,
          isNew: true
        })
      },
      handleChange(value, key, column) {
        const newData = [...this.tabledata]
        const target = newData.filter(item => key === item.key)[0]
        if (target) {
          target[column] = value;
          if (target.quantity && target.price) {
            if (target.discount){
              let datatotalamount = parseFloat(target.quantity).toFixed(2) * parseFloat(target.price).toFixed(2);
              datatotalamount = datatotalamount - datatotalamount * (1-(parseFloat(target.discount)/100).toFixed(2));
              target['amount'] = Math.round(datatotalamount * 100)/100;
            }
            else
              target['amount'] = Math.round( (parseFloat(target.quantity) * parseFloat(target.price)) * 100)/100;
          }
          this.tabledata = newData
        }
      },
      toggle(key) {
        let target = this.tabledata.filter(item => item.key === key)[0]
        target.editable = !target.editable
      },
      cancel(key) {
        let target = this.tabledata.filter(item => item.key === key)[0]
        target.editable = false
      },
      remove(key,id) {
        const newData = this.tabledata.filter(item => item.key !== key)
        this.tabledata = newData;
        if(id){
          purchasedetailDelete({"id" : id}).then((res) => {
            if (res.success) {
            }
          });
        }
      },
      saveRow(key) {
        let target = this.tabledata.filter(item => item.key === key)[0]
        target.editable = false
        target.isNew = false
      },
      backToList() {
        // console.log(this.$route.matched);
        this.$route.matched.splice(this.$route.matched.length-1 ,1);
        this.$parent.closeRouteViewTab(this.$route.fullPath)
        this.$router.replace({ path:'/purchase/PurchaseList' });
      },
      searchData(e, col, key) {
        if (col === 'mtlId') {
          const that = this;
          searchMaterial({"keyword":e.target.valueOf().value}).then((res) => {
            if (res.success) {
              if (res.result && res.result.length > 0) {
                res.result.forEach(function (option) {
                  option.value = option.id;
                  option.text = option.name;
                })
              }
              that.columns[0].list = res.result;
              that.$set(that.dictOptions, 0, that.columns[0])

              const newData = [...this.tabledata]
              const target = newData.filter(item => key === item.key)[0]
              target.editable = true
              that.tabledata = newData;
              // this.$set(this.dictOptions, 'materiallist', res.result)
            }
          });
        }
      },
      searchSaleOrder () {

        let saleOrderNo = this.$refs.saleOrderNo.value;
        if (saleOrderNo) {
          getSaleOrderByCode({code:saleOrderNo}).then((res) => {
            if (res.success) {
              let saleOrder = res.result;
              saleOrder.sourceCode = saleOrderNo;
              saleOrder.sourceId = saleOrder.id;
              saleOrder.code = null;
              saleOrder.id = null;
              delete saleOrder.billDate;
              delete saleOrder.createBy;
              delete saleOrder.createTime;
              delete saleOrder.updateBy;
              delete saleOrder.updateTime;
              delete saleOrder.billStatus;

              searchCustomer({'id': saleOrder.customerId}).then((res) => {
                if (res.success) {
                  if (res.result && res.result.length > 0) {
                    res.result.forEach(function (option) {
                      option.value = option.id;
                      option.text = option.name;
                    })
                  }
                  this.customerList = res.result;
                }
              });

              this.edit(saleOrder)
            }
          })
        } else {
          this.$message.warning('请输入原单编号！');
        }
      },
      handleParentIdChange(value){
        if(!value){
          this.validateStatus="error"
        }else{
          this.validateStatus="success"
        }
      }
    },
    mounted() {
      this.editType = this.$route.query.editType;
      this.form.setFieldsValue({sourceId:this.$route.query.sourceId, sourceCode: this.$route.query.sourceCode});

    }
  }
</script>
