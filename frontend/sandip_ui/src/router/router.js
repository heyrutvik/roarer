import Vue from 'vue'
import VueRouter from 'vue-router'
import Dashboard from '@component/Dashboard'
import Login from '@component/Login'
import {isLogin} from '@apis/api'

Vue.use(VueRouter)

const routes = [
    { 
      name: 'root',
      path: '/', 
      component:  Dashboard
    },
    { 
      name: 'login',
      path: '/login', 
      component:  Login
    },
    {
      name: 'not-found',
      path: '*'
    }
  ]

let router = new VueRouter({
  routes,
  mode: 'history'
})

router.beforeEach((to, from, next) => {
  if(!isLogin() && to.name != "login"){
    next({name: 'login'})
  }else{
    if(isLogin() && to.name === "login" || to.name === 'not-found'){
      next({name: 'root'})
    }else{
      next()
    }
  }
})

export default router