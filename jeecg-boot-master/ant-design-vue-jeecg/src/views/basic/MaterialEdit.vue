<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <detail-list title="基本信息">
        <detail-list-item term="产品名称">{{material.name}}</detail-list-item>
        <detail-list-item term="产品编码">{{material.code}}</detail-list-item>
        <detail-list-item term="状态">{{material.rowStsName}}</detail-list-item>
        <detail-list-item term="产品规格">{{material.specification}}</detail-list-item>
        <detail-list-item term="产品类型">{{material.type}}</detail-list-item>
        <detail-list-item term="产品品牌">{{material.brand}}</detail-list-item>
      </detail-list>
      <a-divider style="margin-bottom: 32px"/>
      <detail-list title="其他信息">
        <detail-list-item term="库存上限">{{material.uplimit}}</detail-list-item>
        <detail-list-item term="库存下限">{{material.downlimit}}</detail-list-item>
        <detail-list-item term="单位">{{material.unit}}</detail-list-item>
      </detail-list>
      <a-divider style="margin-bottom: 32px"/>


      <div class="title">产品计量单位</div>
      <!-- 操作按钮区域 -->
      <div class="table-operator" style="border-top: 5px">
        <a-button @click="handleAddUnit" type="primary" icon="plus">添加产品计量单位</a-button>

        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px">
            批量操作 <a-icon type="down" />
          </a-button>
        </a-dropdown>
      </div>
      <a-table
        :size="size"
        :bordered="bordered"
        :loading="loading"
        :columns="columns"
        :dataSource="dataSource"
        :rowKey="rowKey"
        :pagination="pagination"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
        <!--&gt;-->

        <template slot="avatarslot" slot-scope="text, record, index">
          <div class="anty-img-wrap">
            <a-avatar shape="square" :src="getAvatarView(record.avatar)" icon="user"/>
          </div>
        </template>

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
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>

      <!-- 表单区域 -->
      <material-self-unit-modal ref="modalFormSelfUnit" @ok="modalFormOk"></material-self-unit-modal>


      <a-divider style="margin-bottom: 32px"/>
      <div class="title">产品辅助属性</div>
      <!-- 操作按钮区域 -->
      <div class="table-operator" style="border-top: 5px">
        <a-button @click="addMaterialAuxiliaryItem" type="primary" icon="plus">添加产品辅助属性</a-button>

        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px">
            批量操作 <a-icon type="down" />
          </a-button>
        </a-dropdown>
      </div>
      <a-table
        :size="size"
        :bordered="bordered"
        :loading="loading2"
        :columns="columns2"
        :dataSource="dataSource2"
        :rowKey="rowKey"
        :pagination="pagination2"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
        <!--&gt;-->

        <template slot="avatarslot" slot-scope="text, record, index">
          <div class="anty-img-wrap">
            <a-avatar shape="square" :src="getAvatarView(record.avatar)" icon="user"/>
          </div>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="editMaterialAuxiliaryLItem(record)">编辑</a>
          <a-divider type="vertical"/>
        </span>
      </a-table>
      <!-- 表单区域 -->
      <material-self-unit-modal ref="modalFormSelfUnit" @ok="modalFormOk"></material-self-unit-modal>
      <material-auxiliary-list ref="materialAuxiliaryList" v-on:auxiliaryFlag="modalFormOk2"></material-auxiliary-list>
    </a-card>
  </page-layout>
</template>

<script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import MaterialSelfUnitModal from './MaterialSelfUnitModal'
  import PageLayout from '@/components/page/PageLayout'
  import DetailList from '@/components/tools/DetailList'
  import MaterialAuxiliaryList from './MaterialAuxiliaryList'
  import {getMaterialOne} from '@/api/api'
  import { deleteAction, postAction, getAction } from '@/api/manage'
  const DetailListItem = DetailList.Item

  export default {
    mixins: [JeecgListMixin],
    components: {
      MaterialSelfUnitModal,
      PageLayout,
      DetailList,
      DetailListItem,
      MaterialAuxiliaryList
    },
    data () {
      return {
        material: {},
        /**
         * 表格大小风格，default, middle, small
         */
        size: 'default',
        loading: true,
        bordered: false,
        rowKey: 'id',
        model2: {},
        queryParam2: {},
        dataSource2: [],
        pagination: {},
        ipagination2: {
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
        columns: [
          {
            title: '单位',
            dataIndex: 'unit',
            key: 'unit'
          },
          {
            title: '单位类型',
            align:"center",
            dataIndex: 'unitType',
            customRender:function (text) {
              if (text == '0') {
                return '附属单位';
              }else if (text == '1') {
                return '主单位';
              }else {
                return text;
              }
            }
          },
          {
            title: '与主单位比例',
            align:"center",
            dataIndex: 'qty'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        columns2: [{
          title: '辅助属性名称',
          align: 'center',
          dataIndex: 'suppValueMap'
        },
          {
            title: '辅助属性CODE',
            align: 'center',
            dataIndex: 'suppCodeMap'
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
            align: 'center'
          }],
        selfUnitPage:[],
        selectedRowKeys: [],
        selectedRows: [],
        url: {
          list: '/materialSelfUnit/getPage?sourceId=' + this.$route.query.id,
          delete: '/materialSelfUnit/delete',
          deleteBatch: "/materialSelfUnit/deleteBatch",
          list2: '/materialAuxiliary/getPage?sourceId=' + this.$route.query.id,
          delete2: '/materialAuxiliary/delete',
          deleteBatch2: "/materialAuxiliary/deleteBatch"
        }
      }
    },
    computed: {
      title () {
        return this.$route.meta.title
      }
    },
    watch: {

      '$route' (to, from) {
        console.log(this.$route.query.id)
        if (this.$route.query.id) {
          getMaterialOne({id:this.$route.query.id}).then((res) => {
            if (res.success) {
              this.material = res.result;
            }
          })
        }
      }
    },
    create: {
      '$route' (to, from) {
        console.log(this.$route.query.id)
        if (this.$route.query.id) {
          getMaterialOne({id:this.$route.query.id}).then((res) => {
            if (res.success) {
              this.material = res.result;
            }
          })

        }
      }
    },
    mounted() {
      if (this.$route.query.id) {
        getMaterialOne({id:this.$route.query.id}).then((res) => {
          if (res.success) {
            this.material = res.result;
            this.loadData2();
          }
        })
      }
    },
    methods: {
      addMaterialAuxiliaryItem () {
        this.editMaterialAuxiliaryLItem({});
      },
      editMaterialAuxiliaryLItem (record) {
        // record.sourceAddId = this.saleOrderAddress.sourceAddId;
        this.$refs.materialAuxiliaryList.edit(record);
      },
      handleAddUnit: function () {
        this.$refs.modalFormSelfUnit.add(this.$route.query.id);
        this.$refs.modalFormSelfUnit.title = "新增";
        this.$refs.modalFormSelfUnit.disableSubmit = false;
      },
      handleDetail:function(record){
        this.$refs.modalFormSelfUnit.edit(record);
        this.$refs.modalFormSelfUnit.title="详情";
        this.$refs.modalFormSelfUnit.disableSubmit = true;
      },
      handleEdit: function (record) {
        this.$refs.modalFormSelfUnit.edit(record);
        this.$refs.modalFormSelfUnit.title = "编辑";
        this.$refs.modalFormSelfUnit.disableSubmit = false;
      },
      loadData2(arg) {
        if (!this.url.list2) {
          this.$message.error('请设置url.list2属性!')
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination2.current = 1
        }
        let params = this.getQueryParams2()//查询条件
        params.roleId = this.currentRoleId
        this.loading2 = true
        getAction(this.url.list2, params).then((res) => {
          if (res.success) {
            this.dataSource2 = res.result.records
            this.ipagination2.total = res.result.total

          }
          this.loading2 = false
        })
      },
      getQueryParams2() {
        //获取查询条件
        let param2 = {};
        if(this.$route.query.id){
          param2.sourceId = this.$route.query.id
        }else{
          param2.sourceId = -1;
        }
        param2.field = this.getQueryField2()
        param2.pageNo = this.ipagination2.current
        param2.pageSize = this.ipagination2.pageSize
        return param2;
      },
      getQueryField2() {
        var str = 'id,'
        this.columns2.forEach(function(value) {
          str += ',' + value.dataIndex
        })
        return str
      },
      handleDelete2: function(id) {
        if (!this.url.delete2) {
          this.$message.error('请设置url.delete2属性!')
          return
        }
        var that = this
        deleteAction(that.url.delete2, { id: id }).then((res) => {
          if (res.success) {
            that.$message.success(res.message)
            that.loadData2()
          } else {
            that.$message.warning(res.message)
          }
        })
      },
      modalFormOk2() {
        // 新增/修改 成功时，重载列表
        this.loadData2()
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
