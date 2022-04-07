<template>
  <el-menu mode="horizontal">
    <el-submenu index="1">
      <template v-for="first in list">
        <el-menu-item v-if="first.children == null" :key="first.path" :index="first.path" @click="goUrl(first.path)">{{ first.menuName }}</el-menu-item>
        <el-submenu v-if="first.children != null" :key="first.path" :index="first.path">
          <template slot="title">{{ first.menuName }}</template>
          <template v-for="second in first.children">
            <el-menu-item v-if="second.children == null" :key="second.path" :index="second.path" @click="goUrl(second.path)">{{ second.menuName }}</el-menu-item>
            <el-submenu v-if="second.children != null" :key="second.path" :index="second.path">
              <template slot="title">{{ second.menuName }}</template>
              <template v-for="third in second.children">
                <el-menu-item v-if="third.children == null" :key="third.path" :index="third.path" @click="goUrl(third.path)">{{ third.menuName }}</el-menu-item>
              </template>
            </el-submenu>
          </template>
        </el-submenu>
        <!-- <sidebar-item v-if="!route.hidden" :key="route.path" :item="route" :base-path="route.path" /> -->
      </template>
    </el-submenu>
  </el-menu>
</template>

<script>

export default {
  name: 'Menu',
  components: {},
  data() {
    return {
      list: []
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
    initData() {

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
.navbar .el-submenu__title {
  padding: 0px !important;
  border-bottom: none !important;
}
.navbar .el-submenu__title,
.navbar .my-index-menu-box {
  height: 50px !important;
  line-height: 50px !important;
}
.navbar .my-index-menu-box .el-button {
  padding: 8px 0 !important;
  width: 36px !important;
  height: 36px !important;
  margin: 5px 10px !important;
}
.navbar .my-index-menu-box .el-button i {
  color: #fff !important;
  margin: 0;
}
.navbar .el-menu.el-menu--horizontal {
  border-bottom: none !important;
}
.hamburger-container {
  padding-left: 5px !important;
}
</style>
