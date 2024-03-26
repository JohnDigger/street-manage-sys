<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="街道名称，如：1炳草岗街道,2瓜子坪街道" prop="street">
      <el-input v-model="dataForm.street" placeholder="街道名称，如：1炳草岗街道,2瓜子坪街道"></el-input>
    </el-form-item>
    <el-form-item label="是否常驻人口：0流动人口，1常驻人口" prop="isPermanent">
      <el-input v-model="dataForm.isPermanent" placeholder="是否常驻人口：0流动人口，1常驻人口"></el-input>
    </el-form-item>
    <el-form-item label="家庭地址" prop="address">
      <el-input v-model="dataForm.address" placeholder="家庭地址"></el-input>
    </el-form-item>
    <el-form-item label="家庭总人口数" prop="familyNum">
      <el-input v-model="dataForm.familyNum" placeholder="家庭总人口数"></el-input>
    </el-form-item>
    <el-form-item label="家庭年收入（万元）" prop="familyIncome">
      <el-input v-model="dataForm.familyIncome" placeholder="家庭年收入（万元）"></el-input>
    </el-form-item>
    <el-form-item label="家庭人均收入（万元）" prop="averageIncome">
      <el-input v-model="dataForm.averageIncome" placeholder="家庭人均收入（万元）"></el-input>
    </el-form-item>
    <el-form-item label="人均收入区间：1万元以下（含）：1；1万元至3.3万元（不含）：2；3.3万元以上（含）：3" prop="incomeInterval">
      <el-input v-model="dataForm.incomeInterval" placeholder="人均收入区间：1万元以下（含）：1；1万元至3.3万元（不含）：2；3.3万元以上（含）：3"></el-input>
    </el-form-item>
    <el-form-item label="备注" prop="remark">
      <el-input v-model="dataForm.remark" placeholder="备注"></el-input>
    </el-form-item>
    <el-form-item label="社区调查员" prop="officer">
      <el-input v-model="dataForm.officer" placeholder="社区调查员"></el-input>
    </el-form-item>
    <el-form-item label="调查时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="调查时间"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          street: '',
          isPermanent: '',
          address: '',
          familyNum: '',
          familyIncome: '',
          averageIncome: '',
          incomeInterval: '',
          remark: '',
          officer: '',
          createTime: ''
        },
        dataRule: {
          street: [
            { required: true, message: '街道名称，如：1炳草岗街道,2瓜子坪街道不能为空', trigger: 'blur' }
          ],
          isPermanent: [
            { required: true, message: '是否常驻人口：0流动人口，1常驻人口不能为空', trigger: 'blur' }
          ],
          address: [
            { required: true, message: '家庭地址不能为空', trigger: 'blur' }
          ],
          familyNum: [
            { required: true, message: '家庭总人口数不能为空', trigger: 'blur' }
          ],
          familyIncome: [
            { required: true, message: '家庭年收入（万元）不能为空', trigger: 'blur' }
          ],
          averageIncome: [
            { required: true, message: '家庭人均收入（万元）不能为空', trigger: 'blur' }
          ],
          incomeInterval: [
            { required: true, message: '人均收入区间：1万元以下（含）：1；1万元至3.3万元（不含）：2；3.3万元以上（含）：3不能为空', trigger: 'blur' }
          ],
          remark: [
            { required: true, message: '备注不能为空', trigger: 'blur' }
          ],
          officer: [
            { required: true, message: '社区调查员不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '调查时间不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/jdbmanage/income/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.street = data.income.street
                this.dataForm.isPermanent = data.income.isPermanent
                this.dataForm.address = data.income.address
                this.dataForm.familyNum = data.income.familyNum
                this.dataForm.familyIncome = data.income.familyIncome
                this.dataForm.averageIncome = data.income.averageIncome
                this.dataForm.incomeInterval = data.income.incomeInterval
                this.dataForm.remark = data.income.remark
                this.dataForm.officer = data.income.officer
                this.dataForm.createTime = data.income.createTime
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/jdbmanage/income/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'street': this.dataForm.street,
                'isPermanent': this.dataForm.isPermanent,
                'address': this.dataForm.address,
                'familyNum': this.dataForm.familyNum,
                'familyIncome': this.dataForm.familyIncome,
                'averageIncome': this.dataForm.averageIncome,
                'incomeInterval': this.dataForm.incomeInterval,
                'remark': this.dataForm.remark,
                'officer': this.dataForm.officer,
                'createTime': this.dataForm.createTime
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
