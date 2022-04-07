import request from '@/utils/request'
import uploadAjax from '@/utils/upload'
/**
 * 工作流的流程任务的拓展表 api
 *
 * @author huangxj
 * @date 2022-03-22
 */

/**
 * 获取分页数据
 */
const getTodoTaskPage = (params) => {
  return request({
    url: '/flowable/customTask/getTodoTaskPage',
    method: 'get',
    params
  })
}

const getDoneTaskPage = (params) => {
  return request({
    url: '/flowable/customTask/getDoneTaskPage',
    method: 'get',
    params
  })
}

const page = (params) => {
  return request({
    url: '/flowable/customTask/page',
    method: 'get',
    params
  })
}

/**
 * 获取列表数据
 */
const list = (params) => {
  return request({
    url: '/flowable/customTask/list',
    method: 'get',
    params
  })
}

const getTaskListByProcessInstanceId = (params) => {
  return request({
    url: '/flowable/customTask/getTaskListByProcessInstanceId',
    method: 'get',
    params
  })
}

const getActivityListByProcessInstanceId = (params) => {
  return request({
    url: '/flowable/customTask/getActivityListByProcessInstanceId',
    method: 'get',
    params
  })
}

/**
 * 根据ID查找数据
 */
const get = (id) => {
  return request({
    url: '/flowable/customTask/detail/' + id,
    method: 'get'
  })
}

/**
 * 添加数据
 */
const create = (data) => {
  return request({
    url: '/flowable/customTask/create',
    method: 'post',
    data
  })
}

/**
 * 更新数据
 */
const update = (data) => {
  return request({
    url: '/flowable/customTask/update',
    method: 'put',
    data
  })
}

/**
 * 删除数据
 */
const remove = (id) => {
  return request({
    url: '/flowable/customTask/delete/' + id,
    method: 'delete'
  })
}

/**
 * 导出数据
 */
const exportData = params => {
  return request({
    url: '/flowable/customTask/export',
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
    url: '/flowable/customTask/import',
    method: 'post',
    data
  })
}

/**
 * 审批通过
 */
const approve = (data) => {
  return request({
    url: '/flowable/customTask/approve',
    method: 'put',
    data
  })
}

const reject = (data) => {
  return request({
    url: '/flowable/customTask/reject',
    method: 'put',
    data
  })
}

const updateAssignee = (data) => {
  return request({
    url: '/flowable/customTask/updateAssignee',
    method: 'put',
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
  getTodoTaskPage,
  getDoneTaskPage,
  getTaskListByProcessInstanceId,
  getActivityListByProcessInstanceId,
  updateAssignee,
  approve,
  reject
}
