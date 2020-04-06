<template>
<div>
    <a-card :bordered="false">
        <a-form :form="form">
            <a-row>
              <a-col :span="12">
                <a-form-item label="采购批次号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select v-decorator="['batchNo', {}]" placeholder="请输入批次号，入库计算同批次产品平均采购成本">
                    <a-select-option v-for="(item, key) in dictOptions.purchaseBatch" :key="key" :value="item.code">
                      {{ item.code }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
                <a-col :span="12">
                    <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-select v-decorator="['vendorId', {}]" placeholder="请选择供应商">
                            <a-select-option v-for="(item, key) in dictOptions.vendorId" :key="key" :value="item.id">
                                {{ item.name }}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item label="采购单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="[ 'code', {}]" :readOnly="true" placeholder="后台自动生成"></a-input>
                  </a-form-item>
                </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="仓库" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select v-decorator="['warehouseId', {}]" placeholder="请选择仓库">
                    <a-select-option v-for="(item, key) in dictOptions.warehouse" :key="key" :value="item.id">
                      {{ item.name }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
                <a-col :span="12">
                    <a-form-item label="结算账户" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="[ 'account', {}]" placeholder="请输入结算账户" />
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="12">
                    <a-form-item label="实付金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number v-decorator="[ 'payamount', {}]" placeholder="请输入实付金额" style="width:100%" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="总金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number v-decorator="[ 'totalamount', {}]" placeholder="请输入总金额" style="width:100%"/>
                        <div style="display:none;">
                            <a-input v-decorator="[ 'id', {}]" placeholder="实体主键" type="hidden" />
                            <a-input v-decorator="[ 'code', {}]" placeholder="代码" type="hidden"/>
                        </div>
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
                                <template v-for="(col, i) in ['mtlId', 'unitId','quantity', 'price', 'discount', 'amount', 'content', 'action']" :slot="col" clearable slot-scope="text, record, index">
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
import JDate from '@/components/jeecg/JDate'
import JDictSelectTag from '@/components/dict/JDictSelectTag'
import FooterToolBar from '@/components/tools/FooterToolBar'
import InventoryModal from '../inventory/InventoryInModal'
import {
    getVendorList,
    ajaxGetDictItems,
    getWarehouseList,
    searchMaterial,
    getMaterialUnitList,
    purchasequeryById,
    purchasedetailDelete,
    getPurchaseBatchList
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
                vendorId: {},
                businessDate: {
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
                add: '/purchase/add',
                edit: '/purchase/edit'
            },
            vendorIddictOptions: [],
            warehouseOptions: [],
            dictOptions: {
                vendorId: [],
                warehouse: [],
                materiallist: [],
                materialunitlist: [],
                purchaseBatch:[]
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
                    title: '折扣',
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
        initDictConfig() {
            //供应商
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
            //仓库
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
          //仓库
          getPurchaseBatchList('').then((res) => {
            if (res.success) {
              if (res.result && res.result.length > 0) {
                res.result.forEach(function (option) {
                  option.value = option.code;
                  option.text = option.code;
                })
              }
              this.$set(this.dictOptions, 'purchaseBatch', res.result)
            }
          });
        },
        add() {
            if(this.$route.params.id){
                purchasequeryById({"id":this.$route.params.id}).then((res)=>{
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
                    pick(this.model, 'id', 'code', 'vendorId', 'content', 'warehouseId', 'account', 'payamount', 'totalamount', 'batchNo')
                )
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
                          that.$router.push({ path:'/purchase/PurchaseList' });
                      })
              }
          })
        },
        handleCancel() {
            this.close()
        },
        popupCallback(row) {
            this.form.setFieldsValue(
                pick(row, 'code', 'vendorId', 'content', 'warehouseId', 'account', 'payamount', 'totalamount')
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
        }
    },
    mounted() {
      if (this.$route.query.unEditable === false) {
        this.unEditable = this.$route.query.unEditable;
      }
    }
}
</script>
<style scoped>
.hide{
    display: none;
}
</style>
