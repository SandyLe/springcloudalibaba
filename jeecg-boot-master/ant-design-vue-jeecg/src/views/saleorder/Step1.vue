<template>
  <div>
    <a-form style=" margin: 40px auto 0;">
      <a-row>
        <a-col :md="6" :sm="6">
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="客户">
            <a-select v-decorator="['custid', {}]" placeholder="请选择客户">
              <a-select-option value="">请选择</a-select-option>
              <a-select-option v-for="(item, key) in customerList" :key="key" :value="item.id">
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
            label="单号">
            <a-input placeholder="请输入代码" v-decorator="[ 'code', {}]" />
          </a-form-item>
        </a-col>
        <a-col :md="6" :sm="6">
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="渠道">
            <a-select v-decorator="['channel', {}]" placeholder="请选择渠道">
              <a-select-option value="">请选择</a-select-option>
              <a-select-option v-for="(item, key) in customerList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :md="6" :sm="6">
          <a-form-item
            label="付款账户"
            :labelCol="{span: 5}"
            :wrapperCol="{span: 19}"
          >
            <a-select value="1" placeholder="ant-design@alipay.com">
              <a-select-option value="1">ant-design@alipay.com</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-form-item
        label="付款账户"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
      >
        <a-select value="1" placeholder="ant-design@alipay.com">
          <a-select-option value="1">ant-design@alipay.com</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="收款账户"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
      >
        <a-input-group :compact="true" style="display: inline-block; vertical-align: middle">
          <a-select defaultValue="alipay" style="width: 100px">
            <a-select-option value="alipay">支付宝</a-select-option>
            <a-select-option value="wexinpay">微信</a-select-option>
          </a-select>
          <a-input :style="{width: 'calc(100% - 100px)'}" value="test@example.com"/>
        </a-input-group>
      </a-form-item>
      <a-form-item
        label="收款人姓名"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
      >
        <a-input value="Alex" />
      </a-form-item>
      <a-form-item
        label="转账金额"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
      >
        <a-input prefix="￥" value="5000" />
      </a-form-item>
      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <a-button type="primary" @click="nextStep">下一步</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
  export default {
    name: "Step1",
    data () {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        model: {},
        form: this.$form.createForm(this),
        validatorRules: {
          code: {
            rules: [
              { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' }
            ]
          }
        }
      }
    },
    methods: {
      nextStep () {
        this.$emit('nextStep')
      }
    }
  }
</script>

<style scoped>

</style>
<script>
  import ARow from "ant-design-vue/es/grid/Row";
  export default {
    components: {ARow}
  }
</script>