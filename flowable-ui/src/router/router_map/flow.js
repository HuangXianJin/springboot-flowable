/* Layout */
import Layout from '@/layout'

export var flowRouter = [

  {
    path: '/flowable',
    component: Layout,
    name: 'flowable',
    redirect: 'noredirect',
    meta: {
      title: '流程管理'
    },
    children: [
      {
        path: 'modelInfo',
        name: 'modelInfo',
        component: () => import('@/views/module/flowable/flow_model_info'),
        meta: {
          title: '流程设计',
          icon: ''
        }
      },
      {
        path: 'deploy',
        name: 'deploy',
        component: () => import('@/views/module/flowable/flow_model_info/deploy'),
        hidden: true,
        meta: {
          title: '流程发布',
          icon: ''
        }
      },
      {
        path: 'bpmnDesigner',
        component: () => import('@/views/module/flowable/designer/flow_designer'),
        name: 'bpmnDesigner',
        hidden: true,
        meta: {
          title: '流程设计器'
        }
      },
      {
        path: 'form',
        component: () => import('@/views/module/flowable/flow_form'),
        name: 'form',
        meta: {
          title: '表单设计'
        }
      },
      {
        path: 'formDesigner',
        component: () => import('@/views/module/flowable/designer/form_designer'),
        name: 'formDesigner',
        hidden: true,
        meta: {
          title: '表单设计器'
        }
      },
      {
        path: 'formView',
        component: () => import('@/views/module/flowable/designer/form_view'),
        name: 'formView',
        hidden: true,
        meta: {
          title: '表单预览'
        }
      },
      {
        path: 'myProcess',
        component: () => import('@/views/module/flowable/flow_process/index'),
        name: 'myProcess',
        meta: {
          title: '我的流程'
        }
      },
      {
        path: 'createProcess',
        component: () => import('@/views/module/flowable/flow_process/create'),
        name: 'createProcess',
        meta: {
          title: '发起流程'
        }
      },
      {
        path: 'myTaskToDo',
        component: () => import('@/views/module/flowable/flow_task/todo'),
        name: 'createProcess',
        meta: {
          title: '待办任务'
        }
      },
      {
        path: 'myTaskDone',
        component: () => import('@/views/module/flowable/flow_task/done'),
        name: 'myTaskDone',
        meta: {
          title: '已办任务'
        }
      },
      {
        path: 'audit',
        component: () => import('@/views/module/flowable/flow_process/audit'),
        name: 'createProcess',
        hidden: true,
        meta: {
          title: '审批'
        }
      }
    ]
  }
]

