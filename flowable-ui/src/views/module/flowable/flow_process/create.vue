<!-- 流程实例 -->
<template>
  <div class="app-container my-app-container">
    <!--条件开始-->
    <div class="my-filter-container">
      <div class="my-filter-list">
        <el-page-header content="发起流程" @back="onClickBack" />
      </div>
    </div>
    <!--条件结束-->
    <div v-if="!selectProcessInstance">
      <el-table v-loading="loading" :data="list">
        <el-table-column label="序号" prop="id" align="center" width="60" fixed="left">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="流程名称" align="center" prop="name" sortable="custom">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="描述" align="center" prop="description" sortable="custom">
          <template slot-scope="scope">
            <span>{{ scope.row.description }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-setting" @click="handleSelect(scope.row)">选择流程</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 第二步，填写表单，进行流程的提交 -->
    <div v-else>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span class="el-icon-document">申请信息【{{ selectProcessInstance.name }}】</span>
          <el-button style="float: right;" type="primary" @click="save()">提交</el-button>
          <el-button style="float: right;margin-right:10px;" type="primary" @click="selectProcessInstance = undefined">选择其它流程</el-button>
        </div>
        <el-col :span="16" :offset="6">
          <div>
            <FormView
              ref="formView"
              :conf="form.conf"
              :fields="form.fields"
            />
          </div>
        </el-col>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span class="el-icon-picture-outline">流程图</span>
        </div>
        <process-viewer key="designer" v-model="bpmnXML" />
      </el-card>
    </div>
  </div>

</template>

<script>
import CustomProcessInstance from '@/api/flowable/flow_custom_process_instance'
import ModelInfo from '@/api/flowable/flow_model_info'
import Form from '@/api/flowable/flow_form'
import ProcessViewer from '@/components/BpmnProcessDesigner/ProcessViewer'
import FormView from '@/components/FormDesigner/FormView'

export default {
  name: 'CreateProcess',
  components: { ProcessViewer, FormView },
  data() {
    return {
      selectProcessInstance: undefined,
      modelList: [],
      refs: undefined,
      tableKey: 0,
      list: null,
      total: 0,
      loading: false,
      fApi: null,
      rule: [],
      formId: null,
      option: {},
      bpmnXML: '',
      modeler: {},
      form: {}
    }
  },
  mounted() {
    this.refs = this.$refs
    this.initData()
    // this.handleFilter()
  },
  methods: {
    initData() {
      ModelInfo.list().then(response => {
        this.list = response.data
      })
    },
    initForm() {
      if (this.formId) {
        Form.get(this.formId).then(response => {
          this.form = response.data
        })
      }
    },
    onClickBack() {
      this.$router.back(-1)
    },
    save() {
      var data = {}
      data.processDefinitionId = this.selectProcessInstance.definitionId
      data.modelInfoId = this.selectProcessInstance.id
      var formVariables = this.$refs.formView.save()
      debugger
      if (formVariables === false) {
        return
      }
      data.formVariables = JSON.stringify(formVariables)
      CustomProcessInstance.create(data).then(ret => {
        this.$message({
          message: '流程已提交成功',
          type: 'success'
        })
        this.onClickBack()
      })
    },
    /** 处理选择流程的按钮操作 **/
    handleSelect(row) {
      // 设置选择的流程
      this.selectProcessInstance = row

      // 流程表单
      if (row.formId) {
        // 设置对应的表单
        this.formId = row.formId
        this.initForm()
      }
      //   // 加载流程图
      ModelInfo.getBpmnXml({ definitionId: row.definitionId }).then(response => {
        this.bpmnXML = response.data
      })
      // } else if (row.formCustomCreatePath) {
      //   this.$router.push({ path: row.formCustomCreatePath })
      //   // 这里暂时无需加载流程图，因为跳出到另外个 Tab；
      // }
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

