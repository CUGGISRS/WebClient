const PREFIX_API_1 = "http://192.168.8.166:8091";
// const PREFIX_API_1 = "http://39.104.61.47:8091";

export default {


    validateIp: function (rule, value, callback) {
        if (value == null || !this.isValidIP(value)) {
            callback(new Error('ip地址格式不正确!'));
        } else {
            callback();
        }
    },
    //ip是否合法
    isValidIP: function (ip) {
        var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
        return reg.test(ip);
    },
    //验证字符串是否是数字
    isValidNumber: function (val) {
        var reg = /^[0-9]+.?[0-9]*$/;
        if (reg.test(val)) {
            return true;
        }
        return false;
    },

    getDirsMayFilesJSON: function (ip) {

        if (ip == null || !this.isValidIP(ip)) {
            ip = `${PREFIX_API_1}`;
        } else {
            ip = "http://" + ip + ":8091";
        }

        return `${ip}/FileDirOpt/getDirsMayFilesJSON`
    },
    //本地代理
    getLocalAgent: `${PREFIX_API_1}/LocalAgent/page`,
    localAgent: `${PREFIX_API_1}/LocalAgent`,
    localAgentAddId: id =>`${PREFIX_API_1}/LocalAgent/`+id,

    dataList: `${PREFIX_API_1}/FileDirOpt/getAllJsonFileJSON`,
    addDataItem: `${PREFIX_API_1}/FileDirOpt/writeJsonFile`,
    getCataContent: `${PREFIX_API_1}/FileDirOpt/getJsonByPath`,
    addJsonNode: `${PREFIX_API_1}/FileDirOpt/addJsonNode`,
    delJsonFile: fileName =>
        `${PREFIX_API_1}/FileDirOpt/delJsonFile?fileName=${fileName}`,
    delJsonChildrenNode: (id, fileName) =>
        `${PREFIX_API_1}/FileDirOpt/delJsonNode?id=${id}&fileName=${fileName}`,
    editJsonNode: `${PREFIX_API_1}/FileDirOpt/updJsonNode`,
    moveJsonNode: `${PREFIX_API_1}/FileDirOpt/moveJsonNode`,//移动节点接口

    //用户
    getUser:`${PREFIX_API_1}/User/pageByCondition`,
    user:`${PREFIX_API_1}/User`,
    userAddId:id =>`${PREFIX_API_1}/User/`+id,

    //登陆
    login:`${PREFIX_API_1}/login`,

    //服务状态
    getServiceState:`${PREFIX_API_1}/ServerStatus/page`,
    serviceState: `${PREFIX_API_1}/ServerStatus`
};
