import request from '@/utils/request'

/**
 * 获取列表数据
 */
const list = (params) => {
  return request({
    url: '/user/list',
    method: 'get',
    params
  })
}

export default {
  list
}
