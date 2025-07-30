<script setup>
import { RouterView } from 'vue-router'
</script>

<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <v-toolbar-title>Telda</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn text to="/login" router>Login</v-btn>
      <v-btn text to="/signup" router>Sign Up</v-btn>
    </v-app-bar>

    <v-main>
      <RouterView />
    </v-main>
  </v-app>
</template>

<style scoped>
</style>
