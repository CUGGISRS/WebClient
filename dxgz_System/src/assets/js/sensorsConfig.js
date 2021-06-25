export const sensors_config = {
    /**
     * 系统名称
     */
    system_name: '监利市稻虾共作物联网应用系统',

    /**
     * 温度计px范围
     */
    temp_px_range: [0, 64],
    /**
     * 土壤温度范围
     */
    soil_temp_range: [0, 50],
    /**
     * 水温范围
     */
    water_temp_range: [0, 30],
    /**
     * 气温范围
     */
    air_temp_range: [0, 40],

    /**
     * 获得温度计px数值
     * @param val 当前温度数值
     * @param name 传感器名称
     * @returns {string|*}
     */
    get_temp_px_val: function (val, name) {
        let r, pr;
        pr = this.temp_px_range;
        if (name.indexOf('土') !== -1) {
            r = this.soil_temp_range;
        } else if (name.indexOf('水') !== -1) {
            r = this.water_temp_range;
        } else if (name.indexOf('气') !== -1) {
            r = this.air_temp_range;
        } else {
            return 0;
        }
        // console.log("get_px_val:", val);
        return this.get_display_value(val, r, pr).toFixed(2) + 'px';
    },

    /**
     * 照度
     */
    illumination_range: [0, 80000],
    illumination_options: {
        angle: 0.15,
        lineWidth: 0.44,
        radiusScale: 1,
        pointer: {
            length: 0.6,
            strokeWidth: 0.035,
            color: "#ffffff"
        },
        limitMax: false,
        limitMin: false,
        colorStart: "rgba(255,255,255,1)",
        colorStop: "rgba(255,255,255,1)",
        strokeColor: "#000000",
        generateGradient: true,
        highDpiSupport: true
    },
    /**
     * 获得照度仪表初始化选项
     * @param val 照度数值
     * @returns {{radiusScale: number, pointer: {strokeWidth: number, color: string, length: number}, limitMax: boolean, highDpiSupport: boolean, colorStop: string, angle: number, generateGradient: boolean, colorStart: string, limitMin: boolean, strokeColor: string, lineWidth: number}}
     */
    get_illu_options(val) {
        let r = this.illumination_range;
        let cvstr = this.get_display_value(val, r, [0, 255]).toFixed(0);
        let color = 'rgba(' + cvstr + ',' + cvstr + ',' + cvstr + ')';
        this.illumination_options.colorStart = this.illumination_options.colorStop = color;
        return this.illumination_options;
    },

    /**
     * 湿度
     */
    humidity_index: 0,
    humidity_ids: [],
    humidity_alive: true,
    /**
     * 获得湿度元素id
     * @returns {*}
     */
    forHumidityId(value) {
        let humidity_id = 'hum' + this.humidity_index++;
        this.humidity_ids.push({id: humidity_id, val: value});
        return humidity_id;
    },
    humidityInit(echarts) {
        this.humidity_ids.forEach((ele) => {
            let hum = document.getElementById(ele.id);
            // console.log('hum:', hum);
            if (!hum) {
                return;
            }
            if (hum[0]) {
                hum = hum[0];
            }
            let humidity = echarts.init(hum);
            ele.hum = humidity;
            let option = {
                series: [{
                    type: 'liquidFill',
                    radius: '100%',
                    data: [ele.val / 100],
                    shape: 'path://M 100 120 a 140 140 0 1 0 198 0 c -36 -36 -68 -40 -99 -100 c -31 60 -63 64 -99 100',
                    label: {
                        show: false
                    },
                    color: [{
                        type: 'linear',
                        x: 0,
                        y: 1,
                        x2: 0,
                        y2: 0,
                        colorStops: [{
                            offset: 1,
                            color: ['#6a7feb'], // 0% 处的颜色
                        }, {
                            offset: 0,
                            color: ['#27e5f1'], // 100% 处的颜色
                        }],
                        global: false // 缺省为 false
                    }],
                    outline: {
                        show: true,
                        borderDistance: 5,
                        itemStyle: {
                            borderColor: 'rgba(67,209,100,1)',
                            borderWidth: 0
                        }
                    }
                }]
            }
            humidity.setOption(option)
        })
    },
    humidityDestory() {
        this.humidity_ids.forEach((ele) => {
            if (ele.hum) {
                ele.hum.dispose();
            }
        });
        this.humidity_ids.length = 0;
    },

    /**
     * 溶解氧范围
     */
    dissolved_o_range: [3, 10],
    gas_count_range: [1, 27],
    getDissolvedOCount(val) {
        return Math.round(this.get_display_value(val, this.dissolved_o_range, this.gas_count_range));
    },

    /**
     * 二氧化碳范围
     */
    c_dioxide_range: [300, 5500],
    dissolved_o_count_range: [1, 27],
    getCDioxideCount(val) {
        return Math.round(this.get_display_value(val, this.c_dioxide_range, this.gas_count_range));
    },

    /**
     * 判断卡片加载开始
     * @param index 当前加载索引
     * @returns {boolean} 总是返回false
     */
    loadStart(index) {
        if (index > 0) {
            return false;
        }
        this.humidityDestory();
        return false;
    },
    /**
     * 判断卡片加载完成
     * @param index 当前加载索引
     * @param length 总加载长度
     * @param echarts $echarts对象
     * @param nextTick $nextTick对象
     * @returns {boolean} 总是返回false
     */
    loadDone(index, length, echarts, nextTick) {
        if (index < length - 1) {
            return false;
        }
        nextTick(() => {
            this.humidityInit(echarts);
        })
        return false;
    },

    get_display_value(val, r, pr) {
        if (val <= r[0]) {
            return pr[0];
        } else if (val >= r[1]) {
            return pr[1];
        } else {
            return (val - r[0]) / (r[1] - r[0]) * (pr[1] - pr[0]) + pr[0];
        }
    }
}
