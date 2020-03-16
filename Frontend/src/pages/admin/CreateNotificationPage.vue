<template>
  <q-card flat bordered id="card">
    <q-card-section>
      <div class="text-h6">Send Notification</div>
    </q-card-section>

    <q-separator inset />

    <div class="q-pa-md">
      <q-table
        title="Send to"
        :data="toSend"
        :columns="columns"
        row-key="studentName"
      />
      <q-input filled v-model="text" label="Title" style="margin-top: 3%" />
      <q-input v-model="text" filled type="textarea" style="margin-top: 3%" />
      <q-btn color="primary" label="Send Notification" style="margin-top: 3%" />
    </div>
  </q-card>
</template>

<script>
// import HomeCurrentCoopStudentItem from 'components/admin/HomeCurrentCoopStudentItem.vue'
export default {
  name: "HomeMainInfo",
  toSendTo: {
    type: Array,
    required: true
  },
  data: () => ({
    columns: [
      {
        name: "studentName",
        required: true,
        label: "Student Name",
        align: "left",
        field: "studentName",
        sortable: true,
        classes: "my-class",
        style: "width: 500px"
      },
      {
        name: "Email",
        required: true,
        label: "Email",
        align: "left",
        field: "email",
        sortable: true,
        classes: "my-class",
        style: "width: 500px"
      },
      {
        name: "Student ID",
        required: true,
        label: "Student ID",
        align: "left",
        field: "studentID",
        sortable: true,
        classes: "my-class",
        style: "width: 500px"
      }
    ]
  }),
  methods: {
    goToStudentCoop() {
      this.$router.push("/admin/studentcoops");
    }
  }
};
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
