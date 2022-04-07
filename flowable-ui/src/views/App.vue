<template>
  <div id="app" :class="$store.getters.theme.themeClass">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      themeClass: ''
    }
  },
  created() {
  },
  methods: {

  }
}
</script>
