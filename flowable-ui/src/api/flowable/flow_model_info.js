import request from '@/utils/request'
import uploadAjax from '@/utils/upload'
/**
 * Bpm 流程定义表
 api
 *
 * @author huangxj
 * @date 2022-03-11
 */

/**
 * 获取分页数据
 */
const page = (params) => {
  return request({
    url: '/flowable/modelInfo/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查找数据
 */
const get = (id) => {
  return request({
    url: '/flowable/modelInfo/detail/' + id,
    method: 'get'
  })
}

/**
 * 获取列表数据
 */
const list = (params) => {
  return request({
    url: '/flowable/modelInfo/list',
    method: 'get',
    params
  })
}

/**
 * 获得流程定义的XML
 */
const getBpmnXml = (params) => {
  return request({
    url: '/flowable/modelInfo/getBpmnXml',
    method: 'get',
    params
  })
}

/**
 * 获得流程定义列表
 */
const getProcessDefinitionList = (params) => {
  return request({
    url: '/flowable/modelInfo/getProcessDefinitionList',
    method: 'get',
    params
  })
}

/**
 * 根据ID查找数据
 */
const deploy = (params) => {
  return request({
    url: '/flowable/modelInfo/deploy',
    method: 'post',
    params
  })
}

/**
 * 添加数据
 */
const create = (data) => {
  return request({
    url: '/flowable/modelInfo/create',
    method: 'post',
    data
  })
}

/**
 * 更新数据
 */
const update = (data) => {
  return request({
    url: '/flowable/modelInfo/update',
    method: 'put',
    data
  })
}

/**
 * 删除数据
 */
const remove = (id) => {
  return request({
    url: '/flowable/modelInfo/delete/' + id,
    method: 'delete'
  })
}

/**
 * 导出数据
 */
const exportData = params => {
  return request({
    url: '/flowable/modelInfo/export',
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
    url: '/flowable/modelInfo/import',
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
  getProcessDefinitionList,
  getBpmnXml,
  deploy
}
