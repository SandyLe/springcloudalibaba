<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <detail-list title="基本信息">
        <a-row>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="辅助属性名称">{{supplementary.name}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="辅助属性编码">{{supplementary.code}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">{{supplementary.rowStsName}} </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">{{supplementary.content}} </a-form-item>
          </a-col>
        </a-row>
      </detail-list>
      <a-divider style="margin-bottom: 32px"/>
      <a-divider style="margin-bottom: 32px"/>
      <detail-list title="辅助属性值">
        <div class="table-operator" style="border-top: 5px">
          <a-button @click="handleAddSupplementaryValue" type="primary" icon="plus">添加辅助属性值</a-button>
        </div>
        <a-table
          ref="table"
          bordered
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading">
           <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical"/>
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
              <a>删除</a>
            </a-popconfirm>
          </span>
        </a-table>
      </detail-list>
      <supplementaryValue-modal ref="supplementaryValueModal" :entity = "supplementary" @ok="modalFormOk"></supplementaryValue-modal>
    </a-card>
  </page-layout>
</template>

<script>
  import PageLayout from '@/components/page/PageLayout'
  import DetailList from '@/components/tools/DetailList'
  import SupplementaryValueModal from './SupplementaryValueModal'
  import { deleteAction, postAction, getAction } from '@/api/manage'
  import {getSupplementaryOne, getAreaOne} from '@/api/api'
  import ARow from "ant-design-vue/es/grid/Row";
  const DetailListItem = DetailList.Item

  export default {
    components: {
      ARow,
      PageLayout,
      DetailList,
      DetailListItem,
      SupplementaryValueModal
    },
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
        supplementary: {},
        /**
         * 表格大小风格，default, middle, small
         */
        size: 'default',
        loading: true,
        bordered: false,
        rowKey: 'id',
        ipagination: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        columns: [{
            title: '属性值编码',
            align: 'center',
            dataIndex: 'code',
            width: 60
          },
          {
            title: '属性值',
            align: 'center',
            dataIndex: 'name',
            width: 60
          },
          {
            title: '备注',
            align: 'center',
            width: 150,
            dataIndex: 'content'
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
            align: 'center',
            width: 120
          }],
        url: {
          list: '/supplementaryValue/getPage',
          delete: '/supplementaryValue/delete',
          deleteBatch: '/supplementaryValue/deleteBatch',
        },
        dataSource: []
      }
    },
    computed: {
      title () {
        return this.$route.meta.title
      }
    },
    methods: {

      handleDelete: function(id) {
        if (!this.url.delete) {
          this.$message.error('请设置url.delete3属性!')
          return
        }
        var that = this
        deleteAction(that.url.delete, { id: id }).then((res) => {
          if (res.success) {
            that.$message.success(res.message)
            that.loadData();
          } else {
            that.$message.warning(res.message)
          }
        })
      },
      modalFormOk() {
        // 新增/修改 成功时，重载列表
        this.loadData()
      },
      loadData(arg) {
        if (!this.url.list) {
          this.$message.error('请设置url.list属性!')
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1
        }
        let params = this.getQueryParams()//查询条件
        params.roleId = this.currentRoleId
        this.loading = true
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total

          }
          this.loading = false
        })

      },
      getQueryParams() {
        //获取查询条件
        let param = {};
        if(this.$route.query.id){
          param.sourceId = this.$route.query.id
        }else{
          param.sourceId = -1;
        }
        param.field = this.getQueryField()
        param.pageNo = this.ipagination.current
        param.pageSize = this.ipagination.pageSize
        return param;
      },
      handleAddSupplementaryValue(){
        this.$refs.supplementaryValueModal.add();
        this.$refs.supplementaryValueModal.title = "新增辅助属性值";
      },
      getQueryField() {
        var str = 'id,'
        this.columns.forEach(function(value) {
          str += ',' + value.dataIndex
        })
        return str
      },
      handleEdit: function(record) {
        this.$refs.supplementaryValueModal.title = '编辑'
        this.$refs.supplementaryValueModal.edit(record)
      },
    },
    watch: {

      '$route' (to, from) {
        console.log(this.$route.query.id)
        if (this.$route.query.id) {
          getSupplementaryOne({id:this.$route.query.id}).then((res) => {
            if (res.success) {
              this.supplementary = res.result;
            }
          })
        }
        this.loadData();
      }
    },
    create: {
      '$route' (to, from) {
        console.log(this.$route.query.id)

        if (this.$route.query.id) {
          getSupplementaryOne({id:this.$route.query.id}).then((res) => {
            if (res.success) {
              this.supplementary = res.result;
            }
          })
        }
      }
    },
    mounted() {
      if (this.$route.query.id) {
        getSupplementaryOne({id:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.supplementary = res.result;
          }
        });
        this.loadData();
      }
    }

  }
</script>

<style lang="scss" scoped>
  .title {
    color: rgba(0,0,0,.85);
    font-size: 16px;
    font-weight: 500;
    margin-bottom: 16px;
  }
</style>
