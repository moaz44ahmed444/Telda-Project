<template>
    <v-container class="py-10">
        <v-row justify="center">
            <v-col cols="12" md="8">
                <v-card>
                    <v-card-title class="text-h5">Transaction History</v-card-title>
                    <v-card-text>
                        <v-table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Sender</th>
                                    <th>Receiver</th>
                                    <th>Amount</th>
                                    <th>Type</th>
                                    <th>Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="tx in transactions" :key="tx.transactionId">
                                    <td>{{ tx.transactionId }}</td>
                                    <td>{{ tx.senderEmail || 'N/A' }}</td>
                                    <td>{{ tx.receiverEmail || 'N/A' }}</td>
                                    <td>{{ tx.amount }}</td>
                                    <td>{{ tx.type }}</td>
                                    <td>{{ formatDate(tx.timestamp) }}</td>
                                </tr>
                            </tbody>
                        </v-table>

                        <div v-if="loading" class="text-center mt-4">
                            Loading...
                        </div>
                        <div v-if="error" class="text-red mt-4">
                            {{ error }}
                        </div>
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
    name: "TransactionHistory",
    data() {
        return {
            transaction: [],
            loading: false,
            error: null
        };
    },
    async mounted() {
        this.fetchTransactions();
    },
    methods: {
        async fetchTransactions(){
            this.loading = true;
            this.error = null;

            const token = localStorage.getItem("token")
            const decoded = jwtDecode(token);
            const email = decoded.sub || decoded.email;

            try{
                const res = await axios.get("http://localhost:8081/api/transaction/user/transactions", {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });

                this.transactions = res.data;
            } catch (err) {
                this.error = "Failed to load transactions.";
                console.error(err.response?.data || err.message);
            } finally {
                this.loading = false;
            }
        },
        formatDate(dateString) {
            const date = new Date(dateString);
            return date.toLocaleString();
        }
    }
    
}
</script>