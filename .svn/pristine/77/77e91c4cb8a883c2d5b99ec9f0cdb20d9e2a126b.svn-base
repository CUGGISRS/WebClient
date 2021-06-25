const PREFIX_API_1 = 'http://39.104.61.47:8776'; // 电子商务系统
const PREFIX_API_2 = 'http://39.104.61.47:8766'; //信息追溯系统
const PREFIX_API_3 = 'http://39.104.61.47:8777'; //公益性子系统

// const PREFIX_API_1 = 'http://192.168.8.166:8776'; // 电子商务系统
// const PREFIX_API_2 = 'http://192.168.8.166:8766'; //信息追溯系统
// const PREFIX_API_3 = 'http://192.168.8.166:8777'; //公益性子系统

// const PREFIX_API_1 = 'http://192.168.8.124:8776'; // 电子商务系统
// const PREFIX_API_22 = 'http://192.168.8.124:8766'; //信息追溯系统

export default {
  // 用户登录
  login: `${PREFIX_API_1}/user/login`,
  // 获取当前用户信息
  getUserInfo: `${PREFIX_API_1}/user/getCurrentUser`,
  // 分页获取所有用户信息
  getAllUserInfo: `${PREFIX_API_1}/user/list`,
  //重置用户密码
  resetPwd: `${PREFIX_API_1}/user/resetPWD`,
  // 删除用户
  delUserInfo: id => `${PREFIX_API_1}/user/delete/${id}`,
  delUserList: `${PREFIX_API_1}/user/delete/List`,
  // 更新用户信息
  updateUserInfo: `${PREFIX_API_1}/user/update`,
  //追溯信息系统
  //文章管理获取文章列表
  articleList: `${PREFIX_API_2}/article/list`,
  articleListPage: `${PREFIX_API_2}/article/listArticleByPage`,
  //文章管理通过文章id获取文章数据
  articleId: id => `${PREFIX_API_2}/article/id?id=${id}`,
  //文章管理系统删除文章
  articleDel: `${PREFIX_API_2}/article/delStatus`,
  //文章管理增加文章
  articleAdd: `${PREFIX_API_2}/article/add`,
  //文章管理更新文章
  articleUpdate: `${PREFIX_API_2}/article/update`,
  //特色农产品管理获取列表
  featureProductsList: `${PREFIX_API_2}/product/all`,
  //新增特色农产品
  featureProductAdd: `${PREFIX_API_2}/product/add`,
  //删除特色农产品
  featureProductDel: id => `${PREFIX_API_2}/product/del?ids=${id}`,
  //修改特色农产品
  featureProductEdit: `${PREFIX_API_2}/product/update`,
  // 新增三品一标
  authInfoAdd: `${PREFIX_API_2}/productAuth/add`,
  // 修改三品一标
  authInfoEdit: `${PREFIX_API_2}/productAuth/update`,
  // 删除三品一标
  authInfoDel: id => `${PREFIX_API_2}/productAuth/del/?id=${id}`,
  // 根据id获取一条三品一标详细信息
  authInfoById: id => `${PREFIX_API_2}/productAuth/getByAuthId?AuthId=${id}`,
  //获取企业名录列表
  getCompanylist: `${PREFIX_API_2}/company/allByPage`,
  //新增企业名录
  businessDirectoryAdd: `${PREFIX_API_2}/company/addNewCompanyInfo`,
  //修改企业名录
  businessDirectoryEdit: `${PREFIX_API_2}/company/updateCompanyInfo`,
  //删除企业名录
  businessDirectoryDel: id => `${PREFIX_API_2}/company/del?ids=${id}`,
  //获取轮播图列表
  getInformationSwiperList: `${PREFIX_API_2}/carousel/page`,
  //修改轮播图发布状态
  swiperStatusEdit: `${PREFIX_API_2}/carousel/updateStatus`,
  //删除轮播图信息
  swiperInformationDelete: id => `${PREFIX_API_2}/carousel/del?id=${id}`,
  //获取投诉建议列表
  getSuggestList: `${PREFIX_API_2}/suggest/listByType`,
  //修改投诉建议处理状态
  suggestStatusEdit: `${PREFIX_API_2}/suggest/deal`,

  // 电子商务系统
  // 分页获取优质农产品列表
  getProductListByPage: `${PREFIX_API_1}/perfectProduct/listByPage`,
  // 新增优质农产品
  addHighQualityProduct: `${PREFIX_API_1}/perfectProduct/add`,
  // 更新优质农产品
  updateHighQualityProduct: `${PREFIX_API_1}/perfectProduct/updateQualityProduct`,
  // 更新优质农产品审核状态
  ModifyProductStatus: `${PREFIX_API_1}/perfectProduct`,
  // 删除优质农产品信息
  delHighQualityProductById: id => `${PREFIX_API_1}/perfectProduct/del?ids=${id}`,
  // 根据留言id获取回复消息
  getQAReplyInfo: `${PREFIX_API_1}/reply/getReplyByqaId`,
  // 更新留言审核状态
  ModifyQAStatus: `${PREFIX_API_1}/qa`,
  // 分页获取留言信息
  getQAInfoByPage: `${PREFIX_API_1}/qa/listByPage`,
  // 回复指定id的留言信息
  replyQAById: `${PREFIX_API_1}/reply/replyQAById`,
  // 更新一条回复消息
  updateReplyQAInfo: `${PREFIX_API_1}/reply`,
  // 删除一条留言
  delQAById: `${PREFIX_API_1}/qa`,
  // 分页获取市场行情列表
  getMarketingListByPage: `${PREFIX_API_1}/marketing/listByPage`,
  // 更新市场行情信息
  updateInfoById: `${PREFIX_API_1}/marketing/updateInfoById`,
  // 获取价格行情乡镇数据字典
  getTownDic: `${PREFIX_API_1}/dic/getDicByType`,
  //根据id获取一条信息
  MarketingInfo: `${PREFIX_API_1}/marketing`,
  // 根据Type类型获取农产品名称
  getProductNameDicByType: `${PREFIX_API_1}/dic/getDicByTypeAndText`,
  // 新增数据字典
  addDicInfo: `${PREFIX_API_1}/dic/addDic`,
  // 更新数据字典
  updateDicInfo: `${PREFIX_API_1}/dic/updateDic`,
  // 删除数据字典
  delDicInfo: `${PREFIX_API_1}/dic/delDic`,
  // 分页获取价格行情列表
  getPriceInfoListByPage: `${PREFIX_API_1}/price/listByPage`,
  // 通过Excel导入价格行情信息
  importByExcel: `${PREFIX_API_1}/price/importByExcel`,
  // 价格行情
  priceInfo: `${PREFIX_API_1}/price`,
  // 分页获取供求信息
  getSupplyDemandListByPage: `${PREFIX_API_3}/supplydemand/allByPage`,
  // 供求信息
  SupplyDemandInfo: `${PREFIX_API_3}/supplydemand`,
  // 新增供求信息
  addNewSupplyDemandInfo: `${PREFIX_API_3}/supplydemand/addNewSupplyDemand`,
  // 获取供求信息
  getSupplyDemandById: `${PREFIX_API_3}/supplydemand/getSupplyDemandById`,
  // 更新供求信息
  updateSupplyDemand: `${PREFIX_API_3}/supplydemand/updateSupplyDemand`,
  delSupplyDemandById: id => `${PREFIX_API_3}/supplydemand/del?ids=${id}`,

  //公益性系统
  // 分页获取专家信息
  getExpertList: `${PREFIX_API_3}/expert/getExpertInfoList`,
  // 获取一条专家信息
  getExpertInfo: `${PREFIX_API_3}/expert/getExpert/`,
  // 新增一条专家信息
  addExpertInfo: `${PREFIX_API_3}/expert/addExpert`,
  // 更新一条专家信息
  updateExpert: `${PREFIX_API_3}/expert/updateExpert`,
  // 删除专家信息
  delExpertById: id => `${PREFIX_API_3}/expert/del?ids=${id}`,
  //分页获取农业问答
  getConsultInfoByPage: `${PREFIX_API_3}/consult/getPageByState`,
  // 更新农业问答
  updateConsultInfo: `${PREFIX_API_3}/consult/update`,
  // 删除农业问答
  delConsultById: id => `${PREFIX_API_3}/consult/del?ids=${id}`,

  // 分页获取视频列表
  getVideoList: `${PREFIX_API_1}/video/videosByPage`,
  //新增视频信息
  videoInfoAdd: `${PREFIX_API_1}/video/addVideo`,
  //删除视频信息
  videoInfoDel: id => `${PREFIX_API_1}/video/del?ids=${id}`,

  // 新增轮播图
  addCarousel: `${PREFIX_API_2}/carousel/add`,
  // 更新轮播图
  updateCarousel: `${PREFIX_API_2}/carousel/update`,
  //修改用户启用状态
  updateUserStatus: `${PREFIX_API_1}/user/update`
};
