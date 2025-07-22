<template>
    <div class="login-page">
        <h2>Login</h2>
        <form @submit.prevent="login">
            <input v-model="email" type="email" placeholder="Email" required />
            <input v-model="password" type="password" placeholder="Password" required />
            <button type="submit">Login</button>  
        </form>
    </div>
</template> 
<script>
export default{
    // eslint-disable-next-line vue/multi-word-component-names
    name: "Login",
    data() {
        return {
            email: "",
            password: ""
        };
    },
    methods: {
        async login() {
            console.log("Logging in with:", this.email, this.password);
        }
    }
};
</script>

<style scoped>
.login-page {
  max-width: 400px;
  margin: auto;
  padding: 2rem;
}
</style>
