<template>
  <v-container class="py-10">
    <v-row justify="center">
      <v-col cols="12" md="6">
        <v-card>
          <v-card-title class="text-h5">Send Money</v-card-title>
          <v-card-text>
            <v-form @submit.prevent="sendMoney">
              <v-text-field
                v-model="recipient"
                label="Recipient Email"
                required
              ></v-text-field>

              <v-text-field
                v-model="amount"
                label="Amount"
                type="number"
                required
              ></v-text-field>

              <v-btn color="blue" type="submit" class="mt-4" block>Send</v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  name: "SendMoney",
  data() {
    return {
      recipient: "",
      amount: 0
    };
  },
  methods: {
    async sendMoney() {
      const token = localStorage.getItem("token");

      try {
        await axios.post(
          "http://localhost:8081/api/transaction",
          {
            receiverEmail: this.recipient,
            amount: this.amount
          },
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
        );

        alert("Money sent successfully!");
        this.$router.push("/dashboard");

      } catch (err) {
        console.error("Failed to send money", err.response?.data);
        alert("Error: " + (err.response?.data?.message || "Sending failed."));
      }
    }
  }
};
</script>
