<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>溯源信息监督</el-breadcrumb-item>
      <el-breadcrumb-item>追溯查询</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>追溯来源:</span>
					<el-select v-model="selectSource" style="width: 200px;" @change="changeSelect">
						<el-option
							v-for="item in selectOptions"
							:key="item.value"
							:label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="query-item" v-if="selectSource">
					<span>追溯码:</span>
					<el-input v-model="params.productBatchNumber" clearable style="width: 220px;" placeholder="请输入追溯码"></el-input>
				</div>
				<div class="query-item" v-else>
					<span>收购码:</span>
					<el-input v-model="params5.traceCode" clearable style="width: 220px;" placeholder="请输入收购码"></el-input>
				</div>
				<el-button size="small" type="primary" icon="el-icon-search" @click="searchRow">查询</el-button>
				<div class="changeType">
					<el-select v-model="params.baseType" @change="changeEnterpriseType">
						<el-option
							v-for="item in enterpriseTypeOptions"
							:key="item.value"
							:label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
			</div>
			<!-- 表格显示区域 -->
			<el-table :data="rows" v-if="selectSource"
				border stripe fit highlight-current-row 
				:row-style="{height:'5px'}"
				:cell-style="{padding:'5px 0'}"
				:height="curHeight2">
				<el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="追溯码" th="160">
          <template slot-scope="scope">
            {{ scope.row.productBatchNumber }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="加工开始时间" width="160">
          <template slot-scope="scope">
            {{ filterTime(scope.row.startDate) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="加工结束时间" width="160">
          <template slot-scope="scope">
            {{ filterTime(scope.row.endDate) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="追溯信息" width="160">
          <template slot-scope="scope">
            <el-button size="small"  icon="el-icon-view" type="success" @click="showRow(scope.row.productBatchNumber)">查看</el-button>
          </template>
        </el-table-column>
				<el-table-column align="center" label="追溯二维码" width="160">
          <template slot-scope="scope">
            <el-button size="small"  icon="el-icon-view" type="success" @click="showQRCode(scope.row.productBatchNumber)">查看</el-button>
          </template>
        </el-table-column>
			</el-table>
			<el-table :data="rows" v-else
				border stripe fit highlight-current-row 
				:row-style="{height:'5px'}"
				:cell-style="{padding:'5px 0'}"
				:height="curHeight2">
				<el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="收购码" min-width="160">
          <template slot-scope="scope">
            {{ scope.row.traceCode }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="追溯信息" width="160">
          <template slot-scope="scope">
            <el-button size="small"  icon="el-icon-view" type="success" @click="showRow(scope.row.traceCode)">查看</el-button>
          </template>
        </el-table-column>
				<el-table-column align="center" label="追溯二维码" width="160">
          <template slot-scope="scope">
            <el-button size="small"  icon="el-icon-view" type="success" @click="showQRCode(scope.row.traceCode)">查看</el-button>
          </template>
        </el-table-column>
			</el-table>
			<!-- 分页 -->
			<el-pagination
				background
				@size-change="handleSizeChange"
				@current-change="handleCurrentChange"
				:current-page="currentPage"
				:page-sizes="[10, 20, 30, 40]"
				:page-size="pageSize"
				layout="total, sizes, prev, pager, next, jumper"
				:total="total">
			</el-pagination>
    </el-card>
    <!-- 新增编辑对话框 -->
		<el-dialog v-cloak title="追溯信息" center :visible.sync="dialogShow" top="12vh" width="80%" class="row-form">
			<el-form :model="row" label-width="160px" class="demo-ruleForm">
				<el-tabs type="border-card" v-model="activeName">
					<el-tab-pane label="产品信息" name="first" :style="curHeight">
						<el-row>
							<el-col :span="12">
								<el-form-item label="追溯码:" v-if="selectSource">
									{{ row.aqzs_process_batch.productBatchNumber }}
								</el-form-item>
								<el-form-item label="收购码:" v-else>
									{{ row.aqzs_harvest.traceCode }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="产品名称:">
									{{ row.aqzs_product.name }}
								</el-form-item>
							</el-col>
						</el-row>
						<el-row>
							<el-col :span="12">
								<el-form-item label="开始时间:">
									{{ filterTime(row.aqzs_product.startDate) }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="结束时间:">
									{{ filterTime(row.aqzs_product.endDate) }}
								</el-form-item>
							</el-col>
						</el-row>
						<el-row>
							<el-col :span="12">
								<el-form-item label="商品名称:">
									{{ row.aqzs_product_detail.name }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="商品条码:">
									{{ row.aqzs_product_detail.code }}
								</el-form-item>
							</el-col>
						</el-row>
						<el-row>
							<el-col :span="12">
								<el-form-item label="商品类型:">
									{{ getDictionaryName(row.aqzs_product_detail.type) }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="商品规格:">
									{{ row.aqzs_product_detail.specs }}
								</el-form-item>
							</el-col>
						</el-row>
						<el-row>
							<el-col :span="12">
								<el-form-item label="商品用途:">
									{{ row.aqzs_product_detail.purpose }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="商品保质期:">
									{{ row.aqzs_product_detail.shelfLife }}
								</el-form-item>
							</el-col>
						</el-row>
						<el-row>
							<el-col :span="24">
								<el-form-item label="商品介绍:">
									{{ row.aqzs_product_detail.introduce }}
								</el-form-item>
							</el-col>
						</el-row>
						<!-- 商品图片 -->
						<el-row>
							<el-col :span="24">
								<el-form-item label="商品图片:">
									<div class="upload-div" v-if="imgProductPictureLists.length !== 0">
										<div class="upload-div-list" v-for="(item,index) of imgProductPictureLists" :key="index">
											<el-image :src="baseUrl + item.url" :preview-src-list="srcList"></el-image>
										</div>
									</div>
									<div v-else>暂无</div>
								</el-form-item>
							</el-col>	
						</el-row>
						<!-- 商品证书 -->
						<el-row>
							<el-col :span="24">
								<el-form-item label="商品证书:">
									<div class="upload-div" v-if="imgProductCertificateLists.length !== 0">
										<div class="upload-div-list" v-for="(item,index) of imgProductCertificateLists" :key="index">
											<el-image :src="baseUrl + item.url" :preview-src-list="srcList02"></el-image>
										</div>
									</div>
									<div v-else>暂无</div>
								</el-form-item>
							</el-col>	
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="基地信息" name="second" :style="curHeight">
						<!-- 种植/养殖基地 -->
						<el-row>
							<el-col :span="12">
								<el-form-item label="基地名称:">
									{{ row.aqzs_base1.name }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="基地位置:">
									{{ row.aqzs_base1.address }}
								</el-form-item>
							</el-col>
						</el-row>
						<el-row>
							<el-col :span="12">
								<el-form-item label="基地面积/规模(亩):">
									{{ row.aqzs_base1.area }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="基地环境检验结果:">
									<el-tag type="success" v-if="row.aqzs_base1.isReportQualified === 1">合格</el-tag>
									<el-tag type="danger" v-else-if="row.aqzs_base1.isReportQualified === 0">不合格</el-tag>
								</el-form-item>
							</el-col>
						</el-row>
						<el-row>
							<el-col :span="12">
								<el-form-item label="基地负责人:">
                  {{ row.aqzs_base1.lender }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="基地负责人电话:">
                  {{ row.aqzs_base1.lenderPhone }}
								</el-form-item>
							</el-col>
						</el-row>
						<!-- 生产基地 -->
						<el-row v-if="selectSource">
							<el-col :span="12">
								<el-form-item label="生产基地名称:">
									{{ row.aqzs_base2.name }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="生产基地位置:">
									{{ row.aqzs_base2.address }}
								</el-form-item>
							</el-col>
						</el-row>
						<el-row v-if="selectSource">
							<el-col :span="12">
								<el-form-item label="生产基地面积/规模(亩):">
									{{ row.aqzs_base2.area }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="生产基地检验结果:">
									<el-tag type="success" v-if="row.aqzs_base2.isReportQualified === 1">合格</el-tag>
									<el-tag type="danger" v-else-if="row.aqzs_base2.isReportQualified === 0">不合格</el-tag>
								</el-form-item>
							</el-col>
						</el-row>
						<el-row v-if="selectSource">
							<el-col :span="12">
								<el-form-item label="生产基地负责人:">
                  {{ row.aqzs_base2.lender }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="生产基地负责人电话:">
                  {{ row.aqzs_base2.lenderPhone }}
								</el-form-item>
							</el-col>
						</el-row>
						<!-- 检验报告 -->
						<el-row>
							<el-col :span="24">
								<el-form-item label="基地检验报告:">
									<div class="upload-div" v-if="imgPlantingInspectionReportLists.length !== 0">
										<div class="upload-div-list" v-for="(item,index) of imgPlantingInspectionReportLists" :key="index">
											<el-image :src="baseUrl + item.url" :preview-src-list="srcList03"></el-image>
										</div>
									</div>
									<div v-else>暂无</div>
								</el-form-item>
							</el-col>	
						</el-row>
						<el-row v-if="selectSource">
							<el-col :span="24">
								<el-form-item label="生产基地检验报告:">
									<div class="upload-div" v-if="imgProductionInspectionReportLists.length !== 0">
										<div class="upload-div-list" v-for="(item,index) of imgProductionInspectionReportLists" :key="index">
											<el-image :src="baseUrl + item.url" :preview-src-list="srcList04"></el-image>
										</div>
									</div>
									<div v-else>暂无</div>
								</el-form-item>
							</el-col>	
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="基本作业信息" name="third" :style="curHeight">
						<el-table :data="row.aqzs_base_work"
							border stripe fit highlight-current-row 
							:row-style="{height:'5px'}"
							:cell-style="{padding:'5px 0'}">
							<el-table-column align="center" label="作业类型" width="80">
								<template slot-scope="scope">
									{{ getDictionaryName3(scope.row.workType) }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="作业物名称" width="160">
								<template slot-scope="scope">
									{{ scope.row.workObjectName}}
								</template>
							</el-table-column>
							<el-table-column align="center" label="作业物类型" width="120">
								<template slot-scope="scope">
									{{ scope.row.workObjectType }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="作业物来源" min-width="120">
								<template slot-scope="scope">
									{{ scope.row.workObjectSource }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="作业物数量" width="120">
								<template slot-scope="scope">
									{{ scope.row.workObjectAmount + getDictionaryName2(scope.row.workObjectAmountUnit) }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="作业开始日期" width="120">
								<template slot-scope="scope">
									{{ filterTime(scope.row.startDate) }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="作业结束日期" width="120">
								<template slot-scope="scope">
									{{ filterTime(scope.row.endDate) }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="天气状况" width="100">
								<template slot-scope="scope">
									{{ scope.row.weather }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="作业图片" width="100">
								<template slot-scope="scope">
									<el-button size="small" type="success" icon="el-icon-view" @click="showPhoto(scope.row.workPictureList)">查看</el-button>
								</template>
							</el-table-column>
						</el-table>
					</el-tab-pane>
					<el-tab-pane label="生长环境信息" name="fourth" :style="curHeight">
						<el-table :data="row.xph_device_sensor"
							border stripe fit highlight-current-row 
							:row-style="{height:'5px'}"
							:cell-style="{padding:'5px 0'}">
							<el-table-column align="center" label="操作" width="160">
								<template slot-scope="scope">
									<el-button size="mini" type="success" icon="el-icon-view" @click="showSensorData(scope.row)">查看数据</el-button>
								</template>
							</el-table-column>
							<el-table-column align="center" label="传感器名称">
								<template slot-scope="scope">
									{{ scope.row.name }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="传感器最新日期">
								<template slot-scope="scope">
									{{ scope.row.lastRecordTime }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="传感器最新数值">
								<template slot-scope="scope">
									{{ scope.row.lastRecordData + ' ' + scope.row.unit }}
								</template>
							</el-table-column>
						</el-table>
					</el-tab-pane>
					<el-tab-pane label="生产作业信息" name="fifth" :style="curHeight">
						<!-- 采收/捕捞 -->
						<el-row>
							<el-col :span="12">
								<el-form-item label="收获方式:">
									{{ row.aqzs_harvest.harvestWay }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="收获数量:">
									{{ row.aqzs_harvest.harvestAmount + getDictionaryName2(row.aqzs_harvest.harvestAmountUnit) }}
								</el-form-item>
							</el-col>
						</el-row>
						<el-row>
							<el-col :span="12">
								<el-form-item label="收获时间:">
									{{ filterTime(row.aqzs_harvest.harvestDate) }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="天气状况:">
									{{ row.aqzs_harvest.weather }}
								</el-form-item>
							</el-col>
						</el-row>
						<!-- 收购 -->
						<div :style="{ display: visible }">
							<el-row>
								<el-col :span="12">
									<el-form-item label="收购码:">
										{{ row.aqzs_buy.traceCode }}
									</el-form-item>
								</el-col>
								<el-col :span="12">
									<el-form-item label="收购产品名称:">
										{{ row.aqzs_buy.name }}
									</el-form-item>
								</el-col>
							</el-row>
							<el-row>
								<el-col :span="12">
									<el-form-item label="收购总价(元):">
										{{ row.aqzs_buy.buyTotal }}
									</el-form-item>
								</el-col>
								<el-col :span="12">
									<el-form-item label="收获数量:">
										{{ row.aqzs_buy.buyAmount + getDictionaryName2(row.aqzs_buy.buyAmountUnit) }}
									</el-form-item>
								</el-col>
							</el-row>
							<el-row>
								<el-col :span="12">
									<el-form-item label="收购时间:">
										{{ filterTime(row.aqzs_buy.buyDate) }}
									</el-form-item>
								</el-col>
							</el-row>
							<el-row>
								<el-col :span="24">
									<el-form-item label="描述:">
										{{ row.aqzs_buy.description }}
									</el-form-item>
								</el-col>
							</el-row>
						</div>
						<!-- 加工 -->
						<el-row v-if="selectSource">
							<el-col :span="12">
								<el-form-item label="加工开始时间:">
									{{ filterTime(row.aqzs_process_batch.startDate) }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="加工结束时间:">
									{{ filterTime(row.aqzs_process_batch.endDate) }}
								</el-form-item>
							</el-col>
						</el-row>
						<el-row v-if="selectSource">
							<el-col :span="12">
								<el-form-item label="加工数量:">
									{{ row.aqzs_process_batch.processAmount + getDictionaryName2(row.aqzs_process_batch.processAmountUnit) }}
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="加工方式:">
									{{ row.aqzs_process_batch.processWay }}
								</el-form-item>
							</el-col>
						</el-row>
						<!-- 采收/捕捞图片 -->
						<el-row>
							<el-col :span="24">
								<el-form-item label="采收/捕捞图片:">
									<div class="upload-div" v-if="imgHarvestPictureLists.length !== 0">
										<div class="upload-div-list" v-for="(item,index) of imgHarvestPictureLists" :key="index">
											<el-image :src="baseUrl + item.url" :preview-src-list="srcList06"></el-image>
										</div>
									</div>
									<div v-else>暂无</div>
								</el-form-item>
							</el-col>	
						</el-row>
						<!-- 加工图片 -->
						<el-row v-if="selectSource">
							<el-col :span="24">
								<el-form-item label="加工图片:">
									<div class="upload-div" v-if="imgProcessPictureLists.length !== 0">
										<div class="upload-div-list" v-for="(item,index) of imgProcessPictureLists" :key="index">
											<el-image :src="baseUrl + item.url" :preview-src-list="srcList07"></el-image>
										</div>
									</div>
									<div v-else>暂无</div>
								</el-form-item>
							</el-col>	
						</el-row>
						<!-- 收购图片 -->
						<el-row :style="{ display: visible }">
							<el-col :span="24">
								<el-form-item label="收购图片:">
									<div class="upload-div" v-if="imgBuyPictureLists.length !== 0">
										<div class="upload-div-list" v-for="(item,index) of imgBuyPictureLists" :key="index">
											<el-image :src="baseUrl + item.url" :preview-src-list="srcList11"></el-image>
										</div>
									</div>
									<div v-else>暂无</div>
								</el-form-item>
							</el-col>	
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="检验结果信息" name="sixth" :style="curHeight" v-if="selectSource">
						<el-table :data="row.aqzs_finish_product_test"
							border stripe fit highlight-current-row 
							:row-style="{height:'5px'}"
							:cell-style="{padding:'5px 0'}">
							<el-table-column align="center" label="追溯码" min-width="120">
								<template slot-scope="scope">
									{{ scope.row.productBatchNumber }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="检验数量" width="240">
								<template slot-scope="scope">
									{{ scope.row.testAmount + getDictionaryName2(scope.row.testAmountUnit) }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="检验日期" width="120">
								<template slot-scope="scope">
									{{ filterTime(scope.row.testDate) }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="检验结果" width="100">
								<template slot-scope="scope">
									{{ scope.row.weather }}
									<el-tag type="success" v-if="scope.row.testResult === '合格'">合格</el-tag>
									<el-tag type="danger" v-else>不合格</el-tag>
								</template>
							</el-table-column>
							<el-table-column align="center" label="检验图片" width="100">
								<template slot-scope="scope">
									<el-button size="small" type="success" icon="el-icon-view" @click="showInspectionPhoto(scope.row.testResultPictureList)">查看</el-button>
								</template>
							</el-table-column>
						</el-table>
					</el-tab-pane>
					<el-tab-pane label="销售信息" name="seventh" :style="curHeight" v-if="selectSource">
						<el-table :data="row.aqzs_sale"
							border stripe fit highlight-current-row 
							:row-style="{height:'5px'}"
							:cell-style="{padding:'5px 0'}">
							<el-table-column align="center" label="订单编号" width="200">
								<template slot-scope="scope">
									{{ scope.row.orderNumber }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="销售数量" width="120">
								<template slot-scope="scope">
									{{ scope.row.saleAmount + getDictionaryName2(scope.row.saleAmountUnit) }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="销售商名称" width="120">
								<template slot-scope="scope">
									{{ getName(scope.row.sellerId) }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="发货时间" width="120">
								<template slot-scope="scope">
									{{ filterTime(scope.row.sendDate) }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="发货人" width="100">
								<template slot-scope="scope">
									{{ scope.row.sender }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="收货人" width="100">
								<template slot-scope="scope">
									{{ scope.row.receiver }}
								</template>
							</el-table-column>
							<el-table-column align="center" label="始发地" min-width="120">
								<template slot-scope="scope">
									<el-tooltip effect="light" :content="scope.row.startPlace" placement="top">
										<span>{{ scope.row.startPlace }}</span>
									</el-tooltip>
								</template>
							</el-table-column>
							<el-table-column align="center" label="目的地" min-width="120">
								<template slot-scope="scope">
									<el-tooltip effect="light" :content="scope.row.endPlace" placement="top">
										<span>{{ scope.row.endPlace }}</span>
									</el-tooltip>
								</template>
							</el-table-column>
						</el-table>
					</el-tab-pane>
				</el-tabs>
			</el-form>
		</el-dialog>
		<!-- 传感器数据对话框 -->
		<el-dialog v-cloak title="传感器数据" center :visible.sync="dialogSensorShow" width="1000px" class="row-form" top="10vh">
			<el-table :data="sensorRows"
				border stripe fit highlight-current-row 
				:row-style="{height:'5px'}"
				:cell-style="{padding:'5px 0'}"
				:height="curHeight2"
				style="margin-top: 0">
				<el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (sensorCurrentPage - 1) * sensorPageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="日期">
          <template slot-scope="scope">
						{{ scope.row.recordTime }}
          </template>
        </el-table-column>
				<el-table-column align="center" :label="name">
          <template slot-scope="scope">
						{{ scope.row.recordData }}
          </template>
        </el-table-column>
			</el-table>
      <!-- 分页 -->
			<el-pagination
				background
				@size-change="handleSizeChange2"
				@current-change="handleCurrentChange2"
				:current-page="sensorCurrentPage"
				:page-sizes="[10, 20, 30, 40]"
				:page-size="sensorPageSize"
				layout="total, sizes, prev, pager, next, jumper"
				:total="sensorTotal">
			</el-pagination>
		</el-dialog>
		<!-- 基本作业图片预览 -->
		<el-dialog v-cloak center title="基本作业图片" :visible.sync="dialogOperationPictureVisible" width="80%" top="12vh" :append-to-body="true">
			<div class="upload-div" v-if="imgOperationPictureLists.length !== 0">
				<div class="upload-div-list" v-for="(item,index) of imgOperationPictureLists" :key="index">
					<el-image :src="baseUrl + item.url" :preview-src-list="srcList05"></el-image>
				</div>
			</div>
			<div v-else style="text-align: center;font-size: 24px">暂无</div>
		</el-dialog>
		<!-- 检验结果图片预览 -->
		<el-dialog v-cloak center title="检验结果图片" :visible.sync="dialogInspectionPictureVisible" width="80%" top="12vh" :append-to-body="true">
			<div class="upload-div" v-if="imgInspectionPictureLists.length !== 0">
				<div class="upload-div-list" v-for="(item,index) of imgInspectionPictureLists" :key="index">
					<el-image :src="baseUrl + item.url" :preview-src-list="srcList08"></el-image>
				</div>
			</div>
			<div v-else style="text-align: center;font-size: 24px">暂无</div>
		</el-dialog>
		<!-- 追溯二维码图片 -->
		<el-dialog v-cloak center title="追溯二维码图片" @close="closeDialog" :visible.sync="dialogQRCodeVisible" width="300px">
			<div id="qrCode" ref="qrCode"></div>
		</el-dialog>
  </div>
</template>

<script>
import QRCode from 'qrcodejs2';
export default {
  data() {
		return {
			// 参数条件
			params: {
				page: null,
				limit: null,
				productBatchNumber: '',
				enterpriseId: window.sessionStorage.getItem('enterpriseId'),
				baseType: '种植基地'
			},
			params2: {
				type: '种植产品类型',
				remark: 'X2'
			},
			params3: {
				type: '种植作业类型',
				remark: 'X2'
			},
			// 传感器数据查询参数
			params4: {
				recordTimeS: null,
				recordTimeE: null,
				interval: 1,
				sensorId: null
			},
			// 收购追溯
			params5: {
				page: null,
				limit: null,
				traceCode: '',
				enterpriseId: window.sessionStorage.getItem('enterpriseId'),
				baseType: '种植基地'
			},
			// 选择追溯来源
			selectSource: true,
			selectOptions: [
				{ label: '加工追溯', value: true },
				{ label: '收获追溯', value: false }
			],
			// 企业类型
			enterpriseTypeOptions: [
				{
					label: '种植数据',
					value: '种植基地'
				},
				{
					label: '水产数据',
					value: '养殖基地'
				}
			],
			// 表单
			rows: [],
			row: {
				aqzs_product: {},
				aqzs_product_detail: {},
				aqzs_base1: {},
				aqzs_base2: {},
				aqzs_base_work: [],
				xph_device_sensor: [],
				aqzs_harvest: {},
				aqzs_buy: {},
				aqzs_process_batch: {},
				aqzs_finish_product_test: [],
				aqzs_sale: {}
			},
			// 传感器数据
			sensorRows: [],
			sensorRow: {},
			// 传感器名称
			name: '',
			// 分页
			sensorCurrentPage: 1,
			sensorTotal: 0,
			sensorPageSize: 20,
			// 空数据隐藏
			visible: 'block',
			// 分页
			currentPage: 1,
			total: 0,
			pageSize: 20,
			// 表单高度
			curHeight: {
				height: 0,
				overflow: 'auto'
			},
			curHeight2: 0,
			// 活跃tab栏
			activeName: 'first',
			// 对话框
			dialogShow: false,
			dialogOperationPictureVisible: false,
			dialogInspectionPictureVisible: false,
			dialogQRCodeVisible: false,
			// 传感器数据对话框
			dialogSensorShow: false,
			baseUrl: this.$baseImgUrl,
			// 多选框
			typeOptions: [],
			amountUnitOptions: [],
			baseOperationOptions: [],
			retailerOptions: [],
			// 商品图片
			imgProductPictureLists: [],
			srcList: [],
			// 商品证书
			imgProductCertificateLists: [],
			srcList02: [],
			// 种植/基地检验报告
			imgPlantingInspectionReportLists: [],
			srcList03: [],
			// 种植/养殖基地照片
			imgPlantingBaseLists: [],
			srcList09: [],
			// 生产基地检验报告
			imgProductionInspectionReportLists: [],
			srcList04: [],
			// 生产基地照片
			imgProductionBaseLists: [],
			srcList10: [],
			// 基本作业图片
			imgOperationPictureLists: [],
			srcList05: [],
			// 采收图片
			imgHarvestPictureLists: [],
			srcList06: [],
			// 加工图片
			imgProcessPictureLists: [],
			srcList07: [],
			// 收购图片
			imgBuyPictureLists: [],
			srcList11: [],
			// 检验图片
			imgInspectionPictureLists: [],
			srcList08: []
		}
	},
	created() {
		this.getTypeDictionary();
		this.getAmountUnitDictionary();
		this.getBaseOperationDictionary();
		this.getRetailerDictionary();
		this.getAllList();
		this.setTableHeight();
		window.onresize = () => {
			this.setTableHeight();
		}
	},
	methods: {
		// 获取数据字典
		async getTypeDictionary() {
			const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
				params: this.params2
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取产品类型失败！'
				});
			}
			this.typeOptions = res.data;
		},
		async getAmountUnitDictionary() {
			const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
				params: { type: '数量单位' }
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取数量单位失败！'
				});
			}
			this.amountUnitOptions = res.data;
		},
		async getBaseOperationDictionary() {
			const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
				params: this.params3
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取水产作业类型失败！'
				});
			}
			this.baseOperationOptions = res.data;
		},
		async getRetailerDictionary() {
			const { data: res } = await this.$http.get('zsSys/Seller/pageByEId', {
				params: {
					enterpriseId: window.sessionStorage.getItem('enterpriseId')
				}
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取销售商数据失败！'
				});
			}
			this.retailerOptions = res.data.rows;
		},
		// 获取全部的列表数据
		async getAllList() {
			this.params.page = this.currentPage;
			this.params.limit = this.pageSize;
			const { data: res } = await this.$http.get('zsSys/ProcessBatch/pageByEId', { params: this.params });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取加工批次数据失败！'
				});
			}
			this.rows = res.data.rows;
			this.total = res.data.total;
		},
		async getAllList2() {
			this.params5.page = this.currentPage;
			this.params5.limit = this.pageSize;
			const { data: res } = await this.$http.get('zsSys/Harvest/pageByEId', { params: this.params5 });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取收购码失败！'
				});
			}
			this.rows = res.data.rows;
			this.total = res.data.total;
		},
		// 改变追溯来源
		changeSelect(value) {
			this.currentPage = 1;
			this.pageSize = 10;
			this.total = 0;
			this.rows = [];
			if(value) {
				this.getAllList();
			} else {
				this.getAllList2();
			}
		},
		// 每页多少条
		handleSizeChange(val) {
			this.pageSize = val;
			if(this.selectSource) {
				this.getAllList();
			} else {
				this.getAllList2();
			}
		},
		handleSizeChange2(val) {
			this.sensorPageSize = val;
			this.getSensroData(this.sensorRow.id);
		},
		// 当前页
    handleCurrentChange(val) {
			this.currentPage = val;
			if(this.selectSource) {
				this.getAllList();
			} else {
				this.getAllList2();
			}
		},
		handleCurrentChange2(val) {
			this.sensorCurrentPage = val;
			this.getSensroData(this.sensorRow.id);
		},
		// 设定表格高度
		setTableHeight(){
      let h = document.documentElement.clientHeight || document.body.clientHeight;
			this.curHeight.height = h - 400 + 'px';
			this.curHeight2 = h - 425;
		},
		// 查询数据
		searchRow() {
			this.currentPage = 1;
			if(this.selectSource) {
				this.getAllList();
			} else {
				this.getAllList2();
			}
		},
		// 查看追溯信息
		async showRow(productBatchNumber) {
			this.srcList = [];
			this.srcList02 = [];
			this.srcList03 = [];
			this.srcList04 = [];
			this.srcList06 = [];
			this.srcList07 = [];
			this.srcList09 = [];
			this.srcList10 = [];
			this.srcList11 = [];
			this.imgProductPictureLists = [];
			this.imgProductCertificateLists = [];
			this.imgPlantingInspectionReportLists = [];
			this.imgPlantingBaseLists = [];
			this.imgProductionInspectionReportLists = [];
			this.imgProductionBaseLists = [];
			this.imgHarvestPictureLists = [];
			this.imgProcessPictureLists = [];
			this.imgBuyPictureLists = [];
			const { data: res } = await this.$http.get('zsSys/ProcessBatch/traceByPBN', { params: { productBatchNumber } });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取追溯数据失败！'
				});
			}
			this.row = res.data;
			if(!res.data.aqzs_buy) {
				this.row.aqzs_buy = {};
				this.visible = 'none';
			}
			if(!res.data.aqzs_product) {
				this.row.aqzs_product = {};
			}
			if(!res.data.aqzs_product_detail) {
				this.row.aqzs_product_detail = {};
			}
			if(!res.data.aqzs_base1) {
				this.row.aqzs_base1 = {};
			}
			if(!res.data.aqzs_base2) {
				this.row.aqzs_base2 = {};
			}
			if(!res.data.aqzs_harvest) {
				this.row.aqzs_harvest = {};
			}
			if(!res.data.aqzs_process_batch) {
				this.row.aqzs_process_batch = {};
			}
			if(!res.data.aqzs_sale) {
				this.row.aqzs_sale = {};
			}
			// 商品图片
			if(res.data.aqzs_product_detail.pictureList && res.data.aqzs_product_detail.pictureList.length !== 0) {
				this.imgProductPictureLists = res.data.aqzs_product_detail.pictureList;
				for(let i=0; i<this.imgProductPictureLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgProductPictureLists[i].url);
        }
			}
			// 商品证书
			if(res.data.aqzs_product_detail.certificateList && res.data.aqzs_product_detail.certificateList.length !== 0) {
				this.imgProductCertificateLists = res.data.aqzs_product_detail.certificateList;
				for(let i=0; i<this.imgProductCertificateLists.length; i++) {
          this.srcList02.push(this.baseUrl + this.imgProductCertificateLists[i].url);
        }
			}
			// 种植/养殖基地检验报告
			if(res.data.aqzs_base1.reportPictureList && res.data.aqzs_base1.reportPictureList.length !== 0) {
				this.imgPlantingInspectionReportLists = res.data.aqzs_base1.reportPictureList;
				for(let i=0; i<this.imgPlantingInspectionReportLists.length; i++) {
          this.srcList03.push(this.baseUrl + this.imgPlantingInspectionReportLists[i].url);
        }
			}
			// 种植/养殖基地照片
			if(res.data.aqzs_base1.basePictureList && res.data.aqzs_base1.basePictureList.length !== 0) {
				this.imgPlantingBaseLists = res.data.aqzs_base1.basePictureList;
				for(let i=0; i<this.imgPlantingBaseLists.length; i++) {
          this.srcList09.push(this.baseUrl + this.imgPlantingBaseLists[i].url);
        }
			}
			// 生产基地检验报告
			if(res.data.aqzs_base2.reportPictureList && res.data.aqzs_base2.reportPictureList.length !== 0) {
				this.imgProductionInspectionReportLists = res.data.aqzs_base2.reportPictureList;
				for(let i=0; i<this.imgProductionInspectionReportLists.length; i++) {
          this.srcList04.push(this.baseUrl + this.imgProductionInspectionReportLists[i].url);
        }
			}
			// 生产基地照片
			if(res.data.aqzs_base2.basePictureList && res.data.aqzs_base2.basePictureList.length !== 0) {
				this.imgProductionBaseLists = res.data.aqzs_base2.basePictureList;
				for(let i=0; i<this.imgProductionBaseLists.length; i++) {
          this.srcList10.push(this.baseUrl + this.imgProductionBaseLists[i].url);
        }
			}
			// 采收/捕捞图片
			if(res.data.aqzs_harvest.harvestPictureList && res.data.aqzs_harvest.harvestPictureList.length !== 0) {
				this.imgHarvestPictureLists = res.data.aqzs_harvest.harvestPictureList;
				for(let i=0; i<this.imgHarvestPictureLists.length; i++) {
          this.srcList06.push(this.baseUrl + this.imgHarvestPictureLists[i].url);
        }
			}
			// 加工图片
			if(res.data.aqzs_process_batch.productBatchNumberPictureList && res.data.aqzs_process_batch.productBatchNumberPictureList.length !== 0) {
				this.imgProcessPictureLists = res.data.aqzs_process_batch.productBatchNumberPictureList;
				for(let i=0; i<this.imgProcessPictureLists.length; i++) {
          this.srcList07.push(this.baseUrl + this.imgProcessPictureLists[i].url);
        }
			}
			// 收购图片
			if(res.data.aqzs_buy.buyPictureList && res.data.aqzs_buy.buyPictureList.length !== 0) {
				this.imgBuyPictureLists = res.data.aqzs_buy.buyPictureList;
				for(let i=0; i<this.imgBuyPictureLists.length; i++) {
          this.srcList11.push(this.baseUrl + this.imgBuyPictureLists[i].url);
        }
			}
			this.activeName = 'first';
			this.dialogShow = true;
		},
		// 获取传感器数据
		async getSensroData(sensorId) {
			this.params4.page = this.sensorCurrentPage
			this.params4.limit = this.sensorPageSize;
			this.params4.recordTimeS = this.row.aqzs_product.startDate;
			this.params4.recordTimeE = this.row.aqzs_product.endDate;
			this.params4.sensorId = sensorId;
			const { data: res } = await this.$http.get(`zsSys/DeviceSensorData/pageBySId`, { params: this.params4 });
			this.sensorRows = res.data.rows;
			this.sensorTotal = res.data.total;
		},
		// 展示传感器数据
		showSensorData(row) {
			this.sensorRows = [];
			this.sensorRow = row;
			this.name = row.unit ? `${row.name}(${row.unit})` : row.name;
			this.getSensroData(row.id);
			this.dialogSensorShow = true;
		},
		// 查看基本作业图片
		showPhoto(row) {
			this.srcList05 = [];
			this.imgOperationPictureLists = [];
			if(row.length !== 0) {
				this.imgOperationPictureLists = row;
				for(let i=0; i<this.imgOperationPictureLists.length; i++) {
          this.srcList05.push(this.baseUrl + this.imgOperationPictureLists[i].url);
        }
			}
			this.dialogOperationPictureVisible = true;
		},
		// 查看检验结果图片
		showInspectionPhoto(row) {
			this.srcList08 = [];
			this.imgInspectionPictureLists = [];
			if(row.length !== 0) {
				this.imgInspectionPictureLists = row;
				for(let i=0; i<this.imgInspectionPictureLists.length; i++) {
          this.srcList08.push(this.baseUrl + this.imgInspectionPictureLists[i].url);
        }
			}
			this.dialogInspectionPictureVisible = true;
		},
		// 查看追溯二维码
		showQRCode(row) {
			this.dialogQRCodeVisible = true;
			this.$nextTick(() => {
				new QRCode(this.$refs.qrCode, {
					text: this.$baseQrUrl + 'zsxx_System/html/retrospect-detail/mobile-retrospect.html?' + row,
					width: 200,
					height: 200,
					colorDark: "#333333", //二维码颜色
					colorLight: "#ffffff", //二维码背景色
					correctLevel: QRCode.CorrectLevel.L	//容错率，L/M/H
				});
			});
		},
		// 避免重复生成
		closeDialog() {
      this.$refs.qrCode.innerHTML = "";
		},
		// 过滤时间
		filterTime(time) {
			if(time) {
				return time.slice(0, 10);
			}
			return time
		},
		// 根据id获取name
		getName(id) {
			for(let i=0; i<this.retailerOptions.length; i++) {
				if(id === this.retailerOptions[i].id) {
					return this.retailerOptions[i].name;
				}
			}
		},
		// 根据code转换成name
		getDictionaryName(code) {
			for(let i=0; i<this.typeOptions.length; i++) {
				if(code === this.typeOptions[i].code) {
					return this.typeOptions[i].name;
				}
			}
		},
		getDictionaryName2(code) {
			for(let i=0; i<this.amountUnitOptions.length; i++) {
				if(code === this.amountUnitOptions[i].code) {
					return this.amountUnitOptions[i].name;
				}
			}
		},
		getDictionaryName3(code) {
			for(let i=0; i<this.baseOperationOptions.length; i++) {
				if(code === this.baseOperationOptions[i].code) {
					return this.baseOperationOptions[i].name;
				}
			}
		},
		// 数据切换
		changeEnterpriseType(value) {
			this.params5.baseType = value;
			if(value === '种植基地') {
				this.params2.type = '种植产品类型';
				this.params3.type = '种植作业类型';
			} else if(value === '养殖基地') {
				this.params2.type = '水产产品类型';
				this.params3.type = '水产作业类型';
			}
			this.getTypeDictionary();
			this.getBaseOperationDictionary();
			if(this.selectSource) {
				this.getAllList();
			} else {
				this.getAllList2();
			}
		}
	}
}
</script>

<style lang="less" scoped>
	#qrCode {
		width: 200px;
		margin: 0 auto;
	}

	.el-table {
		margin-top: 0;
	}
</style>