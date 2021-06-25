import Vue from "vue";
import App from "./App.vue";
import router from "./router";

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

// 导入全局样式表
import "./assets/css/global.css";
// 导入字体样式
import "./assets/fonts/iconfont.css";

Vue.config.productionTip = false
Vue.use(ElementUI)

// 引入地图插件
import VueAMap from "vue-amap";
Vue.use(VueAMap);
import VGauge from "vgauge";
Vue.use(VGauge);
import echarts from 'echarts';
import 'echarts-liquidfill';
Vue.prototype.$echarts = echarts;
import {
  sensors_config
} from "./assets/js/sensorsConfig"
Vue.prototype.$sc = sensors_config;
VueAMap.initAMapApiLoader({
  key: 'd700ed8f81aa62669b118978cefd82b7',
  plugin: ['AMap.Scale', 'AMap.OverView', 'AMap.ToolBar', 'AMap.MapType', 'AMap.PlaceSearch', 'AMap.Geolocation', 'AMap.Geocoder'],
  v: '1.4.4',
  uiVersion: '1.0'
});

import axios from "axios";
// 配置请求根路径
// 物联网系统
let ioTInstance = axios.create({
  baseURL: 'http://47.105.215.208:8005/'
});
// 追溯系统
let retrospectInstance = axios.create({
  baseURL: 'http://192.168.8.166:8765/api/'
  // baseURL: 'http://39.104.61.47:8765/api/'
});
// 视频监控
let videoInstance = axios.create({
  headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
  },
  baseURL: 'https://open.ys7.com/'
});

// 配置拦截器
// 物联网系统
ioTInstance.interceptors.request.use(config => {
  config.headers.token = window.sessionStorage.getItem('ioTToken');
  // 在最后必须 return config
  return config
});
// 追溯系统
retrospectInstance.interceptors.request.use(config => {
  config.headers.Authorization = window.sessionStorage.getItem('retrospectToken');
  // 在最后必须 return config
  return config
});

Vue.prototype.$http = retrospectInstance;
Vue.prototype.$http2 = ioTInstance;
Vue.prototype.$http3 = videoInstance;

Vue.config.productionTip = false;

new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
