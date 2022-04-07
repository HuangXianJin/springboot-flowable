<!-- Bpm 流程定义表-->
<template>
  <div class="app-container my-app-container">

    <!--条件开始-->
    <div class="my-filter-container">
      <div class="my-filter-list">
        <span class="my-filter-item my-filter-item-search">
          <span class="search-input">
            <el-input v-model="listQuery.filter" placeholder="关键字" clearable @change="handleFilter" @keyup.enter.native="handleFilter" />
          </span>
          <span class="search-btn">
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
          </span>
        </span>
        <span class="my-filter-item">
          <el-button type="primary" icon="el-icon-delete" @click="handleRemove(listQuery.ids)">批量删除</el-button>
        </span>
        <span class="my-filter-item">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">新增</el-button>
        </span>
      </div>
    </div>
    <!--条件结束-->

    <!--主体开始-->
    <div class="my-layout-height my-layout-height-filter-table">
      <el-scrollbar class="my-scroll my-scroll-column">
        <div class="my-scroll-padding-right">
          <div class="my-table-container">
            <el-table :key="tableKey" v-loading="listLoading" :data="list" border fit highlight-current-row @sort-change="sortChange" @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="40" fixed="left" />
              <el-table-column label="序号" prop="id" align="center" width="60" fixed="left">
                <template slot-scope="scope">
                  <span>{{ scope.$index + (listQuery.current - 1) * listQuery.size + 1 }}</span>
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
              <el-table-column label="预览表单" align="center">
                <template slot-scope="scope">
                  <el-button type="text" icon="el-icon-view" @click="handleFormView(scope.row)">点击预览</el-button>
                </template>
              </el-table-column>
              <el-table-column label="最后修改时间" align="center" prop="modify_time" sortable="custom">
                <template slot-scope="scope">
                  <span>{{ scope.row.modifyTime }}</span>
                </template>
              </el-table-column>
              <el-table-column label="最后发布时间" align="center" prop="deploy_time" sortable="custom">
                <template slot-scope="scope">
                  <span>{{ scope.row.deployTime }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="300" fixed="right">
                <template slot-scope="scope">
                  <el-button type="text" icon="el-icon-setting" @click="handleDeploy(scope.row.id)">发布流程</el-button>
                  <el-button type="text" icon="el-icon-setting" @click="handleDesign(scope.row.id)">设计流程</el-button>
                  <el-button type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
                  <el-button type="text" icon="el-icon-delete" @click="handleRemove(scope.row.id)">删除</el-button>
                  <!-- <el-button type="primary" icon="el-icon-edit" circle alt="编辑" title="编辑" @click="handleUpdate(scope.row)" />
                  <el-button type="danger" icon="el-icon-delete" circle alt="删除" title="删除" @click="handleRemove(scope.row.id)" /> -->
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="total > 0" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getList" />
          </div>
        </div>
      </el-scrollbar>
    </div>
    <!--主体结束-->

    <!--新增开始-->
    <el-dialog :close-on-click-modal="false" :visible.sync="createDialogFormVisible" title="新增" width="600px">
      <div class="my-form-container">
        <el-form ref="createForm" :rules="rules" :model="temp" label-position="right" label-width="100px">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="流程名称" prop="name">
                <el-input v-model="temp.name" type="text" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="描述" prop="description">
                <el-input v-model="temp.description" type="textarea" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="表单类型" prop="formType">
                <el-radio-group v-model="temp.formType">
                  <el-radio label="自定义表单"> 自定义表单 </el-radio>
                  <el-radio label="组件"> 组件 </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="temp.formType === '自定义表单'" :gutter="20">
            <el-col :span="24">
              <el-form-item label="自定义表单" prop="formId">
                <el-select v-model="temp.formId" clearable style="width: 100%">
                  <el-option v-for="form in formList" :key="form.id" :label="form.description" :value="form.id" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="temp.formType === '组件'" :gutter="20">
            <el-col :span="24">
              <el-form-item label="组件名称" prop="formPath">
                <el-input v-model="temp.formPath" type="text" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="createData()">确定</el-button>
      </div>
    </el-dialog>
    <!--新增结束-->

    <!--编辑开始-->
    <el-dialog :close-on-click-modal="false" :visible.sync="updateDialogFormVisible" title="编辑" width="600px">
      <div class="my-form-container">
        <el-form ref="updateForm" :rules="rules" :model="temp" label-position="right" label-width="100px">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="描述" prop="description">
                <el-input v-model="temp.description" type="textarea" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="表单类型" prop="formType">
                <el-radio-group v-model="temp.formType">
                  <el-radio label="自定义表单"> 自定义表单 </el-radio>
                  <el-radio label="路径"> 路径 </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="temp.formType === '自定义表单'" :gutter="20">
            <el-col :span="24">
              <el-form-item label="自定义表单" prop="formId">
                <el-select v-model="temp.formId" clearable style="width: 100%">
                  <el-option v-for="form in formList" :key="form.id" :label="form.description" :value="form.id" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="temp.formType === '路径'" :gutter="20">
            <el-col :span="24">
              <el-form-item label="表单路径" prop="formPath">
                <el-input v-model="temp.formPath" type="text" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateDialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="updateData()">确定</el-button>
      </div>
    </el-dialog>
    <!--编辑结束-->

  </div>
</template>

<script>
import ModelInfo from '@/api/flowable/flow_model_info'
import Form from '@/api/flowable/flow_form'

export default {
  name: 'ModelInfo',
  components: {
    Pagination: () => import('@/components/Pagination')
  },
  data() {
    return {
      refs: undefined,
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: false,
      listQuery: {
        current: 1,
        size: 10,
        filter: undefined,
        ids: undefined,
        withTemplate: false,
        ascs: undefined,
        descs: undefined
      },
      formList: [],
      temp: {},
      createDialogFormVisible: false,
      updateDialogFormVisible: false,
      uploadDialogFormVisible: false,
      fileName: '流程定义', // 文件名
      importFunction: ModelInfo.importData, // 导入方法
      templateFunction: ModelInfo.exportData, // 模板方法，有自定义模板则不传
      rules: {
        modelId: [{ required: true, message: '不能为空', trigger: 'change' }],
        modelKey: [{ required: true, message: '不能为空', trigger: 'change' }],
        name: [{ required: true, message: '不能为空', trigger: 'change' }],
        formType: [{ required: true, message: '不能为空', trigger: 'change' }],
        formId: [{ required: true, message: '不能为空', trigger: 'change' }],
        formPath: [{ required: true, message: '不能为空', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.refs = this.$refs
    this.initData()
    this.handleFilter()
  },
  methods: {
    initData() {
      Form.list().then(resp => {
        this.formList = resp.data
      })
    },
    getList() {
      ModelInfo.page(this.listQuery).then(response => {
        this.listQuery.current = response.data.current
        this.list = response.data.records
        this.total = response.data.total
        this.listLoading = false
      })
    },
    sortChange(data) {
      const { prop, order } = data
      if (order === 'ascending') {
        this.listQuery.ascs = prop
        this.listQuery.descs = undefined
      } else {
        this.listQuery.ascs = undefined
        this.listQuery.descs = prop
      }
      this.handleFilter()
    },
    handleFilter() {
      this.listQuery.current = 1
      this.getList()
    },
    handleCreate() {
      this.temp = {}
      this.createDialogFormVisible = true
      this.$nextTick(() => {
        this.refs.createForm.clearValidate()
      })
    },
    createData() {
      this.refs.createForm.validate((valid) => {
        if (valid) {
          ModelInfo.create(this.temp).then(ret => {
            this.createDialogFormVisible = false
            this.$message({
              message: ret.message,
              type: 'success'
            })
            this.getList()
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.updateDialogFormVisible = true
      this.$nextTick(() => {
        this.refs.updateForm.clearValidate()
      })
    },
    updateData(row) {
      this.refs.updateForm.validate((valid) => {
        if (valid) {
          ModelInfo.update(this.temp).then(ret => {
            this.updateDialogFormVisible = false
            this.$message({
              message: ret.message,
              type: 'success'
            })
            this.getList()
          })
        }
      })
    },
    handleRemove(id) {
      if (id && id !== '') {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteData(id)
        })
      } else {
        this.$message.warning('请选择')
      }
    },
    handleDesign(id) {
      // var url = window.location.origin + window.location.pathname + '#/flowable/bpmnDesigner?id=' + id
      // window.open(url, '', 'menubar=0,toolbar=0,status=0,resizable=1,left=0,top=0,scrollbars=1,width=' + (screen.availWidth - 10) + ',height=' + (screen.availHeight - 50) + '"')

      this.$router.push({
        path: '/flowable/bpmnDesigner',
        query: { id: id }
      })
    },
    handleDeploy(id) {
      this.$router.push({
        path: '/flowable/deploy',
        query: { id: id }
      })
    },
    handleFormView(item) {
      if (item.formType === '自定义表单') {
        this.$router.push({
          path: '/flowable/formView',
          query: { formId: item.formId }
        })
      }
      if (item.formType === '路径') {
        this.$router.push({
          path: item.formPath
        })
      }
    },
    deleteData(id) {
      ModelInfo.remove(id).then((ret) => {
        this.$message({
          type: 'success',
          message: ret.message
        })
        if (this.list.length === 1) {
          this.listQuery.current -= 1
        }
        this.getList()
      })
    },
    handleSelectionChange(val) {
      this.listQuery.ids = val.map(e => e.id).toString()
    }
  }
}
</script>

