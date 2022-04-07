<template>
  <div style="margin-top: 16px">
    <el-form-item label="处理用户">
      <el-select v-model="userTaskForm.assignee" filterable @change="updateElementTask('assignee')">
        <el-option v-for="user in userData" :key="user.id" :label="user.userName" :value="user.userId" />
      </el-select>
    </el-form-item>
    <!-- <el-form-item label="候选用户">
      <el-select v-model="userTaskForm.candidateUsers" multiple collapse-tags @change="updateElementTask('candidateUsers')">
        <el-option v-for="uk in mockData" :key="'user-' + uk" :label="`用户${uk}`" :value="`user${uk}`" />
      </el-select>
    </el-form-item> -->
    <!-- <el-form-item label="候选分组">
      <el-select v-model="userTaskForm.candidateGroups" multiple collapse-tags @change="updateElementTask('candidateGroups')">
        <el-option v-for="gk in mockData" :key="'ass-' + gk" :label="`分组${gk}`" :value="`group${gk}`" />
      </el-select>
    </el-form-item>
    <el-form-item label="到期时间">
      <el-input v-model="userTaskForm.dueDate" clearable @change="updateElementTask('dueDate')" />
    </el-form-item>
    <el-form-item label="跟踪时间">
      <el-input v-model="userTaskForm.followUpDate" clearable @change="updateElementTask('followUpDate')" />
    </el-form-item>
    <el-form-item label="优先级">
      <el-input v-model="userTaskForm.priority" clearable @change="updateElementTask('priority')" />
    </el-form-item> -->
  </div>
</template>

<script>

export default {
  name: 'UserTask',
  props: {
    id: String,
    type: String,
    userData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      defaultTaskForm: {
        assignee: '',
        candidateUsers: [],
        candidateGroups: [],
        dueDate: '',
        followUpDate: '',
        priority: ''
      },
      userTaskForm: {},

      userDataMap: {}
    }
  },
  watch: {
    id: {
      immediate: true,
      handler() {
        this.bpmnElement = window.bpmnInstances.bpmnElement
        this.$nextTick(() => this.resetTaskForm())
      }
    }
  },
  beforeDestroy() {
    this.bpmnElement = null
  },
  methods: {
    resetTaskForm() {
      for (const key in this.defaultTaskForm) {
        let value
        if (key === 'candidateUsers' || key === 'candidateGroups') {
          value = this.bpmnElement.businessObject[key] ? this.bpmnElement.businessObject[key].split(',') : []
        } else {
          value = this.bpmnElement.businessObject[key] || this.defaultTaskForm[key]
        }
        this.$set(this.userTaskForm, key, value)
      }
    },
    updateElementTask(key) {
      const taskAttr = Object.create(null)
      if (key === 'candidateUsers' || key === 'candidateGroups') {
        taskAttr[key] = this.userTaskForm[key] && this.userTaskForm[key].length ? this.userTaskForm[key].join() : null
      } else {
        taskAttr[key] = this.userTaskForm[key] || null
      }
      window.bpmnInstances.modeling.updateProperties(this.bpmnElement, taskAttr)
    }
  }
}
</script>
