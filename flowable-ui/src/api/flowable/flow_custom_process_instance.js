import request from '@/utils/request'
import uploadAjax from '@/utils/upload'
/**
 * 流程实例 api
 *
 * @author huangxj
 * @date 2022-03-22
 */

/**
 * 获取分页数据
 */
const page = (params) => {
  return request({
    url: '/flowable/customProcessInstance/page',
    method: 'get',
    params
  })
}

/**
 * 获取列表数据
 */
const list = (params) => {
  return request({
    url: '/flowable/customProcessInstance/list',
    method: 'get',
    params
  })
}

const getByProcessInstanceId = (params) => {
  return request({
    url: '/flowable/customProcessInstance/getByProcessInstanceId',
    method: 'get',
    params
  })
}

/**
 * 根据ID查找数据
 */
const get = (id) => {
  return request({
    url: '/flowable/customProcessInstance/detail/' + id,
    method: 'get'
  })
}

/**
 * 添加数据
 */
const create = (data) => {
  return request({
    url: '/flowable/customProcessInstance/create',
    method: 'post',
    data
  })
}

/**
 * 添加数据
 */
const cancel = (data) => {
  return request({
    url: '/flowable/customProcessInstance/cancel',
    method: 'post',
    data
  })
}

/**
 * 更新数据
 */
const update = (data) => {
  return request({
    url: '/flowable/customProcessInstance/update',
    method: 'put',
    data
  })
}

/**
 * 删除数据
 */
const remove = (id) => {
  return request({
    url: '/flowable/customProcessInstance/delete/' + id,
    method: 'delete'
  })
}

/**
 * 导出数据
 */
const exportData = params => {
  return request({
    url: '/flowable/customProcessInstance/export',
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
    url: '/flowable/customProcessInstance/import',
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
  importData,
  cancel,
  getByProcessInstanceId
}
