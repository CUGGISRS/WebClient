const host = 'http://192.168.8.166:8765';


function f() {
    alert(window.host);
}

/*
const pageSize = 8;//每页显示条数初始化
const pageIndex = 0;//页面索引初始值
const newsid = String();
const title = String();
const type = String();
const updatetime = new Date();
$(function () {
    InitList(0);//Load事件，初始化列表数据，页面索引为0（第一页）
    $(document).ready(function () {
        //分页
        /!*$("#Pagination").pagination({
            items_per_page: pageSize, //显示条数
        });*!/
        function InitList(pageIndex){
            $.ajax({
                url: "http://192.168.8.166:8765/api/news/simple/page?page=1&limit=8",//请求地址
                dataType: "json",//数据格式
                type: "get",//请求方式
                cache: true,
                contentType: 'application/json;charset=utf-8',
                async: false,//是否异步请求
                data: {
                    "pageIndex=" + (pageIndex + 1) + "&pageSize=" + pageSize,
                    newsid: newsid,
                    title : title,
                    type : type,
                    updatetime : updatetime,
                },
                /!* data: JSON.stringify(data),
           *!/
                success: function (data) {   //如何发送成功
                    console.log(data);
                }


                //$("#news-list").html(html);//在html页面id=news-list的标签里显示html内容
            })
        }
    })
*/



/*
const newsid = String();
const title = String();
const type = String();
const updatetime = new Date();
const pageSize = 8;//显示条数初始化
$.ajax({
type : "get",
dataType : "json",
url : "192.168.8.166:8765/api/news/news/page",
async : false,
data : "&pageSize=" + pageSize,
success : function (data) {
}
})

*/
