import Vue from 'vue'
import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import formCreate from '@form-create/element-ui'
import FcDesigner from '@form-create/designer'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import '@/styles/index.scss' // global css
import '@/assets/icon/iconfont.css'

import '@/styles/common.scss'

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission'
import Plugin from '@/utils/install'

// import components from '@/components/install'
import api from '@/api/install'

import './theme/index.scss'

// flow
import 'bpmn-js/dist/assets/diagram-js.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css'

import { vuePlugin } from '@/components/Highlight'
import 'highlight.js/styles/atom-one-dark-reasonable.css'
Vue.use(vuePlugin)

// Vue.use(components)
Vue.use(api)
// set ElementUI lang to EN
Vue.use(ElementUI, {
  size: 'small '
})
Vue.use(formCreate)
Vue.use(FcDesigner)

Vue.use(Plugin)
Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})

