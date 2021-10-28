import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
import axios from '@/axios/http.js'
import VueVideoPlayer from 'vue-video-player'
import store from './store/store.js'



Vue.use(VueVideoPlayer)

// Vue.use(Vuex)



axios.defaults.withCredentials = true


// Vue.prototype.$store = store

Vue.prototype.$axios = axios

Vue.prototype.$location = 'http://127.0.0.1'

Vue.prototype.$token = ''

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
