<template>
    <v-container class="py-10">
        <v-row justify="center">
            <v-col cols="12" md="6">
                <v-card>
                    <v-card-title class="text-h5">Deposit Money</v-card-title>
                    <v-card-text>
                        <v-form @submit.prevent="depositMoney">
                            <v-text-field
                                v-model="amount"
                                label="Amount"
                                type="number"
                                required
                            ></v-text-field>

                            <v-btn color="green" type="submit" class="mt-4" block>Deposit</v-btn>
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
    name: "DepositMoney",
    data() {
        return {
            amount: 0
        };
    },
    methods: {
        async depositMoney() {
            const token = localStorage.getItem("token");
            const decoded = jwtDecode(token);

            const email = decoded.sub || decoded.email;

            try {
                await axios.post("http://localhost:8081/api/transaction/deposit",
                    {
                        receiverEmail: email,
                        amount: this.amount
                    },
                    {
                        headers: {
                        Authorization: `Bearer ${token}`
                        }
                    }
                );

                alert("Deposit added successfully!");
                this.$router.push("/dashboard");

            } catch (err) {
                console.error("Failed to add Deposit", err.response?.data);
                alert("Error: " + (err.response?.data?.message || "Sending failed."));
            }
        }
    }
    
}
</script>