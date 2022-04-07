<template>
  <div class="my-process-designer">
    <div class="my-process-designer__container">
      <div ref="bpmn-canvas" class="my-process-designer__canvas"></div>
    </div>
  </div>
</template>

<script>
import BpmnViewer from 'bpmn-js/lib/Viewer'
import DefaultEmptyXML from './plugins/defaultEmpty'
import User from '@/api/system/sys_user'
import { transToMap } from '@/utils'

export default {
  name: 'MyProcessViewer',
  componentName: 'MyProcessViewer',
  props: {
    value: { // BPMN XML 字符串
      type: String
    },
    prefix: { // 使用哪个引擎
      type: String,
      default: 'flowable'
    },
    activityData: { // 活动的数据。传递时，可高亮流程
      type: Array,
      default: () => []
    },
    processInstanceData: { // 流程实例的数据。传递时，可展示流程发起人等信息
      type: Object
    },
    taskData: { // 任务实例的数据。传递时，可展示 UserTask 审核相关的信息
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      xml: '',
      activityList: [],
      processInstance: undefined,
      taskList: [],
      userData: []
    }
  },
  watch: {
    value: function(newValue) { // 在 xmlString 发生变化时，重新创建，从而绘制流程图
      this.xml = newValue
      this.createNewDiagram(this.xml)
    },
    activityData: function(newActivityData) {
      this.activityList = newActivityData
      this.createNewDiagram(this.xml)
    },
    processInstanceData: function(newProcessInstanceData) {
      this.processInstance = newProcessInstanceData
      this.createNewDiagram(this.xml)
    },
    taskData: function(newTaskListData) {
      this.taskList = newTaskListData
      this.createNewDiagram(this.xml)
    }
  },
  created() {
    User.list({ listMode: true }).then(ret => {
      this.userData = transToMap(ret.data, 'userId', 'userName')
    })
  },
  mounted() {
    this.xml = this.value
    this.activityList = this.activityData
    // 初始化
    this.initBpmnModeler()
    this.createNewDiagram(this.xml)
    this.$once('hook:beforeDestroy', () => {
      if (this.bpmnModeler) this.bpmnModeler.destroy()
      this.$emit('destroy', this.bpmnModeler)
      this.bpmnModeler = null
    })
    // 初始模型的监听器
    this.initModelListeners()
  },
  methods: {
    initBpmnModeler() {
      if (this.bpmnModeler) return
      this.bpmnModeler = new BpmnViewer({
        container: this.$refs['bpmn-canvas'],
        bpmnRenderer: {
        }
      })
      this.$emit('init-finished', this.bpmnModeler)
    },
    /* 创建新的流程图 */
    async createNewDiagram(xml) {
      // 将字符串转换成图显示出来
      const newId = `Process_${new Date().getTime()}`
      const newName = `业务流程_${new Date().getTime()}`
      const xmlString = xml || DefaultEmptyXML(newId, newName, this.prefix)
      try {
        // console.log(this.bpmnModeler.importXML);
        const { warnings } = await this.bpmnModeler.importXML(xmlString)
        if (warnings && warnings.length) {
          warnings.forEach(warn => console.warn(warn))
        }
        // 高亮流程图
        await this.highlightDiagram()
      } catch (e) {
        console.error(e)
        // console.error(`[Process Designer Warn]: ${e?.message || e}`);
      }
    },
    /* 高亮流程图 */
    // TODO 芋艿：如果多个 endActivity 的话，目前的逻辑可能有一定的问题。https://www.jdon.com/workflow/multi-events.html
    async highlightDiagram() {
      const activityList = this.activityList
      if (activityList.length === 0) {
        return
      }
      // 参考自 https://gitee.com/tony2y/RuoYi-flowable/blob/master/ruoyi-ui/src/components/Process/index.vue#L222 实现
      // 再次基础上，增加不同审批结果的颜色等等
      const canvas = this.bpmnModeler.get('canvas')
      if (!this.bpmnModeler.getDefinitions() || !this.bpmnModeler.getDefinitions().rootElements[0].flowElements) {
        return
      }

      this.bpmnModeler.getDefinitions().rootElements[0].flowElements.forEach(n => {
        const out = []
        // out.push(n)
        console.log(n)
        console.log(out)
        const activity = activityList.find(m => m.key === n.id) // 找到对应的活动
        if (n.$type === 'bpmn:UserTask') { // 用户任务
          if (!activity) {
            return
          }
          // 处理用户任务的高亮
          const task = this.taskList.find(m => m.taskId === activity.taskId) // 找到活动对应的 taskId

          if (task) {
            canvas.addMarker(n.id, this.getResultCss(task.result))
          }
        } else if (n.$type === 'bpmn:ExclusiveGateway') { // 排它网关
          if (!activity) {
            return
          }
          // 设置【bpmn:ExclusiveGateway】排它网关的高亮
          canvas.addMarker(n.id, this.getActivityHighlightCss(activity))
        } else if (n.$type === 'bpmn:ParallelGateway') { // 并行网关
          if (!activity) {
            return
          }
          // 设置【bpmn:ParallelGateway】并行网关的高亮
          canvas.addMarker(n.id, this.getActivityHighlightCss(activity))
        } else if (n.$type === 'bpmn:StartEvent') { // 开始节点
          if (!activity) {
            return
          }
          canvas.addMarker(n.id, this.getActivityHighlightCss(activity))
        } else if (n.$type === 'bpmn:EndEvent') { // 结束节点
          if (!this.processInstance || this.processInstance.result === '处理中') {
            return
          }
          canvas.addMarker(n.id, this.getResultCss(this.processInstance.result))
        } else {
          if (!activity) {
            return
          }
          canvas.addMarker(n.id, this.getActivityHighlightCss(activity))
        }
      })
    },
    getActivityHighlightCss(activity) {
      return activity.endTime ? 'highlight' : 'highlight-todo'
    },
    getResultCss(result) {
      if (result === '处理中') {
        return 'highlight-todo'
      } else if (result === '通过') {
        return 'highlight'
      } else if (result === '未通过') {
        return 'highlight-reject'
      } else if (result === '已取消') {
        return 'highlight-cancel'
      }
      return ''
    },
    getActivityOutgoing(activity) {
      // 如果有 outgoing，则直接使用它
      if (activity.outgoing && activity.outgoing.length > 0) {
        return activity.outgoing
      }
      if (!this.bpmnModeler.getDefinitions().rootElements[0].flowElements) {
        return
      }
      // 如果没有，则遍历获得起点为它的【bpmn:SequenceFlow】节点们。原因是：bpmn-js 的 UserTask 拿不到 outgoing
      const flowElements = this.bpmnModeler.getDefinitions().rootElements[0].flowElements
      const outgoing = []
      flowElements.forEach(item => {
        if (item.$type !== 'bpmn:SequenceFlow') {
          return
        }
        if (item.sourceRef.id === activity.key) {
          outgoing.push(item)
        }
      })
      return outgoing
    },
    initModelListeners() {
      const EventBus = this.bpmnModeler.get('eventBus')
      const that = this
      // 注册需要的监听事件
      EventBus.on('element.hover', function(eventObj) {
        const element = eventObj ? eventObj.element : null
        that.elementHover(element)
      })
      EventBus.on('element.out', function(eventObj) {
        const element = eventObj ? eventObj.element : null
        that.elementOut(element)
      })
    },
    // 流程图的元素被 hover
    elementHover(element) {
      this.element = element
      !this.elementOverlayIds && (this.elementOverlayIds = {})
      !this.overlays && (this.overlays = this.bpmnModeler.get('overlays'))
      // 展示信息
      if (!this.elementOverlayIds[element.id] && element.type !== 'bpmn:Process') {
        let html = `<div class="element-overlays">
            <p>Elemet id: ${element.id}</p>
            <p>Elemet type: ${element.type}</p>
          </div>` // 默认值
        if (element.type === 'bpmn:StartEvent' && this.processInstance) {
          html = `<div>
                  <p>发起人：${this.processInstance.startUserName}</p>
                  <p>创建时间：${this.processInstance.createTime}</p>
                  </div>
                  `
        } else if (element.type === 'bpmn:UserTask') {
          const assignee = element.businessObject.$attrs['flowable:assignee']
          // debugger
          const activity = this.activityList.find(m => m.key === element.id)
          // if (!activity) {
          //   return
          // }
          var task = {}
          if (activity) {
            task = this.taskList.find(m => m.taskId === activity.taskId) // 找到活动对应的 taskId
          }

          // if (!task) {
          //   return
          // }
          html = `<p>审批人：${this.userData[assignee]}</p>`
          if (task.taskCreateTime) {
            html += `<p>创建时间：${task.taskCreateTime}</p>`
          }
          if (task.result) {
            html += `<p>结果：${task.result}</p>`
          }
          if (task.endTime) {
            html += `<p>结束时间：${task.endTime}</p>`
          }
          if (task.comment) {
            html += `<p>审批建议：${task.comment}</p>`
          }
        } else if (element.type === 'bpmn:EndEvent' && this.processInstance) {
          html = `<p>结果：${this.processInstance.result}</p>`
          if (this.processInstance.endTime) {
            html += `<p>结束时间：${this.processInstance.endTime}</p>`
          }
        }
        this.elementOverlayIds[element.id] = this.overlays.add(element, {
          position: { left: 0, bottom: 0 },
          html: `<div class="element-overlays">${html}</div>`
        })
      }
    },
    // 流程图的元素被 out
    elementOut(element) {
      this.overlays.remove({ element })
      this.elementOverlayIds[element.id] = null
    }
  }
}
</script>

<style>

/** 处理中 */
.highlight-todo.djs-connection > .djs-visual > path {
  stroke: #1890ff !important;
  stroke-dasharray: 4px !important;
  fill-opacity: 0.2 !important;
}
.highlight-todo.djs-shape .djs-visual > :nth-child(1) {
  fill: #1890ff !important;
  stroke: #1890ff !important;
  stroke-dasharray: 4px !important;
  fill-opacity: 0.2 !important;
}

/deep/.highlight-todo.djs-connection > .djs-visual > path {
  stroke: #1890ff !important;
  stroke-dasharray: 4px !important;
  fill-opacity: 0.2 !important;
  marker-end: url(#sequenceflow-end-_E7DFDF-_E7DFDF-803g1kf6zwzmcig1y2ulm5egr);
}
/deep/.highlight-todo.djs-shape .djs-visual > :nth-child(1) {
  fill: #1890ff !important;
  stroke: #1890ff !important;
  stroke-dasharray: 4px !important;
  fill-opacity: 0.2 !important;
}

/** 通过 */
.highlight.djs-shape .djs-visual > :nth-child(1) {
  fill: green !important;
  stroke: green !important;
  fill-opacity: 0.2 !important;
}
.highlight.djs-shape .djs-visual > :nth-child(2) {
  fill: green !important;
}
.highlight.djs-shape .djs-visual > path {
  fill: green !important;
  fill-opacity: 0.2 !important;
  stroke: green !important;
}
.highlight.djs-connection > .djs-visual > path {
  stroke: green !important;
}

.highlight:not(.djs-connection) .djs-visual > :nth-child(1) {
  fill: green !important; /* color elements as green */
}

/deep/.highlight.djs-shape .djs-visual > :nth-child(1) {
  fill: green !important;
  stroke: green !important;
  fill-opacity: 0.2 !important;
}
/deep/.highlight.djs-shape .djs-visual > :nth-child(2) {
  fill: green !important;
}
/deep/.highlight.djs-shape .djs-visual > path {
  fill: green !important;
  fill-opacity: 0.2 !important;
  stroke: green !important;
}
/deep/.highlight.djs-connection > .djs-visual > path {
  stroke: green !important;
}

/** 不通过 */
.highlight-reject.djs-shape .djs-visual > :nth-child(1) {
  fill: red !important;
  stroke: red !important;
  fill-opacity: 0.2 !important;
}
.highlight-reject.djs-shape .djs-visual > :nth-child(2) {
  fill: red !important;
}
.highlight-reject.djs-shape .djs-visual > path {
  fill: red !important;
  fill-opacity: 0.2 !important;
  stroke: red !important;
}
.highlight-reject.djs-connection > .djs-visual > path {
  stroke: red !important;
}

.highlight-reject:not(.djs-connection) .djs-visual > :nth-child(1) {
  fill: red !important; /* color elements as green */
}

/deep/.highlight-reject.djs-shape .djs-visual > :nth-child(1) {
  fill: red !important;
  stroke: red !important;
  fill-opacity: 0.2 !important;
}
/deep/.highlight-reject.djs-shape .djs-visual > :nth-child(2) {
  fill: red !important;
}
/deep/.highlight-reject.djs-shape .djs-visual > path {
  fill: red !important;
  fill-opacity: 0.2 !important;
  stroke: red !important;
}
/deep/.highlight-reject.djs-connection > .djs-visual > path {
  stroke: red !important;
}

/** 已取消 */
.highlight-cancel.djs-shape .djs-visual > :nth-child(1) {
  fill: grey !important;
  stroke: grey !important;
  fill-opacity: 0.2 !important;
}
.highlight-cancel.djs-shape .djs-visual > :nth-child(2) {
  fill: grey !important;
}
.highlight-cancel.djs-shape .djs-visual > path {
  fill: grey !important;
  fill-opacity: 0.2 !important;
  stroke: grey !important;
}
.highlight-cancel.djs-connection > .djs-visual > path {
  stroke: grey !important;
}

.highlight-cancel:not(.djs-connection) .djs-visual > :nth-child(1) {
  fill: grey !important; /* color elements as green */
}

/deep/.highlight-cancel.djs-shape .djs-visual > :nth-child(1) {
  fill: grey !important;
  stroke: grey !important;
  fill-opacity: 0.2 !important;
}
/deep/.highlight-cancel.djs-shape .djs-visual > :nth-child(2) {
  fill: grey !important;
}
/deep/.highlight-cancel.djs-shape .djs-visual > path {
  fill: grey !important;
  fill-opacity: 0.2 !important;
  stroke: grey !important;
}
/deep/.highlight-cancel.djs-connection > .djs-visual > path {
  stroke: grey !important;
}

.element-overlays {
  box-sizing: border-box;
  padding: 8px;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 4px;
  color: #fafafa;
  width: 200px;
}
</style>
