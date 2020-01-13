<template>
  <a-modal
    :width="modelStyle.width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    wrapClassName="ant-modal-cust-warp"
    :bodyStyle ="bodyStyle">

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
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="mtlId"
          :columns="columns"
          :dataSource="inventoryLogs"
          :pagination="ipagination"
          :loading="loading"
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          @change="handleTableChange">

          <span slot="nameAction" slot-scope="text, record, index">
                <a-input type="hidden" :defaultValue="record.mtlId"/>
                <a @click="goDetail(record.mtlId)">{{record.material}}</a>

          </span>
          <span slot="amountAction" slot-scope="text, record, index">
            <a-input placeholder="请输入出库数量" :defaultValue="record.quantity" name="quantity"
                     @change="e => handleChange(e.target.value, record.mtlId, index, 'quantity')"/>
          </span>
        </a-table>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import {duplicateCheck } from '@/api/api'

  export default {
    name: "InventoryLogModal",
    mixins: [JeecgListMixin],
    props: {
      inventoryLogs: {
        type: Array,
        default: () => []
      }
    },
    data() {
      return {
        title: "操作",
        visible: false,
        confirmLoading: false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title: '仓库',
            align:"center",
            dataIndex: 'warehouse'
          },
          {
            title: '产品',
            align:"center",
            dataIndex: '',
            scopedSlots: { customRender: 'nameAction' }
          },
          {
            title: '操作前数量',
            align:"center",
            dataIndex: 'beforeAmount'
          },
          {
            title: '操作数量',
            align:"center",
            dataIndex: 'optAmount'
          },
          {
            title: '操作后库存',
            align:"center",
            dataIndex: 'stockAmount'
          },
          {
            title: '单位',
            align:"center",
            dataIndex: 'unit'
          },
          {
            title: '出库时间',
            align:"center",
            dataIndex: 'createTime'
          }
        ],
        bodyStyle:{
          padding: "0",
          height:(window.innerHeight*0.8)+"px",
          "overflow-y":"auto"
        },
        modelStyle:{
          width: '80%',
          style: { top: '20px' },
          fullScreen: false
        },
        model: {},
        form: this.$form.createForm(this),
        validatorRules: {
          rcontent: {
            rules: [
              { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' }
            ]
          }
        }
      }
    },
    components: {AFormItem},
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        that.confirmLoading = false;
        that.close();
        /**
        let obj = deliveryStockOut(this.putoutmtls);
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


        // 触发表单验证
        this.form.validateFields((err, values) => {
          debugger
          if (!err) {
            that.confirmLoading = true;
            console.log(this.model)
            let formData = Object.assign(this.model, values);

          }
        }) **/
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
          this.modelStyle.width = '80%'
          this.modelStyle.style.top = '50px'
        }
        this.modelStyle.fullScreen = mode
      },
      handleChange (val, key, index, col) {
        this.putoutmtls[index].quantity = val;
      }
    }
  }
</script>

<style scoped>

</style>