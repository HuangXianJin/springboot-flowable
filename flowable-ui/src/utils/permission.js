import store from '@/store'

/**
 * @param {String} value
 * @returns {Boolean}
 * @example see @/views/permission/directive.vue
 */
export function checkPermission(value) {
  if (value) {
    const perms = store.getters.menus
    return perms.some(perm => value === perm.menuCode)
  } else {
    return true
  }
}

/**
 * @returns {Boolean}
 */
export function checkViewPermission() {
  var value = this.$route.meta.code ? (this.$route.meta.code + '/view') : ''
  return checkPermission(value)
}

/**
 * @returns {Boolean}
 */
export function checkOperationPermission() {
  var value = this.$route.meta.code ? (this.$route.meta.code + '/operation') : ''
  return checkPermission(value)
}
