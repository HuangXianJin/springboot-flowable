import Cookies from 'js-cookie'

const state = {
  sidebar: {
    opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
    withoutAnimation: false
  },
  device: 'desktop',
  autoLoading: true,
  keep: [],
  noKeep: []
}

const mutations = {
  TOGGLE_SIDEBAR: state => {
    state.sidebar.opened = !state.sidebar.opened
    state.sidebar.withoutAnimation = false
    if (state.sidebar.opened) {
      Cookies.set('sidebarStatus', 1)
    } else {
      Cookies.set('sidebarStatus', 0)
    }
  },
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    Cookies.set('sidebarStatus', 0)
    state.sidebar.opened = false
    state.sidebar.withoutAnimation = withoutAnimation
  },
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
  },
  SET_KEEP: (state, keep) => {
    state.keep = keep
  },
  SET_NO_KEEP: (state, noKeep) => {
    state.noKeep = noKeep
  },
  SET_AUTOLOADING: (state, autoLoading) => {
    state.autoLoading = autoLoading
  }
}

const actions = {
  toggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  closeSideBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  toggleDevice({ commit }, device) {
    commit('TOGGLE_DEVICE', device)
  },
  setKeep({ commit }, keep) {
    commit('SET_KEEP', keep)
  },
  setNoKeep({ commit }, noKeep) {
    commit('SET_NO_KEEP', noKeep)
  },
  setAutoLoading({ commit }, autoLoading) {
    commit('SET_AUTOLOADING', autoLoading)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
