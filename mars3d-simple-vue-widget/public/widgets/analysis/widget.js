(function (window, mars3d) {
    //创建widget类，需要继承BaseWidget
    class MyWidget extends mars3d.widget.BaseWidget { 
        //弹窗配置
        get view() {
            return {
                type: "window",
                url: "view.html",
                windowOptions: {
                    width: 320,
                    height: 400
                }
            }
        } 

        //初始化[仅执行1次]
        create() {
            this.arrKsyList = [];

        }

        //每个窗口创建完成后调用
        winCreateOK(opt, result) {
            this.viewWindow = result;
        }

        //激活插件
        activate() {

        }
        //释放插件
        disable() {
            this.viewWindow = null;
            this.destroyAll();
        }

        openTerrainDepthTest() {
            this._last_depthTestAgainstTerrain = this.viewer.scene.globe.depthTestAgainstTerrain;
            this.viewer.scene.globe.depthTestAgainstTerrain = true;
        }
        resetTerrainDepthTest() {
            if (Cesium.defined(this._last_depthTestAgainstTerrain)) {
                this.viewer.scene.globe.depthTestAgainstTerrain = this._last_depthTestAgainstTerrain;
                delete this._last_depthTestAgainstTerrain
            }
        }

        destroyAll() {
            this.destroyRZFX()//日照分析
            this.destroyPDPX()//坡度坡向
            this.destroyKSY()//可视域分析

            this.destroyFLFX()//方量分析
            this.destroyDXKW() //地形开挖 
            this.destroyDBTM()//地表透明

            this.destroyMXPQ()//模型剖切
            this.destroyMXYP()//模型压平
            this.destroyMXCJ()//模型裁剪 
        }

        enableControl(value) {
            if (this.viewer.mars.popup)
                this.viewer.mars.popup.enable = value;
            if (this.viewer.mars.tooltip)
                this.viewer.mars.tooltip.enable = value;
            if (this.viewer.mars.contextmenu)
                this.viewer.mars.contextmenu.enable = value;
        }

        //=========日照分析========
        createRZFX() {
            this.viewer.clock.onTick.addEventListener(this.showNowTimeRZFX, this);
        }
        destroyRZFX() {
            this.viewer.clock.clockRange = Cesium.ClockRange.UNBOUNDED;
            this.viewer.clock.multiplier = 1;

            this.viewer.clock.onTick.removeEventListener(this.showNowTimeRZFX, this);
            this.viewer.shadows = false;
        }
        showNowTimeRZFX() {
            if (!this.viewWindow || !this.viewer.clock.shouldAnimate) return;

            var currentTime = this.viewer.clock.currentTime;
            var date = Cesium.JulianDate.toDate(currentTime);
            this.viewWindow.setRZFXNowTime(date);
        }
        clearRZFX() {//停止 
            this.viewer.shadows = false;
            this.viewer.clock.shouldAnimate = false;
        }
        startPlayRZFX(startDate, endDate) {
            if (this.stopTime) {
                this.viewer.clock.currentTime = this.stopTime;
            }

            this.viewer.clock.startTime = Cesium.JulianDate.fromDate(startDate);
            this.viewer.clock.currentTime = Cesium.JulianDate.fromDate(startDate);
            this.viewer.clock.stopTime = Cesium.JulianDate.fromDate(endDate);

            this.viewer.clock.clockRange = Cesium.ClockRange.LOOP_STOP;
            this.viewer.clock.multiplier = 1600;
            this.viewer.clock.shouldAnimate = true;

            this.viewer.shadows = true;
        }
        pauseRZFX() {
            if (this.viewer.clock.shouldAnimate) {
                this.stopTime = this.viewer.clock.currentTime;
                this.viewer.clock.shouldAnimate = false;
            } else {
                this.viewer.clock.currentTime = this.stopTime || this.viewer.clock.currentTime;
                this.viewer.clock.shouldAnimate = true;
            }
            return this.viewer.clock.shouldAnimate
        }


        //=========可视域分析========
        createKSY() {
            //不开启抗锯齿，可视域会闪烁
            this.viewer.scene.postProcessStages.fxaa.enabled = true;

            //不加无法投射到地形上
            this.openTerrainDepthTest();
        }
        destroyKSY() {
            this.clearKSY();
            this.resetTerrainDepthTest();
        }
        clearKSY() {
            for (var i = 0, len = this.arrKsyList.length; i < len; i++) {
                this.arrKsyList[i].destroy();
            }
            this.arrKsyList = [];
            delete this.lastViewField;
        }
        getLastKSY() {
            return this.lastViewField || {};
        }
        addKSY(options) {
            var that = this;

            var thisViewField = new mars3d.analysi.ViewShed3D(this.viewer, {
                horizontalAngle: options.horizontalAngle,
                verticalAngle: options.verticalAngle,
                distance: options.distance,
                offsetHeight: 1.5, //增加人的升高 
            });
            thisViewField.on(mars3d.analysi.Slope.event.end, function (event) {
                if (that.viewWindow)
                    that.viewWindow.updateKsyDistance(event.distance);
            })

            this.lastViewField = thisViewField;
            this.arrKsyList.push(thisViewField);
        }
        updateKsyDebugFrustum(show) {
            for (var i = 0, len = this.arrKsyList.length; i < len; i++) {
                this.arrKsyList[i].showFrustum = show;
            }
        }

        //=========方量分析========
        createFLFX() {
            if (this.measureVolume) return;

            var that = this;
            this.measureVolume = new mars3d.analysi.MeasureVolume({
                viewer: this.viewer,
                heightLabel: true,
            });
            this.measureVolume.on(mars3d.analysi.MeasureVolume.event.start, function (e) {
                haoutil.loading.show({ type: "loader-bar" });
            })
            this.measureVolume.on(mars3d.analysi.MeasureVolume.event.end, function (e) {
                haoutil.loading.hide();
                that.viewWindow.showFLFXHeightRg();
            })
        }
        destroyFLFX() {
            if (!this.measureVolume) return;

            this.measureVolume.destroy();
            delete this.measureVolume;
        }
        clearFLFX() {
            if (!this.measureVolume) return;

            this.measureVolume.clear();
        }

        //=========地形开挖========
        createDXKW() {
            this.openTerrainDepthTest();
        }
        startDrawDXKW() {
            var that = this;

            this.enableControl(false);

            viewer.mars.draw.startDraw({
                type: "polygon",
                style: {
                    color: "#29cf34",
                    opacity: 0.5,
                    clampToGround: true
                },
                success: function (entity) { //绘制成功后回调
                    that.enableControl(true);

                    var positions = viewer.mars.draw.getPositions(entity);
                    viewer.mars.draw.deleteAll();
                    that.showDXKWClippingPlanes(positions, true);
                }
            });
        }
        startDrawDXKWExtent() {
            var that = this;

            this.enableControl(false);

            viewer.mars.draw.startDraw({
                type: 'rectangle',
                style: {
                    color: '#007be6',
                    opacity: 0.8,
                    outline: false,
                },
                success: function (entity) { //绘制成功后回调
                    that.enableControl(true);

                    var positions = mars3d.draw.attr.rectangle.getOutlinePositions(entity, true);
                    viewer.mars.draw.deleteAll();
                    that.showDXKWClippingPlanes(positions, false);
                }
            });
        }
        destroyDXKW() {
            this.clearDXKW();
            this.resetTerrainDepthTest();
        }
        clearDXKW() {
            if (this.terrainClip) {
                this.terrainClip.destroy();
                delete this.terrainClip;
            }
            if (this.terrainClipPlan) {
                this.terrainClipPlan.destroy();
                delete this.terrainClipPlan;
            }

            //同时有模型时，清除模型裁剪
            this.clearMXCJ();
        }
        showDXKWClippingPlanes(positions, has2) {
            this.clearDXKW();

            //同时有模型时，进行模型裁剪
            this.addMxcjPoly(positions)

            var height = this.viewWindow.getDXKWNowHeight();


            //cesium原生的clip组成的【因为转cesium原生接口的plan组成，存在问题：不稳定，时而不生效】
            this.terrainClipPlan = new mars3d.analysi.TerrainClipPlan({
                viewer: this.viewer,
                positions: positions,
                height: height,
                wall: true,
                splitNum: 50, //wall边界插值数
                wallImg: this.path + 'img/textures/excavationregion_top.jpg',
                bottomImg: this.path + 'img/textures/excavationregion_side.jpg'
            });

            // marsgis扩展的地形开挖【存在问题：鼠标无法穿透地表，地下管网的popup无法展示】
            if (has2) {
                this.terrainClip = new mars3d.analysi.TerrainClip({
                    viewer: this.viewer,
                    positions: positions,
                    height: height,
                    wall: false
                });
            }

        }
        updateDXKWHeight(nowValue) {
            if (this.terrainClipPlan)
                this.terrainClipPlan.height = nowValue;
        }

        //=========地表透明========
        createDBTM() {
            this.openTerrainDepthTest();

            this.underObj = new mars3d.analysi.Underground({
                viewer: this.viewer,
                alpha: 0.5,
                enable: false,
            });
        }
        destroyDBTM() {
            if (!this.underObj) return;

            this.resetTerrainDepthTest();

            this.underObj.destroy();
            delete this.underObj;
        }
        clearDBTM() {


        }

        //=========坡度坡向========
        createPDPX() {
            if (this.slope) return;

            this.slope = new mars3d.analysi.Slope({
                viewer: this.viewer,
                point: {
                    pixelSize: 9,
                    color: Cesium.Color.RED.withAlpha(0.5),
                    //disableDepthTestDistance: Number.POSITIVE_INFINITY,
                },
                arrow: {
                    scale: 0.3, //箭头长度的比例（范围0.1-0.9）
                    width: 15,  //箭头宽度
                    color: Cesium.Color.YELLOW
                }
            })

        }
        destroyPDPX() {
            if (!this.slope) return;

            this.clearPDPX();
            this.slope.destroy();
            delete this.slope;
        }
        clearPDPX() {
            if (!this.slope) return;

            this.viewer.mars.draw.deleteAll();
            this.slope.clear();
        }
        drawPDPXLine(splitNum) {
            var that = this;
            this.viewer.mars.draw.deleteAll();
            this.viewer.mars.draw.startDraw({
                "type": "polygon",
                "style": {
                    "color": "#29cf34",
                    "opacity": 0.3,
                    "outline": true,
                    "outlineColor": "#ffffff",
                    "clampToGround": true
                },
                success: function (entity) { //绘制成功后回调  
                    var positions = that.viewer.mars.draw.getPositions(entity);
                    that.viewer.mars.draw.deleteAll()

                    that.slope.add(positions, {
                        splitNum: splitNum  //splitNum插值分割的个数
                    });
                }
            });
        }


        //=========模型剖切======== 
        selectedPQMX() {
            var that = this;

            this.enableControl(false);

            this.viewer.mars.draw.startDraw({
                type: "point",
                style: {
                    color: "#007be6",
                },
                success: function (entity) { //绘制成功后回调
                    var positions = that.viewer.mars.draw.getPositions(entity);

                    that.viewer.mars.draw.deleteAll();

                    that.enableControl(true);

                    var tileset = mars3d.tileset.pick3DTileset(that.viewer, positions);//拾取绘制返回的模型
                    if (!tileset) {
                        haoutil.msg("请单击选择模型");
                        return;
                    }
                    that.clipTileset = new mars3d.tiles.TilesClipPlan({ tileset: tileset });

                    var radius = tileset.boundingSphere.radius / 2;
                    that.viewWindow.setClipDistanceRange(radius, tileset.name);
                }
            });
        }
        drawLinePQMX() {
            var that = this;

            if (this.clipTileset) {
                this.clipTileset.clear();
            }
            viewer.mars.draw.startDraw({
                type: 'polyline',
                config: { maxPointNum: 2 },
                style: {
                    color: '#007be6',
                    opacity: 0.8,
                    outline: false,
                },
                success: function (entity) { //绘制成功后回调 
                    var points = viewer.mars.draw.getPositions(entity);
                    viewer.mars.draw.deleteAll();

                    if (that.clipTileset) {
                        that.clipTileset.clear();
                    }
                    else {
                        var tileset = mars3d.tileset.pick3DTileset(viewer, points);//拾取绘制返回的模型
                        if (!tileset) {
                            haoutil.msg("请单击选择模型");
                            return;
                        }
                        that.clipTileset = new mars3d.tiles.TilesClipPlan({ tileset: tileset });
                    }

                    that.clipTileset.clipByPoints(points);
                }
            });
        }
        destroyMXPQ() {
            if (!this.clipTileset) return;

            this.clipTileset.destroy();
            delete this.clipTileset;
        }
        clearMXPQ() {
            if (!this.clipTileset) return;

            this.clipTileset.clear();
        }

        //=========模型压平========
        createMXYP() {

        }
        destroyMXYP() {
            this.clearMXYP();

        }
        clearMXYP() {
            if (!this.flatObj) return;

            this.flatObj.destroy();
            delete this.flatObj;
        }
        drawMxypPoly(flatHeight) {
            this.clearMXYP();

            var that = this;
            viewer.mars.draw.startDraw({
                type: "polygon",
                style: {
                    color: "#007be6",
                    opacity: 0.5,
                    clampToGround: false
                },
                success: function (entity) { //绘制成功后回调
                    var positions = viewer.mars.draw.getPositions(entity);

                    viewer.mars.draw.deleteAll();
                    var tileset = mars3d.tileset.pick3DTileset(viewer, positions);//拾取绘制返回的模型
                    if (!tileset) {
                        haoutil.msg("请单击选择模型");
                        return;
                    }
                    that.flatObj = new mars3d.tiles.TilesFlat({
                        viewer: that.viewer,
                        tileset: tileset,
                        positions: positions,
                        flatHeight: flatHeight
                    });

                }
            });
        }
        //=========模型裁剪========
        createMXCJ() {

        }
        destroyMXCJ() {
            this.clearMXCJ();
        }
        clearMXCJ() {
            if (!this.tilesetClip) return;

            this.tilesetClip.destroy();
            delete this.tilesetClip;
        }
        drawMxcjPoly(clipOutSide) {
            this.clearMXCJ();

            var that = this;
            viewer.mars.draw.startDraw({
                type: 'rectangle',
                style: {
                    color: '#007be6',
                    opacity: 0.8,
                    outline: false,
                    // clampToGround: true
                },
                success: function (entity) { //绘制成功后回调
                    var positions = mars3d.draw.attr.rectangle.getOutlinePositions(entity, true);

                    viewer.mars.draw.deleteAll();
                    var isAdd = that.addMxcjPoly(positions, clipOutSide)
                    if (!isAdd) {
                        haoutil.msg("请单击选择模型");
                    }
                }
            });
        }
        addMxcjPoly(positions, clipOutSide) {
            this.clearMXCJ();

            var tileset = mars3d.tileset.pick3DTileset(this.viewer, positions);//拾取绘制返回的模型
            if (!tileset) {
                return false;
            }


            this.tilesetClip = new mars3d.tiles.TilesClipPlan({
                tileset: tileset,
                positions: positions,
                clipOutSide: clipOutSide
            });

            return true;
        }
    }


    //注册到widget管理器中。
    mars3d.widget.bindClass(MyWidget);

    //每个widet之间都是直接引入到index.html中，会存在彼此命名冲突，所以闭包处理下。
})(window, mars3d)

