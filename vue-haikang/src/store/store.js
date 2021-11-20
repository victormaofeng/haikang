import Vuex from 'vuex'
import Vue from 'vue'

Vue.use(Vuex)
const store = new Vuex.Store({
  state: {
    token: "",
    user: null,
    messages: [],
    messageCount: 0,
  },
  mutations: {
    changeToken(state, token) {
      console.log("store:", token)
      state.token = token
    },
    setUser(state, user) {
      state.user = user
    },
    addMessage(state, msg) {
      console.info("store.message", msg);
      state.messages.unshift(msg)
    },
    addMessageCount(state) {
      state.messageCount = state.messageCount + 1
    },
    setMessageCount(state, count) {
      state.messageCount = count
    }
  }
})


export default store
