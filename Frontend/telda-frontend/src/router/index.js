import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Dashboard from '@/views/Dashboard.vue'
import Signup from '@/views/Signup.vue'
import SendMoney from '@/views/SendMoney.vue'
import Deposit from '@/views/Deposit.vue'
import Withdraw from '@/views/Withdraw.vue'
import { jwtDecode } from 'jwt-decode'
import Transactions from '@/views/Transactions.vue'
import ManageUsers from '@/views/ManageUsers.vue'
import EditProfile from '@/views/EditProfile.vue'


const router = createRouter({
  history: createWebHistory(),
  routes: [
  { path: '/login', name: 'Login', component: Login },
  { path: '/signup', name: 'Signup', component: Signup },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard, meta: { requiresAuth: true } },
  { path: '/send', name: 'sendMoney', component: SendMoney, meta: { requiresAuth: true } },
  { path: '/deposit', name: 'Deposit', component: Deposit, meta: { requiresAuth: true } },
  { path: '/withdraw', name: 'WithdrawMoney', component: Withdraw, meta: { requiresAuth: true } },
  { path: '/transactions', name: 'Transactions', component: Transactions, meta: { requiresAuth: true } },
  { path: '/admin/users', name: 'ManageUsers', component: ManageUsers, meta: { requiresAuth: true } },
  { path: '/edit-profile', name: 'EditProfile', component: EditProfile, meta: { requiresAuth: true } },
]
});

router.beforeEach((to, from, next) => {

  if(to.meta.requiresAuth) {
    const token = localStorage.getItem("token");
    if(!token){
      return next('/login');
    }

    try {
      const decoded = jwtDecode(token);
      const isExpired = Date.now() >= decoded.exp * 1000;

      if (isExpired) {
        localStorage.removeItem("token");
        return next('/login');
      }
    }catch (err) {
        localStorage.removeItem("token");
        return next('/login');
    }
  }

  next();
})

export default router
