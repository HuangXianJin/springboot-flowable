
<template>
  <div class="browser">
    <div class="browser_con">
      <img src="/static/images/icon/icon_browser.png" />
      <div class="tips">
        <p>您正在使用的浏览器版本过低，将不能正常浏览</p>
        <p>请升级IE10版本以上，或更换浏览器</p>
      </div>
      <div class="type">
        <el-row :gutter="20">
          <el-col :span="12">
            <a href="https://support.microsoft.com/zh-cn/help/17621/internet-explorer-downloads">
              <p>

                <img src="/static/images/icon//icon_browser_ie.png" />
              </p>
              <p>升级IE浏览器</p>
            </a>
          </el-col>
          <el-col :span="12">
            <a href="https://www.google.cn/chrome/">
              <p>
                <img src="/static/images/icon/icon_browser_google.png" />
              </p>
              <p>使用谷歌浏览器</p>
            </a>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {}
  },
  methods: {}
}
</script>

<style lang="scss" scoped>
.browser {
  position: relative;
  width: 100%;
  height: 100%;
  .browser_con {
    width: 600px;
    height: 400px;
    position: absolute;
    left: 50%;
    top: 50%;
    margin: -200px 0 0 -300px;
    text-align: center;
    .tips {
      margin: 30px 0 40px 0;
      p {
        font-size: 16px;
        font-weight: bold;
        line-height: 22px;
        margin: 5px 0;
        color: #666;
      }
    }
    .type {
      width: 80%;
      margin: 0 auto;
      text-align: center;
      p {
        color: #999;
        margin: 0 0 5px 0;
        font-size: 13px;
        cursor: pointer
      }
      img {
        width: 60px;
        height: 60px;
        margin-bottom: 5px;
      }
    }
  }
}
</style>
