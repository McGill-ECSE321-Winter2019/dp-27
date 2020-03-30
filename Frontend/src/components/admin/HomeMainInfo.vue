<template>
  <q-card flat bordered id="card">
    <q-card-section>
      <div class="text-h6">Welcome, Admin!</div>
    </q-card-section>

    <q-separator inset />

    <div class="q-pa-md">
      <q-btn
        class="dashBtn"
        label="View Current Coop Students"
        color="primary"
        @click="viewCurrentStudents"
      />
      <q-btn
        class="dashBtn"
        label="View Offers to Approve"
        color="primary"
        @click="viewApprovalsPage('new_coops')"
      />
      <q-btn
        class="dashBtn"
        label="View
      Completed Coops to Approve"
        color="primary"
        @click="viewApprovalsPage('completed_coops')"
      />
      <q-btn
        class="dashBtn"
        label="View Students with Late Reports"
        color="primary"
        @click="viewCurrentStudents"
      />
      <q-btn class="dashBtn" label="View Incoming Students" color="primary" />
      <q-btn
        v-for="cname in course_names"
        :key="cname"
        class="dashBtn"
        :label="cname"
        color="primary"
        @click="viewStudentsOfCourse(cname)"
      />
    </div>
  </q-card>
</template>

<script>
export default {
  name: "HomeMainInfo",
  data() {
    return {
      course_names: [],
      course_name_data: "",
      season_data: "",
      year_data: "",
      status_data: ""
    };
  },
  created: function() {
    this.$axios.get("/courses/names", {}).then(resp => {
      this.course_names = resp.data;
    });
  },
  methods: {
    goToStudentCoop() {
      this.$router.push("/admin/studentcoops");
    },
    viewStudentsOfCourse(cname) {
      var year = this.$common.getCurrentYear().toString();
      var term = this.$common.getCurrentTerm();
      this.$router.push({
        path: "/admin/students",
        name: "AdminViewStudents",
        params: {
          course_name: cname,
          year: year,
          term: term
        }
      });
    },
    viewApprovalsPage(tab) {
      this.$router.push({
        path: "/admin/coops/review",
        name: "Review",
        params: {
          currentTab: tab
        }
      });
    },
    viewCurrentStudents() {
      var year = this.$common.getCurrentYear().toString();
      var term = this.$common.getCurrentTerm();
      this.$router.push({
        path: "/admin/students",
        name: "loadofshit",
        params: {
          year: year,
          term: term
        }
      });
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

.dashBtn {
  width: 46%;
  margin: 2%;
  align: center;
}
</style>
