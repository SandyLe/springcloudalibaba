<template>
  <a-modal
    class="customModal"
    :width="modelStyle.width"
    :visible="visible"
    :bodyStyle ="bodyStyle"
    @ok="handleOk"
    @cancel="handleCancel"
    destroyOnClose
    :confirmLoading="confirmLoading"
    cancelText="关闭">
    <template slot="title">
      <div style="width: 100%;height:20px;padding-right:32px;">
        <div style="float: left;">{{ title }}</div>
        <div style="float: right;">
          <a-button
            icon="fullscreen"
            style="width:56px;height:100%;border:0"
            @click="handleClickToggleFullScreen"/>
        </div>
      </div>
    </template>

    <a-spin :spinning="confirmLoading">

      <a-form :form="form">
        <a-card class="card" title="基础信息" :bordered="true">
          <a-row>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="名称">
                <a-input placeholder="请输入名称" v-decorator="[ 'name', validatorRules.name]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="代码">
                <a-input placeholder="后台自动生成" :readOnly="true" v-decorator="[ 'code', validatorRules.code]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="客户类型">
                <a-select v-decorator="['customerTypeId', {}]" placeholder="请选择类型列表">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in typeList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="客户来源">
                <a-select v-decorator="['customerSourceId', {}]" placeholder="请选择来源">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in sourceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="手机">
                <a-input placeholder="请输入手机号码" v-decorator="[ 'phone', validatorRules.phone]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="微商城会员">
                <a-input placeholder="微商城会员" v-decorator="[ 'memberNo', validatorRules.memberNo]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="会员手机">
                <a-input placeholder="请输入会员手机号" v-decorator="[ 'memberPhone', validatorRules.memberPhone]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="会员昵称">
                <a-input placeholder="请输入代码" v-decorator="[ 'memberNickName', validatorRules.memberNickName]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="客户昵称">
                <a-input placeholder="请输入客户昵称" v-decorator="[ 'nickName', validatorRules.nickName]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="性别">
                <j-dict-select-tag v-decorator="['gender', {}]" placeholder="性别" :type="'radio'" :triggerChange="true" dictCode="sex"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="生日">
                <a-date-picker
                  style="width: 100%"
                  placeholder="请选择生日"
                  v-decorator="['birthday', {initialValue:!model.birthday?null:moment(model.birthday,dateFormat)}]"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="联系人">
                <a-input placeholder="请输入联系人" v-decorator="[ 'linkman', validatorRules.linkman]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="电话">
                <a-input placeholder="请输入电话号码" v-decorator="[ 'tel', validatorRules.tel]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="邮箱">
                <a-input placeholder="请输入邮箱" v-decorator="[ 'email', validatorRules.email]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="传真">
                <a-input placeholder="请输入传真号码" v-decorator="[ 'fax', validatorRules.fax]" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="折扣">
                <a-input placeholder="请输入折扣" v-decorator="[ 'discount', validatorRules.discount]" style="width: 40%" />
                <j-dict-select-tag v-decorator="['discountTypeId', {}]" placeholder="折扣类型" :type="'select'" style="width: 60%" :triggerChange="true" dictCode="discount_type"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :md="8" :sm="8">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="银行账号">
                <a-input placeholder="请输入银行账号" v-decorator="[ 'bankaccount', validatorRules.bankaccount]" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="银行账户">
                <a-input placeholder="请输入银行账户" v-decorator="[ 'bankacctName', validatorRules.bankacctName]" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="银行名称">
                <a-input placeholder="请输入银行名称" v-decorator="[ 'bankName', validatorRules.bankName]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :md="24" :sm="24">
              <a-form-item
                :labelCol="hlabelCol"
                :wrapperCol="hwrapperCol"
                label="地址">
                <a-select v-decorator="['province', {}]" placeholder="省" style="width: 15%" @change="areaChange" >
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in provinceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
                <a-select v-decorator="['city', {}]" placeholder="市" style="width: 15%" @change="areaChange" >
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in cityList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
                <a-select v-decorator="['district', {}]" placeholder="区、县" style="width: 15%">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in districtList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
                <a-input placeholder="请输入详细地址" v-decorator="[ 'address', validatorRules.address]" style="width: 55%"  />
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card class="card" title="收货信息" :bordered="true">
          <a-row>
            <a-col :md="24" :sm="24">
              <a-form-item
                :labelCol="hlabelCol"
                :wrapperCol="hwrapperCol"
                label="默认收货方式"
                label-width="4">
                <a-input style="display: none" v-decorator="[ 'cdiId', validatorRules.cdiId]" />
                <a-input style="display: none" v-decorator="[ 'cdiCreateTime', validatorRules.cdiCreateTime]" />
                <a-input style="display: none" v-decorator="[ 'cdiUpdateTime', validatorRules.cdiUpdateTime]" />
                <a-input style="display: none" v-decorator="[ 'cdiCreateBy', validatorRules.cdiCreateBy]" />
                <a-input style="display: none" v-decorator="[ 'cdiUpdateBy', validatorRules.cdiUpdateBy]" />
                <j-dict-select-tag v-decorator="['cdiDefaultType', {}]" @change="deliveryChange" placeholder="请选择默认发货方式" :type="'select'" :triggerChange="true" dictCode="delivery_type"/>
              </a-form-item>
            </a-col>
          </a-row>
          <template v-if="cdiDefaultType === 'ZHIDINGDIAN'">
            <a-row>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="说明"
                  label-width="4">
                  <a-input placeholder="请输入说明" v-decorator="[ 'cdiDescription', validatorRules.cdiDescription]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="联系人"
                  label-width="4">
                  <a-input placeholder="请输入联系人" v-decorator="[ 'cdiLinkman', validatorRules.cdiLinkman]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="联系电话"
                  label-width="4">
                  <a-input placeholder="请输入联系电话" v-decorator="[ 'cdiPhone', validatorRules.cdiPhone]" />
                </a-form-item>
              </a-col>
            </a-row>
          </template>
          <template v-if="cdiDefaultType === 'SONGCHE'">
            <a-row>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="车牌"
                  label-width="4">
                  <a-input placeholder="请输入车牌号" v-decorator="[ 'cdiCarLicense', validatorRules.cdiCarLicense]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="司机姓名"
                  label-width="4">
                  <a-input placeholder="请输入司机姓名" v-decorator="[ 'cdiLinkman', validatorRules.cdiLinkman]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="司机电话"
                  label-width="4">
                  <a-input placeholder="请输入司机电话" v-decorator="[ 'cdiPhone', validatorRules.cdiPhone]" />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="发货地址"
                  label-width="4">
                  <a-input placeholder="请输入发货地址" v-decorator="[ 'cdiDeliveryAddress', validatorRules.cdiDeliveryAddress]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="收件人"
                  label-width="4">
                  <a-input placeholder="请输入收件人" v-decorator="[ 'cdiRecipients', validatorRules.cdiRecipients]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
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
              <a-col :md="24" :sm="24">
                <a-form-item
                  :labelCol="hlabelCol"
                  :wrapperCol="hwrapperCol"
                  label="地址">
                  <a-select v-decorator="['cdiProvince', {}]" placeholder="省" style="width: 10%" @change="areaChangeCdi" >
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in provinceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                  <a-select v-decorator="['cdiCity', {}]" placeholder="市" style="width: 10%" @change="areaChangeCdi" >
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in cdiCityList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                  <a-select v-decorator="['cdiDistrict', {}]" placeholder="区、县" style="width: 10%">
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in cdiDistrictList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                  <a-input placeholder="请输入详细地址" v-decorator="[ 'cdiAddress', validatorRules.cdiAddress]" style="width: 70%"  />
                </a-form-item>
              </a-col>
            </a-row>
          </template>
          <template v-if="cdiDefaultType === 'WULIU'">
            <a-row>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="物流"
                  label-width="4">
                  <a-select v-decorator="['cdiLogistics', {}]" placeholder="物流">
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in cdiLogisticsCompanyList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="网点"
                  label-width="4">
                  <a-input placeholder="请输入网点" v-decorator="[ 'cdiBranch', validatorRules.cdiBranch]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="电话"
                  label-width="4">
                  <a-input placeholder="请输入电话" v-decorator="[ 'cdiTel', validatorRules.cdiTel]" />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="收件人"
                  label-width="4">
                  <a-input placeholder="请输入收件人" v-decorator="[ 'cdiRecipients', validatorRules.cdiRecipients]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
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
              <a-col :md="24" :sm="24">
                <a-form-item
                  :labelCol="hlabelCol"
                  :wrapperCol="hwrapperCol"
                  label="地址">
                  <a-select v-decorator="['cdiProvince', {}]" placeholder="省" style="width: 10%" @change="areaChangeCdi" >
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in provinceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                  <a-select v-decorator="['cdiCity', {}]" placeholder="市" style="width: 10%" @change="areaChangeCdi" >
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in cdiCityList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                  <a-select v-decorator="['cdiDistrict', {}]" placeholder="区、县" style="width: 10%">
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in cdiDistrictList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                  <a-input placeholder="请输入详细地址" v-decorator="[ 'cdiAddress', validatorRules.cdiAddress]" style="width: 70%"  />
                </a-form-item>
              </a-col>
            </a-row>
          </template>
        </a-card>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import pick from 'lodash.pick'
  import moment from 'moment'
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {saveCustomer,duplicateCheck, getCustomerTypeList, getCustomerSourceList, getAreaList, getLogisticsCompanyList, getDeliveryInfo} from '@/api/api'
  export default {
    name: "Customer",
    data() {
      return {
        description: '高级表单常见于一次性输入和提交大批量数据的场景。',
        title: "操作",
        visible: false,
        roleDisabled: false,
        confirmLoading: false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        hlabelCol: {
          xs: { span: 24 },
          sm: { span: 2 },
        },
        hwrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        model: {},
        form: this.$form.createForm(this),
        validatorRules: {
          name: {
            rules: [
              { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' }
            ]
          }
        },
        bodyStyle:{
          padding: "0",
          height:(window.innerHeight*0.8)+"px",
          "overflow-y":"auto",

        },
        modelStyle:{
          width: '80%',
          style: { top: '20px' },
          fullScreen: false
        },
        dateFormat:"YYYY-MM-DD",
        cdiDefaultType: '',
        sourceList: [],
        typeList: [],
        provinceList: [],
        cityList: [],
        districtList:[],
        cdiCityList: [],
        cdiDistrictList:[],
        cdiLogisticsCompanyList:[]
      }
    },
    components: {AFormItem,JDictSelectTag},
    created () {
    },
    methods: {
      add () {

        this.edit({'gender':'1','cdiDefaultType':''});
      },
      edit (record) {

        getCustomerTypeList().then((res) => {
          if (res.success) {
            this.typeList = res.result;
          }
        })
        getCustomerSourceList().then((res) => {
          if (res.success) {
            this.sourceList = res.result;
          }
        })
        getAreaList({parentId:'100000'}).then((res) => {
          if (res.success) {
            this.provinceList = res.result
          }
        })
        if (record.birthday) {
          record.birthday = moment(new Date(record.birthday),this.dateFormat);
        }
        if (record.province){
          getAreaList({parentId:record.province}).then((res) => {
            if (res.success) {
              if(res.result && res.result.length>0){
                if(res.result[0].levelType==2){
                  this.cityList = res.result;
                }
              }
            }
          })
        }
        if (record.city){
          getAreaList({parentId:record.city}).then((res) => {
            if (res.success) {
              if(res.result && res.result.length>0){
                if(res.result[0].levelType==3){
                  this.districtList = res.result;
                }
              }
            }
          })
        }

        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;

        //编辑页面禁止修改角色编码
        if(this.model.id){
          this.roleDisabled = true;

          let that = this;
          getDeliveryInfo({cdiSourceId:this.model.id}).then((res) => {
            if (res.success) {
              if (res.result) {
                that.model.cdiSourceId = res.result.cdiSourceId;
                that.model.cdiDefaultType = res.result.cdiDefaultType;
                that.model.cdiDescription = res.result.cdiDescription;
                that.model.cdiLinkman = res.result.cdiLinkman;
                that.model.cdiPhone = res.result.cdiPhone;
                that.model.cdiDeliveryAddress = res.result.cdiDeliveryAddress;
                that.model.cdiRecipients = res.result.cdiRecipients;
                that.model.cdiRecipientsPhone = res.result.cdiRecipientsPhone;
                that.model.cdiProvince = res.result.cdiProvince;
                that.model.cdiCity = res.result.cdiCity;
                that.model.cdiDistrict = res.result.cdiDistrict;
                that.model.cdiAddress = res.result.cdiAddress;
                that.model.cdiLogistics = res.result.cdiLogistics;
                that.model.cdiBranch = res.result.cdiBranch;
                that.model.cdiTel = res.result.cdiTel;
                that.model.cdiCarLicense = res.result.cdiCarLicense;
                that.model.cdiId = res.result.id;
                that.deliveryChange(that.model.cdiDefaultType)

                if (that.model.cdiProvince){
                  getAreaList({parentId:that.model.cdiProvince}).then((res) => {
                    if (res.success) {
                      if(res.result && res.result.length>0){
                        if(res.result[0].levelType==2){
                          this.cdiCityList = res.result;
                        }
                      }
                    }
                  })
                }
                if (that.model.cdiCity){
                  getAreaList({parentId:that.model.cdiCity}).then((res) => {
                    if (res.success) {
                      if(res.result && res.result.length>0){
                        if(res.result[0].levelType==3){
                          this.cdiDistrictList = res.result;
                        }
                      }
                    }
                  })
                }
                getLogisticsCompanyList({}).then((res) => {
                  if (res.success) {
                    if(res.result && res.result.length>0){
                      this.cdiLogisticsCompanyList = res.result;
                    }
                  }
                })
                this.$nextTick(() => {
                  this.form.setFieldsValue(pick(that.model,'cdiDefaultType','cdiDescription','cdiLinkman','cdiPhone','cdiDeliveryAddress','cdiCarLicense',
                    'cdiRecipients','cdiRecipientsPhone','cdiProvince','cdiCity','cdiDistrict','cdiAddress','cdiLogistics','cdiBranch','cdiTel','cdiId'));
                });
              }
            }
          })
        }else{
          this.roleDisabled = false;
        }

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name', 'code','content','customerTypeId','customerSourceId','phone','memberNo','memberPhone',
          'memberNickName','nickName','gender','birthday','linkman','tel','email','fax','discount','discountTypeId','bankaccount','bankacctName',
            'bankName','province','city','district','address'))
        });
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      moment,
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            if(!values.birthday){
              values.birthday = '';
            }else{
              values.birthday = values.birthday.format(this.dateFormat);
            }
            let formData = Object.assign(this.model, values);
            let obj;
            console.log(formData)
            if(this.model){
              obj=saveCustomer(formData);
            }
            obj.then((res)=>{
              if(res.success){
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
      handleCancel () {
        this.close()
      },
      /** 切换全屏显示 */
      handleClickToggleFullScreen() {
        let mode = !this.modelStyle.fullScreen
        if (mode) {
          this.modelStyle.width = '100%'
          this.modelStyle.style.top = '20px'
        } else {
          this.modelStyle.width = '60%'
          this.modelStyle.style.top = '50px'
        }
        this.modelStyle.fullScreen = mode
      },
      deliveryChange(val) {
        this.cdiDefaultType = val;
      },
      areaChange(val){

        getAreaList({parentId:val}).then((res) => {
          if (res.success) {
            if(res.result && res.result.length>0){
              if(res.result[0].levelType==2){
                this.cityList = res.result;
                this.model.city = '';
                this.model.district = '';
                this.$nextTick(() => {
                  this.form.setFieldsValue(pick(this.model,'city', 'district'))
                });
              }else if(res.result[0].levelType==3){
                this.districtList = res.result;
                this.model.district = '';
                this.$nextTick(() => {
                  this.form.setFieldsValue(pick(this.model, 'district'))
                });
              }
            }
          }
        })
      },
      areaChangeCdi(val) {

        getAreaList({parentId:val}).then((res) => {
          if (res.success) {
            if(res.result && res.result.length>0){
              if(res.result[0].levelType==2){
                this.cdiCityList = res.result;
                this.model.cdiCity = '';
                this.model.cdiDistrict = '';
                this.$nextTick(() => {
                  this.form.setFieldsValue(pick(this.model,'cdiCity', 'cdiDistrict'))
                });
              }else if(res.result[0].levelType==3){
                this.cdiDistrictList = res.result;
                this.model.cdiDistrict = '';
                this.$nextTick(() => {
                  this.form.setFieldsValue(pick(this.model, 'cdiDistrict'))
                });
              }
            }
          }
        })
      }
    }
  }
</script>

<style scoped>

  .customModal {
    .ant-modal-header {
      border: none;
      display: inline-block;
      position: absolute;
      z-index: 1;
      right: 56px;
      padding: 0;
      .ant-modal-title {
        .custom-btn{
          width: 56px;
          height: 56px;
          border: none;
          box-shadow: none;
        }
      }
    }
    .daily-article{
      border-bottom: 0;
    }
  }
</style>