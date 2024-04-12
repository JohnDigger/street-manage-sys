<template>
  <div class="mod-config">
    <el-button type="primary" @click="exportExcel()" class="exportBtn">导出为excel表格</el-button>
    <el-table
      id="educe-table"
      :data="dataList"
      ref="out-excel"
      v-loading="loading"
      border stripe
      show-summary
      :header-cell-style="rowClass"
      :span-method="objectSpanMethod"
      :summary-method="getSummaries"
      :cell-style="{'text-align':'center'}"
      style="width: 100%">
      <el-table-column
        type="index"
        :index="indexMethod"
        label="序号"
        min-width="1%">
      </el-table-column>
      <el-table-column
        label="人均收入档位"
        min-width="4%">
        <el-table-column label="" prop="incomeGroups" min-width="2%"></el-table-column>
        <el-table-column label="" prop="subIncomeGroups" min-width="2%"></el-table-column>
      </el-table-column>
      <el-table-column
        prop="totalPopulation"
        label="总人数(常住)"
        min-width="1%">
      </el-table-column>
      <el-table-column label="家庭人口情况(户)" prop="familyCountEntityList.familysCount" min-width="5%">
        <el-table-column
          prop="totalFamilys"
          label="总户数"
          width="110">
        </el-table-column>
        <el-table-column
          prop="family.familyCountEntityList"
          label="1口之家"
          width="115">
          <template slot-scope="scope">
            <div v-for="a in scope.row.familyCountEntityList">
              <span v-if="a.familySize === '1口之家'">{{ a.familysCount }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="family.familyCountEntityList"
          label="2口之家"
          width="115">
          <template slot-scope="scope">
            <div v-for="a in scope.row.familyCountEntityList">
              <span v-if="a.familySize === '2口之家'">{{ a.familysCount }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="3口之家"
          prop="family.familyCountEntityList"
          width="115">
          <template slot-scope="scope">
            <div v-for="a in scope.row.familyCountEntityList">
              <span v-if="a.familySize === '3口之家'">{{ a.familysCount }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="family.familyCountEntityList"
          label="4口之家"
          width="115">
          <template slot-scope="scope">
            <div v-for="a in scope.row.familyCountEntityList">
              <span v-if="a.familySize === '4口之家'">{{ a.familysCount }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="family.familyCountEntityList"
          label="5口之家及以上"
          width="115">
          <template slot-scope="scope">
            <div v-for="a in scope.row.familyCountEntityList">
              <span v-if="a.familySize === '5口之家及以上'">{{ a.familysCount }}</span>
            </div>
          </template>
        </el-table-column>

      </el-table-column>
      <el-table-column
        prop="totalIncome"
        label="总收入(万元)"
        min-width="1%">
      </el-table-column>
      <el-table-column
        prop="averageIncome"
        label="人均收入(万元)"
        min-width="1%">
      </el-table-column>
      <el-table-column
        prop="incomePercentage"
        label="占比(%)人"
        min-width="1%">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import FileSaver from 'file-saver'
import * as XLSX from 'xlsx'
export default {
  data () {
    return {
      dataList: [],
      dataListLoading: false,
      dataListSelections: [],
      loading: true,
      chartName: ''
    }
  },
  activated () {
    this.getDataList()
  },
  methods: {
    getDistrict(){
      this.$http({
        url: this.$http.adornUrl('/jdbmanage/income/getDistrict'),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.chartName = data.list
          console.log(this.chartName)
        } else {
          this.chartName =  ''
        }
      })
    },
    exportExcel() {
      let wb = XLSX.utils.table_to_book(document.querySelector('#educe-table'));
      let newSheetName = '附件3 居民收入摸底排查汇总表';
      wb.Sheets.Sheet1.E2 = {t: 's', v: '总户数'}
      wb.Sheets.Sheet1.F2 = {t: 's', v: '1口之家'}
      wb.Sheets.Sheet1.G2 = {t: 's', v: '2口之家'}
      wb.Sheets.Sheet1.H2 = {t: 's', v: '3口之家'}
      wb.Sheets.Sheet1.I2 = {t: 's', v: '4口之家'}
      wb.Sheets.Sheet1.J2 = {t: 's', v: '5口之家及以上'}
      wb.Sheets.Sheet1.N2 = {t: 's', v: ''}
      wb.Sheets.Sheet1.O2 = {t: 's', v: ''}
      // wb.SheetNames = ['附件3 居民收入摸底排查汇总表']
      wb.Sheets.Sheet1['!merges'].push(
        {s: {r: 18, c: 1}, e: {r: 19, c: 1}}, // 合并 B19:B20
        {s: {r: 18, c: 2}, e: {r: 19, c: 2}}, // 合并 C19:C20
        {s: {r: 18, c: 4}, e: {r: 19, c: 4}}, // 合并 E19:E20
        {s: {r: 18, c: 5}, e: {r: 19, c: 5}},  // 合并 F19:F20
        {s: {r: 18, c: 6}, e: {r: 19, c: 6}},
        {s: {r: 18, c: 7}, e: {r: 19, c: 7}},
        {s: {r: 18, c: 8}, e: {r: 19, c: 8}},
        {s: {r: 18, c: 9}, e: {r: 19, c: 9}}
      );
      // 更改默认Sheet名并更新引用
      if (wb.SheetNames.length > 0) {
        let originalSheet = wb.Sheets[wb.SheetNames[0]];
        delete wb.Sheets[wb.SheetNames[0]]; // 删除旧名称的引用
        wb.SheetNames[0] = newSheetName; // 更新Sheet名
        wb.Sheets[newSheetName] = originalSheet; // 更新Sheet引用
      } else {
        console.error('No sheets found in the workbook');
        return;
      }

      let sheet = wb.Sheets[newSheetName];
      let updatedSheet = {};
      let maxRow = 0;
      let cellRegex = /([A-Z]+)(\d+)/;

      // 遍历并移动单元格
      Object.keys(sheet).forEach(key => {
        if (key[0] === '!') { // 忽略特殊键，直接复制
          updatedSheet[key] = sheet[key];
        } else {
          const match = key.match(cellRegex);
          if (match) {
            const col = match[1];
            const originalRow = parseInt(match[2], 10);
            const newRow = originalRow + 2; // 下移两行
            maxRow = Math.max(maxRow, newRow); // 更新最大行号
            const newKey = `${col}${newRow}`;
            updatedSheet[newKey] = sheet[key];
          }
        }
      });

      // 确保!merges数组存在
      if (!updatedSheet['!merges']) updatedSheet['!merges'] = [];

      // 更新合并单元格的位置
      if (updatedSheet['!merges']) {
        updatedSheet['!merges'].forEach(merge => {
          merge.s.r += 2; // 起始行下移两行
          merge.e.r += 2; // 结束行下移两行
        });
      }

      // 添加合并A3到M4的操作
      updatedSheet['!merges'].push({
        s: {r: 0, c: 0}, // A3
        e: {r: 1, c: 12} // M4
      });

     // 更新!ref属性以反映新的数据范围
      const ref = sheet['!ref'];
      if (ref) {
        const refParts = ref.split(':');
        const startRef = refParts[0].match(/([A-Z]+)(\d+)/);
        const endRef = refParts[1].match(/([A-Z]+)(\d+)/);
        const newStartRef = `${startRef[1]}${parseInt(startRef[2], 10) + 2}`;
        const newEndRef = `${endRef[1]}${maxRow}`;
        updatedSheet['!ref'] = `${newStartRef}:${newEndRef}`;
      }

      wb.Sheets[newSheetName] = updatedSheet;
      wb.Sheets[newSheetName]['!ref'] = 'A1:P21'
      this.getDistrict()
      console.log(this.chartName)
      wb.Sheets['附件3 居民收入摸底排查汇总表'].A1 = {t: 's', v: '炳草岗街道'+ this.chartName +'社区居民收入摸底排查汇总表'}
      let wbout = XLSX.write(wb, {
        bookType: 'xlsx',
        bookSST: false,
        type: 'array'
      });
      FileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), '附件3.xlsx');
      return wbout;
    },
    getSummaries (param) {
      const {columns, data} = param

      let [one, two, three, four, five] = [0, 0, 0, 0, 0]

      data.map((ele) => {
        ele.familyCountEntityList.map((count) => {
          if (count.familySize === '1口之家') {
            one += Number(count.familysCount)
          } else if (count.familySize === '2口之家') {
            two += Number(count.familysCount)
          } else if (count.familySize === '3口之家') {
            three += Number(count.familysCount)
          } else if (count.familySize === '4口之家') {
            four += Number(count.familysCount)
          } else if (count.familySize === '5口之家及以上') {
            five += Number(count.familysCount)
          }
        })
      })
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        const values = data.map(item => Number(item[column.property]))
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!isNaN(value)) {
              return Math.floor((prev + curr) * 100) / 100
            } else {
              return prev
            }
          }, 0)
          sums[index] += ''
        } else {
          sums[index] = 'N/A'
        }
      })
      sums[2] = 'N/A'
      sums[5] = one
      sums[6] = two
      sums[7] = three
      sums[8] = four
      sums[9] = five
      sums[11] = Math.round((sums[10] / sums[3]) * 100) / 100
      return sums
    },
    //
    indexMethod (index) {
      return (index * 1) + 1
    },
    // eslint-disable-next-line standard/object-curly-even-spacing
    rowClass ({row, column, rowIndex, columnIndex}) {
      if (rowIndex === 0 && columnIndex === 1) {
        this.$nextTick(() => {
          if (document.getElementsByClassName(column.id).length !== 0) {
            document.getElementsByClassName(column.id)[0].setAttribute('rowSpan', 2)
            return false
          }
        })
      }
      if (rowIndex === 1 && (columnIndex === 0 || columnIndex === 1)) {
        return {display: 'none'}
      }
    },
    // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/jdbmanage/income/listArr'),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.dataList = data.list
        } else {
          this.dataList = []
        }
        this.dataListLoading = false
        this.loading = false
      })
    },
    objectSpanMethod ({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 1) {
        // 获取当前单元格的值
        const currentValue = row[column.property]

        // 获取上一行相同列的值
        const preRow = this.dataList[rowIndex - 1]

        const preValue = preRow ? preRow[column.property] : null
        // 如果当前值和上一行的值相同，则将当前单元格隐藏
        if (currentValue === preValue) {
          return {rowspan: 0, colspan: 0}
        } else {
          // 否则计算当前单元格应该跨越多少行
          let rowspan = 1
          for (let i = rowIndex + 1; i < this.dataList.length; i++) {
            const nextRow = this.dataList[i]
            const nextValue = nextRow[column.property]
            if (nextValue === currentValue) {
              rowspan++
            } else {
              break
            }
          }
          return {rowspan, colspan: 1}
        }
      }
    }
  }
}
</script>
<style scoped>
/deep/ .el-table th {
  text-align: center !important; /* 确保文本居中 */
  background-color: #f2f2f2; /* 表头的背景色 */
  color: #333; /* 表头的字体颜色 */
  font-size: 16px; /* 表头的字体大小 */
}
/deep/ .el-table .cell, .el-table th div {
  text-align: center;
}
.exportBtn{
  margin-bottom: 10px;
}
</style>
