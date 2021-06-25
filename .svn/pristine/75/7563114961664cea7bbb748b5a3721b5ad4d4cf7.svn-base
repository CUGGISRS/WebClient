const fire = {
  fireSpecial(viewer, longitude, latitude) {
    var entity = viewer.entities.add({
      // 加载飞机模型
      // model: {
      //   uri: './lib/Cesium/SampleData/models/CesiumAir/Cesium_Air.glb',
      //   minimumPixelSize: 64
      // },
      position: Cesium.Cartesian3.fromDegrees(longitude, latitude, 0.0),
    });
    // viewer.trackedEntity = entity;

    function computeModelMatrix(entity, time) {
      var position = Cesium.Property.getValueOrUndefined(entity.position, time, new Cesium.Cartesian3());
      if (!Cesium.defined(position)) {
        return undefined;
      }
      var orientation = Cesium.Property.getValueOrUndefined(entity.orientation, time, new Cesium.Quaternion());
      if (!Cesium.defined(orientation)) {
        var modelMatrix = Cesium.Transforms.eastNorthUpToFixedFrame(position, undefined, new Cesium.Matrix4());
      } else {
        modelMatrix = Cesium.Matrix4.fromRotationTranslation(Cesium.Matrix3.fromQuaternion(orientation, new Cesium.Matrix3()), position, new Cesium.Matrix4());
      }
      return modelMatrix;
    }

    function computeEmitterModelMatrix() {
      var hpr = Cesium.HeadingPitchRoll.fromDegrees(0.0, 0.0, 0.0, new Cesium.HeadingPitchRoll());
      var trs = new Cesium.TranslationRotationScale();
      trs.translation = Cesium.Cartesian3.fromElements(2.5, 4.0, 1.0, new Cesium.Cartesian3());
      trs.rotation = Cesium.Quaternion.fromHeadingPitchRoll(hpr, new Cesium.Quaternion());
      return Cesium.Matrix4.fromTranslationRotationScale(trs, new Cesium.Matrix4());
    }
    var particleSystem = viewer.scene.primitives.add(new Cesium.ParticleSystem({
      // Particle appearance
      image: './lib/Cesium/SampleData/fire.png',
      startScale: 1.0,
      endScale: 4.0,
      particleLife: 1.0,
      speed: 5.0,
      imageSize: new Cesium.Cartesian2(20, 20),
      emissionRate: 5.0,
      lifetime: 16.0,

      modelMatrix: computeModelMatrix(entity, Cesium.JulianDate.now()),
      emitterModelMatrix: computeEmitterModelMatrix(),
    }));
  },
  addFireSpecial(viewer, handler) {
    //定义canvas屏幕点击事件
    // var handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
    //注册鼠标事件,event参数是点击的地方是在哪里
    handler.setInputAction((event) => {
      //定义一个屏幕点击的事件，pickPosition封装的是获取点击的位置的坐标
      var position = viewer.scene.pickPosition(event.position);
      //输出之后我们发现如前言所说的坐标都是笛卡尔坐标，所以我们需要转换笛卡尔坐标

      //将笛卡尔坐标转化为弧度坐标
      var cartographic = Cesium.Cartographic.fromCartesian(position);
      //将弧度坐标转换为经纬度坐标
      var longitude = Cesium.Math.toDegrees(cartographic.longitude); //经度
      var latitude = Cesium.Math.toDegrees(cartographic.latitude); //纬度
      var height = cartographic.height; //高度
      console.log("经纬度：" + longitude, latitude, height);
      this.fireSpecial(viewer, longitude, latitude);
    }, Cesium.ScreenSpaceEventType.LEFT_CLICK);
    handler.setInputAction(() => {
      handler.removeInputAction(Cesium.ScreenSpaceEventType.LEFT_CLICK) //移除事件
    }, Cesium.ScreenSpaceEventType.RIGHT_CLICK);
  },
  removeFireSpecial(viewer) {
    var handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
    handler.setInputAction((movement) => {
      var pick = viewer.scene.pickPosition(movement.position);
      console.log(pick)
    }, Cesium.ScreenSpaceEventType.LEFT_CLICK)
  }
}

export default fire;