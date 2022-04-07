<template>
  <div class="my-upload-head-box">
    <el-upload class="avatar-uploader" action="这里填入后台的接口地址" :http-request="upload" :show-file-list="false" :before-upload="beforeAvatarUpload">
      <el-image v-if="imageUrl" :src="imageUrl" fit="cover" class="avatar" />
      <span v-if="imageUrl" class="el-upload-action">
        <i class="el-icon-delete" @click.stop="handleRemove()"></i>
        <i class="el-icon-zoom-in" @click.stop="handleView()"></i>
      </span>
      <i v-else class="el-icon-upload2 avatar-uploader-icon" stop></i>
    </el-upload>
    <el-dialog :close-on-click-modal="false" :visible.sync="dialogVisible" append-to-body width="500px" class="my-upload-head-gallery-box">
      <el-image :src="imageUrl" width="100%" />
    </el-dialog>
  </div>
</template>

<script>
import File from '@/api/system/sys_file'
import { uuid } from '@/utils'

export default {
  props: {
    code: { type: String, required: true },
    desc: { type: String, default: '头像' },
    fileType: { type: String, default: 'AVATAR' }
  },
  data() {
    return {
      imageUrl: '',
      file: undefined,
      dialogVisible: false
    }
  },
  watch: {
    code: {
      handler(newValue, oldValue) {
        this.loadFile()
      }
    }
  },
  mounted() {
    if (this.code) {
      this.loadFile()
    } else {
      var attachmentCode = uuid()
      this.$emit('update:code', attachmentCode)
    }
  },
  methods: {
    async upload(obj) {
      const datafile = {} // 创建form对象
      datafile.attachmentCode = this.code
      datafile.sysFileType = this.fileType
      datafile.desc = this.desc
      datafile.file = obj.file
      await File.uploadAttachment(datafile).then(e => {
        this.loadFile()
      })
    },
    loadFile() {
      this.file = undefined
      this.imageUrl = ''
      File.getAttachmentsByAttachmentCode({ attachmentCode: this.code }).then((response) => {
        if (response.data.length > 0) {
          this.file = response.data[0]
          this.imageUrl = this.file.url
        }
      })
    },
    // 移除图片
    handleRemove() {
      this.$confirm('此操作将永久删除头像, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (this.file) {
          File.deleteAttachment(this.file.id).then(e => {
            this.loadFile()
          })
        }
      })
    },
    // 上传成功回调
    handleView() {
      this.dialogVisible = true
    },
    // 上传前格式和图片大小限制
    beforeAvatarUpload(file) {
      const type = file.type === 'image/jpeg' || 'image/jpg' || 'image/webp' || 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!type) {
        this.$message.error('图片格式不正确!(只能包含jpg，png，webp，jpeg)')
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
      }
      return type && isLt2M
    }
  }
}
</script>

<style >
.my-upload-head-box .avatar-uploader {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  background: url("/static/images/my-photo-header.png") no-repeat;
  background-size: 100% 100%;
}
.my-upload-head-box .avatar-uploader-icon {
  font-size: 0;
  color: #fff;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
.my-upload-head-box .avatar-uploader-icon:hover {
  font-size: 28px;
  background-color: rgba(0, 0, 0, 0.3);
}
.my-upload-head-box .avatar {
  position: relative;
  width: 100px;
  height: 100px;
  display: block;
}
.my-upload-head-box .el-upload-action {
  position: absolute;
  top: 0;
  left: 0;
  display: block;
  width: 100%;
  height: 100%;
  font-size: 0;
  color: #fff;
  text-align: center;
  line-height: 100px;
}
.my-upload-head-box .el-upload-action:hover {
  font-size: 20px;
  background-color: rgba(0, 0, 0, 0.3);
}
.my-upload-head-gallery-box .el-dialog__body {
  text-align: center;
}
</style>
