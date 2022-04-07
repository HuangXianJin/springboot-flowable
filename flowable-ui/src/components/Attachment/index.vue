
<template>
  <div :class="onlyPreview?'onlyPreview uploadmyfile':'uploadmyfile'">
    <!--start-->
    <div v-show="onlyPreview && photoNothing && total < 1" class="nothing">
      <div class="iconfont my-icon-nothing nothing_img"></div>
      <div class="nothing_name">暂无数据</div>
    </div>
    <!--end-->
    <!--start-->
    <div v-show="onlyPreview && !photoNothing && total < 1" class="nothing_photo">
      <div class="iconfont my-icon-nothingPhoto nothing_photo_name"></div>
    </div>
    <!--end-->
    <div v-if="!onlyPreview && tip" slot="tip" class="el-upload__tip tips">【{{ tip }}】</div>
    <el-upload
      v-if="!onlyPreview || (onlyPreview && total >0)"
      ref="upload"
      :data="data"
      :multiple="multiple"
      :on-preview="handlePreview"
      :before-remove="handleBeforeRemove"
      :before-upload="handleBeforeUpload"
      :file-list="fileList"
      :http-request="upload"
      :action="action"
      :list-type="listType"
      :disabled="onlyPreview"
      :limit="limit"
    >
      <div v-if="!onlyPreview" style="width:100%;">
        <i v-if="listType === 'picture-card'" class="el-icon-plus"></i>
        <div v-else class="videobtn">
          <i class="el-icon-plus"></i>
        </div>
      </div>
    </el-upload>
    <el-dialog :close-on-click-modal="false" :visible.sync="dialogVisible" append-to-body>
      <img :src="dialogImageUrl" width="100%" alt />
    </el-dialog>
  </div>
</template>

<script>
import File from '@/api/system/sys_file'
import ajax from '@/utils/ajax'
import { FILE_TYPE } from '@/api/enum/enum'

export default {
  name: 'Attachment',
  props: {
    photoNothing: { type: Boolean, default: false },
    code: { type: String, required: true },
    fileType: { type: String, required: true },
    onlyPreview: { type: Boolean, default: false },
    desc: { type: String, default: '附件' },
    multiple: { type: Boolean, default: true }, // 多选
    limit: { type: Number, default: undefined }, // 最大上传数
    isShowSingle: { type: Boolean, default: false }, // 是否显示单个
    returnTotal: { type: Boolean, default: false } // 是否返回总数,可以帮助必传场景的校验（大于0,则说明已上传）,需要绑定@returnTotal事件
  },
  data() {
    return {
      action: '',
      tip: undefined,
      IMG: FILE_TYPE.ATTACHMENT_IMG,
      VIDEO: FILE_TYPE.ATTACHMENT_VIDEO,
      FIRMWARE: FILE_TYPE.ATTACHMENT_FIRMWARE,
      ALLTYPE: FILE_TYPE.ATTACHMENT_FILE,
      listType: 'text',
      fileList: [],
      dialogImageUrl: '',
      dialogVisible: false,
      httpRequest: ajax,
      isImg: false
    }
  },
  computed: {
    data: {
      get() {
        return { attachmentCode: this.code, sysFileType: this.fileType, desc: this.desc }
      },
      set(val) {
        this.$emit('input', val)
      }
    },
    total: {
      get() {
        return this.fileList.length
      },
      set(val) {

      }
    }
  },
  watch: {
    code: {
      handler(newValue, oldValue) {
        this.getAttachments()
      },
      deep: true
    },
    fileType: {
      handler(newValue, oldValue) {
        this.changeTip()
        this.getAttachments()
      },
      deep: true
    },
    total(newValue) { // 返回总数
      if (this.returnTotal) {
        this.$emit('returnTotal', newValue)
      }
    }
  },
  mounted() {
    this.getAttachments()
    this.changeTip()
  },
  methods: {

    changeTip() {
      this.tip = ''
      this.listType = 'text'
      if (this.fileType === FILE_TYPE.ATTACHMENT_IMG || this.fileType === FILE_TYPE.AVATAR || this.fileType === FILE_TYPE.STUDENT_AVATAR) {
        this.tip = '仅支持jpg/jpeg/png/bmp/gif文件'
        this.listType = 'picture-card'
      }
      if (this.fileType === this.VIDEO) {
        this.tip = '仅支持rmvb/avi/mov/mpeg/mp4/mkv/wmv文件'
      }
      if (this.fileType === this.FIRMWARE) {
        this.tip = '仅支持bin格式的文件'
      }
      if (this.fileType === this.ALLTYPE) {
        this.tip = '可支持文件、图片、音频格式'
      }
    },
    getAttachments() {
      File.getAttachmentsByAttachmentCode({ attachmentCode: this.code }).then((response) => {
        // 只显示一张图片
        if (this.isShowSingle && response.data.length > 0) {
          this.fileList[0] = response.data[0]
          this.fileList[0].name = this.fileList[0].originName
          return
        }
        this.fileList = response.data
        this.fileList.forEach(v => {
          v.name = v.originName
        })
      })
    },
    handleBeforeRemove(file, fileList) {
      var _self = this
      if (file.id) {
        return new Promise((resolve, reject) => {
          this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            File.deleteAttachment(file.id).then(response => {
              _self.getAttachments()
              resolve(response.data)
            }).catch(error => { reject(error) })
          }).catch(() => {
            reject()
          })
        })
      }
    },
    handleBeforeUpload(file) {
      const fileName = file.name.toLowerCase()
      this.$emit('handleBeforeUpload', file)
      if (!this.fileType) {
        return true
      }
      if (this.fileType === this.ALLTYPE) {
        return true
      }
      var regex
      if (this.fileType === FILE_TYPE.ATTACHMENT_IMG || this.fileType === FILE_TYPE.AVATAR || this.fileType === FILE_TYPE.STUDENT_AVATAR) {
        regex = /(.jpg|.jpeg|.png|.bmp|.gif)$/
      }
      if (this.fileType === this.VIDEO) {
        regex = /(.rmvb|.avi|.mpeg|.mov|.mp4|.mkv|.wmv)$/
      }
      if (this.fileType === this.FIRMWARE) {
        regex = /(.bin)$/
      }
      if (regex && !regex.test(fileName.toLowerCase())) {
        this.$message.error('请上传格式支持的文件')
        return false
      }
    },
    handlePreview(file) {
      const fileName = file.name.toLowerCase()
      var regex = /(.jpg|.jpeg|.png|.bmp|.gif)$/
      if (regex.test(fileName.toLowerCase())) {
        this.dialogImageUrl = file.url
        this.dialogVisible = true
      }
      if (!regex.test(fileName.toLowerCase()) && file.url !== undefined) {
        window.open(file.url)
      }
      if (!regex.test(fileName.toLowerCase()) && file.response.data.url !== undefined) {
        window.open(file.response.data.url)
      }
    },
    upload(options) {
      const dataFile = options.data
      dataFile.file = options.file
      File.uploadAttachment(dataFile).then(e => {
        e.data.name = e.data.originName

        this.fileList.push(e.data)
      }).catch(err => {
        console.log(err)
        this.$refs.upload.uploadFiles.pop()
      })
    }
  }
}
</script>

<style>
.nothing_photo .nothing_photo_name {
  font-size: 50px;
}
.onlyPreview .el-upload--picture-card {
  visibility: hidden;
}

.uploadmyfile .el-upload-list--picture .el-upload-list__item-thumbnail {
  display: none;
}

.uploadmyfile .el-upload-list--text {
  width: 100%;
}

.uploadmyfile .el-upload--text {
  width: 100%;
}

.uploadmyfile .el-upload-list--picture .el-upload-list__item {
  padding: 10px;
}

.uploadmyfile
  .el-upload-list--picture
  .el-upload-list__item.is-success
  .el-upload-list__item-name {
  line-height: 30px;
}

.uploadmyfile .el-upload-list--picture .el-upload-list__item {
  height: 50px;
}

.uploadmyfile .tips {
  color: red;
  margin: 0 0 5px 0;
  font-size: 14px;
}

.uploadmyfile .el-upload-list--picture-card .el-upload-list__item {
  width: 100px;
  height: 100px;
}

.uploadmyfile .el-upload--picture-card {
  width: 98px;
  height: 98px;
  line-height: 98px;
}

.uploadmyfile .el-upload-list__item-name {
  color: #333;
}

.uploadmyfile .videobtn {
  display: inline-block;
  width: 100%;
  height: 50px;
  line-height: 50px;
  border-radius: 6px;
  color: #8c939d;
  background: #fbfdff;
  border: 1px dashed #c0ccda;
}

.uploadmyfile .videobtn i {
  font-size: 25px;
  color: #8c939d;
  margin-top: 10px;
}
</style>
