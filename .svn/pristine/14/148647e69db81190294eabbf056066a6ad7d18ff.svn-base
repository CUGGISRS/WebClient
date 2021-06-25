let viewer = null; // [ Object, 地图对象 ]
let nowI = Date.now(); // [ Number, 当前时间戳-毫秒级 ]
let stopTime = null; //日照分析

let tilesetObj = {}; //存储模型的对象
let curTerrain = null; //存储当前显示的地形
let terrainProviderDefault = null; //存储当前显示的地形

var measure;
var weather;
var flyMode;
var visibilityAnalysis;
var viewableAnalysis;
var shadowAnalysis;
var skylineAnalysis;
var excavationAnalysis;
var clippingAnalysis;
var ioTDevice;
// 分析参数
var viewModel = {
    verticalAngle: 90,
    horizontalAngle: 120,
    distance: 10
};


import flightPath from './lib/methods/flightPath.js';
import setDate from './lib/methods/setDate.js';
import watchAdmin from './lib/methods/watchAdmin.js';
import earthInit from './lib/methods/earthInit.js';
import spotData from './lib/methods/spotData.js';
import figure from './lib/methods/figure.js';

new Vue({
    el: '#app',
    data: {
        initBool: true, // [ Bool ,  初始化弹层-显示隐藏 ]
        headBool: false, // [ Bool ,  head头部显示隐藏 ]
        navState: '', // [ String, nav选中状态 ]
        navNewBool: false, // [ Bool ,  下侧导航显示隐藏 ]
        dateYear: '',
        dateMonth: '',
        dateDate: '',
        dateHour: '',
        dateMinu: '',
        dateSec: '',
        dateWeek: '',
        rainValue: 820.0, // 雨量
        snowValue: 0.3, // 雪量
        fogValue: 0.3, // 雾浓度
        initEchartsBool: false,
        data4: [{
            "offsetHeight": "60",
            "checked": true,
            "id": "3-3",
            "title": "3DTiles",
            "type": "3DTiles",
            "url": "./cesium/tileset.json"
        }, {
            "checked": true,
            "id": "3-4",
            "title": "glTF",
            "type": "glTF",
            "url": "./lib/Cesium/SampleData/models/CesiumAir/Cesium_Air.glb"
        }], //三维模型数据
        treeData: [],
        selBool: false, // [ Bool ,  导航搜索-显示隐藏 ]
        alertboxBool: false, // [ Bool ,  搜索弹框-显示隐藏 ]
        streeBool: false, //[Bool ,  图层管理 ]
        selInp: '', // [ String, 导航搜索内容 ]
        tipsboxBool: true, //引导提示框
        searchBoxBool: false, //左侧搜索框整体
        mapSwitch: false, // [ Bool , 地图切换 false-3D true-2Ddark ]
        vehicleBool: false, // [ Bool ,  右侧弹框-显示隐藏 ]
        vehicleSettingBool: false, // [ Bool , 右侧场景特效设置弹框 显示隐藏 ]
        vehicleSunshineBool: false, // [ Bool , 右侧挖填方设置弹框 显示隐藏 ]
        vehicleExcavationBool: false, // [ Bool , 右侧挖填方设置弹框 显示隐藏 ]
        vehicleSensorBool: false, // [ Bool , 右侧物联网监控列表 显示隐藏 ]
        vehicleClippingBool: false, // [ Bool , 右侧地形剖面分析结果弹框 显示隐藏 ]
        vehicleBigBool: false, // [ Bool , 右侧弹框 管理弹框放大缩小 ]
        vehicleSettingBigBool: false, // [ Bool , 右侧场景特效设置弹框 管理弹框放大缩小 ]
        vehicleSunshineBigBool: false, // [ Bool , 右侧挖填方设置弹框 显示隐藏 ]
        vehicleExcavationBigBool: false, // [ Bool , 右侧挖填方分析设置弹框 管理弹框放大缩小 ]
        vehicleSensorBigBool: false, // [ Bool , 右侧物联网监控列表弹框 管理弹框放大缩小 ]
        vehicleClippingBigBool: false, // [ Bool , 右侧地形剖面分析结果弹框 管理弹框放大缩小 ]
        vePeBool: false, // [ Bool ,  true车辆管理&false人员管理 ]
        // vehicleItemBool: false, // [ Bool ,  车辆管理列表点击弹出框-显示隐藏 ]
        watchBool: false, // [Boll,左侧视点管理弹框显示]
        watchBigBool: false, // [Boll,左侧视点管理弹框隐藏]
        flyBool: false,
        flyBigBool: false, // [Boll, 右侧飞行管理弹框隐藏]
        spotIdx: 0, //Array 下标
        spotDataIdx: {},
        watchList: [], //视点列表数组
        modal2: false, //增加视点对话框的显示
        modal3: false, //增加飞行浏览对话框的显示
        watchValue: '', //视点名称
        flyValue: '', //飞行浏览名称
        spotData: spotData, //功能数组
        isStart: true, //日照
        pageSize: 5, //视点列表分页每页数量
        dataCount: 1, //视点列表分页当前页数
        total: '', //视点列表分页总条数
        isAddWatch: false,
        deleteViewableAnalysis: true, // 是否清除视域分析
        cuttingDepth: 5, // 开挖深度
        excavationArea: '', // 挖方面积
        excavationResult: '', // 挖方结果
        excavationFillResult: '', // 填方结果
        flyTrackList: '', //飞行轨迹列表
        flyPageSize: 5, //飞行轨迹列表分页每页数量
        flyLimit: 1, //飞行轨迹列表分页当前页数
        flyTotal: '', //飞行轨迹列表分页总条数
        terrainClipPlan: null,
        deleteExcavation: true,
        tpmTileset: null,
        showIoT: false, // 显示物联网设备
        ioTData: [], // 物联网设备
        ioTDatas: [], // 物联网设备
        ioTCurrent: 1, // 物联网当前页
        ioTPageSize: 10, // 物联网每页条数
        ioTTotal: 0, // 物联网设备总数
        isFirstLoad: true, // 首屏加载
        sensorData: [], // 传感器
        sensorDatas: [], // 传感器
        sensorCurrent: 1, // 物联网当前页
        sensorPageSize: 8, // 物联网每页条数
        sensorTotal: 0, // 物联网设备总数
        currentFacId: '', // 当前设备
        flvPlayer: null,
        isActive: '', //点击飞行项背景色变深
        marksIndex: 1, //飞行轨迹点索引
        pitchValue: -90, //飞行视角角度
        changeCameraTime: 5,
        flytime: 5,
        Exection: {},
        activeShapePoints: [],
        floatingPoint: undefined,
        activeShape: undefined,
        drawingMode: 'line',
        height: 2000, //飞行浏览高度
        marks: [], //飞行轨迹点组成线的数组
        trackWayId: '', // 飞行轨迹的id
        flying: false, //判断是否飞行
        mapType: '电子地图', //显示当前地图类型
        searchInputValue: ''
    },
    created() {
        // 数据初始化
        this.dataInit();
    },
    watch: {
        // nav搜索内容监听
        selInp(n, o) {
            // nav搜索栏内容状态处理方法
            //this.selNavState(n)
        },
    },
    mounted() {
        // 初始化地球
        this.mapInit();
    },
    methods: {
        // 切换地图类型
        switchMap() {
            if (this.mapType === '电子地图') {
                this.mapType = '三维球';
                viewer.scene.morphTo2D(0);
                // scene.morphToColumbusView(0);
            } else {
                this.mapType = '电子地图';
                viewer.scene.morphTo3D(0);
            }
        },
        //人物漫游
        figureRoaming() {
            figure.figureRoaming(viewer);
            // var position = Cesium.Cartesian3.fromDegrees(114.305, 30.515, 100);
            // var url = "/lib/Cesium/SampleData/models/CesiumMan/Cesium_Man.glb";
            // viewer.trackedEntity = viewer.entities.add({
            // 	name: url,
            // 	position: position,
            // 	model: {
            // 		uri: url,
            // 		scale: 10,
            // 		// 贴地
            // 		heightReference: Cesium.HeightReference.CLAMP_TO_GROUND
            // 	},
            // });


            // viewer.scene.camera.flyTo({
            // 	destination: Cesium.Cartesian3.fromDegrees(114.305, 30.515, 6.0), //定位坐标点，建议使用谷歌地球坐标位置无偏差
            // 	duration: 3 //定位的时间间隔
            // });
            // var modelMatrix = Cesium.Transforms.eastNorthUpToFixedFrame(Cesium.Cartesian3.fromDegrees(114.305, 30.515, 0.0));
            // var model = viewer.scene.primitives.add(Cesium.Model.fromGltf({
            // 	url: "/lib/Cesium/SampleData/models/CesiumMan/Cesium_Man.glb",
            // 	modelMatrix: modelMatrix,
            // 	scale: 100.0
            // }))

            // //利用entity进行加载
            // var modleGlb = viewer.entities.add({
            // 	position: Cesium.Cartesian3.fromDegrees(114.305, 30.515, 100.0),
            // 	name: "奶牛车glb",
            // 	model: {
            // 		uri: "/lib/Cesium/SampleData/models/CesiumMan/Cesium_Man.glb"
            // 	}
            // });

            // //定位过去
            // viewer.zoomTo(modleGlb);


        },
        //初始化飞行
        initFly() {
            viewer.scene.camera.flyTo({
                destination: Cesium.Cartesian3.fromDegrees(this.marks[0].longitude, this.marks[0].latitude, this.marks[0].height), //定位坐标点，建议使用谷歌地球坐标位置无偏差
                duration: 3 //定位的时间间隔
            });
            handler = new Cesium.ScreenSpaceEventHandler(viewer.canvas);
        },
        //沿轨迹飞行
        flyExtent() {
            flightPath.flyExtent(this, viewer);
        },

        // 相机原地定点转向
        changeCameraHeading() {
            flightPath.changeCameraHeading(this, viewer);
        },

        // 设置飞行的时间到viewer的时钟里
        setExtentTime(time) {
            flightPath.setExtentTime(time, viewer);
        },

        // 相机视角飞行 结束
        // 飞行时 camera的方向调整(heading) 开始
        // 角度转弧度
        toRadians(degrees) {
            return degrees * Math.PI / 180;
        },
        // 弧度转角度
        toDegrees(radians) {
            return radians * 180 / Math.PI;
        },
        //计算俯仰角
        bearing(startLat, startLng, destLat, destLng) {
            startLat = this.toRadians(startLat);
            startLng = this.toRadians(startLng);
            destLat = this.toRadians(destLat);
            destLng = this.toRadians(destLng);

            let y = Math.sin(destLng - startLng) * Math.cos(destLat);
            let x = Math.cos(startLat) * Math.sin(destLat) - Math.sin(startLat) * Math.cos(destLat) * Math.cos(destLng - startLng);
            let brng = Math.atan2(y, x);
            let brngDgr = this.toDegrees(brng);

            return (brngDgr + 360) % 360;
        },

        // 飞行时 camera的方向调整(heading) 结束
        // 绘制路线
        drawLineRoad(id) {
            flightPath.drawLineRoad(id, this, viewer, handler);
        },

        // 重新绘制形状，使它不是动态的，并删除动态形状。
        terminateShape(id) {
            flightPath.terminateShape(id, this, viewer);
        },

        //绘制点
        createPoint(worldPosition) {
            var point = viewer.entities.add({
                position: worldPosition,
                point: {
                    color: Cesium.Color.RED,
                    pixelSize: 8,
                    outlineColor: Cesium.Color.WHITE,
                    outlineWidth: 3,
                    heightReference: Cesium.HeightReference.CLAMP_TO_GROUND
                }
            });
            return point;
        },
        //初始化为线
        drawShape(positionData) {
            var shape;
            if (this.drawingMode === 'line') {
                shape = viewer.entities.add({
                    polyline: {
                        material: Cesium.Color.YELLOW,
                        positions: positionData,
                        clampToGround: true,
                        width: 3
                    }
                });
            }
            return shape;
        },


        // 文字截取加点方法
        textSubstr(value, length) {
            let val = value
            if (value == '' || value == undefined) {
                return ''
            }
            if (value.length > length) {
                val = val.substring(0, length) + '...'
            }
            return val
        },
        // 右侧弹框 详情 进入方法
        spotInfoClick(item, i) {
            //是否贴地
            var clampToGround = true;

            if (item.name === '空间距离') {
                measure.drawLineMeasureGraphics({
                    clampToGround: clampToGround,
                    callback: () => {
                    }
                });
            } else if (item.name === '空间面积') {
                measure.drawAreaMeasureGraphics({
                    clampToGround: clampToGround,
                    callback: () => {
                    }
                });
            } else if (item.name === '水平距离') {
                measure.drawHorizontalMeasureGraphics({
                    clampToGround: clampToGround,
                    callback: () => {
                    }
                });
            } else if (item.name === '垂直距离') {
                measure.drawVerticalMeasureGraphics({
                    clampToGround: clampToGround,
                    callback: () => {
                    }
                });
            } else if (item.name === '三角测量') {
                measure.drawTrianglesMeasureGraphics({
                    clampToGround: clampToGround,
                    callback: () => {
                    }
                });
            } else if (item.name === '测量清除') {
                measure._drawLayer.entities.removeAll();
            } else if (item.name === '雨') {
                weather.showRainWeather(true, 1000 - this.rainValue);
            } else if (item.name === '雪') {
                weather.showSnowWeather(true, this.snowValue);
            } else if (item.name === '雾') {
                weather.showFogWeather(true, this.fogValue);
            } else if (item.name === '特效清除') {
                weather.removeRainWeather();
                weather.removeSnowWeather();
                weather.removeFogWeather();
            } else if (item.name === '特效设置') {
                this.vehicleSettingBool = true;
            } else if (item.name === '场景漫游') {
                // flyMode.firstViewMode(true);
                this.figureRoaming();
            } else if (item.name === '停止场景漫游') {
                // flyMode.firstViewMode(false);
                figure.quitFigure(viewer);
            } else if (item.name === '飞行浏览') {
                this.showFlyViewFrame();
            } else if (item.name === '日照分析') {
                this.sunPlay();
            } else if (item.name === '通视分析') {
                visibilityAnalysis.drawVisibilityGraphics({
                    clampToGround: clampToGround,
                    callback: () => {
                    }
                });
            } else if (item.name === '视域分析') {
                if (this.deleteViewableAnalysis) {
                    viewableAnalysis = new Cesium.ViewShed3D(viewer, {
                        horizontalAngle: Number(viewModel.horizontalAngle),
                        verticalAngle: Number(viewModel.verticalAngle),
                        distance: Number(viewModel.distance),
                        calback: function () {
                            viewModel.distance = viewableAnalysis.distance
                        }
                    });
                    this.deleteViewableAnalysis = false;
                }
            } else if (item.name === '阴影分析') {
                shadowAnalysis.drawShadowGraphics({
                    clampToGround: clampToGround,
                    callback: () => {
                    }
                });
            } else if (item.name === '天际线分析') {
                skylineAnalysis.drawSkylineGraphics({
                    clampToGround: clampToGround,
                    callback: () => {
                    }
                });
            } else if (item.name === '挖填方分析') {
                this.vehicleExcavationBool = true;
            } else if (item.name === '地形剖面') {
                clippingAnalysis.profile({
                    callback: () => {
                        this.vehicleClippingBool = true;
                        this.vehicleClippingBigBool = true;
                    }
                });
            } else if (item.name === '清除分析') {
                // 清除日照分析
                if (this.isStart === false) {
                    this.sunPlay();
                }
                // viewer.scene.globe.enableLighting = false;
                viewer.clock.shouldAnimate = true;
                // 清除通视分析
                visibilityAnalysis._drawLayer.entities.removeAll();
                // 清除视域分析
                if (!this.deleteViewableAnalysis) {
                    viewableAnalysis.destroy();
                    this.deleteViewableAnalysis = true;
                }
                // 清除阴影分析
                shadowAnalysis._drawLayer.entities.removeAll();
                // 清除挖填方
                excavationAnalysis._drawLayer.entities.removeAll();
                excavationAnalysis.clear();
                // 清除地形剖面
                clippingAnalysis._drawLayer.entities.removeAll();
                this.vehicleClippingClose();
                this.excavationArea = '';
                this.excavationResult = '';
                this.excavationFillResult = '';
                this.deleteExcavation = true;
                // 清除天际线分析
                if (viewer.scene.postProcessStages) {
                    viewer.scene.postProcessStages.removeAll();
                }
            }
        },

        //日照分析
        sunPlay() {
            if (this.isStart === true) {
                this.isStart = false
                earthInit.stratPlay(viewer, stopTime)
            } else {
                this.isStart = true
                viewer.shadows = false;
                viewer.clock.currentTime = new Date();
                stopTime = null;
            }
        },

        //  场景特效设置
        //  refreshRain 刷新雨
        refreshRain(value) {
            weather.removeRainWeather();
            weather.showRainWeather(true, 1000 - value);
        },
        //  refreshSnow 刷新雪
        refreshSnow(value) {
            weather.removeSnowWeather();
            weather.showSnowWeather(true, value);
        },
        //  refreshFog 刷新雾
        refreshFog(value) {
            weather.removeFogWeather();
            weather.showFogWeather(true, value);
        },
        rainFormat(value) {
            if (1000 - value < 100) {
                return '暴雨'
            } else if (1000 - value >= 100 && 1000 - value < 400) {
                return '大雨'
            } else if (1000 - value >= 400 && 1000 - value < 800) {
                return '中雨'
            } else if (1000 - value >= 800) {
                return '小雨'
            }
        },
        clippingResultResize() {
            clippingAnalysis.resizeChart();
        },
        /**
         * 右侧弹框
         */
        tipsClickVehicle(bool) {
            this.navState = bool;
            //this.vehicleItemBool = false
            // this.vehicleCut = 1
            // this.vePeListSel(this.vePeBool)
            //右侧弹框
            this.vehicleBool = true;
            this.showIoT = false;
            console.log(bool)
            if (bool === '系统量算') {
                this.spotIdx = 0;
                document.getElementById("vehicle-box-head-name").innerHTML = "系统量算";
            } else if (bool === '场景特效') {
                this.spotIdx = 1;
                document.getElementById("vehicle-box-head-name").innerHTML = "场景特效";
            } else if (bool === '虚拟活动') {
                this.spotIdx = 2;
                document.getElementById("vehicle-box-head-name").innerHTML = "虚拟活动";
            } else if (bool === '三维分析') {
                this.spotIdx = 3;
                document.getElementById("vehicle-box-head-name").innerHTML = "三维分析";
            } else if (bool === '其他模块') {
                this.spotIdx = 4;
                document.getElementById("vehicle-box-head-name").innerHTML = "其他模块";
            }
            this.spotData.map(item => {
                if (bool === item.name) {
                    this.spotDataIdx = item.children;
                }
            })
            if (bool === '全面感知') {
                this.showIoT = true;
                document.getElementById("vehicle-box-head-name").innerHTML = "物联网设备";
                // (async () => {
                //     this.ioTData = ioTData;
                //     this.ioTDatas = ioTDatas;
                //     this.ioTTotal = ioTTotal;
                // })();
                this.ioTData = [];
                this.ioTDatas = [];
                this.ioTTotal = 0;
                ioTDevice.login({
                    enterprises: ioTEnterprises
                });
                for (let i = 0; i < ioTEnterprises.length; i++) {
                    let enterprise = ioTEnterprises[i];
                    if (!enterprise.token) {
                        continue;
                    }
                    this.ioTDatas.push(enterprise);
                    this.ioTTotal++;
                    if (this.ioTTotal > (this.ioTCurrent - 1) * this.ioTPageSize &&
                        this.ioTTotal <= this.ioTCurrent * this.ioTPageSize) {
                        this.ioTData.push(enterprise);
                    }
                }
                ioTDevice.drawDeviceGraphics({
                    ioTData: this.ioTDatas
                });
            }
        },
        //右侧弹框关闭方法
        vehicleClose() {
            this.vehicleBigBool = false
            //右侧弹框
            this.vehicleBool = false
            // this.vehicleItemBool = false
            this.vehicleSensorClose();
        },
        vehicleSettingClose() {
            this.vehicleSettingBigBool = false;
            //右侧弹框
            this.vehicleSettingBool = false;
        },
        vehicleExcavationClose() {
            this.vehicleExcavationBool = false;
            //右侧弹框
            this.vehicleExcavationBigBool = false;
        },
        vehicleSensorClose() {
            this.vehicleSensorBool = false;
            //右侧弹框
            this.vehicleSensorBigBool = false;
        },
        vehicleClippingClose() {
            this.vehicleClippingBool = false;
            //右侧弹框
            this.vehicleClippingBigBool = false;
        },
        //右侧飞行浏览弹框
        showFlyViewFrame() {
            this.flyBool = true;
            this.watchBool = false
            //右侧弹框
            this.watchBigBool = false
            document.getElementById('fly-box-head-name').innerHTML = '飞行浏览';
            this.getFlyTrackList();
        },
        //右侧飞行浏览弹框关闭
        flyViewClose() {
            this.flyBool = false;
            this.flyBigBool = false;
            viewer.entities.removeAll();
            this.exitFly();
        },
        //左侧弹框关闭方法
        watchClose() {
            this.watchBool = false
            //右侧弹框
            this.watchBigBool = false
        },
        //点击弹出增加视点对话框
        handleModalShow() {
            this.modal2 = true;
        },
        //保存视点项
        saveWatchList() {
            watchAdmin.saveWatchList(this, viewer);
        },
        //删除视点的一项
        delWatchPoint(id) {
            watchAdmin.delWatchPoint(id, this);
        },
        //视点列表弹框显示
        watchTipsClickItem() {
            this.flyBool = false;
            this.flyBigBool = false;
            watchAdmin.watchTipsClickItem('视点列表', this);
        },
        //分页切换
        changepage(index) {
            this.dataCount = index;
            watchAdmin.watchTipsClickItem('视点列表', this);
        },
        //点击视点跳转
        handleWatchPoint(val) {
            watchAdmin.handleWatchPoint(val, viewer);
        },
        //新建飞行轨迹
        addFlyPath() {
            this.modal3 = true;
        },
        // //保存飞行浏览项
        saveFlyList() {
            flightPath.saveFlyList(this);
        },
        //分页获取飞行路径列表
        getFlyTrackList() {
            flightPath.getFlyTrackList(this);
        },
        //设定飞行路线
        setPathPlan() {
            if (this.trackWayId) {
                this.drawLineRoad(this.trackWayId);
            } else {
                this.$Message.error('请选中一条飞行路径')
            }
        },
        //开始飞行
        startFly() {
            const {
                Exection,
            } = this;
            if (Object.keys(Exection).length > 0) {
                this.exitFly();
            }
            this.flying = true;
            this.flyExtent();
        },
        //暂停飞行
        stopFly() {
            viewer.clock.shouldAnimate = false;
        },
        //继续飞行
        continueFly() {
            viewer.clock.shouldAnimate = true;
        },
        //退出飞行
        exitFly() {
            flightPath.exitFly(this, viewer);
        },
        //点击飞行轨迹
        handleFlyTrack(item, index) {
            flightPath.handleFlyTrack(item, index, this, viewer);
        },
        //删除飞行轨迹
        delFlyTrack(id) {
            flightPath.delFlyTrack(this, id, viewer);
        },
        //飞行轨迹分页切换
        flyChangepage(index) {
            this.flyLimit = index;
            this.getFlyTrackList();
        },
        //图层管理
        //点击树节点时触发
        selectChange(data) {
            console.log(data);
            let checkNodes = this.$refs.tree.getSelectedNodes();
            console.log(checkNodes)
        },
        //勾选复选框时触发
        checkChange(data, node) {
            //所有已勾选数据
            // console.log(data, node);
            // console.log('tree', this.$refs.tree);
            if (node.children) {
                for (let i = 0; i < node.children.length; i++) {
                    this.checkChange([node.children[i]], node.children[i]);
                }
            } else {
                if (node.checked === false) {
                    tilesetObj[node.name].show = false;
                    if (node.type == 'DEM') {
                        if (curTerrain != null) {
                            viewer.terrainProvider = terrainProviderDefault;
                            curTerrain = null;
                        }
                    }

                } else {
                    tilesetObj[node.name].show = true;
                    if (node.type == 'DEM') {
                        if (curTerrain != null) {
                            let nodeKey = curTerrain.nodeKey;
                            curTerrain = null;
                            this.$refs.tree.handleCheck({
                                checked: false,
                                nodeKey: nodeKey
                            })
                        }
                        viewer.terrainProvider = tilesetObj[node.name];
                        curTerrain = node;
                    }
                }
            }
        },
        //树列表，定位功能
        locationPoint(data) {
            let tileset = tilesetObj[data.name]
            //使用viewer定位到模型位置
            this.flyToTileSet(tileset);
        },
        //渲染树节点后的按钮
        renderContent(h, {
            root,
            node,
            data
        }) {
            return h('span', {
                style: {
                    display: 'inline-block',
                    width: '100%'
                }
            }, [
                h('span', [
                    h('Icon', {
                        style: {
                            marginRight: '8px'
                        }
                    }),
                    h('span', data.title)
                ]),
                h('span', {
                    style: {
                        display: 'inline-block',
                        float: 'right',
                        marginRight: '50px'
                    }
                }, [
                    h('Button', {
                        style: {
                            display: data.type !== 'DEM' && data.checked && !data.root === true ? 'block' : 'none',
                        },
                        props: Object.assign({}, {
                            type: 'success',
                            size: 'small',
                        }),
                        on: {
                            click: () => {
                                this.locationPoint(data)
                            }
                        }
                    }, '定位')
                    // }, data.view === true ? '定位':'XXX' )
                ])
            ]);
        },
        test() {
            earthInit.measureLineSpace(viewer, handler);
        },
        // nav搜索栏清空方法
        searchClear() {
            //搜索框清空
            this.selInp = ''
            this.searchInputValue = '';
            this.searchClick();
            //搜索弹框 active
            this.alertboxBool = false
            this.initEchartsBool = true;
            //this.access()
        },
        // 默认弹框-历史记录点击&模块点击
        tipsClickItem(name) {
            //选中状态样式
            this.navState = name
            if (name == '图层管理') {
                this.watchBool = false;
                // this.flyBool = false;
                // this.vehicleBool = false;
                // this.vehicleSettingBool = false;
                // this.vehicleExcavationBool = false;
                // this.vehicleSensorBool = false;
                this.streeBool = true;
            }
            //搜索框
            //this.selBool = true;
            //搜索框内的文字
            this.selInp = name;
            this.searchFocus();
        },
        // nav搜索栏获取焦点方法
        searchFocus() {
            // console.log('seach聚焦')
            //搜索框 active
            this.alertboxBool = true;
            this.selNavState(this.selInp);
            this.streeBool = true;
        },
        //搜索按钮事件
        searchClick() {
            // if (this.searchInputValue !== '') {
            $.ajax({
                url: IP + PORT_1 + '/FileDirOpt/getJsonByPath',
                data: {
                    fileName: 'layerData',
                    nodeKey: 'title',
                    nodeValue: this.searchInputValue
                },
                type: 'POST',
                success: (res) => {
                    if (res.status === 200) {
                        this.data4 = res.data;
                        this.data4.map(item => {
                            if (item.children) {
                                item.children.map(this.analysisData);
                            } else {
                                this.isDataFile(item);
                            }
                        })
                    }
                }
            })
            // } else {
            //     this.searchClick();
            // }
        },
        // nav搜索栏失去焦点方法
        searchBlur() {
            // this.alertboxBool = false
        },
        // nav搜索栏内容状态处理方法
        selNavState(info) {
        },
        // 设置时间
        dataSet() {
            setDate.dataSet(this);
        },
        // 进入地图方法
        access() {
            let data = null;
            for (let i in tilesetObj) {
                if (tilesetObj[i].type !== 'DEM' && tilesetObj[i].show) {
                    data = i;
                    break;
                }
            }
            if (data) {
                let tileset = tilesetObj[data];
                this.flyToTileSet(tileset);
            }
        },
        // 进入地图事件
        clickAccess() {
            earthInit.clickAccess(this);
        },
        // 数据大屏链接跳转方法
        routerLinkClick() {

        },
        // 地球自转控制 bool-true开启 bool-false结束
        earthRotate(bool) {
            if (bool) {
                viewer.clock.onTick.addEventListener(this.rotate)
            } else {
                viewer.clock.onTick.removeEventListener(this.rotate)
            }
        },
        // 地球自转方法
        rotate() {
            // console.log('执行自转')
            let a = .1;
            let t = Date.now();
            let n = (t - nowI) / 1e3;
            nowI = t;
            viewer.scene.camera.rotate(Cesium.Cartesian3.UNIT_Z, -a * n)
        },
        // Map地图初始化方法
        mapInit() {
            Cesium.Ion.defaultAccessToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiI1MDZiODg5ZS0yMTYwLTQwNzgtODNkMi0xMjk5NzU1NDMzYTciLCJpZCI6MTM0MTUsInNjb3BlcyI6WyJhc3IiLCJnYyJdLCJpYXQiOjE1NjMyNTQ1ODF9.ZSTW9uIkKQKW2hO3KsHdBRJ-udZ4tWMEChZCSslFTyw';
            // 初始化 Map 对象
            viewer = new Cesium.Viewer('cesiumContainer', {
                animation: false, // [ Bool, 是否显示动画控件 ]
                shouldAnimate: true, // [ Bool, 是否开启动画 ]
                homeButton: false, // [ Bool, 是否显示Home按钮 ]
                vrButton: false, // [ Bool, 是否显示vr按钮 ]
                fullscreenButton: true, // [ Bool, 是否显示全屏按钮 ]
                baseLayerPicker: false, // [ Bool, 是否显示图层选择控件 ]
                geocoder: false, // [ Bool, 是否显示地名查找控件 ]
                timeline: false, // [ Bool, 是否显示时间线控件 ]
                navigationHelpButton: false, // [ Bool, 是否显示帮助信息控件 ]
                infoBox: false, // [ Bool, 是否显示点击要素之后显示的信息 ]
                requestRenderMode: true, // [ Bool, 启用请求渲染模式 ]
                scene3DOnly: false, // [ Bool, 每个几何实例将只能以3D渲染以节省GPU内存 ]
                sceneModePicker: false, // [ Bool, 是否显示场景切换控件 ]
                sceneMode: 3, // [ Number,初始场景模式 1 2D模式 2 2D循环模式 3 3D模式  Cesium.SceneMode ]
                fullscreenElement: document.body, // [ Object, 全屏时渲染的HTML元素 ]
            });

            // 清除默认的第一个影像 bing地图影像
            viewer.imageryLayers.remove(viewer.imageryLayers.get(0));
            // arcGis影像
            // let img = viewer.imageryLayers.addImageryProvider(
            // 	new Cesium.ArcGisMapServerImageryProvider({
            // 		url: 'https://services.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer',
            // 		baseLayerPicker: false
            // 	})
            // );
            // // token
            // let tk = "9f227eff2fa8bbe7cd16f618aadcbc78";
            // // 服务域名
            // let tdtUrl = 'https://t{s}.tianditu.gov.cn/';
            // // 服务负载子域
            // let subdomains = ['0', '1', '2', '3', '4', '5', '6', '7'];
            // let img = viewer.imageryLayers.addImageryProvider(new Cesium.UrlTemplateImageryProvider({
            //     url: "http://t{s}.tianditu.gov.cn/DataServer?T=img_w&X={x}&Y={y}&L={z}" + "&tk=" + tk,
            //     maximumLevel: 18, // 天地图的最大缩放级别
            //     subdomains: subdomains,
            //     show: false
            // }));
            // let img = viewer.imageryLayers.addImageryProvider(new Cesium.UrlTemplateImageryProvider({
            //     url: "https://mt0.google.cn/vt?lyrs=s&amp&x={x}&y={y}&z={z}",
            //     maximumLevel: 21, // 天地图的最大缩放级别
            //     format: "image/jpeg",
            //     tileMatrixSetID: "GoogleMapsCompatible"
            //     show: false
            // }));
            // // 影像的亮度
            // img.brightness = 0.8;

            // 天地图国界
            // let gj = viewer.imageryLayers.addImageryProvider(new Cesium.UrlTemplateImageryProvider({
            //     url: "http://t{s}.tianditu.gov.cn/DataServer?T=ibo_w&X={x}&Y={y}&L={z}" + "&tk=" + tk,
            //     maximumLevel: 18, // 天地图的最大缩放级别
            //     subdomains: subdomains,
            //     show: false
            // }));
            // // 省界
            // let sj = viewer.dataSources.add(Cesium.GeoJsonDataSource.load('https://geo.datav.aliyun.com/areas_v2/bound/100000_full.json', {
            //     stroke: Cesium.Color.YELLOW,
            //     fill: Cesium.Color.RED.withAlpha(0),
            //     strokeWidth: 3,
            //     markerSymbol: '?',
            //     clampToGround: true
            // }));
            //
            // // 叠加天地图三维地名服务
            // var wtfs = new Cesium.GeoWTFS({
            //     viewer,
            //     subdomains: subdomains,
            //     metadata: {
            //         boundBox: {
            //             minX: -180,
            //             minY: -90,
            //             maxX: 180,
            //             maxY: 90
            //         },
            //         minLevel: 1,
            //         maxLevel: 20
            //     },
            //     aotuCollide: true, //是否开启避让
            //     collisionPadding: [5, 10, 8, 5], //开启避让时，标注碰撞增加内边距，上、右、下、左
            //     serverFirstStyle: true, //服务端样式优先
            //     labelGraphics: {
            //         font: "28px sans-serif",
            //         fontSize: 28,
            //         fillColor: Cesium.Color.WHITE,
            //         scale: 0.5,
            //         outlineColor: Cesium.Color.BLACK,
            //         outlineWidth: 5,
            //         style: Cesium.LabelStyle.FILL_AND_OUTLINE,
            //         showBackground: false,
            //         backgroundColor: Cesium.Color.RED,
            //         backgroundPadding: new Cesium.Cartesian2(10, 10),
            //         horizontalOrigin: Cesium.HorizontalOrigin.MIDDLE,
            //         verticalOrigin: Cesium.VerticalOrigin.TOP,
            //         eyeOffset: Cesium.Cartesian3.ZERO,
            //         pixelOffset: new Cesium.Cartesian2(0, 8)
            //     },
            //     billboardGraphics: {
            //         horizontalOrigin: Cesium.HorizontalOrigin.CENTER,
            //         verticalOrigin: Cesium.VerticalOrigin.CENTER,
            //         eyeOffset: Cesium.Cartesian3.ZERO,
            //         pixelOffset: Cesium.Cartesian2.ZERO,
            //         alignedAxis: Cesium.Cartesian3.ZERO,
            //         color: Cesium.Color.WHITE,
            //         rotation: 0,
            //         scale: 1,
            //         width: 18,
            //         height: 18
            //     }
            // });
            //
            // //三维地名服务，使用wtfs服务
            // wtfs.getTileUrl = function () {
            //     return tdtUrl + 'mapservice/GetTiles?lxys={z},{x},{y}&tk=' + tk;
            // }
            //
            // wtfs.initTDT([{
            //     "x": 6,
            //     "y": 1,
            //     "level": 2,
            //     "boundBox": {"minX": 90, "minY": 0, "maxX": 135, "maxY": 45}
            // }, {"x": 7, "y": 1, "level": 2, "boundBox": {"minX": 135, "minY": 0, "maxX": 180, "maxY": 45}}, {
            //     "x": 6,
            //     "y": 0,
            //     "level": 2,
            //     "boundBox": {"minX": 90, "minY": 45, "maxX": 135, "maxY": 90}
            // }, {"x": 7, "y": 0, "level": 2, "boundBox": {"minX": 135, "minY": 45, "maxX": 180, "maxY": 90}}, {
            //     "x": 5,
            //     "y": 1,
            //     "level": 2,
            //     "boundBox": {"minX": 45, "minY": 0, "maxX": 90, "maxY": 45}
            // }, {"x": 4, "y": 1, "level": 2, "boundBox": {"minX": 0, "minY": 0, "maxX": 45, "maxY": 45}}, {
            //     "x": 5,
            //     "y": 0,
            //     "level": 2,
            //     "boundBox": {"minX": 45, "minY": 45, "maxX": 90, "maxY": 90}
            // }, {"x": 4, "y": 0, "level": 2, "boundBox": {"minX": 0, "minY": 45, "maxX": 45, "maxY": 90}}, {
            //     "x": 6,
            //     "y": 2,
            //     "level": 2,
            //     "boundBox": {"minX": 90, "minY": -45, "maxX": 135, "maxY": 0}
            // }, {"x": 6, "y": 3, "level": 2, "boundBox": {"minX": 90, "minY": -90, "maxX": 135, "maxY": -45}}, {
            //     "x": 7,
            //     "y": 2,
            //     "level": 2,
            //     "boundBox": {"minX": 135, "minY": -45, "maxX": 180, "maxY": 0}
            // }, {"x": 5, "y": 2, "level": 2, "boundBox": {"minX": 45, "minY": -45, "maxX": 90, "maxY": 0}}, {
            //     "x": 4,
            //     "y": 2,
            //     "level": 2,
            //     "boundBox": {"minX": 0, "minY": -45, "maxX": 45, "maxY": 0}
            // }, {"x": 3, "y": 1, "level": 2, "boundBox": {"minX": -45, "minY": 0, "maxX": 0, "maxY": 45}}, {
            //     "x": 3,
            //     "y": 0,
            //     "level": 2,
            //     "boundBox": {"minX": -45, "minY": 45, "maxX": 0, "maxY": 90}
            // }, {"x": 2, "y": 0, "level": 2, "boundBox": {"minX": -90, "minY": 45, "maxX": -45, "maxY": 90}}, {
            //     "x": 0,
            //     "y": 1,
            //     "level": 2,
            //     "boundBox": {"minX": -180, "minY": 0, "maxX": -135, "maxY": 45}
            // }, {"x": 1, "y": 0, "level": 2, "boundBox": {"minX": -135, "minY": 45, "maxX": -90, "maxY": 90}}, {
            //     "x": 0,
            //     "y": 0,
            //     "level": 2,
            //     "boundBox": {"minX": -180, "minY": 45, "maxX": -135, "maxY": 90}
            // }]);

            // // 天地图标注
            // let label = viewer.imageryLayers.addImageryProvider(new Cesium.UrlTemplateImageryProvider({
            //     url: "http://t{s}.tianditu.gov.cn/DataServer?T=cia_w&X={x}&Y={y}&L={z}" + "&tk=" + tk,
            //     maximumLevel: 18, // 天地图的最大缩放级别
            //     subdomains: subdomains,
            //     show: false
            // }));
            // // 标注的亮度
            // label.brightness = 0.8;

            viewer.scene.undergroundMode = true; // [ Bool , 设置开启地下场景 ]
            viewer.scene.screenSpaceCameraController.minimumZoomDistance = -50; // [ Number ,设置相机最小缩放距离,距离地表n米 ]
            viewer.scene.terrainProvider.isCreateSkirt = false; // [ Bool , 关闭裙边 ]
            viewer.scene.globe.enableLighting = true; // [ Bool , 是否添加全球光照，scene(场景)中的光照将会随着每天时间的变化而变化 ]
            viewer.scene.globe.showGroundAtmosphere = true; // [ Bool , 是否关闭大气效果 ]
            //viewer.scene.globe.depthTestAgainstTerrain = false; // [ Bool , 地面以下不可见（高程遮挡） ]
            viewer._cesiumWidget._creditContainer.style.display = "none"; // [ String , 隐藏logo ]

            //是否开启抗锯齿
            viewer.scene.fxaa = true;
            viewer.scene.postProcessStages.fxaa.enabled = true;
            // 开启地形深度检测
            viewer.scene.globe.depthTestAgainstTerrain = true;

            terrainProviderDefault = viewer.terrainProvider;
            // 测量
            measure = new Cesium.Measure(viewer);
            // 天气
            weather = new Weather(viewer);
            // 自由视角
            // flyMode = new FlyMode(viewer);
            // 通视分析
            visibilityAnalysis = new Cesium.VisibilityAnalysis(viewer);
            // 天际线分析
            skylineAnalysis = new Cesium.SkylineAnalysis(viewer);
            // 阴影分析
            shadowAnalysis = new Cesium.ShadowAnalysis(viewer);
            // 挖填方分析
            excavationAnalysis = new Cesium.ExcavationAnalysis(viewer);
            // 地形剖面
            clippingAnalysis = new Cesium.ClippingAnalysis(viewer);
            // 物联网设备
            ioTDevice = new Cesium.IoT(viewer);
            // 地球自转开启
            this.earthRotate(true);
            this.searchClick();
        },
        getJsonData() {
            doApi(res => {
                if (res.data) {
                    this.data4 = res.data;
                    this.data4.map(item => {
                        if (item.children) {
                            item.children.map(this.analysisData);
                        } else {
                            this.isDataFile(item);
                        }
                    })
                }
            }, err => {
                console.log(err)
            }, null, `/FileDirOpt/getJsonByPath?fileName=${sys_layerName}`, 'post', null)
        },
        //判断根节点是否为数据文件
        isDataFile(data) {
            data.name = 'id' + data.id;
            let maximumLevel = data.maxLevel ? parseInt(data.maxLevel) : 23;
            let minimumLevel = data.minLevel ? parseInt(data.minLevel) : 0;
            let rectangle = data.west && data.east && data.north && data.south ? Cesium.Rectangle.fromDegrees(
                data.west, data.south, data.east, data.north) : undefined;
            if (data.type == 'DEM') {
                data.url = handleUrl_DemDom(data.url);
                let terrainProvider = new Cesium.CesiumTerrainProvider({
                    url: data.url,
                });
                tilesetObj[data.name] = terrainProvider;
                tilesetObj[data.name].show = data.checked;
                tilesetObj[data.name].type = data.type;
                if (data.checked == true) {
                    viewer.terrainProvider = tilesetObj[data.name];
                    curTerrain = data;
                }
            } else if (data.type == 'DOM') {
                data.url = handleUrl_DemDom(data.url);
                let imgProvider = viewer.imageryLayers.addImageryProvider(new Cesium.UrlTemplateImageryProvider({
                    url: data.url + '{z}/{x}/{y}.png',
                    maximumLevel: maximumLevel, // 天地图的最大缩放级别
                    minimumLevel: minimumLevel,
                    rectangle: rectangle,
                    fileExtension: "png"
                }));
                tilesetObj[data.name] = imgProvider;
                tilesetObj[data.name].show = data.checked;
                tilesetObj[data.name].type = data.type;
            } else if (data.type == '3DTiles') {
                let m = undefined;
                // m = Cesium.Matrix4.fromArray([
                //     1.0, 0.0, 0.0, 0.0,
                //     0.0, 1.0, 0.0, 0.0,
                //     0.0, 0.0, 1.0, 0.0,
                //     0.0, data.offsetHeight, 0.0, 1.0
                // ]);
                let tiles = new Cesium.Cesium3DTileset({
                    url: data.url,
                    //maximumScreenSpaceError: 2,        //最大的屏幕空间误差
                    //maximumNumberOfLoadedTiles: 1000,  //最大加载瓦片个数
                    // modelMatrix: m //形状矩阵
                });
                tiles.offsetHeight = data.offsetHeight;
                tiles.readyPromise.then((model) => {
                    if (model.offsetHeight) {
                        let offsetHeight = Number(model.offsetHeight);
                        if (isNaN(offsetHeight)) {
                            return;
                        }
                        var cartographic = Cesium.Cartographic.fromCartesian(model.boundingSphere.center);
                        var surface = Cesium.Cartesian3.fromRadians(cartographic.longitude, cartographic.latitude, cartographic.height);
                        var offset = Cesium.Cartesian3.fromRadians(cartographic.longitude, cartographic.latitude, cartographic.height + offsetHeight);
                        var translation = Cesium.Cartesian3.subtract(offset, surface, new Cesium.Cartesian3());
                        model.modelMatrix = Cesium.Matrix4.fromTranslation(translation);
                    }
                });
                let tileset = viewer.scene.primitives.add(tiles);
                tilesetObj[data.name] = tileset;
                tilesetObj[data.name].show = data.checked;
                tilesetObj[data.name].type = data.type;
            } else if (data.type == 'XYZ服务') {
                let subdomains = JSON.parse(data.subdomains);
                let xyz = viewer.imageryLayers.addImageryProvider(new Cesium.UrlTemplateImageryProvider({
                    url: data.url,
                    maximumLevel: maximumLevel, // 天地图的最大缩放级别
                    minimumLevel: minimumLevel,
                    subdomains: subdomains
                }));
                tilesetObj[data.name] = xyz;
                tilesetObj[data.name].show = data.checked;
                tilesetObj[data.name].type = data.type;
            } else if (data.type == 'GLTF') {
                let lon = Number(data.lon);
                let lat = Number(data.lat);
                let entity = viewer.entities.add({
                    position: Cesium.Cartesian3.fromDegrees(lon, lat),
                    model: {
                        uri: data.url
                    }
                });
                tilesetObj[data.name] = entity;
                tilesetObj[data.name].show = data.checked;
                tilesetObj[data.name].type = data.type;
            }
        },
        //获取的数据判断是否有children递归函数
        analysisData(data) {
            if (data.children) {
                data.children.map(this.analysisData);
            } else {
                this.isDataFile(data);
            }
        },
        //使用viewer定位到模型位置
        flyToTileSet(tileset) {
            var flyPromise = viewer.flyTo(tileset, {
                duration: 2, //持续时间
                offset: new Cesium.HeadingPitchRange(0.0, Cesium.Math.toRadians(-90))
            });
            flyPromise.then(function () {
                var rotatePromise = viewer.flyTo(tileset, {
                    duration: 1, //持续时间
                    offset: new Cesium.HeadingPitchRange(0.0, Cesium.Math.toRadians(-20))
                });
                rotatePromise.then(function () {
                    this.setPitch();
                });
            });
        },
        //相机有倾斜角的时候，距离会变远,拉近相机距离
        setPitch() {
            var center = Cesium.Rectangle.center(rect);
            var car = Cesium.Cartesian3.fromRadians(center.longitude, center.latitude);
            var range = Cesium.Cartesian3.distance(car, viewer.camera.position) * Math.cos(20);
            viewer.zoomTo(loactionTectEntity, new Cesium.HeadingPitchRange(0.0, Cesium.Math.toRadians(-20.0), range));
        },
        // 地球飞入方法
        flyToFun(long, lat, height, bool) {
            let destination;
            if (bool) {
                destination = new Cesium.Cartesian3(long, lat, height)
            } else {
                destination = Cesium.Cartesian3.fromDegrees(long, lat, height)
            }
            viewer.scene.camera.flyTo({
                destination: destination,
            })
        },
        // 数据初始化
        async dataInit() {
            setTimeout(() => {
                loading.close()
            }, 100)
        },
        // 开始挖填方
        getExcavation() {
            //是否贴地
            if (!this.deleteExcavation) {
                this.clearExcavation();
            }
            excavationAnalysis.drawExcavationGraphics({
                cuttingDepth: this.cuttingDepth,
                clampToGround: true,
                callback: (item) => {
                    this.excavationArea = item.excavationArea;
                    this.excavationResult = item.excavationResult;
                    this.excavationFillResult = item.excavationFillResult;
                }
            });
            this.deleteExcavation = false

        },
        // 清除挖填方
        clearExcavation() {
            excavationAnalysis._drawLayer.entities.removeAll();
            if (viewer.scene.globe.clippingPlanes) {
                excavationAnalysis.clear();
            }
            this.excavationArea = '';
            this.excavationResult = '';
            this.excavationFillResult = '';
            this.deleteExcavation = true;
        },
        // 物联网分页
        changeIoTPage(index) {
            if ((index - 1) * this.ioTPageSize < this.ioTTotal) {
                this.ioTCurrent = index;
                this.ioTData = [];
                for (let i = (index - 1) * this.ioTPageSize; i < this.ioTTotal && i < index * this.ioTPageSize; i++) {
                    this.ioTData.push(this.ioTDatas[i]);
                }
            }
        },
        // 跳转到设备点
        flyToDevice(item) {
            if (item.info.longitude && item.info.latitude) {
                if (this.flvPlayer) {
                    // 计时器解决连续操作的bug
                    setTimeout(function () {
                        this.flvPlayer.pause();
                        this.flvPlayer.unload();
                        this.flvPlayer.detachMediaElement();
                        this.flvPlayer.destroy();
                        this.flvPlayer = null;
                    }, 1000);
                }
                viewer.camera.flyTo({
                    destination: Cesium.Cartesian3.fromDegrees(item.info.longitude, item.info.latitude, 1500),
                })
                ioTDevice.closePopUp();
                ioTDevice.videoClosePopUp();
                ioTDevice.setCurItem({
                    curItem: item
                });
                ioTDevice.showSensorData();
                // this.currentFacId = item.facId;
                this.vehicleSensorBool = true;
                this.sensorData = [];
                this.sensorTotal = 0;
                this.sensorCurrent = 1;
                ioTDevice.getVideo({
                    ioTData: item,
                    callback: (res) => {
                        this.sensorDatas = res.data.rows;
                        this.sensorTotal = res.data.total;
                        this.changeSensorPage(1);
                        this.sensorDatas.forEach((ele) => {
                            ele.position = item.position;
                        })
                    }
                });
            }
        },
        // 跳转到传感器点
        showVideo(item) {
            // $('#trackPopUp').hide();
            ioTDevice.closePopUp();
            ioTDevice.videoClosePopUp();
            ioTDevice._curVideo = item;
            ioTDevice.showSensorData();
        },
        // 传感器分页
        changeSensorPage(index) {
            this.sensorCurrent = index;
            this.sensorData = [];
            for (let i = (index - 1) * this.sensorPageSize; i < this.sensorTotal && i < index * this.sensorPageSize; i++) {
                this.sensorData.push(this.sensorDatas[i]);
            }
        },
    }
});


//点击时存在模型则点击事件获取模型高度，没有模型时获取地形高度

// viewer.screenSpaceEventHandler.setInputAction(function (clickEvent) {
//     var ray1 = viewer.camera.getPickRay(clickEvent.position);
//     var cartesian = viewer.scene.globe.pick(ray1, viewer.scene);
//     console.log('地形高度点', cartesian);
//     var pick = viewer.scene.pickPosition(clickEvent.position);
//     var pickModel = viewer.scene.pick(clickEvent.position);
//     if (pickModel && pick && !pickModel.id) {
//         var height = Cesium.Cartographic.fromCartesian(pick).height;
//         var lat = Cesium.Math.toDegrees(Cesium.Cartographic.fromCartesian(pick).latitude);
//         var lng = Cesium.Math.toDegrees(Cesium.Cartographic.fromCartesian(pick).longitude);
//         cartesian = Cesium.Cartesian3.fromDegrees(lng, lat, height);
//         console.log('模型高度点', cartesian);
//     }
// }, Cesium.ScreenSpaceEventType.LEFT_CLICK);


// <!-- 经纬度实时显示 -->
var longitude_show = document.getElementById('longitude_show');
var latitude_show = document.getElementById('latitude_show');
var altitude_show = document.getElementById('altitude_show');
var canvas = viewer.scene.canvas;
//具体事件的实现
var ellipsoid = viewer.scene.globe.ellipsoid;
var handler = new Cesium.ScreenSpaceEventHandler(canvas);
handler.setInputAction(function (movement) {
    //捕获椭球体，将笛卡尔二维平面坐标转为椭球体的笛卡尔三维坐标，返回球体表面的点
    var cartesian = viewer.camera.pickEllipsoid(movement.endPosition, ellipsoid);
    if (cartesian) {
        //将笛卡尔三维坐标转为地图坐标（弧度）
        var cartographic = viewer.scene.globe.ellipsoid.cartesianToCartographic(cartesian);
        //将地图坐标（弧度）转为十进制的度数
        var lat_String = Cesium.Math.toDegrees(cartographic.latitude).toFixed(3)
        var log_String = Cesium.Math.toDegrees(cartographic.longitude).toFixed(3)
        var alti_String = (viewer.camera.positionCartographic.height / 1000).toFixed(2);
        longitude_show.innerHTML = log_String;
        latitude_show.innerHTML = lat_String;
        altitude_show.innerHTML = alti_String;
        // console.log(log_String, lat_String, alti_String)
    }
}, Cesium.ScreenSpaceEventType.MOUSE_MOVE);
