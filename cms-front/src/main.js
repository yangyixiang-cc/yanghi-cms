import {
  createApp
} from 'vue';
import App from './App.vue';
import router from './router';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import {
  createPinia
} from "pinia";
import '@/assets/main.css';
import Antd from 'ant-design-vue';
// 引入组件库全局样式资源
import 'ant-design-vue/dist/antd.css';
import * as antIcons from '@ant-design/icons-vue'
const app = createApp(App);
Object.keys(antIcons).forEach(key => {
  app.component(key, antIcons[key])
})
// 添加到全局
app.config.globalProperties.$antIcons = antIcons
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
app.use(router).use(pinia).use(Antd).mount('#app');
