const state = {
  dict: {}
}

const mutations = {
  SET_DICT: (state, item) => {
    state.dict[item.code] = item.data
  }
}

const actions = {
  saveDict({ commit }, item) {
    return new Promise((resolve, reject) => {
      commit('SET_DICT', item)
    })
  }

}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

