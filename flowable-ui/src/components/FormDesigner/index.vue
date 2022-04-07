<template>

  <div id="form-designer" class="app-container">
    <div class="_fc-t-header">
      <!-- <img class="_fc-t-logo" src="http://form-create.com/logo.png" /> -->
      <!-- <div class="_fc-t-name">form-create-designer</div> -->
      <div class="_fc-t-menu" style="flow: rgiht">
        <el-button size="mini" icon="fc-icon icon-import" @click="save">保存</el-button>
        <!-- <el-button size="mini" icon="fc-icon icon-import" @click="setJson"> 导入JSON</el-button> -->
        <el-button size="mini" type="primary" @click="showJson">预览JSON</el-button>
        <el-button size="mini" type="success" @click="showOption">预览Options</el-button>
        <el-button size="mini" type="danger" @click="showTemplate">预览组件</el-button>
      </div>
    </div>
    <fc-designer ref="designer" />

    <el-dialog :title="title[type]" :visible.sync="state" class="_fc-t-dialog">
      <div v-if="state" ref="editor">
        <highlightjs language="json" :code="previewResult" />
      </div>
      <span v-if="err" style="color: red;">输入内容格式有误!</span>
      <span v-if="type > 2" slot="footer" class="dialog-footer">
        <el-button size="small" @click="state = false">取 消</el-button>
        <el-button type="primary" size="small" @click="onOk">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import is from '@form-create/utils/lib/type'
import formCreate from '@form-create/element-ui'
import 'element-ui/lib/theme-chalk/index.css'
const TITLE = ['生成规则', '表单规则', '生成组件', '设置生成规则', '设置表单规则']
export default {
  name: 'Demo',
  props: {
    rule: { type: Array, default: () => [] },
    option: { type: Object, default: () => {} }
  },
  data() {
    return {
      state: false,
      value: null,
      title: TITLE,
      editor: null,
      err: false,
      type: -1,
      previewResult: ''
    }
  },
  watch: {
    rule() {
      this.setJson()
    },
    option() {
      this.setOption()
    },
    state(n) {
      if (!n) {
        this.value = null
        this.err = false
      }
    },
    value() {
      this.load()
    }
  },
  beforeCreate() {
  },
  mounted() {
    this.$refs.designer.setOption({
      form: {
        labelPosition: 'right',
        size: 'small',
        labelWidth: '125px',
        hideRequiredAsterisk: false,
        showMessage: true,
        inlineMessage: false
      }
    })
  },
  methods: {
    load() {
      let val
      if (this.type === 2) {
        val = this.value
      } else if (this.type === 0) {
        val = formCreate.toJson(this.value, 2)
      } else {
        val = JSON.stringify(this.value, null, 2)
      }
      this.previewResult = val
    },
    onValidationError(e) {
      this.err = e.length !== 0
    },
    showJson() {
      this.state = true
      this.type = 0
      this.value = this.$refs.designer.getRule()
    },
    showOption() {
      this.state = true
      this.type = 1
      this.value = this.$refs.designer.getOption()
    },
    showTemplate() {
      this.state = true
      this.type = 2
      this.value = this.makeTemplate()
    },
    setJson() {
      this.$refs.designer.setRule(this.rule)
    },
    setOption() {
      this.$refs.designer.setOption(this.option)
    },
    save() {
      const option = JSON.stringify(this.$refs.designer.getOption(), null, 2)
      const rule = JSON.stringify(this.$refs.designer.getRule(), null, 2)
      // 触发 save 事件
      this.$emit('save', { option, rule })
    },
    onOk() {
      if (this.err) return
      const json = this.editor.getValue()
      const val = JSON.parse(json)
      if (this.type === 3) {
        if (!Array.isArray(val)) {
          this.err = true
          return
        }
        this.$refs.designer.setRule(formCreate.parseJson(json))
      } else {
        if (!is.Object(val) || !val.form) {
          this.err = true
          return
        }
        this.$refs.designer.setOption(val)
      }
      this.state = false
    },
    makeTemplate() {
      const rule = this.$refs.designer.getRule()
      const opt = this.$refs.designer.getOption()
      return `<template>
  <form-create
    v-model="fapi"
    :rule="rule"
    :option="option"
    @submit="onSubmit"
  ></form-create>
</template>
<script>
import formCreate from "@form-create/element-ui";
export default {
  data () {
    return {
        fapi: null,
        rule: formCreate.parseJson('${formCreate.toJson(rule).replaceAll('\\', '\\\\')}'),
        option: formCreate.parseJson('${JSON.stringify(opt)}')
    }
  },
  methods: {
    onSubmit (formData) {
      //todo 提交表单
    }
  }
}
<\/script>`
    }
  }
}
</script>

<style lang="scss">
    ._fc-t-header {
        height: 60px;
        margin: 0 20px;
        position: relative;
        display: flex;
        align-items: center;
        cursor: default;
    }

    ._fc-t-logo {
        height: 26px;
    }

    ._fc-t-name {
        display: inline-block;
        color: rgba(0, 0, 0, 0.8);
        font-size: 20px;
        font-weight: 600;
        margin-left: 5px;
    }

    ._fc-t-menu {
        position: absolute;
        right: 0;
    }

    ._fc-t-menu i {
        font-size: 12px;
    }
    #form-designer {
        min-height: 100vh;
        padding: 0;
        margin: 0;
        display: flex !important;
        flex-direction: column !important;
        .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
            margin-bottom: 18px;
        }
        .el-input-number--mini {
            width: 130px;
            line-height: 26px;
        }
    }
    #form-designer {
        display: flex;
        flex-direction: column;
        flex: 1;
    }
</style>
