<template>
  <div class="mod-config">
    <el-table
      :data="dataList"
      v-loading="loading"
      border stripe
      :header-cell-style="{'text-align':'center'}"
      :cell-style="{'text-align':'center'}"
      style="width: 100%">
      <el-table-column
        prop="date"
        label="序号"
        min-width="1%">
      </el-table-column>
      <el-table-column
        prop="incomeGroups"
        label="人均收入档位"
        min-width="4%">
      </el-table-column>
      <el-table-column
        prop="totalPopulation"
        label="总人数(常住)"
        min-width="1%">
      </el-table-column>
      <el-table-column label="家庭人口情况(户)" min-width="10%">
        <el-table-column
          prop="totalFamilys"
          label="总户数"
          width="110">
        </el-table-column>
        <el-table-column
          prop="family.familyCountEntityList"
          label="1口之家"
          width="115">
          <template scope="scope">
              <div v-for="a in scope.row.familyCountEntityList">
                <span v-if="a.familySize === '1口之家'">{{a.familysCount}}</span>
              </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="family.familyCountEntityList"
          label="2口之家"
          width="115">
          <template scope="scope">
              <div v-for="a in scope.row.familyCountEntityList">
                <span v-if="a.familySize === '2口之家'">{{a.familysCount}}</span>
              </div>
          </template>
        </el-table-column>
        <el-table-column
          label="3口之家"
          prop="family.familyCountEntityList"
          width="115">
          <template scope="scope">
              <div v-for="a in scope.row.familyCountEntityList">
                <span v-if="a.familySize === '3口之家'">{{a.familysCount}}</span>
              </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="family.familyCountEntityList"
          label="4口之家"
          width="115">
          <template scope="scope">
              <div v-for="a in scope.row.familyCountEntityList">
                <span v-if="a.familySize === '4口之家'">{{a.familysCount}}</span>
              </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="family.familyCountEntityList"
          label="5口之家及以上"
          width="115">
          <template scope="scope">
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
        prop="name"
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
      }
    }
  }
</script>
