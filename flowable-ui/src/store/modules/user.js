import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  userId: undefined,
  account: undefined,
  menus: []
}

const mutations = {
  SET_USER: (state, user) => {
    state.account = user
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  CLEAR_DICT: (state, token) => {
    for (var i = 0; i < localStorage.length; i++) {
      var key = localStorage.key(i) // 获取本地存储的Key
      if (key.startsWith('dict')) {
        localStorage.removeItem(key)
      }
    }
  },
  SET_USERID: (state, userId) => {
    state.userId = userId
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_MENUS: (state, menus) => {
    state.menus = menus
  },
  SET_AVATAR: (state, avatar) => {
    if (avatar) {
      state.avatar = avatar
    } else {
      state.avatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    }
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    userInfo.username = userInfo.username.trim()
  },
  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      const data = { nickName: '管理员', userId: 'admin', avatar: '' }
      const { nickName, avatar, userId } = data
      commit('SET_USERID', userId)
      commit('SET_NAME', nickName)
      commit('SET_AVATAR', avatar)
      commit('SET_USER', data)
      resolve(data)
    })
  },
  getMenu({ commit, state }) {

  },
  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', '')
      commit('SET_USERID', '')
      commit('SET_NAME', '')
      commit('SET_AVATAR', '')
      removeToken()
      resetRouter()
      resolve()
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      removeToken()
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

