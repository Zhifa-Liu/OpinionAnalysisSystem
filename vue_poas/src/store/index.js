import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

function storeLocalStore (state) {
  window.localStorage.setItem("userMsg",JSON.stringify(state));
}


export default new Vuex.Store({
  state: {
    changeSelectStyle:0
  },
  mutations: {
    storeChangeSelectStyle (state,changeSelectStyle) {
      state.changeSelectStyle = changeSelectStyle
      storeLocalStore (state)
    }
  },
  actions: {
  },
  modules: {
  }
})
