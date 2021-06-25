import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import ElementUI from 'element-ui';
// import VueResource from 'vue-resource';
import 'element-ui/lib/theme-chalk/index.css';
import '@/styles/reset.less';
import '@/styles/common.less';
import '@/assets/fonts/iconfont.css';
import axios from 'axios';
import '@/assets/css/global.css';

// 导入nprogress
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';

import moment from 'moment';

// 导入富文本编辑器
import VueQuillEditor from 'vue-quill-editor';
// 导入富文本编辑器对应的样式
import 'quill/dist/quill.core.css'; // import styles
import 'quill/dist/quill.snow.css'; // for snow theme
import 'quill/dist/quill.bubble.css'; // for bubble theme
// 将富文本编辑器，注册为全局可以的组件
Vue.use(VueQuillEditor);
Vue.prototype.$moment = moment;
Vue.use(ElementUI);
// Vue.use(VueResource);

// 在request拦截器中显示进度条 NProgress.start()
axios.interceptors.request.use(config => {
  NProgress.start();
  config.headers.Authorization = window.sessionStorage.getItem('token');
  return config;
});
// 在response拦截器中隐藏进度条 NProgress.done()
axios.interceptors.response.use(config => {
  NProgress.done();
  return config;
});
Vue.prototype.$http = axios;

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
