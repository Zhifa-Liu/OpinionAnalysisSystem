import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import axios from "axios";
import dataV from '@jiaminghi/data-view';
Vue.use(dataV);
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(ElementUI);

// 按需引入vue-awesome图标
import Icon from 'vue-awesome/components/Icon';
import 'vue-awesome/icons/chart-bar.js';
import 'vue-awesome/icons/chart-area.js';
import 'vue-awesome/icons/chart-pie.js';
import 'vue-awesome/icons/chart-line.js';
import 'vue-awesome/icons/align-left.js';

// 全局注册图标
Vue.component('icon', Icon);

// 适配flex
import '@/common/flexible.js';

// 引入全局css
import './assets/scss/style.scss';


//引入echart
import echarts from 'echarts'
Vue.prototype.$echarts = echarts;

Vue.config.productionTip = false;

// axios.defaults.baseURL = 'http://localhost:8000';
axios.defaults.baseURL = 'http://master:8888/api/';

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app');

