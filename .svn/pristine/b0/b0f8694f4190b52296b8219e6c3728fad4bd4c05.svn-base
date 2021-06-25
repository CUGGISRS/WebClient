const IP = 'http://192.168.8.166';
// const IP = 'http://39.104.61.47';
const PORT_1 = ':8091';
const PORT_2 = ':8765/api';
const PORT_3 = ':8091';
var sys_layerName = 'layerData'
const ioTLoginUrl = "/comSys/login/user";
const ioTUserUrl = "/comSys/getCurrentUser";
const ioTEnterpriseUrl = "/zsSys/Enterprise/";
const ioTSensorUrl = "/zsSys/DeviceSensor/pageByCId";
const ioTVideoUrl = "/zsSys/DeviceVideo/pageByEId";
let ioTEnterprises = [{
    username: 'admin',
    password: '123456'
},{
    username: '湖北龙兴湖米业有限公司',
    password: '123456'
},{
    username: '湖北恒泰农业集团有限公司',
    password: '123456'
},{
    username: '监利县远博水产养殖专业合作社',
    password: '123456'
}]

function doApi(sucFunc, errFunc, comFunc, url, type, params, contentType, async) {
    $.ajax({
        url: IP + PORT_1 + url,
        data: params,
        type: type,
        async: async ?async :true,
        contentType: contentType,
        success: sucFunc,
        error: function (err) {
            console.log(err);
            if (errFunc) {
                errFunc(err);
            }
        },
        complete: comFunc
    });
}

function doIoTApi(sucFunc, errFunc, url, type, params, token, async) {
    $.ajax({
        beforeSend: function (request) {
            if (token) {
                request.setRequestHeader('Authorization', token);
            }
        },
        url: IP + PORT_2 + url,
        data: params,
        type: type,
        async: async | false,
        contentType: 'application/json',
        success: sucFunc,
        error: function (err) {
            console.log(err);
            if (errFunc) {
                errFunc(err);
            }
        },
    });
}
function doApi_2(sucFunc, errFunc, url, type, params, token) {
    $.ajax({
        beforeSend: function (request) {
            if (token) {
                request.setRequestHeader('Authorization', token);
            }
        },
        url: IP + PORT_3 + url,
        data: params,
        type: type,
        //async: false,
        contentType: 'application/json',
        success: sucFunc,
        error: function (err) {
            console.log(err);
            if (errFunc) {
                errFunc(err);
            }
        },
    });
}
