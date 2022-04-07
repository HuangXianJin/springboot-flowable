<template>
  <div class="navbar">
    <NavMenu class="menu-container" />
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <breadcrumb class="breadcrumb-container" />
    <!-- <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <el-avatar v-if="avatar" :size="30" :src="avatar" fit="cover" class="user-avatar" />
          <el-avatar v-else :size="30" src="/static/images/my-photo-header.png" fit="cover" class="user-avatar" />
          <el-button type="text">
            {{ name }}<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
        </div>
      </el-dropdown>
    </div> -->

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import User from '@/api/system/sys_user'
import NavMenu from './Navbar/Menu'
// import Item from './Sidebar/Item.vue'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    NavMenu
  },
  data() {
    return {
      title: false,
      refs: undefined,
      modifyPasswordDialogFormVisible: false,
      personalMessageDialog: false,
      modifyThemeDialogVisible: false,
      temp: {},
      isShow: false,
      rules: {
        password: [{ required: true, message: '不能为空', trigger: 'change' }],
        newPassword: [{ required: true, message: '不能为空', trigger: 'change' }]
      },
      themeState: this.$store.getters.theme.themeStyle,
      themeList: [
        { id: 1, name: '白天模式', label: 'light' },
        { id: 2, name: '夜间模式', label: 'dark' }
      ],
      themeEnabled: this.$store.getters.theme.themeEnabled
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'name',
      'userId'
    ])
  },
  created() {
    this.themeState = this.$store.getters.theme.themeStyle
    const currentTheme = this.$store.getters.account.username + 'ThemeTopic'
    const themeTopicLocal = localStorage.getItem(currentTheme)
    if (themeTopicLocal) {
      this.$store.dispatch('theme/setThemeStyle', themeTopicLocal)
      this.themeState = themeTopicLocal
    }
  },
  mounted() {
    this.refs = this.$refs
  },
  methods: {
    // 改变主题
    onChangeTheme(data) {
      const themeData = data
      localStorage.setItem(this.$store.getters.account.username + 'ThemeTopic', themeData)
      this.$store.dispatch('theme/setThemeStyle', data)
      this.modifyThemeDialogVisible = false
    },
    handleChangeTheme() {
      this.modifyThemeDialogVisible = true
    },
    // 显示个人信息弹窗
    handlePersonalMessage() {
      this.personalMessageDialog = true
    },
    // 控制个人信息弹窗
    showPersonalMessageDialog(data) {
      if (data === 'false') {
        this.personalMessageDialog = false
      } else {
        this.personalMessageDialog = true
      }
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },

    fullScreen() {
      this.$router.push('/fullScreen')
    },
    handleModifyPassword() {
      this.temp = { id: this.userId }
      this.modifyPasswordDialogFormVisible = true
      this.$nextTick(() => {
        this.refs.modifyPasswordForm.clearValidate()
      })
    },
    inputChange(str) {
      if (str.length >= 6) {
        this.isShow = false
      } else if (str.length >= 1 && str.length < 6) {
        this.isShow = true
      } else {
        this.isShow = false
      }
    },
    modifyPassword() {
      this.refs.modifyPasswordForm.validate((valid) => {
        if (valid) {
          if (this.temp.password.length < 6 || this.temp.newPassword.length < 6) {
            this.$message.error('密码的长度需大于6位数')
            return false
          } else if (this.temp.password !== this.temp.newPassword) {
            this.$message.error('两次密码输入不一致')
            return false
          }
          const data = {
            id: this.userId,
            password: this.temp.password
          }
          User.modifyPassword(data).then(async ret => {
            this.modifyPasswordDialogFormVisible = false
            this.$message({ message: ret.message, type: 'success' })
            if (ret.code === 200 && ret.success === true) {
              await this.$store.dispatch('user/logout')
              this.$router.push(`/login?redirect=${this.$route.fullPath}`)
            }
          })
        }
      })
    },
    handleScreenMap() {
      // this.$router.push({
      //   path: '/map'
      // })
      const { href } = this.$router.resolve({
        path: '/map'
      })
      window.open(href, '_blank')
    },
    handleScreenData() {
      if (process.env.VUE_APP_THEME === 'dark') {
        const { href } = this.$router.resolve({
          path: '/datav'
        })
        window.open(href, '_blank')
      }
      if (process.env.VUE_APP_THEME === 'light') {
        const { href } = this.$router.resolve({
          path: '/screenData'
        })
        window.open(href, '_blank')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  // position: relative;
  position: fixed;
  z-index:5;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .my-header-go-btn{
    color:#409EFF;
    font-size: 13px;
    margin-right: 15px;
    text-decoration: underline;
    cursor: pointer;
  }

  .menu-container {
    line-height: 50px;
    height: 100%;
    float: left;
  }

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }
    .avatar-container {
      margin-right: 30px;
      .avatar-wrapper {
        // margin-top: 5px;
        position: relative;
        .user-avatar {
          cursor: pointer;
          vertical-align: middle;
        }
        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>

<style>
.my-header-menu {
  vertical-align: top;
  display: inline-block;
  font-size: 14px;
  font-weight: bold;
  margin-right: 10px;
  text-decoration: underline;
  color: #409eff;
  cursor: pointer;
}
.my-form-theme-container{
  text-align: center;
}
.my-form-theme-container .el-radio-button__inner {
  padding: 15px 20px;
  font-size: 16px;
  margin-bottom: 20px;
}
</style>
