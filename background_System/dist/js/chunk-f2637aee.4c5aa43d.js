(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-f2637aee"],{"1da1":function(t,e,r){"use strict";r.d(e,"a",(function(){return i}));r("d3b7");function n(t,e,r,n,i,o,a){try{var s=t[o](a),l=s.value}catch(c){return void r(c)}s.done?e(l):Promise.resolve(l).then(n,i)}function i(t){return function(){var e=this,r=arguments;return new Promise((function(i,o){var a=t.apply(e,r);function s(t){n(a,i,o,s,l,"next",t)}function l(t){n(a,i,o,s,l,"throw",t)}s(void 0)}))}}},"5a48":function(t,e,r){"use strict";r.r(e);var n=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",[r("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right"}},[r("el-breadcrumb-item",{attrs:{to:{path:"/home"}}},[t._v("首页")]),r("el-breadcrumb-item",[t._v("农产品追溯信息系统")]),r("el-breadcrumb-item",[t._v("投诉建议管理")])],1),r("el-tabs",{on:{"tab-click":t.handleClick},model:{value:t.activeName,callback:function(e){t.activeName=e},expression:"activeName"}},[r("el-tab-pane",{attrs:{label:"投诉管理",name:"first"}}),r("el-tab-pane",{attrs:{label:"建议管理",name:"second"}})],1),r("el-card",{staticStyle:{margin:"0"}},[r("div",[r("el-input",{staticStyle:{width:"200px","margin-left":"2rem"},attrs:{size:"small",placeholder:"投诉建议",clearable:""},on:{input:t.handleFilter},model:{value:t.suggestInfo,callback:function(e){t.suggestInfo=e},expression:"suggestInfo"}}),r("el-button",{staticStyle:{"margin-left":"0.5rem"},attrs:{size:"small",type:"primary",icon:"el-icon-search"},on:{click:t.handleFilter}},[t._v(" 查询 ")])],1),r("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.listLoading,expression:"listLoading"}],attrs:{data:t.rows,height:t.curHeight,border:"",stripe:"",fit:"","highlight-current-row":"","row-style":{height:"5px"},"cell-style":{padding:"5px 0"}}},[r("el-table-column",{attrs:{align:"center",label:"序号",width:"60"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(" "+t._s((t.listQuery.page-1)*t.listQuery.limit+e.$index+1)+" ")]}}])}),r("el-table-column",{attrs:{align:"center",label:"类型",width:"120"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(" "+t._s(e.row.type)+" ")]}}])}),r("el-table-column",{attrs:{align:"center",label:"主题",width:"200"},scopedSlots:t._u([{key:"default",fn:function(e){return[r("el-link",{attrs:{type:"primary",underline:!1}},[t._v(t._s(e.row.theme))])]}}])}),r("el-table-column",{attrs:{align:"center",label:"内容","min-width":"180"},scopedSlots:t._u([{key:"default",fn:function(e){return[r("el-popover",{attrs:{placement:"top-start",width:"250",trigger:"hover"}},[r("div",[t._v(t._s(e.row.content))]),e.row.hasOwnProperty("content")&&JSON.stringify(e.row.content).length>20?r("span",{attrs:{slot:"reference"},slot:"reference"},[t._v(t._s(e.row.content.substr(0,20))+"... ")]):r("span",{attrs:{slot:"reference"},slot:"reference"},[t._v(" "+t._s(e.row.content)+" ")])])]}}])}),r("el-table-column",{attrs:{align:"center",label:"姓名",width:"120"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(" "+t._s(e.row.name)+" ")]}}])}),r("el-table-column",{attrs:{align:"center",label:"联系方式",width:"160"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(" "+t._s(e.row.phone)+" ")]}}])}),r("el-table-column",{attrs:{align:"center",label:"地址",width:"200"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(" "+t._s(e.row.address)+" ")]}}])}),r("el-table-column",{attrs:{align:"center",label:"是否处理",width:"200"},scopedSlots:t._u([{key:"default",fn:function(e){return[r("el-switch",{attrs:{"active-color":"#13ce66","inactive-color":"#ff4949","active-value":1,"inactive-value":0,"active-text":"已处理","inactive-text":"未处理",disabled:!!e.row.status},on:{change:function(r){return t.changSwitch(r,e.row)}},model:{value:e.row.status,callback:function(r){t.$set(e.row,"status",r)},expression:"scope.row.status"}})]}}])}),r("el-table-column",{attrs:{align:"center",label:"修改人",width:"120"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(" "+t._s(e.row.updName)+" ")]}}])})],1),r("el-pagination",{attrs:{"current-page":t.listQuery.page,"page-sizes":[5,10,15,20],"page-size":t.listQuery.limit,layout:"total, sizes, prev, pager, next, jumper",total:t.total},on:{"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}})],1)],1)},i=[],o=(r("96cf"),r("1da1")),a=r("a66f"),s={data:function(){return{suggestInfo:"",suggestOptions:[],curHeight:0,listLoading:!1,rows:[],listQuery:{page:1,limit:10,typeList:""},total:0,activeName:"first",isDispose:!1,state:"",updName:"汪昇"}},created:function(){var t=this;this.getAllList(),this.setTableHeight(),window.onresize=function(){t.setTableHeight()}},methods:{getAllList:function(){var t=this;this.listLoading=!0,this.listQuery.typeList="first"==this.activeName?"投诉":"建议";var e=new FormData;e.append("page",this.listQuery.page),e.append("limit",this.listQuery.limit),e.append("type",this.listQuery.typeList),a["a"].getSuggestList(e).then((function(e){if(200!==e.status)return t.$message.error("获取失败");t.listLoading=!1,t.rows=e.data.rows,t.total=e.data.total}))},handleClick:function(){this.getAllList()},handleFilter:function(){var t=this;if(""!==this.suggestInfo){this.listLoading=!0,this.listQuery.typeList="first"==this.activeName?"投诉":"建议";var e=new FormData;e.append("page",this.listQuery.page),e.append("limit",this.listQuery.limit),e.append("theme",this.suggestInfo),a["a"].getSuggestList(e).then((function(e){if(200!==e.status)return t.$message.error("获取失败");t.listLoading=!1,t.rows=e.data.rows,t.total=e.data.total}))}else this.getAllList()},handleCurrentChange:function(t){this.listQuery.page=t,this.getAllList()},changSwitch:function(t,e){var r=this,n=e.id,i={id:n,updName:this.updName};a["a"].suggestStatusEdit(i).then((function(t){if(200!==t.status)return r.$message.error("处理失败");r.$message.success("处理成功"),r.getAllList()}))},deleteInfo:function(t){var e=this;return Object(o["a"])(regeneratorRuntime.mark((function r(){var n,i;return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:return n=[],n.push(t),r.next=4,e.$confirm("此操作将删除该企业信息,是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).catch((function(t){return t}));case 4:if(i=r.sent,"confirm"===i){r.next=7;break}return r.abrupt("return",e.$message.info("已经取消删除"));case 7:a["a"].businessDirectoryDel(n).then((function(t){if(200!==t.status)return e.$message.error("删除企业信息失败");e.$message.success("删除企业信息成功"),e.getAllList()}));case 8:case"end":return r.stop()}}),r)})))()},handleSizeChange:function(t){this.listQuery.limit=t,this.getAllList()},setTableHeight:function(){var t=document.documentElement.clientHeight||document.body.clientHeight;this.curHeight=t-310}}},l=s,c=r("2877"),u=Object(c["a"])(l,n,i,!1,null,null,null);e["default"]=u.exports},"96cf":function(t,e){!function(e){"use strict";var r,n=Object.prototype,i=n.hasOwnProperty,o="function"===typeof Symbol?Symbol:{},a=o.iterator||"@@iterator",s=o.asyncIterator||"@@asyncIterator",l=o.toStringTag||"@@toStringTag",c="object"===typeof t,u=e.regeneratorRuntime;if(u)c&&(t.exports=u);else{u=e.regeneratorRuntime=c?t.exports:{},u.wrap=b;var h="suspendedStart",f="suspendedYield",p="executing",d="completed",g={},m={};m[a]=function(){return this};var v=Object.getPrototypeOf,y=v&&v(v($([])));y&&y!==n&&i.call(y,a)&&(m=y);var w=k.prototype=L.prototype=Object.create(m);x.prototype=w.constructor=k,k.constructor=x,k[l]=x.displayName="GeneratorFunction",u.isGeneratorFunction=function(t){var e="function"===typeof t&&t.constructor;return!!e&&(e===x||"GeneratorFunction"===(e.displayName||e.name))},u.mark=function(t){return Object.setPrototypeOf?Object.setPrototypeOf(t,k):(t.__proto__=k,l in t||(t[l]="GeneratorFunction")),t.prototype=Object.create(w),t},u.awrap=function(t){return{__await:t}},S(E.prototype),E.prototype[s]=function(){return this},u.AsyncIterator=E,u.async=function(t,e,r,n){var i=new E(b(t,e,r,n));return u.isGeneratorFunction(e)?i:i.next().then((function(t){return t.done?t.value:i.next()}))},S(w),w[l]="Generator",w[a]=function(){return this},w.toString=function(){return"[object Generator]"},u.keys=function(t){var e=[];for(var r in t)e.push(r);return e.reverse(),function r(){while(e.length){var n=e.pop();if(n in t)return r.value=n,r.done=!1,r}return r.done=!0,r}},u.values=$,F.prototype={constructor:F,reset:function(t){if(this.prev=0,this.next=0,this.sent=this._sent=r,this.done=!1,this.delegate=null,this.method="next",this.arg=r,this.tryEntries.forEach(Q),!t)for(var e in this)"t"===e.charAt(0)&&i.call(this,e)&&!isNaN(+e.slice(1))&&(this[e]=r)},stop:function(){this.done=!0;var t=this.tryEntries[0],e=t.completion;if("throw"===e.type)throw e.arg;return this.rval},dispatchException:function(t){if(this.done)throw t;var e=this;function n(n,i){return s.type="throw",s.arg=t,e.next=n,i&&(e.method="next",e.arg=r),!!i}for(var o=this.tryEntries.length-1;o>=0;--o){var a=this.tryEntries[o],s=a.completion;if("root"===a.tryLoc)return n("end");if(a.tryLoc<=this.prev){var l=i.call(a,"catchLoc"),c=i.call(a,"finallyLoc");if(l&&c){if(this.prev<a.catchLoc)return n(a.catchLoc,!0);if(this.prev<a.finallyLoc)return n(a.finallyLoc)}else if(l){if(this.prev<a.catchLoc)return n(a.catchLoc,!0)}else{if(!c)throw new Error("try statement without catch or finally");if(this.prev<a.finallyLoc)return n(a.finallyLoc)}}}},abrupt:function(t,e){for(var r=this.tryEntries.length-1;r>=0;--r){var n=this.tryEntries[r];if(n.tryLoc<=this.prev&&i.call(n,"finallyLoc")&&this.prev<n.finallyLoc){var o=n;break}}o&&("break"===t||"continue"===t)&&o.tryLoc<=e&&e<=o.finallyLoc&&(o=null);var a=o?o.completion:{};return a.type=t,a.arg=e,o?(this.method="next",this.next=o.finallyLoc,g):this.complete(a)},complete:function(t,e){if("throw"===t.type)throw t.arg;return"break"===t.type||"continue"===t.type?this.next=t.arg:"return"===t.type?(this.rval=this.arg=t.arg,this.method="return",this.next="end"):"normal"===t.type&&e&&(this.next=e),g},finish:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var r=this.tryEntries[e];if(r.finallyLoc===t)return this.complete(r.completion,r.afterLoc),Q(r),g}},catch:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var r=this.tryEntries[e];if(r.tryLoc===t){var n=r.completion;if("throw"===n.type){var i=n.arg;Q(r)}return i}}throw new Error("illegal catch attempt")},delegateYield:function(t,e,n){return this.delegate={iterator:$(t),resultName:e,nextLoc:n},"next"===this.method&&(this.arg=r),g}}}function b(t,e,r,n){var i=e&&e.prototype instanceof L?e:L,o=Object.create(i.prototype),a=new F(n||[]);return o._invoke=N(t,r,a),o}function _(t,e,r){try{return{type:"normal",arg:t.call(e,r)}}catch(n){return{type:"throw",arg:n}}}function L(){}function x(){}function k(){}function S(t){["next","throw","return"].forEach((function(e){t[e]=function(t){return this._invoke(e,t)}}))}function E(t){function e(r,n,o,a){var s=_(t[r],t,n);if("throw"!==s.type){var l=s.arg,c=l.value;return c&&"object"===typeof c&&i.call(c,"__await")?Promise.resolve(c.__await).then((function(t){e("next",t,o,a)}),(function(t){e("throw",t,o,a)})):Promise.resolve(c).then((function(t){l.value=t,o(l)}),a)}a(s.arg)}var r;function n(t,n){function i(){return new Promise((function(r,i){e(t,n,r,i)}))}return r=r?r.then(i,i):i()}this._invoke=n}function N(t,e,r){var n=h;return function(i,o){if(n===p)throw new Error("Generator is already running");if(n===d){if("throw"===i)throw o;return A()}r.method=i,r.arg=o;while(1){var a=r.delegate;if(a){var s=O(a,r);if(s){if(s===g)continue;return s}}if("next"===r.method)r.sent=r._sent=r.arg;else if("throw"===r.method){if(n===h)throw n=d,r.arg;r.dispatchException(r.arg)}else"return"===r.method&&r.abrupt("return",r.arg);n=p;var l=_(t,e,r);if("normal"===l.type){if(n=r.done?d:f,l.arg===g)continue;return{value:l.arg,done:r.done}}"throw"===l.type&&(n=d,r.method="throw",r.arg=l.arg)}}}function O(t,e){var n=t.iterator[e.method];if(n===r){if(e.delegate=null,"throw"===e.method){if(t.iterator.return&&(e.method="return",e.arg=r,O(t,e),"throw"===e.method))return g;e.method="throw",e.arg=new TypeError("The iterator does not provide a 'throw' method")}return g}var i=_(n,t.iterator,e.arg);if("throw"===i.type)return e.method="throw",e.arg=i.arg,e.delegate=null,g;var o=i.arg;return o?o.done?(e[t.resultName]=o.value,e.next=t.nextLoc,"return"!==e.method&&(e.method="next",e.arg=r),e.delegate=null,g):o:(e.method="throw",e.arg=new TypeError("iterator result is not an object"),e.delegate=null,g)}function j(t){var e={tryLoc:t[0]};1 in t&&(e.catchLoc=t[1]),2 in t&&(e.finallyLoc=t[2],e.afterLoc=t[3]),this.tryEntries.push(e)}function Q(t){var e=t.completion||{};e.type="normal",delete e.arg,t.completion=e}function F(t){this.tryEntries=[{tryLoc:"root"}],t.forEach(j,this),this.reset(!0)}function $(t){if(t){var e=t[a];if(e)return e.call(t);if("function"===typeof t.next)return t;if(!isNaN(t.length)){var n=-1,o=function e(){while(++n<t.length)if(i.call(t,n))return e.value=t[n],e.done=!1,e;return e.value=r,e.done=!0,e};return o.next=o}}return{next:A}}function A(){return{value:r,done:!0}}}(function(){return this}()||Function("return this")())}}]);
//# sourceMappingURL=chunk-f2637aee.4c5aa43d.js.map