<script setup>
import { jwtDecode } from 'jwt-decode';
import { computed, onMounted, ref } from 'vue';
import { RouterView, useRouter } from 'vue-router'

const router = useRouter();
const token = ref(localStorage.getItem("token"))

const isLoggedIn = computed(() => {
  if(!token.value) return false;
  try{
      const decoded = jwtDecode(token.value);
      return Date.now() < decoded.exp * 1000;
    } catch {
      return false;
    }
  }
);


function logout(){
  localStorage.removeItem("token");
  token.value = null;
  router.push("/login");
}

// لما يحصل navigation، جدد قيمة الـ token
router.afterEach(() => {
  token.value = localStorage.getItem("token");
});


window.addEventListener("storage", () => {
  token.value = localStorage.getItem("token")
})
</script>

<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <v-toolbar-title @click="$router.push('/dashboard')" style="cursor: pointer;">Telda</v-toolbar-title>

      <v-spacer></v-spacer>
      <div v-if="!isLoggedIn">
        <v-btn text to="/login" router>Login</v-btn>
        <v-btn text to="/signup" router>Sign Up</v-btn>
      </div>
      <div v-else>
          <v-btn text @click="logout">Logout</v-btn>
      </div>

    </v-app-bar>

    <v-main>
      <RouterView />
    </v-main>
  </v-app>
</template>

<style scoped>
</style>
