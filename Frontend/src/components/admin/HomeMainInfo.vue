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
        @click="viewToApprove"
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
  props: {
    course_name_p: {
      type: String
    },
    season_p: {
      type: String
    },
    status_p: {
      type: String
    },
    year_p: {
      type: String
    }
  },
  data() {
    return {
      course_names: [],
      course_name: "",
      season: "",
      year: "",
      status: ""
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
        name: "loadofshit",
        params: {
          course_name_p: cname,
          year_p: year,
          term_p: term
        }
      });
    },
    viewToApprove() {
      this.$router.push({
        path: "/admin/coops/review",
        name: "Review",
        params: {
          currentTab_p: "new_coop"
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
          year_p: year,
          term_p: term
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
  height: 60%;
  width: 40%;
  margin: 2%;
}
</style>
