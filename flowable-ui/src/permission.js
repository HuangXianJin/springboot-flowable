import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()
  // set page title
  document.title = getPageTitle(to.meta.title)
  if (store.getters.userId) {
    next()
  } else {
    try {
      await store.dispatch('user/getInfo')
      const accessRoutes = await store.dispatch('permission/generateRoutes', [])
      router.addRoutes(accessRoutes)

      next({ ...to, replace: true })
    } catch (error) {
      // remove token and go to login page to re-login
      await store.dispatch('user/resetToken')
      Message.error(error || 'Has Error')
      next(`/login?redirect=${to.path}`)
    }
  }
})

router.afterEach((to, from) => {
  // finish progress bar
  NProgress.done()
})

