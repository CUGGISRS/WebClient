/**
 * 物联网
 */
if (typeof Cesium !== 'undefined') {
	/**
	 * @param viewer  {object} 三维对象
	 * @param options {object} 初始化参数
	 * @constructor
	 */
	Cesium.IoT = (function (Cesium) {
		/**
		 * 绘制对象
		 * @param viewer
		 * @param options
		 * @constructor
		 */
		function _(viewer) {
			if (viewer && viewer instanceof Cesium.Viewer) {
				this._drawLayer = new Cesium.CustomDataSource('IoT');
				viewer && viewer.dataSources.add(this._drawLayer);
				this._viewer = viewer;

				this.token = '';

				$.ajax({
					url: 'https://open.ys7.com/api/lapp/token/get',
					data: { appKey: '7053177ab6b74e279e9a2b43301ebe54', appSecret: '32a5a3f2646906bbea321c3ee48c15cd' },
					type: 'POST',
					success: (res) => {
						if (res && res.code == 200 && res.data && res.data.accessToken) {
							this.token = res.data.accessToken;
						}
					}
				})

				let infoDiv = `
                    <div id="trackPopUp" class="trackPopUp">
                      <div id="trackPopUpContent" class="leaflet-popup" style="top:5px;left:0;">
                        <a id="trackPopUpClose" class="leaflet-popup-close-button" href="#">×</a>
                        <div class="leaflet-popup-content-wrapper">
                          <div id="trackPopUpLink" class="leaflet-popup-content"></div>
                        </div>
                      </div>
                    </div>
                  `;
				let videoDiv = `
                    <div id="videoTrackPopUp" class="trackPopUp">
                      <div id="videoTrackPopUpContent" class="leaflet-popup" style="top:5px;left:0;">
                        <a id="videoTrackPopUpClose" class="leaflet-popup-close-button" href="#">×</a>
                        <div class="video-leaflet-popup-content-wrapper">
                            <iframe
                                id="videoFrame"
                                src=""
                                id="ysopen"
                                allowfullscreen
                                frameborder="0"
                                scrolling="no"
                                width="100%"
                                height="100%"
                                style="display:block">
                            </iframe>
                        </div>
                      </div>
                    </div>
                  `;
				$("#cesiumContainer").append(infoDiv);
				$("#cesiumContainer").append(videoDiv);
				$('#trackPopUpClose').click(this.closePopUp);
				document.getElementById('trackPopUpClose').ioT = this;
				document.getElementById('videoTrackPopUpClose').ioT = this;
				$('#videoTrackPopUpClose').click(this.videoClosePopUp);

				this.videoEle = document.getElementById('videoFrame');
				this.popupEle = document.getElementById('cesiumContainer');
				this.init();
				let $this = this;
				//绑定地图移动
				this._viewer.scene.postRender.addEventListener(function () {
					$this.showSensorData();
				});
			}
		}

		_.prototype = {
			init: function () {
				for (let i = 0; i < ioTEnterprises.length; i++) {
					let epObj = ioTEnterprises[i];
					let enterprise = {};
					enterprise.username = epObj.username;
					enterprise.password = epObj.password;
					enterprise.sysName = 'X2';
					// token
					if (!epObj.token || epObj.expireTime < new Date().getTime()) {// 登陆
						doIoTApi(function (res) {
							epObj.token = res.data;
							epObj.expireTime = new Date().getTime() + 3600000;
							doIoTApi(function (res) {
								if (res.status === 200) {
									epObj.userInfo = res.data;
									// 企业信息
									doIoTApi(function (res) {
										if (res.status === 200) {
											epObj.info = res.data;
										}
									}, undefined, ioTEnterpriseUrl + epObj.userInfo.enterpriseId, 'get', undefined, epObj.token, true);
								}
							}, undefined, ioTUserUrl, 'get', { creatorId: enterprise.info }, epObj.token, true);
						}, undefined, ioTLoginUrl, 'post', JSON.stringify(enterprise), undefined, true);
					}
				}
			},
			closePopUp: function () {
				let $this = this;
				if (this.ioT) {
					$this = this.ioT;
				}
				$this.setCurItem({
					curItem: null
				});
				$('#trackPopUp').hide();
				$('#trackPopUpLink').empty();
				$(".cesium-selection-wrapper").hide();
				return false;
			},
			videoClosePopUp: function () {
				let $this = this;
				if (this.ioT) {
					$this = this.ioT;
				}
				$this._curVideo = null;
				$('#videoTrackPopUp').hide();
				if ($this.videoObj) {
					// this.videoObj.destroy();
					$this.videoObj = null;
				}
				$this.videoEle.src = '';
				$(".cesium-selection-wrapper").hide();
				return false;
			},

			login: function (options = {}) {
				if (!options) {
					return;
				}
				for (let i = 0; i < options.enterprises.length; i++) {
					let epObj = options.enterprises[i];
					let enterprise = {};
					enterprise.username = epObj.username;
					enterprise.password = epObj.password;
					enterprise.sysName = 'X2';
					// token
					if (!epObj.token || epObj.expireTime < new Date().getTime()) {// 登陆
						doIoTApi(function (res) {
							epObj.token = res.data;
							epObj.expireTime = new Date().getTime() + 3600000;
						}, undefined, ioTLoginUrl, 'post', JSON.stringify(enterprise));
					}

					// 用户信息
					if (!epObj.userInfo) {
						doIoTApi(function (res) {
							if (res.status === 200) {
								epObj.userInfo = res.data;
							}
						}, undefined, ioTUserUrl, 'get', { creatorId: enterprise.info }, epObj.token);
					}

					// 企业信息
					doIoTApi(function (res) {
						if (res.status === 200) {
							epObj.info = res.data;
						}
					}, undefined, ioTEnterpriseUrl + epObj.userInfo.enterpriseId, 'get', undefined, epObj.token, !!epObj.info);
				}
			},

			setCurItem: function (options = {}) {
				if (this._dataTimer) {
					clearInterval(this._dataTimer);
					this._dataTimer = undefined;
				}
				if (!options) {
					return;
				}
				this._curItem = options.curItem;

				if (this._curItem) {
					this.getData({
						ioTData: this._curItem
					})
					if (this._viewer.selectedEntity !== this._curItem.entity) {
						this._viewer.selectedEntity = this._curItem.entity;
					}
				} else {
					this._viewer.selectedEntity = null;
				}
			},

			getData: function (options = {}) {
				if (!options) {
					return;
				}
				let enterprise = options.ioTData;
				if (!enterprise) {
					return;
				}
				// 传感器信息
				doIoTApi(function (res) {
					if (res.status === 200) {
						enterprise.data = res.data.rows;
					}
				}, undefined, ioTSensorUrl, 'get', { creatorId: enterprise.info.id }, enterprise.token);
				this._dataTimer = setInterval(function () {
					doIoTApi(function (res) {
						if (res.status === 200) {
							enterprise.data = res.data.rows;
						}
					}, undefined, ioTSensorUrl, 'get', { creatorId: enterprise.info.id }, enterprise.token, true);
				}, 60000);
			},

			getVideo: function (options = {}) {
				if (!options) {
					return;
				}
				let enterprise = options.ioTData;
				// 传感器信息
				doIoTApi(function (res) {
					if (res.status === 200) {
						options.callback(res);
					}
				}, undefined, ioTVideoUrl, 'get', { enterpriseId: enterprise.info.id }, enterprise.token);
			},
			/**
			 * 设备信息
			 * @param {*} options
			 */
			drawDeviceGraphics: function (options = {}) {
				if (this._viewer && options) {
					this._drawLayer.entities.removeAll();
					this.closePopUp();
					this.videoClosePopUp();
					let $this = this,
						heightArr = [],
						heightIndex = 0,
						_handlers = new Cesium.ScreenSpaceEventHandler(this._viewer.scene.canvas);
					_getHeigthByLonLat();

					// 通过经纬度获得高程
					function _getHeigthByLonLat() {
						if (heightIndex < options.ioTData.length) {
							let positions = Cesium.Cartographic.fromDegrees(options.ioTData[heightIndex].info.longitude, options.ioTData[heightIndex].info.latitude);
							Cesium.when(new Cesium.sampleTerrain($this._viewer.terrainProvider, 13, [positions]), function (updatedPositions) {
								heightArr.push(updatedPositions[0].height);
								heightIndex++;
								_getHeigthByLonLat();
							});
						} else {
							options.ioTData.map((item, index) => {
								if (item.info.longitude && item.info.latitude) {
									let height = heightArr[index] ? heightArr[index] + 9 : 30;
									let position = Cesium.Cartesian3.fromDegrees(item.info.longitude, item.info.latitude, height);
									item.position = position;
									item.entity = $this._drawLayer.entities.add({
										position: position,
										point: { //点
											pixelSize: 5,
											color: Cesium.Color.RED,
											outlineColor: Cesium.Color.WHITE,
											outlineWidth: 2
										},
										label: { //文字标签
											text: item.info.name,
											font: '14pt monospace',
											fillColor: Cesium.Color.YELLOW,
											style: Cesium.LabelStyle.FILL,
											outlineWidth: 2,
											verticalOrigin: Cesium.VerticalOrigin.BOTTOM, //垂直方向以底部来计算标签的位置
											pixelOffset: new Cesium.Cartesian2(0, -35)   //偏移量
										},
										billboard: { //图标
											image: './images/position.png',
											width: 30,
											height: 30,
											pixelOffset: new Cesium.Cartesian2(0, -20)
										}
									});
								}
							});
						}
					};

					// 左击选中
					_handlers.setInputAction(function (event) {
						$this.closePopUp();
						$this.videoClosePopUp();
						let pick = $this._viewer.scene.pick(event.position);
						//选中某模型   pick选中的对象
						if (pick && pick.id) {
							let cartesian = $this._viewer.scene.pickPosition(event.position);
							let cartographic = $this._viewer.scene.globe.ellipsoid.cartesianToCartographic(cartesian)
							let longitude = Cesium.Math.toDegrees(cartographic.longitude);
							let latitude = Cesium.Math.toDegrees(cartographic.latitude);
							options.ioTData.map(item => {
								if (item.info.longitude.toFixed(3) === longitude.toFixed(3) && item.info.latitude.toFixed(3) === latitude.toFixed(3)) {
									$this.setCurItem({
										curItem: item
									});
									$this.showSensorData();
								}
							})
						}
					}, Cesium.ScreenSpaceEventType.LEFT_CLICK);
				}
			}
			,
			/**
			 * 传感器信息
			 * @param {*} options
			 */
			drawSensorGraphics: function (options = {}) {
				if (this._viewer && options) {
					let $this = this,
						heightArr = [],
						heightIndex = 0,
						_handlers = new Cesium.ScreenSpaceEventHandler(this._viewer.scene.canvas);
					_getHeigthByLonLat();

					// 通过经纬度获得高程
					function _getHeigthByLonLat() {
						if (heightIndex < options.sensorData.length) {
							let positions = Cesium.Cartographic.fromDegrees(options.sensorData[heightIndex].longitude, options.sensorData[heightIndex].latitude);
							Cesium.when(new Cesium.sampleTerrain($this._viewer.terrainProvider, 13, [positions]), function (updatedPositions) {
								heightArr.push(updatedPositions[0].height);
								heightIndex++;
								_getHeigthByLonLat();
							});
						} else {
							options.sensorData.map((item, index) => {
								if (item.longitude && item.latitude) {
									let height = heightArr[index] ? heightArr[index] + 9 : 30;
									$this._drawLayer.entities.add({
										position: Cesium.Cartesian3.fromDegrees(item.longitude, item.latitude, height),
										point: { //点
											pixelSize: 5,
											color: Cesium.Color.RED,
											outlineColor: Cesium.Color.WHITE,
											outlineWidth: 2
										},
										label: { //文字标签
											text: item.name,
											font: '14pt monospace',
											fillColor: Cesium.Color.YELLOW,
											style: Cesium.LabelStyle.FILL,
											outlineWidth: 2,
											verticalOrigin: Cesium.VerticalOrigin.BOTTOM, //垂直方向以底部来计算标签的位置
											pixelOffset: new Cesium.Cartesian2(0, -25)   //偏移量
										},
										billboard: { //图标
											image: './images/position.png',
											width: 30,
											height: 30,
											pixelOffset: new Cesium.Cartesian2(0, -20)
										}
									});
								}
							});
						}
					};
				}
			},

			positionPopUp: function (c, element) {
				let x = c.x - (element.width()) / 2;
				let y = c.y - (element.height() + 60);
				element.css('transform', 'translate3d(' + x + 'px, ' + y + 'px, 0)');
			},


			// 创建弹窗
			creatPopup: function (obj) {
				if (!obj || !obj.position || !obj.position.x || !obj.position.y ||
					obj.position.x < 0 || obj.position.x > this.popupEle.clientWidth ||
					obj.position.y < 0 || obj.position.y > this.popupEle.clientHeight) {
					$('#trackPopUp').hide();
					return;
				}
				$(".cesium-selection-wrapper").show();
				$('#trackPopUpLink').empty();
				$('#trackPopUpLink').append(obj.content);

				let c = new Cesium.Cartesian2(obj.position.x, obj.position.y);
				$('#trackPopUp').show();
				this.positionPopUp(c, $('#trackPopUpContent')); // Initial position
			},

			// 创建弹窗
			creatVideoPopup: function (obj) {
				if (!obj || !obj.position || !obj.position.x || !obj.position.y ||
					obj.position.x < 0 || obj.position.x > this.popupEle.clientWidth ||
					obj.position.y < 0 || obj.position.y > this.popupEle.clientHeight) {
					$('#videoTrackPopUp').hide();
					return;
				}
				$(".cesium-selection-wrapper").show();

				let c = new Cesium.Cartesian2(obj.position.x, obj.position.y);
				$('#videoTrackPopUp').show();
				this.positionPopUp(c, $('#videoTrackPopUpContent')); // Initial position
			},

			// 显示数据
			showSensorData: function () {
				if (this._curVideo && !this.videoObj) {
					let url_prefix = 'https://open.ys7.com/ezopen/h5/iframe?'
					let deviceSerial = this._curVideo.serialNum;
					let channelNo = this._curVideo.passNum;
					let verifyCode = this._curVideo.verifyCode;
					let hd = '';
					let subUrl = 'url=ezopen://' + verifyCode + '@open.ys7.com/' + deviceSerial + '/' + channelNo + hd + '.live'
					// let autoPlay = '&autoplay=' + this._curVideo.isAutoPlay;
					let autoPlay = '&autoplay=1';
					let audio = '&audio=' + this._curVideo.isOpenAudio;
					let accessToken = '&accessToken=' + this.token;
					let url = url_prefix + subUrl + autoPlay + accessToken + '&templete=0';
					// var accessToken = this.token;
					// // var deviceSerial = 'E69956177';
					// var deviceSerial = 'F32691349';
					// var channelNo = '1';
					// var hd = '';
					// var url = "ezopen://CUGZDLC@open.ys7.com/" + deviceSerial + "/" + channelNo + ".live";
					// if (hd && hd == 1) {
					//     url = url.replace(".live", ".hd.live");
					// }
					// var env = null;
					// var width = document.documentElement.clientWidth;
					// var height = width * 9 / 16;
					//
					// this.videoObj = new EZUIKit.EZUIPlayer({
					//     id: 'playWind',
					//     autoplay: true,
					//     url: url,
					//     iType: 2,
					//     accessToken: accessToken,
					//     decoderPath: './lib/ezuikit_v3.4',
					//     width: 640,
					//     height: 400,
					//     controls: ['play', 'voice', 'hd', 'fullScreen'],
					//     handleError: (e) => {
					//         console.log('捕获到错误', e)
					//     },
					//     handleSuccess: () => {
					//         console.log("播放成功回调函数，此处可执行播放成功后续动作");
					//     },
					//     env: env
					// });
					let position = this._viewer.scene.cartesianToCanvasCoordinates(this._curVideo.position);
					let obj = { position };
					this.creatVideoPopup(obj);
					if (this.videoEle.src != url) {
						this.videoEle.src = url;
					}
				} else if (this._curItem) {
					let item = this._curItem;
					let position = this._viewer.scene.cartesianToCanvasCoordinates(this._curItem.position);
					let data_content = '';
					for (let i = 0; i < item.data.length; i++) {
						let data = item.data[i];
						data_content += `<tr>
                            <td><b>${data.name}:</b> ${data.lastRecordData} ${data.unit}</td>
                        </tr>`;
					}
					let content = `
                        <table>
                          <tbody>
                        ` + data_content + `
                          </tbody>
                        </table>
                      `;
					let obj = { position, content };
					this.creatPopup(obj);
				}
			}
		}
		return _
	})
		(Cesium);
}
