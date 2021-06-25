/**
 * 阴影分析
 */
if (typeof Cesium !== 'undefined') {
  /**
  * @param viewer  {object} 三维对象
  * @param options {object} 初始化参数
  * @constructor
  */
  Cesium.ShadowAnalysis = (function (Cesium) {
    /**
     * 绘制对象
     * @param viewer
     * @param options
     * @constructor
     */
    function _(viewer) {
      if (viewer && viewer instanceof Cesium.Viewer) {
        this._drawLayer = new Cesium.CustomDataSource('ShadowAnalysis');
        viewer && viewer.dataSources.add(this._drawLayer);
        this._viewer = viewer;
      }
    }
    _.prototype = {
      /**
       * 阴影分析
       * @param {*} options
       */
      drawShadowGraphics: function (options = {}) {
        if (this._viewer && options) {
          let $this = this,
            _handlers = createScreenSpaceEventHandler(this._viewer.scene.canvas);
          // left
          _handlers.setInputAction(function (movement) {
            positions = [];
            let cartesian = $this._viewer.scene.pickPosition(movement.position);
            if (cartesian && cartesian.x) {
              // 清除上一个点
              $this._drawLayer.entities.removeAll();
              _drawCircle(cartesian);
              // $this._viewer.scene.globe.enableLighting = true;
              // $this._viewer.shadows = true
            }
          }, Cesium.ScreenSpaceEventType.LEFT_CLICK);
          // right
          _handlers.setInputAction(function (movement) {
            destroyScreenSpaceEventHandler();
          }, Cesium.ScreenSpaceEventType.RIGHT_CLICK);
          // 绘制线
          function _drawCircle(cartesian) {
            $this._drawLayer.entities.add({
              position: cartesian,
              name: 'Red ellipse on surface with outline',
              ellipse: {
                semiMinorAxis: 100.0,
                semiMajorAxis: 100.0,
                height: 50.0,
                material: Cesium.Color.RED.withAlpha(0.1),
                outline: true,
                outlineColor: Cesium.Color.YELLOW
              }
            })
          };
        }
      }
    }
    return _
  })(Cesium);
}
