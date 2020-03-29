<template>
  <div>
    <q-card-section>All Students</q-card-section>

    <q-separator inset />
    <div class="q-pa-md">
      <q-select
        v-model="course_name"
        :options="course_names"
        label="Course"
        style="width:25%"
      />
      <q-select
        v-model="status"
        :options="statusOptions"
        label="Coop Status"
        style="width:25%"
      />
      <q-select
        v-model="year"
        :options="years"
        label="Years"
        style="width:25%"
      />
      <q-select
        v-model="season"
        :options="seasons"
        label="Term"
        style="width:25%"
      />
    </div>
    <div class="q-pa-md">
      <q-btn @click="applyFilter" class="q-mr-sm">Apply Filter</q-btn>
      <q-btn @click="clearFilter" class="q-mr-sm">Clear Filter</q-btn>
    </div>

    <div class="q-pa-md">
      <q-table
        :data="students"
        :columns="columns"
        row-key="studentName"
        @row-click="goToStudentCoop"
      />
    </div>
  </div>
</template>
<script>
export default {
  name: "AdminStudentsPage",
  data: () => ({
    students: [],
    course_names: [],
    course_name: "",
    statusOptions: [],
    status: "",
    seasons: [],
    season: "",
    years: [],
    year: "",
    columns: [
      {
        name: "studentLastName",
        required: true,
        label: "Student Last Name",
        align: "left",
        field: "lastName",
        sortable: true,
        classes: "my-class",
        style: "width: 500px"
      },
      {
        name: "studentFirstName",
        required: true,
        label: "Student First Name",
        align: "left",
        field: "firstName",
        sortable: true,
        classes: "my-class",
        style: "width: 500px"
      },
      {
        name: "studentID",
        required: true,
        label: "Student ID",
        align: "left",
        field: "id",
        sortable: true,
        classes: "my-class",
        style: "width: 500px"
      },
      {
        name: "email",
        required: true,
        label: "Email",
        align: "left",
        field: "email",
        sortable: true,
        classes: "my-class",
        style: "width: 500px"
      }
    ]
  }),
  created: function() {
    this.$axios
      .get("/students", {
        headers: {
          Authorization: this.$store.state.token
        }
      })
      .then(resp => {
        this.students = resp.data;
      });
    this.$axios
      .get("/coops/status", {
        headers: {
          Authorization: this.$store.state.token
        }
      })
      .then(resp => {
        this.statusOptions = resp.data;
      });
    this.years = this.$common.getYears();
    console.log(this.$common.getYears());
    this.$axios
      .get("/course-offerings/seasons", {
        headers: {
          Authorization: this.$store.state.token
        }
      })
      .then(resp => {
        this.seasons = resp.data;
      });
    this.$axios.get("/courses/names", {}).then(resp => {
      this.course_names = resp.data;
    });
  },
  methods: {
    goToStudentCoop() {
      this.$router.push("/admin/studentcoops");
    },
    clearFilter() {
      this.course_name = "";
      this.season = "";
      this.year = "";
      this.status = "";
      this.$axios
        .get("/students", {
          headers: {
            Authorization: this.$store.state.token
          }
        })
        .then(resp => {
          this.students = resp.data;
        });
    },
    applyFilter() {
      this.$axios
        .get(
          "/students?season=" +
            this.season +
            "&year=" +
            this.year +
            "&name=" +
            this.course_name +
            "&status=" +
            this.status,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          }
        )
        .then(resp => {
          this.students = resp.data;
        });
    }
  }
};
</script>

<style lang="scss"></style>
