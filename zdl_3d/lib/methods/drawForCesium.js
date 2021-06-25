//编辑或删除标识,1为编辑，2为删除
var flag = 0;
//全局变量，用来记录shape坐标信息
var shapeDic = {};
//图层名称
var layerId = "globeDrawerDemoLayer";
const drawGraph = {
  drawPolygon(viewer) {
    var tracker = new GlobeTracker(viewer);
    flag = 0;
    tracker.trackPolygon((positions) => {
      var objId = (new Date()).getTime();
      shapeDic[objId] = positions;
      this.showPolygon(objId, positions, viewer);

    });
  },

  drawPolyline(viewer) {
    var tracker = new GlobeTracker(viewer);
    flag = 0;
    tracker.trackPolyline((positions) => {
      var objId = (new Date()).getTime();
      shapeDic[objId] = positions;
      this.showPolyline(objId, positions, viewer);
    });
  },

  drawRectangle(viewer) {
    var tracker = new GlobeTracker(viewer);
    flag = 0;
    tracker.trackRectangle((positions) => {
      var objId = (new Date()).getTime();
      shapeDic[objId] = positions;
      this.showRectangle(objId, positions, viewer);
    });
  },

  drawCircle(viewer) {
    var tracker = new GlobeTracker(viewer);
    flag = 0;
    tracker.trackCircle((positions) => {
      var objId = (new Date()).getTime();
      shapeDic[objId] = positions;
      this.showCircle(objId, positions, viewer);
    });
  },


  drawPoint(viewer) {
    var tracker = new GlobeTracker(viewer);
    flag = 0;
    tracker.trackPoint((position) => {
      var objId = (new Date()).getTime();
      shapeDic[objId] = position;
      this.showPoint(objId, position, viewer);
    });
  },

  straightArrow(viewer) {
    var tracker = new GlobeTracker(viewer);
    flag = 0;
    tracker.trackStraightArrow((positions) => {
      var objId = (new Date()).getTime();
      shapeDic[objId] = positions;
      this.showStraightArrow(objId, positions, viewer);
    });
  },

  attackArrow(viewer) {
    var tracker = new GlobeTracker(viewer);
    flag = 0;
    tracker.trackAttackArrow((positions, custom) => {
      var objId = (new Date()).getTime();
      shapeDic[objId] = {
        custom: custom,
        positions: positions
      };
      this.showAttackArrow(objId, positions, viewer);
    });
  },

  pincerArrow(viewer) {
    var tracker = new GlobeTracker(viewer);
    flag = 0;
    tracker.trackPincerArrow((positions, custom) => {
      var objId = (new Date()).getTime();
      shapeDic[objId] = {
        custom: custom,
        positions: positions
      };
      this.showPincerArrow(objId, positions, viewer);
    });
  },

  editShape(viewer) {
    var tracker = new GlobeTracker(viewer);
    layer.msg("点击要编辑的箭头！");
    flag = 1;
    //清除标绘状态
    tracker.clear();
  },

  deleteShape(viewer) {
    var tracker = new GlobeTracker(viewer);
    layer.msg("点击要删除的箭头！");
    flag = 2;
    //清除标绘状态
    tracker.clear();
  },


  showCircle(objId, positions, viewer) {
    var tracker = new GlobeTracker(viewer);
    var material = Cesium.Color.fromCssColorString('#ff0').withAlpha(0.5);
    var outlineMaterial = new Cesium.PolylineDashMaterialProperty({
      dashLength: 16,
      color: Cesium.Color.fromCssColorString('#f00').withAlpha(0.7)
    });
    var radiusMaterial = new Cesium.PolylineDashMaterialProperty({
      dashLength: 16,
      color: Cesium.Color.fromCssColorString('#00f').withAlpha(0.7)
    });
    var pnts = tracker.circleDrawer._computeCirclePolygon(positions);
    var dis = tracker.circleDrawer._computeCircleRadius3D(positions);
    dis = (dis / 1000).toFixed(3);
    var text = dis + "km";
    var bData = {
      layerId: layerId,
      objId: objId,
      shapeType: "Circle",
      position: positions[0],
      label: {
        text: text,
        font: '16px Helvetica',
        fillColor: Cesium.Color.SKYBLUE,
        outlineColor: Cesium.Color.BLACK,
        outlineWidth: 1,
        style: Cesium.LabelStyle.FILL_AND_OUTLINE,
        eyeOffset: new Cesium.ConstantProperty(new Cesium.Cartesian3(0, 0, -9000)),
        pixelOffset: new Cesium.Cartesian2(16, 16)
      },
      billboard: {
        image: "images/circle_center.png",
        eyeOffset: new Cesium.ConstantProperty(new Cesium.Cartesian3(0, 0, -500)),
        heightReference: Cesium.HeightReference.CLAMP_TO_GROUND
      },
      polyline: {
        positions: positions,
        clampToGround: true,
        width: 2,
        material: radiusMaterial
      },
      polygon: new Cesium.PolygonGraphics({
        hierarchy: pnts,
        asynchronous: false,
        material: material
      })
    };
    var entity = viewer.entities.add(bData);

    var outlineBdata = {
      layerId: layerId,
      objId: objId,
      shapeType: "Circle",
      polyline: {
        positions: pnts,
        clampToGround: true,
        width: 2,
        material: outlineMaterial
      }
    };
    var outlineEntity = viewer.entities.add(outlineBdata);
  },

  showPoint(objId, position, viewer) {
    var entity = viewer.entities.add({
      layerId: layerId,
      objId: objId,
      shapeType: "Point",
      position: position,
      billboard: {
        image: "images/circle_red.png",
        eyeOffset: new Cesium.ConstantProperty(new Cesium.Cartesian3(0, 0, -500)),
        heightReference: Cesium.HeightReference.CLAMP_TO_GROUND
      }
    });
  },



  showPolygon(objId, positions, viewer) {
    var material = Cesium.Color.fromCssColorString('#ff0').withAlpha(0.5);
    var outlineMaterial = new Cesium.PolylineDashMaterialProperty({
      dashLength: 16,
      color: Cesium.Color.fromCssColorString('#00f').withAlpha(0.7)
    });
    var outlinePositions = [].concat(positions);
    outlinePositions.push(positions[0]);
    var bData = {
      layerId: layerId,
      objId: objId,
      shapeType: "Polygon",
      polyline: {
        positions: outlinePositions,
        clampToGround: true,
        width: 2,
        material: outlineMaterial
      },
      polygon: new Cesium.PolygonGraphics({
        hierarchy: positions,
        asynchronous: false,
        material: material
      })
    };
    var entity = viewer.entities.add(bData);
  },

  //绑定cesiu事件
  bindGloveEvent(viewer) {
    var handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
    handler.setInputAction((movement) => {
      var pick = viewer.scene.pick(movement.position);
      if (!pick) {
        return;
      }
      var obj = pick.id;
      if (!obj || !obj.layerId || flag == 0) {
        return;
      }
      var objId = obj.objId;
      //flag为编辑或删除标识,1为编辑，2为删除
      if (flag == 1) {
        switch (obj.shapeType) {
          case "Polygon":
            flag = 0;
            this.editPolygon(objId, viewer);
            break;
          case "Polyline":
            flag = 0;
            this.editPolyline(objId, viewer);
            break;
          case "Rectangle":
            flag = 0;
            this.editRectangle(objId, viewer);
            break;
          case "Circle":
            flag = 0;
            this.editCircle(objId, viewer);
            break;
          case "Point":
            flag = 0;
            this.editPoint(objId, viewer);
            break;
          case "BufferLine":
            flag = 0;
            this.editBufferLine(objId, viewer);
            break;
          case "StraightArrow":
            flag = 0;
            this.editStraightArrow(objId, viewer);
            break;
          case "AttackArrow":
            flag = 0;
            this.editAttackArrow(objId, viewer);
            break;
          case "PincerArrow":
            flag = 0;
            this.editPincerArrow(objId, viewer);
            break;
          default:
            break;
        }
      } else if (flag == 2) {
        this.clearEntityById(objId, viewer);
      }
    }, Cesium.ScreenSpaceEventType.LEFT_CLICK);
  },


  showPolyline(objId, positions, viewer) {
    var material = new Cesium.PolylineGlowMaterialProperty({
      glowPower: 0.25,
      color: Cesium.Color.fromCssColorString('#00f').withAlpha(0.9)
    });
    var bData = {
      layerId: layerId,
      objId: objId,
      shapeType: "Polyline",
      polyline: {
        positions: positions,
        clampToGround: true,
        width: 8,
        material: material
      }
    };
    var entity = viewer.entities.add(bData);
  },

  showRectangle(objId, positions, viewer) {
    var material = Cesium.Color.fromCssColorString('#ff0').withAlpha(0.5);
    var outlineMaterial = new Cesium.PolylineDashMaterialProperty({
      dashLength: 16,
      color: Cesium.Color.fromCssColorString('#00f').withAlpha(0.7)
    });
    var rect = Cesium.Rectangle.fromCartesianArray(positions);
    var arr = [rect.west, rect.north, rect.east, rect.north, rect.east, rect.south, rect.west, rect.south, rect.west, rect.north];
    var outlinePositions = Cesium.Cartesian3.fromRadiansArray(arr);
    var bData = {
      layerId: layerId,
      objId: objId,
      shapeType: "Rectangle",
      polyline: {
        positions: outlinePositions,
        clampToGround: true,
        width: 2,
        material: outlineMaterial
      },
      rectangle: {
        coordinates: rect,
        material: material
      }
    };
    var entity = viewer.entities.add(bData);
  },



  showBufferLine(objId, positions, radius, viewer) {
    var buffer = tracker.bufferLineDrawer.computeBufferLine(positions, radius);
    var material = Cesium.Color.fromCssColorString('#ff0').withAlpha(0.5);
    var lineMaterial = new Cesium.PolylineDashMaterialProperty({
      dashLength: 16,
      color: Cesium.Color.fromCssColorString('#00f').withAlpha(0.7)
    });
    var bData = {
      layerId: layerId,
      objId: objId,
      shapeType: "BufferLine",
      polygon: new Cesium.PolygonGraphics({
        hierarchy: buffer,
        asynchronous: false,
        material: material
      }),
      polyline: {
        positions: positions,
        clampToGround: true,
        width: 2,
        material: lineMaterial
      }
    };
    var entity = viewer.entities.add(bData);
  },

  showStraightArrow(objId, positions, viewer) {
    var material = Cesium.Color.fromCssColorString('#ff0').withAlpha(0.5);
    var outlineMaterial = new Cesium.PolylineDashMaterialProperty({
      dashLength: 16,
      color: Cesium.Color.fromCssColorString('#f00').withAlpha(0.7)
    });
    var outlinePositions = [].concat(positions);
    outlinePositions.push(positions[0]);
    var bData = {
      layerId: layerId,
      objId: objId,
      shapeType: "StraightArrow",
      polyline: {
        positions: outlinePositions,
        clampToGround: true,
        width: 2,
        material: outlineMaterial
      },
      polygon: new Cesium.PolygonGraphics({
        hierarchy: positions,
        asynchronous: false,
        material: material
      })
    };
    var entity = viewer.entities.add(bData);
  },

  showAttackArrow(objId, positions, viewer) {
    var material = Cesium.Color.fromCssColorString('#ff0').withAlpha(0.5);
    var outlineMaterial = new Cesium.PolylineDashMaterialProperty({
      dashLength: 16,
      color: Cesium.Color.fromCssColorString('#f00').withAlpha(0.7)
    });
    var outlinePositions = [].concat(positions);
    outlinePositions.push(positions[0]);
    var bData = {
      layerId: layerId,
      objId: objId,
      shapeType: "AttackArrow",
      polyline: {
        positions: outlinePositions,
        clampToGround: true,
        width: 2,
        material: outlineMaterial
      },
      polygon: new Cesium.PolygonGraphics({
        hierarchy: positions,
        asynchronous: false,
        material: material
      })
    };
    var entity = viewer.entities.add(bData);
  },

  showPincerArrow(objId, positions, viewer) {
    var material = Cesium.Color.fromCssColorString('#ff0').withAlpha(0.5);
    var outlineMaterial = new Cesium.PolylineDashMaterialProperty({
      dashLength: 16,
      color: Cesium.Color.fromCssColorString('#f00').withAlpha(0.7)
    });
    var outlinePositions = [].concat(positions);
    outlinePositions.push(positions[0]);
    var bData = {
      layerId: layerId,
      objId: objId,
      shapeType: "PincerArrow",
      polyline: {
        positions: outlinePositions,
        clampToGround: true,
        width: 2,
        material: outlineMaterial
      },
      polygon: new Cesium.PolygonGraphics({
        hierarchy: positions,
        asynchronous: false,
        material: material
      })
    };
    var entity = viewer.entities.add(bData);
  },

  editPolygon(objId, viewer) {
    var tracker = new GlobeTracker(viewer);

    var oldPositions = shapeDic[objId];

    //先移除entity
    this.clearEntityById(objId, viewer);

    //进入编辑状态
    tracker.polygonDrawer.showModifyPolygon(oldPositions, (positions) => {
      shapeDic[objId] = positions;
      this.showPolygon(objId, positions, viewer);
    }, () => {
      this.showPolygon(objId, oldPositions, viewer);
    });
  },

  editPolyline(objId, viewer) {
    var tracker = new GlobeTracker(viewer);
    var oldPositions = shapeDic[objId];

    //先移除entity
    this.clearEntityById(objId, viewer);

    //进入编辑状态
    tracker.polylineDrawer.showModifyPolyline(oldPositions, (positions) => {
      shapeDic[objId] = positions;
      this.showPolyline(objId, positions, viewer);
    }, () => {
      this.showPolyline(objId, oldPositions, viewer);
    });
  },

  editRectangle(objId, viewer) {
    var tracker = new GlobeTracker(viewer);
    var oldPositions = shapeDic[objId];

    //先移除entity
    this.clearEntityById(objId, viewer);

    //进入编辑状态
    tracker.rectDrawer.showModifyRectangle(oldPositions, (positions) => {
        shapeDic[objId] = positions;
        this.showRectangle(objId, positions, viewer);
      },
      () => {
        this.showRectangle(objId, oldPositions, viewer);
      });
  },

  editCircle(objId, viewer) {
    var tracker = new GlobeTracker(viewer);
    var oldPositions = shapeDic[objId];

    //先移除entity
    this.clearEntityById(objId, viewer);

    //进入编辑状态
    tracker.circleDrawer.showModifyCircle(oldPositions, (positions) => {
      shapeDic[objId] = positions;
      this.showCircle(objId, positions, viewer);
    }, () => {
      this.showCircle(objId, oldPositions, viewer);
    });
  },

  editPoint(objId, viewer) {
    var tracker = new GlobeTracker(viewer);
    var oldPosition = shapeDic[objId];

    //先移除entity
    this.clearEntityById(objId, viewer);

    //进入编辑状态
    tracker.pointDrawer.showModifyPoint(oldPosition, (position) => {
      shapeDic[objId] = position;
      this.showPoint(objId, position, viewer);
    }, () => {
      this.showPoint(objId, oldPosition, viewer);
    });
  },

  editBufferLine(objId, viewer) {
    var tracker = new GlobeTracker(viewer);
    var old = shapeDic[objId];

    //先移除entity
    this.clearEntityById(objId, viewer);

    //进入编辑状态
    tracker.bufferLineDrawer.showModifyBufferLine(old.positions, old.radius, (positions, radius) => {
      shapeDic[objId] = {
        positions: positions,
        radius: radius
      };
      this.showBufferLine(objId, positions, radius, viewer);
    }, () => {
      this.showBufferLine(old.positions, old.radius, oldPositions, viewer);
    });
  },

  editStraightArrow(objId, viewer) {
    var tracker = new GlobeTracker(viewer);
    var oldPositions = shapeDic[objId];

    //先移除entity
    this.clearEntityById(objId, viewer);

    //进入编辑状态
    tracker.straightArrowDrawer.showModifyStraightArrow(oldPositions, (positions) => {
      shapeDic[objId] = positions;
      this.showStraightArrow(objId, positions, viewer);
    }, () => {
      this.showStraightArrow(objId, oldPositions, viewer);
    });
  },

  editAttackArrow(objId, viewer) {
    var tracker = new GlobeTracker(viewer);
    var old = shapeDic[objId];

    //先移除entity
    this.clearEntityById(objId, viewer);

    tracker.attackArrowDrawer.showModifyAttackArrow(old.custom, (positions, custom) => {
      //保存编辑结果
      shapeDic[objId] = {
        custom: custom,
        positions: positions
      };
      this.showAttackArrow(objId, positions, viewer);
    }, () => {
      this.showAttackArrow(objId, old.positions, viewer);
    });
  },

  editPincerArrow(objId, viewer) {
    var tracker = new GlobeTracker(viewer);
    var old = shapeDic[objId];

    //先移除entity
    this.clearEntityById(objId, viewer);

    tracker.pincerArrowDrawer.showModifyPincerArrow(old.custom, (positions, custom) => {
      //保存编辑结果
      shapeDic[objId] = {
        custom: custom,
        positions: positions
      };
      this.showPincerArrow(objId, positions, viewer);
    }, () => {
      this.showPincerArrow(objId, old.positions, viewer);
    });
  },

  clearEntityById(objId, viewer) {
    var entityList = viewer.entities.values;
    if (entityList == null || entityList.length < 1) {
      return;
    }
    for (var i = 0; i < entityList.length; i++) {
      var entity = entityList[i];
      if (entity.layerId == layerId && entity.objId == objId) {
        viewer.entities.remove(entity);
        i--;
      }
    }
  },

  //三维对象绘制
  handleDrawcylinder(viewer, handler, vm) {
    handler.setInputAction((event) => {
      //定义一个屏幕点击的事件，pickPosition封装的是获取点击的位置的坐标
      var position = viewer.scene.pickPosition(event.position);
      document.body.style.cursor = "Default";
      //输出之后我们发现如前言所说的坐标都是笛卡尔坐标，所以我们需要转换笛卡尔坐标

      //将笛卡尔坐标转化为弧度坐标
      var cartographic = Cesium.Cartographic.fromCartesian(position);
      //将弧度坐标转换为经纬度坐标
      var longitude = Cesium.Math.toDegrees(cartographic.longitude); //经度
      var latitude = Cesium.Math.toDegrees(cartographic.latitude); //纬度
      var height = cartographic.height; //高度

      if (vm.graphCate == '圆柱' || vm.graphCate == '圆锥') {
        vm.tdGraphModal = true;
      } else if (vm.graphCate == '球体') {
        vm.ballGraphModal = true;
      } else if (vm.graphCate == '立方体') {
        vm.cubeGraphModal = true;
      }
      vm.formItem.longitude = longitude;
      vm.formItem.latitude = latitude;
      handler.removeInputAction(Cesium.ScreenSpaceEventType.LEFT_CLICK) //移除事件
    }, Cesium.ScreenSpaceEventType.LEFT_CLICK);
    handler.setInputAction(() => {
      document.body.style.cursor = "Default";
      handler.removeInputAction(Cesium.ScreenSpaceEventType.LEFT_CLICK) //移除事件
    }, Cesium.ScreenSpaceEventType.RIGHT_CLICK);
  },
  handleSumbitCylinder(viewer, vm) {
    vm.$refs['formItem'].validate((valid) => {
      if (!valid) return vm.$Message.warning('请检查信息是否填写完整！');
      let cylinderContent = {
        longitude: vm.formItem.longitude,
        latitude: vm.formItem.latitude,
        graphHeight: vm.formItem.graphHeight,
        topRadius: vm.formItem.topRadius,
        bottomRadius: vm.formItem.bottomRadius,
        graphColor: vm.formItem.graphColor,
        outline: vm.formItem.outline,
        outlineColor: vm.formItem.outlineColor,
      }
      let params = {
        name: vm.formItem.name,
        type: vm.formItem.type,
        content: JSON.stringify(cylinderContent)
      }
      doApi_2(
        res => {
          if (res.status == 200) {
            vm.$Message.info('创建图形成功！');
            this.getGraphData(vm, viewer);
            vm.tdGraphModal = false;
          } else if (res.status == 500) {
            vm.$Message.warning('已有相同名称图形！');
            vm.$refs.formItem.resetFields();
          }
        },
        err => {
          console.log(err)
        },
        '/FigureData',
        'post',
        JSON.stringify(params),
      )
    })
  },
  //创建球体数据存后台
  handleSumbitBall(viewer, vm) {
    vm.$refs['ballFormItem'].validate((valid) => {
      if (!valid) return vm.$Message.warning('请检查信息是否填写完整！');
      let ballContent = {
        longitude: vm.formItem.longitude,
        latitude: vm.formItem.latitude,
        radii: vm.ballFormItem.radii,
        graphColor: vm.ballFormItem.graphColor,
        outline: vm.ballFormItem.outline,
        outlineColor: vm.ballFormItem.outlineColor,
      }
      let params = {
        name: vm.ballFormItem.name,
        type: vm.ballFormItem.type,
        content: JSON.stringify(ballContent)
      }
      doApi_2(
        res => {
          if (res.status == 200) {
            vm.$Message.info('创建图形成功！');
            this.getGraphData(vm, viewer);
            vm.ballGraphModal = false;
          } else if (res.status == 500) {
            vm.$Message.warning('已有相同名称图形！');
            vm.$refs.ballFormItem.resetFields();
          }
        },
        err => {
          console.log(err)
        },
        '/FigureData',
        'post',
        JSON.stringify(params),
      )
    })
  },
  //创建立方体数据存后台
  handleSumbitCube(viewer, vm) {
    vm.$refs['cubeFormItem'].validate((valid) => {
      if (!valid) return vm.$Message.warning('请检查信息是否填写完整！');
      let cubeContent = {
        longitude: vm.formItem.longitude,
        latitude: vm.formItem.latitude,
        length: vm.cubeFormItem.length,
        width: vm.cubeFormItem.width,
        height: vm.cubeFormItem.height,
        graphColor: vm.cubeFormItem.graphColor,
        outline: vm.cubeFormItem.outline,
        outlineColor: vm.cubeFormItem.outlineColor,
      }
      let params = {
        name: vm.cubeFormItem.name,
        type: vm.cubeFormItem.type,
        content: JSON.stringify(cubeContent)
      }
      doApi_2(
        res => {
          if (res.status == 200) {
            vm.$Message.info('创建图形成功！');
            this.getGraphData(vm, viewer);
            vm.cubeGraphModal = false;
          } else if (res.status == 500) {
            vm.$Message.warning('已有相同名称图形！');
            vm.$refs.cubeFormItem.resetFields();
          }
        },
        err => {
          console.log(err)
        },
        '/FigureData',
        'post',
        JSON.stringify(params),
      )
    })
  },
  getGraphDataAll(vm, viewer) {
    doApi_2(res => {
        if (res.status == 200) {
          vm.ballGraphList = res.data.rows;
          vm.graphParams.total = res.data.total;
          res.data.rows.map(item => {
            if (item.type == '球体') {
              let ballData = JSON.parse(item.content);
              this.createdBallGraph(ballData, item, viewer, vm);
              item.show = item.obj.show;
            } else if (item.type == '立方体') {
              let cubeData = JSON.parse(item.content);
              this.createdCubeGraph(cubeData, item, viewer, vm);
            } else if (item.type == '圆柱' || item.type == '圆锥') {
              let cylinderData = JSON.parse(item.content);
              this.createdCylinderGraph(cylinderData, item, viewer, vm);
            }
          })
        }
      }, err => {
        console.log(err)
      },
      '/FigureData/page',
      'get',
    )
  },
  //获取三维图形数据数据
  getGraphData(vm, viewer) {
    doApi_2(res => {
        if (res.status == 200) {
          vm.ballGraphList = res.data.rows;
          vm.graphParams.total = res.data.total;
          res.data.rows.map(item => {
            if (item.type == '球体') {
              let graphData = JSON.parse(item.content);
              this.createdBallGraph(graphData, item, viewer, vm)
            } else if (item.type == '立方体') {
              let cubeData = JSON.parse(item.content);
              this.createdCubeGraph(cubeData, item, viewer, vm)
            } else if (item.type == '圆柱' || item.type == '圆锥') {
              let cylinderData = JSON.parse(item.content);
              this.createdCylinderGraph(cylinderData, item, viewer, vm)
            }
          })
        }
      }, err => {
        console.log(err)
      },
      '/FigureData/page',
      'get',
      vm.graphParams
    )
  },
  //创建圆柱体/圆锥体
  createdCylinderGraph(data, item, viewer, vm) {
    let graphColor = data.graphColor.slice(5, data.graphColor.length - 1);
    let graphColorArr = graphColor.split(',');
    let outlineColor = data.outlineColor.slice(5, data.outlineColor.length - 1);
    let outlineColorArr = outlineColor.split(',');
    //圆柱体
    if (!item.obj) {
      if (!vm.graphMap.get(item.id)) {
        let ballObj = viewer.entities.add({
          name: item.name,
          position: Cesium.Cartesian3.fromDegrees(data.longitude, data.latitude, parseInt(data.graphHeight) / 2),
          cylinder: {
            length: parseInt(data.graphHeight), //圆柱体高度
            topRadius: vm.isCylinder ? parseInt(data.topRadius) : 0, //圆柱体的顶部半径。
            bottomRadius: parseInt(data.bottomRadius), //    圆柱体底部的半径。
            material: new Cesium.Color(parseInt(graphColorArr[0]) / 255, parseInt(graphColorArr[1]) / 255, parseInt(graphColorArr[2]) / 255, parseFloat(graphColorArr[3])),
            outline: data.outline == '1' ? true : false, //轮廓
            outlineColor: new Cesium.Color(parseInt(outlineColorArr[0]) / 255, parseInt(outlineColorArr[1]) / 255, parseInt(outlineColorArr[2]) / 255, parseFloat(outlineColorArr[3])),
          }
        });
        vm.graphMap.set(item.id, ballObj);
        item.obj = ballObj;
        item.show = item.obj.show;
      } else {
        item.obj = vm.graphMap.get(item.id);
        item.show = item.obj.show;
      }
    }
  },
  //创建球体
  createdBallGraph(data, item, viewer, vm) {
    let graphColor = data.graphColor.substr(5, data.graphColor.length - 1);
    let graphColorArr = graphColor.split(',');
    let outlineColor = data.outlineColor.slice(5, data.outlineColor.length - 1);
    let outlineColorArr = outlineColor.split(',');
    if (!item.obj) {
      if (!vm.graphMap.get(item.id)) {
        let ballObj = viewer.entities.add({
          name: item.name,
          position: Cesium.Cartesian3.fromDegrees(data.longitude, data.latitude, parseInt(data.radii)),
          ellipsoid: {
            radii: new Cesium.Cartesian3(parseInt(data.radii), parseInt(data.radii), parseInt(data.radii)),
            material: new Cesium.Color(parseInt(graphColorArr[0]) / 255, parseInt(graphColorArr[1]) / 255, parseInt(graphColorArr[2]) / 255, parseFloat(graphColorArr[3])),
            outline: data.outline == '1' ? true : false, //轮廓
            outlineColor: new Cesium.Color(parseInt(outlineColorArr[0]) / 255, parseInt(outlineColorArr[1]) / 255, parseInt(outlineColorArr[2]) / 255, parseFloat(outlineColorArr[3])),
          }
        });
        vm.graphMap.set(item.id, ballObj);
        item.obj = ballObj;
        item.show = item.obj.show;
      } else {
        item.obj = vm.graphMap.get(item.id);
        item.show = item.obj.show;
      }
    }
  },
  //创建立方体
  createdCubeGraph(data, item, viewer, vm) {
    //箱子
    let graphColor = data.graphColor.slice(5, data.graphColor.length - 1);
    let graphColorArr = graphColor.split(',');
    let outlineColor = data.outlineColor.slice(5, data.outlineColor.length - 1);
    let outlineColorArr = outlineColor.split(',');
    if (!item.obj) {
      if (!vm.graphMap.get(item.id)) {
        let ballObj = viewer.entities.add({
          name: item.name,
          position: Cesium.Cartesian3.fromDegrees(data.longitude, data.latitude, parseInt(data.height) / 2),
          box: {
            dimensions: new Cesium.Cartesian3(parseInt(data.length), parseInt(data.width), parseInt(data.height)),
            material: new Cesium.Color(parseInt(graphColorArr[0]) / 255, parseInt(graphColorArr[1]) / 255, parseInt(graphColorArr[2]) / 255, parseFloat(graphColorArr[3])),
            outline: data.outline == '1' ? true : false, //轮廓
            outlineColor: new Cesium.Color(parseInt(outlineColorArr[0]) / 255, parseInt(outlineColorArr[1]) / 255, parseInt(outlineColorArr[2]) / 255, parseFloat(outlineColorArr[3])),
          }
        });
        vm.graphMap.set(item.id, ballObj);
        item.obj = ballObj;
        item.show = item.obj.show;
      } else {
        item.obj = vm.graphMap.get(item.id);
        item.show = item.obj.show;
      }
    }
  },
  //根据id删除球体实例
  delSingleGraph(id, vm, viewer) {
    vm.$Modal.confirm({
      title: '删除',
      content: '确认删除图形吗？',
      onOk: () => {
        doApi_2(
          (res) => {
            if (res.status == 200) {
              vm.$Message.success('删除图形成功！');
              viewer.entities.remove(vm.graphMap.get(id))
              vm.graphMap.delete(id);
              this.getGraphData(vm, viewer)
            }
          },
          (err) => {
            console.log(err);
          },
          '/FigureData/' + id,
          'delete',
        );
      },
      onCancel: () => {
        vm.$Message.info('已取消删除！');
      }
    });
  }
}

export default drawGraph;