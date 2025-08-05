<template>
    <v-container class="fill-height" fluid>
        <v-row align="center" justify="center">
            <v-col cols="12" sm="8" md="4">
                <v-card>
                    <v-card-title >Sign Up</v-card-title>
                    <v-card-text>
                        <v-form @submit.prevent="signup">
                            <v-text-field v-model="name" label="Name" required ></v-text-field>
                            <v-text-field v-model="email" label="Email" type="email" required ></v-text-field>
                            <v-text-field v-model="password" label="Password" type="password" required ></v-text-field>
                            <v-btn color="primary" type="submit" class="mt-4" block>Sign Up</v-btn>
                        </v-form>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import axios from 'axios';
export default {
    // eslint-disable-next-line vue/multi-word-component-names
    name: "Signup",
    data() {
        return {
                name: "",
                email: "",
                password: "",
        };
    },
    methods: {
        async signup() {
            console.log("Form submitted:", this.form);
            try {
                const response = await axios.post("http://localhost:8081/api/auth/signup", {
                    name: this.name,
                    email: this.email,
                    password: this.password
                });
                const token = response.data.token;
                localStorage.setItem("token", token);
                localStorage.setItem("name", this.name);
                console.log("Signup successful", response.data);
                this.$router.push("/dashboard");
                //Login(this.form.email, this.form.password);
            } catch (error) {
                console.error("Signup Failed", error.response?.data || error.data);
                alert("Signup Failed: " + (error.response?.data?.message || "Check your Details"));
            }

        },
    },
};
</script>