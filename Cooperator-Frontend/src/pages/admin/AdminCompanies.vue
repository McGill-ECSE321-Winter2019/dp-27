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
        name: 'companyName',
        required: true,
        label: 'Company Name',
        align: 'left',
        field: 'companyName',
        sortable: true,
        classes: 'my-class',
        style: 'width: 500px'
      },
      {
        name: 'location',
        required: true,
        label: 'Location',
        align: 'left',
        field: 'location',
        sortable: true,
        classes: 'my-class',
        style: 'width: 500px'
      },
      {
        name: 'currentCoops',
        required: true,
        label: 'Current Coops',
        align: 'left',
        field: 'currentCoops',
        sortable: true,
        classes: 'my-class',
        style: 'width: 500px'
      }],
    tableData: [
      {
        companyName: 'Facebook',
        location: 'USA',
        currentCoops: '3'
      },
      {
        companyName: 'Lightspeed',
        location: 'Montreal',
        currentCoops: '2'
      }]
  })
}
</script>

<style lang="scss">
</style>
