import fetch from 'utils/fetch';

// 获取用户token
export function userToken(data) {
  return fetch({
    url: '/api/auth/jwt/token',
    method: 'post',
    data
  })
}


// 添加上传的视频信息到数据库
export function uploadfile(data) {
  return fetch({
    url: '/api/uploadfile/file/add2Server',
    method: 'post',
    contentType: 'application/json;charset=utf-8',
    data
  })
}

// 上传视频文件
export function uploadvideofile(data) {
  return fetch({
    url: '/api/uploadfile/file/appendUpload2Server',
    method: 'post',
    data
  })
}

// 获取视频列表
export function getvideolist(query) {
  return fetch({
    url: '/api/uploadfile/file/allVideos',
    method: 'get',
    params: query
  })
}

// 按页获取视频列表
export function getvideobypage(data) {
  return fetch({
    url: '/api/uploadfile/file/videosByPage',
    method: 'get',
    data
  })
}


