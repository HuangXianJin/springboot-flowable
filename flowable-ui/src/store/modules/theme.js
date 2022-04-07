import defaultSettings from '@/settings'

const { themeStyle, themeEnabled } = defaultSettings

const state = {
  themeStyle: themeStyle,
  themeClass: '',
  themeColor: '',
  themeEnabled: themeEnabled
}

if (state.themeStyle === 'dark') {
  // 夜间模式
  state.themeClass = 'my-dark-box'
  state.themeColor = '#288dff'
}
if (state.themeStyle === 'light') {
  // 白天模式
  state.themeClass = ''
  state.themeColor = '#333'
}
console.log(state)
const mutations = {
  SET_THEME_STYLE: (state, themeStyle) => {
    state.themeStyle = themeStyle
    if (state.themeStyle === 'dark') {
      // 夜间模式
      state.themeClass = 'my-dark-box'
      state.themeColor = '#288dff'
    }
    if (state.themeStyle === 'light') {
      // 白天模式
      state.themeClass = ''
      state.themeColor = '#333'
    }
  }
}

const actions = {
  setThemeStyle({ commit }, themeStyle) {
    commit('SET_THEME_STYLE', themeStyle)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
