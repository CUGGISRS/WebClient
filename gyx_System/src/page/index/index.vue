<template>
  <div class="home_index">
    <!--新闻播报-->

    <!-- 轮播图 -->
    <div class="content">
      <div class="block">
        <el-carousel>
          <el-carousel-item v-for="item in imgData" :key="item">
            <img :src="item.url" alt />
          </el-carousel-item>
        </el-carousel>
      </div>
      <!-- 播报栏 -->
      <div class="list">
        <div class="lists">
          <span class="lists_a">农业要闻</span>
          <span class="lists_b">更多...</span>
        </div>
        <ul class="list_content">
          <li v-for="(item,index) in list " :key="index">
            <p>[{{item.a}}]</p>
            <p>{{item.b}}</p>
            <p>{{item.c}}</p>
          </li>
        </ul>
      </div>
    </div>

    <!--采购 -->
    <div class="msgs">
      <div class="msg">
        <div class="msgtitle">
          <ul>
            <li>供应需求</li>
            <li>求购信息</li>
            <li>我要发布</li>
          </ul>
          <p>更多...</p>
        </div>
        <el-table :data="tableData" stripe style="width: 100%">
          <el-table-column prop="title" label="标题" width="200"></el-table-column>
          <el-table-column prop="name" label="联系人" width="90"></el-table-column>
          <el-table-column prop="phone" label="电话" width="120"></el-table-column>
          <el-table-column prop="address" label="地址"></el-table-column>
          <el-table-column prop="date" label="日期" width="100"></el-table-column>
        </el-table>
      </div>
      <!-- 专家 -->
      <div class="expert">
        <img src="../../assets/image/zhuanjia.jpg" alt />
      </div>
    </div>

    <!-- 横幅 -->
    <div class="banner">
      <img src="../../assets/image/banner.jpg" alt />
      <!-- 行情榜单 -->
      <div class="market">
        <trend></trend>
      </div>
    </div>
    <!-- 百科气象 -->
    <div class="agroScene">
      <!-- 播报栏 -->
      <div class="agro">
        <div class="agros">
          <span class="agro_a">农业百科</span>
          <span class="agro_b">更多...</span>
        </div>
        <ul class="agro_content">
          <li v-for="(item,index) in agro " :key="index">
            <p>[{{item.a}}]</p>
            <p>{{item.b}}</p>
            <p>{{item.c}}</p>
          </li>
        </ul>
      </div>
      <div class="scene">
        <div class="scenes">
          <span class="scene_a">气象信息</span>
          <span class="scene_b"></span>
        </div>
      </div>
    </div>
    <!-- 农业视频 -->
    <div class="video">
      <!--  -->
      <div class="tvd">
        <div class="tvds">
          <span class="tvds_a">农业百科</span>
          <span class="tvds_b">更多...</span>
        </div>
        <div class="dvd">66</div>
        <div class="tvdList">
          <ul class="tvds_content">
            <li v-for="(item,index) in tvds " :key="index">
              <p>[{{item.a}}]</p>
              <p>{{item.b}}</p>
              <p>{{item.c}}</p>
            </li>
          </ul>
        </div>
      </div>

      <!--  -->
      <div class="deo">问答</div>
    </div>

    <div class="aaa">
      <img src="../../assets/image/banner.jpg" alt />
      <!-- 行情榜单 -->
    </div>
  </div>
</template>

    <script>
import trend from "@/page/home/trend.vue";
export default {
  name: "index",
  props: {},
  data() {
    return {
      imgData: [
        { url: require("../../assets/image/1.jpg") },
        { url: require("../../assets/image/1.jpg") },
        { url: require("../../assets/image/1.jpg") },
        { url: require("../../assets/image/1.jpg") },
      ],
      tableData: [
        {
          title: "油麦菜",
          name: "王小虎",
          phone: "18710186144",
          address: "上海市普陀区",
          date: "2016-05-02",
        },
        {
          title: "油麦菜",
          name: "王小虎",
          phone: "18710186144",
          address: "上海市普陀区",
          date: "2016-05-02",
        },
        {
          title: "油麦菜",
          name: "王小虎",
          phone: "18710186144",
          address: "上海市普陀区",
          date: "2016-05-02",
        },
        {
          title: "油麦菜",
          name: "王小虎",
          phone: "18710186144",
          address: "上海市普陀区",
          date: "2016-05-02",
        },
      ],
      list: [
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
      ],
      agro: [
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
      ],
      tvds: [
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
        { a: "农业要闻", b: "2020年第21期土壤摘情简报", c: "2020-09-31" },
      ],
    };
  },
  computed: {},
  created() {},
  mounted() {},
  watch: {},
  methods: {},
  components: { trend },
};
</script>


<style scoped lang="less">
.home_index {
  .content {
    display: flex;
    .block {
      height: 295px;
      width: 633px;
      padding: 10px 10px 0 0;
    }
    .list {
      width: 425px;
      height: 268px;
      float: left;
      background-color: white;
      margin: 10px 0 0 0;
      .lists {
        height: 29px;
        width: 418px;
        // background-color: #475669;
        border-bottom: 1px solid #457a10;
        .lists_a {
          height: 29px;
          width: 120px;
          background-color: #457a10;
          text-indent: 2em;
          float: left;
          color: aliceblue;
          font-weight: bold;
        }
        .lists_b {
          float: right;
          color: #16ad39;
          font-weight: bold;
        }
      }
      .list_content {
        padding: 2px 15px 10px;
        li {
          display: flex;
          height: 16px;
          padding: 8px 0 4px;
          border-bottom: 1px dashed #b0aded;
          justify-content: space-between;
          p {
            font-size: 13px;
          }
          p:nth-child(1) {
            flex: 1;
            color: #457a10;
          }
          p:nth-child(2) {
            flex: 3;
          }
          p:nth-child(3) {
            color: #828282;
            flex: 1;
            font-size: 10px;
          }
        }
      }  .list_content {
        padding: 2px 15px 10px;
        li {
          display: flex;
          height: 16px;
          padding: 8px 0 4px;
          border-bottom: 1px dashed #b0aded;
          justify-content: space-between;
          p {
            font-size: 13px;
          }
          p:nth-child(1) {
            flex: 1;
            color: #457a10;
          }
          p:nth-child(2) {
            flex: 3;
          }
          p:nth-child(3) {
            color: #828282;
            flex: 1;
            font-size: 10px;
          }
        }
      }
    }
  }

  .msgs {
    display: flex;
    height: 300px;
    .msg {
      height: 100%;
      width: 675px;
      margin: 10px 10px 0 0;
      .msgtitle {
        display: flex;
        height: 50px;
        justify-self: space-between;
        background-color: aliceblue;
        ul {
          flex: 1;
          display: flex;
          align-items: center;

          li {
            height: 40px;
            width: 100px;
            background-color: #16ad39;
            text-align: center;
          }
        }
        p {
          flex: 1;
          display: flex;
          flex-direction: row-reverse;
          align-items: center;
          color: #16ad39;
        }
      }
    }
    .expert img {
      height: 297px;
      width: 390px;
      background-color: firebrick;
    }
  }
  .banner {
    margin-top: 10px;
    // height: 419px;
    // background-color: aquamarine;
    > img {
      width: 1045px;
    } 
    .market{
      width: 100%;
      height: 350px;
     background-color: aquamarine;
    }

  }
  .agroScene {
    display: flex;
    margin-top: 10px;
    height: 372px;
    .agro {
      // width: 600px;
      flex: 6;
      background-color: white;
      margin-right: 15px;
      .agros {
        height: 35px;
        border-bottom: 1px solid #457a10;
        .agro_a {
          height: 35px;
          width: 120px;
          background-color: #457a10;
          text-indent: 2em;
          float: left;
          color: aliceblue;
          font-weight: bold;
        }
        .agro_b {
          float: right;
          color: #16ad39;
          font-weight: bold;
        }
      }
      .agro_content {
        padding: 2px 15px 10px;
        li {
          display: flex;
          height: 16px;
          padding: 8px 0 4px 0;
          border-bottom: 1px dashed #b0aded;
          justify-content: space-between;
          p {
            font-size: 14px;
          }
          p:nth-child(1) {
            flex: 1;
            color: #457a10;
          }
          p:nth-child(2) {
            flex: 3;
          }
          p:nth-child(3) {
            color: #828282;
            flex: 1;
          }
        }
      }
    }
    .scene {
      width: 400px;
      background-color: red;
      flex: 4;
      .scenes {
        display: flex;
        width: 100%;
        height: 35px;
        border-bottom: 1px solid #457a10;
        .scene_a {
          height: 35px;
          width: 50%;
          background-color: #457a10;
          text-indent: 2em;
          float: left;
          color: aliceblue;
          font-weight: bold;
        }
      }
    }
  }
  .video {
    margin-top: 10px;
    height: 206px;
    display: flex;
    .tvd {
      flex: 6;
      background-color: white;
      margin-right: 15px;
      .tvds {
        height: 25px;
        border-bottom: 1px solid #457a10;
        .tvds_a {
          height: 25px;
          width: 120px;
          background-color: #457a10;
          text-indent: 2em;
          float: left;
          color: aliceblue;
          font-weight: bold;
        }
        .tvds_b {
          float: right;
          color: #16ad39;
          font-weight: bold;
        }
      }
      .dvd {
        width: 200px;
        // height: 206px;
        background-color: aqua;
        float: left;
      }
      .tvdList {
        // height: 206px;
        width: 450px;
        float: right;
        .tvds_content {
          padding: 2px 15px 10px;
          li {
            display: flex;
            height: 16px;
            padding: 8px 0 4px 0;
            border-bottom: 1px dashed #b0aded;
            justify-content: space-between;
            p {
              font-size: 14px;
            }
            p:nth-child(1) {
              flex: 1;
              color: #457a10;
            }
            p:nth-child(2) {
              flex: 3;
            }
            p:nth-child(3) {
              color: #828282;
              flex: 1;
            }
          }
        }
      }
    }
    .deo {
      flex: 3;
      background-color: cadetblue;
    }
  }
  .aaa > img {
    width: 1045px;
  }
}
</style>