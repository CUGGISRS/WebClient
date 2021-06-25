const flightPath = {
  //保存飞行项
  saveFlyList(vm) {
    let flyValue = {
      name: vm.flyValue
    };
    doApi(
      (res) => {
        if (res.status == 200) {
          vm.$Message.success('新增成功！');
          vm.modal3 = false;
          vm.flyValue = '';
          vm.getFlyTrackList();
        }
      },
      function (err) {
        console.log(err);
      },
      null,
      '/FlyPath',
      'post',
      JSON.stringify(flyValue),
      "application/json"
    );
  },
  //分页获取飞行路径列表
  getFlyTrackList(vm) {
    var params = {
      page: vm.flyLimit,
      limit: vm.flyPageSize
    }
    doApi(
      (res) => {
        if (res.status == 200) {
          vm.flyTrackList = res.data.rows;
          vm.flyTotal = res.data.total;
        }
      },
      function (err) {
        console.log(err);
      },
      null,
      '/FlyPath/pageLink',
      'get',
      params,
      null
    );
  },
  //删除飞行轨迹
  delFlyTrack(vm, id, viewer) {
    vm.$Modal.confirm({
      title: '删除',
      content: '确认删除飞行路径吗？',
      onOk: () => {
        doApi(
          (res) => {
            console.log(res)
            if (res.status == 200) {
              vm.$Message.success('删除成功！');
              viewer.entities.removeAll();
              vm.access();
              vm.getFlyTrackList();
            }
          },
          function (err) {
            console.log(err);
          },
          null,
          '/FlyPath/' + id,
          'delete',
          null,
          null
        );
      },
      onCancel: () => {
        vm.$Message.info('已取消删除！');
      }
    });
  },
  //退出飞行
  exitFly(vm, viewer) {
    const {
      Exection,
      marks,
      pitchValue,
    } = vm
    viewer.clock.onTick.removeEventListener(Exection);
    // 相机看点的角度，如果大于0那么则是从地底往上看，所以要为负值
    const pitch = Cesium.Math.toRadians(pitchValue);
    const marksIndex = 1;
    let preIndex = marksIndex - 1;
    //计算俯仰角
    let heading = vm.bearing(marks[preIndex].latitude, marks[preIndex].longitude, marks[marksIndex].latitude, marks[marksIndex].longitude);
    heading = Cesium.Math.toRadians(heading);
    // const endPosition = Cesium.Cartesian3.fromDegrees(
    //   marks[0].longitude,
    //   marks[0].latitude,
    //   marks[0].height
    // );
    // viewer.scene.camera.setView({
    //   destination: endPosition,
    //   orientation: {
    //     heading: heading,
    //     pitch: pitch,
    //   }
    // });
    // 进入地球方法
    vm.access();
    vm.flying = false;
  },
  //点击飞行轨迹
  handleFlyTrack(item, index, vm, viewer) {
    vm.isActive = index;
    if (vm.flying) {
      vm.exitFly();
    }
    viewer.entities.removeAll();
    vm.trackWayId = item.id;

    if (item.flyPathPoints.length > 0) {
      vm.marks = item.flyPathPoints[0].flyPathPointSingles;
      let positionData = []
      vm.marks.map(item => {
        let destination = Cesium.Cartesian3.fromDegrees(item.longitude, item.latitude, item.height);
        positionData.push(destination);
        vm.createPoint(destination);
      })

      vm.drawShape(positionData);
      vm.initFly();

    }
  },
  //沿轨迹飞行
  flyExtent(vm, viewer) {
    const {
      marks,
      flytime,
      pitchValue
    } = vm;
    // 相机看点的角度，如果大于0那么则是从地底往上看，所以要为负值
    const pitch = Cesium.Math.toRadians(pitchValue);
    // 时间间隔2秒钟
    vm.setExtentTime(flytime);
    vm.Exection = function TimeExecution() {
      let preIndex = vm.marksIndex - 1;
      if (vm.marksIndex == 0) {
        preIndex = marks.length - 1;
      }
      //计算俯仰角
      let heading = vm.bearing(marks[preIndex].latitude, marks[preIndex].longitude, marks[vm.marksIndex].latitude, marks[vm.marksIndex].longitude);
      heading = Cesium.Math.toRadians(heading);
      // 当前已经过去的时间，单位s
      const delTime = Cesium.JulianDate.secondsDifference(viewer.clock.currentTime, viewer.clock.startTime);
      const originLat = vm.marksIndex == 0 ? marks[marks.length - 1].latitude : marks[vm.marksIndex - 1].latitude;
      const originLng = vm.marksIndex == 0 ? marks[marks.length - 1].longitude : marks[vm.marksIndex - 1].longitude;
      const endPosition = Cesium.Cartesian3.fromDegrees(
        (originLng + (marks[vm.marksIndex].longitude - originLng) / flytime * delTime),
        (originLat + (marks[vm.marksIndex].latitude - originLat) / flytime * delTime),
        marks[vm.marksIndex].height
      );
      viewer.scene.camera.setView({
        destination: endPosition,
        orientation: {
          heading: heading,
          pitch: pitch,
        }
      });
      if (Cesium.JulianDate.compare(viewer.clock.currentTime, viewer.clock.stopTime) >= 0) {
        viewer.clock.onTick.removeEventListener(vm.Exection);
        //有个转向的功能
        vm.changeCameraHeading();
      }
    };
    viewer.clock.onTick.addEventListener(vm.Exection);
  },
  // 相机原地定点转向
  changeCameraHeading(vm, viewer) {
    const {
      marks,
      pitchValue,
      changeCameraTime
    } = vm
    let {
      marksIndex
    } = vm

    let nextIndex = vm.marksIndex + 1;
    if (marksIndex == marks.length - 1) {
      nextIndex = 0;
    }
    // 计算两点之间的方向
    const heading = vm.bearing(marks[marksIndex].latitude, marks[marksIndex].longitude, marks[nextIndex].latitude, marks[nextIndex].longitude);
    // 相机看点的角度，如果大于0那么则是从地底往上看，所以要为负值
    const pitch = Cesium.Math.toRadians(pitchValue);
    // 给定飞行一周所需时间，比如10s, 那么每秒转动度数
    const angle = (heading - Cesium.Math.toDegrees(viewer.camera.heading)) / changeCameraTime;
    // 时间间隔2秒钟
    vm.setExtentTime(changeCameraTime);
    // 相机的当前heading
    const initialHeading = viewer.camera.heading;
    vm.Exection = function TimeExecution() {
      // 当前已经过去的时间，单位s
      const delTime = Cesium.JulianDate.secondsDifference(viewer.clock.currentTime, viewer.clock.startTime);
      const heading = Cesium.Math.toRadians(delTime * angle) + initialHeading;
      viewer.scene.camera.setView({
        orientation: {
          heading: heading,
          pitch: pitch,
        }
      });

      if (Cesium.JulianDate.compare(viewer.clock.currentTime, viewer.clock.stopTime) >= 0) {
        viewer.clock.onTick.removeEventListener(vm.Exection);
        vm.marksIndex = ++vm.marksIndex >= marks.length ? 0 : vm.marksIndex;
        if (vm.marksIndex != 0) {
          vm.flyExtent();
        }
      }
    };
    viewer.clock.onTick.addEventListener(vm.Exection);
  },
  // 设置飞行的时间到viewer的时钟里
  setExtentTime(time, viewer) {
    const startTime = Cesium.JulianDate.fromDate(new Date());
    const stopTime = Cesium.JulianDate.addSeconds(startTime, time, new Cesium.JulianDate());
    viewer.clock.startTime = startTime.clone(); // 开始时间
    viewer.clock.stopTime = stopTime.clone(); // 结速时间
    viewer.clock.currentTime = startTime.clone(); // 当前时间
    viewer.clock.clockRange = Cesium.ClockRange.CLAMPED; // 行为方式-达到终止时间后停止
    viewer.clock.clockStep = Cesium.ClockStep.SYSTEM_CLOCK; // 时钟设置为当前系统时间; 忽略所有其他设置。
  },

  // 飞行时 camera的方向调整(heading) 结束 
  // 绘制路线 
  drawLineRoad(id, vm, viewer, handler) {
    const {
      activeShapePoints
    } = vm
    //鼠标左键
    handler.setInputAction(function (event) {
      var earthPosition = viewer.scene.pickPosition(event.position);
      if (Cesium.defined(earthPosition)) {
        if (activeShapePoints.length === 0) {
          vm.floatingPoint = vm.createPoint(earthPosition);
          activeShapePoints.push(earthPosition);
          var dynamicPositions = new Cesium.CallbackProperty(function () {
            return activeShapePoints;
          }, false);
          vm.activeShape = vm.drawShape(dynamicPositions); //绘制动态图
        }
        activeShapePoints.push(earthPosition);
        vm.createPoint(earthPosition);
      }
    }, Cesium.ScreenSpaceEventType.LEFT_CLICK);
    //鼠标移动
    handler.setInputAction(function (event) {
      if (Cesium.defined(vm.floatingPoint)) {
        var newPosition = viewer.scene.pickPosition(event.endPosition);
        if (Cesium.defined(newPosition)) {
          vm.floatingPoint.position.setValue(newPosition);
          activeShapePoints.pop();
          activeShapePoints.push(newPosition);
        }
      }
    }, Cesium.ScreenSpaceEventType.MOUSE_MOVE);
    handler.setInputAction(function () {
      vm.terminateShape(id);
      handler.removeInputAction(Cesium.ScreenSpaceEventType.LEFT_CLICK) //移除事件
    }, Cesium.ScreenSpaceEventType.RIGHT_CLICK);
  },
  // 重新绘制形状，使它不是动态的，并删除动态形状。
  terminateShape(id, vm, viewer) {
    const {
      activeShapePoints,
      height
    } = vm
    activeShapePoints.pop(); //去除最后一个动态点
    if (activeShapePoints.length) {
      vm.marks = [];
      for (const position of activeShapePoints) {
        const latitude = vm.toDegrees(Cesium.Cartographic.fromCartesian(position).latitude);
        const longitude = vm.toDegrees(Cesium.Cartographic.fromCartesian(position).longitude);
        vm.marks.push({
          latitude: latitude,
          longitude: longitude,
          height
        })
      }
      let params = {
        flyPathId: id,
        flyPathPointSingles: vm.marks,
        height
      }
      doApi(res => {
          console.log(res)
          if (res.status === 200) {
            flightPath.getFlyTrackList(vm);
          }
        }, err => {
          console.log(err)
        },
        null,
        '/FlyPathPoint/addMaySingles',
        'post',
        JSON.stringify(params),
        "application/json", )
      vm.drawShape(activeShapePoints); //绘制最终图
    }
    viewer.entities.remove(vm.floatingPoint); //去除动态点图形（当前鼠标点）
    viewer.entities.remove(vm.activeShape); //去除动态图形
    vm.floatingPoint = undefined;
    vm.activeShape = undefined;
    vm.activeShapePoints = [];
  },
}

export default flightPath