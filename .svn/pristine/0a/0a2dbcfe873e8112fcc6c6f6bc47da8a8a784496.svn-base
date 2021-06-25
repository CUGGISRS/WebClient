/**
 * 天际线分析
 */
if (typeof Cesium !== 'undefined') {
  /**
  * @param viewer  {object} 三维对象
  * @param options {object} 初始化参数
  * @constructor
  */
  Cesium.SkylineAnalysis = (function (Cesium) {
    /**
     * 绘制对象
     * @param viewer
     * @param options
     * @constructor
     */
    function _(viewer) {
      if (viewer && viewer instanceof Cesium.Viewer) {
        this._viewer = viewer;
        this.hasDisplay = false;
      }
    }
    _.prototype = {
      /**
       * 天际线分析
       * @param {*} options
       */
      drawSkylineGraphics: function (options = {}) {
        if (this._viewer && options) {
          $this = this;
          let collection = $this._viewer.scene.postProcessStages;
          if ($this.hasDisplay) {
            collection.removeAll();
            return;
          }
          let edgeDetection = Cesium.PostProcessStageLibrary.createEdgeDetectionStage();
          let fs1 = `
            uniform sampler2D colorTexture;
            uniform sampler2D depthTexture;
            varying vec2 v_textureCoordinates;
            void main(void) {
              float depth = czm_readDepth(depthTexture, v_textureCoordinates);
              vec4 color = texture2D(colorTexture, v_textureCoordinates);
              if(depth < 1.0 - 0.000001) {
                gl_FragColor = color;
              } else {
                gl_FragColor = vec4(1.0,0.0,0.0,1.0);
              }
            }
          `;
          let postProccessStage = new Cesium.PostProcessStage({
            name: 'czm_skylinetemp',
            fragmentShader: fs1
          });
          let fs2 = `
            uniform sampler2D colorTexture;
            uniform sampler2D redTexture;
            uniform sampler2D silhouetteTexture;
            varying vec2 v_textureCoordinates;
            void main(void) {
              vec4 redcolor=texture2D(redTexture, v_textureCoordinates);
              vec4 silhouetteColor = texture2D(silhouetteTexture, v_textureCoordinates);
              vec4 color = texture2D(colorTexture, v_textureCoordinates);
              if(redcolor.r == 1.0) {
                gl_FragColor = mix(color, vec4(1.0,0.0,0.0,1.0), silhouetteColor.a);
              } else {
                gl_FragColor = color;
              }
            }
          `;
          let postProccessStage2 = new Cesium.PostProcessStage({
            name: 'czm_skylinetemp2',
            fragmentShader: fs2,
            uniforms: {
              redTexture: postProccessStage.name,
              silhouetteTexture: edgeDetection.name
            }
          });
          let postProccessStage3 = new Cesium.PostProcessStageComposite({
            name: 'czm_skyline',
            stages: [edgeDetection, postProccessStage, postProccessStage2],
            inputPreviousStageTexture: false,
            uniforms: edgeDetection.uniforms
          });
          collection.add(postProccessStage3);
        }
      }
    }
    return _
  })(Cesium);
}
