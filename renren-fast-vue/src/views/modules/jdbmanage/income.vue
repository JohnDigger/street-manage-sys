<template>
  <div class="mod-config">
    <el-table
      :data="dataList"
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
        <el-table-column label="" prop="incomeGroups" min-width="2%" > </el-table-column>
        <el-table-column label="" prop="subIncomeGroups" min-width="2%"></el-table-column>
      </el-table-column>
      <el-table-column
        prop="totalPopulation"
        label="总人数(常住)"
        min-width="1%">
      </el-table-column>
      <el-table-column label="家庭人口情况(户)" min-width="5%">
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
                <span v-if="a.familySize === '1口之家'">{{a.familysCount}}</span>
              </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="family.familyCountEntityList"
          label="2口之家"
          width="115">
          <template slot-scope="scope">
              <div v-for="a in scope.row.familyCountEntityList">
                <span v-if="a.familySize === '2口之家'">{{a.familysCount}}</span>
              </div>
          </template>
        </el-table-column>
        <el-table-column
          label="3口之家"
          prop="family.familyCountEntityList"
          width="115">
          <template slot-scope="scope">
              <div v-for="a in scope.row.familyCountEntityList">
                <span v-if="a.familySize === '3口之家'">{{a.familysCount}}</span>
              </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="family.familyCountEntityList"
          label="4口之家"
          width="115">
          <template slot-scope="scope">
              <div v-for="a in scope.row.familyCountEntityList">
                <span v-if="a.familySize === '4口之家'">{{a.familysCount}}</span>
              </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="familyCountEntityList"
          label="5口之家及以上"
          width="115">
          <template slot-scope="scope">
              <div v-for="a in scope.row.familyCountEntityList">
                <span v-if="a.familySize === '5口之家及以上'">{{a.familysCount}}</span>
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
        prop="name"
        label="占比(%)人"
        min-width="1%">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        dataList: [],
        dataListLoading: false,
        dataListSelections: [],
        loading: true
      }
    },
    activated () {
      this.getDataList()
    },
    methods: {
      getSummaries (param) {
        const { columns, data } = param
        const sums = []
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '合计'
            return
          }
          const values = data.map((item) => {
            if (column.property === 'familyCountEntityList'){
               item[column.property].map(e=>{
                 // console.log(e.familysCount)
               })
            }
            Number(item[column.property])
          })
        console.log(values)
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
        return sums
      },

      indexMethod (index) {
        return (index * 1) + 1
      },
      // eslint-disable-next-line standard/object-curly-even-spacing
      rowClass ({ row, column, rowIndex, columnIndex}) {
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
      objectSpanMethod ({ row, column, rowIndex, columnIndex }) {
        if (columnIndex === 1) {
          // 获取当前单元格的值
          const currentValue = row[column.property]

          // 获取上一行相同列的值
          const preRow = this.dataList[rowIndex - 1]

          const preValue = preRow ? preRow[column.property] : null
          // 如果当前值和上一行的值相同，则将当前单元格隐藏
          if (currentValue === preValue) {
            return { rowspan: 0, colspan: 0 }
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
            return { rowspan, colspan: 1 }
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
/deep/.el-table .cell, .el-table th div{
  text-align: center;
}
tr>th>div {
  text-align: center !important;
}
</style>
