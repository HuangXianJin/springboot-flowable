<template>
  <el-date-picker
    v-model="dateValue"
    :picker-options="pickerOptions"
    :value-format="format"
    :readonly="readonly"
    :disabled="disabled"
    type="daterange"
    align="left"
    unlink-panels
    range-separator="~"
    start-placeholder="开始日期"
    end-placeholder="结束日期"
    @change="selectDate"
  />
</template>
<script>
import { parseTime, getLastMonthDate } from '@/utils'

export default {
  props: {
    disabled: { type: Boolean, default: false },
    readonly: { type: Boolean, default: false },
    value: { type: Array, default: undefined },
    format: { type: String, default: undefined }
  },
  data() {
    return {
      date: [],
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      }
    }
  },
  computed: {
    dateValue: {
      get() {
        if (this.value) {
          return this.value
        } else {
          return this.date
        }
      },
      set(val) {
        if (!val) {
          val = this.date.slice()
        }
        this.$emit('input', val)
      }
    }
  },
  methods: {
    formatTime(time, cFormat) {
      return parseTime(time, cFormat)
    },
    lastMonthDate(time) {
      return getLastMonthDate(time)
    },
    selectDate(dateValue) {
      this.$emit('pick', dateValue)
    }
  }
}
</script>
