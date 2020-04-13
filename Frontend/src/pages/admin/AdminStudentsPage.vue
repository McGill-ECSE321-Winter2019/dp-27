<template>
  <BasePage title="Students">
    <q-card flat bordered>
      <q-card-section>
        <div class="q-pa-md">
          <q-select
            v-model="courseNameData"
            :options="courseNames"
            label="Course"
            style="width:25%"
          />
          <q-select
            v-model="statsData"
            :options="statusOptions"
            label="Coop Status"
            style="width:25%"
          />
          <q-select
            v-model="yearData"
            :options="years"
            label="Years"
            style="width:25%"
          />
          <q-select
            v-model="termData"
            :options="terms"
            label="Term"
            style="width:25%"
          />
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

        <div class="q-pa-md">
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
  data: () => ({
    selected: [],
    students: [],
    courseNames: [],
    courseNameData: "",
    statusOptions: [],
    statsData: "",
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
  }),
  created: function() {
    this.courseNameData = this.courseName;
    this.yearData = this.year;
    this.termData = this.term;
    this.statsData = this.status;
    console.log(this.newStudents);
    if (this.newStudents === false) {
      console.log("getting all");
      this.$axios
        .get(
          "/students?season=" +
            this.termData +
            "&year=" +
            this.yearData +
            "&name=" +
            this.courseNameData +
            "&status=" +
            this.statsData,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          }
        )
        .then(resp => {
          this.students = resp.data;
        });
    } else {
      this.$axios.get("/students/new").then(resp => {
        this.students = resp.data;
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
    goToStudentCoop() {
      this.$router.push("/admin/student-coops");
    },
    sendNotif() {
      this.$router.push({
        path: "/admin/notification",
        name: "CreateNotif",
        params: {
          selected: this.selected
        }
      });
    },
    viewNewStudents() {
      this.$axios.get("/students/new").then(resp => {
        this.students = resp.data;
        this.courseNameData = "";
        this.termData = "";
        this.yearData = "";
        this.statsData = "";
      });
    },
    viewAllStudents() {
      this.$axios.get("/students").then(resp => {
        this.students = resp.data;
        this.courseNameData = "";
        this.termData = "";
        this.yearData = "";
        this.statsData = "";
      });
    },
    clearFilter() {
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
        });
    },
    applyFilter() {
      this.$axios
        .get(
          "/students?season=" +
            this.termData +
            "&year=" +
            this.yearData +
            "&name=" +
            this.courseNameData +
            "&status=" +
            this.statsData,
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

<style lang="scss">
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
</style>
