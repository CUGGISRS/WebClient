<template>
  <div class="createPost-container">
    <el-form ref="postForm" :model="postForm" :rules="rules" class="form-container">

      <sticky :z-index="10" :class-name="'sub-navbar '">
        <CommentDropdown v-model="postForm.comment_disabled"/>
        <PlatformDropdown v-model="postForm.platforms"/>
        <SourceUrlDropdown v-model="postForm.source_uri"/>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">
          发布
        </el-button>
        <el-button v-loading="loading" type="warning" @click="draftForm">
          草稿
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-row>
          <Warning/>

          <el-col :span="24">
            <el-form-item style="margin-bottom: 40px;" prop="title">
              <MDinput v-model="postForm.title" :maxlength="100" name="name" required>
                标题
              </MDinput>
            </el-form-item>

            <div class="postInfo-container">
              <el-row>
                <!--<el-select v-model="postForm.author" :remote-method="getRemoteUserList" filterable     // 不要的
                           default-first-option remote placeholder="搜索用户">
                  <el-option v-for="(item,index) in userListOptions" :key="item+index" :label="item" :value="item"/>
                </el-select>-->
                <el-col :span="8">
                  <el-form-item label-width="60px" label="作者:" class="postInfo-container-item">

                    <el-input v-model="postForm.author" type="textarea" class="publisher-textarea" autosize
                              placeholder="请输入作者"/>
                  </el-form-item>
                </el-col>

                <el-col :span="10">
                  <el-form-item label-width="120px" label="发布时间 :" class="postInfo-container-item">
                    <el-date-picker id="crtTime" v-model="postForm.updatetime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
                                    placeholder="选择日期和时间"/>
                  </el-form-item>
                </el-col>

                <el-form-item label-width="80px" label="新闻类型:" class="postInfo-container-item">
                  <el-select v-model="postForm.type" filterable placeholder="选择类型">
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.label">
                    </el-option>
                  </el-select>
                </el-form-item>


                <!--<el-col :span="6">
                  <el-form-item label-width="90px" label="重要性:" class="postInfo-container-item">
                    <el-rate
                      v-model="postForm.importance"
                      :max="3"
                      :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                      :low-threshold="1"
                      :high-threshold="3"
                      style="display:inline-block"
                    />
                  </el-form-item>
                </el-col>-->
              </el-row>
            </div>
          </el-col>
        </el-row>

        <!--<el-form-item style="margin-bottom: 40px;" label-width="70px" label="摘要:">
          <el-input v-model="postForm.content_short" :rows="1" type="textarea" class="article-textarea" autosize
                    placeholder="请输入内容"/>
          <span v-show="contentShortLength" class="word-counter">{{ contentShortLength }}words</span>
        </el-form-item>-->

        <el-form-item prop="content" style="margin-bottom: 30px;">
          <Tinymce ref="editor" v-model="postForm.content" :height="400"/>
        </el-form-item>

        <!--<el-form-item prop="image_uri" style="margin-bottom: 30px;">
          <Upload v-model="postForm.image"/>
        </el-form-item>-->
      </div>
    </el-form>
  </div>
</template>

<script>
  import Tinymce from 'components/Tinymce'
  import Upload from 'components/Upload/SingleImage3'
  import MDinput from 'components/MDinput'
  import Sticky from 'components/Sticky' // 粘性header组件
  // import { validURL } from 'utils/validate'
  import { fetchArticle } from 'api/article'
  import { searchUser } from 'api/remoteSearch'
  import Warning from './Warning'
  import { CommentDropdown, PlatformDropdown, SourceUrlDropdown } from './Dropdown'
  import fetch from 'utils/fetch';

    const defaultForm = {
    type: '', // 新闻类型
    platforms: ['a-platform'],
    source_uri: '',
    status: 'draft', // 状态
    author: '', // 作者
    // sTtatus: 'draft',
    title: '', // 文章题目
    content: '', // 文章内容
    // content_short: '', // 文章摘要
    //  source_uri: '', // 文章外链
   // image: '', // 文章图片
    updatetime: undefined,  // 前台展示时间
   // updTime: undefined,
    newsid: 0,
    // platforms: ['a-platform'],
    comment_disabled: false
    // importance: 0
  }

  export default {
    name: 'ArticleDetail',
    components: { Tinymce, MDinput, Upload, Sticky, Warning, CommentDropdown, PlatformDropdown, SourceUrlDropdown },
    props: {
      isEdit: {
        type: Boolean,
        default: false
      }
    },
    data() { // 判断用户输入的内容
      /* const validateRequire = (rule, value, callback) => {
        if (value === '') {
          this.$message({
            message: rule.field + '为必传项',
            type: 'error'
          })
          callback(new Error(rule.field + '为必传项'))
        } else {
          callback()
        }
      } */
      /* const validateSourceUri = (rule, value, callback) => {
        if (value) {
          if (validURL(value)) {
            callback()
          } else {
            this.$message({
              message: '外链url填写不正确',
              type: 'error'
            })
            callback(new Error('外链url填写不正确'))
          }
        } else {
          callback()
        }
      }*/
      return {
        options: [{
          value: '选项1',
          label: '时政要闻'
        }, {
          value: '选项2',
          label: '农业要闻'
        }, {
          value: '选项3',
          label: '全区联播'
        }, {
          value: '选项4',
          label: '政策法规'
        }],
        postForm: Object.assign({}, defaultForm),
        loading: false,
        userListOptions: [],
        rules: {
          // author: [{ validator: validateRequire }],
          // image: [{ validator: validateRequire }],
          // title: [{ validator: validateRequire }],
          // content: [{ validator: validateRequire }]
          // source_uri: [{ validator: validateSourceUri, trigger: 'blur' }]
        },
        tempRoute: {}
      }
    },
    computed: {
     /* contentShortLength() {
        return this.postForm.content_short.length
      },*/
      displayTime: {
        // set and get is useful when the data
        // returned by the back end api is different from the front end
        // back end return => "2013-06-25 06:59:25"
        // front end need timestamp => 1372114765000
        get() {
          return +new Date(this.postForm.updatetime)
        },
        set(val) {
          this.postForm.updatetime = new Date(val)
        }
      }
    },
    created() {
      if (this.isEdit) {
        const id = this.$route.params && this.$route.params.id
        this.fetchData(id)
      } else {
        this.postForm = Object.assign({}, defaultForm)
      }

      // Why need to make a copy of this.$route here?
      // Because if you enter this page and quickly switch tag, may be in the execution of the setTagsViewTitle function, this.$route is no longer pointing to the current page
      // https://github.com/PanJiaChen/vue-element-admin/issues/1221
      this.tempRoute = Object.assign({}, this.$route)
    },
    methods: {
      fetchData(newsid) {
        fetchArticle(newsid).then(response => {
          this.postForm = response.data

          // just for test
          /* this.postForm.title += `Article newsid:${this.postForm.newsid}`
          this.postForm.content_short += `Article newsid:${this.postForm.newsid}`*/

          // set tagsview title
         // this.setTagsViewTitle()

          // set page title
          this.setPageTitle()
        }).catch(err => {
          console.log(err)
        })
      },
     // setTagsViewTitle() {
     //   const title = 'EditArticle'
      //  const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.postForm.newsid}` })
      //  this.$store.dispatch('tagsView/updateVisitedView', route)
     // },
      setPageTitle() {
        const title = 'EditArticle'
        document.title = `${title} - ${this.postForm.newsid}`
      },
      submitForm() {
        console.log(this.postForm);
        const data = this.postForm;
        if (typeof data.updatetime != 'string') {
          const yyyy = data.updatetime.getFullYear();
          let MM = data.updatetime.getMonth() + 1;
          let dd = data.updatetime.getDate();
          let HH = data.updatetime.getHours();
          let mm = data.updatetime.getMinutes();
          let ss = data.updatetime.getSeconds();
          if (MM < 10) {
            MM = '0' + MM;
          }
          if (dd < 10) {
            dd = '0' + dd;
          }
          if (HH < 10) {
            HH = '0' + HH;
          }
          if (mm < 10) {
            mm = '0' + mm;
          }
          if (ss < 10) {
            ss = '0' + ss;
          }
          // date.UpdateTime = yyyy + '-' + MM + '-' + dd + ' ' + HH + ':' + mm + ':' + ss;
          const oDate = yyyy + '-' + MM + '-' + dd + ' ' + HH + ':' + mm + ':' + ss;
          data.updatetime = oDate;
        }
        this.$refs.postForm.validate(valid => {
          if (valid) {
            this.loading = true
            this.$notify({
              Title: '成功',
              message: '发布文章成功',
              type: 'success',
              duration: 2000
            })
            this.postForm.status = 'published'
            this.loading = false
          } else {
            console.log('错误提交!!')
            return false
          }
        });
        // 拼接yyyy-MM-dd HH:mm:ss
        /* if (!is_string(oDate)) {
          console.log(oDate)
        }*/
       // oDate = oDate.toString()
        /* data.updTime =*/
        if (this.isEdit) {
          return fetch({
            url: 'api/news/news/' + data.newsid,
            method: 'put',
            data
          })
        } else {
          return fetch({
            url: '/api/news/news',
            method: 'post',
            data
          })
        }
      },
      draftForm() {
        if (this.postForm.content.length === 0 || this.postForm.title.length === 0) {
          this.$message({
            message: '请填写必要的标题和内容',
            type: 'warning'
          })
          return
        }
        this.$message({
          message: '保存成功',
          type: 'success',
          showClose: true,
          duration: 1000
        })
        this.postForm.status = 'draft'
      },
      getRemoteUserList(query) {
        searchUser(query).then(function(response) {
          if (!response.data.items) return;
          this.userListOptions = response.data.items.map(v => v.name)
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "~@/styles/mixin.scss";

  .createPost-container {
    position: relative;

    .createPost-main-container {
      padding: 40px 45px 20px 50px;

      .postInfo-container {
        position: relative;
        @include clearfix;
        margin-bottom: 10px;

        .postInfo-container-item {
          float: left;
        }
      }
    }

    .word-counter {
      width: 40px;
      position: absolute;
      right: 10px;
      top: 0;
    }
  }

  .publisher-textarea /deep/ {
    textarea {
      height: 31px;
      padding-right: 40px;
      resize: none;
    }
  }

  .article-textarea /deep/ {
    textarea {
      padding-right: 40px;
      resize: none;
      border: none;
      border-radius: 0;
      border-bottom: 1px solid #bfcbd9;
    }
  }
</style>
