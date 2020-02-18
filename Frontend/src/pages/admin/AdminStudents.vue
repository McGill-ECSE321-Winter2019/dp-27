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
        :data="students"
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
    students: [],
    columns: [
      {
        name: 'studentName',
        required: true,
        label: 'Student Name',
        align: 'left',
        field: 'name',
        sortable: true,
        classes: 'my-class',
        style: 'width: 500px'
      },
      {
        name: 'studentID',
        required: true,
        label: 'Student ID',
        align: 'left',
        field: 'id',
        sortable: true,
        classes: 'my-class',
        style: 'width: 500px'
      },
      // {
      //   name: 'year',
      //   required: true,
      //   label: 'Year',
      //   align: 'left',
      //   field: 'year',
      //   sortable: true,
      //   classes: 'my-class',
      //   style: 'width: 500px'
      // },
      // {
      //   name: 'coopsCompleted',
      //   required: true,
      //   label: 'Coops Completed',
      //   align: 'left',
      //   field: 'coopsCompleted',
      //   sortable: true,
      //   classes: 'my-class',
      //   style: 'width: 500px'
      // }
    ]
  }),
  created: function (){
    const user = this.$store.state.currentUser;
    this.$axios.get("/students",{
          headers: {
            Authorization: this.$store.state.token
          }
        }).then(resp => {
      this.students = resp.data;
    });

  },
  methods: {
    goToStudentCoop () {
      this.$router.push('/admin/studentcoops')
    }
  }
}
</script>

<style lang="scss">
</style>
