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
                  notFoundContent="没有匹配的仓库"  >
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
        <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'deliveryTime', {}]"/>
      </a-form-item>
      <a-form-item
        label="安装时间"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText"
      >
        <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'installTime', {}]"/>
      </a-form-item>
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
  import {getWarehouseList,editSaleOrder, getSaleOrderOne,duplicateCheck } from '@/api/api'
  export default {
    name: "Step3",
    data () {
      return {
        model: {},
        dateFormat:"YYYY-MM-DD HH:mm:ss",
        warehouseList: [],
        form: this.$form.createForm(this)
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
              obj=editSaleOrder(formData);
            }
            obj.then((res)=>{
              if(res.success){
                that.saleOrder.totalamount = res.result;
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
        })
      },
      nextStep () {
        this.handleOk()
        this.$emit('nextStep')
      },
      prevStep () {
        this.$emit('prevStep')
      }
    },
    mounted() {
      if (this.$route.query.id) {
        debugger
        console.log(this.form)
        getSaleOrderOne({id:this.$route.query.id}).then((res) => {
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

        getWarehouseList('').then((res) => {
          if (res.success) {
            this.warehouseList = res.result;
          }
        });
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