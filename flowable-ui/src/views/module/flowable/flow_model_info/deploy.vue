<!-- Bpm 流程定义表-->
<template>
  <div class="app-container my-app-container">
    <!--条件开始-->
    <div class="my-filter-container">
      <div class="my-filter-list">
        <el-page-header content="流程设计" @back="onClickBack" />
      </div>
    </div>
    <!--条件结束-->
    <!--条件开始-->
    <div class="my-filter-container">
      <div class="my-filter-list">
        <span class="my-filter-item">
          <el-button type="primary" icon="el-icon-plus" @click="handleDeploy">发布最新流程</el-button>
        </span>
      </div>
    </div>
    <!--条件结束-->

    <!--主体开始-->
    <div class="my-layout-height my-layout-height-filter-table">
      <el-scrollbar class="my-scroll my-scroll-column">
        <div class="my-scroll-padding-right">
          <div class="my-table-container">
            <el-table :key="tableKey" v-loading="listLoading" :data="list" border fit highlight-current-row>
              <el-table-column label="序号" prop="id" align="center" width="60" fixed="left">
                <template slot-scope="scope">
                  <span>{{ scope.$index + (listQuery.current - 1) * listQuery.size + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column label="流程编号" align="center" prop="model_key">
                <template slot-scope="scope">
                  <span>{{ scope.row.modelKey }}</span>
                </template>
              </el-table-column>
              <el-table-column label="流程名称" align="center" prop="name">
                <template slot-scope="scope">
                  <span>{{ scope.row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column label="流程版本" align="center" prop="description">
                <template slot-scope="scope">
                  <span>{{ scope.row.version }}</span>
                </template>
              </el-table-column>
              <el-table-column label="激活状态" align="center" prop="description">
                <template slot-scope="scope">
                  <span>
                    <el-tag v-if="!scope.row.isSuspended" type="success">激活</el-tag>
                    <el-tag v-if="scope.row.isSuspended" type="info">挂起</el-tag>
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-scrollbar>
    </div>
    <!--主体结束-->

  </div>
</template>

<script>
import ModelInfo from '@/api/flowable/flow_model_info'

export default {
  name: 'Deploy',
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
      model: {}
    }
  },
  mounted() {
    this.refs = this.$refs
    this.initData()
  },
  methods: {
    initData() {
    // 如果 modelId 非空，说明是修改流程模型
      const modelId = this.$route.query && this.$route.query.id
      if (modelId) {
        ModelInfo.get(modelId).then(response => {
          this.model = response.data
          this.getList()
        })
      }
    },
    getList() {
      console.log(this.model)
      ModelInfo.getProcessDefinitionList({ modelKey: this.model.modelKey }).then(response => {
        this.list = response.data
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
    handleDeploy() {
      this.$confirm('是否发布当前流程?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deployModel()
      })
    },
    deployModel() {
      if (this.model && this.model.id) {
        ModelInfo.deploy({ id: this.model.id }).then(ret => {
          this.updateDialogFormVisible = false
          this.$message({
            message: ret.message,
            type: 'success'
          })
          this.getList()
        })
      }
    },
    onClickBack() {
      this.$router.back(-1)
    }
  }
}
</script>

