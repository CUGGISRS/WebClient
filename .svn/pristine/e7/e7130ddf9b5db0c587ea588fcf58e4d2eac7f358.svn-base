import request from '@/globals/request/axios.js';
import API from '@/globals/request/api.js';

const userService = {
  login(params) {
    return request.post(API.login, params);
  },
  getUserInfo(params) {
    return request.get(API.getUserInfo, {}, params);
  },
  // 分页获取所有用户信息
  getAllUserInfo(params) {
    return request.get(API.getAllUserInfo, params);
  },
  // 重置用户密码
  resetPwd(params) {
    return request.post(API.resetPwd, params);
  },
  // 删除用户
  delUserInfo(id) {
    return request.delete(API.delUserInfo(id));
  },
  delUserList(params) {
    return request.post(API.delUserList, params);
  },
  // 更新用户信息
  updateUserInfo(params) {
    return request.put(API.updateUserInfo, params);
  },
  //文章管理获取文章列表
  getArticleList(params) {
    return request.get(API.articleList, params);
  },
  getArticleListPage(params) {
    return request.get(API.articleListPage, params);
  },
  //文章管理通过文章id获取文章数据
  getArticleId(id) {
    return request.get(API.articleId(id));
  },
  //文章管理系统删除文章
  articleDelete(params) {
    return request.delete(API.articleDel, params);
  },
  //文章管理增加文章
  articleAdd(params) {
    return request.post(API.articleAdd, params);
  },
  //文章管理更新文章
  articleUpdate(params) {
    return request.put(API.articleUpdate, params);
  },
  //特色农产品获取产品列表
  featureProductsList(params) {
    return request.get(API.featureProductsList, params);
  },
  //新增特色农产品
  featureProductAdd(formData) {
    return request.post(API.featureProductAdd, formData);
  },
  //删除特色农产品
  featureProductDel(id) {
    return request.delete(API.featureProductDel(id));
  },
  //修改特色农产品
  featureProductEdit(formData) {
    return request.put(API.featureProductEdit, formData);
  },
  // 新增三品一标
  authInfoAdd(formData) {
    return request.post(API.authInfoAdd, formData);
  },
  // 更新三品一标信息
  authInfoEdit(formData) {
    return request.post(API.authInfoEdit, formData);
  },
  // 删除三品一标信息
  authInfoDel(id) {
    return request.delete(API.authInfoDel(id));
  },
  getAuthInfoById(id) {
    return request.get(API.authInfoById(id));
  },
  //企业名录列表
  getCompanylist(params) {
    return request.get(API.getCompanylist, params);
  },
  //新增企业名录
  businessDirectoryAdd(formData) {
    return request.post(API.businessDirectoryAdd, formData);
  },
  //修改企业名录
  businessDirectoryEdit(formData) {
    return request.put(API.businessDirectoryEdit, formData);
  },
  //删除企业名录
  businessDirectoryDel(id) {
    return request.delete(API.businessDirectoryDel(id));
  },
  //获取信息系统轮播图列表
  getInformationSwiperList(params) {
    return request.get(API.getInformationSwiperList, params);
  },
  //修改轮播图发布状态
  swiperStatusEdit(params) {
    return request.put(API.swiperStatusEdit, params);
  },
  //删除轮播图信息
  swiperInformationDelete(id) {
    return request.delete(API.swiperInformationDelete(id));
  },
  //获取投诉建议列表
  getSuggestList(formData) {
    return request.post(API.getSuggestList, formData);
  },
  //修改投诉建议处理状态
  suggestStatusEdit(formData) {
    return request.get(API.suggestStatusEdit, formData);
  },

  // 优质农产品
  // 分页获取优质农产品列表
  getProductListByPage(params) {
    return request.get(API.getProductListByPage, params);
  },
  //添加一条优质农产品
  addHighQualityProduct(params) {
    return request.post(API.addHighQualityProduct, params);
  },
  // 更新优质农产品
  updateHighQualityProduct(params) {
    return request.post(API.updateHighQualityProduct, params);
  },
  // 修改优质农产品审核状态
  ModifyProductStatus(params) {
    return request.put(API.ModifyProductStatus, params);
  },
  // 删除优质农产品信息
  delHighQualityProductById(id) {
    return request.get(API.delHighQualityProductById(id));
  },

  // 留言板信息
  // 获取指定留言的回复消息
  getQAReplyInfo(qaId) {
    return request.post(API.getQAReplyInfo + '?qaId=' + qaId);
  },
  // 修改留言审核状态
  ModifyQAStatus(params) {
    return request.put(API.ModifyQAStatus, params);
  },
  getQAInfoByPage(params) {
    return request.get(API.getQAInfoByPage, params);
  },
  // 回复一条留言信息
  replyQAById(params) {
    return request.post(API.replyQAById, params);
  },
  // 删除一条留言信息
  delQAById(id) {
    return request.delete(API.delQAById + '/' + id);
  },
  // 更新一条回复消息
  updateReplyQAInfo(params) {
    return request.put(API.updateReplyQAInfo, params);
  },
  // 产销信息、市场行情
  // 修改 农产品产销信息/市场行情 发布状态
  updateMarketingInfo(params) {
    return request.put(API.updateInfoById, params);
  },
  //删除一条  农产品产销信息/市场行情
  delMarketingInfoById(id) {
    return request.delete(API.MarketingInfo + '/' + id);
  },
  // 新增一条农产品产销信息/市场行情信息
  addMarketingInfo(params) {
    return request.post(API.MarketingInfo, params);
  },
  //获取一条农产品产销信息/市场行情信息
  getMarketingInfo(id) {
    return request.get(API.MarketingInfo + '/' + id);
  },

  // 根据类型获取农产品名称的数据字典
  getProductNameDicByType(params) {
    return request.get(API.getProductNameDicByType, params);
  },
  // 新增数据字典
  addDicInfo(params) {
    return request.post(API.addDicInfo, params);
  },
  // 更新数据字典
  updateDicInfo(params) {
    return request.post(API.updateDicInfo, params);
  },
  // 删除数据字典
  delDicInfo(params) {
    return request.post(API.delDicInfo, params);
  },
  // 价格行情
  // 分页获取价格行情列表
  getPriceInfoListByPage(params) {
    return request.get(API.getPriceInfoListByPage, params);
  },
  importByExcel(formData) {
    return request.post(API.importByExcel, formData);
  },
  // 根据id删除价格行情
  delPriceInfoById(id) {
    return request.delete(API.priceInfo + '/' + id);
  },
  // 更新价格行情
  updatePriceInfo(params) {
    return request.put(API.priceInfo, params);
  },
  // 新增价格行情
  addPriceInfo(params) {
    return request.post(API.priceInfo, params);
  },
  // 获取价格行情乡镇数据字典
  getTownDic(params) {
    return request.post(API.getTownDic, params);
  },
  // 分页获取市场行情列表
  getMarketingListByPage(params) {
    return request.get(API.getMarketingListByPage, params);
  },
  //分页获取供求信息
  getSupplyDemandListByPage(params) {
    return request.get(API.getSupplyDemandListByPage, params);
  },
  // 更新供求信息
  updateSupplyDemand(params) {
    return request.put(API.updateSupplyDemand, params);
  },
  // 更新供求信息 审核状态
  updateSupplyDemandStatus(params) {
    return request.put(API.updateSupplyDemand, params);
  },
  // 获取一条供求信息
  getSupplyDemand(id) {
    return request.get(API.getSupplyDemandById + '?id=' + id);
  },
  // 新增一条供求信息
  addSupplyDemand(params) {
    return request.post(API.addNewSupplyDemandInfo, params);
  },
  // 删除一条供求信息
  delSupplyDemandById(id) {
    return request.get(API.delSupplyDemandById(id));
  },

  // 分页获取专家信息列表
  getExpertList(params) {
    return request.get(API.getExpertList, params);
  },
  // 获取一条专家信息
  getExpertInfo(id) {
    return request.get(API.getExpertInfo + id);
  },
  // 新增一条专家信息
  addExpertInfo(params) {
    return request.post(API.addExpertInfo, params);
  },
  // 更新一条专家信息
  updateExpert(params) {
    return request.post(API.updateExpert, params);
  },
  // 删除一条专家信息
  delExpertById(id) {
    return request.get(API.delExpertById(id));
  },
  // 分页获取农业问答
  getConsultInfoByPage(params) {
    return request.get(API.getConsultInfoByPage, params);
  },
  // 更新一条农业问答
  updateConsultInfo(params) {
    return request.put(API.updateConsultInfo, params);
  },
  // 删除农业问答
  delConsultById(id) {
    return request.get(API.delConsultById(id));
  },
  // 分页获取视频列表
  getVideoList(params) {
    return request.get(API.getVideoList, params);
  },
  //新增视频信息
  videoInfoAdd(formData) {
    return request.post(API.videoInfoAdd, formData);
  },
  //删除视频信息
  videoInfoDel(id) {
    return request.delete(API.videoInfoDel(id));
  },
  // 新增轮播图
  addCarousel(params) {
    return request.post(API.addCarousel, params);
  },
  // 更新轮播图
  updateCarousel(params) {
    return request.put(API.updateCarousel, params);
  },
  //更新用户状态
  updateUserStatus(params) {
    return request.put(API.updateUserStatus, params);
  }
};
export default userService;