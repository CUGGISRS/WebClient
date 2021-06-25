import { axios } from '@/utils/request'

export function searchSpider (data) {
  return axios({
    url: '/configurable-spider/search',
    method: 'post',
    data
  })
}

export function addSpider (data) {
  return axios({
    url: '/configurable-spider',
    method: 'post',
    data
  })
}

/**
 * 跟新爬虫
 */
export function updateSpider (data) {
  return axios({
    url: '/configurable-spider',
    method: 'put',
    data
  })
}

/**
 * 设置爬虫允许参数
 */
export function configSpiderSettings (data) {
  return axios({
    url: '/configurable-spider/settings',
    method: 'put',
    data
  })
}

/**
 * 配置代理
 */
export function configSpiderProxy (data) {
  return axios({
    url: '/configurable-spider/config-proxy',
    method: 'post',
    data
  })
}

export function exportSpider (id) {
  return axios({
    url: `configurable-spider/export/${id}`,
    method: 'post'
  })
}

/**
 * 删除爬虫
 * @param {*} id 爬虫id
 */
export function deleteSpider (id) {
  return axios({
    url: `configurable-spider/${id}`,
    method: 'delete'
  })
}

/**
 * 测试爬虫页面
 */
export function testPage (url) {
  return axios({
    url: 'configurable-spider/test-page',
    method: 'post',
    data: {
      url
    }
  })
}

/**
 * 测试xpath
 */
export function testXpath (url, xpath) {
  return axios({
    url: 'configurable-spider/test-xpath',
    method: 'post',
    data: {
      url,
      xpath
    }
  })
}

/**
 * 测试正则
 */
export function testRegex (url, regex) {
  return axios({
    url: 'configurable-spider/test-regex',
    method: 'post',
    data: {
      url, regex
    }
  })
}

/**
 * 测试内容页字段xpath
 * @param entryUrl 入口页链接
 *
 * @param contentUrl 正文页XPATH补充url
 *
 * @param contentXpath 正文页XPATH
 * @param xpath 正文页字段xpath
 */
export function testContentXpath (entryUrl,contentUrl, contentXpath, xpath) {
  return axios({
    url: 'configurable-spider/test-content-xpath',
    method: 'post',
    data: {
      entryUrl, contentUrl,contentXpath, xpath
    }
  })
}
