const data = {
  // ip: "http://192.168.8.132:8766"
  // ip: "http://192.168.2.6:8766"
  // 服务器地址
  // ip: "http://192.168.8.166:8766",
  // ip_2: "http://192.168.8.166:8765/api/comSys",
  // ip_3: "http://192.168.8.166:8765",
  // ip_4: "http://192.168.8.166:8765/api"

  ip: "http://39.104.61.47:8766",
  ip_2: "http://39.104.61.47:8765/api/comSys",
  ip_3: "http://39.104.61.47:8765",
  ip_4: "http://39.104.61.47:8765/api"
};

function doApi(sucFunc, errFunc, comFunc, url, type, params, contentType, async) {
  $.ajax({
    url: data.ip + url,
    data: params,
    type: type,
    // async: async ?async :true,
    contentType: contentType,
    // async: true,
    success: sucFunc,
    error: function (err) {
      console.log(err)
    },
    complete: comFunc
  });
}

function doApi_2(sucFunc, errFunc, comFunc, url, type, params, contentType, async) {
  $.ajax({
    url: data.ip_4 + url,
    data: params,
    type: type,
    // async: async ?async :true,
    contentType: contentType,
    // async: true,
    success: sucFunc,
    error: function (err) {
      console.log(err)
    },
    complete: comFunc
  });
}