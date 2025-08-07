<script setup>
import { jwtDecode } from 'jwt-decode';
import { onMounted, ref } from 'vue';
import { RouterView, useRouter } from 'vue-router'

const router = useRouter();
const isLoggedIn = ref(false);

onMounted(() => {
  const token = localStorage.getItem("token");
  if (token){
    try {
      const decode = jwtDecode(token);
      const exp = decode.exp * 1000;

      isLoggedIn.value = Date.now() < exp;
    } catch {
      isLoggedIn.value = false;
    }
  }
});

function logout(){
  localStorage.removeItem("token");
  isLoggedIn.value = false;
  router.push("/login");
}
</script>

<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <v-toolbar-title @click="$router.push('/dashboard')" style="cursor: pointer;">Telda</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn v-if="!isLoggedIn" text to="/login" router>Login</v-btn>
      <v-btn v-if="!isLoggedIn" text to="/signup" router>Sign Up</v-btn>
      <v-btn v-else text @click="logout">Logout</v-btn>

    </v-app-bar>

    <v-main>
      <RouterView />
    </v-main>
  </v-app>
</template>

<style scoped>
</style>
