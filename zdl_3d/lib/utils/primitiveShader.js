/**
 * 着色器
 */

if (typeof Cesium !== 'undefined') {
    /**
     * 色表纹理的宽度
     * @type {number}
     */
    const transferFunction1D_Width = 1000;
    /**
     * @param viewer  {object} 三维对象
     * @param options {object} 初始化参数
     * @constructor
     */
    Cesium.primitiveShader = (function (Cesium) {
            function _(options) {
                this.vec2 = new Cesium.Cartesian2(0.0, 0.0);
                this._viewer = options.viewer;
                this._density = 0.005;
                this._alpha = 1.0;
                this._cutX = [0.0, 1.0];
                this._cutY = [0.0, 1.0];
                this._cutZ = [0.0, 1.0];
                this._position = null;
                this.createPrimitive(options);
            }

            _.prototype = {
                /**
                 * 创建云图模型原始对象
                 * @param options 参数列表
                 * @returns {*}
                 */
                createPrimitive: function (options) {
                    let positions = [];
                    let xLen = options.volumeData.sizeX * options.volumeData.stepX;
                    let yLen = options.volumeData.sizeY * options.volumeData.stepY;
                    let zLen = options.volumeData.sizeZ * options.volumeData.stepZ;
                    let lon = options.volumeData.startX;
                    let lat = options.volumeData.startY;
                    let alt = options.volumeData.startZ;
                    positions.push(Cesium.Cartesian3.fromDegrees(lon, lat, alt));
                    let o = positions[positions.length - 1];
                    lon += xLen;
                    positions.push(Cesium.Cartesian3.fromDegrees(lon, lat, alt));
                    let os = positions[positions.length - 1];
                    lat += yLen;
                    positions.push(Cesium.Cartesian3.fromDegrees(lon, lat, alt));
                    lon -= xLen;
                    positions.push(Cesium.Cartesian3.fromDegrees(lon, lat, alt));
                    let ot = positions[positions.length - 1];
                    alt += zLen;
                    lat -= yLen;
                    positions.push(Cesium.Cartesian3.fromDegrees(lon, lat, alt));
                    let or = positions[positions.length - 1];
                    lon += xLen;
                    positions.push(Cesium.Cartesian3.fromDegrees(lon, lat, alt));
                    lat += yLen;
                    positions.push(Cesium.Cartesian3.fromDegrees(lon, lat, alt));
                    lon -= xLen;
                    positions.push(Cesium.Cartesian3.fromDegrees(lon, lat, alt));

                    this._position = new Cesium.ConstantPositionProperty({
                        value: new Cesium.Cartesian3(
                            (positions[0].x + positions[6].x) / 2,
                            (positions[0].y + positions[6].y) / 2,
                            (positions[0].z + positions[6].z) / 2,
                        )
                    });

                    let viewer = options.viewer;
                    let tfImg = createTransferFunction1D(options.clrs);
                    let volImg = createVolumeImg(options.volumeData);
                    let positionIndex = [
                        0, 1, 2,
                        0, 2, 3,
                        0, 3, 7,
                        0, 7, 4,
                        1, 5, 6,
                        1, 6, 2,
                        3, 2, 6,
                        3, 6, 7,
                        0, 4, 5,
                        0, 5, 1,
                        4, 7, 6,
                        4, 6, 5,
                    ];
                    let stsr = [
                        0, 0, 0,
                        1, 0, 0,
                        1, 1, 0,
                        0, 1, 0,
                        0, 0, 1,
                        1, 0, 1,
                        1, 1, 1,
                        0, 1, 1,
                    ];
                    let vertexShader = v_shader();
                    let fragShader = f_shader();
                    stsr = new Uint8Array(stsr);//纹理数据
                    positionIndex = new Uint16Array(positionIndex);//顶点索引数据
                    let tempPosition = [];
                    for (var i = 0; i < positions.length; i++) {
                        tempPosition.push(positions[i].x);
                        tempPosition.push(positions[i].y);
                        tempPosition.push(positions[i].z);
                    }
                    positions = new Float64Array(tempPosition);//顶点数据
                    let offsetMat = Cesium.Matrix4.fromArray([
                        1, 0, 0, 0,
                        0, 1, 0, 0,
                        0, 0, 1, 0,
                        -o.x, -o.y, -o.z, 1
                    ]);
                    let posToStrMat = this.getTransMatToStr(o, os, ot, or);
                    let geometry = createGeometry(positions, stsr, positionIndex);//几何体
                    let uniforms = [this._density, this._alpha, ...this._cutX, ...this._cutY, ...this._cutZ, this._isoSurfaceValue];
                    uniforms.push(offsetMat);
                    uniforms.push(posToStrMat);
                    let appearance = createAppearance(vertexShader, fragShader, tfImg, volImg, uniforms);//外观
                    //primitive方式加载
                    return this._primitive = viewer.scene.primitives.add(new Cesium.Primitive({
                        geometryInstances: new Cesium.GeometryInstance({//渲染的几何体
                            geometry: geometry
                        }),
                        appearance: appearance,//外观
                        asynchronous: false
                    }));
                },

                /**
                 * 设置当前云图模型为交互焦点
                 */
                tracked: function () {
                    this._viewer.trackedEntity = this._primitive;
                },

                /**
                 * 计算获得 世界坐标系 到 纹理坐标系 的转换矩阵
                 * @param o 纹理坐标原点在世界坐标系的坐标值
                 * @param os 纹理坐标x轴基向量在世界坐标系的坐标值
                 * @param ot 纹理坐标y轴基向量在世界坐标系的坐标值
                 * @param or 纹理坐标z轴基向量在世界坐标系的坐标值
                 * @returns {Matrix3}
                 */
                getTransMatToStr: function (o, os, ot, or, offsetMat) {
                    let s = new Cesium.Cartesian3();
                    let t = new Cesium.Cartesian3();
                    let r = new Cesium.Cartesian3();
                    Cesium.Cartesian3.subtract(os, o, s);
                    Cesium.Cartesian3.subtract(ot, o, t);
                    Cesium.Cartesian3.subtract(or, o, r);
                    let mat = Cesium.Matrix4.fromArray([
                        s.x, s.y, s.z, 0,
                        t.x, t.y, t.z, 0,
                        r.x, r.y, r.z, 0,
                        0, 0, 0, 1,
                    ]);
                    Cesium.Matrix4.inverse(mat, mat);

                    // Cesium.Matrix4.multiplyByPoint(offsetMat, os, s);
                    // Cesium.Matrix4.multiplyByPoint(offsetMat, ot, t);
                    // Cesium.Matrix4.multiplyByPoint(offsetMat, or, r);
                    // Cesium.Matrix4.multiplyByPoint(mat, s, s);
                    // Cesium.Matrix4.multiplyByPoint(mat, s, t);
                    // Cesium.Matrix4.multiplyByPoint(mat, s, r);
                    return mat;
                }
            }

            Object.defineProperties(_.prototype, {
                density: {
                    get() {
                        return this._density;
                    },
                    set(value) {
                        this._density = value;
                        this._primitive._material.uniforms.Compress_DA = compress([this._density, this._alpha], 0);
                    },
                },
                alpha: {
                    get() {
                        return this._alpha;
                    },
                    set(value) {
                        this._alpha = value;
                        this._primitive._material.uniforms.Compress_DA = compress([this._density, this._alpha], 0);
                    },
                },
                cutMinX: {
                    get() {
                        return this._cutX[0];
                    },
                    set(value) {
                        this._cutX[0] = value;
                        this._primitive._material.uniforms.Compress_CutX = compress(this._cutX, 0);
                    },
                },
                cutMaxX: {
                    get() {
                        return this._cutX[1];
                    },
                    set(value) {
                        this._cutX[1] = value;
                        this._primitive._material.uniforms.Compress_CutX = compress(this._cutX, 0);
                    },
                },
                cutMinY: {
                    get() {
                        return this._cutY[0];
                    },
                    set(value) {
                        this._cutY[0] = value;
                        this._primitive._material.uniforms.Compress_CutY = compress(this._cutY, 0);
                    },
                },
                cutMaxY: {
                    get() {
                        return this._cutY[1];
                    },
                    set(value) {
                        this._cutY[1] = value;
                        this._primitive._material.uniforms.Compress_CutY = compress(this._cutY, 0);
                    },
                },
                cutMinZ: {
                    get() {
                        return this._cutZ[0];
                    },
                    set(value) {
                        this._cutZ[0] = value;
                        this._primitive._material.uniforms.Compress_CutZ = compress(this._cutZ, 0);
                    },
                },
                cutMaxZ: {
                    get() {
                        return this._cutZ[1];
                    },
                    set(value) {
                        this._cutZ[1] = value;
                        this._primitive._material.uniforms.Compress_CutZ = compress(this._cutZ, 0);
                    },
                },
                isoSurfaceValue: {
                    get() {
                        return this._isoSurfaceValue;
                    },
                    set(value) {
                        this._isoSurfaceValue = value;
                        this._primitive._material.uniforms.u_IsoSurfaceValue = this._isoSurfaceValue;
                    },
                },
                position: {
                    get() {
                        return this._position;
                    }
                },
            });

            let vec2 = new Cesium.Cartesian2(0.0, 0.0);

            function compress(valArr, strIdx) {
                Cesium.Cartesian2.fromArray(valArr, strIdx, vec2);
                return Cesium.AttributeCompression.compressTextureCoordinates(vec2);
            }

            //构建几何体
            function createGeometry(positions, sts, positionIndex) {
                return new Cesium.Geometry({
                    attributes: {//几何顶点属性
                        position: new Cesium.GeometryAttribute({
                            componentDatatype: Cesium.ComponentDatatype.DOUBLE,//数据类型
                            componentsPerAttribute: 3,//定义几个为一组
                            values: positions//坐标值
                        }),
                        str: new Cesium.GeometryAttribute({
                            componentDatatype: Cesium.ComponentDatatype.FLOAT,//数据类型
                            componentsPerAttribute: 3,//定义几个为一组
                            values: sts//坐标值
                        })
                    },
                    indices: positionIndex,//顶点索引
                    primitiveType: Cesium.PrimitiveType.TRIANGLES,//图元类型
                    boundingSphere: Cesium.BoundingSphere.fromVertices(positions)//包围球
                });
            }

            function createAppearance(vertexShader, fragShader, tfImg, volImg, uniforms) {
                let compressVal_DA = compress(uniforms, 0);
                let compressVal_CutX = compress(uniforms, 2);
                let compressVal_CutY = compress(uniforms, 4);
                let compressVal_CutZ = compress(uniforms, 6);
                let isoSurfaceValue = uniforms[8];
                let offsetMat = uniforms[9];
                let posToStrMat = uniforms[10];
                return new Cesium.Appearance({
                    attributeName: 'normal',
                    // perInstanceAttribute: false,
                    // glslDatatype: 'vec4',
                    material: new Cesium.Material({
                        // translucent: false,//显示不为半透明
                        fabric: {
                            uniforms: {
                                tfImage: tfImg,
                                volImage: volImg,
                                Compress_DA: compressVal_DA,
                                Compress_CutX: compressVal_CutX,
                                Compress_CutY: compressVal_CutY,
                                Compress_CutZ: compressVal_CutZ,
                                u_IsoSurfaceValue: isoSurfaceValue,
                                u_OffsetMat: Cesium.Matrix4.pack(offsetMat, []),
                                u_PosToStrMat: Cesium.Matrix4.pack(posToStrMat, []),
                            },
                            source:
                                "float Alpha;\n" +
                                "float Density;\n" +
                                "vec2 CutX;\n" +
                                "vec2 CutY;\n" +
                                "vec2 CutZ;\n" +
                                "float IsoSurfaceValue;\n" +
                                "mat4 OffsetMat;\n" +
                                "mat4 PosToStrMat;\n" +
                                "\n" +
                                "czm_material czm_getMaterial(czm_materialInput materialInput) { \n" +
                                "    czm_material material = czm_getDefaultMaterial(materialInput); \n" +
                                "    return material; \n" +
                                "} \n" +
                                "\n" +
                                "float czm_getValue(vec3 str) { \n" +
                                "    return texture3D(volImage, str).r; \n" +
                                "} \n" +
                                "vec4 czm_getColor(float volValue) { \n" +
                                "    return texture2D(tfImage, vec2(volValue, 0.0)); \n" +
                                "} \n" +
                                "\n" +
                                "void getCutAndAlphaValue() {\n" +
                                "    vec2 DA = czm_decompressTextureCoordinates(Compress_DA); \n" +
                                "    CutX = czm_decompressTextureCoordinates(Compress_CutX); \n" +
                                "    CutY = czm_decompressTextureCoordinates(Compress_CutY); \n" +
                                "    CutZ = czm_decompressTextureCoordinates(Compress_CutZ); \n" +
                                "    Density = DA.x; \n" +
                                "    Alpha = DA.y; \n" +
                                "    IsoSurfaceValue = u_IsoSurfaceValue; \n" +
                                "    OffsetMat = u_OffsetMat; \n" +
                                "    PosToStrMat = u_PosToStrMat; \n" +
                                "} \n",
                        },
                    }),
                    closed: true,
                    renderState: {
                        // blending: Object.freeze({
                        //     enabled: true,
                        //     // color: {red: 1.0, green: 1.0, blue: 1.0, alpha: 1.0},
                        //     equationRgb: Cesium.BlendEquation.ADD,
                        //     equationAlpha: Cesium.BlendEquation.ADD,
                        //     functionSourceRgb: Cesium.BlendFunction.SOURCE_ALPHA,
                        //     functionSourceAlpha: Cesium.BlendFunction.ONE_MINUS_SOURCE_ALPHA,
                        //     functionDestinationRgb: Cesium.BlendFunction.SOURCE_ALPHA,
                        //     functionDestinationAlpha: Cesium.BlendFunction.ONE_MINUS_SOURCE_ALPHA,
                        // }),
                        blending: Cesium.BlendingState.PRE_MULTIPLIED_ALPHA_BLEND,//使用Alpha混合功能启用混合
                        depthTest: {enabled: true},//深度检测
                        depthMask: true,//将深度值写入深度缓冲区
                        cull: {
                            enabled: true,
                            face: Cesium.CullFace.BACK,
                        },//只显示反面
                    },
                    fragmentShaderSource: fragShader,//片段着色器
                    vertexShaderSource: vertexShader//顶点着色器
                });
            }

            function v_shader() {
                return '' +
                    'attribute vec3 position3DHigh;\n' +
                    'attribute vec3 position3DLow;\n' +
                    'attribute float batchId;\n' +
                    // 'attribute vec3 normal;\n' +
                    // 'attribute vec3 tangent;\n' +
                    // 'attribute vec3 bitangent;\n' +
                    'attribute vec3 str;\n' +
                    '\n' +
                    'varying vec3 v_str;\n' +
                    "\n" +
                    "varying vec3 v_positionEC;\n" +
                    // "varying vec3 v_normalEC;\n" +
                    // "varying vec3 v_tangentEC;\n" +
                    // "varying vec3 v_bitangentEC;\n" +
                    '\n' +
                    'void main() {\n' +
                    '    vec4 p = czm_computePosition();\n' +
                    '    gl_Position = czm_modelViewProjectionRelativeToEye * p;\n' +
                    '    v_str = str;\n' +
                    "\n" +
                    "    vec3 v_positionEC = (czm_modelViewRelativeToEye * gl_Position).xyz;      // position in eye coordinates\n" +
                    // "    vec3 v_normalEC = czm_normal * normal;                         // normal in eye coordinates\n" +
                    // "    vec3 v_tangentEC = czm_normal * tangent;                       // tangent in eye coordinates\n" +
                    // "    vec3 v_bitangentEC = czm_normal * bitangent;                   // bitangent in eye coordinates\n" +
                    '}\n';
            }

            function f_shader() {
                return '' +
                    'varying vec3 v_str;\n' +
                    "varying vec4 cameraPos;\n" +
                    "varying vec3 v_positionEC;\n" +
                    // "varying vec3 v_normalEC;\n" +
                    // "varying vec3 v_tangentEC;\n" +
                    // "varying vec3 v_bitangentEC;\n" +
                    '\n' +
                    'void main() {\n' +
                    "    float mistake = 0.00001;\n" +
                    "    vec4 cameraPos = czm_inverseModelView * vec4(0,0,0,1);\n" +
                    '    vec4 fragColor = vec4(0.0, 0.0, 0.0, 1.0); \n' +
                    '    float AlphaFuncValue = 1.0;\n' +
                    '    vec3 positionToEyeEC = -v_positionEC;\n' +
                    // '    mat3 tangentToEyeMatrix = czm_tangentToEyeSpaceMatrix(v_normalEC, v_tangentEC, v_bitangentEC);\n' +
                    // '    vec3 normalEC = normalize(v_normalEC);\n' +
                    '#ifdef FACE_FORWARD\n' +
                    // '    normalEC = faceforward(normalEC, vec3(0.0, 0.0, 1.0), -normalEC);\n' +
                    '#endif\n' +
                    '    czm_materialInput materialInput;\n' +
                    // '    materialInput.normalEC = normalEC;\n' +
                    // '    materialInput.tangentToEyeMatrix = tangentToEyeMatrix;\n' +
                    '    materialInput.positionToEyeEC = positionToEyeEC;\n' +
                    '    materialInput.str = v_str;\n' +
                    "    czm_material material = czm_getDefaultMaterial(materialInput); \n" +
                    "    vec4 baseColor = vec4(material.diffuse, 1.0);\n" +
                    '    getCutAndAlphaValue();\n' +
                    "\n" +
                    '    vec3 t0 = v_str;\n' +
                    // '    fragColor.xy = CutX; \n' +
                    // '    fragColor = czm_getColor(czm_getValue(t0)); \n' +
                    // '    fragColor = vec4(Alpha,Alpha,Alpha,1.0); \n' +
                    '    vec3 te = (PosToStrMat * (OffsetMat * cameraPos)).xyz;\n' +
                    '\n' +
                    '    if (te.x>=CutX.x && te.x<=CutX.y &&\n' +
                    '        te.y>=CutY.x && te.y<=CutY.y &&\n' +
                    '        te.z>=CutZ.x && te.z<=CutZ.y)\n' +
                    '    {\n' +
                    '        // do nothing... te inside volume\n' +
                    '    }\n' +
                    '    else\n' +
                    '    {\n' +
                    '        if (te.x<CutX.x)\n' +
                    '        {\n' +
                    '            float r = (CutX.x-te.x) / (t0.x-te.x);\n' +
                    '            te = te + (t0-te)*r;\n' +
                    // '            fragColor.x = 1.0;\n' +
                    '        }\n' +
                    '\n' +
                    '        if (te.x>CutX.y)\n' +
                    '        {\n' +
                    '            float r = (CutX.y-te.x) / (t0.x-te.x);\n' +
                    '            te = te + (t0-te)*r;\n' +
                    // '            fragColor.x = 1.0;\n' +
                    '        }\n' +
                    '\n' +
                    '        if (te.y<CutY.x)\n' +
                    '        {\n' +
                    '            float r = (CutY.x-te.y) / (t0.y-te.y);\n' +
                    '            te = te + (t0-te)*r;\n' +
                    // '            fragColor.y = 1.0;\n' +
                    '        }\n' +
                    '\n' +
                    '        if (te.y>CutY.y)\n' +
                    '        {\n' +
                    '            float r = (CutY.y-te.y) / (t0.y-te.y);\n' +
                    '            te = te + (t0-te)*r;\n' +
                    // '            fragColor.y = 1.0;\n' +
                    '        }\n' +
                    '\n' +
                    '        if (te.z<CutZ.x)\n' +
                    '        {\n' +
                    '            float r = (CutZ.x-te.z) / (t0.z-te.z);\n' +
                    '            te = te + (t0-te)*r;\n' +
                    // '            fragColor.z = 1.0;\n' +
                    '        }\n' +
                    '\n' +
                    '        if (te.z>CutZ.y)\n' +
                    '        {\n' +
                    '            float r = (CutZ.y-te.z) / (t0.z-te.z);\n' +
                    '            te = te + (t0-te)*r;\n' +
                    // '            fragColor.z = 1.0;\n' +
                    '        }\n' +
                    '    }\n' +
                    '\n' +
                    // '    fragColor = czm_getColor(czm_getValue(te)); \n' +
                    '    const highp int max_iteratrions = 16;\n' +
                    '    highp int num_iterations = 2;\n' +
                    '    if (Alpha<1.0) {\n' +
                    '        num_iterations = int(ceil(length(te-t0)/Density));\n' +
                    '    }\n' +
                    '    if (num_iterations<2) {\n' +
                    '        num_iterations = 2;\n' +
                    '    }\n' +
                    '    else if (num_iterations>max_iteratrions) {\n' +
                    '        num_iterations = max_iteratrions;\n' +
                    '    }\n' +
                    '    #ifdef NVIDIA_Corporation\n' +
                    '    // Recent NVidia drivers have a bug in length() where it throws nan for some values of input into length() so catch these\n' +
                    '    else if (num_iterations!=num_iterations) {\n' +
                    '        num_iterations = max_iteratrions;\n' +
                    '    }\n' +
                    '    #endif\n' +
                    '\n' +
                    '    vec3 deltaTexCoord=(te-t0).xyz/float(num_iterations-1);\n' +
                    '    vec3 texCoord = t0.xyz;\n' +
                    '\n' +
                    '    vec4 color;\n' +
                    '    float p = 0.5;\n' +
                    '    int n = 0;\n' +
                    '    while(num_iterations>0)\n' +
                    '    {\n' +
                    '        if (texCoord.x<CutX.x-mistake || texCoord.x>CutX.y+mistake ||\n' +
                    '            texCoord.y<CutY.x-mistake || texCoord.y>CutY.y+mistake ||\n' +
                    '            texCoord.z<CutZ.x-mistake || texCoord.z>CutZ.y+mistake)\n' +
                    '        {\n' +
                    '            texCoord += deltaTexCoord; \n' +
                    '            --num_iterations;\n' +
                    '            continue;\n' +
                    '        }\n' +
                    '        else\n' +
                    '        {\n' +
                    '            float value = czm_getValue(texCoord);\n' +
                    '            color = czm_getColor(value);\n' +
                    '        }\n' +
                    '\n' +
                    '        float r = color.w*Alpha;\n' +
                    '        if(r>p)\n' +
                    '            r *= r*r;\n' +
                    '        else\n' +
                    '            r *= p*p;\n' +
                    '        {\n' +
                    // '            fragColor = fragColor*(1.0-r)+color*r;\n' +
                    '            fragColor.xyz = fragColor.xyz*(1.0-r)+color.xyz*r;\n' +
                    '            fragColor.w += r;\n' +
                    '        }\n' +
                    '        if (fragColor.w<color.w)\n' +
                    '        {\n' +
                    '            fragColor = color;\n' +
                    '        }\n' +
                    // '        color.w *= Alpha;\n' +
                    // '        float fs = 1.0 - color.w;\n' +
                    // '        float fd = 1.0 - fragColor.w;\n' +
                    // '        fragColor.xyz = fragColor.xyz * fs + color.xyz * color.w * fd;\n' +
                    // '        fragColor.w = fragColor.w * fs + color.w * fd;\n' +
                    '\n' +
                    '        texCoord += deltaTexCoord; \n' +
                    '        --num_iterations;\n' +
                    '    }\n' +
                    '\n' +
                    // '    fragColor.w *= Alpha;\n' +
                    // '    fragColor.xyz /= fragColor.w;\n' +
                    '    if (fragColor.w>1.0) fragColor.w = 1.0;\n' +
                    '\n' +
                    // '    fragColor *= baseColor;\n' +
                    // '\n' +
                    // '    if (fragColor.w<AlphaFuncValue) discard;\n' +
                    // '\n' +
                    '\n' +
                    '    if (fragColor.r < 0.01 && fragColor.g < 0.01 && fragColor.b < 0.01) discard;\n' +
                    '\n' +
                    '    gl_FragColor = fragColor;\n' +
                    '}\n';
            }

            /**
             * 创建色表纹理图像
             * @param clrs 用户设置的色表信息
             * @returns {HTMLImageElement}
             */
            function createTransferFunction1D(clrs) {
                let canvas = document.createElement('canvas');
                canvas.width = transferFunction1D_Width;
                canvas.height = 1;
                let cxt = canvas.getContext('2d');
                let startClr = {};
                let endClr = {};
                for (let i = 0; i < clrs.length; i++) {
                    if (i === 0) {
                        startClr = getClrObj(clrs[i]);
                        continue;
                    }
                    endClr = getClrObj(clrs[i]);
                    let deltaClr = getDeltaObj(startClr, endClr);
                    for (let j = 0; j < deltaClr.length; j++) {
                        let pos = startClr.pos + j;
                        let r = (startClr.r + deltaClr.dr * j).toFixed(0);
                        let g = (startClr.g + deltaClr.dg * j).toFixed(0);
                        let b = (startClr.b + deltaClr.db * j).toFixed(0);
                        let a = (startClr.a + deltaClr.da * j).toFixed(2);
                        cxt.fillStyle = 'rgba(' + r + ',' + g + ',' + b + ',' + a + ')';
                        cxt.fillRect(pos, 0, 1, 1);
                    }
                    startClr = endClr;
                }
                console.log(canvas.toDataURL());
                return canvas;
            }

            /**
             * 创建体数据纹理图像
             * @param data
             * @returns {HTMLImageElement}
             */
            function createVolumeImg(data) {
                let canvas = {
                    isTex3D: true,
                    width: data.sizeX,
                    height: data.sizeY,
                    depth: data.sizeZ,
                    pixelFormat: WebGL2RenderingContext.RED,
                    pixelDatatype: Cesium.PixelDatatype.FLOAT,
                    internalFormat: WebGL2RenderingContext.R32F,
                    // arrayBufferView: new Float32Array(data.data),
                    arrayBufferView: new Float32Array(data.sizeX * data.sizeY * data.sizeZ),
                };
                for (let i = 0; i < data.sizeZ * data.sizeY * data.sizeX; i++) {
                    canvas.arrayBufferView[i] = data.data[i];
                }
                return canvas;
            }

            /**
             * 获得色表中单一颜色信息
             * @param clr 色表中单一颜色信息
             * @returns {{a: *, r: number, b: number, pos: number, g: number}}
             */
            function getClrObj(clr) {
                let r = (clr.clr[0] * 255);
                let g = (clr.clr[1] * 255);
                let b = (clr.clr[2] * 255);
                let a = clr.clr[3];
                let pos = clr.val * transferFunction1D_Width;
                if (pos < 0) {
                    pos = 0;
                } else if (pos > transferFunction1D_Width) {
                    pos = transferFunction1D_Width;
                }
                return {r, g, b, a, pos};
            }

            /**
             * 获得两个单一颜色信息之间的步进信息
             * @param clr0 起始颜色信息
             * @param clr1 种植颜色信息
             * @returns {{dg: number, length: number, da: number, dr: number, db: number}}
             */
            function getDeltaObj(clr0, clr1) {
                let length = clr1.pos - clr0.pos;
                let dr = (clr1.r - clr0.r) / length;
                let dg = (clr1.g - clr0.g) / length;
                let db = (clr1.b - clr0.b) / length;
                let da = (clr1.a - clr0.a) / length;
                return {dr, dg, db, da, length};
            }

            return _
        }
    )
    (Cesium);
}
