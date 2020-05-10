<template>
<div>
    <a-card :bordered="false">
        <a-form :form="form">
            <a-row>
                <a-col :span="12">
                    <a-form-item label="原单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input v-decorator="[ 'sourceCode', {}]" placeholder="请输入原单编号" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item label="物流单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="[ 'code', {}]" :readOnly="true" placeholder="后台自动生成"></a-input>
                  </a-form-item>
                </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="单据类型">
                  <a-select v-decorator="['sourceBillType', validatorRules.sourceBillType]" placeholder="请选择单据类型" showSearch optionFilterProp="children"
                            notFoundContent="没有匹配的单据类型"  >
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in billTypeList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="默认收货方式"
                  label-width="4">
                  <j-dict-select-tag v-decorator="['cdiDefaultType', {}]" placeholder="请选择默认发货方式" :type="'select'" :triggerChange="true" dictCode="delivery_type"/>
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
                  <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="物流公司"
                    label-width="4">
                    <a-select v-decorator="['cdiLogisticsId', {}]" placeholder="请选择物流公司">
                      <a-select-option value="">请选择</a-select-option>
                      <a-select-option v-for="(item, key) in cdiLogisticsCompanyList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
            </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="收件人"
                label-width="4">
                <a-input placeholder="请输入收件人" v-decorator="[ 'cdiRecipients', validatorRules.cdiRecipients]" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="联系电话"
                label-width="4">
                <a-input placeholder="请输入联系电话" v-decorator="[ 'cdiRecipientsPhone', validatorRules.cdiRecipientsPhone]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="物流单号"
                label-width="4">
                <a-input placeholder="请输入物流单号" v-decorator="[ 'cdiLogisticsNo', validatorRules.cdiLogisticsNo]" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="邮政编码"
                label-width="4">
                <a-input placeholder="请输入邮政编码" v-decorator="[ 'postCode', validatorRules.postCode]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="件数"
                label-width="4">
                <a-input placeholder="请输入件数" v-decorator="[ 'number', validatorRules.number]" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="总重量"
                label-width="4">
                <a-input placeholder="请输入总重量" v-decorator="[ 'totalWeight', validatorRules.totalWeight]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="保价"
                label-width="4">
                <a-input placeholder="请输入保价" v-decorator="[ 'insurance', validatorRules.insurance]" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="费用总计"
                label-width="4">
                <a-input placeholder="请输入费用总计" v-decorator="[ 'totalCharge', validatorRules.totalCharge]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :md="24" :sm="24">
              <a-form-item
                :labelCol="hlabelCol"
                :wrapperCol="hwrapperCol"
                label="收货地址">
                <a-select v-decorator="['cdiProvince', {}]" placeholder="省" style="width: 15%" @change="areaChangeCdi" >
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in provinceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
                <a-select v-decorator="['cdiCity', {}]" placeholder="市" style="width: 15%" @change="areaChangeCdi" >
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in cityList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
                <a-select v-decorator="['cdiDistrict', {}]" placeholder="区、县" style="width: 15%">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in districtList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
                <a-input placeholder="请输入详细地址" v-decorator="[ 'cdiAddress', validatorRules.cdiAddress]" style="width: 55%"  />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="24">
              <a-form-item label="发货地址" :labelCol="hlabelCol" :wrapperCol="hwrapperCol">
                <a-input v-decorator="[ 'cdiDeliveryAddress', {}]" placeholder="请输入发货地址"></a-input>
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
                                <template v-for="(col, i) in ['mtlId', 'quantity', 'unitId','content', 'action']" :slot="col" clearable slot-scope="text, record, index">
                                    <a-select :style="['mtlId'].indexOf(columns[i].dataIndex) > -1 ? 'width: 250px;' : ''" v-if="['mtlId', 'unitId'].indexOf(columns[i].dataIndex) > -1" v-decorator="[record[columns[i].dataIndex], {}]" showSearch
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
import moment from 'moment'
import JDate from '@/components/jeecg/JDate'
import JDictSelectTag from '@/components/dict/JDictSelectTag'
import FooterToolBar from '@/components/tools/FooterToolBar'
import {
    getAreaList,
    getBillTypeList,
    ajaxGetDictItems,
    getLogisticsCompanyList,
    searchMaterial,
    getMaterialUnitList,
    getLogisticsOrderOne,
    logisticsOrderDtlDelete
} from '@/api/api'
export default {
    name: 'LogisticsOrderModal',
    components: {
        JDate,
        FooterToolBar,
        JDictSelectTag
    },
    data() {
        return {
            dateFormat:"YYYY-MM-DD HH:mm:ss",
            billTypeList: [],
            provinceList: [],
            cityList: [],
            districtList:[],
            cdiLogisticsCompanyList:[],
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
              sourceBillType: {
                    rules: [{
                        required: true,
                        message: '请选择单据类型!'
                    }]
                },
              cdiRecipients: {
                rules: [{
                  required: true,
                  message: '请输入收件人!'
                }]
              },
              cdiRecipientsPhone: {
                rules: [{
                  required: true,
                  message: '请输入收件人电话!'
                }]
              },
              cdiAddress: {
                rules: [{
                  required: true,
                  message: '请输入收件人地址!'
                }]
              }
            },
            url: {
                add: '/logisticsOrder/add',
                edit: '/logisticsOrder/edit'
            },
            dictOptions: {
                vendorId: [],
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
                  title: '数量',
                  dataIndex: 'quantity',
                  key: 'quantity',
                  width: '10%',
                  scopedSlots: {
                    customRender: 'quantity'
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
        this.newMember();
        this.add();

    },
    watch: {
        // 如果 `data` 发生改变，这个函数就会运行
    },
    methods: {
        moment,
        add() {
            if(this.$route.params.id){
                getLogisticsOrderOne({"id":this.$route.params.id}).then((res)=>{
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
            if (record.cdiProvince){
              getAreaList({parentId:record.cdiProvince  }).then((res) => {
                if (res.success) {
                  if(res.result && res.result.length>0){
                    if(res.result[0].levelType==2){
                      this.cityList = res.result;
                    }
                  }
                }
              })
            }
            if (record.cdiCity){
              getAreaList({parentId:record.cdiCity}).then((res) => {
                if (res.success) {
                  if(res.result && res.result.length>0){
                    if(res.result[0].levelType==3){
                      this.districtList = res.result;
                    }
                  }
                }
              })
            }
            this.$nextTick(() => {
                this.form.setFieldsValue(
                    pick(this.model, 'id', 'code', 'sourceCode', 'content', 'sourceBillType', 'cdiDefaultType', 'sourceId', 'cdiLogisticsId',
                    'cdiRecipients', 'cdiRecipientsPhone', 'cdiLogisticsNo', 'postCode', 'number', 'totalWeight', 'insurance', 'totalCharge',
                    'cdiProvince', 'cdiCity', 'cdiDistrict', 'cdiAddress', 'cdiDeliveryAddress', 'content')
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
                          } else {
                              that.$message.warning(res.message)
                          }
                      })
                      .finally(() => {
                          that.confirmLoading = false
                          that.close()
                          that.$parent.closeRouteViewTab(this.$route.path)
                          that.$router.push({ path:'/logistics/LogisticsOrderList' });
                      })
              }
          })
        },
        handleCancel() {
            this.close()
        },
        popupCallback(row) {
          this.form.setFieldsValue(
            pick(this.model, 'id', 'code', 'sourceCode', 'content', 'sourceBillType', 'cdiDefaultType', 'sourceId', 'cdiLogisticsId',
              'cdiRecipients', 'cdiRecipientsPhone', 'cdiLogisticsNo', 'postCode', 'number', 'totalWeight', 'insurance', 'totalCharge',
              'cdiProvince', 'cdiCity', 'cdiDistrict', 'cdiAddress', 'cdiDeliveryAddress', 'content')
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
                logisticsOrderDtlDelete({"id" : id}).then((res) => {
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
            this.$router.replace({ path:'/logistics/LogisticsOrderList' });
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
        areaChangeCdi(val) {
          getAreaList({parentId:val}).then((res) => {
            if (res.success) {
              if(res.result && res.result.length>0){
                if(res.result[0].levelType==2){
                  this.cityList = res.result;
                  this.model.cdiCity = '';
                  this.model.cdiDistrict = '';
                  this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model,'cdiCity', 'cdiDistrict'))
                  });
                }else if(res.result[0].levelType==3){
                  this.districtList = res.result;
                  this.model.cdiDistrict = '';
                  this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model, 'cdiDistrict'))
                  });
                }
              }
            }
          })
        }
    },
    mounted() {
      if (this.$route.query.unEditable === false) {
        this.unEditable = this.$route.query.unEditable;
      }
      getBillTypeList().then((res) => {
        if (res.success) {
          this.billTypeList = res.result;
        }
      })
      // 物流公司
      getLogisticsCompanyList({}).then((res) => {
        if (res.success) {
          if(res.result && res.result.length>0){
            this.cdiLogisticsCompanyList = res.result;
          }
        }
      })
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
          this.columns[3].list = res.result;
          this.$set(this.dictOptions, 0, this.columns[0])
          this.$set(this.dictOptions, 3, this.columns[3])
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
          this.columns[5].list = res.result;
          this.$set(this.dictOptions, 2, this.columns[2])
          this.$set(this.dictOptions, 5, this.columns[5])
          // this.$set(this.dictOptions, 'materialunitlist', res.result)
        }
      });
      getAreaList({parentId:'100000'}).then((res) => {
        if (res.success) {
          this.provinceList = res.result
        }
      });
    }
}
</script>
<style scoped>
.hide{
    display: none;
}
</style>
