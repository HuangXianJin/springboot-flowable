<!--字典组件-->
<template>
  <el-select v-model="dateValue" clearable filterable v-bind="$attrs" class="my-form-full" @change="handleChange">
    <el-option v-for="item in dictionarys" :key="item.itemValue" :label="item.itemName" :value="item.itemValue" />
  </el-select>
</template>

<script>
import SysCommon from '@/api/system/sys_common'
export default {
  name: 'Dictionary',
  components: {
  },
  props: {
    value: { type: [String, Number], default: undefined },
    dictionaryCode: { type: String, required: true } // 字典编码
  },
  data() {
    return {
      dictionarys: []
    }
  },
  computed: {
    dateValue: {
      get() {
        return this.value
      },
      set(val) {
        this.$emit('input', val)
      }
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
    initData() {
      SysCommon.getDictionaryByCode({ code: this.dictionaryCode }).then(response => {
        this.dictionarys = response.data
      })
    },
    handleChange(val) {
      this.$emit('change', val)
    }
  }

}
</script>

<style>

</style>
