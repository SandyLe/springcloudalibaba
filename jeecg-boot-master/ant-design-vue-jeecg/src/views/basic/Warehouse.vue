<template>
  <a-card :bordered="false" class="card-area">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="12">
            <a-form-item label="名称">
              <j-input placeholder="输入名称模糊查询" v-model="queryParam.name"></j-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="12">
            <a-form-item label="代码">
              <j-input placeholder="输入代码模糊查询" v-model="queryParam.code"></j-input>
            </a-form-item>
          </a-col>


          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
        <a-row :gutter="24">
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="所属门店">
                <a-select v-model="queryParam.belongsToId" placeholder="请选择门店">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in shopList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.departName || item.departNameEn ">
                      {{ item.departName || item.departNameEn }}
                    </span>
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="负责人">
                <a-select v-model="queryParam.principalId" placeholder="请选择负责人">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="(item, key) in userList" :key="key" :value="item.id">
                    <span style="display: inline-block;width: 100%" :title=" item.realname || item.username ">
                      {{ item.realname || item.username }}
                    </span>
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </template>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator" style="border-top: 5px">
      <a-button @click="handleAdd" type="primary" icon="plus">添加仓库</a-button>

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
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <warehouse-modal ref="modalForm" @ok="modalFormOk"></warehouse-modal>

  </a-card>
</template>

<script>
  import WarehouseModal from './WarehouseModal'
  import JInput from '@/components/jeecg/JInput'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {loadShopData, getAllUser} from '@/api/api'
  import ARow from "ant-design-vue/es/grid/Row";
  export default {
    name: "Warehouse",
    mixins: [JeecgListMixin],
    components: {
      ARow,
      JInput,
      WarehouseModal
    },
    data() {
      return {
        queryParam: {},
        // 表头
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
            title: '仓库名称',
            align:"center",
            dataIndex: 'name'
          },
          {
            title: '仓库编码',
            align:"center",
            dataIndex: 'code'
          },
          {
            title: '所属门店',
            align:"center",
            dataIndex: 'belongsToName'
          },
          {
            title: '负责人',
            align:"center",
            dataIndex: 'principalName'
          },
          {
            title: '备注',
            align:"center",
            dataIndex: 'content'
          },
          {
            title: '创建时间',
            dataIndex: 'createTime',
            align:"center",
            sorter: true
          },
          {
            title: '更新时间',
            dataIndex: 'updateTime',
            align:"center",
            sorter: true
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/warehouse/getPage",
          delete: "/warehouse/delete",
          deleteBatch: "/warehouse/deleteBatch"
        },
        shopList: [],
        userList: []
      }
    },
    mounted() {
      var params = this.getQueryParams();//查询条件
      params.orgType = '1';
      loadShopData(params).then((res) => {
        if (res.success) {
          this.shopList = res.result;
        }
      })
      getAllUser().then((res) => {
        if (res.success) {
          this.userList = res.result;
        }
      })
    }
  }
</script>

<style scoped>

</style>