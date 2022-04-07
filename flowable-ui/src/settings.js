
const config = {
  name: 'yunct-monitor-admin',
  title: process.env.VUE_APP_TITLE,
  screenTitle: process.env.VUE_APP_SCREEN_TITLE,

  /**
     * @type {boolean} true | false
     * @description Whether fix the header
     */
  fixedHeader: false,

  /**
     * @type {boolean} true | false
     * @description Whether show the logo in sidebar
     */
  sidebarLogo: false,

  /** 设置主题
     * @themeStyle 'light' | 'dark'
     * @themeClass '' | 'my-dark-box'
     * @themeColor '#333' | '#288dff'
     */
  themeStyle: process.env.VUE_APP_THEME,
  themeEnabled: false
}

// export default config
module.exports = config
