import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '../store'
import { getToken } from '@/utils/auth'
import { Loading } from 'element-ui'

var loading
// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // api 的 base_url
  withCredentials: false
  // timeout: 5000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(
  config => {
    const token = getToken()
    const auth = config.auth
    if (auth !== false && token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    if (config.method === 'get' || config.method === 'post') {
      config.params = {
        _t: Date.parse(new Date()) / 1000,
        ...config.params
      }
    }

    if (config.method === 'put' || config.method === 'post' || config.url.indexOf('export') !== -1 || config.url.indexOf('import') !== -1) {
      loading = Loading.service({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    }
    return config
  },
  error => {
    Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    var config = response.config
    if (config.method === 'put' || config.method === 'post' || config.url.indexOf('export') !== -1 || config.url.indexOf('import') !== -1) {
      if (store.getters.autoLoading === true) {
        loading.close()
      }
    }

    const contentType = response.headers['content-type']

    if (contentType && contentType.startsWith('application/force-download') && response.headers['content-disposition']) {
      const temp = {}
      temp.data = response.data
      temp.filename = response.headers['content-disposition']
      return temp
    }

    if (contentType && contentType.startsWith('application/json')) {
      const res = response.data
      if (res.success) {
        return response.data
      } else {
        if (response.config.responseType === 'arraybuffer') {
          ab2str(res)
        } else {
          console.log(res)
          Message({
            message: res.message,
            type: 'error',
            duration: 5 * 1000
          })

          if (res.code === 2012 || res.code === 2000) {
            MessageBox.confirm(
              '认证失败,请重新登录!',
              '确定登出',
              {
                confirmButtonText: '重新登录',
                cancelButtonText: '取消',
                type: 'warning'
              }
            ).then(() => {
              store.dispatch('user/logout').then(() => {
                location.reload() // 为了重新实例化vue-router对象 避免bug
              })
            })
          }
        }
        return Promise.reject(res.message)
      }
    }
    return response.data
  },
  error => {
    if (loading) { loading.close() }
    let message = '连接服务器失败'
    if (error && error.response) {
      switch (error.response.status) {
        case 502:
          message = '连接服务器失败'
          break
        case 429:
          message = '访问太过频繁，请稍后再试!'
          break
        default:
          message = error.response.data.path + error.response.data.message ? error.response.data.message : '服务器错误'
          break
      }
    }
    Message({
      message: message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

// ArrayBuffer转字符串
function ab2str(array) {
  var blob = new Blob([array])
  // 将 Blob对象 读成字符串
  var reader = new FileReader()
  reader.readAsText(blob, 'utf-8')
  reader.onload = function(e) {
    Message({
      message: JSON.parse(reader.result).message,
      type: 'error',
      duration: 5 * 1000
    })
  }
}
export default service
