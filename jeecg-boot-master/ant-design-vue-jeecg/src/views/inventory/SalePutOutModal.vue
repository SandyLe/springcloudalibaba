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
        <a-row>
          <a-col :md="12" :sm="12">
            <a-form-item label="出货方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="['deliveryTypeId', {}]" placeholder="请选择出货方式" showSearch optionFilterProp="children"
                        notFoundContent="没有匹配的出货方式" @change="deliveryTypeChange" >
                <a-select-option value="">请选择</a-select-option>
                <a-select-option v-for="(item, key) in deliveryTypeList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.name || item.code ">
                      {{ item.name || item.code }}
                    </span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="出库时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'deliveryDate', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row v-if="deliveryType < 1" >
          <a-col :span="12">
            <a-form-item label="物流单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input placeholder="请输入物流单号" v-decorator="[ 'logisticsNo', {}]" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <!--<a-form-item label="完工时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'finishedDate', {}]"/>
            </a-form-item>-->
          </a-col>
        </a-row>
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="mtlId"
          :columns="columns"
          :dataSource="putoutmtls"
          :pagination="ipagination"
          :loading="loading"
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          @change="handleTableChange">

          <span slot="nameAction" slot-scope="text, record, index">
                <a-input type="hidden" :defaultValue="record.mtlId"/>
                <a @click="goDetail(record.mtlId)">{{record.mtl}}</a>

          </span>
          <span slot="amountAction" slot-scope="text, record, index">
            <a-input placeholder="请输入出库数量" :defaultValue="record.quantity" name="quantity"
                     @change="e => handleChange(e.target.value, record.mtlId, index, 'quantity')"/>
          </span>
          <span slot="warehouseAction" slot-scope="text, record, index">
            <a-select v-decorator="['warehouseId', validatorRules.warehouseId]" @change="((val)=>{handleChange(val, record.mtlId, index, 'warehouseId')})" placeholder="请选择仓库">
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
  import {deliveryStockOut, duplicateCheck, getDeliveryTypeList, getWarehouseList } from '@/api/api'

  export default {
    name: "SalePutOutModal",
    mixins: [JeecgListMixin],
    props: {
      putoutmtls: {
        type: Array,
        default: () => []
      }
    },
    data() {
      return {
        dateFormat:"YYYY-MM-DD HH:mm:ss",
        title: "操作",
        visible: false,
        confirmLoading: false,
        deliveryType: 0,
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
            title: 'Code',
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
            title: '规格',
            align:"center",
            dataIndex: 'specification'
          },
          {
            title: '出货仓库',
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
        deliveryTypeList: [],
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
        getDeliveryTypeList().then((res) => {
          if (res.success) {
            this.deliveryTypeList = res.result;
          }
        })
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
        this.form.validateFields((err, values) => {
          if (!err) {
            if(!values.deliveryDate){
              values.deliveryDate = '';
            }else{
              values.deliveryDate = values.deliveryDate.format(this.dateFormat);
            }
            let formData = Object.assign(this.model, values);
            debugger
            let formDataDto = {
              deliveryTypeId: formData.deliveryTypeId,
              deliveryDate: formData.deliveryDate,
              logisticsNo: formData.logisticsNo,
              mtls: this.putoutmtls
            }
            let obj=deliveryStockOut(formDataDto);
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
        if ('quantity' == col) {
          this.putoutmtls[index].quantity = val;
        } else if ('warehouseId' == col) {
          this.putoutmtls[index].warehouseId = val;
        }
      },
      deliveryTypeChange (val) {
        this.deliveryType = val;
      }
    }
  }
</script>

<style scoped>

</style>