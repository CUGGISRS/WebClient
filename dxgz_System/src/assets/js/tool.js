// 防抖
export const Debounce = (fn, t) => {
	let delay = t || 1000;
	let timer;
	return function () {
		let args = arguments;
		if (timer) {
			clearTimeout(timer);
		}
		let callNow = !timer
		timer = setTimeout(() => {
			timer = null;
		}, delay)
		if (callNow) fn.apply(this, args)
	}
}

// 导出为excel
// titleData 首行标题   contentData 表内容    title 文件名称
export function exportToExcel(titleData, contentData, fileName) {
  // 建立表格
  let excel = "<table>";
  // 设置表头
  let row = "<tr>";
  titleData.map((item) => {
    row += `<th align="center">${item}</th>`;
  });
  excel += `${row}</tr>`;
  // 设置数据
  contentData.map((item) => {
    let row = "<tr>";
    item.map((data) => {
      row += `<td align="center">${data}</td>`;
    });
    excel += `${row}</tr>`;
  });
  excel += "</table>";
  let excelFile = `
    <html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'>
      <meta http-equiv="content-type" content="application/vnd.ms-excel; charset=UTF-8">
      <head>
        <!--[if gte mso 9]>
          <xml>
            <x:ExcelWorkbook>
              <x:ExcelWorksheets>
                <x:ExcelWorksheet>
                  <x:Name>
                    {worksheet}
                  </x:Name>
                  <x:WorksheetOptions>
                    <x:DisplayGridlines/>
                  </x:WorksheetOptions>
                </x:ExcelWorksheet>
              </x:ExcelWorksheets>
            </x:ExcelWorkbook>
          </xml>
        <![endif]-->
      </head>
      <body>
        ${excel}
      </body>
    </html>
  `;
  let flag;
  //判断是否IE浏览器
  let isIE = !!window.ActiveXObject || "ActiveXObject" in window;
  // 兼容ie11
  if(isIE) {
    let blob = new Blob([excelFile], { type: "application/vnd.ms-excel;charset=utf-8" });
    window.navigator.msSaveOrOpenBlob(blob, fileName + ".xls");
    flag = "end";
  } else {
    try {
      let blob = new Blob([excelFile], { type: "application/vnd.ms-excel;charset=utf-8" });
      let url = window.URL.createObjectURL(blob);
      let link = document.createElement("a");
      link.href = url;
      link.style.visibility = "hidden";
      link.download = fileName + ".xls";
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (e) {
      alert("下载异常！");
    }
    flag = 'end';
  }
  return flag
}

// 日期转换
export function formatDateTime(date) {
  if(date) {
    let y = date.getFullYear();
    let m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    let d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    let hour = date.getHours();
    hour = hour < 10 ? ('0' + hour) : hour;
    let minute = date.getMinutes();
    minute = minute < 10 ? ('0' + minute) : minute;
    let second = date.getSeconds();
    second = second < 10 ? ('0' + second) : second;
    return y + '-' + m + '-' + d+' '+ hour + ':' + minute + ':' + second;
  } else {
    return '';
  }
}

// 时间格式化
export function dateFormat(fmt, date) {
  let ret;
  const opt = {
    "Y+": date.getFullYear().toString(),        // 年
    "m+": (date.getMonth() + 1).toString(),     // 月
    "d+": date.getDate().toString(),            // 日
    "H+": date.getHours().toString(),           // 时
    "M+": date.getMinutes().toString(),         // 分
    "S+": date.getSeconds().toString()          // 秒
    // 有其他格式化字符需求可以继续添加，必须转化成字符串
  };
  for (let k in opt) {
    ret = new RegExp("(" + k + ")").exec(fmt);
    if (ret) {
      fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
    };
  };
  return fmt;
}

// 手机号码验证
export function checkPhoneNumber(num) {
  var reg = new RegExp(/^[1]([3-9])[0-9]{9}$/)
  if (!reg.test(num)) {
    return false;
  }
  return true
}