import MyStorage from "./storage";

class DataStore {
  constructor() {
    this.init();
  }

  init() {
    this.map = new Map();
    this.storage = new MyStorage();
  }

  static getInstance() {
    if (!DataStore.instance) {
      DataStore.instance = new DataStore();
    }
    return DataStore.instance;
  }
//删除某一本地存储数据
  removeData(key) {
    this.storage.delete(key);
  }
//清空本地存储数据
  clear() {
    this.storage.clear();
  }

//添加某一本地存储数据
  setData(key,value) {
    this.storage.set(key, value);
  }
//获得某一本地存储数据
  getData(key) {
    return  this.storage.get(key);
  }

  //token
  setToken(value) {
    this.storage.set("3d_token", value);
  }
  getToken() {
    return  this.storage.get("3d_token");
  }
  //用户名
  setUsername(value){
    this.storage.set("username", value);
  }
  getUsername(){
    return  this.storage.get("username");
  }

}

export default DataStore.getInstance();
