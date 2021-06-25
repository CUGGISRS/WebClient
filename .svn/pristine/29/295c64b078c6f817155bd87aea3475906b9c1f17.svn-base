<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>农产品追溯信息系统</el-breadcrumb-item>
      <el-breadcrumb-item>特色农产品管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 添加区域 -->
      <div>
        <el-button type="danger" size="small" icon="el-icon-delete" @click="handleBatchRemove()">批量删除 </el-button>
        <el-button type="primary" size="small" icon="el-icon-circle-plus" @click="addRow">添加</el-button>
        <el-select
          allow-create
          filterable
          v-model="listQuery.allType"
          size="small"
          placeholder="产品分类"
          clearable
          class="filter-item"
          style="width: 130px;margin-left: 2rem"
        >
          <el-option v-for="item in productOptions" :key="item" :label="item" :value="item" />
        </el-select>
        <el-button
          class="filter-item"
          size="small"
          type="primary"
          style=";margin-left: 0.5rem"
          icon="el-icon-search"
          @click="handleFilter"
        >
          查询
        </el-button>
      </div>
      <!-- 表格显示区域 -->
      <el-table
        v-loading="listLoading"
        :data="rows"
        border
        stripe
        fit
        highlight-current-row
        :row-style="{ height: '5px' }"
        :cell-style="{ padding: '5px 0' }"
        :height="curHeight"
        @selection-change="checkSelect"
      >
        <el-table-column type="selection" width="40" label="全选"></el-table-column>
        <el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (listQuery.page - 1) * listQuery.size + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="产品名称" width="120">
          <template slot-scope="scope">
            <el-link type="primary" :underline="false" @click="editRow(scope.row)">{{ scope.row.name }} </el-link>
          </template>
        </el-table-column>
        <el-table-column align="center" label="产品分类" width="160">
          <template slot-scope="scope">
            {{ scope.row.allType }}
          </template>
        </el-table-column>

        <el-table-column align="center" style="display: inline-grid" label="特色" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.authInfos.length > 0" type="success">
              {{ scope.row.authInfos[0].authName }}
            </el-tag>
            <el-tag v-if="scope.row.authInfos.length > 1" type="success">
              {{ scope.row.authInfos[1].authName }}
            </el-tag>
            <el-tag v-if="scope.row.authInfos.length > 2" type="success">
              {{ scope.row.authInfos[2].authName }}
            </el-tag>
            <el-tag v-if="scope.row.authInfos.length > 3" type="success">
              {{ scope.row.authInfos[3].authName }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column align="center" label="描述" min-width="160">
          <template slot-scope="scope">
            <el-popover placement="top-start" title="描述" width="250" trigger="hover">
              <div>{{ scope.row.description }}</div>
              <span
                slot="reference"
                v-if="scope.row.hasOwnProperty('description') && JSON.stringify(scope.row.description).length > 20"
                >{{ scope.row.description.substr(0, 30) }}...
              </span>
              <span v-else slot="reference">
                {{ scope.row.description }}
              </span>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column align="center" label="产品图片" width="260">
          <template slot-scope="scope">
            <div class="products-img-container">
              <el-image
                :preview-src-list="handleImgUrl(scope.row.imgLists)"
                v-for="url in scope.row.imgLists"
                :key="url.path"
                :src="'http://' + url.path"
                lazy
              ></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="editRow(scope.row)">编辑 </el-button>
            <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteRow(scope.row.id)"
              >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.page"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="listQuery.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>
    <!-- 新增编辑对话框 -->
    <el-dialog
      v-cloak
      title="特色农产品信息"
      @close="closeDialog"
      center
      :visible.sync="dialogShow"
      top="7vh"
      width="70%"
      class="row-form"
    >
      <el-form :model="row" label-width="100px" class="demo-ruleForm" :rules="rules" ref="row">
        <el-tabs v-model="activeName" type="border-card">
          <el-tab-pane label="基本信息" name="basic">
            <el-row>
              <el-col :span="12">
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="产品分类:" prop="allType">
                      <el-select
                        v-model="row.allType"
                        style="width: 100%"
                        @change="getDic"
                        placeholder="请选择产品分类"
                      >
                        <el-option v-for="item in productOptions" :key="item" :label="item" :value="item"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="产品名称:" prop="name">
                      <el-select
                        v-model="row.name"
                        filterable
                        allow-create
                        style="width: 100%"
                        placeholder="请输入产品名称关键词"
                      >
                        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.label">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="描述:" prop="description">
                      <el-input
                        v-model="row.description"
                        clearable
                        type="textarea"
                        :autosize="{ minRows: 16, maxRows: 16 }"
                        placeholder="请输入产品描述"
                      ></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-col>
              <el-col :span="12" :push="1" class="register-upload">
                <el-form-item label="产品图片:">
                  <el-upload
                    :action="updateUrl"
                    :http-request="httpRequest"
                    :before-upload="beforeAvatarUpload"
                    :on-preview="handleProductPreview"
                    :on-remove="handleProductRemove"
                    :on-change="handleProductChange"
                    list-type="picture-card"
                    :file-list="fileProductList"
                  >
                    <i class="el-icon-plus"></i>
                  </el-upload>
                  <!-- 农产品图片预览 -->
                  <el-dialog :visible.sync="dialogProductVisible" width="70%" top="5vh" :append-to-body="true">
                    <img width="100%" :src="imageUrl" alt="" />
                  </el-dialog>

                  <div class="upload-div">
                    <div class="upload-div-list" v-for="(item, index) of imgRemoveLists" :key="index">
                      <div class="upload-div-top">
                        <span class="upload-div-del" @click="fileDelIdPhoto(index, item.id)">X</span>
                      </div>
                      <el-image
                        class="el-image-preview"
                        :src="'http://' + item.path"
                        :preview-src-list="srcList"
                      ></el-image>
                    </div>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="无公害农产品认证" name="PFAP">
            <el-checkbox v-model="isPFAP" style="margin-left: 52px">是无公害农产品</el-checkbox>
            <el-row v-if="isPFAP" style="margin-top: 10px">
              <el-col :span="12">
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="证书编号:" prop="number">
                      <el-input
                        v-model="PFAPAuthRow.number"
                        style="width: 100%"
                        onkeyup="this.value=this.value.replace(/[^\w\.-]/ig,'')"
                        clearable
                        placeholder="请输入三品一标证书编号"
                      >
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="颁证机构:" prop="institution">
                      <el-input v-model="PFAPAuthRow.institution" clearable placeholder="请输入颁证机构"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="认证日期:" prop="authDate">
                      <el-date-picker
                        v-model="PFAPAuthRow.authDate"
                        type="date"
                        style="width: 100%"
                        placeholder="请选择证书认证日期"
                      >
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="过期时间:" prop="pastDate">
                      <el-date-picker
                        v-model="PFAPAuthRow.pastDate"
                        type="date"
                        style="width: 100%"
                        placeholder="请选择证书过期时间"
                      >
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-col>
              <el-col :span="12" :push="1" class="register-upload">
                <el-form-item label="三品一标图片:">
                  <el-upload
                    :action="updateUrl"
                    :http-request="authHttpRequest"
                    :before-upload="beforeAvatarUpload"
                    :on-preview="authHandleProductPreview"
                    :on-remove="authHandleProductRemove"
                    :on-change="authHandleProductChange"
                    list-type="picture-card"
                    :file-list="PFAPFileAuthImgList"
                  >
                    <i class="el-icon-plus"></i>
                  </el-upload>
                  <!-- 三品一标图片预览 -->
                  <el-dialog :visible.sync="dialogAuthProductVisible" width="70%" top="5vh" :append-to-body="true">
                    <img width="100%" :src="authImageUrl" alt="" />
                  </el-dialog>
                  <div class="upload-div">
                    <div class="upload-div-list" v-for="(item, index) of PFAPAuthImgRemoveLists" :key="index">
                      <div class="upload-div-top">
                        <span class="upload-div-del" @click="authfileDelIdPhoto(index, item.fileId)">X</span>
                      </div>
                      <el-image
                        class="el-image-preview"
                        :src="'http://' + item.path"
                        :preview-src-list="PFAPAuthSrcList"
                      ></el-image>
                    </div>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <el-tab-pane label="绿色食品认证" name="GVF">
            <el-checkbox v-model="isGVF" style="margin-left: 52px">是绿色食品</el-checkbox>
            <el-row v-if="isGVF" style="margin-top: 10px">
              <el-col :span="12">
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="证书编号:" prop="number">
                      <el-input
                        v-model="GVFAuthRow.number"
                        style="width: 100%"
                        onkeyup="this.value=this.value.replace(/[^\w\.-]/ig,'')"
                        clearable
                        placeholder="请输入三品一标证书编号"
                      >
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="颁证机构:" prop="institution">
                      <el-input v-model="GVFAuthRow.institution" clearable placeholder="请输入颁证机构"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="认证日期:" prop="authDate">
                      <el-date-picker
                        v-model="GVFAuthRow.authDate"
                        type="date"
                        style="width: 100%"
                        placeholder="请选择证书认证日期"
                      >
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="过期时间:" prop="pastDate">
                      <el-date-picker
                        v-model="GVFAuthRow.pastDate"
                        type="date"
                        style="width: 100%"
                        placeholder="请选择证书过期时间"
                      >
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-col>
              <el-col :span="12" :push="1" class="register-upload">
                <el-form-item label="三品一标图片:">
                  <el-upload
                    :action="updateUrl"
                    :http-request="authHttpRequest"
                    :before-upload="beforeAvatarUpload"
                    :on-preview="authHandleProductPreview"
                    :on-remove="authHandleProductRemove"
                    :on-change="authHandleProductChange"
                    list-type="picture-card"
                    :file-list="GVFFileAuthImgList"
                  >
                    <i class="el-icon-plus"></i>
                  </el-upload>
                  <!-- 三品一标图片预览 -->
                  <el-dialog :visible.sync="dialogAuthProductVisible" width="70%" top="5vh" :append-to-body="true">
                    <img width="100%" :src="authImageUrl" alt="" />
                  </el-dialog>
                  <div class="upload-div">
                    <div class="upload-div-list" v-for="(item, index) of GVFAuthImgRemoveLists" :key="index">
                      <div class="upload-div-top">
                        <span class="upload-div-del" @click="authfileDelIdPhoto(index, item.fileId)">X</span>
                      </div>
                      <el-image
                        class="el-image-preview"
                        :src="'http://' + item.path"
                        :preview-src-list="GVFAuthSrcList"
                      ></el-image>
                    </div>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <el-tab-pane label="有机农产品认证" name="OF">
            <el-checkbox v-model="isOF" style="margin-left: 52px">是有机农产品</el-checkbox>
            <el-row v-if="isOF" style="margin-top: 10px">
              <el-col :span="12">
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="证书编号:" prop="number">
                      <el-input
                        v-model="OFAuthRow.number"
                        style="width: 100%"
                        onkeyup="this.value=this.value.replace(/[^\w\.-]/ig,'')"
                        clearable
                        placeholder="请输入三品一标证书编号"
                      >
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="颁证机构:" prop="institution">
                      <el-input v-model="OFAuthRow.institution" clearable placeholder="请输入颁证机构"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="认证日期:" prop="authDate">
                      <el-date-picker
                        v-model="OFAuthRow.authDate"
                        type="date"
                        style="width: 100%"
                        placeholder="请选择证书认证日期"
                      >
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="过期时间:" prop="pastDate">
                      <el-date-picker
                        v-model="OFAuthRow.pastDate"
                        type="date"
                        style="width: 100%"
                        placeholder="请选择证书过期时间"
                      >
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-col>
              <el-col :span="12" :push="1" class="register-upload">
                <el-form-item label="三品一标图片:">
                  <el-upload
                    :action="updateUrl"
                    :http-request="authHttpRequest"
                    :before-upload="beforeAvatarUpload"
                    :on-preview="authHandleProductPreview"
                    :on-remove="authHandleProductRemove"
                    :on-change="authHandleProductChange"
                    list-type="picture-card"
                    :file-list="OFFileAuthImgList"
                  >
                    <i class="el-icon-plus"></i>
                  </el-upload>
                  <!-- 三品一标图片预览 -->
                  <el-dialog :visible.sync="dialogAuthProductVisible" width="70%" top="5vh" :append-to-body="true">
                    <img width="100%" :src="authImageUrl" alt="" />
                  </el-dialog>
                  <div class="upload-div">
                    <div class="upload-div-list" v-for="(item, index) of OFAuthImgRemoveLists" :key="index">
                      <div class="upload-div-top">
                        <span class="upload-div-del" @click="authfileDelIdPhoto(index, item.fileId)">X</span>
                      </div>
                      <el-image
                        class="el-image-preview"
                        :src="'http://' + item.path"
                        :preview-src-list="OFAuthSrcList"
                      ></el-image>
                    </div>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <el-tab-pane label="农产品地理标志认证" name="GIF">
            <el-checkbox v-model="isGIF" style="margin-left: 52px">是农产品地理标志</el-checkbox>
            <el-row v-if="isGIF" style="margin-top: 10px">
              <el-col :span="12">
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="证书编号:" prop="number">
                      <el-input
                        v-model="GIFAuthRow.number"
                        style="width: 100%"
                        onkeyup="this.value=this.value.replace(/[^\w\.-]/ig,'')"
                        clearable
                        placeholder="请输入三品一标证书编号"
                      >
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="颁证机构:" prop="institution">
                      <el-input v-model="GIFAuthRow.institution" clearable placeholder="请输入颁证机构"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="认证日期:" prop="authDate">
                      <el-date-picker
                        v-model="GIFAuthRow.authDate"
                        type="date"
                        style="width: 100%"
                        placeholder="请选择证书认证日期"
                      >
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="过期时间:" prop="pastDate">
                      <el-date-picker
                        v-model="GIFAuthRow.pastDate"
                        type="date"
                        style="width: 100%"
                        placeholder="请选择证书过期时间"
                      >
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-col>
              <el-col :span="12" :push="1" class="register-upload">
                <el-form-item label="三品一标图片:">
                  <el-upload
                    :action="updateUrl"
                    :http-request="authHttpRequest"
                    :before-upload="beforeAvatarUpload"
                    :on-preview="authHandleProductPreview"
                    :on-remove="authHandleProductRemove"
                    :on-change="authHandleProductChange"
                    list-type="picture-card"
                    :file-list="GIFFileAuthImgList"
                  >
                    <i class="el-icon-plus"></i>
                  </el-upload>
                  <!-- 三品一标图片预览 -->
                  <el-dialog :visible.sync="dialogAuthProductVisible" width="70%" top="5vh" :append-to-body="true">
                    <img width="100%" :src="authImageUrl" alt="" />
                  </el-dialog>
                  <div class="upload-div">
                    <div class="upload-div-list" v-for="(item, index) of GIFAuthImgRemoveLists" :key="index">
                      <div class="upload-div-top">
                        <span class="upload-div-del" @click="authfileDelIdPhoto(index, item.fileId)">X</span>
                      </div>
                      <el-image
                        class="el-image-preview"
                        :src="'http://' + item.path"
                        :preview-src-list="GIFAuthSrcList"
                      ></el-image>
                    </div>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
        <span class="t-btn">
          <el-button type="info" @click="dialogShow = false">取 消</el-button>
          <el-button type="success" @click="submitRow()">提 交</el-button>
        </span>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import userService from '@/globals/service/user.js';

export default {
  data() {
    return {
      //提交状态
      flag: 'add',
      productList: [],
      options: [],
      // 搜索条件
      listQuery: {
        page: 1,
        size: 10,
      },
      // 总数
      total: 0,
      // 表单高度
      curHeight: 0,
      //删除ids数组
      ids: [],
      // 对话框
      dialogShow: false,
      listLoading: false,
      // 表单
      rows: [],
      row: {
        id: '', //农产品的id
        name: '', // 产品名称
        allType: '', // 产品分类
        description: '', //描述
        imgLists: [] //图片列表
      },
      PFAPAuthRow: {
        authId: '',
        authName: '', // 三品一标名称
        number: '', // 认证编号
        authDate: '', //认证日期
        pastDate: '', //过期日期
        institution: '', //认证机构
        authImgLists: [] // 证书图片
      },
      GVFAuthRow: {
        authId: '',
        authName: '', // 三品一标名称
        number: '', // 认证编号
        authDate: '', //认证日期
        pastDate: '', //过期日期
        institution: '', //认证机构
        authImgLists: [] // 证书图片
      },
      OFAuthRow: {
        authId: '',
        authName: '', // 三品一标名称
        number: '', // 认证编号
        authDate: '', //认证日期
        pastDate: '', //过期日期
        institution: '', //认证机构
        authImgLists: [] // 证书图片
      },
      GIFAuthRow: {
        authId: '',
        authName: '', // 三品一标名称
        number: '', // 认证编号
        authDate: '', //认证日期
        pastDate: '', //过期日期
        institution: '', //认证机构
        authImgLists: [] // 证书图片
      },
      activeName: 'basic',
      isPFAP: false, // 无公害农产品
      isGVF: false, // 绿色食品
      isOF: false, //有机农产品
      isGIF: false, // 农产品地理标志
      //编辑删除图片id的数组
      deleteIds: [],
      PFAPdeleteIds: [], // 无公害农产品
      GVFdeleteIds: [], // 绿色食品
      OFdeleteIds: [], // 有机农产品
      GIFdeleteIds: [], // 农产品地理标志
      // 预览图片src列表
      srcList: [],
      PFAPAuthSrcList: [], // 无公害农产品
      GVFAuthSrcList: [], // 绿色食品
      OFAuthSrcList: [], // 有机农产品
      GIFAuthSrcList: [], // 农产品地理标志
      dialogProductVisible: false,
      dialogAuthProductVisible: false,
      productRemoveListPhoto: [],
      PFAPAuthProductRemoveListPhoto: [], // 无公害农产品
      GVFAuthProductRemoveListPhoto: [], // 绿色食品
      OFAuthProductRemoveListPhoto: [], // 有机农产品
      GIFAuthProductRemoveListPhoto: [], // 农产品地理标志
      imgRemoveLists: [],
      PFAPAuthImgRemoveLists: [], // 无公害农产品
      GVFAuthImgRemoveLists: [], // 绿色食品
      OFAuthImgRemoveLists: [], // 有机农产品
      GIFAuthImgRemoveLists: [], // 农产品地理标志
      //上传图片文件列表
      fileProductList: [],
      // 三品一标认证图片
      PFAPFileAuthImgList: [], // 无公害农产品
      GVFFileAuthImgList: [], // 绿色食品
      OFFileAuthImgList: [], // 有机农产品
      GIFFileAuthImgList: [], // 农产品地理标志
      // 图片文件上传
      updateUrl: '#',
      imageUrl: '',
      authImageUrl: '',
      // 多选框
      productOptions: ['粮油', '蔬菜', '水果','酒','粮食作物', '牛羊猪肉', '家禽蛋类', '水产品', '其他'],
      authNameOptions: ['无', '无公害农产品', '绿色食品', '有机农产品', '农产品地理标志'],
      // 验证规则
      rules: {
        allType: [{ required: true, message: '请选择产品分类', trigger: 'blur' }],
        name: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
        description: [{ required: true, message: '请输入产品描述', trigger: 'blur' }]
      }
    };
  },
  created() {
    this.getAllList();
    this.setTableHeight();
    window.onresize = () => {
      this.setTableHeight();
    };
  },
  methods: {
    // 获取全部的列表数据
    getAllList() {
      this.listLoading = true;
      let params = this.listQuery;
      userService.featureProductsList(params).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.rows = res.data.rows;

        this.listLoading = false;
        this.total = res.data.total;
      });
    },
    //图片预览url处理
    handleImgUrl(data) {
      let imgList = [];
      data.map(item => {
        imgList.push('http://' + item.path);
      });

      return imgList;
    },
    // 查询
    handleFilter() {
      this.listQuery.page = 1;
      this.getAllList();
    },
    // 当前页
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.getAllList();
    },
    // 每页多少条
    handleSizeChange(val) {
      this.listQuery.size = val;
      this.getAllList();
    },
    //全选框事件
    checkSelect(data) {
      data.forEach(item => {
        this.ids.push(item.id);
      });
    },
    async handleBatchRemove() {
      if (this.ids.length === 0) return this.$message.warning('请先选中要删除的特色农产品信息');
      const confirmResult = await this.$confirm('此操作将删除选中特色农产品信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.featureProductDel(this.ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除特色农产品失败');
        this.$message.success('删除特色农产品成功');
        this.getAllList();
      });
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 280;
    },
    //农产品图片预览对话框
    handleProductPreview(file) {
      this.imageUrl = file.url;
      this.dialogProductVisible = true;
    },
    // 三品一标图片预览对话框
    authHandleProductPreview(file) {
      this.authImageUrl = file.url;
      this.dialogAuthProductVisible = true;
    },
    //删除要上传列表中的图片
    handleProductRemove(file, fileLists) {
      for (let i in this.productRemoveListPhoto) {
        if (this.productRemoveListPhoto[i].key == file.uid) {
          this.productRemoveListPhoto.splice(i, 1);
        }
      }
      this.fileProductList = fileLists;
    },
    //删除要上传列表中的三品一标图片
    authHandleProductRemove(file, fileLists) {
      if (this.activeName === 'PFAP') {
        for (let i in this.PFAPAuthProductRemoveListPhoto) {
          if (this.PFAPAuthProductRemoveListPhoto[i].key === file.uid) {
            this.PFAPAuthProductRemoveListPhoto.splice(i, 1);
          }
        }
        this.PFAPFileAuthImgList = fileLists;
      }
      if (this.activeName === 'GVF') {
        for (let i in this.GVFAuthProductRemoveListPhoto) {
          if (this.GVFAuthProductRemoveListPhoto[i].key === file.uid) {
            this.GVFAuthProductRemoveListPhoto.splice(i, 1);
          }
        }
        this.GVFFileAuthImgList = fileLists;
      }
      if (this.activeName === 'OF') {
        for (let i in this.OFAuthProductRemoveListPhoto) {
          if (this.OFAuthProductRemoveListPhoto[i].key === file.uid) {
            this.OFAuthProductRemoveListPhoto.splice(i, 1);
          }
        }
        this.OFFileAuthImgList = fileLists;
      }
      if (this.activeName === 'GIF') {
        for (let i in this.GIFAuthProductRemoveListPhoto) {
          if (this.GIFAuthProductRemoveListPhoto[i].key === file.uid) {
            this.GIFAuthProductRemoveListPhoto.splice(i, 1);
          }
        }
        this.GIFFileAuthImgList = fileLists;
      }
    },
    //上传图片列表文件
    handleProductChange(file, fileList) {
      this.fileProductList = fileList;
    },
    authHandleProductChange(file, fileList) {
      if (this.activeName === 'PFAP') {
        this.PFAPFileAuthImgList = fileList;
      } else if (this.activeName === 'GVF') {
        this.GVFFileAuthImgList = fileList;
      } else if (this.activeName === 'OF') {
        this.OFFileAuthImgList = fileList;
      } else if (this.activeName === 'GIF') {
        this.GIFFileAuthImgList = fileList;
      }
    },
    getDic() {
      let params = {
        type: this.row.allType
      };
      userService.getProductNameDicByType(params).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.productList = res.data.rows;

        this.options = this.productList.map(item => {
          return { value: `${item.text}`, label: `${item.text}` };
        });

        this.row.name = this.options[0].value;
      });
    },
    //删除已上传的图片
    fileDelIdPhoto(index, id) {
      this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.deleteIds.push(id);
          this.imgRemoveLists.splice(index, 1);
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作'
          });
        });
    },
    authfileDelIdPhoto(index, id) {
      this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          if (this.isPFAP) {
            this.PFAPdeleteIds.push(id);
            this.PFAPAuthImgRemoveLists.splice(index, 1);
          } else if (this.isGVF) {
            this.GVFdeleteIds.push(id);
            this.GVFAuthImgRemoveLists.splice(index, 1);
          } else if (this.isOF) {
            this.OFdeleteIds.push(id);
            this.OFAuthImgRemoveLists.splice(index, 1);
          } else if (this.GIF) {
            this.GIFdeleteIds.push(id);
            this.GIFAuthImgRemoveLists.splice(index, 1);
          }
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作'
          });
        });
    },
    // 处理图片
    httpRequest(data) {
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        this.imageUrl = this.result; // this指向当前方法onloadend的作用域
      };
    },
    authHttpRequest(data) {
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        this.authImageUrl = this.result; // this指向当前方法onloadend的作用域
      };
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isPng = file.type === 'image/png';
      if (!isJPG && !isPng) {
        this.$message.error('上传图片格式只能为.jpg或.png!');
      }
      return isJPG || isPng;
    },
    // 添加
    addRow() {
      this.activeName = 'basic';
      this.flag = 'add';
      this.row.allType = this.productOptions[0];
      this.getDic();
      this.isPFAP = false;
      this.isOF = false;
      this.isGVF = false;
      this.isGIF = false;
      this.dialogShow = true;
    },
    // 编辑
    editRow(row) {
      this.activeName = 'basic';
      this.flag = 'edit';
      this.row = row;
      this.isPFAP = false;
      this.isGVF = false;
      this.isOF = false;
      this.isGIF = false;

      if (row.authInfos.length > 0) {
        row.authInfos.forEach(item => {
          if (item.authName === '无公害农产品') {
            this.isPFAP = true;
            userService.getAuthInfoById(item.id).then(res => {
              if (res.status === 200) {
                this.PFAPAuthRow = res.data;
                this.PFAPAuthImgRemoveLists = res.data.fileLists;
                res.data.fileLists.map(item => {
                  this.PFAPAuthSrcList.push('http://' + item.path);
                });
              }
            });
          } else if (item.authName === '绿色食品') {
            this.isGVF = true;
            userService.getAuthInfoById(item.id).then(res => {
              if (res.status === 200) {
                this.GVFAuthRow = res.data;
                this.GVFAuthImgRemoveLists = res.data.fileLists;
                res.data.fileLists.map(item => {
                  this.GVFAuthSrcList.push('http://' + item.path);
                });
              }
            });
          } else if (item.authName === '农产品地理标志') {
            this.isGIF = true;
            userService.getAuthInfoById(item.id).then(res => {
              if (res.status === 200) {
                this.GIFAuthRow = res.data;
                this.GIFAuthImgRemoveLists = res.data.fileLists;
                res.data.fileLists.map(item => {
                  this.GIFAuthSrcList.push('http://' + item.path);
                });
              }
            });
          } else if (item.authName === '有机农产品') {
            this.isOF = true;
            userService.getAuthInfoById(item.id).then(res => {
              if (res.status === 200) {
                this.OFAuthRow = res.data;
                this.OFAuthImgRemoveLists = res.data.fileLists;
                res.data.fileLists.map(item => {
                  this.OFAuthSrcList.push('http://' + item.path);
                });
              }
            });
          }
        });
      }
      this.imgRemoveLists = row.imgLists;
      row.imgLists.map(item => {
        this.srcList.push('http://' + item.path);
      });
      this.dialogShow = true;
    },
    // 删除
    async deleteRow(ids) {
      const confirmResult = await this.$confirm('此操作将删除该特色农产品,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.featureProductDel(ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除特色农产品失败');
        this.$message.success('删除特色农产品成功');
        this.getAllList();
      });
    },
    // 关闭对话框
    closeDialog() {
      this.row = {};
      this.PFAPAuthRow.authId = '';
      this.GVFAuthRow.authId = '';
      this.OFAuthRow.authId = '';
      this.GIFAuthRow.authId = '';
      this.isPFAP = false;
      this.isGVF = false;
      this.isOF = false;
      this.isGIF = false;
      this.fileProductList = [];
      this.imgRemoveLists = [];
      this.deleteIds = [];
      this.$refs.row.clearValidate();
      this.rows = [];
      this.getAllList();
    },
    // 新增 无公害信息
    addPFAPAuthInfo(productId) {
      let PFAPForm = new FormData();
      PFAPForm.append('productId', productId);
      PFAPForm.append('authName', '无公害农产品'); // 认证名称
      PFAPForm.append('number', this.PFAPAuthRow.number); // 认证编号
      PFAPForm.append('institution', this.PFAPAuthRow.institution); // 认证机构
      if (this.PFAPAuthRow.authDate !== undefined || this.PFAPAuthRow.authDate !== '') {
        this.PFAPAuthRow.authDate = this.$moment(new Date(this.PFAPAuthRow.authDate)).format('YYYY-MM-DD');
        this.PFAPAuthRow.pastDate = this.$moment(new Date(this.PFAPAuthRow.pastDate)).format('YYYY-MM-DD');
        PFAPForm.append('authDate', this.PFAPAuthRow.authDate); // 认证日期
        PFAPForm.append('pastDate', this.PFAPAuthRow.pastDate); // 过期日期
      } else {
        PFAPForm.append('authDate', ''); // 认证日期
        PFAPForm.append('pastDate', ''); // 过期日期
      }
      this.PFAPFileAuthImgList.forEach(item => {
        PFAPForm.append('files', item.raw);
      });
      userService.authInfoAdd(PFAPForm).then(res => {
        if (res.status === 200) this.$message.success('无公害信息新增成功');
        this.getAllList();
      });
    },
    // 更新 无公害信息
    updatePFAPAuthInfo(productId) {
      let PFAPForm = new FormData();
      PFAPForm.append('id', this.PFAPAuthRow.authId);
      PFAPForm.append('productId', productId);
      PFAPForm.append('authName', this.PFAPAuthRow.authName); // 认证名称
      PFAPForm.append('number', this.PFAPAuthRow.number); // 认证编号
      PFAPForm.append('institution', this.PFAPAuthRow.institution); // 认证机构
      if (this.PFAPAuthRow.authDate !== undefined || this.PFAPAuthRow.authDate !== '') {
        this.PFAPAuthRow.authDate = this.$moment(new Date(this.PFAPAuthRow.authDate)).format('YYYY-MM-DD');
        this.PFAPAuthRow.pastDate = this.$moment(new Date(this.PFAPAuthRow.pastDate)).format('YYYY-MM-DD');
        PFAPForm.append('authDate', this.PFAPAuthRow.authDate); // 认证日期
        PFAPForm.append('pastDate', this.PFAPAuthRow.pastDate); // 过期日期
      } else {
        PFAPForm.append('authDate', ''); // 认证日期
        PFAPForm.append('pastDate', ''); // 过期日期
      }

      this.PFAPFileAuthImgList.forEach(item => {
        PFAPForm.append('files', item.raw);
      });
      // delIds
      this.PFAPdeleteIds.map(item => {
        PFAPForm.append('delIds', item);
      });
      userService.authInfoEdit(PFAPForm).then(res => {
        if (res.status !== 200) this.$message.success('无公害信息更新失败');
        this.getAllList();
      });
    },
    // 删除 无公害信息
    delPFAPAuthInfo() {
      userService.authInfoDel(this.PFAPAuthRow.authId).then(res => {
        if (res.status !== 200) this.$message.success('无公害信息删除失败');
        this.getAllList();
      });
    },
    // 新增绿色食品信息
    addGVFPAuthInfo(productId) {
      let GVFPForm = new FormData();
      GVFPForm.append('productId', productId);
      GVFPForm.append('authName', '绿色食品'); // 认证名称
      GVFPForm.append('number', this.GVFAuthRow.number); // 认证编号
      GVFPForm.append('institution', this.GVFAuthRow.institution); // 认证机构
      this.GVFAuthRow.authDate = this.$moment(new Date(this.GVFAuthRow.authDate)).format('YYYY-MM-DD');
      this.GVFAuthRow.pastDate = this.$moment(new Date(this.GVFAuthRow.pastDate)).format('YYYY-MM-DD');
      GVFPForm.append('authDate', this.GVFAuthRow.authDate); // 认证日期
      GVFPForm.append('pastDate', this.GVFAuthRow.pastDate); // 过期日期
      this.GVFFileAuthImgList.forEach(item => {
        GVFPForm.append('files', item.raw);
      });
      userService.authInfoAdd(GVFPForm).then(res => {
        if (res.status === 200) this.$message.success('绿色食品信息新增成功');
        this.getAllList();
      });
    },
    // 更新 绿色食品信息
    updateGVFPAuthInfo(productId) {
      let GVFPForm = new FormData();
      GVFPForm.append('id', this.GVFAuthRow.authId);
      GVFPForm.append('productId', productId);
      GVFPForm.append('authName', this.GVFAuthRow.authName); // 认证名称
      GVFPForm.append('number', this.GVFAuthRow.number); // 认证编号
      GVFPForm.append('institution', this.GVFAuthRow.institution); // 认证机构
      this.GVFAuthRow.authDate = this.$moment(new Date(this.GVFAuthRow.authDate)).format('YYYY-MM-DD');
      this.GVFAuthRow.pastDate = this.$moment(new Date(this.GVFAuthRow.pastDate)).format('YYYY-MM-DD');
      GVFPForm.append('authDate', this.GVFAuthRow.authDate); // 认证日期
      GVFPForm.append('pastDate', this.GVFAuthRow.pastDate); // 过期日期
      this.GVFFileAuthImgList.forEach(item => {
        GVFPForm.append('files', item.raw);
      });
      userService.authInfoEdit(GVFPForm).then(res => {
        if (res.status !== 200) this.$message.success('绿色食品信息更新失败');
        this.getAllList();
      });
    },
    // 删除 绿色食品信息
    delGVFPAuthInfo() {
      userService.authInfoDel(this.GVFAuthRow.authId).then(res => {
        if (res.status !== 200) this.$message.success('绿色食品信息删除失败');
        this.getAllList();
      });
    },
    // 新增有机农产品信息
    addOFAuthInfo(productId) {
      let OFPForm = new FormData();
      OFPForm.append('productId', productId);
      OFPForm.append('authName', '有机农产品'); // 认证名称
      OFPForm.append('number', this.OFAuthRow.number); // 认证编号
      OFPForm.append('institution', this.OFAuthRow.institution); // 认证机构
      this.OFAuthRow.authDate = this.$moment(new Date(this.OFAuthRow.authDate)).format('YYYY-MM-DD');
      this.OFAuthRow.pastDate = this.$moment(new Date(this.OFAuthRow.pastDate)).format('YYYY-MM-DD');
      OFPForm.append('authDate', this.OFAuthRow.authDate); // 认证日期
      OFPForm.append('pastDate', this.OFAuthRow.pastDate); // 过期日期
      this.OFFileAuthImgList.forEach(item => {
        OFPForm.append('files', item.raw);
      });
      userService.authInfoAdd(OFPForm).then(res => {
        if (res.status === 200) this.$message.success('有机农产品信息新增成功');
        this.getAllList();
      });
    },
    //  更新 有机农产品信息
    updateOFAuthInfo(productId) {
      let OFPForm = new FormData();
      OFPForm.append('id', this.OFAuthRow.authId);
      OFPForm.append('productId', productId);
      OFPForm.append('authName', this.OFAuthRow.authName); // 认证名称
      OFPForm.append('number', this.OFAuthRow.number); // 认证编号
      OFPForm.append('institution', this.OFAuthRow.institution); // 认证机构
      this.OFAuthRow.authDate = this.$moment(new Date(this.OFAuthRow.authDate)).format('YYYY-MM-DD');
      this.OFAuthRow.pastDate = this.$moment(new Date(this.OFAuthRow.pastDate)).format('YYYY-MM-DD');
      OFPForm.append('authDate', this.OFAuthRow.authDate); // 认证日期
      OFPForm.append('pastDate', this.OFAuthRow.pastDate); // 过期日期
      this.OFFileAuthImgList.forEach(item => {
        OFPForm.append('files', item.raw);
      });
      userService.authInfoEdit(OFPForm).then(res => {
        if (res.status !== 200) this.$message.success('有机农产品信息更新失败');
        this.getAllList();
      });
    },
    // 删除  有机农产品信息
    delOFAuthInfo() {
      userService.authInfoDel(this.OFAuthRow.authId).then(res => {
        if (res.status !== 200) this.$message.success('有机农产品信息删除失败');
        this.getAllList();
      });
    },
    // 新增农产品地理标志
    addGIFAuthInfo(productId) {
      let GIFForm = new FormData();
      GIFForm.append('productId', productId);
      GIFForm.append('authName', '农产品地理标志'); // 认证名称
      GIFForm.append('number', this.GIFAuthRow.number); // 认证编号
      GIFForm.append('institution', this.GIFAuthRow.institution); // 认证机构
      this.GIFAuthRow.authDate = this.$moment(new Date(this.GIFAuthRow.authDate)).format('YYYY-MM-DD');
      this.GIFAuthRow.pastDate = this.$moment(new Date(this.GIFAuthRow.pastDate)).format('YYYY-MM-DD');
      GIFForm.append('authDate', this.GIFAuthRow.authDate); // 认证日期
      GIFForm.append('pastDate', this.GIFAuthRow.pastDate); // 过期日期
      this.GIFFileAuthImgList.forEach(item => {
        GIFForm.append('files', item.raw);
      });
      userService.authInfoAdd(GIFForm).then(res => {
        if (res.status === 200) this.$message.success('农产品地理标志信息新增成功');
        this.getAllList();
      });
    },
    // 更新 农产品地理标志
    updateGIFAuthInfo(productId) {
      let GIFForm = new FormData();
      GIFForm.append('id', this.GIFAuthRow.authId);
      GIFForm.append('productId', productId);
      GIFForm.append('authName', this.GIFAuthRow.authName); // 认证名称
      GIFForm.append('number', this.GIFAuthRow.number); // 认证编号
      GIFForm.append('institution', this.GIFAuthRow.institution); // 认证机构
      this.GIFAuthRow.authDate = this.$moment(new Date(this.GIFAuthRow.authDate)).format('YYYY-MM-DD');
      this.GIFAuthRow.pastDate = this.$moment(new Date(this.GIFAuthRow.pastDate)).format('YYYY-MM-DD');
      GIFForm.append('authDate', this.GIFAuthRow.authDate); // 认证日期
      GIFForm.append('pastDate', this.GIFAuthRow.pastDate); // 过期日期
      this.GIFFileAuthImgList.forEach(item => {
        GIFForm.append('files', item.raw);
      });
      userService.authInfoEdit(GIFForm).then(res => {
        if (res.status !== 200) this.$message.success('农产品地理标志信息更新失败');
        this.getAllList();
      });
    },
    // 删除 农产品地理标志
    delGIFAuthInfo() {
      userService.authInfoDel(this.GIFAuthRow.authId).then(res => {
        if (res.status !== 200) this.$message.success('农产品地理标志信息删除失败');
        this.getAllList();
      });
    },
    // 提交数据
    submitRow() {
      this.$refs.row.validate(valid => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        if (this.flag === 'add') {
          let formData = new FormData();
          formData.append('name', this.row.name);
          formData.append('allType', this.row.allType);
          formData.append('description', this.row.description);
          this.fileProductList.forEach(item => {
            formData.append('files', item.raw);
          });
          userService
            .featureProductAdd(formData)
            .then(res => {
              if (res.status !== 200) return this.$message.error('失败');
              // this.$message.success('新增成功');
              // 特色农产品新增成功后，提交三品一标信息。
              let productId = res.data; // 提交成功后获取特色农产品id
              if (this.isPFAP) {
                this.addPFAPAuthInfo(productId); // 需要提交无公害农产品信息
              }
              if (this.isGVF) {
                this.addGVFPAuthInfo(productId); // 需要提交绿色食品
              }
              if (this.isOF) {
                this.addOFAuthInfo(productId); // 需要提交有机农产品
              }
              if (this.isGIF) {
                this.addGIFAuthInfo(productId); // 需要提交 农产品地理标志
              }
              this.dialogShow = false;
              this.getAllList();
            })
            .catch(() => {
              this.$message({ type: 'error', message: '异常' });
            });
        } else if (this.flag === 'edit') {
          let formData = new FormData();
          formData.append('id', this.row.id);
          formData.append('name', this.row.name);
          formData.append('allType', this.row.allType);
          formData.append('description', this.row.description);
          this.deleteIds.map(item => {
            formData.append('delIds', item);
          });
          this.fileProductList.forEach(item => {
            formData.append('files', item.raw);
          });
          userService
            .featureProductEdit(formData)
            .then(res => {
              if (res.status !== 200) return this.$message.error('失败');
              // this.$message.success('更新成功');
              let productId = this.row.id; // 特色农产品id
              if (this.isPFAP) {
                // 如果 authId存在，则更新，否则新增
                if (this.PFAPAuthRow.authId !== undefined && this.PFAPAuthRow.authId !== '') {
                  this.updatePFAPAuthInfo(productId); // 需要提交无公害农产品信息
                } else {
                  this.addPFAPAuthInfo(productId);
                }
              } else if (this.PFAPAuthRow.authId !== undefined && this.PFAPAuthRow.authId !== '') {
                this.delPFAPAuthInfo(); // 删除 无公害农产品认证信息
              }
              if (this.isGVF) {
                if (this.GVFAuthRow.authId !== undefined && this.GVFAuthRow.authId !== '') {
                  this.updateGVFPAuthInfo(productId); // 需要提交绿色食品
                } else {
                  this.addGVFPAuthInfo(productId);
                }
              } else if (this.GVFAuthRow.authId !== undefined && this.GVFAuthRow.authId !== '') {
                this.delGVFPAuthInfo(); // 删除 绿色食品 认证信息
              }
              if (this.isOF) {
                if (this.OFAuthRow.authId !== undefined && this.OFAuthRow.authId !== '') {
                  this.updateOFAuthInfo(productId); // 需要提交有机农产品
                } else {
                  this.addOFAuthInfo(productId);
                }
              } else if (this.OFAuthRow.authId !== undefined && this.OFAuthRow.authId !== '') {
                this.delOFAuthInfo(); // 删除 有机农产品 认证信息
              }
              if (this.isGIF) {
                if (this.GIFAuthRow.authId !== undefined && this.GIFAuthRow.authId !== '') {
                  this.updateGIFAuthInfo(productId); // 需要提交 农产品地理标志
                } else {
                  this.addGIFAuthInfo(productId);
                }
              } else if (this.GIFAuthRow.authId !== undefined && this.GIFAuthRow.authId !== '') {
                this.delGIFAuthInfo(); // 删除 农产品地理标志 认证信息
              }
              this.isPFAP = false;
              this.isGVF = false;
              this.isOF = false;
              this.isGIF = false;
              this.dialogShow = false;
              this.getAllList();
            })
            .catch(() => {
              this.$message({ type: 'error', message: '异常' });
            });
        }
      });
    }
  }
};
</script>

<style lang="less" scoped>
.upload-div {
  width: 350px;
  display: flex;
  justify-content: start;
  align-items: center;
  flex-wrap: wrap;
}

.upload-div-del {
  cursor: pointer;
}

.upload-div-del:hover {
  color: #64b7ff;
}

.link-type,
.link-type:focus {
  color: #337ab7;
  cursor: pointer;

  & :hover {
    color: rgb(32, 160, 255);
  }
}

.products-img-container {
  height: 100px;
  padding: 10px;
  overflow: auto;
}

.el-image-preview {
  width: 100px;
  height: 100px;
  margin-right: 10px;
}
</style>
