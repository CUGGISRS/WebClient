import request from '@/globals/request/axios.js';
import API from '@/globals/request/api.js';
import DataStore from '@/globals/storage/index';

const AuthService = {
  smsSend: function({ phone }) {
    return request.post(API.smsSend, {
      phone
    });
  },
  smsLogin: function({ phone, code }) {
    return request
      .post(API.smsLogin, {
        phone,
        code
      })
      .then(res => {
        DataStore.setToken(res.token);
        return res;
      });
  },
  wechatAuthUrl: function(params) {
    return request.get(API.socialWechatUrl, params);
  },
  wechatAuth: function(params) {
    return request.get(API.socialWechat, params).then(res => {
      DataStore.setToken(res.token);
      return res;
    });
  },
  wechatAuthBind: function(params) {
    return request.get(API.userWechatBind, params);
  },
  wechatAuthUnBind: function(params) {
    return request.delete(API.userWechatBind, params);
  },
  phoneBind: function(params) {
    return request.put(API.userPhoneBind, params);
  }
};

export default AuthService;
