<template>
  <BasePage title="Check Enrollment">
    <q-card bordered flat>
      <q-card-section>
        <div v-if="loading" class="center-item">
          <!-- Show spinner while loading -->
          <q-spinner color="primary" size="3em" />
        </div>
        <div v-else>
          <div class="text-caption q-mb-md">
            Select the semester where you would like to check students'
            enrollment in their respective courses and/or if they have created a
            corresponding co-op yet.
          </div>
          <div class="input-section">
            <q-select
              outlined
              class="q-mb-sm"
              v-model="selectedYear"
              :options="yearList"
              label="Select Year"
            />
            <q-select
              outlined
              class="q-mb-sm"
              v-model="selectedSeason"
              :options="seasonList"
              label="Select Season"
            />
            <q-select
              outlined
              class="q-mb-sm"
              v-model="selectedCourse"
              :options="courseList"
              label="Select Course"
            />
            <q-file
              outlined
              class="q-mb-sm"
              v-model="csvFile"
              label="Upload Course CSV"
              accept=".csv"
            >
              <template v-slot:prepend>
                <q-icon name="attach_file" />
              </template>
            </q-file>

            <q-btn
              color="primary"
              label="Check Students"
              :loading="uploading"
              @click="parseCsv"
            />
          </div>
        </div>
      </q-card-section>
    </q-card>

    <div id="container" class="row flex justify-center" v-if="submitted">
      <div class="col">
        <q-card flat bordered class="q-mb-md">
          <q-card-section>
            <div class="text-h6 q-mb-md">Students who have not enrolled</div>
          </q-card-section>
          <div v-if="unenrolledStudents.length > 0" class="q-ml-md">
            <q-chip
              icon="email"
              size="md"
              v-for="student in unenrolledStudents"
              :key="student"
            >
              {{ student }}
            </q-chip>
          </div>
          <q-card-actions vertical align="right">
            <q-btn @click="showPopup = true" flat>Notify all</q-btn>
            <q-btn
              v-clipboard:copy="unenrolledStudents"
              v-clipboard:success="onCopy"
              flat
              >Copy List</q-btn
            >
          </q-card-actions>
        </q-card>

        <q-card flat bordered class="q-mb-md">
          <q-card-section>
            <div class="text-h6 q-mb-md">
              Students who have not created a new Co-op
            </div>
          </q-card-section>
          <div v-if="unregisteredStudents.length > 0" class="q-ml-md">
            <q-chip
              icon="email"
              size="md"
              v-for="student in unregisteredStudents"
              :key="student"
            >
              {{ student }}
            </q-chip>
          </div>
          <q-card-actions vertical align="right">
            <q-btn
              v-clipboard:copy="unregisteredStudents"
              v-clipboard:success="onCopy"
              flat
              >Copy List</q-btn
            >
          </q-card-actions>
        </q-card>

        <q-dialog v-model="showPopup">
          <NotificationPopup
            :title="selectedCourse"
            :course="specificCourse"
            :students="unenrolledStudents"
          />
        </q-dialog>
      </div>
    </div>
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";
import NotificationPopup from "../../components/admin/NotificationPopup.vue";

export default {
  name: "AdminVerifyCsvPage",
  components: {
    BasePage,
    NotificationPopup
  },
  data: function() {
    return {
      coursesLoading: true,
      seasonsLoading: true,
      yearsLoading: true,
      selectedYear: "",
      yearList: [],
      seasonList: [],
      selectedSeason: "",
      courseList: [],
      selectedCourse: "",
      unenrolledStudents: [],
      unregisteredStudents: [],
      csvFile: null,
      uploading: false,
      courseOfferingId: "",
      showPopup: false,
      specificCourse: [],
      submitted: false
    };
  },
  created: function() {
    this.$axios.get("/courses").then(resp => {
      resp.data.forEach(course => {
        this.courseList.push(course.name);
        this.coursesLoading = false;
      });
    });
    this.$axios.get("/seasons").then(resp => {
      resp.data.forEach(season => {
        this.seasonList.push(season);
        this.seasonsLoading = false;
      });
    });
    this.$axios.get("/course-offerings/years").then(resp => {
      resp.data.forEach(year => {
        this.yearList.push(year);
        this.yearsLoading = false;
      });
    });
  },
  computed: {
    loading: function() {
      return this.coursesLoading || this.seasonsLoading || this.yearsLoading;
    }
  },
  methods: {
    parseCsv: function() {
      this.uploading = true;

      this.$axios
        .get("/course-offerings/single-offering", {
          params: {
            year: this.selectedYear,
            season: this.selectedSeason,
            courseName: this.selectedCourse
          }
        })
        .then(resp => {
          this.specificCourse = resp.data;
          this.courseOfferingId = resp.data.id;
          const formData = new FormData();
          formData.append("file", this.csvFile);
          formData.append("course_id", this.courseOfferingId);

          this.$axios
            .post("/csv-parser/check-registered", formData, {
              headers: { "Content-Type": "multipart/form-data" }
            })
            .then(resp => {
              this.unregisteredStudents = resp.data;
            });

          this.$axios
            .post("/csv-parser/check-enrollment", formData, {
              headers: { "Content-Type": "multipart/form-data" }
            })
            .then(resp => {
              this.unenrolledStudents = resp.data;
            });

          this.uploading = false;
          this.submitted = true;
        });
    },
    onCopy: function() {
      this.$q.notify({
        color: "blue-grey-4",
        position: "top",
        textColor: "white",
        icon: "assignment_turned_in",
        message: "Copied Successfully"
      });
    },
    notifyEnrolled: function() {
      unenrolledStudents.forEach(email => {
        this.$axios
          .get("/students", {
            params: {
              email: email
            }
          })
          .then(resp => {
            this.$axios.post();
          });
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.input-section {
  width: 50%;
}

.center-item {
  text-align: center;
}
</style>
