<template>
  <v-container class="py-10">
    <v-row justify="center">
      <v-col cols="12" md="8">
        <v-card>
          <v-card-title class="text-h5">
            Edit Profile
          </v-card-title>

          <v-card-text>
            <v-form v-if="user" @submit.prevent="updateProfile">
              <v-text-field
                label="Name"
                v-model="form.name"
                :disabled="loading"
              />
              <v-text-field
                label="Email"
                v-model="form.email"
                type="email"
                :disabled="loading"
              />
              <v-text-field
                label="Phone"
                v-model="form.phone"
                type="tel"
                :disabled="loading"
              />

              <v-text-field
                label="Created At"
                :model-value="user.createdAt"
                disabled
              />

              <v-chip
                class="mt-2"
                :color="user.active ? 'green' : 'red'"
                label
              >
                {{ user.active ? "Active" : "Inactive" }}
              </v-chip>

              <div class="mt-6 d-flex gap-4">
                <v-btn type="submit" color="primary" :loading="loading">
                  Save
                </v-btn>
                <v-btn color="secondary" @click="resetForm" :disabled="loading">
                  Reset
                </v-btn>
              </div>
            </v-form>

            <v-alert v-if="error" type="error" class="mt-4">
              {{ error }}
            </v-alert>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Snackbar واحد فقط -->
    <v-snackbar v-model="snackbar.show" :color="snackbar.color" timeout="3000">
      {{ snackbar.text }}
    </v-snackbar>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  name: "EditProfile",
  data() {
    return {
      user: null,
      form: { name: "", email: "", phone: "" },
      loading: false,
      error: null,
      snackbar: { show: false, color: "success", text: "" },
    };
  },
  async mounted() {
    await this.fetchProfile();
  },
  methods: {
    api() {
      const token = localStorage.getItem("token") || "";
      return axios.create({
        baseURL: import.meta.env.VITE_API_BASE || "http://localhost:8081",
        headers: { Authorization: `Bearer ${token}` },
      });
    },

    async fetchProfile() {
      this.loading = true;
      this.error = null;
      try {
        const { data } = await this.api().get("/api/user/profile");
        this.user = data;
        this.form = {
          name: data.name,
          email: data.email,
          phone: data.phone,
        };
      } catch (err) {
        this.error = err?.response?.data?.message || "Failed to load profile.";
      } finally {
        this.loading = false;
      }
    },

    async updateProfile() {
      this.loading = true;
      this.error = null;
      try {
        await this.api().put("/api/user/profile", this.form);
        this.notify("Profile updated successfully", "success");
        await this.fetchProfile(); // refresh data
      } catch (err) {
        this.error = err?.response?.data?.message || "Update failed.";
        this.notify(this.error, "error");
      } finally {
        this.loading = false;
      }
    },

    resetForm() {
      if (this.user) {
        this.form = {
          name: this.user.name,
          email: this.user.email,
          phone: this.user.phone,
        };
      }
    },

    notify(text, level = "success") {
      this.snackbar = {
        show: true,
        text,
        color:
          level === "error"
            ? "red"
            : level === "warning"
            ? "orange"
            : "green",
      };
    },
  },
};
</script>
