import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import { flowRouter } from './router_map/flow'

export const constantRoutes = [
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '/demo',
    component: () => import('@/views/demo/index'),
    hidden: true
  }
]

export const homeRouterMap = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    hiddenOnOneChild: true,
    name: '首页菜单',
    children: [{
      path: 'dashboard',
      name: '首页',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'sidebar-index' }
    }]
  },
  { path: '*', redirect: '/404', hidden: true }
]

export var asyncRoutes = [].concat(
  homeRouterMap,
  flowRouter
)

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()
// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
