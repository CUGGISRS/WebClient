<template>
  <div>
    <div class="content">
      <div class="list_left">
        <div>
          <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
            <el-tab-pane label="种植技术" name="1"></el-tab-pane>
            <el-tab-pane label="水产养殖技术" name="2"></el-tab-pane>
            <el-tab-pane label="畜牧兽医技术" name="3"></el-tab-pane>
            <el-tab-pane label="农技视频" name="video"></el-tab-pane>
          </el-tabs>
        </div>
        <div class="list">
          <ul class="list_content">
            <li v-for="(item,index) in list " :key="index">
              <p>[{{item.a}}]</p>
              <p>{{item.b}}</p>
              <p>{{item.c}}</p>
            </li>
          </ul>
        </div>
      </div>
      <div class="list_right">
        <img src alt />
        <img src alt />
      </div>
    </div>
    <div class="paging">
      <el-pagination background layout="prev, pager, next" :total="9999"></el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  props: {},
  data() {
    return {
      activeName: "first",
      list: [
        { a: "种植要闻", b: "主导马铃薯品种【天薯一号】", c: "2020-09-31" },
        { a: "种植要闻", b: "主推小麦技术", c: "2020-09-31" },
        { a: "种植要闻", b: "主导马铃薯品种【天薯一号】", c: "2020-06-31" },
        { a: "种植要闻", b: "主推小麦技术", c: "2020-09-31" },
        { a: "种植要闻", b: "主导马铃薯品种【天薯一号】", c: "2020-09-31" },
        { a: "种植要闻", b: "主推小麦技术", c: "2020-09-31" },
        { a: "种植要闻", b: "主导马铃薯品种【天薯一号】", c: "2020-09-31" },
        { a: "种植要闻", b: "主推小麦技术", c: "2020-09-31" },
      ],
    };
  },
  computed: {},
  created() {},
  mounted() {},
  watch: {},
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
  },
  components: {},
};
</script>

<style scoped lang='less'>
.content {
  width: 95%;
  margin: 0 auto;
  display: flex;
    background-color: #fff; 
  .list_left {
    flex: 7;
   
    .list {
      // background-color: white;
      // margin: 10px 0 0 0;
      .list_content {
        padding: 2px 15px 10px;
        li {
          display: flex;
          height: 16px;
          padding: 10px 0 10px;
          border-bottom: 1px dashed #b0aded;
          justify-content: space-between;
          p {
            font-size: 16px;
          }
          p:nth-child(1) {
            flex: 1;
            color: #457a10;
          }
          p:nth-child(2) {
            flex: 5;
          }
          p:nth-child(3) {
            color: #828282;
            flex: 1;
            font-size: 13px;
          }
        }
      }
    }
  }
  .list_right {
    flex: 3;
    background-color: #6666;
  }
}
.paging{
 width: 95%;
 .el-pagination{
   margin: 20px;
   margin-left: 30%;
 }
}
</style>
