const setDate = {
  // 设置时间
  dataSet(vm) {
    var now = new Date();
    var year = now.getFullYear(); //得到年份
    var month = now.getMonth(); //得到月份
    var date = now.getDate(); //得到日期
    var day = now.getDay(); //得到周几
    var hour = now.getHours(); //得到小时
    var minu = now.getMinutes(); //得到分钟
    var sec = now.getSeconds(); //得到秒
    var MS = now.getMilliseconds(); //获取毫秒
    var week;
    month = month + 1;
    if (month < 10) month = "0" + month;
    if (date < 10) date = "0" + date;
    if (hour < 10) hour = "0" + hour;
    if (minu < 10) minu = "0" + minu;
    if (sec < 10) sec = "0" + sec;
    if (MS < 100) MS = "0" + MS;
    var arr_week = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
    week = arr_week[day];
    var time = "";
    time = year + "年" + month + "月" + date + "日" + " " + hour + ":" + minu + ":" + sec + " " + week;
    vm.dateYear = year;
    vm.dateMonth = month;
    vm.dateDate = date;
    vm.dateHour = hour;
    vm.dateMinu = minu;
    vm.dateSec = sec;
    vm.dateWeek = week;
    var timer = setTimeout(() => {
      vm.dataSet()
    }, 1000)
  }
}

export default setDate