<template>
  <div class="app-container my-app-container my-summary-menu-page">
    <!--主体开始-->
    <div class="my-layout-height" style="height: calc(100vh - 90px);">
      <el-scrollbar class="my-scroll my-scroll-column">
        <div class="my-scroll-padding-right">
          <el-timeline>
            <el-timeline-item v-for="(item,index) in list" :key="index" :timestamp="item.menuName" placement="top">
              <el-card>
                <span v-for="(smallItem,smallIndex) in item.children" :key="smallIndex">
                  <a v-if="smallItem.children == null" div class="my-menu-first" @click="goUrl(smallItem.path)">{{ smallItem.menuName }}</a>
                  <div v-if="smallItem.children != null">
                    <div class="my-menu-second">{{ smallItem.menuName }}</div>
                    <a v-for="(miniItem,miniIndex) in smallItem.children" :key="miniIndex" class="my-menu-third" @click="goUrl(miniItem.path)">{{ miniItem.menuName }}</a>
                  </div>
                </span>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-scrollbar>
    </div>
    <!--主体结束-->
  </div>
</template>

<script>
import Menu from '@/api/system/sys_authority'

export default {
  name: 'Summary',
  components: {},
  data() {
    return {
      list: undefined
    }
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      Menu.userMenu(this.$store.getters.userId, { tree: true }).then(response => {
        response.data.forEach(element => {
          if (element.menuCode === '后台管理系统') {
            this.list = element.children
          }
        })
      })
    },
    goUrl(url) {
      // var addr = location.origin + location.pathname + "#" + url
      // location.href = addr
      // location.reload()
      this.$router.push({
        path: url
      })
    }
  }
}
</script>

<style>
.my-summary-menu-page .el-card__body {
  padding-bottom: 0;
}
.my-summary-menu-page .my-menu-first,
.my-menu-third {
  margin-right: 20px;
  margin-bottom: 20px;
  display: inline-block;
  font-size: 16px;
  color: #333;
  font-weight: 500;
}
.my-summary-menu-page .my-menu-first:hover,
.my-menu-third:hover {
  text-decoration: underline;
  color: #409eff;
}
.my-summary-menu-page .my-menu-second {
  color: #999;
  font-size: 14px;
  margin-bottom: 10px;
}
</style>
