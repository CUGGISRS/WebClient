(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-27c936e9"],{"76fe":function(t,e,a){"use strict";a.d(e,"a",(function(){return s})),a.d(e,"b",(function(){return r})),a.d(e,"h",(function(){return c})),a.d(e,"d",(function(){return o})),a.d(e,"e",(function(){return i})),a.d(e,"f",(function(){return u})),a.d(e,"g",(function(){return l})),a.d(e,"c",(function(){return d}));var n=a("b775");function s(t){return Object(n["b"])({url:"/job",method:"post",data:t})}function r(t){return Object(n["b"])({url:"/job/".concat(t),method:"get"})}function c(t){return Object(n["b"])({url:"/job/stop/".concat(t),method:"post"})}function o(t){return Object(n["b"])({url:"/job/pause/".concat(t),method:"post"})}function i(t){return Object(n["b"])({url:"/job/restart/".concat(t),method:"post"})}function u(t){return Object(n["b"])({url:"/job/search",method:"post",data:t})}function l(t){return Object(n["b"])({url:"/job-record/search",method:"post",data:t})}function d(t){return Object(n["b"])({url:"/job/get-stat-info/".concat(t),method:"get"})}},ac62:function(t,e,a){"use strict";a.r(e);var n,s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("a-card",{attrs:{bordered:!1}},[a("div",{staticClass:"table-page-search-wrapper"},[a("a-form",{attrs:{layout:"inline"}},[a("a-row",{attrs:{gutter:48}},[a("a-col",{attrs:{md:8,sm:24}},[a("a-form-item",{attrs:{label:"蜘蛛名"}},[a("a-input",{attrs:{placeholder:""},model:{value:t.searchModel.spiderName,callback:function(e){t.$set(t.searchModel,"spiderName",e)},expression:"searchModel.spiderName"}})],1)],1),t.advanced?[a("a-col",{attrs:{md:8,sm:24}},[a("a-form-item",{attrs:{label:"表名"}},[a("a-input",{attrs:{placeholder:""},model:{value:t.searchModel.spiderTableName,callback:function(e){t.$set(t.searchModel,"spiderTableName",e)},expression:"searchModel.spiderTableName"}})],1)],1)]:t._e(),a("a-col",{attrs:{md:t.advanced?24:8,sm:24}},[a("span",{staticClass:"table-page-search-submitButtons",style:t.advanced&&{float:"right",overflow:"hidden"}||{}},[a("a-button",{attrs:{type:"primary"},on:{click:t.search}},[t._v("查询")]),a("a-button",{staticStyle:{"margin-left":"8px"},on:{click:function(){return t.searchModel={}}}},[t._v("重置")]),a("a",{staticStyle:{"margin-left":"8px"},on:{click:t.toggleAdvanced}},[t._v("\n              "+t._s(t.advanced?"收起":"展开")+"\n              "),a("a-icon",{attrs:{type:t.advanced?"up":"down"}})],1)],1)])],2)],1)],1),a("div",{staticClass:"table-operator"}),a("a-table",{attrs:{columns:t.columns,rowKey:function(t){return t.id},dataSource:t.data,pagination:t.pagination,loading:t.loading},on:{change:t.handleTableChange},scopedSlots:t._u([{key:"status",fn:function(e){return[a("a-badge",{attrs:{status:t._f("statusTypeFilter")(e),text:t._f("statusFilter")(e)}})]}},{key:"action",fn:function(e,n){return[a("a-button",{staticClass:"m-r-8 m-b-8",attrs:{type:"primary",size:"small"},on:{click:function(e){return t.seeResult(n)}}},[t._v("查看爬取结果")]),"success"!==n.status||"error"===n.status?a("a-popconfirm",{attrs:{title:"确认结束?",okText:"确认",cancelText:"取消"},on:{confirm:function(e){return t.stopJob(n.id)}}},[a("a-icon",{staticStyle:{color:"red"},attrs:{slot:"icon",type:"question-circle-o"},slot:"icon"}),a("a-button",{staticClass:"m-r-8 m-b-8",attrs:{type:"danger",size:"small"}},[t._v("结束")])],1):t._e(),"running"===n.status?a("a-popconfirm",{attrs:{title:"确认暂停?",okText:"确认",cancelText:"取消"},on:{confirm:function(e){return t.pauseJob(n.id)}}},[a("a-icon",{staticStyle:{color:"red"},attrs:{slot:"icon",type:"question-circle-o"},slot:"icon"}),a("a-button",{staticClass:"m-r-8 m-b-8",attrs:{size:"small"}},[t._v("暂停")])],1):t._e(),"paused"===n.status?a("a-popconfirm",{attrs:{title:"确认重启?",okText:"确认",cancelText:"取消"},on:{confirm:function(e){return t.restartJob(n.id)}}},[a("a-icon",{staticStyle:{color:"red"},attrs:{slot:"icon",type:"question-circle-o"},slot:"icon"}),a("a-button",{staticClass:"m-r-8",attrs:{size:"small"}},[t._v("重启")])],1):t._e()]}}])})],1)},r=[],c=(a("2338"),a("f763"),a("fb37"),a("22dc")),o=(a("55a0"),a("caaf"),a("a14a")),i=a.n(o),u=a("2af9"),l=a("76fe");a("b27f");function d(t,e){var a=Object.keys(t);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(t);e&&(n=n.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),a.push.apply(a,n)}return a}function p(t){for(var e=1;e<arguments.length;e++){var a=null!=arguments[e]?arguments[e]:{};e%2?d(Object(a),!0).forEach((function(e){Object(c["a"])(t,e,a[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(a)):d(Object(a)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(a,e))}))}return t}var b={wait:{status:"default",text:"待运行"},queuing:{status:"default",text:"排队中"},paused:{status:"default",text:"暂停中"},running:{status:"processing",text:"运行中"},success:{status:"success",text:"完成"},error:{status:"error",text:"异常"}},f={pageNo:1,pageSize:10,count:0},h=[{title:"蜘蛛名",dataIndex:"spiderName"},{title:"表名",dataIndex:"spiderTableName"},{title:"状态",dataIndex:"status",width:100,scopedSlots:{customRender:"status"}},{title:"开始时间",dataIndex:"startTime"},{title:"完成时间",dataIndex:"endTime"},{title:"耗时(秒)",dataIndex:"timeCost"},{title:"爬取数量",dataIndex:"successNum"},{title:"操作",dataIndex:"action",scopedSlots:{customRender:"action"}}],m={name:"JobList",components:{Ellipsis:u["c"]},data:function(){return{advanced:!0,searchModel:i.a.cloneDeep(f),data:[],columns:h,pagination:{},loading:!1,opLoading:!1}},filters:{statusFilter:function(t){return b[t].text},statusTypeFilter:function(t){return b[t].status}},mounted:function(){this.searchModel.spiderName=this.$route.query.spiderName,this.searchModel.spiderTableName=this.$route.query.spiderTableName,this.search()},methods:(n={pauseJob:function(t){var e=this;Object(l["d"])(t).then((function(t){t.success&&(e.$message.success("暂停成功"),e.search())}))},restartJob:function(t){var e=this;Object(l["e"])(t).then((function(t){t.success&&(e.$message.success("重启成功"),e.search())}))},stopJob:function(t){var e=this;Object(l["h"])(t).then((function(t){t.success&&(e.$message.success("结束成功"),e.search())}))}},Object(c["a"])(n,"stopJob",(function(t){var e=this;Object(l["h"])(t).then((function(t){t.success&&(e.$message.success("停止成功"),e.search())}))})),Object(c["a"])(n,"seeResult",(function(t){this.$router.push("/spider/jobRecord/".concat(t.id))})),Object(c["a"])(n,"toggleAdvanced",(function(){this.advanced=!this.advanced})),Object(c["a"])(n,"handleTableChange",(function(t,e,a){console.log(t);var n=p({},this.pagination);n.current=t.current,this.pagination=n,this.getList(t.current)})),Object(c["a"])(n,"search",(function(){this.getList(1)})),Object(c["a"])(n,"getList",(function(t){var e=this;this.loading=!0,void 0!=t&&(this.searchModel.pageNo=t);var a=i.a.cloneDeep(this.searchModel);Object(l["f"])(a).then((function(t){if(t.success){e.data=t.data;var a=p({},e.pagination);a.total=t.count,e.pagination=a}e.loading=!1}))})),n)},g=m,j=a("9ca4"),v=Object(j["a"])(g,s,r,!1,null,null,null);e["default"]=v.exports}}]);