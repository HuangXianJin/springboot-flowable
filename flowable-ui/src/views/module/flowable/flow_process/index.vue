<!-- 流程实例 -->
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
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">发起流程</el-button>
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
              <el-table-column label="流程状态" align="center" prop="status" sortable="custom">
                <template slot-scope="scope">
                  <span>{{ scope.row.status }}</span>
                </template>
              </el-table-column>
              <el-table-column label="流程结果" align="center" prop="result" sortable="custom">
                <template slot-scope="scope">
                  <span>{{ scope.row.result }}</span>
                </template>
              </el-table-column>
              <el-table-column label="结束时间" align="center" prop="end_time" sortable="custom">
                <template slot-scope="scope">
                  <span>{{ scope.row.endTime }}</span>
                </template>
              </el-table-column>
              <el-table-column label="提交时间" align="center" prop="create_time" sortable="custom">
                <template slot-scope="scope">
                  <span>{{ scope.row.createTime }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="240" fixed="right">
                <template slot-scope="scope">
                  <el-button type="text" icon="el-icon-view" @click="handleAudit(scope.row)">详情</el-button>
                  <el-button v-if="scope.row.result === '处理中'" type="text" icon="el-icon-delete" @click="handleCancel(scope.row)">取消</el-button>
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="total > 0" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getList" />
          </div>
        </div>
      </el-scrollbar>
    </div>
    <!--主体结束-->

  </div>
</template>

<script>
import CustomProcessInstance from '@/api/flowable/flow_custom_process_instance'

export default {
  name: 'CustomProcessInstance',
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
      dateValue: [],
      listQuery: {
        current: 1,
        size: 10,
        filter: undefined,
        ids: undefined,
        withTemplate: false,
        ascs: undefined,
        descs: 'create_time'
      },
      temp: {}
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
      this.listQuery.startUser = this.$store.getters.account.mobile
      CustomProcessInstance.page(this.listQuery).then(response => {
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
      this.$router.push({ path: '/flowable/createProcess' })
    },
    handleCancel(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.$confirm('是否取消该流程?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.cancel()
      })
    },
    cancel() {
      CustomProcessInstance.cancel(this.temp).then((ret) => {
        this.$message({
          type: 'success',
          message: ret.message
        })
        this.getList()
      })
    },
    handleSelectionChange(val) {
      this.listQuery.ids = val.map(e => e.id).toString()
    },
    handleAudit(row) {
      this.$router.push({ path: '/flowable/audit', query: { processInstanceId: row.processInstanceId }})
    }
  }
}
</script>

