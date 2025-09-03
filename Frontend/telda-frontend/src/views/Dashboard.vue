<template>
  <v-container class="py-10">
    <v-row justify="center">
      <v-col cols="12" md="8">
        <v-card>
          <v-card-title class="text-h5">
            Welcome, {{ userName }}!
          </v-card-title>

          <v-card-subtitle>
            Your current balance is:
          </v-card-subtitle>

          <v-card-text class="text-h4">
            {{ balance.balance }} EGP
          </v-card-text>

          <v-divider></v-divider>

          <v-card-text>
            <v-row class="mt-4" justify="center" align="center">
              <v-col cols="12" sm="6" md="3">
                <v-btn color="green" block @click="$router.push('/deposit')">Deposit</v-btn>
              </v-col>
              <v-col cols="12" sm="6" md="3">
                <v-btn color="red" block @click="$router.push('/withdraw')">Withdraw</v-btn>
              </v-col>
              <v-col cols="12" sm="6" md="3">
                <v-btn color="blue" block @click="$router.push('/send')">Send Money</v-btn>
              </v-col>
              <v-col cols="12" sm="6" md="3" >
                <v-btn color="primary" block @click="$router.push({path:'/transactions' , query: {mode : 'user'}})">History</v-btn>
              </v-col>
              <v-col cols="12" sm="6" md="4" v-if="role === 'ADMIN'">
                <v-btn color="primary" block @click="$router.push({path:'/transactions' , query: {mode : 'all'}})">View all Transactions</v-btn>
              </v-col>
              <v-col cols="12" sm="6" md="4" v-if="role === 'ADMIN'">
                <v-btn color="primary" block @click="$router.push('/admin/users')">Manage Users</v-btn>
              </v-col>
              <v-col cols="12" sm="6" md="4" v-if="role === 'ADMIN'">
                <v-btn color="primary" block @click="$router.push('/transactions')">Statistics / Reports</v-btn>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Dashboard",
  data() {
    return {
      userName: "",
      balance: 0,
      role: ""
    };
  },
  async created() {
    // هنا هتجيب اسم المستخدم لو مخزنه في localStorage
    this.userName = localStorage.getItem("name") || "User";
    console.log(localStorage.getItem("name"));
    // جلب الرصيد
    const token = localStorage.getItem("token");

    // Get Role
    const decoded = jwtDecode(token);
    this.role = decoded.role;
    console.log(this.role);
    try {
      const response = await axios.get("http://localhost:8081/api/user/balance", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      this.balance = response.data;
    } catch (err) {
      console.error("Failed to fetch balance", err);
    }
  }
};
</script>
