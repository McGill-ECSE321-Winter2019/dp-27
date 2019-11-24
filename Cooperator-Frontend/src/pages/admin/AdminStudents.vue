<template>
  <q-card
    flat
    bordered
    id="card"
  >
    <q-card-section>All Students
    </q-card-section>

    <q-separator inset />

    <div class="q-pa-md">
    <q-table
      :data="tableData"
      :columns="columns"
      row-key="studentName"
      @row-click="goToStudentCoop"
    />
    </div>
  </q-card>
</template>
<script>
export default {
  name: 'AdminAllStudents',
  data: () => ({
    columns: [
      {
        name: 'studentName',
        required: true,
        label: 'Student Name',
        align: 'left',
        field: 'studentName',
        sortable: true,
        classes: 'my-class',
        style: 'width: 500px'
      },
      {
        name: 'studentID',
        required: true,
        label: 'Student ID',
        align: 'left',
        field: 'studentID',
        sortable: true,
        classes: 'my-class',
        style: 'width: 500px'
      },
      {
        name: 'year',
        required: true,
        label: 'Year',
        align: 'left',
        field: 'year',
        sortable: true,
        classes: 'my-class',
        style: 'width: 500px'
      },
      {
        name: 'coopsCompleted',
        required: true,
        label: 'Coops Completed',
        align: 'left',
        field: 'coopsCompleted',
        sortable: true,
        classes: 'my-class',
        style: 'width: 500px'
      }],
    tableData: [
      {
        studentName: 'Emma',
        studentID: '260709533',
        year: 'U3',
        coopsCompleted: '3'
      },
      {
        studentName: 'Albert',
        studentID: '260712312',
        year: 'U3',
        coopsCompleted: '2'
      }]
  }),
  methods: {
    goToStudentCoop () {
      this.$router.push('/admin/studentcoops')
    }
  }
}
</script>

<style lang="scss">
</style>
