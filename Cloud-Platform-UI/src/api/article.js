import fetch from 'utils/fetch';

export function getList() {
  return fetch({
    url: '/article/list',
    method: 'get'
  });
}

export function getArticle() {
  return fetch({
    url: '/article/detail',
    method: 'get'
  });
}

export function fetchList(query) {
  return fetch({
    url: '/api/news/news/page',
    method: 'get',
    params: query
  })
}
export function deleteArticle(id) {
  return fetch({
    url: '/api/news/news/' + id,
    method: 'delete'
  })
}

export function fetchArticle(id) {
  return fetch({
    url: '/api/news/news/' + id,
    method: 'get'
   // params: { id }
  })
}

export function fetchPv(pv) {
  return request({
    url: '/article/pv',
    method: 'get',
    params: { pv }
  })
}

export function createArticle(data) {
  return request({
    url: '/article/create',
    method: 'post',
    data
  })
}

export function updateArticle(data) {
  return request({
    url: '/article/update',
    method: 'post',
    data
  })
}
