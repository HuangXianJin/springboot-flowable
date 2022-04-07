/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
}

/* 电话校验 */
var validPhone = (rule, value, callback) => {
  const reg = /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/
  if (!value) {
    callback(new Error('手机号码不能为空'))
  } else if (!reg.test(value)) {
    callback(new Error('请输入11位有效手机号码'))
  } else {
    callback()
  }
}

export default validPhone
