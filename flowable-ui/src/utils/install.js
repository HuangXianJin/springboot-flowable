import { checkPermission, checkViewPermission, checkOperationPermission } from './permission'
import { formatNumber, uuid } from './index'

export default {
  install(Vue, options) {
    Vue.prototype.$uuid = uuid
    Vue.prototype.$formatNumber = formatNumber
    Vue.prototype.$hasPermission = checkPermission
    Vue.prototype.$hasViewPermission = checkViewPermission
    Vue.prototype.$hasOperationPermission = checkOperationPermission
  }
}
