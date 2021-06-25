import request from '@/globals/request/axios.js';
import API from '@/globals/request/api.js';

const userService = {
  searchSuggest(params) {
    return request.post(API.searchSuggest, params);
  },
};
export default userService;