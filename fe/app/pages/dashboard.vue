<!-- pages/dashboard.vue -->
<script setup lang="ts">
import { useAuthStore } from '~/stores/auth'

// Daftarkan middleware proteksi yang kita buat di Langkah 5
definePageMeta({
  middleware: 'auth'
})

const authStore = useAuthStore()
const secretData = ref('')

// Ambil data rahasia dari endpoint Spring Boot dengan menyertakan JWT Token di Header
const { data, error } = await useFetch<string>('http://localhost:8080/api/test/user', {
  headers: {
    Authorization: `Bearer ${authStore.token}`
  }
})

if (data.value) {
  secretData.value = data.value
} else if (error.value) {
  secretData.value = 'Gagal memuat data terproteksi (403 Forbidden / Unauthorized)'
}
</script>

<template>
  <div class="min-h-screen bg-slate-900 text-slate-100">
    <!-- Navbar -->
    <nav class="bg-slate-800 border-b border-slate-700 px-6 py-4 flex justify-between items-center">
      <h1 class="text-xl font-bold text-sky-400">Dashboard RBAC</h1>
      <button @click="authStore.logout" class="bg-rose-600 hover:bg-rose-700 px-4 py-2 rounded-lg text-sm font-medium transition">
        Logout
      </button>
    </nav>

    <!-- Main Content -->
    <main class="max-w-4xl mx-auto mt-10 p-6 bg-slate-800 rounded-xl border border-slate-700 shadow-lg">
      <h2 class="text-2xl font-semibold mb-4">Status Autentikasi: <span class="text-emerald-400">Aktif (Logged In)</span></h2>
      
      <!-- Box Token -->
      <div class="bg-slate-950 p-4 rounded-lg border border-slate-800 mb-6">
        <p class="text-sm font-mono text-slate-400 break-all">
          <span class="text-amber-400 font-bold">JWT Token Anda:</span> <br>
          {{ authStore.token }}
        </p>
      </div>

      <!-- Box Data Secure Backend -->
      <div class="p-4 bg-sky-950/40 border border-sky-800 rounded-lg">
        <h3 class="text-lg font-medium text-sky-400 mb-1">Data dari Secure Backend (/api/test/user):</h3>
        <p class="text-slate-300 font-medium">{{ secretData }}</p>
      </div>
    </main>
  </div>
</template>