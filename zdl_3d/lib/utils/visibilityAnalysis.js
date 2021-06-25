/**
 * 通视分析功能模块
 */
;
if (typeof Cesium !== 'undefined')
    /**
     * @param viewer  {object} 三维对象
     * @param options {object} 初始化参数
     * @constructor
     */
    Cesium.VisibilityAnalysis = (function (Cesium) {
        /**
         * 绘制对象
         * @param viewer
         * @param options
         * @constructor
         */
        function _(viewer) {
            if (viewer && viewer instanceof Cesium.Viewer) {
                this._drawLayer = new Cesium.CustomDataSource('measureLayer');
                viewer && viewer.dataSources.add(this._drawLayer);
                this._viewer = viewer;
            }
        }

        _.prototype = {
            /**
             * 通视分析
             * @param {*} options
             */
            drawVisibilityGraphics: function (options = {}) {
                if (this._viewer && options) {
                    let positions = [],
                        objLine = null,
                        $this = this,
                        _handlers = createScreenSpaceEventHandler(this._viewer.scene.canvas);
                    // left
                    _handlers.setInputAction(function (movement) {
                        positions = [];
                        let cartesian = $this._viewer.scene.pickPosition(movement.position);
                        if (cartesian && cartesian.x) {
                            // 清除上一个点
                            $this._drawLayer.entities.removeAll();
                            _addInfoPoint(cartesian);
                            positions.push(cartesian);
                            positions.push(cartesian.clone());
                            if (objLine) {
                                $this._drawLayer.entities.remove(objLine);
                                objLine = null;
                            }
                            objLine = $this._drawLayer.entities.add({
                                polyline: {
                                    positions: new Cesium.CallbackProperty(() => {
                                        return positions;
                                    }, false),
                                    arcType: Cesium.ArcType.NONE,
                                    width: 5,
                                    material: Cesium.Color.BLUE,
                                    depthFailMaterial: Cesium.Color.BLUE
                                }
                            })
                        }
                    }, Cesium.ScreenSpaceEventType.LEFT_CLICK);
                    // move
                    _handlers.setInputAction(function (movement) {
                        let cartesian = $this._viewer.scene.pickPosition(movement.endPosition);
                        if (positions.length >= 2) {
                            if (cartesian && cartesian.x) {
                                positions.pop();
                                positions.push(cartesian);
                            }
                        }
                    }, Cesium.ScreenSpaceEventType.MOUSE_MOVE);
                    // right
                    _handlers.setInputAction(function (movement) {
                        destroyScreenSpaceEventHandler();
                        _handlers = null;
                        let cartesian = $this._viewer.scene.pickPosition(movement.position);
                        if (cartesian && cartesian.x) {
                            _addInfoPoint(cartesian);
                            positions.pop();
                            positions.push(cartesian);
                            $this._drawLayer.entities.remove(objLine);
                            objLine = null;
                            setTimeout(() => {
                                _visibilityAnalysis(positions);
                            }, 0);
                        }
                    }, Cesium.ScreenSpaceEventType.RIGHT_CLICK);

                    //添加坐标点
                    function _addInfoPoint(position) {
                        let _labelEntity = new Cesium.Entity();
                        _labelEntity.position = position;
                        _labelEntity.point = {
                            pixelSize: 10,
                            color: Cesium.Color.BLUE
                        }
                        $this._drawLayer.entities.add(_labelEntity);
                    };

                    // 绘制线
                    function _drawLine(leftPoint, secPoint, color) {
                        return $this._drawLayer.entities.add({
                            polyline: {
                                positions: [leftPoint, secPoint],
                                arcType: Cesium.ArcType.NONE,
                                width: 5,
                                material: color,
                                depthFailMaterial: color
                            }
                        })
                    };

                    function _visibilityAnalysis(p) {
                        // 计算射线的方向
                        let direction = Cesium.Cartesian3.normalize(Cesium.Cartesian3.subtract(p[1], p[0], new Cesium.Cartesian3()), new Cesium.Cartesian3());
                        // 建立射线
                        let ray = new Cesium.Ray(p[0], direction);
                        let rayTest = {
                            direction: new Cesium.Cartesian3(0.8653331106656453, 0.37369531311102033, -0.3339916474173786),
                            origin: new Cesium.Cartesian3(-2158187.6647615014, 5088506.444651321, 3172170.801776763)
                        };
                        // 距离计算
                        let rad1 = Cesium.Cartographic.fromCartesian(p[0]);
                        let rad2 = Cesium.Cartographic.fromCartesian(p[1]);
                        let degree1 = {
                            longitude: rad1.longitude / Math.PI * 180,
                            latitude: rad1.latitude / Math.PI * 180,
                            height: rad1.height
                        };
                        let degree2 = {
                            longitude: rad2.longitude / Math.PI * 180,
                            latitude: rad2.latitude / Math.PI * 180,
                            height: rad2.height
                        };
                        // 是否可视
                        let isVisiblity = '';
                        //在第二点处放置一个label说明一些信息
                        let entity = $this._drawLayer.entities.add({
                            label: {
                                name: 'tongshifenxi',
                                show: false,
                                showBackground: true,
                                font: '14px monospace',
                                horizontalOrigin: Cesium.HorizontalOrigin.LEFT,
                                verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
                                pixelOffset: new Cesium.Cartesian2(15, -10)
                            }
                        });
                        // 计算交互点，返回第一个
                        let result = $this._viewer.scene.pickFromRay(ray);
                        if (Cesium.defined(result) && Cesium.defined(result.object) && Cesium.defined(result.position)) {
                            _drawLine(result.position, p[0], Cesium.Color.GREEN); // 可视区域
                            _drawLine(result.position, p[1], Cesium.Color.RED); // 不可视区域
                            isVisiblity = '否';
                        } else {
                            _drawLine(p[0], p[1], Cesium.Color.GREEN);
                            isVisiblity = '是';
                        }
                        entity.position = p[1];
                        entity.label.show = true;
                        let length_ping = Math.sqrt(Math.pow(p[0].x - p[1].x, 2) + Math.pow(p[0].y - p[1].y, 2) + Math.pow(p[0].z - p[1].z, 2));
                        let length_h = Math.abs(degree2.height - degree1.height);
                        let length = Math.sqrt(Math.pow(length_ping, 2) + Math.pow(length_h, 2));
                        let text =
                            '起点坐标: (' + degree1.longitude + '\u00B0' + ', ' + degree1.latitude + '\u00B0' + ', ' + degree1.height + ')' +
                            '\n终点坐标: (' + degree2.longitude + '\u00B0' + ', ' + degree2.latitude + '\u00B0' + ', ' + degree2.height + ')' +
                            '\n垂直距离: ' + length_h +
                            '\n水平距离: ' + length_ping +
                            '\n空间距离: ' + length +
                            '\n是否可视: ' + isVisiblity;
                        entity.label.text = text;
                    };
                }
            }
        }
        return _
    })(Cesium);
