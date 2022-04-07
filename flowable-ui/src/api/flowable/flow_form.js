import request from '@/utils/request'
import uploadAjax from '@/utils/upload'
/**
 * 自定义表单 api
 *
 * @author huangxj
 * @date 2022-03-16
 */

/**
 * 获取分页数据
 */
const page = (params) => {
  return request({
    url: '/flowable/form/page',
    method: 'get',
    params
  })
}

/**
 * 获取列表数据
 */
const list = (params) => {
  return request({
    url: '/flowable/form/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID查找数据
 */
const get = (id) => {
  return request({
    url: '/flowable/form/detail/' + id,
    method: 'get'
  })
}

/**
 * 添加数据
 */
const create = (data) => {
  return request({
    url: '/flowable/form/create',
    method: 'post',
    data
  })
}

/**
 * 更新数据
 */
const update = (data) => {
  return request({
    url: '/flowable/form/update',
    method: 'put',
    data
  })
}

/**
 * 删除数据
 */
const remove = (id) => {
  return request({
    url: '/flowable/form/delete/' + id,
    method: 'delete'
  })
}

/**
 * 导出数据
 */
const exportData = params => {
  return request({
    url: '/flowable/form/export',
    method: 'get',
    params: params,
    responseType: 'arraybuffer'
  })
}

/**
 * 导入数据
 */
const importData = data => {
  return uploadAjax({
    url: '/flowable/form/import',
    method: 'post',
    data
  })
}

export default {
  list,
  page,
  get,
  create,
  update,
  remove,
  exportData,
  importData
}
