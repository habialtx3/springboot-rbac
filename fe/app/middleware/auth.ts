// middleware/auth.ts
import { useAuthStore } from '~/stores/auth'

export default defineNuxtRouteMiddleware((to, from) => {
  const authStore = useAuthStore()

  // Jika user belum login DAN mencoba mengakses halaman selain '/login', tendang ke '/login'
  if (!authStore.isAuthenticated && to.path !== '/login') {
    return navigateTo('/login')
  }
})