<template>
  <div>
    <form-create v-model="fApi" :rule="rule" :option="options" />
    <h2 v-text="disabled? '取消' : '禁用'"></h2>
    <ElButton @click="disabled1">方式1</ElButton>
    <ElButton @click="disabled2">方式2</ElButton>
    <ElButton @click="disabled3">方式3</ElButton>
  </div>
</template>

<script>
export default {
  data() {
    return {
      disabled: false,
      fApi: {},
      options: {
        onSubmit: (formData) => {
          alert(JSON.stringify(formData))
        },
        submitBtn: false,
        resetBtn: false
      },
      rule: [
        {
          type: 'input',
          field: 'input',
          title: '商品名称',
          value: '',
          props: {
            disabled: false
          }
        }
      ]
    }
  },
  computed: {
    title() {
      return this.disabled ? '商品名称(禁用)' : '商品名称'
    }
  },
  methods: {
    swatch() {
      this.disabled = !this.disabled
    },
    disabled1() {
      this.swatch()
      this.fApi.disabled(this.disabled, 'input')
      this.rule[0].title = this.title
    },
    disabled2() {
      this.swatch()
      this.fApi.updateRule('input', {
        title: this.title,
        props: {
          disabled: this.disabled
        }
      })
    },
    disabled3() {
      this.swatch()
      const rule = this.fApi.getRule('input')
      rule.title = this.title
      rule.props.disabled = this.disabled
    }
  }
}
</script>
