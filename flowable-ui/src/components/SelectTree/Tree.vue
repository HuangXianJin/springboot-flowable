<template>
  <el-popover v-model="isShowSelect" placement="bottom-start" trigger="click">
    <el-input slot="reference" ref="input" v-model="treeShowValue" :clearable="false" :multiple="false" :placeholder="tipText" suffix-icon="el-icon-arrow-down" @click.native="isShowSelect = !isShowSelect" />
    <el-tree
      v-if="isShowSelect"
      ref="tree"
      :show-checkbox="multiple"
      :expand-on-click-node="true"
      :default-expanded-keys="defaultExpandedKeys"
      :default-checked-keys="key"
      :default-expand-all="defaultExpandAll.default"
      :props="defaultProps"
      :data="treeData"
      :node-key="nodeKey"
      highlight-current
      @node-click="handleNodeClick"
      @check="getKeys"
    />
  </el-popover>
</template>
<script>
// import { validatenull } from '@/util/validate'
export default {
  props: {
    treeData: { type: Array, required: true },
    defaultExpandAll: {
      type: Boolean,
      default: false
    },
    multiple: {
      type: Boolean,
      default: false
    },
    id: {
      type: [String, Array],
      default: null
    },
    selectOptions: {
      type: [String, Array],
      default: null
    },
    leafOnly: { type: Boolean,
      default: true },
    includeHalfChecked: { type: Boolean,
      default: true },
    nodeKey: { type: String, default: 'value' },
    tipText: { type: String, default: '请选择指标项' }
  },
  data() {
    return {
      // 是否显示树状选择器
      isShowSelect: false,
      // 树状菜单显示状态
      showStatus: false,
      key: [],
      showValueTmp: '',
      treeShowValue: '',
      defaultExpandedKeys: [],
      defaultCheckedKeys: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // 返回选中数据，包括父子节点数据
      options: []
    }
  },
  watch: {
    key(val) {
      if (this.multiple) {
        this.$emit('update:id', this.key)
        this.$emit('check', this.options)
      } else {
        this.$emit('update:id', this.key[0])
        this.$emit('check', this.options)
      }
    },
    treeData: {
      handler(newValue, oldValue) {
        this.setTreeCheckNode(this.key)
      },
      deep: true
    }
  },
  mounted() {
    // 把传进来的参数转成数组处理
    if (this.id) {
      if (this.multiple && Array.isArray(this.id)) {
        this.key = this.id
      } else {
        this.key.push(this.id)
      }
    }
  },
  methods: {
    handleNodeClick(data) {
      if (!this.multiple) {
        this.treeShowValue = data.label
        this.key = [data.id]
        this.isShowSelect = !this.isShowSelect
      }
    },
    getKeys(data, checked) {
      const tmp = []
      var nodes = this.$refs.tree.getCheckedNodes(this.leafOnly, this.includeHalfChecked)
      var keys = this.$refs.tree.getCheckedKeys(this.leafOnly, this.includeHalfChecked)
      nodes.forEach(node => {
        tmp.push(node.label)
      })
      this.options = nodes
      this.treeShowValue = tmp.toString()
      this.key = keys
    },
    setTreeCheckNode(ids) {
      var tmp = []
      ids.forEach(id => {
        var ret = this.findTreeNode(this.treeData, id)
        if (ret) {
          tmp.push(ret)
        }
      })
      this.treeShowValue = tmp.toString()
    },
    // 递归查询树形节点
    findTreeNode(tree, id) {
      var ret
      for (var i = 0; i < tree.length; i++) {
        if (tree[i][this.nodeKey] === id) {
          ret = tree[i].label
        } else
        if (tree[i].children != null && tree[i].children.length > 0) {
          ret = this.findTreeNode(tree[i].children, id)
        }
        if (ret) {
          break
        }
      }
      return ret
    }
  }
}
</script>
<style>
.el-input.el-input--suffix {
  cursor: pointer;
  overflow: hidden;
}
.el-input.el-input--suffix.rotate .el-input__suffix {
  transform: rotate(180deg);
}
.select-tree {
  max-height: 350px;
  overflow-y: scroll;
}
/* 菜单滚动条 */
.select-tree::-webkit-scrollbar {
  z-index: 11;
  width: 6px;
}
.select-tree::-webkit-scrollbar-track,
.select-tree::-webkit-scrollbar-corner {
  background: #fff;
}
.select-tree::-webkit-scrollbar-thumb {
  border-radius: 5px;
  width: 6px;
  background: #b4bccc;
}
.select-tree::-webkit-scrollbar-track-piece {
  background: #fff;
  width: 6px;
}
</style>
