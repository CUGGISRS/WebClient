<template>
  <div class="container">
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane label="设备" name="first">
        <div class="container-group">
          <ul>
            <li
              v-for="(item, index) in groupLists"
              :key="item.id"
              @click="changeGroup(item.id, index)"
              class="group-li"
            >
              {{ item.name }}
            </li>
          </ul>
        </div>
      </el-tab-pane>
      <el-tab-pane label="监控" name="second">
        <div class="container-list">
          <ul>
            <li v-for="(item, index) in videoRows" :key="item.id" @click="changeVideo(index)" class="video-li">
              {{ item.title }}
            </li>
          </ul>
        </div>
      </el-tab-pane>
      <el-tab-pane label="分屏" name="third">
        <div class="container-list">
          <!-- <ul>
            <li v-for="(item, index) in videoRows" :key="item.id" @click="changeVideo(index)" class="video-li">
              {{ item.title }}
            </li>
          </ul> -->
          <el-checkbox-group v-model="checkedScreenList" :min="0" :max="4">
            <ul>
              <li v-for="item in videoRows" :key="item.id">
                <el-checkbox-button style="width: 100%; height: 100%" @change="handleCheckedScreen" :label="item.id"
                  >{{ item.title }}
                </el-checkbox-button>
              </li>
            </ul>
          </el-checkbox-group>
        </div>
      </el-tab-pane>
    </el-tabs>
    <div class="container-sensor" v-if="showItem === 'group'">
      <div class="element-container">
        <el-card class="card-container" v-for="(item, index) in sensorLists" :key="index" :style="randomRgb()">
          <div v-if="$sc.loadStart(index)"></div>
          <div class="card-left">
            <div class="visual-content" v-if="item.name.indexOf('温') !== -1">
              <div id="thermometer">
                <div id="temperature" :style="{ height: $sc.get_temp_px_val(item.lastRecordData, item.name) }"></div>
                <div id="graduations"></div>
              </div>
            </div>
            <v-gauge
              v-else-if="item.name.indexOf('照') !== -1"
              width="100"
              height="64"
              :min-value="$sc.illumination_range[0]"
              :max-value="$sc.illumination_range[1]"
              :options="$sc.get_illu_options(item.lastRecordData)"
              :value="item.lastRecordData"
            >
            </v-gauge>
            <div
              v-else-if="item.name.indexOf('湿') !== -1"
              :id="$sc.forHumidityId(item.lastRecordData)"
              style="width: 100%;height: 100%"
            ></div>
            <div v-else-if="item.name.indexOf('二氧') !== -1" class="bubbles">
              <div v-for="count in $sc.getCDioxideCount(item.lastRecordData)" :key="count" class="bubble"></div>
            </div>
            <div v-else-if="item.name.indexOf('氧') !== -1" class="bubbles">
              <div v-for="count in $sc.getDissolvedOCount(item.lastRecordData)" :key="count" class="bubble"></div>
            </div>
            <img v-else class="card-img" :src="getImgUrl(item.number)" />
          </div>
          <div class="card-text">
            <span class="element-name">{{ item.name }}</span>
            <div class="element-data">{{ getEleData(item.lastRecordData) + item.unit }}</div>
          </div>
          <div v-if="$sc.loadDone(index, sensorLists.length, $echarts, $nextTick)"></div>
        </el-card>
      </div>
    </div>
    <div class="container-video" v-else-if="showItem === 'video'" style="position: relative">
      <div class="first-row" style="height:auto;">
        <iframe
          :src="curVideoUrl"
          id="ysopen"
          allowfullscreen
          frameborder="0"
          scrolling="no"
          width="100%"
          height="100%"
          style="display:block"
        >
        </iframe>
      </div>
      <div id="device-controls" class="second-row device-controls"></div>
    </div>
    <div class="screen-container" v-else-if="showItem === 'screen'">
      <div class="screen-item" v-for="item in videoSrcList" :key="item">
        <iframe
          :src="item"
          id="ysopen"
          allowfullscreen
          frameborder="0"
          scrolling="no"
          width="100%"
          height="100%"
          style="display:block"
        >
        </iframe>
      </div>
    </div>
  </div>
</template>

<script>
import '../../assets/css/thermometer.css';
import '../../assets/css/bubble.css';

export default {
  data() {
    return {
      //选择的监控
      checkedScreen: [],
      // 设备列表
      groupLists: [],
      // 传感器列表
      sensorLists: [],
      // 当前设备id
      defaultGroupId: 0,
      // 当前设备索引
      defaultIndex: 0,
      // 设备数据
      deviceData: [],
      // 设备要素
      deviceEle: [],
      // 要素信息
      eleList: [],
      // 定时器
      intervelTime: null,
      // 分页
      activeName: 'first',
      // 展示设备或监控
      showItem: 'group',
      // 监控列表
      videoRows: [],
      //分屏列表
      screenList: [],
      //多选列表
      checkedScreenList: [],
      videoList: [],
      //分屏监控视频url数组
      videoSrcList: [],
      // 单个监控信息
      videoRow: {},
      // 当前监控索引
      defaultVideoIndex: 0,
      curVideoUrl: '',
      deviceCtrlObj: null,
      tokens: new Map(),
    };
  },
  created() {
    this.getAllGroup();
    this.getAllVideo();
    clearInterval(this.intervelTime);
    this.intervelTime = null;
    this.setTimer();
  },
  destroyed() {
    // 每次离开当前界面时，清除定时器
    clearInterval(this.intervelTime);
    this.intervelTime = null;
  },
  methods: {
    // 分页
    handleClick(tab) {
      if (tab.name === 'first') {
        this.showItem = 'group';
      } else if (tab.name === 'second') {
        this.showItem = 'video';
        this.$nextTick(() => {
          this.changeVideo(this.defaultVideoIndex);
        });
      } else if (tab.name === 'third') {
        this.showItem = 'screen';
      }
    },
    // 定时器
    setTimer() {
      if (this.intervelTime == null) {
        this.intervelTime = setInterval(() => {
          this.getAllGroup();
        }, 60000);
      }
    },
    // 获取全部设备
    async getAllGroup() {
      const { data: res } = await this.$http.get('zsSys/DeviceGroup/pageByCId', {
        params: {
          creatorId: window.sessionStorage.getItem('enterpriseId'),
        },
      });
      if (res.status !== 200) return this.$message.error('获取全部设备失败!');
      this.groupLists = res.data.rows;
      setTimeout(() => {
        if (this.groupLists && this.groupLists.length > 0) {
          this.changeGroup(this.groupLists[this.defaultIndex].id, this.defaultIndex);
        }
      }, 0);
    },
    // 根据设备id获取传感器数据
    async getSensorByGroupId(groupId) {
      const { data: res } = await this.$http.get('zsSys/DeviceSensor/pageByGId', {
        params: { groupId },
      });
      if (res.status !== 200) return this.$message.error('获取传感器数据失败!');
      this.sensorLists = res.data.rows;
    },
    // 点击切换设备
    changeGroup(groupId, index) {
      let lis = document.getElementsByClassName('group-li');
      for (let i = 0; i < lis.length; i++) {
        lis[i].classList.remove('activeLi');
        if (index === i) {
          lis[i].classList.add('activeLi');
          this.defaultIndex = index;
          this.defaultGroupId = groupId;
        }
      }
      this.getSensorByGroupId(groupId);
    },
    // 拼接图片url
    getImgUrl(item) {
      if (item != 100 && item != 0) {
        return require('../../assets/img/' + item + '.png');
      }
    },
    // 获取要素数据
    getEleData(value) {
      return value === 32767 ? '-----' : value;
    },
    // 要素数据过滤
    filterData(arr, value) {
      let tmpArr = [];
      for (let item of arr) {
        if (item != value) {
          tmpArr.push(item);
        }
      }
      return tmpArr;
    },
    // 随机背景颜色
    randomRgb() {
      let R = Math.floor(Math.random() * 125 + 20);
      let G = Math.floor(Math.random() * 235 + 20);
      let B = Math.floor(Math.random() * 255);
      return {
        background: 'rgb(' + R + ',' + G + ',' + B + ')',
      };
    },
    // 获取全部监控
    async getAllVideo() {
      this.defaultVideoIndex = 0;
      const { data: res } = await this.$http.get('zsSys/DeviceVideo/pageByEId', {
        params: { enterpriseId: window.sessionStorage.getItem('enterpriseId') },
      });
      if (res.status !== 200) return this.$message.error('获取全部监控失败!');
      this.videoRows = res.data.rows;
      this.videoRows.map((item) => {
        this.screenList.push(item.id);
      });
      this.checkedScreenList = this.screenList.slice(0, 4);
      this.handleCheckedScreen();
      setTimeout(() => {
        this.changeVideo(this.defaultVideoIndex);
      }, 0);
    },
    // 点击切换监控
    changeVideo(index) {
      let lis = document.getElementsByClassName('video-li');
      for (let i = 0; i < lis.length; i++) {
        lis[i].classList.remove('activeLi');
        if (index === i) {
          lis[i].classList.add('activeLi');
          this.defaultVideoIndex = index;
          this.videoRow = this.videoRows[this.defaultVideoIndex];
          if (this.showItem === 'video') {
            this.$nextTick(() => {
              this.setVideo(this.videoRow).then(() => {
                // 设备控制 - start
                this.deviceCtrlObj = new DeviceControls({
                  id: 'device-controls',
                  accessToken: this.videoRow.res.accessToken,
                  deviceSerial: this.videoRow.serialNum,
                  channelNo: this.videoRow.passNum,
                });
                // 设备控制 - end
              });
            });
          }
        }
      }
    },
    //切换分屏
    async handleCheckedScreen() {
      this.videoList = [];
      this.videoSrcList = [];
      this.checkedScreenList.map((item) => {
        this.videoRows.map((data) => {
          if (item == data.id) {
            this.videoList.push(data);
          }
        });
      });
      for (let i = 0; i < this.videoList.length; i++) {
        let item = this.videoList[i];
        await this.setVideo(item);
        this.videoSrcList.push(this.curVideoUrl);
      }
    },
    async setVideo(row, isSplit) {
      // 'https://open.ys7.com/ezopen/h5/iframe_se?url=ezopen://CUGZDLC@open.ys7.com/E69956177/1.live&autoplay=0&audio=1&accessToken=at.9ja1yf10bgc4mz22018231re0rmu54xr-8dvby27ud6-13bt2ra-ctcbgessf&templete=2'
      // console.log('video', row);
      let res = {};
      if (this.tokens.has(row.accessToken)) {
        res.accessToken = this.tokens.get(row.accessToken);
      } else {
        let dataObj = JSON.parse(row.accessToken);
        res = await this.$http3.post(
          'api/lapp/token/get',
          'appKey=' + dataObj.appKey + '&appSecret=' + dataObj.appSecret
        );
        res = res.data;
        if (res && res.data && res.data.accessToken) {
          res = res.data;
          this.tokens.set(row.accessToken, res.accessToken);
        }
      }

      row.res = res;
      let url_prefix = 'https://open.ys7.com/ezopen/h5/iframe_se?';
      if (isSplit) {
        url_prefix = 'https://open.ys7.com/ezopen/h5/iframe?';
      }
      var deviceSerial = row.serialNum;
      var channelNo = row.passNum;
      var verifyCode = row.verifyCode;
      var hd = '';
      var subUrl = 'url=ezopen://' + verifyCode + '@open.ys7.com/' + deviceSerial + '/' + channelNo + hd + '.live';
      var autoPlay = '&autoplay=' + row.isAutoPlay;
      var audio = '&audio=' + row.isOpenAudio;
      var template = '&templete=2';
      if (isSplit) {
        template = '&templete=0';
      }
      var accessToken = '&accessToken=' + res.accessToken;
      var url = url_prefix + subUrl + autoPlay + template + accessToken;
      if (url !== this.curVideoUrl) {
        this.curVideoUrl = url;
        // this.$nextTick(() => {
        //   this.curVideoUrl = url;
        // })
      }
    },
  },
};
</script>

<style lang="less" scoped>
// 容器样式
.container {
  width: 100%;
  height: 100%;
  display: flex;

  // 设备
  .container-group {
    width: 220px;
    height: 100%;

    // 设备栏样式
    ul {
      width: 100%;
      margin: 0;
      padding: 0;
      list-style: none;
      text-align: center;

      // 单个设备样式
      .group-li {
        width: 100%;
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        color: #606266;
        border-bottom: 1px solid #e1e1e1;
        cursor: pointer;
        position: relative;
      }

      // 活跃设备
      .activeLi {
        background-color: rgb(45, 183, 245);
        color: #fff;
        border: 0;
      }
    }

    // 隐藏滚动条
    ul::-webkit-scrollbar {
      width: 0 !important;
      height: 0;
    }
  }

  // 监控列表
  .container-list {
    width: 220px;
    height: 100%;

    // 监控列表栏样式
    ul {
      width: 100%;
      margin: 0;
      padding: 0;
      list-style: none;
      text-align: center;

      // 单个监控样式
      .video-li {
        width: 100%;
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        color: #606266;
        border-bottom: 1px solid #e1e1e1;
        cursor: pointer;
        position: relative;
      }

      // 活跃监控
      .activeLi {
        background-color: rgb(45, 183, 245);
        color: #fff;
        border: 0;
      }
    }

    // 隐藏滚动条
    ul::-webkit-scrollbar {
      width: 0 !important;
      height: 0;
    }
  }

  //分屏样式
  .screen-list {
    width: 220px;
    height: 100%;
  }

  // 数据卡片样式
  .container-sensor {
    width: 100%;
    height: 100%;

    // 检验项数据卡样式
    .element-container {
      width: 100%;
      display: flex;
      flex-wrap: wrap;

      .card-container {
        width: 240px;
        height: 100px;
        box-shadow: none;
        margin: 10px;
        display: flex;
        justify-content: center;
        align-items: center;

        // 卡片图片样式
        .card-img {
          width: 64px;
          height: 64px;

          img {
            width: 100%;
            height: 100%;
          }
        }

        // 卡片数据样式
        .card-text {
          width: 100px;
          height: 64px;
          float: left;
          color: #fff;
          text-align: center;

          .element-name {
            font-size: 16px;
          }

          .element-data {
            font-size: 18px;
            margin-top: 15px;
          }
        }
      }
    }
  }

  // 监控
  .container-video {
    width: 100%;
    height: 100%;
  }

  // 修改滚动条样式
  .container-sensor::-webkit-scrollbar {
    width: 6px !important;
    height: 10px;
  }

  //滑块部分
  .container-sensor::-webkit-scrollbar-thumb {
    height: 50%;
    border-radius: 5px;
    background-color: #bbbfca;
  }
}

.card-left {
  width: 100px;
  height: 64px;
  float: left;
  text-align: center;
}

.visual-content {
  text-align: center;
}

/deep/ .gauge-title {
  display: none;
}

.first-row {
  position: absolute;
  top: 0;
  left: 0;
  right: 208px;
  bottom: 0;
  margin: 0;
  font-size: 0;
}

.second-row {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  width: 208px;
  margin: 0;
  padding: 0;
}

.screen-container {
  width: 100%;
  height: 100%;
}

.screen-item {
  width: 50%;
  height: 50%;
  background: #000;
  box-sizing: border-box;
  float: left;
  overflow: auto;
  border: 2px solid #fff;
}

/deep/ .el-checkbox-button__inner {
  width: 100%;
  border: none;
  margin: 1px 0px;
  border-bottom: 1px solid #e1e1e1;
  background: #f8f8f8;
}
</style>
