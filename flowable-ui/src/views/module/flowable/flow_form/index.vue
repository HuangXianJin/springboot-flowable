<!-- 自定义表单 -->
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
              <el-table-column label="描述" align="center" prop="description" sortable="custom">
                <template slot-scope="scope">
                  <span>{{ scope.row.description }}</span>
                </template>
              </el-table-column>

              <el-table-column label="操作" align="center" width="300" fixed="right">
                <template slot-scope="scope">
                  <el-button type="text" icon="el-icon-setting" @click="handleFormView(scope.row.id)">预览</el-button>
                  <el-button type="text" icon="el-icon-setting" @click="handleDesign(scope.row.id)">设计表单</el-button>
                  <el-button type="text" icon="el-icon-edit" @click="handleUpdate(scope.row.id)">编辑</el-button>
                  <el-button type="text" icon="el-icon-delete" @click="handleRemove(scope.row.id)">删除</el-button>
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
              <el-form-item label="描述" prop="description">
                <el-input v-model="temp.description" type="text" />
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
                <el-input v-model="temp.description" type="text" />
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
import Form from '@/api/flowable/flow_form'

export default {
  name: 'Form',
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
      temp: {},
      createDialogFormVisible: false,
      updateDialogFormVisible: false,
      uploadDialogFormVisible: false,
      fileName: '自定义表单', // 文件名
      importFunction: Form.importData, // 导入方法
      templateFunction: Form.exportData, // 模板方法，有自定义模板则不传
      rules: {
        conf: [{ required: true, message: '不能为空', trigger: 'change' }],
        fields: [{ required: true, message: '不能为空', trigger: 'change' }],
        description: [{ required: true, message: '不能为空', trigger: 'change' }]
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

    },
    getList() {
      Form.page(this.listQuery).then(response => {
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
          Form.create(this.temp).then(ret => {
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
    handleUpdate(id) {
      Form.get(id).then(response => {
        this.temp = response.data
      })
      this.updateDialogFormVisible = true
      this.$nextTick(() => {
        this.refs.updateForm.clearValidate()
      })
    },
    updateData(row) {
      this.refs.updateForm.validate((valid) => {
        if (valid) {
          Form.update(this.temp).then(ret => {
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
    deleteData(id) {
      Form.remove(id).then((ret) => {
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
    },
    handleDesign(id) {
      // var url = window.location.origin + window.location.pathname + '#/flowable/bpmnDesigner?id=' + id
      // window.open(url, '', 'menubar=0,toolbar=0,status=0,resizable=1,left=0,top=0,scrollbars=1,width=' + (screen.availWidth - 10) + ',height=' + (screen.availHeight - 50) + '"')
      this.$router.push({
        path: '/flowable/formDesigner',
        query: { id: id }
      })
    },
    handleFormView(id) {
      this.$router.push({
        path: '/flowable/formView',
        query: { formId: id }
      })
    }
  }
}
</script>

