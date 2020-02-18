<template>
  <q-card
    flat
    bordered
    id="card"
  >
    <q-card-section>
      <div class="text-h6">Welcome, Admin!</div>
    </q-card-section>

    <q-separator inset />

    <div class="q-pa-md">
    <q-table
      title="Current Coop Students"
      :data="tableData"
      :columns="columns"
      row-key="studentName"
      @row-click="goToStudentCoop"
    />
    </div>
  </q-card>
</template>

<script>
// import HomeCurrentCoopStudentItem from 'components/admin/HomeCurrentCoopStudentItem.vue'
export default {
  name: 'HomeMainInfo',
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
        name: 'Company',
        required: true,
        label: 'Company',
        align: 'left',
        field: 'companyName',
        sortable: true,
        classes: 'my-class',
        style: 'width: 500px'
      },
      {
        name: 'status',
        required: true,
        label: 'Status',
        align: 'left',
        field: 'status',
        sortable: true,
        classes: 'my-class',
        style: 'width: 500px'
      }],
    tableData: [
      {
        studentName: 'Emma',
        companyName: 'Lightspeed',
        status: 'Good'
      },
      {
        studentName: 'Albert',
        companyName: 'Facebook',
        status: 'Has Missing Documents'
      },
      {
        studentName: 'Paul',
        companyName: 'CSA',
        status: 'Good'
      },
      {
        studentName: 'Susan',
        companyName: 'Cisco',
        status: 'Good'
      }]
  }),
  methods: {
    goToStudentCoop () {
      this.$router.push('/admin/studentcoops')
    }
  }
}
</script>

<style scoped lang="scss">
h6 {
  margin: 10px;
}
#card {
  width: 100%;
  margin-top: 25px;
  margin-right: 10px;
}
</style>
