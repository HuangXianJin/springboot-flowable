<template>
  <div class="my-nothing-box">
    <div class="my-nothing-box-con">
      <div class="my-mess-title">
        <img src="/static/images/my-nothing.png" />
      </div>
      <div class="my-mess-name">{{ value }}</div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'Nothing',
  components: {
  },
  props: {
    value: { type: String, default: '暂无数据' }
  },
  data() {
    return {
      temp: {}
    }
  },
  computed: {
    uploadDialogFormVisible: {
      get() {
        return this.value
      },
      set(val) {
        this.$emit('input', val)
      }
    }
  },
  methods: {
  }

}
</script>

<style scoped>
.my-nothing-box {
  display: table;
  width: 100%;
  height: 100%;
}
.my-nothing-box .my-nothing-box-con {
  display: table-cell;
  vertical-align: middle;
  text-align: center;
}
.my-nothing-box .my-mess-name {
  font-size: 16px;
  color: #8a8a8a;
  font-weight: bold;
  margin: 10px auto;
  }
</style>
