import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Dashboard from '@/views/Dashboard.vue'
import Signup from '@/views/Signup.vue'
import SendMoney from '@/views/SendMoney.vue'
import Deposit from '@/views/Deposit.vue'
import Withdraw from '@/views/Withdraw.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login,
    },
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: Dashboard,
    },
    {
      path: '/signup',
      name: 'Signup',
      component: Signup,
    },
    {
      path: '/send',
      name: 'sendMoney',
      component: SendMoney,
    },
    {
      path: '/deposit',
      name: 'Deposit',
      component: Deposit
    },
    {
      path: '/withdraw',
      name: 'WithdrawMoney',
      component: Withdraw,
    },
  ],
})

export default router
