<template>
<a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭">
    <a-spin :spinning="confirmLoading">
        <a-form :form="form">
            <a-row>
                <a-col :span="12">
                    <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-select v-decorator="['vendorId', {}]" placeholder="请选择供应商">
                            <a-select-option v-for="(item, key) in vendorIddictOptions" :key="key" :value="item.id">
                                {{ item.name }}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="仓库" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-select v-decorator="['warehouseId', {}]" placeholder="请选择仓库">
                            <a-select-option v-for="(item, key) in warehouseOptions" :key="key" :value="item.id">
                                {{ item.name }}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="12">
                    <a-form-item label="结算账户" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="[ 'account', {}]" placeholder="请输入结算账户" style="width: 100%" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="[ 'description', {}]" placeholder="请输入备注"></a-input>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="12">
                    <a-form-item label="实付金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number v-decorator="[ 'payamount', {}]" placeholder="请输入实付金额" style="width: 100%" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="总金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input-number v-decorator="[ 'totalamount', {}]" placeholder="请输入总金额" style="width: 100%" />
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="24">
                    <a-card>
                        <form :autoFormCreate="(form) => this.form = form">
                            <a-table :columns="columns" :dataSource="data" :pagination="false">
                                <template v-for="(col, i) in ['name', 'workId', 'department']" :slot="col" slot-scope="text, record, index">
                                    <a-input :key="col" v-if="record.editable" style="margin: -5px 0" :value="text" :placeholder="columns[i].title" @change="e => handleChange(e.target.value, record.key, col)" />
                                    <template v-else>{{ text }}</template>
                                </template>
                                <template slot="operation" slot-scope="text, record, index">
                                    <template v-if="record.editable">
                                        <span v-if="record.isNew">
                                            <a @click="saveRow(record.key)">添加</a>
                                            <a-divider type="vertical" />
                                            <a-popconfirm title="是否要删除此行？" @confirm="remove(record.key)">
                                                <a>删除</a>
                                            </a-popconfirm>
                                        </span>
                                        <span v-else>
                                            <a @click="saveRow(record.key)">保存</a>
                                            <a-divider type="vertical" />
                                            <a @click="cancel(record.key)">取消</a>
                                        </span>
                                    </template>
                                    <span v-else>
                                        <a @click="toggle(record.key)">编辑</a>
                                        <a-divider type="vertical" />
                                        <a-popconfirm title="是否要删除此行？" @confirm="remove(record.key)">
                                            <a>删除</a>
                                        </a-popconfirm>
                                    </span>
                                </template>
                            </a-table>
                            <a-button style="width: 100%; margin-top: 16px; margin-bottom: 8px" type="dashed" icon="plus" @click="newMember">新增成员</a-button>
                        </form>
                    </a-card>

                </a-col>
            </a-row>
        </a-form>
    </a-spin>
</a-modal>
</template>

<script>
import {
    httpAction
} from '@/api/manage'
import pick from 'lodash.pick'
import JDate from '@/components/jeecg/JDate'
import JDictSelectTag from '@/components/dict/JDictSelectTag'
export default {
    name: 'PurchasesModal',
    components: {
        JDate,
        JDictSelectTag
    },
    data() {
        return {
            form: this.$form.createForm(this),
            title: '操作',
            width: '80%',
            visible: false,
            model: {},
            labelCol: {
                xs: {
                    span: 24
                },
                sm: {
                    span: 5
                }
            },
            wrapperCol: {
                xs: {
                    span: 24
                },
                sm: {
                    span: 16
                }
            },
            confirmLoading: false,
            validatorRules: {
                vendorId: {},
                businessDate: {
                    rules: [{
                        required: true,
                        message: '请输入业务时间!'
                    }]
                },
                amount: {},
                description: {},
                states: {}
            },
            url: {
                add: '/purchase/add',
                edit: '/purchase/edit'
            },
            vendorIddictOptions: [],
            warehouseOptions: [],
            // purchasestypedict: [],
            columns: [{
                    title: '成员姓名',
                    dataIndex: 'name',
                    key: 'name',
                    width: '20%',
                    scopedSlots: {
                        customRender: 'name'
                    }
                },
                {
                    title: '工号',
                    dataIndex: 'workId',
                    key: 'workId',
                    width: '20%',
                    scopedSlots: {
                        customRender: 'workId'
                    }
                },
                {
                    title: '所属部门',
                    dataIndex: 'department',
                    key: 'department',
                    width: '40%',
                    scopedSlots: {
                        customRender: 'department'
                    }
                },
                {
                    title: '操作',
                    key: 'action',
                    scopedSlots: {
                        customRender: 'operation'
                    }
                }
            ],
        }
    },
    created() {},
    methods: {
        add() {
            this.edit({})
        },
        edit(record) {
            this.vendorIddictOptions = this.$parent.$parent.dictOptions.vendorId
            this.warehouseOptions = this.$parent.$parent.dictOptions.warehouse
            this.form.resetFields()
            this.model = Object.assign({}, record)
            this.visible = true
            this.$nextTick(() => {
                this.form.setFieldsValue(
                    pick(this.model, 'vendorId', 'description', 'warehouseId', 'account', 'payamount', 'totalamount')
                )
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
                if (!err) {
                    that.confirmLoading = true
                    let httpurl = ''
                    let method = ''
                    if (!this.model.id) {
                        httpurl += this.url.add
                        method = 'post'
                    } else {
                        httpurl += this.url.edit
                        method = 'put'
                    }
                    let formData = Object.assign(this.model, values)
                    console.log('表单提交数据', formData)
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
        },
        popupCallback(row) {
            this.form.setFieldsValue(
                pick(row, 'vendorId', 'description', 'warehouseId', 'account', 'payamount', 'totalamount')
            )
        }
    }
}
</script>
