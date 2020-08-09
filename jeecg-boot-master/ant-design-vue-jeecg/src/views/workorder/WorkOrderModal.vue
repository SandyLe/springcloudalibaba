<template>
<div>
    <a-card :bordered="false">
        <a-form :form="form">
            <a-row>
              <a-col :span="12">
                <a-form-item label="原单类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select v-decorator="['sourceBillType', validatorRules.sourceBillType]" placeholder="原单类型">
                    <a-select-option v-for="(item, key) in billTypeList" :key="key" :value="item.id">
                      {{ item.name }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="原单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="[ 'sourceCode', validatorRules.sourceCode]" placeholder="请输入原单编号" />
                  <a-input v-decorator="[ 'sourceId', {}]" placeholder="原单ID" type="hidden" />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
                <a-col :span="12">
                  <a-form-item label="工单类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select v-decorator="['workTypeId', {}]" placeholder="工单类型">
                      <a-select-option v-for="(item, key) in workTypeList" :key="key" :value="item.id">
                        {{ item.name }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item label="工单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="[ 'code', {}]" :readOnly="true" placeholder="后台自动生成"></a-input>
                  </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="12">
                  <a-form-item label="施工人员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select v-decorator="['operateUserId', {}]" placeholder="施工人员">
                      <a-select-option v-for="(item, key) in userList" :key="key" :value="item.id">
                        {{ item.realname }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item label="施工时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'operateDate', {}]"/>
                  </a-form-item>
                </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="单据时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'billDate', {}]"/>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="完工时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'finishedDate', {}]"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="24">
                <a-form-item label="备注" :labelCol="hlabelCol" :wrapperCol="hwrapperCol">
                  <a-input v-decorator="[ 'content', {}]" placeholder="请输入备注"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
                <a-col :span="24">
                    <a-card title="配件">
                        <form :autoFormCreate="(form) => this.form = form">
                            <a-table :columns="columns" :dataSource="tabledata" :pagination="false" rowKey="id" ref="mtltable">
                                <template v-for="(col, i) in ['mtlId', 'unitId','quantity', 'price', 'amount', 'content', 'action']" :slot="col" clearable slot-scope="text, record, index">
                                    <a-select :style="['mtlId'].indexOf(columns[i].dataIndex) > -1 ? 'width: 250px;' : ''" v-if="['mtlId','unitId'].indexOf(columns[i].dataIndex) > -1" v-decorator="[record[columns[i].dataIndex], {}]" showSearch
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
            <a-row>
              <a-col :span="24">
                <a-card class="card" title="工单地址" :bordered="true">
                  工单地址：<span style="font-weight: bold">{{workAddress.recipients}} &nbsp;</span> <a>{{workAddress.tel}}</a> {{workAddress.fullAddress}}
                  <a v-if = "editType == 1" @click="editAddressItem(workOrder)"><a-icon type="setting"/> 请选择收获地址</a>
                </a-card>
              </a-col>
            </a-row>
        </a-form>
      <sale-address-list ref="saleAddressList" v-on:addressFlag="addressOK"></sale-address-list>

    </a-card>
    <footer-tool-bar>
        <a-button v-if="editType==1" type="primary" @click="handleOk">保存</a-button>
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
import JDate from '@/components/jeecg/JDate'
import moment from 'moment'
import JDictSelectTag from '@/components/dict/JDictSelectTag'
import FooterToolBar from '@/components/tools/FooterToolBar'
import SaleAddressList from '../saleorder/SaleAddressList'
import {
    getAllUser,
    searchMaterial,
    getMaterialUnitList,
    getWorkOrderOne,
    workOrderDtlDelete,
    getBillTypeList,
    getSaleOrderOne,
    getWorkTypeList,
    getWorkAddress
} from '@/api/api'
import ARow from "ant-design-vue/es/grid/Row";
import ACol from "ant-design-vue/es/grid/Col";
export default {
    name: 'WorkOrderModal',
    components: {
      ACol,
      ARow,
      JDate,
      FooterToolBar,
      JDictSelectTag,
      SaleAddressList
    },
    data() {
        return {
            addressSourceId:null,
            userList:[],
            billTypeList:[],
            workTypeList:[],
            hasaddmain: false,
            workOrder: {},
            workAddress:{},
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
              sourceCode: {
                rules: [
                  {required: true, message: '请输入原单编号!'}
                ]
              },
              sourceBillType: {
                rules: [
                  {required: true, message: '请输入原单类型!'}
                ]
              }
            },
            url: {
              add: '/workOrder/add',
              edit: '/workOrder/edit',
              save: '/workOrder/save'
            },
            dictOptions: {
                warehouse: [],
                materiallist: [],
                materialunitlist: []
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
                    title: '金额',
                    dataIndex: 'amount',
                    key: 'amount',
                    width: '10%',
                    scopedSlots: {
                        customRender: 'amount'
                    }
                },
                {
                    title: '备注',
                    dataIndex: 'content',
                    key: 'content',
                    width: '20%',
                    scopedSlots: {
                        customRender: 'content'
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
            editType: 0
        }
    },
    created() {
        this.initDictConfig();
        this.newMember();
        this.add();

    },
    watch: {
    },
    methods: {
        moment,
        initDictConfig() {
            //供应商

            getBillTypeList().then((res) => {
              if (res.success) {
                this.billTypeList = res.result;
              }
            });
            getWorkTypeList().then((res) => {
              if (res.success) {
                this.workTypeList = res.result;
              }
            });
            //产品
            searchMaterial('').then((res) => {
                if (res.success) {
                    if (res.result && res.result.length > 0) {
                        res.result.forEach(function (option) {
                            option.value = option.id;
                            option.text = option.name;
                        })
                    }
                    this.columns[0].list = res.result;
                    this.$set(this.dictOptions, 0, this.columns[0])
                    // this.$set(this.dictOptions, 'materiallist', res.result)
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

            getAllUser().then((res) => {
              if (res.success) {
                this.userList = res.result;
              }
            })
        },
        add() {
          const that = this;
          getWorkOrderOne({"id":this.$route.params.id}).then((res)=>{
            if (res.result) {

              this.workOrder = res.result;
              if (this.$route.query.sourceId) {
                this.workOrder.sourceId = this.$route.query.sourceId;
              }
              if (this.$route.query.sourceBillType) {
                this.workOrder.sourceBillType = this.$route.query.sourceBillType;
              }
              if (this.$route.query.sourceCode) {
                this.workOrder.sourceCode = this.$route.query.sourceCode;
              }
              if(res.result.detaillist){
                this.tabledata = res.result.detaillist;
                for(let i=0;i < this.tabledata.length ; i++){
                  this.tabledata[i].key = i;
                }
              }

              if (this.$route.params.id) {
                getWorkAddress({"sourceId": this.$route.params.id}).then((res) => {
                  if (res.success) {
                    that.workAddress = res.result;
                  }
                });
              }
              this.edit(res.result);
            }
          });
        },
        edit(record) {
            this.form.resetFields()
            this.model = Object.assign({}, record)
            this.visible = true
            this.$nextTick(() => {
                this.form.setFieldsValue(
                    pick(this.model, 'id', 'code', 'sourceId', 'sourceBillType', 'workTypeId', 'sourceCode', 'content', 'warehouseId', 'operateUserId')
                )
                this.form.setFieldsValue({billDate: this.model.billDate ? moment(this.model.billDate) : null})
                this.form.setFieldsValue({operateDate: this.model.operateDate ? moment(this.model.operateDate) : null})
                this.form.setFieldsValue({finishedDate: this.model.finishedDate ? moment(this.model.finishedDate) : null})
            })
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
                  if(!values.billDate){
                    values.billDate = '';
                  }else{
                    values.billDate = values.billDate.format(this.dateFormat);
                  }
                  if(!values.operateDate){
                    values.operateDate = '';
                  }else{
                    values.operateDate = values.operateDate.format(this.dateFormat);
                  }
                  if(!values.finishedDate){
                    values.finishedDate = '';
                  }else{
                    values.finishedDate = values.finishedDate.format(this.dateFormat);
                  }
                  let httpurl = this.url.save
                  let method = 'post'

                  let formData = Object.assign(this.model, values)
                  formData.detaillist = that.tabledata;
                  console.log('表单提交数据', formData)
                  httpAction(httpurl, formData, method)
                      .then(res => {
                          console.log(res);
                          if (res.success) {
                              that.$message.success(res.message)
                              that.$emit('ok')
                              that.hasaddmain = true;
                              that.$emit('close')

                              /*
                              if (!that.model.id) {
                                  that.$refs.inventorymodalForm.add();
                              } else {
                                  that.$refs.inventorymodalForm.edit(res.result.inventory);
                              }
                              that.$refs.inventorymodalForm.disableSubmit = false;*/
                              //

                              // this.$router.replace({
                              //     path: '/workOrder/PurchaseList'
                              // });
                          } else {
                              that.$message.warning(res.message)
                          }
                      })
                      .finally(() => {
                          that.confirmLoading = false
                          that.close()
                          that.$parent.closeRouteViewTab(this.$route.path)
                          that.$router.push({ path:'/workOrder/workOrderList' });
                      })
              }
          })
        },
        handleCancel() {
            this.close()
        },
        popupCallback(row) {
            this.form.setFieldsValue(
                pick(row, 'id', 'code', 'sourceId', 'sourceBillType', 'workTypeId', 'sourceCode', 'content', 'warehouseId', 'operateUserId')
            )
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
                    if (target.discount)
                        target['amount'] = Math.round((parseFloat(target.quantity) * parseFloat(target.price) - parseFloat(target.discount)) * 100)/100;
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
                workOrderDtlDelete({"id" : id}).then((res) => {
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
            this.$router.replace({ path:'/workorder/workOrderList' });
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
      editAddressItem (record) {
        record.customerId = this.addressSourceId;
        record.sourceAddId = this.workAddress.sourceAddId;
        this.$refs.saleAddressList.edit(record);
      },
      addressOK (){
          debugger
        getWorkAddress({"sourceId": this.workOrder.id}).then((res) => {
          if (res.success) {
            this.workAddress = res.result;
          }
        });

        const that = this
        this.form.validateFields((err, values) => {
          if (!err) {
            let httpurl = this.url.save
            let method = 'post'
            let formData = Object.assign(this.model, values)
            formData.billDate = values.billDate.format(this.dateFormat)
            httpAction(httpurl, formData, method)
              .then(res => {
                console.log(res);
                if (res.success) {
                  that.$message.success(res.message)
                  that.$emit('ok')
                  that.model = res.result;
                } else {
                  that.$message.warning(res.message)
                }
              })
              .finally(() => {/*
              that.confirmLoading = false
              that.close()
              that.$parent.closeRouteViewTab(this.$route.path)
              that.$router.push({ path:'/invoice/invoiceList' });*/
              })
          }
        })

      },
    },
    mounted() {
      this.editType = this.$route.query.editType;
      if (this.$route.query.unEditable === false) {
        this.unEditable = this.$route.query.unEditable;
      }
      if (this.$route.query.sourceId) {
        this.form.setFieldsValue({sourceId:this.$route.query.sourceId, sourceBillType:this.$route.query.sourceBillType, sourceCode: this.$route.query.sourceCode, workTypeId: this.$route.query.workTypeId})
        this.workOrder.sourceId = this.$route.query.sourceId;
        this.workOrder.sourceBillType = this.$route.query.sourceBillType;
        this.workOrder.sourceCode = this.$route.query.sourceCode;

        if (this.$route.query.sourceBillType == 0) {
          const that = this;
          getSaleOrderOne({id:this.$route.query.sourceId}).then((res) => {
            if (res.success) {
              that.addressSourceId = res.result.customerId;
              that.form.setFieldsValue({sourceId:that.$route.query.sourceId, sourceBillType:that.$route.query.sourceBillType, sourceCode: that.$route.query.sourceCode})
            }
          })
        }
      }
    }
}
</script>
<style scoped>
.hide{
    display: none;
}
</style>
