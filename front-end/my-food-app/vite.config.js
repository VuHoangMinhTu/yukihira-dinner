import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'
// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      // 2. Sử dụng path.resolve để trỏ thẳng vào thư mục src
      "~": path.resolve(__dirname, "./src"),
    },
  },
})