<template>
  <BasePage title="Students">
    <q-card flat bordered class="q-mb-md">
      <q-card-section>
        <div class="q-pa-md">
          <div class="row q-col-gutter-md">
            <q-select
              outlined
              v-model="courseNameData"
              :options="courseNames"
              class="col-3"
              label="Course"
            />
            <q-select
              outlined
              v-model="statusData"
              :options="statusOptions"
              class="col-3"
              label="Coop Status"
            />
            <q-select
              outlined
              v-model="yearData"
              :options="years"
              class="col-3"
              label="Years"
            />
            <q-select
              outlined
              v-model="termData"
              :options="terms"
              class="col-3"
              label="Term"
            />
          </div>
        </div>
        <div class="q-pa-md">
          <q-btn @click="viewNewStudents" class="largeFilterBtn">
            New Students Only
          </q-btn>
        </div>
        <div class="q-pa-md">
          <q-btn @click="viewAllStudents" class="largeFilterBtn">
            All Students
          </q-btn>
        </div>
        <div class="q-pa-md">
          <q-btn @click="applyFilter" class="filterBtn">Apply Filter</q-btn>
          <q-btn @click="clearFilter" class="filterBtn">Clear Filter</q-btn>
        </div>

        <div v-if="loading" class="center-item">
          <q-spinner color="primary" size="3em"></q-spinner>
        </div>
        <div v-else class="q-pa-md">
          <q-table
            :data="students"
            :columns="columns"
            row-key="email"
            @row-click="goToStudentCoop"
            selection="multiple"
            :selected.sync="selected"
          />
          <q-btn
            class="dashBtn"
            label="Send Notification to Selected Students"
            color="primary"
            @click="sendNotif"
          />
        </div>
      </q-card-section>
    </q-card>
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";

export default {
  name: "AdminStudentsPage",
  components: {
    BasePage
  },
  props: {
    courseName: {
      type: String,
      default: ""
    },
    year: {
      type: String,
      default: ""
    },
    status: {
      type: String,
      default: ""
    },
    term: {
      type: String,
      default: ""
    },
    newStudents: {
      type: Boolean,
      default: false
    }
  },
  data: function() {
    return {
      loading: true,
      selected: [],
      students: [],
      courseNames: [],
      courseNameData: "",
      statusOptions: [],
      statusData: "",
      terms: [],
      termData: "",
      years: [],
      yearData: "",
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
    };
  },
  created: function() {
    this.courseNameData = this.courseName;
    this.yearData = this.year;
    this.termData = this.term;
    this.statsData = this.status;

    if (this.newStudents === false) {
      this.$axios
        .get("/students", {
          headers: {
            Authorization: this.$store.state.token
          },
          params: {
            season: this.termData,
            year: this.yearData,
            name: this.courseNameData,
            status: this.statusData
          }
        })
        .then(resp => {
          this.students = resp.data;
          this.loading = false;
        });
    } else {
      this.$axios.get("/students/new").then(resp => {
        this.students = resp.data;
        this.loading = false;
      });
    }

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
    this.$axios
      .get("/course-offerings/seasons", {
        headers: {
          Authorization: this.$store.state.token
        }
      })
      .then(resp => {
        this.terms = resp.data;
      });

    this.$axios.get("/courses/names", {}).then(resp => {
      this.courseNames = resp.data;
    });
  },
  methods: {
    goToStudentCoop: function() {
      this.$router.push("/admin/student-coops");
    },
    sendNotif: function() {
      this.$router.push({
        path: "/admin/notification",
        name: "CreateNotif",
        params: {
          selected: this.selected
        }
      });
    },
    viewNewStudents: function() {
      this.loading = true;
      this.$axios.get("/students/new").then(resp => {
        this.students = resp.data;
        this.courseNameData = "";
        this.termData = "";
        this.yearData = "";
        this.statsData = "";
        this.loading = false;
      });
    },
    viewAllStudents: function() {
      this.loading = true;
      this.$axios.get("/students").then(resp => {
        this.students = resp.data;
        this.courseNameData = "";
        this.termData = "";
        this.yearData = "";
        this.statsData = "";
        this.loading = false;
      });
    },
    clearFilter: function() {
      this.loading = true;
      this.courseNameData = "";
      this.termData = "";
      this.yearData = "";
      this.statsData = "";
      this.$axios
        .get("/students", {
          headers: {
            Authorization: this.$store.state.token
          }
        })
        .then(resp => {
          this.students = resp.data;
          this.loading = false;
        });
    },
    applyFilter: function() {
      this.loading = true;
      this.$axios
        .get("/students", {
          headers: {
            Authorization: this.$store.state.token
          },
          params: {
            season: this.termData,
            year: this.yearData,
            name: this.courseNameData,
            status: this.statusData
          }
        })
        .then(resp => {
          this.students = resp.data;
          this.loading = false;
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.dashBtn {
  width: 40%;
  margin-top: 4%;
}

.filterBtn {
  width: 19%;
  margin-right: 2%;
}

.largeFilterBtn {
  width: 40%;
  margin-right: 2%;
}

.center-item {
  text-align: center;
}
</style>
