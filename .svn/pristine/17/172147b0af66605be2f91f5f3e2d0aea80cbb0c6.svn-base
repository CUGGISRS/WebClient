/**
 * 挖填方分析
 */
if (typeof Cesium !== 'undefined') {
  /**
   * @param viewer  {object} 三维对象
   * @param options {object} 初始化参数
   * @constructor
   */
  Cesium.ExcavationAnalysis = (function (Cesium) {
    /**
     * 绘制对象
     * @param viewer
     * @param options
     * @constructor
     */
    function _(viewer) {
      if (viewer && viewer instanceof Cesium.Viewer) {
        this._drawLayer = new Cesium.CustomDataSource('ExcavationAnalysis');
        viewer && viewer.dataSources.add(this._drawLayer);
        this._viewer = viewer;
        this.ellipsoid = this._viewer.scene.globe.ellipsoid;
      }
    }
    _.prototype = {
      /**
       * 挖填方分析
       * @param {*} options
       */
      drawExcavationGraphics: function (options = {}) {
        if (this._viewer && options) {
          let activeShapePoints = [], // 所有点坐标
            activeShape, // 记录动态图
            floatingPoint, // 记录当前鼠标点
            allDraw = [], // 记录所有绘制元素
            $this = this,
            _handlers = createScreenSpaceEventHandler(this._viewer.scene.canvas);
          // left
          _handlers.setInputAction(function (event) {
            let ray = $this._viewer.camera.getPickRay(event.position);
            let cartesian = $this._viewer.scene.globe.pick(ray, $this._viewer.scene);
            if (Cesium.defined(cartesian)) {
              if (activeShapePoints.length === 0) {
                floatingPoint = _createPoint(cartesian);
                activeShapePoints.push(cartesian);
                let dynamicPositions = new Cesium.CallbackProperty(function () {
                  return new Cesium.PolygonHierarchy(activeShapePoints);
                }, false);
                //绘制动态图
                activeShape = _drawShape(dynamicPositions);
              }
              activeShapePoints.push(cartesian);
              _createPoint(cartesian);
            }
          }, Cesium.ScreenSpaceEventType.LEFT_CLICK);
          // move
          _handlers.setInputAction(function (event) {
            if (Cesium.defined(floatingPoint)) {
              let newPosition = $this._viewer.scene.pickPosition(event.endPosition);
              if (Cesium.defined(newPosition)) {
                floatingPoint.position.setValue(newPosition);
                activeShapePoints.pop();
                activeShapePoints.push(newPosition);
              }
            }
          }, Cesium.ScreenSpaceEventType.MOUSE_MOVE);
          // right
          _handlers.setInputAction(function () {
            destroyScreenSpaceEventHandler();
            //去除最后一个动态点
            activeShapePoints.pop();
            if (activeShapePoints.length > 2) {
              _terminateShape();
            }
          }, Cesium.ScreenSpaceEventType.RIGHT_CLICK);
          //添加坐标点
          function _createPoint(position) {
            let point = $this._drawLayer.entities.add({
              position: position,
              point: {
                pixelSize: 6,
                color: Cesium.Color.YELLOW
              }
            });
            allDraw.push(point);
            return point;
          };
          // 绘制多边形
          function _drawShape(positionData) {
            let shape = $this._drawLayer.entities.add({
              polygon: {
                hierarchy: positionData,
                material: new Cesium.ImageMaterialProperty({
                  image: "./images/earth.png"
                }),
              }
            });
            allDraw.push(shape);
            return shape;
          };
          // 最后图形
          function _terminateShape() {
            // if (activeShapePoints.length) {
            //   //绘制最终图
            //   _drawShape(new Cesium.PolygonHierarchy(activeShapePoints));
            // }
            //去除动态点图形（当前鼠标点）
            $this._drawLayer.entities.remove(floatingPoint);
            //去除动态图形
            $this._drawLayer.entities.remove(activeShape);
            floatingPoint = undefined;
            activeShape = undefined;
            // 挖填方
            _excavation();
          }

          function _excavation() {
            $this.terrainClipPlan = new Cesium.TerrainClipPlan($this._viewer, {
              height: options.cuttingDepth,
              // splitNum: 1,
              wallImg: "./images/excavate_side_min.jpg",
              bottomImg: "./images/excavate_bottom_min.jpg"
            })
            $this.terrainClipPlan.updateData(activeShapePoints);
            _volumeCalculation(activeShapePoints, $this.terrainClipPlan);
            activeShapePoints = [];
          }

          // 体积计算
          function _volumeCalculation(positions, terrainClipPlan) {
            let totalCutVolume = 0;
            let totalFillVolume = 0;
            // let tileAvailability = $this._viewer.terrainProvider.availability;
            // if (!tileAvailability) {
            //   alert("未获取到地形")
            //   return false;
            // }
            let minHeight = 9999999999;
            // 计算差值点
            let cartographicArray = [];
            for (let i = 0; i < positions.length; i++) {
              let cartographic = Cesium.Cartographic.fromCartesian(positions[i]);
              cartographicArray.push(cartographic);
            }
            Cesium.sampleTerrainMostDetailed($this._viewer.terrainProvider, cartographicArray);
            for (let i = 0; i < cartographicArray.length; i++) {
              let cartographic = cartographicArray[i];
              let height = cartographic.height - options.cuttingDepth;
              if (minHeight > height)
                minHeight = height;
            }

            // 挖填方体积计算
            let m_Cartographic0 = Cesium.Cartographic.fromCartesian(positions[0]);
            let n_Cartographic0 = m_Cartographic0.clone();
            n_Cartographic0.height += 10;
            for (let i = 1; i < cartographicArray.length - 1; i++) {
              let m_Cart0 = (cartographicArray[i]);
              let m_Cart1 = (cartographicArray[i + 1]);
              let j_length = $this.getInterPointsLength(m_Cart0, m_Cart1);
              let prev_m_Cartographic1 = m_Cart0;
              for (let j = 1; j < j_length; j++) {
                let m_Cartographic1 = new Cesium.Cartographic(
                  Cesium.Math.lerp(m_Cart0.longitude, m_Cart1.longitude, j / (j_length - 1)),
                  Cesium.Math.lerp(m_Cart0.latitude, m_Cart1.latitude, j / (j_length - 1))
                )
                let k_length = $this.getInterPointsLength(m_Cartographic0, m_Cartographic1);
                let terrainSamplePositions = [];
                for (let k = 0; k < k_length; k++) {
                  terrainSamplePositions.push(
                    new Cesium.Cartographic(
                      Cesium.Math.lerp(m_Cartographic0.longitude, m_Cartographic1.longitude, k / (k_length - 1)),
                      Cesium.Math.lerp(m_Cartographic0.latitude, m_Cartographic1.latitude, k / (k_length - 1))
                    )
                  );
                }
                let totalCutArea = 0;
                let totalFillArea = 0;
                // await Cesium.sampleTerrainMostDetailed($this._viewer.terrainProvider, terrainSamplePositions);
                let l_length = terrainSamplePositions.length;
                for (let l = 0; l < l_length - 1; l++) {
                  terrainSamplePositions[l].height = $this._viewer.scene.globe.getHeight(terrainSamplePositions[l]);
                  terrainSamplePositions[l + 1].height = $this._viewer.scene.globe.getHeight(terrainSamplePositions[l + 1]);
                  let a = terrainSamplePositions[l].height - minHeight,
                    b = terrainSamplePositions[l + 1].height - minHeight;

                  /**根据经纬度计算出距离**/
                  let h = $this.getPointToPointDistance(terrainSamplePositions[l], terrainSamplePositions[l + 1]);
                  // let h = $this.getPointToPlaneDistance();
                  let area = (a + b) * h / 2;
                  if (area > 0) {
                    totalCutArea += area;
                  } else {
                    totalFillArea -= area;
                  }
                }
                /**根据经纬度计算出距离**/
                // let w = $this.getPointToPointDistance(prev_m_Cartographic1, m_Cartographic1);
                let w = $this.getPointToPlaneDistance(m_Cartographic0, n_Cartographic0, m_Cartographic1, prev_m_Cartographic1);
                totalCutVolume += totalCutArea * w / 2;
                totalFillVolume += totalFillArea * w / 2;
                prev_m_Cartographic1 = m_Cartographic1;
              }
            }
            // 挖填方面积计算
            let coordinates = [];
            let tmpArr = [];
            for (let i = 0; i < positions.length; i++) {
              let cart = Cesium.Cartographic.fromCartesian(positions[i]);
              tmpArr.push([
                Cesium.Math.toDegrees(cart.longitude),
                Cesium.Math.toDegrees(cart.latitude)
              ]);
              if (i == positions.length - 1) {
                tmpArr.push([tmpArr[0][0], tmpArr[0][1]]);
              }
            }
            coordinates.push(tmpArr);
            options.callback({
              excavationArea: turf.area(turf.polygon(coordinates)).toFixed(2),
              excavationResult: totalCutVolume.toFixed(2),
              excavationFillResult: totalFillVolume.toFixed(2)
            })
          };
          /**
           * 计算三角形的面积
           * @param {*} pos1
           * @param {*} pos2
           * @param {*} pos3
           */
          function _computeAreaOfTriangle(pos1, pos2, pos3) {
            let a = Cesium.Cartesian3.distance(pos1, pos2);
            let b = Cesium.Cartesian3.distance(pos2, pos3);
            let c = Cesium.Cartesian3.distance(pos3, pos1);
            let S = (a + b + c) / 2;
            return Math.sqrt(S * (S - a) * (S - b) * (S - c));
          };
        }
      },
      clear: function () {
        if (this.terrainClipPlan) {
          this.terrainClipPlan.clear();
        }
      },
      getInterPointsLength: function (m_Cartographic0, m_Cartographic1) {
        let a = Math.abs(m_Cartographic0.longitude - m_Cartographic1.longitude) * 10000000;
        let b = Math.abs(m_Cartographic0.latitude - m_Cartographic1.latitude) * 10000000;
        //等距采样
        if (a > b) b = a;
        let length = parseInt(b / 2);
        if (length > 50) length = 50;
        if (length < 2) length = 2;
        return length;
      },
      getPointToPlaneDistance: function (planeA, planeB, planeC, point) {
        let a = Cesium.Cartesian3.fromRadians(planeA.longitude, planeA.latitude, planeA.height);
        let b = Cesium.Cartesian3.fromRadians(planeB.longitude, planeB.latitude, planeB.height);
        let c = Cesium.Cartesian3.fromRadians(planeC.longitude, planeC.latitude, planeC.height);
        let d = Cesium.Cartesian3.fromRadians(point.longitude, point.latitude, point.height);
        let ab = Cesium.Cartesian3.subtract(a, b, new Cesium.Cartesian3());
        let bc = Cesium.Cartesian3.subtract(b, c, new Cesium.Cartesian3());
        let f = Cesium.Cartesian3.cross(ab, bc, new Cesium.Cartesian3());
        f = Cesium.Cartesian3.normalize(f, f);
        let p = new Cesium.Plane(f, 0),
          m = Cesium.Plane.getPointDistance(p, a),
          n = Cesium.Plane.getPointDistance(p, d);
        return Math.abs(m - n);
      },
      getPointToPointDistance: function (pointA, pointB) {
        let geodesic = new Cesium.EllipsoidGeodesic(pointA, pointB, this.ellipsoid);
        // geodesic.setEndPoints(prev_m_Cartographic1, m_Cartographic1);
        return geodesic.surfaceDistance;
      }
    }
    return _
  })(Cesium);
}