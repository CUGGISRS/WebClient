// axios封装
import axios from 'axios'
import Vue from 'vue'

import { Loading } from 'element-ui';
// 请求拦截// 增加loading  增加header头
axios.interceptors.request.use((config) => {
    // 直接loading
    Loading.service({ fullscreen: true });
})

// 响应拦截
axios.interceptors.response.use((res) => {
    if (res.status === 200) {
        // 成功，返回数据
    } else {
        // 失败，提示用户错误
        // 返回错误信息
    }
})
// 封装get 和 post ，使用promise封装
export default {
    get(obj) {
        new Promise((resolve, reject) => {
            //封装axios  
            axios({
                methods: "GET",
                url: obj.url,
                params: obj.data,
            }).then((res) => {
                // 做一个操作
                resolve(res)
            }).catch((err) => {
                resolve(err)
            })
        })
    },
    post(obj) {
        new Promise((resolve, reject) => {
            //封装axios  
            axios({
                methods: "POST",
                url: obj.url,
                data: obj.data,
            }).then((res) => {
                // 做一个操作
                resolve(res)
            }).catch((err) => {
                resolve(err)
            })
        })
    }
}
Vue.prototype.$axios = axios