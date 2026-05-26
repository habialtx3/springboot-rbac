// types/auth.ts

// Format standar WebResponse dari backend
export interface WebResponse<T> {
  code: number;
  status: string;
  data: T | null;
  errors: string | null;
}

// Data spesifik yang dikembalikan saat berhasil login
export interface LoginData {
  token: string;
}