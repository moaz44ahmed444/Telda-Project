<template>
  <v-container class="py-10">
    <v-row justify="center">
      <v-col cols="12" md="10">
        <v-card>
          <v-card-title class="text-h5 d-flex align-center justify-space-between">
            <span>Manage Users</span>
            <div class="d-flex align-center" style="gap: 12px; width: 60%">
              <v-text-field
                v-model="q"
                density="comfortable"
                variant="outlined"
                label="Search (email / name)"
                prepend-inner-icon="mdi-magnify"
                clearable
                hide-details
                style="max-width: 280px;"
              />
              <v-btn color="primary" :loading="loading" @click="fetchUsers">
                <v-icon start>mdi-refresh</v-icon>Refresh
              </v-btn>
            </div>
          </v-card-title>

          <v-card-text>
            <v-table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Email</th>
                  <th>Name</th>
                  <th>Balance</th>
                  <th>Role</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="u in filteredUsers" :key="u.id">
                  <td>{{ u.id }}</td>
                  <td>{{ u.email }}</td>
                  <td>{{ u.name }}</td>
                  <td>{{ u.balance || '—' }}</td>
                  <td>
                    <v-chip
                      :color="u.role === 'ADMIN' ? 'deep-purple' : 'blue'"
                      size="small"
                      variant="flat"
                    >
                      {{ u.role }}
                    </v-chip>
                  </td>
                  <td>
                    <div class="d-flex align-center" style="gap:10px;">
                      <v-switch
                        hide-details
                        density="compact"
                        color="success"
                        :model-value="u.active"
                        @update:model-value="onToggleActive(u, $event)"
                        :disabled="rowBusy[u.id] || loading"
                      />
                      <v-chip
                        :color="u.active ? 'green' : 'grey'"
                        size="small"
                        variant="flat"
                      >
                        {{ u.active ? 'Active' : 'Inactive' }}
                      </v-chip>
                    </div>
                  </td>
                </tr>
                <tr v-if="!loading && filteredUsers.length === 0">
                  <td colspan="6" class="text-center py-6">No users found.</td>
                </tr>
              </tbody>
            </v-table>

            <div v-if="loading" class="text-center mt-4">Loading...</div>
            <div v-if="error" class="text-red mt-4">{{ error }}</div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" timeout="3000">
      {{ snackbar.text }}
    </v-snackbar>
  </v-container>
</template>

<script>
import axios from "axios";
import { jwtDecode } from "jwt-decode";

export default {
  name: "ManageUsers",
  data() {
    return {
      users: [],
      loading: false,
      error: null,
      q: "",
      rowBusy: {},
      snackbar: { show: false, color: "success", text: "" },
    };
  },
  computed: {
    filteredUsers() {
      if (!this.q) return this.users;
      const s = this.q.toLowerCase();
      return this.users.filter(
        (u) =>
          (u.email || "").toLowerCase().includes(s) ||
          (u.name || "").toLowerCase().includes(s)
      );
    },
  },
  async mounted() {
    try {
      const token = localStorage.getItem("token");
      if (!token) return this.$router.push({ name: "Login" });
      const decoded = jwtDecode(token);
      const role = decoded.role;
      if (role !== "ADMIN") {
        this.notify("You are not authorized to access Manage Users", "error");
        return this.$router.push({ name: "Home" });
      }
    } catch {
      return this.$router.push({ name: "Login" });
    }
    this.fetchUsers();
  },
  methods: {
    api() {
      const token = localStorage.getItem("token") || "";
      return axios.create({
        baseURL: import.meta.env.VITE_API_BASE || "http://localhost:8081",
        headers: { Authorization: `Bearer ${token}` },
      });
    },

    async fetchUsers() {
      this.loading = true;
      this.error = null;
      try {
        const { data } = await this.api().get("/api/admin/users");
        this.users = Array.isArray(data) ? data : data?.content ?? [];
      } catch (err) {
        this.error =
          err?.response?.data?.message ||
          err.message ||
          "Failed to load users.";
      } finally {
        this.loading = false;
      }
    },

async onToggleActive(u, nextValue) {
  const prev = u.active;
  this.setRowBusy(u.id, true);
  u.active = nextValue;
  try {
    const endpoint = nextValue
      ? `/api/admin/users/${u.id}/activate` 
      : `/api/admin/users/${u.id}/deactivate`;

    await this.api().put(endpoint);

    this.notify(
      `User #${u.id} ${nextValue ? "activated" : "deactivated"} successfully.`,
      "success"
    );
  } catch (err) {
    u.active = prev; // roll back
    this.notify(
      err?.response?.data || "Failed to update status.",
      "error"
    );
  } finally {
    this.setRowBusy(u.id, false);
  }
},

    setRowBusy(id, v) {
      this.rowBusy = { ...this.rowBusy, [id]: v };
    },

    notify(text, level = "success") {
      this.snackbar = {
        show: true,
        text,
        color: level === "error" ? "red" : level === "warning" ? "orange" : "green",
      };
    },
  },
};
</script>
