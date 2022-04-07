<template>
  <div class="app-container">
    <!--条件开始-->
    <div class="my-filter-container">
      <div class="my-filter-list">
        <el-page-header content="流程设计" @back="onClickBack" />
      </div>
    </div>
    <!--条件结束-->
    <BpmnProcessDesigner
      :key="`designer-${reloadIndex}`"
      ref="processDesigner"
      v-model="xmlString"
      v-bind="controlForm"
      :process-id="processId"
      :process-name="processName"
      @init-finished="initModeler"
      @save="save"
    />
    <BpmnPropertiesPanel :key="`penal-${reloadIndex}`" :bpmn-modeler="modeler" prefix="flowable" class="process-panel" />
  </div>

</template>

<script>

import BpmnProcessDesigner from '@/components/BpmnProcessDesigner'
import BpmnPropertiesPanel from '@/components/BpmnPropertiesPanel'
import ModelInfo from '@/api/flowable/flow_model_info'

export default {
  components: { BpmnProcessDesigner, BpmnPropertiesPanel },
  // components: { BpmnProcessDesigner },
  data() {
    return {
      modeler: null,
      reloadIndex: 0,
      canvas: null,
      model: {},
      xmlString: '',
      processId: '',
      processName: '',
      controlForm: {
        labelEditing: false,
        labelVisible: false,
        prefix: 'flowable',
        headerButtonSize: 'mini',
        events: ['element.click', 'element.contextmenu']
      }
    }
  },
  mounted() {
    // 如果 modelId 非空，说明是修改流程模型
    const modelId = this.$route.query && this.$route.query.id
    if (modelId) {
      ModelInfo.get(modelId).then(response => {
        this.processId = 'Process_' + response.data.id
        this.processName = response.data.name
        this.model = response.data
        this.xmlString = this.model.xml
        this.$nextTick(function() {
          this.$refs.processDesigner.createNewDiagram(this.xmlString)
        })
      })
    }
  },
  methods: {
    initModeler(modeler) {
      setTimeout(() => {
        this.modeler = modeler
      }, 10)
    },
    save(bpmnXml) {
      this.model.modelKey = this.processId
      this.model.xml = bpmnXml
      if (this.model.id) {
        ModelInfo.update(this.model).then(ret => {
          this.$message({
            message: ret.message,
            type: 'success'
          })
          this.$router.back(-1)
        })
      }
    },
    onClickBack() {
      this.$router.back(-1)
    }
  }
}
</script>

<style lang="scss">
.element-overlays {
  box-sizing: border-box;
  padding: 8px;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 4px;
  color: #fafafa;
}

.my-process-designer {
  height: calc(100vh - 84px);
}
.process-panel__container {
  position: absolute;
  right: 0;
  top: 55px;
  height: calc(100vh - 84px);
}
</style>
