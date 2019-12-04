<template>
  <a-modal
    class="customModal"
    :width="modelStyle.width"
    :visible="visible"
    :bodyStyle ="bodyStyle"
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
                <a-input placeholder="请输入代码" v-decorator="[ 'code', validatorRules.code]" />
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
                label="默认发货方式"
                label-width="4">
                <j-dict-select-tag v-decorator="['cdi_defaultType', {}]" @change="deliveryChange" placeholder="请选择默认发货方式" :type="'select'" :triggerChange="true" dictCode="delivery_type"/>
              </a-form-item>
            </a-col>
          </a-row>
          <template v-if="zhidingdian">
            <a-row>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="说明"
                  label-width="4">
                  <a-input placeholder="请输入说明" v-decorator="[ 'cdi_description', validatorRules.cdi_description]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="联系人"
                  label-width="4">
                  <a-input placeholder="请输入联系人" v-decorator="[ 'cdi_linkman', validatorRules.cdi_linkman]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="联系电话"
                  label-width="4">
                  <a-input placeholder="请输入联系电话" v-decorator="[ 'cdi_phone', validatorRules.cdi_phone]" />
                </a-form-item>
              </a-col>
            </a-row>
          </template>
          <template v-if="songche">
            <a-row>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="车牌"
                  label-width="4">
                  <a-input placeholder="请输入车牌号" v-decorator="[ 'cdi_carLicense', validatorRules.cdi_carLicense]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="司机姓名"
                  label-width="4">
                  <a-input placeholder="请输入司机姓名" v-decorator="[ 'cdi_linkman', validatorRules.cdi_linkman]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="司机电话"
                  label-width="4">
                  <a-input placeholder="请输入司机电话" v-decorator="[ 'cdi_phone', validatorRules.cdi_phone]" />
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
                  <a-input placeholder="请输入发货地址" v-decorator="[ 'cdi_deliveryAddress', validatorRules.cdi_deliveryAddress]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="收件人"
                  label-width="4">
                  <a-input placeholder="请输入收件人" v-decorator="[ 'cdi_recipients', validatorRules.cdi_recipients]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="联系电话"
                  label-width="4">
                  <a-input placeholder="请输入联系电话" v-decorator="[ 'cdi_recipients_phone', validatorRules.cdi_recipients_phone]" />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :md="24" :sm="24">
                <a-form-item
                  :labelCol="hlabelCol"
                  :wrapperCol="hwrapperCol"
                  label="地址">
                  <a-select v-decorator="['cdi_province', {}]" placeholder="省" style="width: 10%">
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in sourceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                  <a-select v-decorator="['cdi_city', {}]" placeholder="市" style="width: 10%">
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in sourceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                  <a-select v-decorator="['cdi_district', {}]" placeholder="区、县" style="width: 10%">
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in sourceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                  <a-input placeholder="请输入详细地址" v-decorator="[ 'cdi_address', validatorRules.cdi_address]" style="width: 70%"  />
                </a-form-item>
              </a-col>
            </a-row>
          </template>
          <template v-if="wuliu">
            <a-row>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="物流"
                  label-width="4">
                  <a-select v-decorator="['cdi_logistics', {}]" placeholder="物流" style="width: 10%">
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in sourceList" :key="key" :value="item.id">
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
                  <a-input placeholder="请输入网点" v-decorator="[ 'cdi_branch', validatorRules.cdi_branch]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="电话"
                  label-width="4">
                  <a-input placeholder="请输入电话" v-decorator="[ 'cdi_tel', validatorRules.cdi_tel]" />
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
                  <a-input placeholder="请输入收件人" v-decorator="[ 'cdi_recipients', validatorRules.cdi_recipients]" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="联系电话"
                  label-width="4">
                  <a-input placeholder="请输入联系电话" v-decorator="[ 'cdi_recipients_phone', validatorRules.cdi_recipients_phone]" />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :md="24" :sm="24">
                <a-form-item
                  :labelCol="hlabelCol"
                  :wrapperCol="hwrapperCol"
                  label="地址">
                  <a-select v-decorator="['cdi_province', {}]" placeholder="省" style="width: 10%">
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in sourceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                  <a-select v-decorator="['cdi_city', {}]" placeholder="市" style="width: 10%">
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in sourceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                  <a-select v-decorator="['cdi_district', {}]" placeholder="区、县" style="width: 10%">
                    <a-select-option value="">请选择</a-select-option>
                    <a-select-option v-for="(item, key) in sourceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                    </a-select-option>
                  </a-select>
                  <a-input placeholder="请输入详细地址" v-decorator="[ 'cdi_address', validatorRules.cdi_address]" style="width: 70%"  />
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
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {addCustomer,editCustomer,duplicateCheck, getCustomerTypeList, getCustomerSourceList, getAreaList} from '@/api/api'
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
        zhidingdian: false,
        wuliu: false,
        songche: false,
        sourceList: [],
        typeList: [],
        provinceList: [],
        cityList: [],
        districtList:[]
      }
    },
    components: {AFormItem,JDictSelectTag},
    created () {
    },
    methods: {
      add () {
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

        this.edit({'gender':'1'});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;

        //编辑页面禁止修改角色编码
        if(this.model.id){
          this.roleDisabled = true;
        }else{
          this.roleDisabled = false;
        }

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name', 'code','content','gender'))
        });
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let formData = Object.assign(this.model, values);
            let obj;
            console.log(formData)
            if(!this.model.id){
              obj=addCustomer(formData);
            }else{
              obj=editCustomer(formData);
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
        if(val == 'SONGCHE'){
          this.songche = true;
          this.wuliu = false;
          this.zhidingdian = false;
        } else if(val == 'ZHIDINGDIAN'){
          this.songche = false;
          this.wuliu = false;
          this.zhidingdian = true;
        } else if(val == 'WULIU'){
          this.songche = false;
          this.wuliu = true;
          this.zhidingdian = false;
        } else {
          this.songche = false;
          this.wuliu = false;
          this.zhidingdian = false;
        }
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