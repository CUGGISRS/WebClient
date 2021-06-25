import request from "@/globals/request/axios.js";
import API from "@/globals/request/api.js";

const DataList = {
  //本地代理
  getLocalAgent(params){
    return request.get(API.getLocalAgent,params);
  },
  addLocalAgent(params) {
    return request.post(API.localAgent, params);
  },
  editLocalAgent(params) {
    return request.put(API.localAgent, params);
  },
  delLocalAgent(id) {
    return request.delete(API.localAgentAddId(id));
  },

  //用户
  getUser(params){
    return request.get(API.getUser,params);
  },
  addUser(params) {
    return request.post(API.user, params);
  },
  editUser(params) {
    return request.put(API.user, params);
  },
  delUser(id){
    return request.delete(API.userAddId(id));
  },

  //登陆
  login(params){
    return request.post(API.login, params);
  },

  //服务状态
  getServiceState(params){
    return request.get(API.getServiceState,params);
  },
  editServiceState(params) {
    return request.put(API.serviceState, params);
  },

  getDirsMayFilesJSON(ip,params) {
    return request.get(API.getDirsMayFilesJSON(ip),params);
  },
  getDataList() {
    return request.get(API.dataList);
  },
  addDataItem(params) {
    return request.post(API.addDataItem, params);
  },
  getCataContent(params) {
    return request.post(API.getCataContent, params);
  },
  addJsonNode(params) {
    return request.post(API.addJsonNode, params);
  },
  moveJsonNode(params) {
    return request.put(API.moveJsonNode, params);
  },
  delJsonFile(fileName) {
    return request.post(API.delJsonFile(fileName));
  },
  delJsonChildrenNode(id, fileName) {
    return request.delete(API.delJsonChildrenNode(id, fileName));
  },
  editJsonNode(params) {
    return request.put(API.editJsonNode, params);
  }
};

export default DataList;
