let analysisHandler = null;

/**
 * 创建交互
 * @param canvas viewer.scene.canvas对象
 * @returns {Cesium.ScreenSpaceEventHandler} 交互对象
 */
function createScreenSpaceEventHandler(canvas) {
    destroyScreenSpaceEventHandler();
    return analysisHandler = new Cesium.ScreenSpaceEventHandler(canvas);
}

/**
 * 删除交互
 */
function destroyScreenSpaceEventHandler() {
    if (analysisHandler) {
        analysisHandler.destroy();
        analysisHandler = null;
    }
}

function formatFloat(src, pos) {
    return Math.round(src * Math.pow(10, pos)) / Math.pow(10, pos);
}

function handleUrl_DemDom(url){
    let length = url.length;
    if(url[length - 1] !== '/') {
        if (url.substring(length - 5) !== '.json') {
            url += '/';
        } else {
            let index = url.lastIndexOf('/');
            if (index > -1) {
                url = url.substring(0, index + 1);
            }
        }
    }
    return url;
}
