<template>
  <div class="app-container">
    <!--条件开始-->
    <div class="my-filter-container">
      <div class="my-filter-list">
        <el-page-header content="表单设计" @back="onClickBack" />
      </div>
    </div>
    <!--条件结束-->
    <FormDesigner
      :rule="rule"
      :option="option"
      @save="save"
    />
  </div>

</template>

<script>

import FormDesigner from '@/components/FormDesigner'
import Form from '@/api/flowable/flow_form'

export default {
  components: { FormDesigner },
  data() {
    return {
      rule: [],
      option: {},
      form: {}
    }
  },
  mounted() {
    const formId = this.$route.query && this.$route.query.id
    if (formId) {
      Form.get(formId).then(response => {
        this.form = response.data
        if (this.form.conf) {
          this.option = JSON.parse(this.form.conf)
        }
        if (this.form.fields) {
          this.rule = JSON.parse(this.form.fields)
        }
      })
    }
  },
  methods: {
    save(data) {
      // console.log(this.modeler)
      this.form.conf = data.option
      this.form.fields = data.rule
      if (this.form.id) {
        Form.update(this.form).then(ret => {
          this.$message({
            message: ret.message,
            type: 'success'
          })
          this.$router.back(-1)
        })
      }
    },
    onClickBack() {
      this.$router.back(-1)
    }
  }
}
</script>

