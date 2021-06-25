const earthInt = {
  // 进入地图事件
  clickAccess(vm) {
    //设置时间
    vm.dataSet();
    setTimeout(() => {
      // 地球自转结束
      vm.earthRotate(false);
      // 关闭初始化弹层
      vm.initBool = false;
      // map飞入定位
      vm.access()
      // 显示nav 导航栏
      setTimeout(() => {
        //head头部显示隐藏
        vm.headBool = true

      }, 1500);
      setTimeout(() => {
        vm.selBool = true
      }, 3000)
      setTimeout(() => {
        //下侧导航栏
        vm.navNewBool = true;
        //左侧图表
        vm.initEchartsBool = true;
      }, 4000)
    }, 500)
  },
  stratPlay(viewer, stopTime) {
    //alert("开始")
    if (viewer.clock.shouldAnimate = !0, stopTime)
      viewer.clock.currentTime = stopTime;
    else {
      var e = "2019-06-18",
        t = new Date(),
        i = "10",
        a = "18",
        r = new Date(new Date(t).setHours(Number(i))),
        o = new Date(new Date(t).setHours(Number(a)));
      // viewer.scene.globe.enableLighting = !0,
        viewer.shadows = !0,
        viewer.clock.startTime = Cesium.JulianDate.fromDate(r),
        viewer.clock.currentTime = Cesium.JulianDate.fromDate(r),
        viewer.clock.stopTime = Cesium.JulianDate.fromDate(o),
        viewer.clock.clockRange = Cesium.ClockRange.LOOP_STOP,
        viewer.clock.clockStep = Cesium.ClockStep.SYSTEM_CLOCK_MULTIPLIER,
        viewer.clock.multiplier = 1600
    }
  },

  pausePlay(viewer, stopTime) {
    //alert("停止")
    viewer.clock.shouldAnimate = !1
  },

  stopPlay(viewer, stopTime) {
    //alert("停止")
    viewer.clock.shouldAnimate = !1
  },

  //测量空间直线距离
  measureLineSpace(viewer, handler) {
    var PolyLinePrimitive = (function () {
      function _(positions) {
        this.options = {
          name: '直线',
          polyline: {
            show: true,
            positions: [],
            material: Cesium.Color.CHARTREUSE,
            width: 2
          }
        };
        this.positions = positions;
        this._init();
      }

      _.prototype._init = function () {
        var _self = this;
        var _update = function () {
          return _self.positions;
        };
        //实时更新polyline.positions
        this.options.polyline.positions = new Cesium.CallbackProperty(_update, false);
        viewer.entities.add(this.options);
      };

      return _;
    })();

    var handler = createScreenSpaceEventHandler(viewer.scene.canvas);
    var positions = [];
    var poly = null;
    var tooltip = document.getElementById("toolTip");
    var distance = 0;
    var cartesian = null;
    var floatingPoint;
    tooltip.style.display = "block";
    //监听移动事件
    handler.setInputAction(function (movement) {
      tooltip.style.left = movement.endPosition.x + 440 + "px";
      tooltip.style.top = movement.endPosition.y + 30 + "px";
      tooltip.innerHTML = '<p>单击开始，双击结束</p>';
      //移动结束位置
      cartesian = viewer.scene.pickPosition(movement.endPosition);
      //判断点是否在画布上
      if (Cesium.defined(cartesian)) {
        if (positions.length >= 2) {
          if (!Cesium.defined(poly)) {
            //画线
            poly = new PolyLinePrimitive(positions);
          } else {
            positions.pop();
            // cartesian.y += (1 + Math.random());
            positions.push(cartesian);
          }
          distance = getSpaceDistance(positions);
        }
      }

    }, Cesium.ScreenSpaceEventType.MOUSE_MOVE);
    //监听单击事件
    handler.setInputAction(function (movement) {
      tooltip.style.display = "none";
      cartesian = viewer.scene.pickPosition(movement.position);

      if (Cesium.defined(cartesian)) {
        if (positions.length == 0) {
          positions.push(cartesian.clone());
        }
        positions.push(cartesian);
        //在三维场景中添加Label
        var textDisance = distance + "米";
        floatingPoint = viewer.entities.add({
          name: '空间直线距离',
          position: positions[positions.length - 1],
          point: {
            pixelSize: 5,
            color: Cesium.Color.RED,
            outlineColor: Cesium.Color.WHITE,
            outlineWidth: 2,
            heightReference: Cesium.HeightReference.NONE
          },
          label: {
            text: textDisance,
            font: '18px sans-serif',
            fillColor: Cesium.Color.GOLD,
            style: Cesium.LabelStyle.FILL_AND_OUTLINE,
            outlineWidth: 2,
            verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
            pixelOffset: new Cesium.Cartesian2(20, -20),
            heightReference: Cesium.HeightReference.NONE
          }
        });
      }

    }, Cesium.ScreenSpaceEventType.LEFT_CLICK);
    //监听双击事件
    handler.setInputAction(function (movement) {
      destroyScreenSpaceEventHandler(); //关闭事件句柄
      positions.pop(); //最后一个点无效
    }, Cesium.ScreenSpaceEventType.LEFT_DOUBLE_CLICK);
  },
  //空间两点距离计算函数
  getSpaceDistance(positions) {
    var distance = 0;
    for (var i = 0; i < positions.length - 1; i++) {

      var point1cartographic = Cesium.Cartographic.fromCartesian(positions[i]);
      var point2cartographic = Cesium.Cartographic.fromCartesian(positions[i + 1]);
      /**根据经纬度计算出距离**/
      var geodesic = new Cesium.EllipsoidGeodesic();
      geodesic.setEndPoints(point1cartographic, point2cartographic);
      var s = geodesic.surfaceDistance;
      //console.log(Math.sqrt(Math.pow(distance, 2) + Math.pow(endheight, 2)));
      //返回两点之间的距离
      s = Math.sqrt(Math.pow(s, 2) + Math.pow(point2cartographic.height - point1cartographic.height, 2));
      distance = distance + s;
    }
    return distance.toFixed(2);
  },
}

export default earthInt
