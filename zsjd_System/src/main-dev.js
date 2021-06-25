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
  uiVersion: '1.0'
});

import axios from "axios";
Vue.prototype.$axios = axios


// 配置请求根路径
// 监督系统
let supervisionInstance = axios.create({
  baseURL: 'http://192.168.8.166:8765/api/'
});

// 图片地址
Vue.prototype.$baseImgUrl = 'http://192.168.8.166:8765/api/comSys';
// 追溯二维码地址
Vue.prototype.$baseQrUrl = 'http://192.168.8.166:8093/';

// 配置拦截器
// 监督系统
supervisionInstance.interceptors.request.use(config => {
  config.headers.Authorization = window.sessionStorage.getItem('supervisionToken');
  // 在最后必须 return config
  return config
});



//格式化时间
Date.prototype.Format = function (fmt) {
  let o = {
    "M+": this.getMonth() + 1, //月份
    "d+": this.getDate(), //日
    "h+": this.getHours(), //小时
    "m+": this.getMinutes(), //分
    "s+": this.getSeconds(), //秒
    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
    "S": this.getMilliseconds() //毫秒
  };
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  }
  for (let k in o) {
    if (new RegExp("(" + k + ")").test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    }
  }
  return fmt;
}


Vue.prototype.$http = supervisionInstance;


Vue.config.productionTip = false;

new Vue({
  router,
  render: h => h(App)
}).$mount("#app");