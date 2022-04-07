/**
 * Created by PanJiaChen on 16/11/18.
 *
 */

/**
 * Parse the time to string
 * @param {(Object|string|number)} time
 * @param {string} cFormat
 * @returns {string}
 */
export function parseTime(time, cFormat) {
  if (arguments.length === 0) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
      time = parseInt(time)
    }
    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}

/**
 * @param {number} time
 * @param {string} option
 * @returns {string}
 */
export function formatTime(time, option) {
  if (('' + time).length === 10) {
    time = parseInt(time) * 1000
  } else {
    time = +time
  }
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return (
      d.getMonth() +
      1 +
      '月' +
      d.getDate() +
      '日' +
      d.getHours() +
      '时' +
      d.getMinutes() +
      '分'
    )
  }
}

/**
 * @param {string} url
 * @returns {Object}
 */
export function param2Obj(url) {
  const search = url.split('?')[1]
  if (!search) {
    return {}
  }
  return JSON.parse(
    '{"' +
    decodeURIComponent(search)
      .replace(/"/g, '\\"')
      .replace(/&/g, '","')
      .replace(/=/g, '":"')
      .replace(/\+/g, ' ') +
    '"}'
  )
}

export function uuid() {
  var s = []
  var hexDigits = '0123456789abcdef'
  for (var i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1)
  }
  s[14] = '4' // bits 12-15 of the time_hi_and_version field to 0010
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1) // bits 6-7 of the clock_seq_hi_and_reserved to 01
  s[8] = s[13] = s[18] = s[23] = ''

  var uuid = s.join('')
  return uuid
}

export function transToMap(data, k, v) {
  var ret = {}
  for (var i = 0; i < data.length; i++) {
    ret[data[i][k]] = data[i][v]
  }
  return ret
}

export function formatNumber(val) {
  // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除，
  if (val === '' || val == null) {
    return ''
  }
  if (isNaN(val)) {
    return ''
  } else {
    return val
  }
}

export function getLastMonthDate(time) {
  time.setMonth(time.getMonth() - 1)
  var y = time.getFullYear()
  var m = time.getMonth() + 1
  var d = time.getDate()
  return y + '-' + m + '-' + d
}

/**
 * 导出数据
 * @param {Function} exportRequestFunction 导出请求方法
 * @param {String} fileName 文件名
 * @param {Object} data 请求数据
 */
export function exportData(exportRequestFunction, fileName, data) { // 导出数据
  exportRequestFunction(data).then(response => {
    const blob = response.data
    const file = response.filename.split(';')[1].split('=')[1]
    // const now = parseTime(Date.now(), '{y}{m}{d}{h}{i}{s}')
    // const filename = `${fileName}-${now}.xlsx`
    const filename = decodeURI(file)
    saveAsFile([blob], { filename })
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

export function getObjectURL(file) {
  var url = null
  if (window.createObjectURL !== undefined) {
    // basic
    url = window.createObjectURL(file)
  } else if (window.URL !== undefined) {
    if (file.raw !== undefined) {
      url = URL.createObjectURL(file.raw)
    } else {
      // mozilla(firefox)
      url = window.URL.createObjectURL(file)
    }
  } else if (window.webkitURL !== undefined) {
    // webkit or chrome
    url = window.webkitURL.createObjectURL(file)
  }
  return url
}

export function getUrlKey(name) {
  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ''])[1].replace(/\+/g, '%20')) || null// eslint-disable-line
}
