<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <keep-alive :include="include">
        <router-view :key="key" />
      </keep-alive>
    </transition>
  </section>
</template>

<script>
import {
  keep
} from '@/router/keepAlive'
import store from '@/store'

export default {
  name: 'AppMain',
  data() {
    return {
      include: [],
      from: [],
      to: []
    }
  },
  computed: {
    key() {
      return this.$route.path
    }
  },
  watch: {
    $route(to, from) {
      if (this.from.indexOf(to.name) > -1 && this.to.indexOf(from.name) > -1) {
        for (var i = 0; i < this.from.length; i++) {
          if (this.from[i] === to.name) {
            this.from.splice(i, 1)
            i--
          }
        }
        for (var j = 0; j < this.to.length; j++) {
          if (this.to[j] === from.name) {
            this.to.splice(j, 1)
            j--
          }
        }
      }
      this.include = []
      this.$store.getters.keep.forEach(element => {
        if (element.name === from.name && element.to.indexOf(to.name) > -1) {
          this.from.push(from.name)
          this.to.push(to.name)
        }
        if (element.name === to.name) {
          this.include.push(to.name)
        }
      })
      this.include = this.include.concat(this.from, this.to)
    }
  },
  created() {
    store.dispatch('app/setKeep', keep)
  }
}
</script>

<style scoped>
.app-main {
  /*50 = navbar  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}
.fixed-header + .app-main {
  padding-top: 50px;
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}
</style>
