<!-- This component contains the buttons on the Admin's homepage. -->
<template>
  <q-card flat bordered class="q-mt-md">
    <q-card-section>
      <div class="text-h6">
        Welcome, {{ this.$store.state.currentUser.firstName }}!
      </div>
    </q-card-section>

    <q-separator inset />

    <q-card-section class="q-pa-md">
      <q-btn
        class="dash-button"
        label="View Current Coop Students"
        color="primary"
        @click="viewCurrentStudents"
      />
      <q-btn
        class="dash-button q-mt-md q-mb-md"
        label="View New Students"
        color="primary"
        @click="viewNewStudents()"
      />
      <q-btn
        class="dash-button q-mt-md q-mb-md"
        label="View Offer Letters to Approve"
        color="primary"
        @click="viewApprovalsPage('new_coops')"
      />
      <q-btn
        class="dash-button q-mt-md q-mb-md"
        label="View Completed Coops to Approve"
        color="primary"
        @click="viewApprovalsPage('completed_coops')"
      />
      <q-btn
        class="dash-button q-mt-md q-mb-md"
        label="View Students with Late Reports"
        color="primary"
        @click="viewCurrentStudents"
      />
      <q-btn
        class="dash-button q-mt-md q-mb-md"
        label="View Report Configurations"
        color="primary"
        @click="viewReportConfig"
      />

      <div class="text-h6 q-mt-sm q-mb-sm">View Current Students by Course</div>

      <q-separator />

      <div v-if="loading" class="center-item notifications-menu">
        <q-spinner color="primary" size="3em" class="q-ma-md" />
      </div>
      <div v-else>
        <q-btn
          v-for="cname in courseNames"
          :key="cname"
          class="dash-button q-mt-md q-mb-md"
          :label="cname"
          color="primary"
          @click="viewStudentsOfCourse(cname)"
        />
      </div>
    </q-card-section>
  </q-card>
</template>

<script>
export default {
  name: "HomeMainInfo",
  data() {
    return {
      loading: true,
      courseNames: []
    };
  },
  created: function() {
    this.$axios.get("/courses/names", {}).then(resp => {
      this.courseNames = resp.data;
      this.loading = false;
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
          courseName: cname,
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
    viewReportConfig() {
      this.$router.push({
        path: "/admin/report-config"
      });
    },
    viewCurrentStudents() {
      var year = this.$common.getCurrentYear().toString();
      var term = this.$common.getCurrentTerm();
      this.$router.push({
        path: "/admin/students",
        name: "AdminViewStudents",
        params: {
          year: year,
          term: term
        }
      });
    },
    viewNewStudents() {
      this.$router.push({
        path: "/admin/students",
        name: "AdminViewStudents",
        params: {
          newStudents: true
        }
      });
    }
  }
};
</script>

<style scoped lang="scss">
.dash-button {
  width: 46%;
  margin-left: 2%;
  margin-right: 2%;
  text-align: center;
}

.center-item {
  text-align: center;
}
</style>
