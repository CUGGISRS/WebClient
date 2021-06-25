/**
 * 地形剖面
 */
if (typeof Cesium !== 'undefined') {
    /**
     * @param viewer  {object} 三维对象
     * @param options {object} 初始化参数
     * @constructor
     */
    Cesium.ClippingAnalysis = (function (Cesium) {
        /**
         * 绘制对象
         * @param viewer
         * @param options
         * @constructor
         */
        function _(viewer) {
            if (viewer && viewer instanceof Cesium.Viewer) {
                this._drawLayer = new Cesium.CustomDataSource('ClippingAnalysis');
                viewer && viewer.dataSources.add(this._drawLayer);
                this._viewer = viewer;

                if (!this.lineChart) {
                    this.lineChart = echarts.init(document.getElementById("profileChart"));
                }
            }
        }

        _.prototype = {
            // 绘制地形剖面分析路线
            profile: function (options = {}) {
                let $this = this;
                if ($this._viewer && options) {
                    this._drawLayer.entities.removeAll();
                    var handler = createScreenSpaceEventHandler($this._viewer.scene.canvas);
                    var positions = [];
                    var positionsCartographic = [];
                    var positions_Inter = [];
                    var poly = null;
                    var distance = 0;
                    var cartesian = null;
                    var floatingPoint = null;
                    var DistanceArray = [];

                    $this.handler = handler;

                    var profileItem = [];

                    handler.setInputAction(function (movement) {
                        cartesian = $this._viewer.scene.pickPosition(movement.endPosition);
                        if (positions.length >= 2) {
                            if (!Cesium.defined(poly)) {
                                poly = new PolyLinePrimitive(positions);
                                $this.poly = poly;
                            } else {
                                positions.pop();
                                positions.push(cartesian);
                            }
                        }
                    }, Cesium.ScreenSpaceEventType.MOUSE_MOVE);

                    handler.setInputAction(function (movement) {
                        cartesian = $this._viewer.scene.pickPosition(movement.position);
                        if (positions.length == 0) {
                            positions.push(cartesian.clone());
                        }
                        positions.push(cartesian);
                        // if (poly) {
                        //     //进行插值计算
                        //     interPoints(poly.positions);
                        //     distance = getSpaceDistance(positions_Inter);
                        // } else {
                        //     distance = getSpaceDistance(positions);
                        // }
                        // var textDisance = distance + "米";
                        // DistanceArray.push(distance);
                        // floatingPoint = $this._viewer.entities.add({
                        //     position: positions[positions.length - 1],
                        //     point: {
                        //         pixelSize: 5,
                        //         color: Cesium.Color.RED,
                        //         outlineColor: Cesium.Color.WHITE,
                        //         outlineWidth: 2,
                        //         heightReference: Cesium.HeightReference.NONE
                        //     },
                        //     label: {
                        //         text: textDisance,
                        //         font: '18px sans-serif',
                        //         fillColor: Cesium.Color.GOLD,
                        //         style: Cesium.LabelStyle.FILL_AND_OUTLINE,
                        //         outlineWidth: 2,
                        //         verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
                        //         pixelOffset: new Cesium.Cartesian2(20, -20),
                        //         heightReference: Cesium.HeightReference.NONE
                        //     }
                        // });
                        // $this.floatingPoints.push(floatingPoint);
                    }, Cesium.ScreenSpaceEventType.LEFT_CLICK);

                    handler.setInputAction(function () {
                        destroyScreenSpaceEventHandler();//关闭事件句柄
                        positions.pop();//最后一个点无效
                        if (poly && positions.length > 1) {
                            options.callback();
                            //进行插值计算
                            interPoints(poly.positions).then(() => {
                                distance = getSpaceDistance(positions_Inter);
                                $this.createProfileChart(profileItem);
                            });
                        }
                    }, Cesium.ScreenSpaceEventType.RIGHT_CLICK);

                    var PolyLinePrimitive = (function () {
                        function _(positions) {
                            this.options = {
                                polyline: {
                                    show: true,
                                    positions: [],
                                    material: Cesium.Color.CHARTREUSE,
                                    width: 2,
                                    clampToGround: true
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
                            $this._drawLayer.entities.add(this.options);
                        };
                        return _;
                    })();

                    function cartesian3ToDegrees(cartesian) {
                        var cartographic = Cesium.Cartographic.fromCartesian(cartesian);
                        let lon = Cesium.Math.toDegrees(cartographic.longitude);
                        let lat = Cesium.Math.toDegrees(cartographic.latitude);
                        return [lon, lat, cartographic.height];
                    }

                    //空间两点距离计算函数
                    function getSpaceDistance(positions) {
                        profileItem = [
                            {
                                point: cartesian3ToDegrees(positions[0]),
                                distance: 0
                            }
                        ];
                        var distance = 0;
                        for (var i = 0; i < positions.length - 1; i++) {
                            var point1cartographic = Cesium.Cartographic.fromCartesian(positions[i]);
                            var point2cartographic = Cesium.Cartographic.fromCartesian(positions[i + 1]);
                            /**根据经纬度计算出距离**/
                            var geodesic = new Cesium.EllipsoidGeodesic();
                            geodesic.setEndPoints(point1cartographic, point2cartographic);
                            var s = geodesic.surfaceDistance;
                            //返回两点之间的距离
                            s = Math.sqrt(Math.pow(s, 2) + Math.pow(point2cartographic.height - point1cartographic.height, 2));
                            distance = distance + s;
                            var m_Item = {
                                point: cartesian3ToDegrees(positions[i + 1]),
                                distance: distance
                            };
                            profileItem.push(m_Item);
                        }
                        return distance.toFixed(2);
                    }

                    //线段插值点
                    async function interPoints(positions) {
                        positionsCartographic = [];
                        var terrainSamplePositions = [];
                        for (let index = 0; index < positions.length; index++) {
                            const element = positions[index];
                            var ellipsoid = $this._viewer.scene.globe.ellipsoid;
                            var cartographic = ellipsoid.cartesianToCartographic(element);
                            positionsCartographic.push(cartographic);
                        }
                        terrainSamplePositions.push(
                            new Cesium.Cartographic(positionsCartographic[0].longitude, positionsCartographic[0].latitude)
                        );
                        for (let i = 0; i < positionsCartographic.length; i++) {
                            const m_Cartographic0 = positionsCartographic[i];
                            const m_Cartographic1 = positionsCartographic[i + 1];
                            if (m_Cartographic1) {
                                var a = Math.abs(m_Cartographic0.longitude - m_Cartographic1.longitude) * 10000000;
                                var b = Math.abs(m_Cartographic0.latitude - m_Cartographic1.latitude) * 10000000;
                                //等距采样
                                if (a > b) b = a;
                                var length = parseInt(b / 2);
                                if (length > 1000) length = 1000;
                                if (length < 2) length = 2;
                                // var length = 4;//等分采样
                                for (var j = 0; j < length; j++) {
                                    terrainSamplePositions.push(
                                        new Cesium.Cartographic(
                                            Cesium.Math.lerp(m_Cartographic0.longitude, m_Cartographic1.longitude, j / length),
                                            Cesium.Math.lerp(m_Cartographic0.latitude, m_Cartographic1.latitude, j / length)
                                        )
                                    );
                                }
                            } else {
                                terrainSamplePositions.push(m_Cartographic0);
                            }
                        }
                        positions_Inter = [];
                        await Cesium.sampleTerrainMostDetailed($this._viewer.terrainProvider, terrainSamplePositions);
                        for (var n = 0; n < terrainSamplePositions.length; n++) {
                            //地理坐标（弧度）转经纬度坐标
                            var m_cartographic = terrainSamplePositions[n];
                            // var height = $this._viewer.scene.globe.getHeight(m_cartographic);
                            var height = m_cartographic.height;
                            var point = Cesium.Cartesian3.fromRadians(m_cartographic.longitude, m_cartographic.latitude, height);
                            positions_Inter.push(point);
                        }
                    }

                }
            },

            // 显示地形剖面图
            createProfileChart: function (Positions) {
                console.log(Positions);
                var x_Range = parseInt(Positions[Positions.length - 1].distance);
                console.log(x_Range);
                var ProfileData = [];
                var ProfileData_Lon = [];

                var y_Min = 100000000;
                var y_Max = -100000000;
                for (let index = 0; index < Positions.length; index++) {
                    const element = Positions[index];
                    var m_distance = formatFloat(element.distance, 2);
                    var m_Lon = formatFloat(element.point[0], 5);
                    var m_Lat = formatFloat(element.point[1], 5);
                    var m_height = formatFloat(element.point[2], 2);
                    if (m_height < y_Min) {
                        y_Min = m_height;
                    }
                    if (m_height > y_Max) {
                        y_Max = m_height;
                    }
                    var m_data = [m_distance, m_height];
                    ProfileData.push(m_data);
                    ProfileData_Lon.push([m_Lon, m_Lat]);
                }
                var scaleVal = 4.8 * (y_Max - y_Min);
                if (scaleVal == 0) {
                    scaleVal = 10;
                }
                console.log(ProfileData);
                // background: rgba(255, 255, 255, 1);
                var lineoption = {
                    tooltip: {
                        trigger: 'axis',
                        formatter(params) {
                            // console.log(params['data']);
                            return "当前高度：" + params[0]['data'][1];
                        }
                    },
                    // title: {
                    //     text: '地形剖面图',
                    //     textAlign: 'center',
                    //     textStyle: {
                    //         color: '#D9D9D9'
                    //     }
                    // },
                    grid: {},
                    calculable: true,
                    xAxis: [
                        {
                            type: 'value',
                            max: 'dataMax',
                            scale: true
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            boundaryGap: [scaleVal, scaleVal],
                            scale: true
                        }
                    ],
                    dataZoom: [
                        {
                            type: 'inside',
                            orient: 'vertical',
                            start: 40,
                            end: 60
                        }, {}
                    ],
                    series: [
                        {
                            name: '剖面线',
                            type: 'line',
                            areaStyle: {origin: 'start'},
                            showSymbol: false,
                            data: ProfileData,
                            markPoint: {
                                // symbol: 'arrow',
                                data: [
                                    {type: 'max', name: '最高点'},
                                    {type: 'min', name: '最低点', symbolRotate: 180, label: {offset: [0, 10]}}
                                ]
                            }
                        }
                    ]
                };
                this.lineChart.setOption(lineoption);
                this.lineChart.resize();
            },

            // 地形剖面图自适应父窗口大小
            resizeChart: function () {
                if (this.lineChart) {
                    this.lineChart.resize();
                }
            }
        }
        return _
    })(Cesium);
}
