<template>
  <div class="app-container">
    <div v-if="formId" class="my-filter-container">
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
        :value.sync="formVariables"
      />
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
      formVariables: {},
      formId: ''
    }
  },
  watch: {
    conf: {
      handler(newValue, oldValue) {
        this.initOption()
      },
      deep: true
    },
    fields: {
      handler(newValue, oldValue) {
        this.initRule()
      },
      deep: true
    },
    formVariablesString: {
      handler(newValue, oldValue) {
        this.formVariables = JSON.parse(newValue)
        this.initRule()
      },
      deep: true
    }
  },
  mounted() {
    this.formId = this.$route.query && this.$route.query.formId
    if (this.formId) {
      Form.get(this.formId).then(response => {
        this.form = response.data
        this.initOption()
        this.initRule()
      })
    }
  },
  methods: {
    initOption() {
      if (this.conf) {
        this.option = JSON.parse(this.conf)
        this.option.submitBtn = false
      }
      if (this.form) {
        this.option = JSON.parse(this.form.conf)
        this.option.submitBtn = false
      }
    },
    initRule() {
      const that = this
      if (this.fields) {
        this.rule = JSON.parse(this.fields)
      }
      if (this.form) {
        this.rule = JSON.parse(this.form.fields)
      }
      this.rule.forEach(e => {
        if (this.formVariablesString) {
          if (e.props) {
            e.props.disabled = true
          }
          if (!e.props) {
            e.props = { disabled: true }
          }
        }
        if (e.type === 'upload') {
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
      var ret = false
      this.fApi.validate(async(valid, fail) => {
        if (valid) {
          ret = this.fApi.form
        } else {
          return false
        }
      })
      return ret
    },
    onClickBack() {
      this.$router.back(-1)
    }
  }
}
</script>

