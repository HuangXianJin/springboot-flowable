const req = require.context('@/api', true, /\.js$/)

var api = {
}

req.keys().forEach(key => {
  if (key === './install.js') return
  var str = key.substring(key.lastIndexOf('/') + 1, key.lastIndexOf('.js'))
  api[str] = req(key).default
})

export default {
  install(Vue, options) {
    Vue.prototype.$api = api
  }
}
