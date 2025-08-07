<template>
    <v-container class="py-10">
        <v-row justify="center">
            <v-col cols="12" md="6">
                <v-card>
                    <v-card-title class="text-h5"> Withdraw Money</v-card-title>
                    <v-card-text>
                        <v-form @submit.prevent="withdrawMoney">
                            <v-text-field
                                v-model="amount"
                                label="Amount"
                                type="number"
                                reqired
                            ></v-text-field>

                            <v-text-field
                                v-model="password"
                                label="Password"
                                type="password"
                                required
                            ></v-text-field>

                            <v-btn color="blue" type="submit" class="mt-4" block>Withdraw</v-btn>
                        </v-form>
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
    name: "WithdrawMoney",
    data() {
        return {
            amount: 0,
            password: ""
        };
    },
    methods: {
        async withdrawMoney() {
            const token = localStorage.getItem("token");
            const decoded = jwtDecode(token);

            const email = decoded.sub || decoded.email;

            try {
                await axios.post("http://localhost:8081/api/transaction/withdraw",
                    {
                        receiverEmail: email,
                        amount: this.amount,
                        password: this.password
                    },
                    {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    }
                );

                alert("Withdraw successful!");
                this.$router.push("/dashboard");

            } catch (err) {
                console.error("Withdraw failed", err.response?.data);
                alert("Error: " + (err.response?.data || "Withdraw failed."));
            }
        }

    }
}
</script>