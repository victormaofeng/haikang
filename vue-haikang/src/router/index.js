import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

Vue.use(VueRouter)

const routes = [{
    path: '/home',
    name: 'Home',
    component: Home,
    children: [{
        path: '/',
        redirect: 'index'
      },
      {
        path: 'index',
        component: () => import('../views/home/Index.vue'),
        meta: {
          title: ''
        }
      },
      {
        path: 'live',
        component: () => import('../views/home/Live.vue'),
        meta: {
          title: ''
        }
      },
      {
        path: 'detection',
        component: () => import('../views/home/Detection.vue'),
        meta: {
          title: ''
        }
      },
      {
        path: 'message',
        component: () => import('../views/home/Message.vue'),
        meta: {
          title: ''
        }
      },
      {
        path: 'messageIndex',
        component: () => import('../views/home/Messageindex.vue'),
        meta: {
          title: ''
        }
      },
      {
        path: 'upload1',
        component: () => import('../views/home/Upload1.vue'),
        meta: {
          title: ''
        }
      },
      {
        path: 'upload2',
        component: () => import('../views/home/Upload2.vue'),
        meta: {
          title: ''
        }
      },
      {
        path: 'algorithm1',
        component: () => import('../views/home/Algorithm1.vue'),
        meta: {
          title: ''
        }
      },
      {
        path: 'algorithm2',
        component: () => import('../views/home/Algorithm2.vue'),
        meta: {
          title: ''
        }
      }
    ]
  },
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  }
]

const router = new VueRouter({
  routes,
  mode: "history"
})

export default router
