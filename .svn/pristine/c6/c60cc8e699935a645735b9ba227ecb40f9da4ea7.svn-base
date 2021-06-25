const figure = {
  figureRoaming(viewer) {

    // 自由镜头or漫游
    var ellipsoid = viewer.scene.globe.ellipsoid;

    viewer.scene.screenSpaceCameraController.enableRotate = false;
    viewer.scene.screenSpaceCameraController.enableTranslate = false;
    viewer.scene.screenSpaceCameraController.enableZoom = false;
    viewer.scene.screenSpaceCameraController.enableTilt = false;
    viewer.scene.screenSpaceCameraController.enableLook = false;

    var startMousePosition;
    var mousePosition;
    var flags = {
      looking: false,
      moveForward: false,
      moveBackward: false,
      moveUp: false,
      moveDown: false,
      moveLeft: false,
      moveRight: false
    };

    viewer.screenSpaceEventHandler.setInputAction(function (movement) {
      flags.looking = true;
      mousePosition = startMousePosition = Cesium.Cartesian3.clone(movement.position);
    }, Cesium.ScreenSpaceEventType.LEFT_DOWN);

    viewer.screenSpaceEventHandler.setInputAction(function (movement) {
      mousePosition = movement.endPosition;
    }, Cesium.ScreenSpaceEventType.MOUSE_MOVE);

    viewer.screenSpaceEventHandler.setInputAction(function (position) {
      flags.looking = false;
    }, Cesium.ScreenSpaceEventType.LEFT_UP);

    function getFlagForKeyCode(keyCode) {
      switch (keyCode) {
        case 'W'.charCodeAt(0):
          return 'moveForward';
        case 'S'.charCodeAt(0):
          return 'moveBackward';
        // case 'Q'.charCodeAt(0):
        //   return 'moveUp';
        // case 'E'.charCodeAt(0):
        //   return 'moveDown';
        case 'D'.charCodeAt(0):
          return 'moveRight';
        case 'A'.charCodeAt(0):
          return 'moveLeft';
        default:
          return undefined;
      }
    }

    document.addEventListener('keydown', this.down = function (e) {
      var flagName = getFlagForKeyCode(e.keyCode);
      if (typeof flagName !== 'undefined') {
        flags[flagName] = true;
      }
    }, false);

    document.addEventListener('keyup', this.up = (e) => {
      var flagName = getFlagForKeyCode(e.keyCode);
      if (typeof flagName !== 'undefined') {
        flags[flagName] = false;
      }
    }, false);

    viewer.clock.onTick.addEventListener(function (clock) {
      var camera = viewer.camera;
      // 镜头旋转
      if (flags.looking) {
        var width = viewer.canvas.clientWidth;
        var height = viewer.canvas.clientHeight;

        var lookFactor = 0.06;
        var x = (mousePosition.x - startMousePosition.x) / width;
        var y = -(mousePosition.y - startMousePosition.y) / height;

        camera.setView({
          orientation: {
            heading: camera.heading + x * lookFactor,
            pitch: camera.pitch + y * lookFactor,
            roll: 0.0,
          }
        })
      }

      // 根据高度来决定镜头移动的速度
      var cameraHeight = ellipsoid.cartesianToCartographic(camera.position).height;
      var moveRate = cameraHeight / 100.0;

      if (flags.moveForward) {
        camera.moveForward(moveRate);
      }
      if (flags.moveBackward) {
        camera.moveBackward(moveRate);
      }
      // if (flags.moveUp) {
      //   camera.moveUp(moveRate);
      // }
      // if (flags.moveDown) {
      //   camera.moveDown(moveRate);
      // }
      if (flags.moveLeft) {
        camera.moveLeft(moveRate);
      }
      if (flags.moveRight) {
        camera.moveRight(moveRate);
      }
    })
  },
  // 退出漫游
  quitFigure(viewer) {
    viewer.scene.screenSpaceCameraController.enableRotate = true;
    viewer.scene.screenSpaceCameraController.enableTranslate = true;
    viewer.scene.screenSpaceCameraController.enableZoom = true;
    viewer.scene.screenSpaceCameraController.enableTilt = true;
    viewer.scene.screenSpaceCameraController.enableLook = true;
    viewer.screenSpaceEventHandler.removeInputAction(Cesium.ScreenSpaceEventType.LEFT_DOWN)
    viewer.screenSpaceEventHandler.removeInputAction(Cesium.ScreenSpaceEventType.MOUSE_MOVE)
    viewer.screenSpaceEventHandler.removeInputAction(Cesium.ScreenSpaceEventType.LEFT_UP)
    document.removeEventListener('keydown', this.down, false)
    document.removeEventListener('keyup', this.up, false)
  },
}

export default figure