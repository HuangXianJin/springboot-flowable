<!-- 工作流的流程任务的拓展表 -->
<template>
  <div class="app-container my-app-container">

    <!--条件开始-->
    <div class="my-filter-container">
      <div class="my-filter-list">
        <span class="my-filter-item my-filter-item-date">
          <date-picker v-model="dateValue" format="yyyy-MM-dd" clearable @pick="handleFilter" />
        </span>
        <span class="my-filter-item my-filter-item-search">
          <span class="search-input">
            <el-input v-model="listQuery.filter" placeholder="关键字" clearable @change="handleFilter" @keyup.enter.native="handleFilter" />
          </span>

          <span class="search-btn">
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
          </span>
        </span>
      </div>
    </div>
    <!--条件结束-->

    <!--主体开始-->
    <div class="my-layout-height my-layout-height-filter-table">
      <el-scrollbar class="my-scroll my-scroll-column">
        <div class="my-scroll-padding-right">
          <div class="my-table-container">
            <el-table :key="tableKey" v-loading="listLoading" :data="list" border fit highlight-current-row @sort-change="sortChange">
              <el-table-column type="selection" width="40" fixed="left" />
              <el-table-column label="序号" prop="id" align="center" width="60" fixed="left">
                <template slot-scope="scope">
                  <span>{{ scope.$index + (listQuery.current - 1) * listQuery.size + 1 }}</span>
                </template>
              </el-table-column>

              <el-table-column label="任务的名称" align="center" prop="name" sortable="custom">
                <template slot-scope="scope">
                  <span>{{ scope.row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column label="所属流程" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.row.processName }}</span>
                </template>
              </el-table-column>
              <el-table-column label="发起人" align="center" prop="start_user" sortable="custom">
                <template slot-scope="scope">
                  <span>{{ scope.row.startUserName }}</span>
                </template>
              </el-table-column>
              <el-table-column label="创建时间" align="center" prop="task_create_time" sortable="custom">
                <template slot-scope="scope">
                  <span>{{ scope.row.taskCreateTime }}</span>
                </template>
              </el-table-column>
              <el-table-column label="完成时间" align="center" prop="end_time" sortable="custom">
                <template slot-scope="scope">
                  <span>{{ scope.row.endTime }}</span>
                </template>
              </el-table-column>
              <el-table-column label="结果" align="center" prop="result" sortable="custom">
                <template slot-scope="scope">
                  <span>{{ scope.row.result }}</span>
                </template>
              </el-table-column>
              <el-table-column label="审批意见" align="center" prop="comment">
                <template slot-scope="scope">
                  <span>{{ scope.row.comment }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="100" fixed="right">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    icon="el-icon-view"
                    @click="handleAudit(scope.row)"
                  >详情</el-button>
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
import CustomTask from '@/api/flowable/flow_custom_task'

export default {
  name: 'CustomTask',
  components: {
    Pagination: () => import('@/components/Pagination'),
    DatePicker: () => import('@/components/DatePicker')
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
        descs: 'task_create_time'
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
    handleFilter() {
      this.listQuery.current = 1
      this.getList()
    },
    getList() {
      this.listQuery.beginCreateTime = this.dateValue[0]
      this.listQuery.endCreateTime = this.dateValue[1]
      CustomTask.getDoneTaskPage(this.listQuery).then(response => {
        this.listQuery.current = response.data.current
        this.list = response.data.records
        this.total = response.data.total
        this.listLoading = false
      })
    },
    /** 处理审批按钮 */
    handleAudit(row) {
      this.$router.push({ path: '/flowable/audit', query: { processInstanceId: row.processInstanceId }})
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
    }
  }
}
</script>

