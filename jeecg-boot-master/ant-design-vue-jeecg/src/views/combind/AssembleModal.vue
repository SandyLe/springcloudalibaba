<template>
<div>
    <a-card :bordered="false">
        <a-form :form="form">
            <a-row>
                <a-col :span="12">
                  <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="产品">
                    <a-select v-decorator="['mtlId', validatorRules.mtlId]" placeholder="请选择产品"  showSearch
                              optionFilterProp="children" @change="mtlChange"
                              notFoundContent="无法找到，输入关键词Enter搜索" @keyup.enter.native="searchMtl" >
                      <a-select-option value="">请选择</a-select-option>
                      <a-select-option v-for="(item, key) in mtlList" :key="key" :value="item.id">
                        {{ item.info }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item label="组装单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="[ 'code', {}]" :readOnly="true" placeholder="后台自动生成"></a-input>
                  </a-form-item>
                </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="辅助属性" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select v-decorator="['auxiliaryId', {}]" placeholder="请选择辅助属性">
                    <a-select-option v-for="(item, key) in dictOptions.auxiliaryList" :key="key" :value="item.id">
                      {{ item.suppValueMap }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="单位">
                  <a-select v-decorator="['unitId', {}]" placeholder="单位" >
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in unitList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name">
                      {{ item.name }}
                    </span>
                    </a-select-option>
                  </a-select>
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
                  <a-form-item label="单据时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'billDate', {}]"/>
                  </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="12">
                  <a-form-item label="数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="[ 'quantity', {}]" placeholder="组装数量"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item label="新单价" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="[ 'price', {}]" placeholder="新单价" style="width:100%" />
                  </a-form-item>
                  <div style="display:none;">
                    <a-input v-decorator="[ 'id', {}]" placeholder="实体主键" type="hidden" />
                    <a-input v-decorator="[ 'code', {}]" placeholder="代码" type="hidden"/>
                  </div>
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
                                <template v-for="(col, i) in ['mtlId','auxiliaryId', 'unitId','quantity', 'content', 'action']" :slot="col" clearable slot-scope="text, record, index">
                                    <a-select :style="['auxiliaryId','mtlId'].indexOf(columns[i].dataIndex) > -1 ? 'width: 250px;' : ''" v-if="['mtlId','auxiliaryId','unitId'].indexOf(columns[i].dataIndex) > -1" v-decorator="[record[columns[i].dataIndex], {}]" showSearch
                                              optionFilterProp="children" notFoundContent="无法找到，输入关键词回车[Enter]搜索试试" @keyup.enter.native="e => searchData(e, col, record.key)"
                                              @change="e => handleChange(e, record.key, col)" :placeholder="'请选择'+columns[i].title" :value="record[columns[i].dataIndex]" ref="sel">
                                        <a-select-option value="">请选择</a-select-option>
                                        <a-select-option v-for="(item, key) in columns[i].list" :key="key" :value="item.id" :title="item.info">
                                          {{ ['auxiliaryId'].indexOf(columns[i].dataIndex) > -1 ? item.suppValueMap : (item.info || item.text) }}
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
import {
    ajaxGetDictItems,
    getWarehouseList,
    searchMaterial,
    getMaterialUnitList,
    getAssembleOne,
    assembleDtlDelete,
    getMaterialAuxiliaryList,
    getMaterialAuxiliaryListBySourceIds
} from '@/api/api'
export default {
    name: 'AssembleModal',
    components: {
        JDate,
        FooterToolBar,
        JDictSelectTag
    },
    data() {
        return {
            dateFormat:"YYYY-MM-DD HH:mm:ss",
            hasaddmain: false,
            mtlList:[],
            unitList:[],
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
              mtlId: {
                rules: [
                  {required: true, message: '请选择产品!'}
                ]
              }
            },
            url: {
                add: '/assemble/add',
                edit: '/assemble/edit'
            },
            vendorIddictOptions: [],
            warehouseOptions: [],
            dictOptions: {
                vendorId: [],
                warehouse: [],
                materiallist: [],
                materialunitlist: []
            },
            columns: [{
                    title: '子件产品', //顺序不要调整，getMaterialList中有用
                    dataIndex: 'mtlId',
                    key: 'mtlId',
                    width: '20%',
                    scopedSlots: {
                        customRender: 'mtlId'
                    }
                },{
                  title: '辅助属性', //顺序不要调整，getMaterialList中有用
                  dataIndex: 'auxiliaryId',
                  key: 'auxiliaryId',
                  width: '20%',
                  scopedSlots: {
                    customRender: 'auxiliaryId'
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
            mtlIds:[],
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
        }
    },
    methods: {
        moment,
        initDictConfig() {
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
                    this.mtlList = res.result;
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
                    this.columns[2].list = res.result;
                    this.unitList = res.result;
                    this.$set(this.dictOptions, 2, this.columns[2])
                    // this.$set(this.dictOptions, 'materialunitlist', res.result)
                }
            });
        },
        add() {
            if(this.$route.params.id){
                getAssembleOne({"id":this.$route.params.id}).then((res)=>{
                    if (res.result) {
                        if(res.result.detaillist){
                            this.tabledata = res.result.detaillist;
                            for(let i=0;i < this.tabledata.length ; i++){
                                this.tabledata[i].key = i;
                                this.mtlIds[i] = this.tabledata[i].mtlId;
                            }
                        }
                        getMaterialAuxiliaryList({sourceId:res.result.mtlId}).then((res) => {
                          if (res.success) {
                            if (res.result.length>0){
                              res.result.forEach(function (option) {
                                option.value = option.id;
                                option.text = option.suppValueMap;
                              })
                              this.$set(this.dictOptions, 'auxiliaryList', res.result);
                            }
                          }
                        })
                        getMaterialAuxiliaryListBySourceIds({"sourceIds": this.mtlIds.join(",")}).then((res) => {
                          if (res.success) {
                            if (res.result && res.result.length > 0) {
                              res.result.forEach(function (option) {
                                option.value = option.id;
                                option.text = option.suppValueMap;
                              })
                            }
                            this.columns[1].list = res.result;
                            this.$set(this.dictOptions, 1, this.columns[1])
                            // this.$set(this.dictOptions, 'materiallist', res.result)
                            const newData = [...this.tabledata]
                            this.tabledata = newData
                          }
                        });
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
                    pick(this.model, 'id', 'code', 'mtlId', 'content', 'warehouseId', 'unitId', 'quantity', 'auxiliaryId','price')
                )
                this.form.setFieldsValue({billDate: this.model.billDate ? moment(this.model.billDate) : null})
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
                          that.$router.push({ path:'/combind/AssembleList' });
                      })
              }
          })
        },
        handleCancel() {
            this.close()
        },
        popupCallback(row) {
            this.form.setFieldsValue(
                pick(row, 'code', 'mtlId', 'content', 'warehouseId', 'unitId', 'quantity', 'price')
            )
            this.form.setFieldsValue({billDate: this.model.billDate ? moment(this.model.billDate) : null})
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
                if (column === 'mtlId'){
                  getMaterialAuxiliaryList({"sourceId" : value}).then((res) => {
                    if (res.success) {
                      if (res.result.length>0){
                        res.result.forEach(function (option) {
                          option.value = option.id;
                          option.text = option.suppValueMap;
                        })
                        this.columns[1].list = res.result;
                        this.$set(this.dictOptions, 1, this.columns[1])
                        const newData = [...this.tabledata]
                        this.tabledata = newData
                      }
                    }
                  });
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
                assembleDtlDelete({"id" : id}).then((res) => {
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
            this.$router.replace({ path:'/combind/AssembleList' });
        },
        searchMtl (e) {
          searchMaterial({"keyword":e.target.valueOf().value}).then((res) => {
            if (res.success) {
              this.mtlList = res.result;
            }
          })
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
        mtlChange (val) {
          getMaterialAuxiliaryList({sourceId:val}).then((res) => {
            if (res.success) {
              if (res.result.length>0){
                res.result.forEach(function (option) {
                  option.value = option.id;
                  option.text = option.suppValueMap;
                })
                this.$set(this.dictOptions, 'auxiliaryList', res.result);
              }
            }
          })
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
