<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu :default-active="activeMenu" :collapse="isCollapse" :background-color="variables.menuBg" :text-color="variables.menuText" :unique-opened="true" :active-text-color="variables.menuActiveText" :collapse-transition="false" mode="vertical">
        <template v-for="route in menu">
          <sidebar-item v-if="!route.hidden" :key="route.path" :item="route" :base-path="route.path" />
        </template>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'
import { scheduleSubmenuRoute } from '@/router'

export default {
  components: { SidebarItem, Logo },
  computed: {
    menu() {
      if (this.$route.fullPath.startsWith('/schedule/')) {
        return scheduleSubmenuRoute
      }

      return this.$store.getters.addRoutes
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.$store.getters.sidebar.opened
    }
  }
}
</script>
