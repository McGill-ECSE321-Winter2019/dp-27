<template>
  <BasePage title="Co-ops">
    <q-card flat bordered>
      <q-table :data="tableData" :columns="columns" row-key="coopTitle" />
    </q-card>
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";

export default {
  name: "AdminAllCoops",
  components: {
    BasePage
  },
  data: () => ({
    columns: [
      {
        name: "title",
        required: true,
        label: "Title",
        align: "left",
        field: "title",
        sortable: true,
        classes: "my-class",
        style: "width: 500px"
      },
      {
        name: "Company",
        required: true,
        label: "Company",
        align: "left",
        field: "companyName",
        sortable: true,
        classes: "my-class",
        style: "width: 500px"
      },
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
        name: "status",
        required: true,
        label: "Status",
        align: "left",
        field: "status",
        sortable: true,
        classes: "my-class",
        style: "width: 500px"
      }
    ],
    tableData: [
      {
        title: "Backend Intern",
        studentName: "Emma",
        companyName: "Lightspeed",
        status: "Complete"
      },
      {
        title: "SWE Intern",
        studentName: "Albert",
        companyName: "Facebook",
        status: "Future"
      },
      {
        title: "Dev Intern",
        studentName: "Paul",
        companyName: "CSA",
        status: "In Progress"
      }
    ]
  })
};
</script>

<style lang="scss" scoped></style>
