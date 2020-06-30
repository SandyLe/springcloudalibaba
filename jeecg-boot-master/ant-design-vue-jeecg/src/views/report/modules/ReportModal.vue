<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    okText="保存"
    cancelText="关闭"
  >

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="名称"
          hasFeedback
        >
          <a-input
            placeholder="请输入名称"
            v-decorator="['name', {rules: [{ required: true, message: '请输入名称!' }]}]"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="归属页面"
          hasFeedback
        >
          <a-select
            v-decorator="['pageType', {}]"
            placeholder="请选择归属页面"
          >
            <!-- <a-select-option value="">请选择</a-select-option> -->
            <a-select-option
              v-for="(item, key) in pagetypedict"
              :key="key"
              :value="item.value"
            >
              <span
                style="display: inline-block;width: 100%"
                :title=" item.text || item.label "
              >
                {{ item.text || item.label }}
              </span>
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="展示方式"
          hasFeedback
        >
          <a-select
            v-decorator="['showType', {}]"
            placeholder="请选择展示方式"
          >
            <!-- <a-select-option value="">请选择</a-select-option> -->
            <a-select-option
              v-for="(item, key) in showtypedict"
              :key="key"
              :value="item.value"
            >
              <span
                style="display: inline-block;width: 100%"
                :title=" item.text || item.label "
              >
                {{ item.text || item.label }}
              </span>
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="统计SQL语句"
        >
          <a-textarea
            placeholder="请输入统计SQL语句"
            v-decorator="['querySql', {rules: [{ required: true, message: '请输入统计SQL语句!' }]}]"
            :auto-size="{ minRows: 5, maxRows: 5 }"
          />
          <span>使用 #companyid# 占位符会传入账号归属公司ID </span>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="接收结果Bean"
          hasFeedback
        >
          <a-input
            placeholder="请输入接收结果Bean"
            v-decorator="['resultBean', {}]"
          />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import { httpAction } from '@/api/manage'
import pick from 'lodash.pick'
import { ajaxGetDictItems } from '@/api/api'

export default {
  name: 'QuartzJobModal',
  components: {},
  data() {
    return {
      title: '操作',
      visible: false,
      showtypedict: [],
      pagetypedict: [],
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      cron: {
        label: '',
        value: ''
      },
      confirmLoading: false,
      form: this.$form.createForm(this),
      url: {
        add: '/report/add',
        edit: '/report/edit'
      }
    }
  },
  created() {},
  methods: {
    initDictData() {
      //根据字典Code, 初始化字典数组
      ajaxGetDictItems('show_type', null).then(res => {
        if (res.success) {
          this.showtypedict = res.result
        }
      })
      //根据字典Code, 初始化字典数组
      ajaxGetDictItems('page_type', null).then(res => {
        if (res.success) {
          this.pagetypedict = res.result
        }
      })
    },
    add() {
      this.edit({})
    },
    edit(record) {
      //获取字典数据
      this.initDictData()
      let that = this
      that.form.resetFields()
      this.model = Object.assign({}, record)
      console.log(this.model)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'name', 'pageType', 'showType', 'querySql', 'resultBean'))
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
        console.log('values', values)
        if (!err) {
          that.confirmLoading = true
          let httpurl = ''
          let method = 'post'
          if (!that.model.id) {
            httpurl += that.url.add
          } else {
            httpurl += that.url.edit
          }
          let formData = Object.assign(that.model, values)
          //时间格式化

          console.log('提交参数', formData)
          httpAction(httpurl, formData, method)
            .then(res => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
              that.close()
            })
        }
      })
    },
    handleCancel() {
      this.close()
    }
  }
}
</script>

<style scoped>
</style>