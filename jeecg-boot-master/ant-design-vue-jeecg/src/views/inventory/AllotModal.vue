<template>
<div>
    <a-card :bordered="false">
        <a-form :form="form">
          <a-row>
              <a-col :span="12">
                <a-form-item label="调出仓库" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select v-decorator="['fromWarehouseId', {}]" ref="fromWarehouseId" placeholder="请选择仓库">
                    <a-select-option v-for="(item, key) in warehouseList" :key="key" :value="item.id">
                      {{ item.name }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="调入仓库" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select v-decorator="['toWarehouseId', {}]" ref="toWarehouseId" placeholder="请选择仓库">
                    <a-select-option v-for="(item, key) in warehouseList" :key="key" :value="item.id">
                      {{ item.name }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="[ 'code', {}]" :readOnly="true" placeholder="后台自动生成"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                  <a-form-item label="单据时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'billdate', {}]"/>
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
                    <a-card>
                        <form :autoFormCreate="(form) => this.form = form">
                            <a-table :columns="columns" :dataSource="tabledata" :pagination="false" rowKey="id" ref="mtltable">
                                <template v-for="(col, i) in ['mtlId', 'unitId','allotAmount', 'fromAmount', 'toAmount','content', 'action']" :slot="col" clearable slot-scope="text, record, index">
                                    <a-select :style="['mtlId'].indexOf(columns[i].dataIndex) > -1 ? 'width: 250px;' : ''" v-if="['mtlId','unitId'].indexOf(columns[i].dataIndex) > -1" v-decorator="[record[columns[i].dataIndex], {}]" showSearch
                                              optionFilterProp="children" notFoundContent="无法找到，输入关键词回车[Enter]搜索试试" @keyup.enter.native="e => searchData(e, col, record.key)"
                                              @change="e => handleChange(e, record.key, col)" :placeholder="'请选择'+columns[i].title" :value="record[columns[i].dataIndex]" ref="sel">
                                        <a-select-option value="">请选择</a-select-option>
                                        <a-select-option v-for="(item, key) in columns[i].list" :key="key" :value="item.id" :title="item.info">
                                            {{ item.info || item.name }}
                                        </a-select-option>
                                    </a-select>
                                    <a-input :key="col" v-else style="margin: -5px 0" :readOnly="['fromAmount','toAmount'].indexOf(columns[i].dataIndex) > -1" :value="text" :placeholder="columns[i].title" @change="e => handleChange(e.target.value, record.key, col)" />
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
                                        <a-popconfirm title="是否要删除此行？" :data-id="record.id" @confirm="remove(record.key,record.id)">
                                            <a>删除</a>
                                        </a-popconfirm>
                                        <div style="display:none;">
                                            <a-input v-decorator="[ 'id', {}]" placeholder="实体主键" type="hidden"/>
                                            <a-input v-decorator="[ 'code', {}]" placeholder="代码" type="hidden"/>
                                        </div>
                                    </span>
                                </template>
                            </a-table>
                            <a-button style="width: 100%; margin-top: 16px; margin-bottom: 8px" type="dashed" icon="plus" @click="newMember">新增产品</a-button>
                        </form>
                    </a-card>
                </a-col>
            </a-row>
            <a-row>
              <a-col :span="24">
                    <inventory-modal ref="inventorymodalForm" ></inventory-modal>
              </a-col>
            </a-row>
        </a-form>


    </a-card>
    <footer-tool-bar>
        <a-button v-if="unEditable" type="primary" @click="handleOk">保存</a-button>
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
import InventoryModal from '../inventory/InventoryInModal'
import {
    getWarehouseList,
    searchMaterial,
    getMaterialUnitList,
    getAllotOne,
    purchasedetailDelete,
    getinventoryList
} from '@/api/api'
export default {
    name: 'PurchasesModal',
    components: {
        JDate,
        FooterToolBar,
        JDictSelectTag,
        InventoryModal
    },
    data() {
        return {
            dateFormat:"YYYY-MM-DD HH:mm:ss",
            warehouseList: [],
            hasaddmain: false,
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
                fromWarehouseId: {},
                toWarehouseId: {},
                billdate: {
                    rules: [{
                        required: true,
                        message: '请输入业务时间!'
                    }]
                },
                amount: {},
                description: {},
                states: {}
            },
            url: {
                add: '/allot/add',
                edit: '/allot/edit'
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
                    dataIndex: 'allotAmount',
                    key: 'allotAmount',
                    width: '10%',
                    scopedSlots: {
                        customRender: 'allotAmount'
                    }
                },
                {
                  title: '调出仓库当前库存',
                  dataIndex: 'fromAmount',
                  key: 'fromAmount',
                  width: '15%',
                  scopedSlots: {
                    customRender: 'fromAmount'
                  }
                },
                {
                  title: '调入仓库当前库存',
                  dataIndex: 'toAmount',
                  key: 'toAmount',
                  width: '15%',
                  scopedSlots: {
                    customRender: 'toAmount'
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
            unEditable: true
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
                    if (target.discount)
                        datatotalamount += parseFloat(target.quantity).toFixed(2) * parseFloat(target.price).toFixed(2) - parseFloat(target.discount).toFixed(2);
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
        moment,
        initDictConfig() {
            //仓库
            getWarehouseList('').then((res) => {
                if (res.success) {
                    if (res.result && res.result.length > 0) {
                      this.warehouseList = res.result;
                    }
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
                    const newData = [...this.tabledata]
                    newData.editable = true
                    this.tabledata = newData;
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
                }
            });
        },
        add() {
            if(this.$route.params.id){
              getAllotOne({"id":this.$route.params.id}).then((res)=>{
                    if (res.result) {
                        if(res.result.detaillist){
                            this.tabledata = res.result.detaillist;
                            for(let i=0;i < this.tabledata.length ; i++){
                                this.tabledata[i].key = i;
                            }
                        }
                        this.edit(res.result);
                    }
                });
            }
            else
                this.edit({})
        },
        edit(record) {
            this.form.resetFields()
            this.model = Object.assign({}, record)
            this.visible = true
            this.$nextTick(() => {
                this.form.setFieldsValue(
                    pick(this.model, 'id', 'code', 'content', 'fromWarehouseId', 'toWarehouseId')
                )
              this.form.setFieldsValue({billdate: this.model.billdate ? moment(this.model.billdate) : null})
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
                  if(!values.billdate){
                    values.billdate = '';
                  }else{
                    values.billdate = values.billdate.format(this.dateFormat);
                  }
                  that.confirmLoading = true
                  let httpurl = ''
                  let method = ''
                  if (!this.model.id) {
                      httpurl += this.url.add
                      method = 'post'
                  } else {
                      httpurl += this.url.edit
                      method = 'put'
                  }
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
                              //     path: '/purchase/PurchaseList'
                              // });
                          } else {
                              that.$message.warning(res.message)
                          }
                      })
                      .finally(() => {
                          that.confirmLoading = false
                          that.close()
                          that.$parent.closeRouteViewTab(this.$route.path)
                          that.$router.push({ path:'/inventory/AllotList' });
                      })
              }
          })
        },
        handleCancel() {
            this.close()
        },
        popupCallback(row) {
            this.form.setFieldsValue(
                pick(row, 'code', 'vendorId', 'content', 'fromWarehouseId', 'toWarehouseId')
            )
          this.form.setFieldsValue({billdate: this.model.billdate ? moment(this.model.billdate) : null})
        },
        newMember() {
            this.tabledata.push({
                key: this.tabledata.length,
                mtlId: '',
                unitId: '',
                allotAmount: '',
                fromAmount: '',
                toAmount: '',
                editable: true,
                isNew: true
            })
        },
        handleChange(value, key, column) {

            const newData = [...this.tabledata]
            const target = newData.filter(item => key === item.key)[0]
            target[column] = value;
            if ('mtlId' === column){
              const fromWarehouseId = this.$refs.fromWarehouseId.value;
              const toWarehouseId = this.$refs.toWarehouseId.value;
              debugger
              getinventoryList({"warehouseId" : fromWarehouseId, "mtlId": value}).then((res) => {
                if (res.success) {
                  if (res.result.length>0){
                    target.mtlId = res.result[0].mtlId;
                    target.unitId = res.result[0].unitId;
                    target.fromAmount = res.result[0].stockAmount;
                    this.tabledata = newData
                  }
                }
              });

              getinventoryList({"warehouseId" : toWarehouseId, "mtlId": value}).then((res) => {
                if (res.success) {
                  if (res.result.length>0){
                    target.mtlId = res.result[0].mtlId;
                    target.unitId = res.result[0].unitId;
                    target.toAmount = res.result[0].stockAmount;
                    this.tabledata = newData
                  }
                }
              });
            }
          this.tabledata = newData
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
            this.$router.replace({ path:'/inventory/AllotList' });
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

                const newData = [...this.tabledata]
                const target = newData.filter(item => key === item.key)[0]
                target.editable = true
                that.tabledata = newData;
              }
            });
          }
        }
    },
    mounted() {
      if (this.$route.query.unEditable === false) {
        this.unEditable = this.$route.query.unEditable;
      };
    }
}
</script>
<style scoped>
.hide{
    display: none;
}
</style>
