<template>
  <div class="app-container">
    <!--条件开始-->
    <div class="my-filter-container">
      <div class="my-filter-list">
        <el-page-header content="表单" @back="onClickBack" />
      </div>
    </div>
    <!--条件结束-->
    <div style="margin-top: 30px;width: 600px;">
      <form-create
        v-model="fApi"
        :rule="rule"
        :option="option"
      />
      <el-button type="primary" @click="save()">确定</el-button>
    </div>
    <el-dialog :close-on-click-modal="false" :visible.sync="dialogVisible" append-to-body>
      <img :src="dialogImageUrl" width="100%" alt />
    </el-dialog>
  </div>
</template>

<script>

import Form from '@/api/flowable/flow_form'
import { getToken } from '@/utils/auth'

export default {
  props: {
    conf: { type: String, default: '' },
    fields: { type: String, default: '' },
    formVariablesString: { type: String, default: '' }
  },
  data() {
    return {
      fApi: null,
      rule: [],
      option: {},
      form: undefined,
      formView: {},
      dialogVisible: false,
      dialogImageUrl: '',
      formVariables: {}
    }
  },
  mounted() {
    const formId = this.$route.query && this.$route.query.formId
    if (formId) {
      Form.get(formId).then(response => {
        this.form = response.data
        this.init()
      })
    }
  },
  methods: {
    init() {
      const that = this
      if (this.conf) {
        this.option = JSON.parse(this.conf)
        this.option.submitBtn = false
      }
      if (this.form) {
        this.option = JSON.parse(this.form.conf)
        this.option.submitBtn = false
      }

      if (this.fields) {
        this.rule = JSON.parse(this.fields)
      }
      if (this.form) {
        this.rule = JSON.parse(this.form.fields)
      }
      this.rule.forEach(e => {

        if (e.type === 'upload') {
          // console.log(e)
          e.props.action = process.env.VUE_APP_BASE_API + '/system/file/upload'
          e.props.onSuccess = that.onSuccess
          e.props.onPreview = that.handlePreview
          e.props.showFileList = true
          e.props.headers = { 'Authorization': 'Bearer ' + getToken() }
        }
      })
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
    },
    onSuccess(res, file, fileList) {
      if (res.success) {
        file.url = res.data.url
      } else {
        this.$message({
          message: '上传失败',
          type: 'error'
        })
      }
    },
    save() {
      console.log(this.fApi.form)
    },
    onClickBack() {
      this.$router.back(-1)
    }
  }
}
</script>

