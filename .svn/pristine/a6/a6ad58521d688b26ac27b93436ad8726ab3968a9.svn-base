// const PREFIX_API_1 = 'http://192.168.8.166:8766';
// const PREFIX_API_2 = 'http://192.168.8.166:8776';
// const PREFIX_API_3 = 'http://192.168.8.166:8765';
//39.104.61.47
const PREFIX_API_1 = 'http://39.104.61.47:8766';
const PREFIX_API_2 = 'http://39.104.61.47:8776';
const PREFIX_API_3 = 'http://39.104.61.47:8765';


function doApi_1(sucFunc, errFunc, comFunc, url, type, params, contentType, async) {
  $.ajax({
    url: PREFIX_API_1 + url,
    data: params,
    type: type,
    contentType: contentType,
    success: sucFunc,
    error: function (err) {
      console.log(err)
    },
    complete: comFunc
  });
}

function doApi_2(sucFunc, errFunc, comFunc, url, type, params, contentType, async) {
  $.ajax({
    url: PREFIX_API_2 + url,
    data: params,
    type: type,
    contentType: contentType,
    success: sucFunc,
    error: function (err) {
      console.log(err)
    },
    complete: comFunc
  });
}

function doApi_3(sucFunc, errFunc, comFunc, url, type, params, contentType, async) {
  $.ajax({
    url: PREFIX_API_2 + url,
    data: params,
    type: type,
    contentType: contentType,
    // async :async ?async :true,
    // contentType: false,
    processData: false, //是否预处理数据:默认为true，默认情况下，发送的数据将被转换为对象，设为false不希望进行转换
    // async: false,
    success: sucFunc,
    error(err) {
      console.log(err)
    },
    complete: comFunc
  });
}