import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
import axios from '@/axios/http.js'
import VueVideoPlayer from 'vue-video-player'


Vue.use(VueVideoPlayer)

axios.defaults.withCredentials = true

Vue.prototype.$axios = axios

Vue.prototype.$location = 'http://127.0.0.1'

Vue.prototype.$token = ''

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
