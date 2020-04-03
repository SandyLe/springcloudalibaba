<template>
  <div>
    <a-form style="max-width: 500px; margin: 40px auto 0;" :form="form">
      <a-alert
        :closable="true"
        message="填写发货信息后，在出库管理中完成出库操作。"
        style="margin-bottom: 24px;"
      />
      <a-form-item
        label="出货仓库"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText"
      >
        <a-select v-decorator="['warehouseId', {}]" placeholder="请选择仓库" showSearch optionFilterProp="children"
                  notFoundContent="没有匹配的仓库" :disabled="unEditable">
          <a-select-option value="">请选择</a-select-option>
          <a-select-option v-for="(item, key) in warehouseList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="发货时间"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText"
      >
        <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'deliveryTime', {}]" :disabled="unEditable"/>
      </a-form-item>
      <a-form-item
        label="安装时间"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText"
      >
        <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'installTime', {}]" :disabled="unEditable"/>
      </a-form-item>

      <a-card class="card" title="收货信息" :bordered="true">
        <a-row>
          <a-col :md="24" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="默认收货方式"
              label-width="4">
              <a-input style="display: none" v-decorator="[ 'cdiId', validatorRules.cdiId]" />
              <a-input style="display: none" v-decorator="[ 'cdiCreateTime', validatorRules.cdiCreateTime]" />
              <a-input style="display: none" v-decorator="[ 'cdiUpdateTime', validatorRules.cdiUpdateTime]" />
              <a-input style="display: none" v-decorator="[ 'cdiCreateBy', validatorRules.cdiCreateBy]" />
              <a-input style="display: none" v-decorator="[ 'cdiUpdateBy', validatorRules.cdiUpdateBy]" />
              <j-dict-select-tag v-decorator="['cdiDefaultType', {}]" @change="deliveryChange" placeholder="请选择默认发货方式" :type="'select'" :triggerChange="true" dictCode="delivery_type" :disabled="unEditable"/>
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
                <a-input placeholder="请输入说明" v-decorator="[ 'cdiDescription', validatorRules.cdiDescription]" :disabled="unEditable" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="联系人"
                label-width="4">
                <a-input placeholder="请输入联系人" v-decorator="[ 'cdiLinkman', validatorRules.cdiLinkman]" :disabled="unEditable" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="联系电话"
                label-width="4">
                <a-input placeholder="请输入联系电话" v-decorator="[ 'cdiPhone', validatorRules.cdiPhone]" :disabled="unEditable" />
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
                <a-input placeholder="请输入车牌号" v-decorator="[ 'cdiCarLicense', validatorRules.cdiCarLicense]" :disabled="unEditable"/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="司机姓名"
                label-width="4">
                <a-input placeholder="请输入司机姓名" v-decorator="[ 'cdiLinkman', validatorRules.cdiLinkman]" :disabled="unEditable" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="司机电话"
                label-width="4">
                <a-input placeholder="请输入司机电话" v-decorator="[ 'cdiPhone', validatorRules.cdiPhone]" :disabled="unEditable" />
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
                <a-input placeholder="请输入发货地址" v-decorator="[ 'cdiDeliveryAddress', validatorRules.cdiDeliveryAddress]" :disabled="unEditable" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="收件人"
                label-width="4">
                <a-input placeholder="请输入收件人" v-decorator="[ 'cdiRecipients', validatorRules.cdiRecipients]" :disabled="unEditable" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="联系电话"
                label-width="4">
                <a-input placeholder="请输入联系电话" v-decorator="[ 'cdiRecipientsPhone', validatorRules.cdiRecipientsPhone]" :disabled="unEditable" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :md="24" :sm="24">
              <a-form-item
                :labelCol="hlabelCol"
                :wrapperCol="hwrapperCol"
                label="地址">
                <a-select v-decorator="['cdiProvince', {}]" placeholder="省" @change="areaChangeCdi" :disabled="unEditable" >
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in provinceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
                <a-select v-decorator="['cdiCity', {}]" placeholder="市" @change="areaChangeCdi" :disabled="unEditable" >
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in cdiCityList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
                <a-select v-decorator="['cdiDistrict', {}]" placeholder="区、县" :disabled="unEditable">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in cdiDistrictList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
                <a-input placeholder="请输入详细地址" v-decorator="[ 'cdiAddress', validatorRules.cdiAddress]" :disabled="unEditable"/>
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
                <a-select v-decorator="['cdiLogistics', {}]" placeholder="物流" :disabled="unEditable">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in logistics" :key="key" :value="item.id">
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
                <a-input placeholder="请输入网点" v-decorator="[ 'cdiBranch', validatorRules.cdiBranch]" :disabled="unEditable" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="电话"
                label-width="4">
                <a-input placeholder="请输入电话" v-decorator="[ 'cdiTel', validatorRules.cdiTel]" :disabled="unEditable" />
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
                <a-input placeholder="请输入收件人" v-decorator="[ 'cdiRecipients', validatorRules.cdiRecipients]" :disabled="unEditable" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="联系电话"
                label-width="4">
                <a-input placeholder="请输入联系电话" v-decorator="[ 'cdiRecipientsPhone', validatorRules.cdiRecipientsPhone]" :disabled="unEditable" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :md="24" :sm="24">
              <a-form-item
                :labelCol="hlabelCol"
                :wrapperCol="hwrapperCol"
                label="地址">
                <a-select v-decorator="['cdiProvince', {}]" placeholder="省" @change="areaChangeCdi" :disabled="unEditable" >
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in provinceList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
                <a-select v-decorator="['cdiCity', {}]" placeholder="市" @change="areaChangeCdi" :disabled="unEditable" >
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in cdiCityList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
                <a-select v-decorator="['cdiDistrict', {}]" placeholder="区、县" :disabled="unEditable">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in cdiDistrictList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                  </a-select-option>
                </a-select>
                <a-input placeholder="请输入详细地址" v-decorator="[ 'cdiAddress', validatorRules.cdiAddress]" :disabled="unEditable" />
              </a-form-item>
            </a-col>
          </a-row>
        </template>
      </a-card>
      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <!--<a-button :loading="loading" @click="nextStep">提交</a-button>-->
        <a-button style="margin-left: 8px" @click="prevStep">上一步</a-button>
        <a-button style="margin-left: 8px" type="primary" @click="nextStep">下一步</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>

  import pick from 'lodash.pick'
  import moment from 'moment'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {getWarehouseList,editSaleOrder, getSaleOrderOne, getAreaList, delivery, getSaleOrderDeliveryInfo, duplicateCheck, getLogisticsCompanyList } from '@/api/api'
  export default {
    name: "Step3",
    components: {JDictSelectTag},
    data () {
      return {
        model: {},
        dateFormat:"YYYY-MM-DD HH:mm:ss",
        warehouseList: [],
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
        form: this.$form.createForm(this),
        validatorRules: {
          name: {
            rules: [
              { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' }
            ]
          }
        },
        cdiDefaultType: '',
        provinceList: [],
        cityList: [],
        districtList:[],
        cdiCityList: [],
        cdiDistrictList:[],
        logistics: [],
        unEditable: true
      }
    },
    methods: {
      moment,
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            if(!values.installTime){
              values.installTime = '';
            }else{
              values.installTime = values.installTime.format(this.dateFormat);
            }
            if(!values.deliveryTime){
              values.deliveryTime = '';
            }else{
              values.deliveryTime = values.deliveryTime.format(this.dateFormat);
            }
            that.confirmLoading = true;
            values.sourceId = this.$route.query.id;
            let formData = Object.assign(this.model, values);
            let obj;
            console.log(formData)
            if(this.model.id){
              obj=delivery(formData);
            }
            obj.then((res)=>{
              if(res.success){
                that.saleOrder.totalamount = res.result;
                that.$message.success(res.message);
                that.$emit('ok');
                that.$emit('nextStep')
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
        })
      },
      deliveryChange(val) {
        this.cdiDefaultType = val;
        if (val === 'SONGCHE'){
          getAreaList({parentId:'100000'}).then((res) => {
            if (res.success) {
              this.provinceList = res.result
            }
          })
        }
        if (val === 'WULIU'){
          getLogisticsCompanyList({}).then((res) => {
            if (res.success) {
              this.logistics = res.result
            }
          })
          getAreaList({parentId:'100000'}).then((res) => {
            if (res.success) {
              this.provinceList = res.result
            }
          })
        }

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
      },
      setDeliveryInfo (billId) {
        let that = this;
        getSaleOrderDeliveryInfo({sourceId:billId}).then((res) => {
          if (res.success) {
            if (res.result) {
              let obj = res.result[0];
              that.model.sourceId = obj.sourceId;
              that.model.cdiSourceId = obj.cdiSourceId;
              that.model.cdiDefaultType = obj.cdiDefaultType;
              that.model.cdiDescription = obj.cdiDescription;
              that.model.cdiLinkman = obj.cdiLinkman;
              that.model.cdiPhone = obj.cdiPhone;
              that.model.cdiDeliveryAddress = obj.cdiDeliveryAddress;
              that.model.cdiRecipients = obj.cdiRecipients;
              that.model.cdiRecipientsPhone = obj.cdiRecipientsPhone;
              that.model.cdiProvince = obj.cdiProvince;
              that.model.cdiCity = obj.cdiCity;
              that.model.cdiDistrict = obj.cdiDistrict;
              that.model.cdiAddress = obj.cdiAddress;
              that.model.cdiLogistics = obj.cdiLogistics;
              that.model.cdiBranch = obj.cdiBranch;
              that.model.cdiTel = obj.cdiTel;
              that.model.cdiCarLicense = obj.cdiCarLicense;
              that.model.cdiId = obj.id;
              that.model.warehouseId = obj.warehouseId;
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
              this.$nextTick(() => {
                this.form.setFieldsValue(pick(that.model,'cdiDefaultType','cdiDescription','cdiLinkman','cdiPhone','cdiDeliveryAddress','cdiCarLicense',
                  'cdiRecipients','cdiRecipientsPhone','cdiProvince','cdiCity','cdiDistrict','cdiAddress','cdiLogistics','cdiBranch','cdiTel','cdiId'));
              });
            }
          }
        })
      },
      nextStep () {
        if (!this.unEditable) {
          this.handleOk();
        } else {
          this.$emit('nextStep')
        }
      },
      prevStep () {
        this.$emit('prevStep')
      },
      updateInfo(saleOrderId) {
        getSaleOrderOne({id: saleOrderId}).then((res) => {
          if (res.success) {
            this.saleOrder = res.result;
            this.model = Object.assign({}, this.saleOrder);
            // this.model.payamount = this.model.totalamount; // 默认填充
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'warehouseId'))
              this.form.setFieldsValue({installTime: this.model.installTime ? moment(this.model.installTime) : null})
              this.form.setFieldsValue({deliveryTime: this.model.deliveryTime ? moment(this.model.deliveryTime) : null})
            });
          }
        })
        this.setDeliveryInfo(saleOrderId);
        getWarehouseList('').then((res) => {
          if (res.success) {
            this.warehouseList = res.result;
          }
        });
      }
    },
    mounted() {
      if (this.$route.query.id) {
        this.updateInfo(this.$route.query.id);
      }
      this.unEditable = this.$route.query.unEditable;
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