class Weather {
    static _viewer = null;
    static _snowFlag = null;
    static _rainFlag = null;
    static _fogFlag = null;

    static _snowStage = null;
    static _rainStage = null;
    static _fogStage = null;

    constructor(viewer) {
        Weather._viewer = viewer;
    }

    showFogWeather(enabled, value) {
        if (enabled === true && Weather._fogFlag === null) {
            Weather._fogFlag = true;
            let fogValue = value || 0.3;

            let fogfs = `
            uniform sampler2D colorTexture;
                uniform sampler2D depthTexture;
                varying vec2 v_textureCoordinates;
                void main(void) {
                    vec4 origcolor=texture2D(colorTexture, v_textureCoordinates);
                    vec4 fogcolor=vec4(0.8,0.8,0.8,0.5);
                    float depth = czm_readDepth(depthTexture, v_textureCoordinates);
                    vec4 depthcolor=texture2D(depthTexture, v_textureCoordinates);
                    float f=(depthcolor.r-0.22)/0.2;
                    if(f<0.0) f=0.0;
                    else if(f>1.0) f=${fogValue};
                    gl_FragColor = mix(origcolor,fogcolor,f);
                }
            `;
            Weather._fogStage = Weather._viewer.scene.postProcessStages.add(new Cesium.PostProcessStage({
                "name": "fog",
                //sampleMode:PostProcessStageSampleMode.LINEAR,
                fragmentShader: fogfs
            }));

            return;
        }

        if (enabled && Weather._fogFlag === true) {
            Weather._fogStage.enabled = true;
        } else if (!enabled && Weather._fogFlag === true) {
            Weather._fogStage.enabled = false;
        }

    }

    removeFogWeather() {
        if (Weather._fogStage) {
            Weather._viewer.scene.postProcessStages.remove(Weather._fogStage);
            Weather._fogFlag = null;
        }
    }

    showSnowWeather(enabled, value) {
        if (enabled === true && Weather._snowFlag === null) {
            Weather._snowFlag = true;
            let snowValue = value || 0.3;
            snowValue = snowValue + '';

            //调场景明暗 gl_FragColor = mix(texture2D(colorTexture, v_textureCoordinates), vec4(finalColor,1), 0.4)
            let snowfs = `
                uniform sampler2D colorTexture;
                varying vec2 v_textureCoordinates;
                float snow(vec2 uv,float scale) {
                    float time = czm_frameNumber / 60.0;
                    float w=smoothstep(1.,0.,-uv.y*(scale/10.));
                    if(w<.1) return 0.;
                    uv+=time/scale;
                    uv.y+=time*2./scale;
                    uv.x+=sin(uv.y+time*.5)/scale;
                    uv*=scale;
                    vec2 s=floor(uv),f=fract(uv),p;
                    float k=3.,d;
                    p=.5+.35*sin(11.*fract(sin((s+p+scale)*mat2(7,3,6,5))*5.))-f;
                    d=length(p);k=min(d,k);
                    k=smoothstep(0.,k,sin(f.x+f.y)*0.01);
                    return k*w;
                }
                void main(void){
                    vec2 resolution = czm_viewport.zw;
                    vec2 uv=(gl_FragCoord.xy*2.-resolution.xy)/min(resolution.x,resolution.y);
                    vec3 finalColor=vec3(0);
                    float c = 0.0;
                    c+=snow(uv,30.)*.0;
                    c+=snow(uv,20.)*.0;
                    c+=snow(uv,15.)*.0;
                    c+=snow(uv,10.);
                    c+=snow(uv,8.);
                    c+=snow(uv,6.);
                    c+=snow(uv,5.);
                    finalColor=(vec3(c));
                    gl_FragColor = mix(texture2D(colorTexture, v_textureCoordinates), vec4(finalColor,1), ${snowValue});
                }
            `;
            Weather._snowStage = Weather._viewer.scene.postProcessStages.add(new Cesium.PostProcessStage({
                "name": "snow",
                fragmentShader: snowfs
            }));

            return;
        }

        if (enabled && Weather._snowFlag === true) {
            Weather._snowStage.enabled = true;
        } else if (!enabled && Weather._snowFlag === true) {
            Weather._snowStage.enabled = false;
        }
    }

    removeSnowWeather() {
        if (Weather._snowStage) {
            Weather._viewer.scene.postProcessStages.remove(Weather._snowStage);
            Weather._snowFlag = null;
        }
    }

    showRainWeather(enabled, value) {
        if (enabled === true && Weather._rainFlag === null) {
            Weather._rainFlag = true;
            let rainValue = value || '180.0';
            let rainSize = '4.0';
            let rainLength = '2.0';
            if(rainValue<200) {
                rainSize = '8.0';
                rainLength = '4.0';
            } else if(rainValue>=200 && rainValue <600) {
                rainSize = '4.0';
                rainLength = '2.0';
            } else if(rainValue>=600) {
                rainSize = '2.0';
                rainLength = '1.0';
            }
            rainValue = rainValue + '.';

            //  time可以调雨速  可调大小float v=1.-sin(hash(floor(uv.x*100.))*4.);
            //  调雨的倾斜度float a=-.3;
            //  调雨滴长短uv*=length(uv+vec2(0,4.9))*.3+2.;
            let rainfs = `
                uniform sampler2D colorTexture;
                varying vec2 v_textureCoordinates;
                float hash(float x){
                    return fract(sin(x*133.3)*13.13);
                }
                void main(void){
                    float time = czm_frameNumber / ${rainValue};
                    vec2 resolution = czm_viewport.zw;
                    vec2 uv=(gl_FragCoord.xy*2.-resolution.xy)/min(resolution.x,resolution.y);
                    vec3 c=vec3(.6,.7,.8);
                    float a=-.3;
                    float si=sin(a),co=cos(a);
                    uv*=mat2(co,-si,si,co);
                    uv*=length(uv+vec2(0,4.9))*.3+${rainLength};
                    float v=1.-sin(hash(floor(uv.x*100.))*${rainSize});
                    float b=clamp(abs(sin(20.*time*v+uv.y*(5./(2.+v))))-.95,0.,1.)*20.;
                    c*=v*b;
                    gl_FragColor = mix(texture2D(colorTexture, v_textureCoordinates), vec4(c,1), 0.3);
                }
            `;

            Weather._rainStage = Weather._viewer.scene.postProcessStages.add(new Cesium.PostProcessStage({
                "name": "rain",
                fragmentShader: rainfs
            }));

            return;
        }

        if (enabled && Weather._rainFlag === true) {
            Weather._rainStage.enabled = true;
        } else if (!enabled && Weather._rainFlag === true) {
            Weather._rainStage.enabled = false;
        }
    }

    removeRainWeather() {
        if (Weather._rainStage) {
            Weather._viewer.scene.postProcessStages.remove(Weather._rainStage);
            Weather._rainFlag = null;
        }
    }
}