import request from '@/globals/request/axios.js';
import API from '@/globals/request/api.js';
import * as qiniu from 'qiniu-js';

const QINIU_PREFIX = '_for_neon_project/';
const qiniuService = {
  token: () => {
    return request.get(API.qiniuToken);
  },
  upload: ({ file, token, mimeType = null, params = {}, progressCallback }) => {
    // mimeType = ['"image/\*"'];
    return new Promise((resolve, reject) => {
      const fname = file.name;
      const key = QINIU_PREFIX + Date.now() + '_' + fname;
      const config = { useCdnDomain: true };
      const putExtra = { fname, params, mimeType };
      const observable = qiniu.upload(file, key, token, putExtra, config);
      observable.subscribe({
        next(res) {
          typeof progressCallback === 'function' && progressCallback(res);
        },
        error(err) {
          reject(err);
        },
        complete(res) {
          resolve(res);
        }
      });
    });
  },
  start: async ({ file }, progressCallback) => {
    const { token, domain } = await qiniuService.token();
    const upload = await qiniuService.upload({
      file,
      token,
      progressCallback
    });
    const imageUrl = `https://${domain}/${upload.key}`;
    return { imageUrl };
  }
};

export default qiniuService;
