import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login,
    },
  ],
})

export default router
