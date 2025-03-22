// main.js - Vue 애플리케이션 진입점
import { createApp } from 'vue'
import App from './App.vue'
import './assets/index.css' // Tailwind CSS 스타일

// 앱 생성 및 마운트
const app = createApp(App)
app.mount('#app')