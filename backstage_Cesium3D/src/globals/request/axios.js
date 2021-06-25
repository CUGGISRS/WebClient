import axios from "axios";
import { Message } from "element-ui";
import DataStore from "@/globals/storage/index";

const axiosInstance = axios.create();
axiosInstance.defaults.timeout = 20000;

axiosInstance.interceptors.request.use(
  config => {
    const newConfig = {
      ...config
    };
    const TOKEN = DataStore.getToken();
    if (TOKEN) {
      // 设置统一的请求header
      newConfig["headers"]["Authorization"] =  TOKEN;
      //config.headers.Authorization =TOKEN //授权（每次请求把sessionId带给后台）
    }
    return newConfig;
  },
  error => Promise.reject(error)
);

const handleErrorRequest = error => {
  const { response } = error;
  const status = response ? response.status : 408;
  if (response) {
    const { data } = response;
    const message = data.message || "服务器发送错误，请稍后再试";
    if (status === 401) {
      Message.error("登录状态过期，请重新登录");
      DataStore.clear();
    } else if (status === 403) {
      Message.error("没有权限，联系管理员");
    } else {
      Message.error(message);
    }
  } else {
    Message.error("网络超时");
  }
};

const successRes = res => {
  switch (true) {
    case res.data.error_code === 0:
      return res.data.data;
    case res.data.error_code === 1:
      Message.error(res.data.message);
      return Promise.reject(res.data);
    default:
      return res.data;
  }
};
const errorRes = error => {
  handleErrorRequest(error);
  return Promise.reject(error);
};

axiosInstance.interceptors.response.use(successRes, errorRes);

export default {
  post(url = "", data = {}, config = {}) {
    return axiosInstance.post(url, data, config);
  },
  put(url = "", data = {}, config = {}) {
    return axiosInstance.put(url, data, config);
  },
  get(url = "", params = {}, config = {}) {
    const OPTIONS = Object.assign(
      {
        params
      },
      config
    );
    return axiosInstance.get(url, OPTIONS);
  },
  delete(url = "", params = {}, config = {}) {
    const OPTIONS = Object.assign(
      {
        params
      },
      config
    );
    return axiosInstance.delete(url, OPTIONS);
  }
};
