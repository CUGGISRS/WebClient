<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/welcome' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>数据管理</el-breadcrumb-item>
      <el-breadcrumb-item>数据配置</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="data-list-card" :style="curHeight">
      <div class="el-transfer-panel file-tree">
        <p class="el-transfer-panel__header">
          <label class="el-checkbox">
            <span class="el-checkbox__label">数据列表</span>
            <el-button
                type="primary"
                size="mini"
                plain
                @click="handleAddDataDialogShow"
            >新增数据
            </el-button
            >
          </label>
        </p>
        <div class="el-transfer-panel__body">
          <div class="el-transfer-panel__body_overflow">
            <label
                class="el-checkbox el-transfer-panel__item"
                v-for="(item, index) in dataList"
                :key="index"
            >
              <el-radio
                  v-model="radio"
                  :label="index"
                  @change="handleChangeDataList(item.name)"
              >{{ item.name }}
              </el-radio
              >
              <i @click="deleteNode" class="el-icon-error"></i>
            </label>
          </div>
        </div>
      </div>
      <div class="el-transfer-panel data-tree">
        <p class="el-transfer-panel__header">
          <label class="el-checkbox">
            <span class="el-checkbox__label">数据目录</span>

            <div style="display: inline-block;float: right">
              <el-button type="primary" size="mini" plain icon="el-icon-top" @click="moveNode(-1)"></el-button>
              <el-button type="primary" size="mini" plain icon="el-icon-bottom" @click="moveNode(1)"></el-button>
            </div>

          </label>
        </p>
        <div class="el-transfer-panel__body">
          <div class="el-transfer-panel__body_overflow">
            <el-tree
                :data="data"
                ref="tree"
                node-key="id"
                default-expand-all
                :expand-on-click-node="false"
                show-checkbox
                @check-change="getTreeData"
                @node-click="nodeClick"
                check-strictly
                highlight-current
            >
              <span class="custom-tree-node" slot-scope="{ data }">
                <span class="item-text">{{ data.title }}</span>
                <span class="item-btn">
                  <el-button
                      type="text"
                      size="mini"
                      @click.stop="() => addNode(data)"
                      v-if="data.root || data.isRoot"
                  >
                    增加数据
                  </el-button>
                  <el-button
                      type="text"
                      size="mini"
                      @click.stop="() => editNode(data)"
                  >
                    修改
                  </el-button>
                  <el-button
                      type="text"
                      size="mini"
                      @click.stop="() => removeNode(data)"
                  >
                    删除
                  </el-button>
                </span>
              </span>
            </el-tree>
          </div>
        </div>
      </div>
      <div class="el-transfer-panel data-content">
        <p class="el-transfer-panel__header">
          <label class="el-checkbox">
            <span class="el-checkbox__label">数据内容</span>
            <el-button
                type="primary"
                size="mini"
                plain
                @click="addRootNode"
                v-if="addRootBtn"
            >添加目录
            </el-button
            >
          </label>
        </p>
        <el-dialog
            title="请选择链接地址"
            :visible.sync="fileDialogVisible"
            width="50%"
        >
          <el-input
              v-model="fileUrl"
              readonly
          ></el-input>
          <br/>
          <el-select
              v-model="LocalAgentId"
              @change="changeLA" filterable
              placeholder="请选择对象"
          >
            <el-option
                v-for="item in LocalAgents"
                :key="item.id"
                :label="item.title"
                :value="item.id"
            >
            </el-option>
          </el-select>

          <el-select
              v-model="url"
              @change="tt" filterable
              placeholder="请选择文件或文件夹"
          >
            <el-option
                v-for="item in urls"
                :key="item.name"
                :label="item.name"
                :value="item.name"
            >
            </el-option>
          </el-select>
          <el-button @click="tc">确定</el-button>
          <el-button @click="ts">上一级</el-button>


        </el-dialog>

        <div class="el-transfer-panel__body_content">
          <el-form
              :model="treeNodeData"
              ref="treeNodeData"
              style="width: 450px;margin-top:20px;"
              label-position="left"
              label-width="100px"
              v-if="showTreeData === 'show' || showTreeData ==='addShow'"
          >
            <el-form-item label="标题">
              <el-input
                  v-model="treeNodeData.title"
                  :disabled="!editNodeShow"
              ></el-input>
            </el-form-item>
            <el-form-item label="是否展开" v-show="treeNodeData.root || treeNodeData.isRoot">
              <el-switch
                  v-model="treeNodeData.expand"
                  :disabled="!editNodeShow"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
              >
              </el-switch>
            </el-form-item>
            <el-form-item label="类型" v-show="!(treeNodeData.root || treeNodeData.isRoot)">
              <el-select
                  v-model="treeNodeData.type"
                  placeholder="请选择"
                  :disabled="!editNodeShow"
              >
                <el-option
                    v-for="item in typeOptions"
                    :key="item.label"
                    :label="item.label"
                    :value="item.label"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="链接地址" v-show="!(treeNodeData.root || treeNodeData.isRoot)">
              <el-input
                  v-model="treeNodeData.url"
                  :disabled="!editNodeShow"
              ></el-input>
              <el-button type="info" :disabled="!editNodeShow" @click="selectPath(treeNodeData.url)">选择</el-button>
            </el-form-item>
            <el-form-item label="中心点"
                          v-show="isTypes(treeNodeData.type, [lt.gltf])">
              <el-row>
                <el-col :span="10">
                  <el-input placeholder="经度"
                            width="40"
                            v-model="treeNodeData.lon"
                            :disabled="!editNodeShow"
                  ></el-input>
                </el-col>
                <el-col :span="4" style="text-align: center">
                  <span>—</span>
                </el-col>
                <el-col :span="10">
                  <el-input placeholder="纬度"
                            v-model="treeNodeData.lat"
                            :disabled="!editNodeShow"
                  ></el-input>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item label="负载子域" v-show="treeNodeData.url && treeNodeData.url.indexOf('{s}') > -1">
              <el-input
                  v-model="treeNodeData.subdomains"
                  :disabled="!editNodeShow"
              ></el-input>
            </el-form-item>
            <el-form-item label="高程偏移"
                          v-show="isTypes(treeNodeData.type, [lt.tiles_3d])">
              <el-input placeholder="0"
                        v-model="treeNodeData.offsetHeight"
                        :disabled="!editNodeShow"
              ></el-input>
            </el-form-item>
            <el-form-item label="最小级别"
                          v-show="isTypes(treeNodeData.type, [lt.dom, lt.wmts_xyz])">
              <el-input placeholder="0"
                        v-model="treeNodeData.minLevel"
                        :disabled="!editNodeShow"
              ></el-input>
            </el-form-item>
            <el-form-item label="最大级别"
                          v-show="isTypes(treeNodeData.type, [lt.dom, lt.wmts_xyz])">
              <el-input placeholder="23"
                        v-model="treeNodeData.maxLevel"
                        :disabled="!editNodeShow"
              ></el-input>
            </el-form-item>
            <el-form-item label="经度范围"
                          v-show="isTypes(treeNodeData.type, [lt.dom])">
              <el-row>
                <el-col :span="10">
                  <el-input placeholder="-180"
                            width="40"
                            v-model="treeNodeData.west"
                            :disabled="!editNodeShow"
                  ></el-input>
                </el-col>
                <el-col :span="4" style="text-align: center">
                  <span>—</span>
                </el-col>
                <el-col :span="10">
                  <el-input placeholder="180"
                            v-model="treeNodeData.east"
                            :disabled="!editNodeShow"
                  ></el-input>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item label="纬度范围"
                          v-show="isTypes(treeNodeData.type, [lt.dom])">
              <el-row>
                <el-col :span="10">
                  <el-input placeholder="-90"
                            v-model="treeNodeData.south"
                            :disabled="!editNodeShow"
                  ></el-input>
                </el-col>
                <el-col :span="4" style="text-align: center">
                  <span>—</span>
                </el-col>
                <el-col :span="10">
                  <el-input placeholder="90"
                            v-model="treeNodeData.north"
                            :disabled="!editNodeShow"
                  ></el-input>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item label="默认显示" v-show="!(treeNodeData.root || treeNodeData.isRoot)">
              <el-switch
                  v-model="treeNodeData.checked"
                  :disabled="!editNodeShow"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
              >
              </el-switch>
            </el-form-item>
            <el-form-item v-if="editNodeShow">
              <el-button v-if="showTreeData === 'addShow'" type="primary" size="small" @click="submitAddNodeOrData"
              >确认添加
              </el-button>
              <el-button v-else type="primary" size="small" @click="submitEditNode"
              >确认编辑
              </el-button>
              <el-button size="small" @click="cancelEditNode">取消</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-card>

    <el-dialog
        title="新增数据列表"
        :visible.sync="addDialogVisible"
        width="30%"
        @close="handleCloseDialog"
    >
      <el-form
          ref="dataRuleForm"
          :model="dataRuleForm"
          :rules="dataRules"
          label-width="80px"
      >
        <el-form-item label="数据名称" prop="dataName">
          <el-input v-model="dataRuleForm.dataName"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAddData">立即创建</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
// let id = 1;
import DataList from "@/globals/service/dataList.js";
import * as lt from "@/globals/constant/layerType";

export default {
  data() {
    return {
      data: [],
      curHeight: {
        height: ""
      },
      radio: false,
      dataList: [],
      addDialogVisible: false,
      addRootBtn: true,
      dataRuleForm: {
        dataName: ""
      },
      dataRules: {
        dataName: [
          {required: true, message: "请输入数据名称", trigger: "blur"}
        ]
      },
      fileName: "", //文件名
      treeNodeData: {}, //渲染数据对象
      old_treeNodeData: {}, //渲染数据对象
      lt: lt,
      typeOptions: [
        {
          label: lt.tiles_3d
        },
        {
          label: lt.gltf
        },
        {
          label: lt.dem
        },
        {
          label: lt.dom
        },
        {
          label: lt.wmts_xyz
        },
      ],
      editNodeShow: false,
      id: "",
      showTreeData: "", //判断表单显示
      checked: false, //是否选中节点
      fileDialogVisible: false,
      filePath: "",
      fileUrl: "",
      url: "",
      urls: [],
      urlPrefix: "http://",
      LocalAgents: [],
      LocalAgentId: null,
      serverIp: "",
      serverPort: "",
      serverPath: ""
    };
  },
  created() {
    this.getDataList();
    this.setTableHeight();
    window.onresize = () => {
      this.setTableHeight();
    };


    //填充下拉框
    this.getLocalAgent(null, null);

  },
  methods: {
    isTypes: function (type, types) {
      return types.indexOf(type) > -1;
    },
    copyData: function (src, des) {
      for (let p in src) {
        des[p] = src[p];
      }
    },
    cloneData: function (src) {
      let result = {};
      this.copyData(src, result);
      return result;
    },
    getFileUrl: function () {
      return this.urlPrefix + this.serverIp + ":" + this.serverPort + this.filePath;
    },
    changeLA: function () {
      this.url = "";
      this.filePath = "";
      if (this.LocalAgentId == null) {
        this.fileUrl = "";
        this.urls = [];
        this.serverIp = "";
        this.serverPort = "";
        this.serverPath = "";
      } else {
        for (var i = 0, len = this.LocalAgents.length; i < len; i++) {
          var item = this.LocalAgents[i];
          if (this.LocalAgentId == item.id) {
            this.serverIp = item.ip;
            this.serverPort = item.port;
            this.serverPath = item.path;
            this.fileUrl = this.getFileUrl();
            this.fillOption();
            break;
          }
        }
      }

    },
    //打开窗口
    selectPath: function (str) {
      this.fileDialogVisible = true;
      var isClear = 0;
      if (str != undefined && str != null) {
        var index1 = str.indexOf(this.urlPrefix);
        var index2 = str.lastIndexOf(":");
        if (index1 != -1 && index2 != -1) {
          this.serverIp = str.slice(Number(index1) + Number(this.urlPrefix.length), index2);
          str = str.slice(Number(index2) + 1);
          var index3 = str.indexOf("/");
          if (index3 != -1) {
            this.serverPort = str.slice(0, index3);
            this.filePath = str.slice(index3);

            for (var i = 0, len = this.LocalAgents.length; i < len; i++) {
              var item = this.LocalAgents[i];
              if (this.serverIp == item.ip && this.serverPort == item.port) {
                this.serverPath = item.path;
                this.LocalAgentId = item.id;

                this.fileUrl = this.getFileUrl();
                this.fillOption();
                isClear = 1;
                break;
              }
            }
          }
        }

      }

      if (isClear == 0) {
        //初始化
        this.LocalAgentId = null;
        this.fileUrl = "";
        this.urls = [];

        this.serverIp = "";
        this.serverPort = "";
        this.serverPath = "";
        this.filePath = "";
      }
      this.url = "";
    },
    tc: function () {
      if (this.serverPath == "" || this.filePath == "") {
        this.$message.error("路径不能为空");
        return;
      }

      this.fileDialogVisible = false;
      if (this.editNodeShow) {
        this.treeNodeData.url = this.fileUrl;
      } else {
        this.childrenNodeData.url = this.fileUrl;
      }
    },
    ts: function () {
      if (this.serverPath == "" || this.filePath == "") {
        this.$message.error("上一级不存在");
        return;
      }

      var index = this.filePath.lastIndexOf("/");
      if (index != -1) {
        this.filePath = this.filePath.substring(0, index);
      } else {
        this.filePath = "";
      }

      this.fileUrl = this.getFileUrl();
      this.url = "";
      this.fillOption();
    },
    tt: function () {

      this.filePath = this.filePath + "/" + this.url;
      this.url = "";
      this.fileUrl = this.getFileUrl();
      this.fillOption();
    },
    //获得本地代理
    getLocalAgent: function (ip, port) {
      DataList.getLocalAgent({ip: ip, port: port}).then(res => {
        if (res.status == 200) {
          this.LocalAgents = res.data.rows;
        } else {
          this.$message.error(res.message);
        }
      });
    },
    //获得某一目录下所有文件夹和所有类型文件
    fillOption() {
      DataList.getDirsMayFilesJSON(this.serverIp, {
        path: this.serverPath + this.filePath,
        existAllFile: true
      }).then(res => {
        if (res.status == 200) {
          this.urls = res.data;
        } else {
          this.$message.error(res.message);
        }
      });
    },
    //获取json数据
    getDataList() {
      DataList.getDataList().then(res => {
        if (res.status == 200) {
          this.dataList = res.data;
        } else {
          this.$message.error(res.message);
        }
      });
    },
    //添加目录
    addRootNode() {
      this.showTreeData = "addShow";
      this.editNodeShow = true;
      this.treeNodeData = {fileName: this.fileName, isRoot: true,};
      if (this.checked) {
        this.treeNodeData.pId = this.id;
      }
    },
    //确认添加目录/数据
    submitAddNodeOrData() {
      DataList.addJsonNode(this.treeNodeData).then(res => {
        if (res.status == 200) {
          this.$message.success("添加节点成功");
          this.showTreeData = "show";
          this.handleChangeDataList(this.treeNodeData.fileName);
        } else {
          this.$message.error(res.message);
        }
      });
    },
    //取消添加节点
    cancelAddNode() {
      this.showTreeData = "show";
    },
    //添加一个数据
    addNode(data) {
      this.$refs.tree.setCheckedKeys([data.id]);
      this.$nextTick(() => {
        this.treeNodeData = {};
        this.showTreeData = "addShow";
        this.editNodeShow = true;
        this.treeNodeData.fileName = this.fileName;
        if (data.id) {
          this.treeNodeData.pId = data.id;
        }
      });
    },
    //确认修改节点
    submitEditNode() {
      this.treeNodeData.fileName = this.fileName;
      if (this.treeNodeData.children) {
        this.treeNodeData.root = undefined;
        this.treeNodeData.isRoot = true;
      }
      DataList.editJsonNode(this.treeNodeData).then(res => {
        if (res.status == 200) {
          this.$message.success("修改成功");
          this.editNodeShow = false;
        } else {
          this.$message.error(res.message);
        }
      });
    },
    //取消修改节点
    cancelEditNode() {
      this.copyData(this.old_treeNodeData, this.treeNodeData);
      this.editNodeShow = false;
    },
    //修改节点
    editNode(data) {
      this.$refs.tree.setCheckedKeys([data.id]);
      this.$nextTick(() => {
        this.id = data.id;
        this.old_treeNodeData = this.cloneData(this.treeNodeData);
        this.editNodeShow = true;
      });
    },
    //删除子节点
    removeNode(data) {
      let id = data.id;
      let fileName = this.fileName;
      this.$confirm(`是否删除该节点?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
          .then(() => {
            DataList.delJsonChildrenNode(id, fileName).then(res => {
              if (res.status == 200) {
                this.handleChangeDataList(fileName);
                this.$message.success("删除成功");
              } else {
                this.$message.error(res.message);
              }
            });
          })
          .catch(() => {
            this.$message({type: "info", message: "已取消删除"});
          });
    },
    //删除节点
    deleteNode() {
      this.$nextTick(() => {
        this.$confirm(`是否删除该文件?`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
            .then(() => {
              DataList.delJsonFile(this.fileName).then(res => {
                if (res.status == 200) {
                  this.getDataList();
                  this.data = [];
                } else {
                  this.$message.error(res.message);
                }
              });
            })
            .catch(() => {
              this.$message({type: "info", message: "已取消删除"});
            });
      });
    },
    //移动节点位置（正数下移，负数上移）
    moveNode: function (val) {
      if (this.id == "") {
        this.$message.info("请选定一个节点");
        return;
      }
      DataList.moveJsonNode({fileName: this.fileName, id: this.id, moveNum: val}).then(res => {
        if (res.status == 200) {
          this.handleChangeDataList(this.fileName);

          //设置树节点选中
          this.$refs.tree.setCheckedKeys([this.id]); //无效
          this.$message.success("移动节点成功");
        } else {
          this.$message.error(res.message);
        }
      });
    }
    ,
    //获取树节点数据
    getTreeData(data, checked) {
      if (this.editNodeShow) {
        this.cancelEditNode();
      }
      if (data.id == this.id) {
        if (!checked) {
          this.checked = false;
          this.showTreeData = "";
          this.treeNodeData = {};
          this.addRootBtn = true;
          this.id = "";
        }
        return;
      }

      if (checked) {
        // console.log(data, checked);
        this.checked = true;
        // this.$refs.tree.setCheckedKeys([data.id]);
        this.$refs.tree.setCheckedNodes([data]);
        this.showTreeData = "show";
        this.id = data.id;
        this.addRootBtn = !!(data.root || data.isRoot);
        this.treeNodeData = data;
      } else {
        this.checked = false;
      }

    },

    nodeClick(data) {

      this.$refs.tree.setCheckedNodes([data]);

      //渲染页面后调用
      /*  this.$nextTick(() => {
      item.checked = true;
    })*/
    },

    //新增一条data数据
    handleAddData() {
      this.$refs.dataRuleForm.validate(async valid => {
        if (!valid)
          return this.$message.error("信息填写不完整或不准确，请检查再提交！");
        let fileName = this.dataRuleForm.dataName;
        let formData = new FormData();
        formData.append("fileName", fileName);
        DataList.addDataItem(formData).then(res => {
          if (res.status == 200) {
            this.addDialogVisible = false;
            this.$message.success("新增数据成功");
            this.dataRuleForm = {};
            this.getDataList();
          } else if (res.status == 500) {
            this.$message.warning("已存在同名文件或文件夹，请重新填写！");
            this.dataRuleForm.dataName = "";
          }
        });
      });
    },
    //显示新增数据弹框
    handleAddDataDialogShow() {
      this.addDialogVisible = true;
    },
    //关闭新增数据弹框
    handleCloseDialog() {
      this.dataRuleForm = {};
      this.$refs.dataRuleForm.clearValidate();
      this.getDataList();
    },
    //选择json文件事件
    handleChangeDataList(value) {
      this.addRootBtn = value ? true : false;
      this.treeNodeData.fileName = value;
      this.fileName = value;
      let formData = new FormData();
      formData.append("fileName", value);
      DataList.getCataContent(formData).then(res => {
        if (res.status === 200) {
          this.data = res.data;
        } else {
          this.data = [];
        }
        this.checked = false;
        this.showTreeData = "";
        this.treeNodeData = {};
        this.$nextTick(() => {
          this.$refs.tree.setCheckedNodes([]);
        })
      });
    },
    // 设定表格高度
    setTableHeight() {
      let h =
          document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight.height = h - 124 + "px";
    }
  }
};
</script>
<style lang="less" scoped>
.data-list-card {
  display: flex;
  white-space: nowrap;
}

/deep/ .data-list-card > .el-card__body {
  padding-left: 540px;
  width: 100%;
}

.el-transfer-panel {
  height: 100%;
}

.el-checkbox__label {
  padding-right: 10px;
}

.el-transfer-panel__body {
  width: 100%;
  padding-top: 40px;
  height: 100%;
}

.el-transfer-panel__body_overflow {
  overflow: auto;
  height: 100%;
}

.el-transfer-panel__header {
  margin-bottom: -40px;
}

.el-transfer-panel__body_content {
  width: 100%;
  padding-top: 40px;
  overflow: auto;
  height: 100%;
  display: flex;
  justify-content: center;
}

.el-transfer-panel.file-tree {
  width: 200px;
  margin-left: -530px;
}


.el-transfer-panel.data-tree {
  width: 300px;
  margin-left: 20px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
  overflow: hidden;
}

.el-transfer-panel.data-content {
  width: 100%;
  margin-left: 20px;
}

.el-icon-error {
  float: right;
  line-height: 30px;
}

.el-checkbox {
  margin-right: 30px;
}

.item-btn {
  flex-shrink: 0;
}

.item-text {
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
