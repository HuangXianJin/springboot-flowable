import axios from 'axios'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // api 的 base_url
  withCredentials: false,
  timeout: 1e3 * 60 * 5 // 请求超时时间
})

export function downloadFile(option) {
  return service({
    responseType: 'blob',
    ...option
  })
}

/**
 * saveAsFile - saveAsFile([data], { filename: 'filename' })
 * @param {Array<*>} blobParts
 * @param {object} [option]
 */
export function saveAsFile(blobParts, option) {
  const blob = new Blob(blobParts)

  if (window.navigator.msSaveBlob) {
    // IE version
    window.navigator.msSaveBlob(blob, option.filename)
  } else {
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = option.filename
    link.click()
  }
}

export default function progressDownLoad(option) {
  var xhr = new XMLHttpRequest()
  xhr.open('GET', option.url, true)
  // 监听进度事件
  xhr.addEventListener('progress', function(evt) {
    if (option.onProgress) {
      try { option.onProgress.call(evt) } catch (e) {
        console.log(e)
      }
    }
  }, false)

  xhr.responseType = 'blob'
  xhr.onreadystatechange = function() {
    if (!(xhr.readyState === 4 && xhr.status === 200)) {
      return
    }

    const blob = new Blob([xhr.response])

    if (typeof window.chrome !== 'undefined') {
      // Chrome version
      var link = document.createElement('a')
      link.href = window.URL.createObjectURL(xhr.response)
      link.download = option.filename
      link.click()
    } else if (typeof window.navigator.msSaveBlob !== 'undefined') {
      // IE version
      window.navigator.msSaveBlob(blob, option.filename)
    } else {
      // Firefox version
      var file = new File([xhr.response], option.filename, { type: 'application/force-download' })
      window.open(URL.createObjectURL(file))
    }
    if (option.onSuccess) {
      try {
        option.onSuccess.call(xhr)
      } catch (e) {
        console.log(e)
      }
    }
  }
  var paramsStr = ''
  if (option.params) for (var key in option.params) paramsStr += '&' + key + '=' + option.params[key]
  if (paramsStr) paramsStr = paramsStr.substring(1)

  xhr.send(paramsStr)
}

