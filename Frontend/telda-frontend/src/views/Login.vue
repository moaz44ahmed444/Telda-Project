<template>
    <v-container class="fill-height" fluid>
        <v-row align="center" justify="center">
            <v-col cols="12" sm="8" md="4">
                <v-card>
                    <v-card-title>Login</v-card-title>
                    <v-card-text>
                        <v-form @submit.prevent="login">
                            <v-text-field v-model="email" label="Email" type="email" required></v-text-field>
                            <v-text-field v-model="password" label="Password" type="password" required></v-text-field>
                            <v-btn type="submit" color="primary" block>Login</v-btn>
                        </v-form>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template> 
<script>
import axios from 'axios';
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
            try{
                const response = await axios.post("http://localhost:8081/api/auth/login", {
                    email: this.email,
                    password: this.password
                });

                const token = response.data.token;
                localStorage.setItem("token",token);

                console.log("Login successfull", response.data);

                this.$router.push("/dashboard");
            } catch (error) {
                console.error("Login Failed",error.response?.data || error.data);
                alert("Login Failed: " + (error.response?.data?.message || "Check your Credentials"));
            }
        },
    },
};
</script>

<style scoped src="../assets/login.css"></style>
