import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import "./plugins/element.js";

// 导入全局样式表
import "./assets/css/global.css";
// 导入字体样式
import "./assets/fonts/iconfont.css";

// 引入地图插件
import VueAMap from "vue-amap";
Vue.use(VueAMap);
VueAMap.initAMapApiLoader({
  key: 'd700ed8f81aa62669b118978cefd82b7',
  plugin: ['AMap.Scale', 'AMap.OverView', 'AMap.ToolBar', 'AMap.MapType', 'AMap.PlaceSearch', 'AMap.Geolocation', 'AMap.Geocoder'],
  v: '1.4.4',
  uiVersion: '1.0'});

import axios from "axios";
// 配置请求根路径
// 追溯系统
let retrospectInstance = axios.create({
  baseURL: 'http://192.168.8.166:8765/api/'
});
// 图片地址
Vue.prototype.$baseImgUrl = 'http://192.168.8.166:8765/api/comSys';
// 追溯二维码地址
Vue.prototype.$baseQrUrl = 'http://192.168.8.166:8093/';
// 配置拦截器
// 追溯系统
retrospectInstance.interceptors.request.use(config => {
  config.headers.Authorization = window.sessionStorage.getItem('retrospectToken');
  // 在最后必须 return config
  return config
});

Vue.prototype.$http = retrospectInstance;

Vue.config.productionTip = false;

new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
