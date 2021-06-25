<template>
  <div class="upload-image-avatar-container">
    <div v-if="imagePath" class="upload-image-avatar-image" :style="{ backgroundImage: 'url(' + imagePath + ')' }" />
    <span class="upload-image-avatar-mask">编辑</span>
    <input type="file" class="upload-image-avatar-file" @change="changeEvent" />
    <div v-show="loading" class="el-loading-mask">
      <div class="el-loading-spinner">
        <svg viewBox="25 25 50 50" class="circular">
          <circle cx="50" cy="50" r="20" fill="none" class="path" />
        </svg>
      </div>
    </div>
  </div>
</template>

<script>
/* [uploadImage] 七牛文件上传组件
 ** @props   size            [Number]    大小限制（单位: M）
 ** @props   autoUpload      [Boolean]   是否自动上传      @default true
 ** @method  uploadEvent     [Function]  上传方法        @return promise
 ** @event   success         [Object]    上传成功-触发    @arguments file => 文件 id => 七牛返回的数据
 ** @event   fail            [Object]    上传失败-触发     @arguments err => 失败
 */
import qiniuService from '@/globals/service/qiniu';

export default {
  components: {},
  props: {
    imageId: {
      type: Number,
      default: 0
    },
    imageName: {
      type: String,
      default: ''
    },
    imagePath: {
      type: String,
      default: ''
    },
    uploadTips: {
      type: String,
      default: ''
    },
    uploadLimit: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      file: null,
      loading: false,
      progress: 0
    };
  },
  methods: {
    changeEvent(evt) {
      const ele = evt.target;
      const file = ele.files[0];
      if (!file || !this.validateSize(file)) {
        // eslint-disable-next-line
        evt.srcElement.value = '';
        return;
      }

      this.loading = true;
      const params = { file };
      qiniuService
        .start(params, () => {})
        .then(res => {
          this.$emit('update:imageName', res.name);
          this.$emit('update:imagePath', res.imageUrl);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    // 验证大小
    validateSize(file) {
      const size = this.uploadLimit * 1024 * 1024;
      const fileName = file.name;
      const suffix = fileName.split('.').pop();
      const imageRegex = /(png|jpg|jpeg)/;
      if (file.size > size || !imageRegex.test(suffix)) {
        this.$message.error(`请上传不大于 ${this.uploadLimit}MB 且格式为png、jpg、jpeg的图片`);
        return false;
      }
      return true;
    }
  }
};
</script>

<style lang="less" scoped>
.upload-image-avatar-container {
  position: relative;
  width: 160px;
  height: 160px;
  cursor: pointer;
  border-radius: 50%;
  overflow: hidden;
  z-index: 99;
  background-color: #f1f1f1;

  &:hover {
    .upload-image-avatar-mask {
      opacity: 1;
    }
  }

  .upload-image-avatar-image {
    width: 100%;
    height: 100%;
    display: block;
    background-size: 50px 50px;
    background-repeat: no-repeat;
    background-position: center;
  }

  .upload-image-avatar-mask {
    display: block;
    position: absolute;
    bottom: 0;
    right: 0;
    width: 100%;
    height: 100%;
    text-align: center;
    font-size: 15px;
    background-color: rgba(0, 0, 0, 0.4);
    display: flex;
    justify-content: center;
    align-items: center;
    font-style: normal;
    color: #fff;
    opacity: 0;
    transition: opacity 0.2s;
  }

  .upload-image-avatar-file {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    opacity: 0;
    cursor: pointer;
  }

  .percent-load {
    position: absolute;
    z-index: 2000;
    background-color: rgba(255, 255, 255, 0.9);
    margin: 0;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    transition: opacity 0.3s;

    &__spinner {
      top: 50%;
      height: 40px;
      margin-top: -20px;
      width: 100%;
      text-align: center;
      position: absolute;
      font-size: 20px;
    }
  }
}
</style>
