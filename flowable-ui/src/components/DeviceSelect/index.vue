<template>
  <!--新增开始-->
  <el-dialog v-if="dialogVisible" :close-on-click-modal="false" :visible.sync="dialogVisible" :append-to-body="true" :title="multiple? '已选择设备数：'+selectionsKey.length+' / '+total : null" width="1100px">
    <div class="app-container my-app-container" style="padding:0">
      <!--条件开始-->
      <div class="my-filter-container">
        <div class="my-filter-list">
          <span v-if="multiple" class="my-filter-item">
            <el-checkbox label="选择全部设备" border @change="handleSelectAll" />
          </span>
          <!-- <span class="my-filter-item" style="width:150px">
            <el-select v-model="listQuery.com" placeholder="扩展口" clearable @change="handleFilter">
              <el-option label="扩展口1" value="USART_1" />
              <el-option label="扩展口2" value="USART_2" />
            </el-select>
          </span> -->
          <span class="my-filter-item" style="width:150px">
            <el-select v-model="listQuery.netStatus" placeholder="网络状态" clearable @change="handleFilter">
              <el-option label="在线" value="在线" />
              <el-option label="离线" value="离线" />
            </el-select>
          </span>
          <span class="my-filter-item">
            <el-input v-model="listQuery.model" placeholder="型号" clearable @change="handleFilter" @keyup.enter.native="handleFilter" />
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
      <div class="my-layout-height my-layout-height-filter-table" style="height:680px;">
        <el-scrollbar class="my-scroll my-scroll-column">
          <div class="my-scroll-padding-right">
            <div class="my-table-container">
              <el-table
                ref="multipleTable"
                :key="tableKey"
                v-loading="listLoading"
                tooltip-effect="dark"
                :data="devices"
                border
                fit
                highlight-current-row
                @sort-change="sortChange"
                @select="handleSelectionChange"
                @select-all="handleSelectionChangeAll"
                @current-change="handleCurrentChange"
              >
                <el-table-column v-if="multiple" type="selection" align="center" width="40" />
                <el-table-column v-if="!multiple" align="center" width="40">
                  <template slot-scope="scope">
                    <el-radio v-model="currentId" :label="scope.row.id">&nbsp;</el-radio>
                  </template>
                </el-table-column>
                <el-table-column type="index" width="60" align="center" />
                <el-table-column label="点位位置" align="left" prop="location" sortable="custom">
                  <template slot-scope="scope">
                    <span>{{ scope.row.town }}{{ scope.row.location }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="设备IP" align="center" prop="ip" sortable="custom" width="120">
                  <template slot-scope="scope">
                    <span>{{ scope.row.ip }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="设备编号" align="center" prop="device_uid" sortable="custom" width="120">
                  <template slot-scope="scope">
                    <span>{{ scope.row.deviceUid }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="设备型号" align="center" prop="model" sortable="custom" width="120">
                  <template slot-scope="scope">
                    <span>{{ scope.row.model }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="固件版本" align="center" prop="firmware_ver" sortable="custom" width="120">
                  <template slot-scope="scope">
                    <span>{{ scope.row.firmwareVer }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="硬件版本" align="center" prop="hardware_ver" sortable="custom" width="120">
                  <template slot-scope="scope">
                    <span>{{ scope.row.hardwareVer }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="网络" align="center" prop="net_status" sortable="custom" width="120">
                  <template slot-scope="scope">
                    <span>{{ scope.row.netStatus }}</span>
                  </template>
                </el-table-column>
                <!-- <el-table-column label="扩展口1" align="center" prop="com1_model" sortable="custom" width="150">
                  <template slot-scope="scope">
                    <span>{{ scope.row.com1Model }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="扩展口2" align="center" prop="com2_model" sortable="custom" width="150">
                  <template slot-scope="scope">
                    <span>{{ scope.row.com2Model }}</span>
                  </template>
                </el-table-column> -->
              </el-table>
              <pagination v-show="total > 0" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getList" />
            </div>
          </div>
        </el-scrollbar>
      </div>
      <!--主体结束-->
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleConfim()">确定</el-button>
    </div>
  </el-dialog>
  <!--新增结束-->
</template>

<script>
import Device from '@/api/device/dev_device'

export default {
  components: {
    Pagination: () => import('@/components/Pagination')
  },
  props: {
    value: { type: [Object, String, Number, Array], default: undefined },
    visible: { type: Boolean, default: false },
    multiple: { type: Boolean, default: false }, // 多选
    model: { type: String, default: undefined },
    hardwareVer: { type: String, default: undefined },
    com: { type: String, default: undefined }
  },
  data() {
    return {
      refs: undefined,
      tableKey: 0,
      allList: [],
      list: [],
      devices: undefined,
      devicesIds: [],
      listLoading: false,
      listQuery: {
        current: 1,
        size: 10,
        filter: undefined,
        deviceType: '控制终端',
        model: undefined,
        com: undefined,
        ids: undefined,
        listMode: undefined,
        withTemplate: false,
        ascs: undefined,
        descs: undefined
      },
      queryApi: undefined, // 查询api
      currentRow: undefined,
      currentId: undefined,
      dialogVisible: false,
      selections: [],
      selectionsKey: []
    }
  },
  computed: {
    device: {
      get() {
        if (this.multiple) {
          // 多选
          return this.value ? JSON.parse('[' + this.value + ']') : []
        } else {
          // 单选
          return this.value
        }
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    },
    dialogVisible(val) {
      if (val) {
        if (this.multiple) {
          this.initSelection()
        } else {
          this.currentId = Number(this.device)
        }
      }
      this.$emit('update:visible', val)
    },
    model() {
      this.initData()
    },
    com() {
      this.initData()
    }
  },
  mounted() {
    this.refs = this.$refs
    this.initData()
  },
  methods: {
    initData() {
      this.listQuery.model = this.model
      this.listQuery.hardwareVer = undefined
      if (this.hardwareVer) {
        this.listQuery.hardwareVer = this.hardwareVer
      }
      this.listQuery.com = this.com
      Device.list(this.listQuery).then(response => {
        this.allList = response.data
      })
      this.handleFilter()
    },
    initSelection() { // 初始化选项，并获取对应obj
      this.selectionsKey = this.device
      this.selections = this.allList.filter(e => { return this.selectionsKey.indexOf(e.id) >= 0 })
      this.setInitTable()
    },
    getList() {
      Device.page(this.listQuery).then(response => {
        this.listQuery.current = response.data.current
        this.devices = response.data.records
        this.total = response.data.total
        this.devicesIds = this.devices.map(e => { return e.id })
        this.listLoading = false
        if (this.dialogVisible && this.multiple) {
          this.setInitTable()
        }
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
    handleSelectAll(flag) {
      if (flag) {
        Device.list(this.listQuery).then(response => {
          this.list = response.data
          this.total = this.list.length
          this.selections = this.list
          this.selectionsKey = this.list.map(e => { return e.id })
          this.setInitTable()
        })
      } else {
        this.selections = []
        this.selectionsKey = []
        this.setInitTable()
      }
    },
    handleFilter() {
      this.listQuery.current = 1
      this.getList()
    },
    handleSelectionChangeAll(selection) {
      this.handleSelectionChange(selection)
    },
    handleSelectionChange(selection) {
      // 移除掉当前列表
      this.selections = this.selections.filter(e => {
        return this.devicesIds.indexOf(e.id) < 0
      })
      // 增加当前选中的
      this.selections = this.selections.concat(selection)
      this.selectionsKey = this.selections.map(e => { return e.id })
    },
    handleCurrentChange(val) {
      if (val && !this.multiple) {
        this.currentId = val.id
        this.currentRow = val
      }
    },
    handleConfim() {
      if (this.multiple) {
        this.$emit('input', this.selectionsKey.toString())
        this.$emit('select', this.selectionsKey) // 返回id数组，v-model返回字符串
        this.$emit('selectObj', this.selections)
      } else {
        this.$emit('input', this.currentId)
        this.$emit('select', this.currentId)
        this.$emit('selectObj', this.currentRow)
      }
      this.dialogVisible = false
    },
    setInitTable() {
      if (this.multiple) {
        this.$nextTick(() => {
          this.$refs.multipleTable.clearSelection()
          if (!this.devices) {
            return
          }
          this.devices.filter(e => { return this.selectionsKey.indexOf(e.id) >= 0 }).forEach(element => { this.$refs.multipleTable.toggleRowSelection(element, true) })
        })
      }
    }
  }
}
</script>
