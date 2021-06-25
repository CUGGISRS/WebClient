const watchAdmin = {
  //点击视点列表弹出左侧弹框
  watchTipsClickItem(name, vm) {
    //选中状态样式
    vm.navState = name;
    //左侧弹框弹出
    vm.watchBool = true;
    var params = {
      page: vm.dataCount,
      limit: vm.pageSize
    }
    doApi(
      (res) => {
        if (res.status == 200) {
          vm.watchList = res.data.rows;
          vm.total = res.data.total;
        }
      },
      function (err) {
        console.log(err);
      },
      null,
      '/ViewManage/pageByCondition',
      'get',
      params,
      null
    );
  },
  //保存视点项
  saveWatchList(vm, viewer) {
    let heading = viewer.camera.heading;
    let position = viewer.camera.positionCartographic;
    let pitch = viewer.camera.pitch;
    let roll = viewer.camera.roll;
    let watchValue = vm.watchValue;
    let params = {
      heading: heading,
      pitch: pitch,
      roll: roll,
      watchValue: watchValue,
      height: position.height,
      longitude: position.longitude,
      latitude: position.latitude
    }
    if (vm.watchValue == '') return vm.$Message.warning('请输入视点名称！');
    doApi(
      (res) => {
        if (res.status == 200) {
          vm.$Message.success('新增视点成功！');
          this.watchTipsClickItem('视点列表',vm);
          vm.modal2 = false;
          vm.watchValue = '';
        } else if (res.status == 500 && res.message == "观察值存在重复数据") {
          vm.$Message.warning('已有该视点名称，请重新输入视点名称！');
          vm.watchValue = '';
        } else {
          vm.$Message.warning('已有该视点数据，请重新选取视点！');
          vm.modal2 = false;
          vm.watchValue = '';
        }
      },
      function (err) {
        console.log(err);
      },
      null,
      '/ViewManage',
      'post',
      JSON.stringify(params),
      'application/json'
    );
  },
  //删除视点的一项
  delWatchPoint(id, vm) {
    vm.$Modal.confirm({
      title: '删除',
      content: '确认删除视点吗？',
      onOk: () => {
        doApi(
          (res) => {
            if (res.status == 200) {
              vm.$Message.success('删除视点成功！');
              this.watchTipsClickItem('视点列表',vm);
            }
          },
          function (err) {
            console.log(err);
          },
          null,
          '/ViewManage/' + id,
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
  //点击视点跳转
  handleWatchPoint(val, viewer) {
    // 视点列表
    viewer.camera.flyTo({
      // Cesium的坐标是以地心为原点，一向指向南美洲，一向指向亚洲，一向指向北极州
      // fromDegrees()方法，将经纬度和高程转换为世界坐标
      // destination: Cesium.Cartesian3.fromDegrees(val.lon, val.lat, val.alt * 1000),
      // duration: 3,
      destination: Cesium.Cartesian3.fromRadians(val.longitude, val.latitude, val.height),
      // destination: val.position,
      orientation: {
        // 指向
        heading: val.heading,
        // 视角
        pitch: val.pitch,
        roll: val.roll
      }
    });
    // 同理，想要标记某个位置和角度，下次直接进入，可以在选好的角度上按F12进入开发者工具
    // 输入viewer.camera.heading  viewer.camera.pitch  viewer.camera.position回车可以得到信息
  },
}

export default watchAdmin