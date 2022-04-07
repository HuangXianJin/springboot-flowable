<template>
  <div class="my-wrong-box">
    <div class="my-wrong-box-con">
      <div class="my-mess-title">
        <img src="/static/images/my-wrong-401.png" />
      </div>
      <div class="my-mess-name">抱歉，您的权限不足</div>
      <div class="my-mess-desc">
        <el-button round @click="back">返回上一级</el-button>
        <el-button type="primary" round @click="backIndex">返回首页</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Page401',
  data() {
    return {
    }
  },
  methods: {
    back() {
      this.$router.go(-1)
    },
    backIndex() {
      this.$router.push({ path: '/dashboard' })
    }
  }
}
</script>

<style scoped>
.my-wrong-box {
  display: table;
  width: 100%;
  height: 100%;
}
.my-wrong-box .my-wrong-box-con {
  display: table-cell;
  vertical-align: middle;
  text-align: center;
}
.my-wrong-box .my-mess-name {
  font-size: 16px;
  color: #333;
  font-weight: bold;
  margin: 10px auto;
}
.my-wrong-box .my-mess-desc {
  margin-top: 20px;
}
.my-wrong-box .my-mess-desc .el-button {
  font-size: 14px;
  padding: 10px 20px;
}
</style>
