<!-- pages/login.vue -->
<script setup lang="ts">
import { useAuthStore } from '~/stores/auth'
import type { WebResponse, LoginData } from '~/types/auth'

const username = ref('')
const password = ref('')
const errorMessage = ref('')
const loading = ref(false)

const authStore = useAuthStore()

async function handleLogin() {
  errorMessage.value = ''
  loading.value = true

  try {
    // Menembak API Spring Boot kamu menggunakan useFetch bawaan Nuxt
    const { data, error } = await useFetch<WebResponse<LoginData>>('http://localhost:8080/api/auth/signin', {
      method: 'POST',
      body: {
        username: username.value,
        password: password.value
      }
    })

    // Jika terjadi error dari server (misal 401 Unauthorized atau 400 Bad Request)
    if (error.value) {
      errorMessage.value = error.value.data?.errors || 'Terjadi kesalahan saat login.'
      return
    }

    // Jika login sukses dan token didapatkan
    if (data.value && data.value.data?.token) {
      authStore.setToken(data.value.data.token)
      // Redirect ke halaman dashboard terproteksi
      navigateTo('/dashboard')
    }
  } catch (err) {
    errorMessage.value = 'Gagal terhubung ke server backend.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="min-h-screen bg-slate-900 flex items-center justify-center p-4">
    <div class="max-w-md w-full bg-slate-800 rounded-2xl p-8 border border-slate-700 shadow-xl">
      <h2 class="text-3xl font-bold text-center text-sky-400 mb-2">Welcome Back</h2>
      <p class="text-slate-400 text-center mb-6 text-sm">Masuk ke aplikasi Boilerplate RBAC</p>

      <form @submit.prevent="handleLogin" class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-slate-300 mb-1">Username</label>
          <input v-model="username" type="text" required
                 class="w-full bg-slate-950 border border-slate-700 rounded-lg px-4 py-2 text-slate-200 focus:outline-none focus:border-sky-500" />
        </div>

        <div>
          <label class="block text-sm font-medium text-slate-300 mb-1">Password</label>
          <input v-model="password" type="password" required
                 class="w-full bg-slate-950 border border-slate-700 rounded-lg px-4 py-2 text-slate-200 focus:outline-none focus:border-sky-500" />
        </div>

        <div v-if="errorMessage" class="text-red-400 text-sm bg-red-950/50 border border-red-800 p-3 rounded-lg">
          {{ errorMessage }}
        </div>

        <button type="submit" :disabled="loading"
                class="w-full bg-sky-500 hover:bg-sky-600 disabled:bg-sky-800 text-white font-medium py-2 rounded-lg transition duration-200">
          {{ loading ? 'Memproses...' : 'Sign In' }}
        </button>
      </form>
    </div>
  </div>
</template>