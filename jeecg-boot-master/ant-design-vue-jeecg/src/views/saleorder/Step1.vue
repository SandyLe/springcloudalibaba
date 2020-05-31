<template>
  <div>
    <a-form :form="saleOrderForm">
      <a-row>
        <a-col :md="6" :sm="6">
          <a-form-item
            :labelCol="{span: 5}"
            :wrapperCol="{span: 19}"
            label="客户">
            <a-select v-decorator="['customerId', validatorRules.customerId]" placeholder="请选择客户"  showSearch
                      optionFilterProp="children"
                      notFoundContent="无法找到，输入名称、编号、手机号回车搜索" @keyup.enter.native="searchCustomer"
                      :disabled="unEditable" >
              <a-select-option value="">请选择</a-select-option>
              <a-select-option v-for="(item, key) in customerList" :key="key" :value="item.id">
                {{ item.info }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :md="6" :sm="6">
          <a-form-item
            :labelCol="{span: 5}"
            :wrapperCol="{span: 19}"
            label="单号">
            <a-input placeholder="自动生成单号" :readOnly="true" v-decorator="[ 'code', {}]" :disabled="unEditable" />
          </a-form-item>
        </a-col>
        <a-col :md="6" :sm="6">
          <a-form-item
            :labelCol="{span: 5}"
            :wrapperCol="{span: 19}"
            label="渠道">
            <j-dict-select-tag v-decorator="['channelId', {}]" placeholder="请选择销售渠道" :type="'select'" :triggerChange="true" dictCode="channel" :disabled="unEditable" :readOnly = "unEditable" />
          </a-form-item>
        </a-col>
        <a-col :md="6" :sm="6">
          <a-form-item
            :labelCol="{span: 6}"
            :wrapperCol="{span: 18}"
            label="订单日期">
            <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'billDate', validatorRules.billDate]" :readOnly = "unEditable" :disabled="unEditable" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :md="24" :sm="24">
          <a-form-item
            label="备注"
            :labelCol="{span: 2}"
            :wrapperCol="{span: 22}"
          >
            <a-input placeholder="请输入备注" v-decorator="[ 'content', {}]" :readOnly = "unEditable" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>

      <a-row>
        <a-col>
          <!-- 操作按钮区域 -->
          <div class="table-operator" style="border-top: 5px" v-if = "!unEditable">
            <a-button @click="handleAddMtl" type="primary" icon="plus">添加产品</a-button>
            <a-dropdown v-if="selectedRowKeys.length > 0">
              <a-menu slot="overlay">
                <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
              </a-menu>
              <a-button style="margin-left: 8px">
                批量操作 <a-icon type="down" />
              </a-button>
            </a-dropdown>
          </div>

          <!-- table区域-begin -->
          <div>
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
              <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;<a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项&nbsp;&nbsp;
              <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div>

            <a-table
              ref="table"
              bordered
              size="middle"
              rowKey="id"
              :columns="columns"
              :dataSource="dataSource"
              :pagination="ipagination"
              :loading="loading"
              :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
              @change="handleTableChange">

              <template slot="avatarslot" slot-scope="text, record, index">
                <div class="anty-img-wrap">
                  <a-avatar shape="square" :src="getAvatarView(record.avatar)" icon="user"/>
                </div>
              </template>

              <span slot="nameAction" slot-scope="text, record">
                <a @click="goDetail(record.mtlId)">{{record.mtl}}</a>
              </span>
              <span slot="action" slot-scope="text, record" v-if = "!unEditable">
                <a @click="handleEditMtl(record)">编辑</a>
                <a-divider type="vertical"/>

                <a-dropdown>
                  <a class="ant-dropdown-link">
                    更多 <a-icon type="down"/>
                  </a>
                  <a-menu slot="overlay">
                    <a-menu-item>
                      <!--
                      <a href="javascript:;" @click="openAuditTab(1, '3')">详情</a>
                      -->
                      <a href="javascript:;" @click="handleEditMtl(record)">详情</a>
                    </a-menu-item>

                    <a-menu-item>
                      <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                        <a>删除</a>
                      </a-popconfirm>
                    </a-menu-item>
                  </a-menu>
                </a-dropdown>
              </span>
            </a-table>
          </div>
          <!-- table区域-end -->

          <!-- 表单区域 -->

          <sale-order-mtl-modal ref="saleOrderMtlModal" :saleOrder = "saleOrder" @ok="modalFormOk"></sale-order-mtl-modal>
          <!-- 表单区域 -->
        </a-col>
      </a-row>
      <a-row>
        <a-col :md="24" :sm="24" style="text-align: center ">
          <a-button type="primary" @click="nextStep">下一步</a-button>
        </a-col>
      </a-row>
  </div>
</template>

<script>
  import SaleOrderMtlModal from './SaleOrderMtlModal'
  import pick from 'lodash.pick'
  import moment from 'moment'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {addSaleOrder, editSaleOrder, getCustomerList, getSaleOrderOne, getSaleOrderMtlList, searchCustomer } from '@/api/api'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  export default {
    name: "Step1",
    mixins: [JeecgListMixin],
    components: {
      SaleOrderMtlModal,
      JDictSelectTag
    },
    data () {
      return {
        saleOrder: {},
        dateFormat:"YYYY-MM-DD HH:mm:ss",
        saleOrderForm: this.$form.createForm(this),
        mainId: 0,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        model: {},
        // form: this.$form.createForm(this),
        validatorRules: {
          code: {
            rules: [
              { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' }
            ]
          },
          customerId: {
            rules: [
              {required: true, message: '请选择客户!'}
            ]
          },
          billDate: {
            rules: [
              {required: true, message: '单据时间不能为空!'}
            ]
          }
        },
        customerList: [],
        columns:[
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
            title: '产品名称',
            align:"center",
            dataIndex: '',
            scopedSlots: { customRender: 'nameAction' }
          },
          {
            title: '产品编码',
            align:"center",
            dataIndex: 'mtlCode'
          },
          {
            title: '数量',
            align:"center",
            dataIndex: 'quantity'
          },
          {
            title: '单位',
            align:"center",
            dataIndex: 'unit'
          },
          {
            title: '单价',
            align:"center",
            dataIndex: 'price'
          },
          {
            title: '折扣',
            align:"center",
            dataIndex: 'discount'
          },
          {
            title: '成交价',
            dataIndex: 'transactionPrice',
            align:"center"
          },
          {
            title: '金额',
            align:"center",
            dataIndex: 'amount'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        selfUnitPage:[],
        selectedRowKeys: [],
        selectedRows: [],
        url: {
          list: "/saleOrderMtl/getPage",
          delete: "/saleOrderMtl/delete",
          deleteBatch: "/saleOrderMtl/deleteBatch"
        },
        unEditable: true
      }
    },
    methods: {
      getQueryParams(){
        let param = {};
        if(this.$route.query.id){
          param.sourceId = this.$route.query.id
        }else{
          param.sourceId = -1;
        }
        return param;
      },
      moment,
      add () {
        this.edit({'gender':'1','cdiDefaultType':''});
      },
      edit (record) {
        this.saleOrderForm.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        //编辑页面禁止修改角色编码
        if(this.model.id){
          this.roleDisabled = true;
        }else{
          this.roleDisabled = false;
        }
        this.$nextTick(() => {
          console.log(this.model)
          this.saleOrderForm.setFieldsValue(pick(this.model,'name', 'code','content','customerId','channelId','billDate'))
          //时间格式化
          this.saleOrderForm.setFieldsValue({billDate: this.model.billDate ? moment(this.model.billDate) : null})
        });
      },
      saveSaleOrder(id) {
        var validFlag = true;
        const that = this;
        // 触发表单验证
        this.saleOrderForm.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            if(!values.billDate){
              values.billDate = '';
            }else{
              values.billDate = values.billDate.format(this.dateFormat);
            }
            if(id){
              values.id = id;
            }
            let formData = Object.assign(this.model, values);
            let obj;
            if(this.model){
              if (values.id) {
                obj=editSaleOrder(formData);
              }else{
                obj=addSaleOrder(formData);
              }
            }
            obj.then((res)=>{
              if(res.success){
                this.$route.query.id = res.result.id;
                that.saleOrder = res.result;
                // that.$message.success(res.message);
                // that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              // that.close();
            })
          } else {
            validFlag = false;
          }
        })
        return validFlag;
      },
      searchCustomer (e) {
        searchCustomer({"keyword":e.target.valueOf().value}).then((res) => {
          if (res.success) {
            this.customerList = res.result;
          }
        })
      },
      handleAddMtl () {
        this.mainId = this.$route.query.id;
        var idExists = true;
        if (!this.mainId) {
          this.saleOrderForm.validateFields((err, values) => {
            if (!err) {
              this.saveSaleOrder(this.mainId);
            } else {
              idExists = false;
            }
          })
        }
        if (idExists) {
          this.$refs.saleOrderMtlModal.add();
          this.$refs.saleOrderMtlModal.title = "新增";
        }
      },
      handleEditMtl (record) {
        this.$refs.saleOrderMtlModal.edit(record);
        this.$refs.saleOrderMtlModal.title = "编辑";
      },
      goDetail(id) {
        this.$router.push({ name: "material-materialEdit", query: {"id": id}})
      },
      nextStep () {
        this.mainId = this.$route.query.id;
        var validFlag = true;
        if (!this.unEditable) {
          validFlag =  this.saveSaleOrder(this.mainId);
        }
        if (validFlag) {
          this.$emit('nextStep')
        }
      }
    },
    mounted() {
      searchCustomer({}).then((res) => {
        if (res.success) {
          this.customerList = res.result;
        }
      })
      if (this.$route.query.id) {
        getSaleOrderOne({id:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.saleOrder = res.result;
            this.edit(res.result);
          }
        })
      }
      this.unEditable = this.$route.query.unEditable;
    }
  }
</script>
