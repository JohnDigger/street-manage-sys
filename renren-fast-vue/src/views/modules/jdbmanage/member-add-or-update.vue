<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="家庭收入信息外键，关联family_income表" prop="incomeId">
      <el-input v-model="dataForm.incomeId" placeholder="家庭收入信息外键，关联family_income表"></el-input>
    </el-form-item>
    <el-form-item label="家庭成员姓名" prop="name">
      <el-input v-model="dataForm.name" placeholder="家庭成员姓名"></el-input>
    </el-form-item>
    <el-form-item label="性别" prop="gender">
      <el-input v-model="dataForm.gender" placeholder="性别"></el-input>
    </el-form-item>
    <el-form-item label="年龄" prop="age">
      <el-input v-model="dataForm.age" placeholder="年龄"></el-input>
    </el-form-item>
    <el-form-item label="联系电话" prop="phone">
      <el-input v-model="dataForm.phone" placeholder="联系电话"></el-input>
    </el-form-item>
    <el-form-item label="工作单位" prop="unit">
      <el-input v-model="dataForm.unit" placeholder="工作单位"></el-input>
    </el-form-item>
    <el-form-item label="年收入（万元）" prop="income">
      <el-input v-model="dataForm.income" placeholder="年收入（万元）"></el-input>
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
          incomeId: '',
          name: '',
          gender: '',
          age: '',
          phone: '',
          unit: '',
          income: ''
        },
        dataRule: {
          incomeId: [
            { required: true, message: '家庭收入信息外键，关联family_income表不能为空', trigger: 'blur' }
          ],
          name: [
            { required: true, message: '家庭成员姓名不能为空', trigger: 'blur' }
          ],
          gender: [
            { required: true, message: '性别不能为空', trigger: 'blur' }
          ],
          age: [
            { required: true, message: '年龄不能为空', trigger: 'blur' }
          ],
          phone: [
            { required: true, message: '联系电话不能为空', trigger: 'blur' }
          ],
          unit: [
            { required: true, message: '工作单位不能为空', trigger: 'blur' }
          ],
          income: [
            { required: true, message: '年收入（万元）不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/jdbmanage/member/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.incomeId = data.member.incomeId
                this.dataForm.name = data.member.name
                this.dataForm.gender = data.member.gender
                this.dataForm.age = data.member.age
                this.dataForm.phone = data.member.phone
                this.dataForm.unit = data.member.unit
                this.dataForm.income = data.member.income
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
              url: this.$http.adornUrl(`/jdbmanage/member/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'incomeId': this.dataForm.incomeId,
                'name': this.dataForm.name,
                'gender': this.dataForm.gender,
                'age': this.dataForm.age,
                'phone': this.dataForm.phone,
                'unit': this.dataForm.unit,
                'income': this.dataForm.income
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
