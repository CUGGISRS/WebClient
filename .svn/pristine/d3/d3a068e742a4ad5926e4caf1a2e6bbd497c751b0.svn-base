<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-input class="filter-item" style="margin-left: 10px;" @change="handleSelected"
             type="file" accept=".mp4" name="fileToUpload" id="fileToUpload"/>
      <el-button class="filter-item" v-if="userManager_btn_add" style="margin-left: 10px;" @click="upload"
                 type="primary">上传
      </el-button>
      <!-- <el-button class="filter-item" v-if="userManager_btn_add" style="margin-left: 10px;" @click="handlePause"
                  type="primary" >暂停
       </el-button>-->
      <div id="fileFrame"></div>
    </div>

    <Modal :show="show" :title="title" @hidenModal="hidenModal" @submit="submit" id="modal">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="视频名称" prop="videoname">
          <el-input type="text" v-if="dialogStatus === 'create'" v-model="form.videoname"
                    placeholder="请输入视频名称"></el-input>
          <el-input v-else v-model="form.videoname" placeholder="请输入视频名称"></el-input>
        </el-form-item>
        <el-form-item label="视频分类" prop="classification">
          <el-input type="text" v-model="form.classification" placeholder="请输入视频分类"></el-input>
        </el-form-item>
        <el-form-item label="备注信息" prop="remark">
          <el-input type="text" v-model="form.remark" placeholder="请输入备注信息"></el-input>
        </el-form-item>
        <!-- <el-form-item label="上传状态" prop="status">
           <el-input v-model="form.status" placeholder="请直接给0"></el-input>
         </el-form-item>-->
      </el-form>
    </Modal>

    <el-table :key='tableKey' v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%">
      <el-table-column align="center" label="序号" width="100">
        <template slot-scope="scope">
          <span>{{scope.row.id}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="视频名称" width="200">
        <template slot-scope="scope">
          <span>{{scope.row.videoname}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="视频分类" width="200">
        <template slot-scope="scope">
          <span>{{scope.row.classification}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="上传时间" width="200">
        <template slot-scope="scope">
          <span>{{scope.row.uploaddate}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="用户编号" width="120">
        <template slot-scope="scope">
          <span>{{scope.row.userid}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="备注信息" width="130">
        <template slot-scope="scope">
          <span>{{scope.row.remark}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="文件地址" width="467">
        <template slot-scope="scope">
          <span>{{scope.row.url}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" width="100" visible="true">
        <template slot-scope="scope">
          <span>{{scope.row.state}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="150">
        <template slot-scope="scope">
          <!--<el-button v-if="userManager_btn_edit" size="small" type="success" @click="handleUpdate(scope.row)">
            编辑
          </el-button>-->
          <el-button v-if="userManager_btn_del" size="small" type="danger" @click="handleDelete(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<script>
    import {mapGetters} from 'vuex';
    import Upload from 'components/Upload/SingleImage3'
    import {getvideolist, uploadfile} from '../../api/video';
    import Modal from './Modal.vue';
    // import  AxiosRequestConfig as onUploadProgress } from 'axios';


    export default {
        name: 'video',
        components: {Upload, Modal},

        data() {
            return {
                title: '视频上传',
                show: false,
                form: {
                    videoname: undefined,
                    classification: undefined,
                    remark: undefined,
                    extension: '.mp4'
                },
                rules: {
                    videoname: [
                        {
                            required: true,
                            message: '请输入视频名称',
                            trigger: 'blur'
                        }
                    ]
                },
                id: '',
                url: '',
                urlAndId: '',

                list: null,
                listLoading: true,
                listQuery: {},

                fileList: '',
                length: '',
                frame: '',
                fileSize: '',
                file: '',
                nameDiv: null,
                sizeDiv: null,
                typeDiv: null,
                uploadState: '',
                startSize: '',
                endSize: '',
                reader: '',
                xhr: '',
                data: '',
                ui8a: '',
                message: '',
                percentComplete: '',
                transform: '',
                paragraph: 1024 * 1024 * 2,
                blob: '',

                formVisible: false,
                userManager_btn_del: false,
                userManager_btn_add: false,
                userManager_btn_edit: false,
                dialogStatus: '',
                dialogFormVisible: false,
                /* textMap: {
                  update: '视频信息',
                  create: '111'
                },*/
                tableKey: 0
            }
        },
        created() {
            this.getList();
            this.userManager_btn_del = this.elements['userManager:btn_del'];
            this.userManager_btn_add = this.elements['userManager:btn_add'];
            this.userManager_btn_edit = this.elements['userManager:btn_edit'];
        },
        computed: {
            ...mapGetters([
                'elements'
            ])
        },
        methods: {
            getList() {
                this.listLoading = true;
                getvideolist(this.listQuery).then(response => {
                    console.log(response);
                    this.list = response.data;
                    this.listLoading = false;
                })
            },

            handleSelected(event) {
                return true;
                // this.fileList = document.getElementById('fileToUpload');
                // this.length = this.fileList.files.length;
                // this.frame = document.getElementById('fileFrame');
                // for (let i = 0; i < this.length; i++) {
                //     this.file = this.fileList.files[i];
                //     if (this.file) {
                //         this.fileSize = 0;
                //         if (this.file.size > 1024 * 1024) {
                //             this.fileSize = (Math.round(this.file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
                //         } else {
                //             this.fileSize = (Math.round(this.file.size * 100 / 1024) / 100).toString() + 'KB';
                //         }
                //         this.nameDiv = document.createElement('div');
                //         this.nameDiv.setAttribute('id', 'fileName' + i);
                //         this.nameDiv.innerHTML = 'Name: ' + this.file.name;
                //         this.sizeDiv = document.createElement('div');
                //         this.sizeDiv.setAttribute('id', 'fileSize' + i);
                //         this.sizeDiv.innerHTML = 'fileSize: ' + this.fileSize;
                //         this.typeDiv = document.createElement('div');
                //         this.typeDiv.setAttribute('id', 'progressNumber' + i);
                //         this.typeDiv.innerHTML = '';
                //     }
                // }
            },

            upload() {
                this.show = true;
            },
            hidenModal() {
                this.show = false;
                this.form = {};
                this.form.status = 0
            },
            /* uploadFileInit(file, i) {
               if (file) {
                 const startSize = 0;
                 const endSize = 0;
                 uploadfile(file, startSize, endSize, i);
               }
             },
             uploadFile(file, startSize, endSize, i) {
               const reader = new FileReader();
               reader.onload = function loaded(evt) {
                 const xhr = new XMLHttpRequest();   // 构造 XMLHttpRequest 对象，发送文件 Binary 数据
                 xhr.sendAsBinary = function(text) {
                   const data = new ArrayBuffer(text.length);
                   const ui8a = new Unit8Array(data, 0);
                   for (let i = 0; i < text.length; i++) {
                     ui8a[i] = text.charCodeAt(i) & 0xff;
                   }
                   this.send(ui8a);
                 };
                 xhr.onreadystatechange = function() {
                   if (xhr.readyState === 4) {
                     if (xhr.status === 200) {      // 表示服务器的相应代码是200；正确返回了数据
                       const message = xhr.responseText;   // 纯文本数据的接受方法
                       message = Number(message);
                       onUploadProgress(file, startSize, message, i);
                     }
                   }
                 };
                 // 创建回调方法
                 xhr.open(){}
               }
             },*/
            submit() {
                uploadfile(this.form).then(response => {
                    this.urlAndId = response.data;
                    console.log(this.urlAndId);
                    this.id = this.urlAndId.id;
                    this.url = this.urlAndId.url;
                    console.log(this.id);
                    console.log(this.url);
                    /* this.id = response.id;
                     this.url = response.url;*/
                    this.dialogFormVisible = true;
                    this.dialogStatus = 'update'
                });
                this.show = false;
            }

            /* handlePause() {
               this.uploadState = 2;
             }*/
        }
    }

</script>

<style scoped>

</style>
