<!-- 流程实例 -->
<template>
  <div class="app-container my-app-container">
    <!--条件开始-->
    <div class="my-filter-container">
      <div class="my-filter-list">
        <el-page-header content="审批" @back="onClickBack" />
      </div>
    </div>

    <!-- 审批信息 -->
    <el-card v-loading="loading" class="box-card">

      <div slot="header" class="clearfix">
        <span v-if="runningTask" class="el-icon-picture-outline">审批任务【{{ runningTask.name }}】</span>
        <span v-else class="el-icon-picture-outline">流程信息</span>
      </div>
      <el-col :span="16" :offset="6">
        <el-form ref="AuditForm" :model="runningTask" label-width="100px">
          <el-form-item v-if="processInstance && processInstance.name" label="流程名称">
            {{ processInstance.name }}
          </el-form-item>
          <el-form-item v-if="processInstance && processInstance.startUserName" label="流程发起人">
            {{ processInstance.startUserName }}
          </el-form-item>
          <el-form-item v-if="processInstance && processInstance.createTime" label="流程发起时间">
            {{ processInstance.createTime }}
          </el-form-item>
          <el-form-item v-if="runningTask" label="审批建议" prop="comment">
            <el-input v-model="runningTask.comment" type="textarea" placeholder="请输入审批建议" />
          </el-form-item>
        </el-form>
        <div v-if="runningTask" style="margin-left: 10%; margin-bottom: 20px; font-size: 14px;">
          <el-button icon="el-icon-edit-outline" type="success" size="mini" @click="handleApprove()">通过</el-button>
          <el-button icon="el-icon-circle-close" type="danger" size="mini" @click="handleReject(auditForm, false)">未通过</el-button>
          <el-button icon="el-icon-edit-outline" type="primary" size="mini" @click="handleUpdateAssignee()">转办</el-button>
          <!-- <el-button icon="el-icon-refresh-left" type="warning" size="mini" @click="handleBack(item)">退回</el-button> -->
        </div>
      </el-col>
    </el-card>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="el-icon-picture-outline">审批记录</span>
      </div>
      <el-col :span="16" :offset="4">
        <div class="block">
          <el-timeline>
            <el-timeline-item
              v-for="(item, index) in taskList"
              :key="index"
              :icon="timelineIcon[item.result]"
              :type="timelineType[item.result]"
            >
              <p style="font-weight: 700">任务：{{ item.name }}</p>
              <el-card :body-style="{ padding: '10px' }">
                <label v-if="item.assigneeUser" style="font-weight: normal; margin-right: 30px;">
                  审批人：{{ item.assigneeUserName }}
                </label>
                <label style="font-weight: normal">创建时间：</label>
                <label style="color:#8a909c; font-weight: normal">{{ item.taskCreateTime }}</label>
                <label v-if="item.endTime" style="margin-left: 30px;font-weight: normal">审批时间：</label>
                <label v-if="item.endTime" style="color:#8a909c;font-weight: normal"> {{ item.endTime }}</label>
                <label v-if="item.durationInMillis" style="margin-left: 30px;font-weight: normal">耗时：</label>
                <label v-if="item.durationInMillis" style="color:#8a909c;font-weight: normal"> {{ item.durationInMillis }} </label>
                <p v-if="item.comment">
                  <el-tag :type="timelineType[item.result]">{{ item.comment }}</el-tag>
                </p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-col>
    </el-card>

    <!-- 第二步，填写表单，进行流程的提交 -->
    <div>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span v-if="processInstance" class="el-icon-document">申请信息【{{ processInstance.name }}】</span>
        </div>
        <el-col :span="16" :offset="6">
          <!-- <div v-if="formVariables" style="pointer-events: none;"> -->
          <div>
            <FormView
              :conf="processInstance.conf"
              :fields="processInstance.fields"
              :form-variables-string="processInstance.formVariables"
            />
          </div>
        </el-col>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span class="el-icon-picture-outline">流程图</span>
        </div>
        <process-viewer key="designer" v-model="bpmnXML" :task-data="taskList" :process-instance-data="processInstance" :activity-data="activityList" />
      </el-card>
    </div>

    <!--用户选择开始-->
    <el-dialog :close-on-click-modal="false" :visible.sync="selectDialogFormVisible" title="选择处理用户" width="500px">
      <div class="my-form-container">
        <el-form ref="selectForm" :model="assigneeUser" label-position="right" label-width="100px">
          <el-form-item label="">
            <el-select v-model="assigneeUser" value-key="id" filterable>
              <el-option v-for="user in userData" :key="user.userId" :label="user.userName" :value="user" />
            </el-select>
          </el-form-item>
        </el-form>

      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="selectDialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="updateAssignee()">确定</el-button>
      </div>
    </el-dialog>
    <!--用户选择结束-->
  </div>

</template>

<script>
import CustomProcessInstance from '@/api/flowable/flow_custom_process_instance'
import ModelInfo from '@/api/flowable/flow_model_info'
import ProcessViewer from '@/components/BpmnProcessDesigner/ProcessViewer'
import CustomTask from '@/api/flowable/flow_custom_task'
import User from '@/api/system/sys_user'
import FormView from '@/components/FormDesigner/FormView'
import { formatDuring } from '@/utils'

export default {
  name: 'AuditTask',
  components: { ProcessViewer, FormView },
  data() {
    return {
      modelList: [],
      disabled: true,
      refs: undefined,
      tableKey: 0,
      list: null,
      total: 0,
      loading: false,
      fApi: null,
      rule: [],
      option: {},
      form: {},
      formId: null,
      bpmnXML: '',
      modeler: {},
      processInstanceId: undefined,
      taskId: undefined,
      processInstance: {},
      taskList: [],
      auditForm: {},
      runningTask: undefined,
      formVariables: {},
      activityList: [],
      timelineIcon: { '处理中': 'el-icon-time', '通过': 'el-icon-check', '未通过': 'el-icon-close', '已取消': 'el-icon-remove-outline' },
      timelineType: { '处理中': 'primary', '通过': 'success', '未通过': 'danger', '已取消': 'info' },
      selectDialogFormVisible: false,
      assigneeUser: {},
      userData: []
    }
  },
  mounted() {
    this.refs = this.$refs
    this.initData()
    // this.handleFilter()
  },
  methods: {
    initData() {
      User.list({ listMode: true }).then(ret => {
        this.userData = ret.data
      })

      this.processInstanceId = this.$route.query && this.$route.query.processInstanceId
      this.taskId = this.$route.query && this.$route.query.taskId
      this.runningTask = undefined
      if (this.processInstanceId) {
        this.loading = true
        CustomProcessInstance.getByProcessInstanceId({ processInstanceId: this.processInstanceId }).then(rep => {
          this.loading = false
          this.processInstance = rep.data

          if (!this.processInstance) {
            return
          }
          this.getBpmnXml()
        })
        CustomTask.getTaskListByProcessInstanceId({ processInstanceId: this.processInstanceId }).then(rep => {
          this.taskList = rep.data

          // 耗时
          this.taskList.forEach(item => {
            if (item.durationInMillis) {
              item.durationInMillis = formatDuring(item.durationInMillis)
            }
          })

          // 排序，将未完成的排在前面，已完成的排在后面；
          this.taskList.sort((a, b) => {
          // 有已完成的情况，按照完成时间倒序
            if (a.endTime && b.endTime) {
              return b.endTime - a.endTime
            } else if (a.endTime) {
              return 1
            } else if (b.endTime) {
              return -1
            // 都是未完成，按照创建时间倒序
            } else {
              return b.taskCreateTime - a.taskCreateTime
            }
          })

          // 需要审核的记录
          this.taskList.forEach(task => {
            if (task.result !== '处理中') { // 只有待处理才需要
              return
            }
            if (!this.taskId || !task.taskId || task.taskId !== this.taskId || task.assigneeUser !== this.$store.getters.account.mobile) { // 自己不是处理人
              return
            }
            this.runningTask = task
            this.auditForm = {}
          })
        })

        CustomTask.getActivityListByProcessInstanceId({ processInstanceId: this.processInstanceId }).then(resp => {
          this.activityList = resp.data
        })
      }
    },
    getBpmnXml() {
      ModelInfo.getBpmnXml({ definitionId: this.processInstance.processDefinitionId }).then(response => {
        this.bpmnXML = response.data
      })
    },
    handleSelectionChange(val) {
      this.listQuery.ids = val.map(e => e.id).toString()
    },
    onClickBack() {
      this.$router.back(-1)
    },
    save() {
      this.fApi.submit((formData, fApi) => {
        var data = {}
        data.processDefinitionId = this.selectProcessInstance.definitionId
        data.modelInfoId = this.selectProcessInstance.id
        data.formVariables = JSON.stringify(formData)
        CustomProcessInstance.create(data).then(ret => {

        })
      })
    },
    handleApprove() {
      this.$confirm('是否通过该项审批任务?', '提示', {
        confirmButtonText: '是',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        CustomTask.approve(this.runningTask).then(e => {
          this.$message({
            message: '任务已通过审批',
            type: 'success'
          })
          this.initData()
        })
      })
    },
    handleReject() {
      this.$confirm('是否不通过该项审批任务?', '提示', {
        confirmButtonText: '是',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        CustomTask.reject(this.runningTask).then(e => {
          this.$message({
            message: '任务未通过审批',
            type: 'success'
          })
          this.initData()
        })
      })
    },
    handleUpdateAssignee() {
      this.assigneeUser = {}
      this.selectDialogFormVisible = true
    },
    updateAssignee() {
      if (!this.assigneeUser.mobile) {
        this.$message({
          message: '请选择处理用户',
          type: 'warning'
        })
        return
      }
      const data = {
        id: this.runningTask.id,
        assigneeUser: this.assigneeUser.userId,
        assigneeUserName: this.assigneeUser.userName,
        taskId: this.runningTask.taskId
      }
      CustomTask.updateAssignee(data).then(e => {
        this.$message({
          message: '转办成功',
          type: 'success'
        })
        this.initData()
        this.selectDialogFormVisible = false
      })
    }
  }
}

</script>

<style lang="scss">
.my-process-designer {
  height: calc(100vh - 200px);
}
.box-card {
  width: 100%;
  margin-bottom: 20px;
}

</style>

