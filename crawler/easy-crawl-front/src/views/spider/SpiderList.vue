<template>
    <a-card :bordered="false">
        <div class="table-page-search-wrapper">
            <a-form layout="inline">
                <a-row :gutter="48">
                    <a-col
                            :md="8"
                            :sm="24"
                    >
                        <a-form-item label="蜘蛛名">
                            <a-input
                                    v-model="searchModel.name"
                                    placeholder=""
                            />
                        </a-form-item>
                    </a-col>

                    <template v-if="advanced">
                        <a-col
                                :md="8"
                                :sm="24"
                        >
                            <a-form-item label="表名">
                                <a-input
                                        v-model="searchModel.tableName"
                                        placeholder=""
                                />
                            </a-form-item>
                        </a-col>
                    </template>
                    <a-col
                            :md="!advanced && 8 || 24"
                            :sm="24"
                    >
            <span
                    class="table-page-search-submitButtons"
                    :style="advanced && { float: 'right', overflow: 'hidden' } || {} "
            >
              <a-button
                      type="primary"
                      @click="search"
              >查询</a-button>
              <a-button
                      style="margin-left: 8px"
                      @click="() => searchModel = {}"
              >重置</a-button>
              <a
                      @click="toggleAdvanced"
                      style="margin-left: 8px"
              >
                {{ advanced ? '收起' : '展开' }}
                <a-icon :type="advanced ? 'up' : 'down'"/>
              </a>
            </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>

        <div class="table-operator">
            <a-button
                    type="primary"
                    icon="plus"
                    @click="openCreateSpider"
            >新建
            </a-button>
            <a-button
                    icon="plus"
                    type="primary"
                    ghost
                    @click="importVisible = true"
            >导入配置
            </a-button>
        </div>

        <a-table
                :columns="columns"
                :rowKey="record => record.id"
                :dataSource="data"
                :pagination="pagination"
                :loading="loading"
                @change="handleTableChange"
        >

            <template
                    slot="proxy"
                    slot-scope="text, record"
            >
                {{ record.proxyChannelId ? proxies.find(v=>v.id === record.proxyChannelId).alias : "-" }}
            </template>
            <template
                    slot="action"
                    slot-scope="text, record"
            >
                <a-popconfirm
                        title="任务将提交到后台运行, 确认运行?"
                        @confirm="runSpider(record)"
                        okText="确认"
                        cancelText="取消"
                        class="m-b-8"
                >
                    <a-icon
                            slot="icon"
                            type="question-circle-o"
                            style="color: red"
                    />
                    <a-button
                            class="m-r-8"
                            type="primary"
                            size="small"
                    >运行
                    </a-button>
                </a-popconfirm>
                <a-button
                        class="m-r-8 m-b-8"
                        type="primary"
                        size="small"
                        @click="optimizeSpider(record)"
                >设置运行参数
                </a-button>

                <!--<a-button
                  class="m-r-8 m-b-8"
                  type="primary"
                  size="small"
                  @click="configSpiderProxy(record)"
                >配置代理
                </a-button>-->

                <a-button
                        class="m-r-8 m-b-8"
                        type="primary"
                        size="small"
                        @click="goPage('/spider/jobList?spiderName=' + record.name + '&spiderTableName=' + record.tableName)"
                >查看任务
                </a-button>
                <a-button
                        class="m-r-8 m-b-8"
                        type="primary"
                        size="small"
                        @click="copySpiderConfig(record)"
                >导出配置
                </a-button>
                <a-button
                        class="m-r-8 m-b-8"
                        type="primary"
                        size="small"
                        @click="modifySpiderConfig(record)"
                >修改
                </a-button>
                <a-button
                        class="m-r-8 m-b-8"
                        type="primary"
                        size="small"
                        @click="cloneSpiderConfig(record)"
                >克隆
                </a-button>
                <a-popconfirm
                        title="确认删除?"
                        @confirm="deleteSpider(record)"
                        okText="确认"
                        cancelText="取消"
                >
                    <a-icon
                            slot="icon"
                            type="question-circle-o"
                            style="color: red"
                    />
                    <a-button
                            class="m-r-8"
                            type="danger"
                            size="small"
                    >删除
                    </a-button>
                </a-popconfirm>

            </template>
        </a-table>

        <a-modal
                title="设置运行参数"
                width="50%"
                @ok="doOptimizeSpider"
                :maskClosable="false"
                :visible="optimizeDialog"
                @cancel="optimizeDialog = false"
        >
            <a-form :form="optimizeSpiderForm">
                <a-form-item
                        label="线程数"
                        :label-col="{ span: 8 }"
                        :wrapper-col="{ span: 12 }"
                >
                    <a-input
                            v-decorator="['threadNum', {rules: [{ required: true, message: '请输入线程数' }], initialValue: optimizeSpiderForm.threadNum}]"/>
                </a-form-item>
                <a-form-item
                        label="页面处理完后的睡眠时间（秒）"
                        :label-col="{ span: 8 }"
                        :wrapper-col="{ span: 12 }"
                >
                    <a-input
                            v-decorator="['sleepTime', {rules: [{ required: true, message: '请输入每个页面处理完后的睡眠时间' }], initialValue: optimizeSpiderForm.sleepTime}]"/>
                </a-form-item>
                <a-form-item
                        label="页面下载失败重试次数"
                        :label-col="{ span: 8 }"
                        :wrapper-col="{ span: 12 }"
                >
                    <a-input
                            v-decorator="['retryTimes', {rules: [{ required: true, message: '请输入页面下载失败重试次数' }], initialValue: optimizeSpiderForm.retryTimes}]"/>
                </a-form-item>
                <a-form-item
                        label="重试睡眠时间（秒）"
                        :label-col="{ span: 8 }"
                        :wrapper-col="{ span: 12 }"
                >
                    <a-input
                            v-decorator="['retrySleepTime', {rules: [{ required: true, message: '请输入重试睡眠时间' }], initialValue: optimizeSpiderForm.retrySleepTime}]"/>
                </a-form-item>
                <a-form-item
                        label="页面爬取失败后放回队列的次数"
                        :label-col="{ span: 8 }"
                        :wrapper-col="{ span: 12 }"
                >
                    <a-input
                            v-decorator="['cycleRetryTimes', {rules: [{ required: true, message: '请输入页面爬取失败后放回队列的次数' }], initialValue: optimizeSpiderForm.cycleRetryTimes}]"/>
                </a-form-item>
                <a-form-item
                        label="下载页面超时时间（秒）"
                        :label-col="{ span: 8 }"
                        :wrapper-col="{ span: 12 }"
                >
                    <a-input
                            v-decorator="['timeOut', {rules: [{ required: true, message: '请输入下载页面超时时间' }], initialValue: optimizeSpiderForm.timeOut}]"/>
                </a-form-item>
            </a-form>
        </a-modal>

        <a-modal
                :title="isUpdate ? '跟新蜘蛛' : '新建蜘蛛'"
                width="50%"
                :maskClosable="false"
                :visible="visible"
                @cancel="visible = false"
        >

            <a-spin :spinning="opLoading">
                <a-steps
                        :current="currentStep"
                        :style="{ marginBottom: '28px' }"
                        size="small"
                >
                    <a-step title="基本信息"/>
                    <a-step title="配置字段规则"/>
                </a-steps>
                <a-form :form="form">
                    <div v-show="currentStep === 0">
                        <a-form-item
                                label="蜘蛛名"
                                :label-col="{ span: 5 }"
                                :wrapper-col="{ span: 19 }"
                        >
                            <a-row>
                                <a-col :span="15">
                                    <a-input
                                            v-decorator="['name', {rules: [{ required: true, message: '请输入蜘蛛名' }], initialValue: form.name}]"/>
                                </a-col>
                            </a-row>

                        </a-form-item>
                        <a-form-item
                                label="表名"
                                :label-col="{ span: 5 }"
                                :wrapper-col="{ span: 19 }"
                        >
                            <a-row>
                                <a-col :span="15">
                                    <a-input
                                            :disabled="isUpdate"
                                            v-decorator="['tableName', {rules: [{ required: true, message: '请输入表名' }], initialValue: form.tableName}]"
                                    />
                                </a-col>
                            </a-row>

                        </a-form-item>
                        <a-form-item
                                label="入口页"
                                :label-col="{ span: 5 }"
                                :wrapper-col="{ span: 19 }"
                        >
                            <a-row>
                                <a-col :span="15">
                                    <a-input
                                            v-decorator="['entryUrl', {rules: [{ required: true, message: '请输入入口页' }], initialValue: form.entryUrl}]"/>
                                </a-col>
                                <a-col :span="9">
                                    <a-button
                                            class="m-l-24"
                                            type="dashed"
                                            size="small"
                                            @click="testPage(form.getFieldValue('entryUrl'))"
                                    ><i class="iconfont icon-debug"></i></a-button>
                                </a-col>
                            </a-row>
                        </a-form-item>

                        <a-form-item
                                label="列表页正则"
                                :label-col="{ span: 5 }"
                                :wrapper-col="{ span: 19 }"
                        >
                            <a-row>
                                <a-col :span="15">
                                    <a-input
                                            v-decorator="['listRegex', {rules: [{ required: true, message: '请输入列表页正则' }], initialValue: form.listRegex}]"/>
                                </a-col>
                                <a-col :span="9">
                                    <a-button
                                            class="m-l-24"
                                            type="dashed"
                                            size="small"
                                            @click="testRegex(form.getFieldValue('entryUrl'), form.getFieldValue('listRegex'))"
                                    ><i class="iconfont icon-debug"></i></a-button>
                                </a-col>
                            </a-row>

                        </a-form-item>

                        <a-form-item
                                label="正文页XPATH补充URL"
                                :label-col="{ span: 5 }"
                                :wrapper-col="{ span: 19 }"
                        >
                            <a-row>
                                <a-col :span="15">
                                    <a-input
                                            v-decorator="['contentUrl', {rules: [{ required: false }], initialValue: form.contentUrl}]"/>
                                </a-col>
                                <a-col :span="9">
                                    <!--<a-button
                                            class="m-l-24"
                                            type="dashed"
                                            size="small"
                                            @click="testXpath(form.getFieldValue('entryUrl'), form.getFieldValue('contentXpath'))">
                                      <i class="iconfont icon-debug"></i>
                                    </a-button>-->
                                </a-col>
                            </a-row>
                        </a-form-item>

                        <a-form-item
                                label="正文页XPATH"
                                :label-col="{ span: 5 }"
                                :wrapper-col="{ span: 19 }"
                        >
                            <a-row>
                                <a-col :span="15">
                                    <a-input
                                            v-decorator="['contentXpath', {rules: [{ required: false }], initialValue: form.contentXpath}]"/>
                                </a-col>
                                <a-col :span="9">
                                    <a-button
                                            class="m-l-24"
                                            type="dashed"
                                            size="small"
                                            @click="testXpath(form.getFieldValue('entryUrl'), form.getFieldValue('contentXpath'))"
                                    ><i class="iconfont icon-debug"></i></a-button>
                                </a-col>
                            </a-row>

                        </a-form-item>
                    </div>
                    <div v-show="currentStep === 1">
                        <a-form-item
                                label="处理time的正则"
                                :label-col="{ span: 5 }"
                                :wrapper-col="{ span: 19 }"
                        >
                            <a-row>
                                <a-col :span="15">
                                    <a-input
                                            v-decorator="['timeRegex', {rules: [{ required: false }], initialValue: form.timeRegex}]"/>
                                </a-col>

                            </a-row>
                        </a-form-item>

                        <a-form-item
                                label="作者"
                                :label-col="{ span: 5 }"
                                :wrapper-col="{ span: 19 }"
                        >
                            <a-row>
                                <a-col :span="15">
                                    <a-input
                                            v-decorator="['author', {rules: [{ required: false }], initialValue: form.author}]"/>
                                </a-col>

                            </a-row>
                        </a-form-item>

                        <div>
                            <h1>列表页字段</h1>
                            <a-button
                                    v-if="!isUpdate"
                                    class="m-b-8"
                                    type="primary"
                                    @click="handleAdd"
                            >新增列表页字段
                            </a-button>
                            <a-table
                                    :pagination="false"
                                    bordered
                                    :rowKey="record => record.index"
                                    :dataSource="dataSource"
                                    :columns="fields"
                            >
                                <template
                                        slot="key"
                                        slot-scope="text, record"
                                >
                                    <a-input
                                            :value="text"
                                            @change="handleFieldChange(record.index, 'key', $event)"
                                            placeholder=""
                                            :disabled="isUpdate"
                                    />
                                </template>
                                <template
                                        slot="value"
                                        slot-scope="text, record"
                                >
                                    <a-input
                                            :value="text"
                                            @change="handleFieldChange(record.index, 'value', $event)"
                                            placeholder=""
                                    />
                                </template>
                                <template
                                        slot="operation"
                                        slot-scope="text, record"
                                >
                                    <a-button
                                            type="dashed"
                                            size="small"
                                            @click="testXpath(form.getFieldValue('entryUrl'), record.value)"
                                    ><i class="iconfont icon-debug"></i></a-button>

                                    <a-button
                                            v-if="!isUpdate"
                                            class="m-l-8"
                                            type="dashed"
                                            size="small"
                                            @click="handleDelete(record.index)"
                                    ><i class="iconfont icon-shanchu text-danger"></i></a-button>
                                </template>
                            </a-table>
                        </div>
                        <a-divider></a-divider>
                        <div>
                            <h1>正文页字段</h1>
                            <a-button
                                    v-if="!isUpdate"
                                    class="m-b-8"
                                    type="primary"
                                    @click="handleAddContentField"
                            >新增正文页字段
                            </a-button>
                            <a-table
                                    :pagination="false"
                                    bordered
                                    :rowKey="record => record.index"
                                    :dataSource="contentDataSource"
                                    :columns="contentFields"
                            >
                                <template
                                        slot="key"
                                        slot-scope="text, record"
                                >
                                    <a-input
                                            :value="text"
                                            @change="handleContentFiledChange(record.index, 'key', $event)"
                                            placeholder=""
                                            :disabled="isUpdate"
                                    />
                                </template>
                                <template
                                        slot="value"
                                        slot-scope="text, record"
                                >
                                    <a-input
                                            :value="text"
                                            @change="handleContentFiledChange(record.index, 'value', $event)"
                                            placeholder=""
                                    />
                                </template>
                                <template
                                        slot="operation"
                                        slot-scope="text, record"
                                >
                                    <a-button
                                            class="m-l-24"
                                            type="dashed"
                                            size="small"
                                            @click="testContentXpath(form.getFieldValue('entryUrl'),form.getFieldValue('contentUrl') ,form.getFieldValue('contentXpath'), record.value)"
                                    ><i class="iconfont icon-debug"></i></a-button>
                                    <a-button
                                            v-if="!isUpdate"
                                            class="m-l-24"
                                            type="dashed"
                                            size="small"
                                            @click="handleContentDelete(record.index)"
                                    ><i class="iconfont icon-shanchu text-danger"></i></a-button>
                                </template>
                            </a-table>
                        </div>

                    </div>

                </a-form>
            </a-spin>
            <template slot="footer">
                <a-button
                        key="back"
                        @click="backward"
                        v-if="currentStep > 0"
                        :style="{ float: 'left' }"
                >上一步
                </a-button>
                <a-button
                        key="cancel"
                        @click="visible = false"
                >取消
                </a-button>
                <a-button
                        key="forward"
                        :loading="opLoading"
                        type="primary"
                        @click="handleNext(currentStep)"
                >{{ currentStep === 1 && '完成' || '下一步' }}
                </a-button>
            </template>
        </a-modal>

        <a-modal
                title="配置代理"
                width="50%"
                @ok="doConfigProxy"
                :maskClosable="false"
                :visible="configProxyDialog"
                @cancel="configProxyDialog = false"
        >
            <div class="proxy-list">
                <a-card
                        class="proxy"
                        v-for="(item) of proxies"
                        :key="item.id"
                        hoverable
                        @click="chooseProxy(item)"
                >
                    <div
                            class="cover"
                            v-if="item.selected"
                    >选中
                    </div>
                    <img
                            slot="cover"
                            alt="example"
                            src="../../assets/proxy/data5u.jpg"
                    />
                    <template
                            slot="actions"
                            class="ant-card-actions"
                    >

                    </template>
                    <a-card-meta
                            :title="item.alias"
                            :description="'IP提取码：' + item.token"
                    >
                    </a-card-meta>
                </a-card>
            </div>

        </a-modal>

        <a-modal
                title="测试结果"
                v-model="testVisible"
                :maskClosable="true"
                :footer="null"
        >
            <p
                    v-for="(item, index) in testResults"
                    :key="index"
            >{{ item }}</p>
        </a-modal>

        <a-modal
                title="导入配置"
                width="50%"
                :maskClosable="false"
                :visible="importVisible"
                @cancel="importVisible = false"
        >
            <a-form :form="importForm">
                <div>
                    <a-form-item
                            label="蜘蛛配置字符串"
                            :label-col="{ span: 5 }"
                            :wrapper-col="{ span: 16 }"
                    >
                        <a-textarea
                                :rows="10"
                                v-decorator="['spider', {rules: [{ required: true, message: '请粘贴蜘蛛配置字符串' }]}]"
                        />
                    </a-form-item>
                </div>
            </a-form>

            <template slot="footer">
                <a-button
                        key="cancel"
                        @click="importVisible = false"
                >取消
                </a-button>
                <a-button
                        key="forward"
                        :loading="opLoading"
                        type="primary"
                        @click="doImportSpider"
                >导入
                </a-button>
            </template>
        </a-modal>

        <a-modal
                title="导出配置"
                width="50%"
                :maskClosable="false"
                :visible="exportVisible"
                @cancel="exportVisible = false"
        >
            <div>
                {{ spiderJson }}
            </div>
            <template slot="footer">
                <a-button
                        key="cancel"
                        @click="exportVisible = false"
                >关闭
                </a-button>
                <a-button
                        key="forward"
                        type="primary"
                        v-clipboard="spiderJson"
                        v-clipboard:success="clipboardSuccessHandler"
                >复制
                </a-button>
            </template>
        </a-modal>

    </a-card>
</template>

<script>
    import moment from 'moment'
    import _ from 'lodash'
    import {Ellipsis} from '@/components'
    import {
        searchSpider,
        addSpider,
        exportSpider,
        deleteSpider,
        updateSpider,
        configSpiderSettings,
        configSpiderProxy,
        testPage,
        testXpath,
        testRegex,
        testContentXpath
    } from '@/api/spider'
    import {addJob} from '@/api/job'
    import {listProxy} from '@/api/proxy'
    import {setTimeout} from 'timers'
    import {length} from 'lodash.pick'

    const defaultModel = {
        pageNo: 1,
        pageSize: 10,
        count: 0
    }

    const columns = [
        {
            title: '蜘蛛名',
            dataIndex: 'name'
        },
        {
            width: 150,
            title: '表名',
            dataIndex: 'tableName'
        },

        /*{
          title: '代理',
          dataIndex: 'proxy',
          scopedSlots: { customRender: 'proxy' }
        },*/
        {
            width:600,
            title: '操作',
            dataIndex: 'action',
            scopedSlots: {customRender: 'action'}
        }
    ]
    export default {
        name: 'SpiderList',
        components: {
            Ellipsis
        },
        data() {
            return {
                currentStep: 0,
                mdl: {},
                // 高级搜索 展开/关闭
                advanced: false,
                // 查询参数
                searchModel: _.cloneDeep(defaultModel),

                data: [],
                columns,
                pagination: {},
                loading: false,
                visible: false,
                importVisible: false /* 导入配置 */,
                exportVisible: false,
                spiderJson: '',
                importForm: this.$form.createForm(this),
                opLoading: false,
                form: this.$form.createForm(this), /* ant-design-vue UI组件里面的form组件，创建form实例的方法 */
                stepForms: [['name', 'tableName', 'entryUrl', 'listRegex']],
                index: 0,
                fields: [
                    {
                        title: '字段',
                        dataIndex: 'key',
                        scopedSlots: {customRender: 'key'}
                    },
                    {
                        title: 'XPATH规则',
                        dataIndex: 'value',
                        scopedSlots: {customRender: 'value'}
                    },
                    {
                        title: '操作',
                        dataIndex: 'operation',
                        scopedSlots: {customRender: 'operation'}
                    }
                ],
                dataSource: [],
                isUpdate: false,

                contentIndex: 0,
                contentDataSource: [],
                contentFields: [
                    {
                        title: '字段',
                        dataIndex: 'key',
                        scopedSlots: {customRender: 'key'}
                    },
                    {
                        title: 'XPATH规则',
                        dataIndex: 'value',
                        scopedSlots: {customRender: 'value'}
                    },
                    {
                        title: '操作',
                        dataIndex: 'operation',
                        scopedSlots: {customRender: 'operation'}
                    }
                ],

                optimizeDialog: false,
                optimizeSpiderForm: this.$form.createForm(this),

                proxyForm: {},
                configProxyDialog: false,
                proxies: [],

                // 测试
                testResults: [],
                testVisible: false
            }
        },

        mounted() {
            this.listProxy(true)
        },

        methods: {
            testContentXpath(url, contentUrl, contentXpath, xpath) {
                if (!url || !contentXpath || !xpath) {
                    return
                }
                testContentXpath(url, contentUrl, contentXpath, xpath).then((resp) => {
                    if (resp.success) {
                        this.testVisible = true
                        this.testResults = resp.data
                    }
                })
            },

            testPage(url) {
                if (!url) {
                    return
                }
                testPage(url).then((resp) => {
                    if (resp.success) {
                        this.testVisible = true
                        this.testResults = [resp.data]
                    }
                })
            },

            testXpath(url, xpath) {
                if (!url || !xpath) {
                    return
                }
                testXpath(url, xpath).then((resp) => {
                    if (resp.success) {
                        this.testVisible = true
                        this.testResults = resp.data
                    }
                })
            },

            testRegex(url, regex) {
                if (!url || !regex) {
                    return
                }
                testRegex(url, regex).then((resp) => {
                    if (resp.success) {
                        this.testVisible = true
                        this.testResults = resp.data
                    }
                })
            },

            chooseProxy(item) {
                this.proxyForm.proxyChannelId = item.id
                this.proxies.forEach((v) => {
                    if (v.id === item.id) {
                        if (v.selected) {
                            v.selected = false
                            this.proxyForm.proxyChannelId = null
                        } else {
                            v.selected = true
                        }
                    } else {
                        v.selected = false
                    }
                })

                this.proxies = [...this.proxies]
            },

            doConfigProxy() {
                const form = {
                    id: this.proxyForm.id,
                    proxyChannelId: this.proxyForm.proxyChannelId
                }
                configSpiderProxy(form).then((resp) => {
                    if (resp.success) {
                        this.$message.success('配置成功')
                        this.configProxyDialog = false
                        this.search()
                    } else {
                        this.$message.error(resp.resultMessage)
                    }
                })
            },

            listProxy(search) {
                listProxy({}).then((resp) => {
                    if (resp.success) {
                        const proxies = resp.data
                        proxies.forEach((v) => {
                            if (v.id === this.proxyForm.proxyChannelId) {
                                if (v.selected) {
                                    v.selected = false
                                } else {
                                    v.selected = true
                                }
                            } else {
                                v.selected = false
                            }
                        })
                        this.proxies = proxies

                        if (search) {
                            this.search()
                        }
                    }
                })
            },

            configSpiderProxy(record) {
                this.proxyForm = {...record}
                this.configProxyDialog = true
                this.listProxy()
            },

            optimizeSpider(record) {
                this.optimizeSpiderForm = this.$form.createForm(this)
                this.optimizeSpiderForm = {...this.optimizeSpiderForm, ...record}
                this.optimizeDialog = true
            },

            doOptimizeSpider() {
                this.optimizeSpiderForm.validateFields((errors, values) => {
                    if (!errors) {
                        const form = {
                            id: this.optimizeSpiderForm.id,
                            threadNum: values.threadNum,
                            sleepTime: values.sleepTime,
                            retryTimes: values.retryTimes,
                            retrySleepTime: values.retrySleepTime,
                            cycleRetryTimes: values.cycleRetryTimes,
                            timeOut: values.timeOut
                        }
                        configSpiderSettings(form)
                            .then((data) => {
                                if (data.success) {
                                    this.$message.success('设置成功')
                                    this.search()
                                    this.optimizeDialog = false
                                }
                            })
                            .catch((err) => {
                                console.log(err)
                            })
                    }
                })
            },

            // 开始创建爬虫 打开窗口
            openCreateSpider() {
                this.visible = true
                this.form = this.$form.createForm(this)
                this.dataSource = []
                this.index = 0
                this.isUpdate = false
                this.currentStep = 0

                this.contentIndex = 0
                this.contentDataSource = []
            },

            // 修改爬虫
            modifySpiderConfig(record) {
                this.visible = true
                this.form = this.$form.createForm(this)
                this.form = {...this.form, ...record}
                this.dataSource = JSON.parse(this.form.fieldsJson)
                this.index = this.dataSource[this.dataSource.length - 1].index
                this.isUpdate = true
                this.currentStep = 0

                if (this.form.contentFieldsJson) {
                    this.contentDataSource = JSON.parse(this.form.contentFieldsJson)
                    if (this.contentDataSource && this.contentDataSource.length) {
                        this.contentIndex = this.contentDataSource[
                        this.contentDataSource.length - 1
                            ].index
                    }
                }
            },

            // 克隆爬虫
            cloneSpiderConfig(record) {
                this.visible = true
                this.form = this.$form.createForm(this)
                this.form = {...this.form, ...record}
                this.dataSource = JSON.parse(this.form.fieldsJson)
                this.index = this.dataSource[this.dataSource.length - 1].index
                this.isUpdate = false
                this.currentStep = 0

                if (this.form.contentFieldsJson) {
                    this.contentDataSource = JSON.parse(this.form.contentFieldsJson)
                    if (this.contentDataSource && this.contentDataSource.length) {
                        this.contentIndex = this.contentDataSource[
                        this.contentDataSource.length - 1
                            ].index
                    }
                }
            },

            // 删除爬虫
            deleteSpider(record) {
                deleteSpider(record.id).then((data) => {
                    if (data.success) {
                        this.$message.success('删除成功')
                        this.search()
                    }
                })
            },

            goPage(val) {
                this.$router.push(val)
            },
            copySpiderConfig(record) {
                exportSpider(record.id).then((data) => {
                    if (data.success) {
                        this.spiderJson = JSON.stringify(data.data)
                        this.$message.success('导出成功，快去复制吧~')
                        this.exportVisible = true
                    }
                })
            },

            clipboardSuccessHandler() {
                this.$message.success('复制成功!')
            },

            runSpider(record) {
                addJob({configurableSpiderId: record.id}).then((data) => {
                    if (data.success) {
                        this.$message.success('运行成功')
                        // 跳转任务界面
                        this.$router.push(`/spider/jobRecord/${data.data.id}`)
                    }
                })
            },

            doImportSpider() {
                this.opLoading = true
                this.importForm.validateFields((err, values) => {
                    if (!err) {
                        const form = _.cloneDeep(JSON.parse(values.spider))
                        addSpider(form)
                            .then((data) => {
                                if (data.success) {
                                    this.$message.success('新建成功')
                                    this.importVisible = false
                                    this.importForm.resetFields()
                                    this.search()
                                }
                                this.opLoading = false
                            })
                            .catch((err) => {
                                console.log(err)
                                this.opLoading = false
                            })
                    }
                })
            },

            handleAddContentField() {
                const {contentIndex, contentDataSource} = this
                const newData = {
                    index: contentIndex + 1,
                    key: '',
                    value: ''
                }
                this.contentIndex++
                this.contentDataSource = [...contentDataSource, newData]
            },

            handleAdd() {
                const {index, dataSource} = this

                const newData = {
                    index: index + 1,
                    key: '',
                    value: ''
                }
                this.index++
                this.dataSource = [...dataSource, newData]
            },

            handleDelete(index) {
                this.dataSource.splice(
                    this.dataSource.findIndex((item) => item.index === index),
                    1
                )
            },

            handleContentDelete(index) {
                this.contentDataSource.splice(
                    this.contentDataSource.findIndex((item) => item.index === index),
                    1
                )
            },

            handleFieldChange(index, dataIndex, e) {
                const dataSource = [...this.dataSource]
                const target = dataSource.find((item) => item.index === index)
                if (target) {
                    target[dataIndex] = e.target.value
                    this.dataSource = dataSource
                }
            },
            handleContentFiledChange(index, dataIndex, e) {
                const contentDataSource = [...this.contentDataSource]
                const target = contentDataSource.find((item) => item.index === index)
                if (target) {
                    target[dataIndex] = e.target.value
                    this.contentDataSource = contentDataSource
                }
            },
            backward() {
                this.currentStep--
            },
            handleNext(step) {
                const currentStep = step + 1
                if (currentStep <= 1) {
                    // stepForms
                    this.form.validateFields(
                        this.stepForms[this.currentStep],
                        (errors, values) => {
                            if (!errors) {
                                this.currentStep = currentStep
                            }
                        }
                    )
                    return
                }
                // last step
                this.opLoading = true
                this.form.validateFields((errors, values) => {
                    if (!errors) {
                        const form = _.cloneDeep(values)
                        form.fieldsJson = JSON.stringify(this.dataSource)
                        form.contentFieldsJson = JSON.stringify(this.contentDataSource)

                        if (!this.isUpdate) {
                            addSpider(form)
                                .then((data) => {
                                    if (data.success) {
                                        this.$message.success('新建成功')
                                        this.visible = false
                                        this.form.resetFields()
                                        this.dataSource = []
                                        this.index = 0
                                        this.contentDataSource = []
                                        this.contentDataSource = 0
                                        this.currentStep = 0
                                        this.search()
                                    }
                                    this.opLoading = false
                                })
                                .catch((err) => {
                                    console.log(err)
                                    this.opLoading = false
                                })
                        } else {
                            form.id = this.form.id
                            updateSpider(form)
                                .then((data) => {
                                    if (data.success) {
                                        this.$message.success('跟新成功')
                                        this.visible = false
                                        this.form.resetFields()
                                        this.dataSource = []
                                        this.index = 0
                                        this.contentDataSource = []
                                        this.contentIndex = 0
                                        this.currentStep = 0
                                        this.search()
                                    }
                                    this.opLoading = false
                                })
                                .catch((err) => {
                                    console.log(err)
                                    this.opLoading = false
                                })
                        }
                    } else {
                        this.opLoading = false
                    }
                })
            },

            toggleAdvanced() {
                this.advanced = !this.advanced
            },
            handleTableChange(pagination, filters, sorter) {
                const pager = {...this.pagination}
                pager.current = pagination.current
                this.pagination = pager

                this.getList(pagination.current)
            },
            search() {
                this.getList(1)
            },
            getList(pageNo) {
                var self = this
                this.loading = true
                if (pageNo !== undefined) {
                    this.searchModel.pageNo = pageNo
                }

                const searchModel = _.cloneDeep(this.searchModel)

                searchSpider(searchModel).then((data) => {
                    if (data.success) {
                        this.data = data.data
                        const pagination = {...this.pagination}
                        // Read total count from server
                        pagination.total = data.count
                        this.pagination = pagination
                    }
                    this.loading = false
                })
            }
        }
    }
</script>

<style lang="less">
    .proxy-list {
        .proxy {
            position: relative;
            display: inline-block;
            margin-right: 4px;
            margin-bottom: 4px;
            width: 200px;

            .cover {
                display: flex;
                justify-content: center;
                align-items: center;
                color: greenyellow;
                font-size: 32px;
                position: absolute;
                left: 0px;
                top: 0px;
                background: rgba(0, 0, 0, 0.8);
                width: 100%; /*宽度设置为100%，这样才能使隐藏背景层覆盖原页面*/
                height: 100%;
                filter: alpha(opacity=80); /*设置透明度为60%*/
                -moz-opacity: 0.8;
                opacity: 0.8; /*非IE浏览器下设置透明度为60%*/
                z-index: 999;
            }
        }
    }
</style>
