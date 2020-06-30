<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form>
        <a-row :gutter="24">

          <a-col
            :md="6"
            :sm="10"
          >
            <a-form-item
              label="公司"
              hasFeedback
            >
              <a-select
                v-decorator="['showType', {}]"
                placeholder="请选择公司"
                @change="handleCpyChange"
              >
                <!-- <a-select-option value="">请选择</a-select-option> -->
                <a-select-option
                  v-for="(item, key) in companydict"
                  :key="key"
                  :value="item.id"
                >
                  <span
                    style="display: inline-block;width: 100%"
                    :title=" item.text || item.departName "
                  >
                    {{ item.text || item.departName }}
                  </span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col
            :md="24"
            :sm="24"
          >
            <a-form-item
              label="统计报表"
            >

              <a-row :gutter="24">
                <a-checkbox-group :options="reportdict" :value="reportids"  @change="oncheckboxChange"/>
              </a-row>

            </a-form-item>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button
        @click="handleOk"
        type="primary"
      >保存</a-button>
    </div>

  </a-card>
</template>

<script>
import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
// import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { queryDepartTreeList, ajaxGetDictItems } from '@/api/api'
import { httpAction, getAction } from '@/api/manage'

export default {
  name: 'ReportList',
  // mixins:[JeecgListMixin],
  components: {},
  data() {
    return {
      description: '公司配置统计报表',
      reportdict: [],
      companydict: [],
      companyid: '',
      reportids: [],
      // 表头
      url: {
        reportlist: '/report/getList'
      }
    }
  },
  computed: {},
  created() {
    //获取字典数据
    this.initDictData()
  },
  methods: {
    oncheckboxChange(checkedValues) {
      this.reportids = checkedValues
    },
    handleCpyChange(value) {
      const that = this;
      that.reportids =[];
      that.companyid = value
      getAction('/report/company/getList?companyId=' + that.companyid, '').then(res => {
        console.log(res);
        if(res.result && res.result.length > 0){
          for(let index in res.result){
            that.reportids.push(res.result[index].reportId);
          }
        }
      })
    },

    initDictData() {
      let that = this
      getAction(that.url.reportlist, '').then(res => {
        that.reportdict = res.result.map(action=>{
            return { label: action.name, value: action.id, defaultCheck: true };
        })
        console.log(res)
      })
      queryDepartTreeList().then(res => {
        if (res.success) {
          for (let i = 0; i < res.result.length; i++) {
            let temp = res.result[i]
            that.companydict = temp.children
          }
        }
      })
    },

    handleOk() {
      const that = this
      if (that.companyid.length == 0) {
        that.$message.warning('请选择公司')
        return
      }
      if (that.reportids.length == 0) {
        that.$message.warning('请选择需要展示的统计报表')
        return
      }
      httpAction(
        '/report/company/multiadd',
        {
          companyId: that.companyid,
          reportids: that.reportids
        },
        'post'
      )
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
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>