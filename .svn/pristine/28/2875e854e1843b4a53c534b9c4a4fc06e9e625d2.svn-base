import fetch from 'utils/fetch';
// 列表
export function expertList(query) {
  return fetch({
    url: '/api/consultation/expert/page',
    method: 'get',
    params: query
  })
}
// 添加
export function addExpert(data) {
  return fetch({
    url: '/api/consultation/expert',
    method: 'post',
    data
  });
}
// 删除
export function deleteExpert(id) {
  return fetch({
    url: '/api/consultation/expert/' + id,
    method: 'delete'
  })
}
// 获取编辑内容
export function fetchExpert(id) {
  return fetch({
    url: 'api/consultation/expert/' + id,
    method: 'get'
    // params: { id }
  })
}

export function putExpert(id, data) {
  return fetch({
    url: 'api/consultation/expert/' + id,
    method: 'put',
    data
  })
}

// 获得某月专家排班列表
export function expertdutyList(query) {
  return fetch({
    url: '/api/consultation/expertduty/month',
    method: 'get',
    params: query
  })
}

// 添加专家排班信息，返回值包含新增后的排班编号
export function addDuty(data) {
  return fetch({
    url: 'api/consultation/expertduty',
    method: 'post',
    data
  })
}

// 删除专家排班信息
export function deleteDuty(id) {
  return fetch({
    url: 'api/consultation/排班编号' + id,
    method: 'delete'
  })
}

// 专家排班中编辑按钮获取专家信息列表
/* export function fetchDutyExpert(day) {
  return fetch({
    url: 'api/consultation/expert/' + day,
    method: 'get'
    // params: { id }
  })
}*/
export function fetchexpertList(expertquery) {
  return fetch({
    url: '/api/consultation/expert/page',
    method: 'get',
    params: expertquery
  })
}

// 专家排班信息保存
export function saveDuty(day, data) {
  return fetch({
    url: '/api/consultation/expertduty/day?year=2019&month=9&day=' + day,
    method: 'post',
    data
  });
}
