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
          :dataSource="putInMtls"
          :pagination="ipagination"
          :loading="loading"
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          @change="handleTableChange">

          <span slot="nameAction" slot-scope="text, record, index">
                <a-input type="hidden" :defaultValue="record.mtlId"/>
                <a @click="goDetail(record.mtlId)">{{record.mtl}}</a>
          </span>
          <span slot="specificationAction" slot-scope="text, record, index">
                {{record.mtl + ";" + record.suppValueMap}}
          </span>

          <span slot="amountAction" slot-scope="text, record, index">
            <a-input placeholder="请输入入库数量" :defaultValue="record.quantity" name="quantity"
                     @change="e => handleChange(e.target.value, record.mtlId, index, 'quantity')"/>
          </span>
          <span slot="warehouseAction" slot-scope="text, record, index">
            <a-select v-decorator="['warehouseId', validatorRules.warehouseId]"
                      :defaultValue="record.warehouseId"
                      @change="((val)=>{handleChange(val, record.mtlId, index, 'warehouseId')})" placeholder="请选择仓库">
              <a-select-option v-for="(item, key) in warehouseList" :key="key" :value="item.id">
                {{ item.name }}
              </a-select-option>
            </a-select>
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
  import {putStockIn, duplicateCheck, getWarehouseList } from '@/api/api'

  export default {
    name: "PurchasePutInModal",
    mixins: [JeecgListMixin],
    props: {
      putInMtls: {
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
            title: '编码',
            align:"center",
            dataIndex: 'mtlCode'
          },
          {
            title: '产品',
            align:"center",
            dataIndex: '',
            scopedSlots: { customRender: 'nameAction' }
          },
          {
            title: '规格属性',
            align:"center",
            dataIndex: '',
            scopedSlots: { customRender: 'specificationAction' }
          },
          {
            title: '入货仓库',
            align:"center",
            scopedSlots: { customRender: 'warehouseAction' }
          },
          {
            title: '数量',
            align:"center",
            scopedSlots: { customRender: 'amountAction' }
          },
          {
            title: '单位',
            align:"center",
            dataIndex: 'unit'
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
        },
        warehouseList:[]
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
        //仓库
        getWarehouseList('').then((res) => {
          if (res.success) {
            if (res.result && res.result.length > 0) {
              this.warehouseList = res.result;
            }
          }
        });
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
        debugger
        console.log(this.putInMtls)
        let obj = putStockIn(this.putInMtls);
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


        /**
        // 触发表单验证
        this.form.validateFields((err, values) => {
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
        if ('quantity' == col) {
          this.putInMtls[index].quantity = val;
        } else if ('warehouseId' == col) {
          this.putInMtls[index].warehouseId = val;
        }
      }
    }
  }
</script>

<style scoped>

</style>