<template>
  <a-card :bordered="false">

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button
        @click="batchDel"
        v-if="selectedRowKeys.length > 0"
        ghost
        type="primary"
        icon="delete">批量删除
      </a-button>
    </div>

    <!-- table区域-begin -->
    <div>

      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;<a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项&nbsp;&nbsp;
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        :columns="columns"
        size="middle"
        :pagination="false"
        :dataSource="dataSource"
        :loading="loading"
        @expand="expandSubmenu"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;" @click="handleAddSub(record)">添加子菜单</a>
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
    <sale-order-channel-modal ref="modalForm" @ok="modalFormOk"></sale-order-channel-modal>
    <!-- <permission-modal ref="modalForm" @ok="modalFormOk"></permission-modal> -->
    <!-- <permission-data-rule-list ref="PermissionDataRuleList" @ok="modalFormOk"></permission-data-rule-list> -->

  </a-card>
</template>

<script>
  import SaleOrderChannelModal from './SaleOrderChannelModal'
  import { getRootChannel,getSubChannel } from '@/api/api'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  // import PermissionDataRuleList from './PermissionDataRuleList'
  import JEllipsis from '@/components/jeecg/JEllipsis'

  const columns = [
    {
      title: '渠道名称',
      dataIndex: 'name',
      key: 'name'
    },
    // {
    //   title: 'icon',
    //   dataIndex: 'icon',
    //   key: 'icon'
    // },
    // {
    //   title: '组件',
    //   dataIndex: 'component',
    //   key: 'component',
    //   scopedSlots: { customRender: 'component' }
    // },
    // {
    //   title: '路径',
    //   dataIndex: 'url',
    //   key: 'url',
    //   scopedSlots: { customRender: 'url' }
    // },
    // {
    //   title: '排序',
    //   dataIndex: 'sortNo',
    //   key: 'sortNo'
    // },
    {
      title: '操作',
      dataIndex: 'action',
      scopedSlots: { customRender: 'action' },
      align: 'center',
      width: 150
    }
  ]

  export default {
    name: 'SaleOrderChannel',
    mixins: [JeecgListMixin],
    components: {
      // PermissionDataRuleList,
      SaleOrderChannelModal,
      JEllipsis
    },
    data() {
      return {
        description: '这是菜单管理页面',
        // 表头
        columns: columns,
        loading: false,
        url: {
          list: '/saleOrder/getList',
          delete: '/saleOrder/delete',
          deleteBatch: '/saleOrder/deleteBatch'
        }
      }
    },
    methods: {
      loadData() {
        this.dataSource = []
        getRootChannel().then((res) => {
          if (res.success) {
            console.log(res.result)
            this.dataSource = res.result
          }
        })
      },
      expandSubmenu(expanded, record){
        if(expanded){
          getSubChannel({parentId:record.id}).then((res) => {
            if (res.success) {
              record.children = res.result
            }
          })
        }

      },
      // 打开数据规则编辑
      handleDataRule(record) {
        this.$refs.PermissionDataRuleList.edit(record)
      },
      handleAddSub(record) {
        this.$refs.modalForm.title = "添加子渠道";
        this.$refs.modalForm.localMenuType = 1;
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.edit({status:'1',permsType:'1',route:true,'parentId':record.id});
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>