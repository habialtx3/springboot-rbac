// stores/auth.ts
import { defineStore } from 'pinia'
import type { WebResponse, LoginData } from '~/types/auth'

export const useAuthStore = defineStore('auth', () => {
  // Menggunakan cookie agar sinkron antara server (SSR) dan client (browser)
  const token = useCookie<string | null>('auth_token', { default: () => null })
  const user = ref<string | null>(null)

  // Computed properti untuk mengecek apakah user sudah login
  const isAuthenticated = computed(() => !!token.value)

  function setToken(newToken: string) {
    token.value = newToken
  }

  function logout() {
    token.value = null
    user.value = null
    // Setelah logout, tendang user kembali ke halaman login
    navigateTo('/login')
  }

  return { 
    token, 
    user, 
    isAuthenticated, 
    setToken, 
    logout 
  }
})