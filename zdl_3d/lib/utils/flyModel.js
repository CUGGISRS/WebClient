class FlyMode {
    static _flags = {
        looking: false,
        moveForward: false,
        moveBackward: false,
        moveUp: false,
        moveDown: false,
        moveLeft: false,
        moveRight: false,
        lookDown: false,
        lookUp: false,
        lookRight: false,
        lookLeft: false,
        twistLeft: false,
        twistRight: false,
    };
    static _viewer = null;
    static _camera = null;
    static _canvas = null;
    static _scene = null;
    static _ellipsoid = null;
    static _isFirstViewFlag = false;

    constructor(viewer) {
        FlyMode._viewer = viewer;
        FlyMode._camera = viewer.camera;
        FlyMode._canvas = viewer.canvas;
        FlyMode._scene = viewer.scene;
        FlyMode._ellipsoid = viewer.scene.globe.ellipsoid;
    }

    /*  
     **  第一人称
     **  isFirstViewFlag : true 开启 , false 关闭
     **
     **  操作指南:
     **  W               相机前进
     **  A               相机后退
     **  S               相机左移
     **  D               相机右移
     **  Q               相机上升
     **  E               相机下降
     **  ↑               相机视角向上
     **  ↓               相机视角向下
     **  ←               相机视角向左
     **  →               相机视角向右
     **  shift + ←       相机视角向左旋转
     **  shift + →       相机视角向右旋转
     */


    firstViewMode(flag) {
        FlyMode._isFirstViewFlag = flag;
        this.firstView();
    }

    firstView() {
        if (FlyMode._isFirstViewFlag) {
            FlyMode._scene.screenSpaceCameraController.enableRotate = false;
            FlyMode._scene.screenSpaceCameraController.enableZoom = false;
            FlyMode._scene.screenSpaceCameraController.enableTilt = false;
            FlyMode._canvas.setAttribute("tabindex", "0"); // needed to put focus on the canvas
            FlyMode._canvas.focus();

            document.addEventListener("keydown", this.keyDownFun);
            document.addEventListener("keyup", this.keyUpFun);
            FlyMode._viewer.clock.onTick.addEventListener(this.howToFly);
        } else {
            FlyMode._scene.screenSpaceCameraController.enableRotate = true;
            FlyMode._scene.screenSpaceCameraController.enableZoom = true;
            FlyMode._scene.screenSpaceCameraController.enableTilt = true;
            document.removeEventListener("keydown", this.keyDownFun);
            document.removeEventListener("keyup", this.keyUpFun);
            FlyMode._viewer.clock.onTick.removeEventListener(this.howToFly);
        }
    }

    howToFly() {
        var cameraHeight = FlyMode._ellipsoid.cartesianToCartographic(FlyMode._camera.position).height;
        var moveRate = cameraHeight / 100.0;

        if (FlyMode._flags.moveForward) {
            FlyMode._camera.moveForward(moveRate);
        }
        if (FlyMode._flags.moveBackward) {
            FlyMode._camera.moveBackward(moveRate);
        }
        if (FlyMode._flags.moveUp) {
            FlyMode._camera.moveUp(moveRate);
        }
        if (FlyMode._flags.moveDown) {
            FlyMode._camera.moveDown(moveRate);
        }
        if (FlyMode._flags.moveLeft) {
            FlyMode._camera.moveLeft(moveRate);
        }
        if (FlyMode._flags.moveRight) {
            FlyMode._camera.moveRight(moveRate);
        }
        if (FlyMode._flags.lookLeft) {
            FlyMode._camera.lookLeft(0.01);
        }
        if (FlyMode._flags.lookRight) {
            FlyMode._camera.lookRight(0.01);
        }
        if (FlyMode._flags.lookDown) {
            FlyMode._camera.lookDown(0.01);
        }
        if (FlyMode._flags.lookUp) {
            FlyMode._camera.lookUp(0.01);
        }
        if (FlyMode._flags.twistLeft) {
            FlyMode._camera.twistLeft(0.01);
        }
        if (FlyMode._flags.twistRight) {
            FlyMode._camera.twistRight(0.01);
        }
    }


    keyDownFun(e) {
        var flagName = FlyMode.getFlagForKeyCode(e.keyCode);
        if (typeof flagName !== "undefined") {
            if (e.shiftKey && flagName === "lookLeft")
                FlyMode._flags["twistLeft"] = true;
            else if (e.shiftKey && flagName === "lookRight")
                FlyMode._flags["twistRight"] = true;
            else
                FlyMode._flags[flagName] = true;
        }
    }


    // 这里不监听shift , 确保旋转能正常停止
    keyUpFun(e) {
        var flagName = FlyMode.getFlagForKeyCode(e.keyCode);
        if (typeof flagName !== "undefined") {
            if (flagName === "lookLeft") {
                FlyMode._flags["twistLeft"] = false;
            }
            if (flagName === "lookRight") {
                FlyMode._flags["twistRight"] = false;
            }
            FlyMode._flags[flagName] = false;
        }
    }

    static getFlagForKeyCode(keyCode) {
        switch (keyCode) {
            case "W".charCodeAt(0):
                return "moveForward";
            case "S".charCodeAt(0):
                return "moveBackward";
            case "Q".charCodeAt(0):
                return "moveUp";
            case "E".charCodeAt(0):
                return "moveDown";
            case "D".charCodeAt(0):
                return "moveRight";
            case "A".charCodeAt(0):
                return "moveLeft";
            case 37:
                return "lookLeft";
            case 38:
                return "lookDown";
            case 39:
                return "lookRight";
            case 40:
                return "lookUp";
            default:
                return undefined;
        }
    }
}