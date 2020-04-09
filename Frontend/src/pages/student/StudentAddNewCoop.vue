<template>
  <BasePage title="Submit a New Co-op">
    <q-card flat bordered class="q-mb-md">
      <q-form @submit="onSubmit" @reset="onReset">
        <q-card-section>
          <div class="text-h6 q-mb-sm">Course Information</div>
          <div class="text-caption q-mb-md">
            This is the school term during which your co-op will take place.
          </div>

          <!-- Show spinner while loading -->
          <div v-if="loading" class="center-item">
            <q-spinner color="primary" size="3em" />
          </div>
          <!-- Show actual inputs -->
          <div v-else class="section">
            <q-select
              outlined
              v-model="courseTerm"
              :options="courseTermOptions"
              label="Course term"
              :rules="[
                val => (val && val.length > 0) || 'Please select a course term'
              ]"
            />
            <q-input
              outlined
              v-model.number="courseYear"
              type="number"
              label="Course year"
              :rules="[
                val =>
                  (val && val > 2010 && val < 2100) ||
                  'Please enter a valid course year'
              ]"
            />
            <q-select
              outlined
              v-model="courseName"
              :options="courseNameOptions"
              label="Course name"
              :rules="[
                val => (val && val.length > 0) || 'Please select a course name'
              ]"
            />
          </div>
        </q-card-section>

        <q-separator inset />

        <q-card-section>
          <div class="text-h6 q-mb-sm">Offer Letter</div>
          <div class="text-caption q-mb-md">
            Please attach your signed offer letter indicating you have accepted
            a co-op position. The offer letter will be reviewed by a co-op
            administrator, who will then approve or reject your application.
          </div>

          <div class="section">
            <q-file
              outlined
              v-model="offerLetterFile"
              label="Attach offer letter"
              class="q-mb-md"
              lazy-rules
              accept=".pdf"
              :rules="[val => val != null || 'Please upload a file']"
            >
              <template v-slot:prepend>
                <q-icon name="attach_file" />
              </template>
            </q-file>
          </div>

          <!-- Disable buttons while submitting -->
          <q-btn
            label="Submit"
            type="submit"
            color="primary"
            :disabled="submitting"
          />
          <q-btn
            label="Reset"
            type="reset"
            color="primary"
            flat
            class="q-ml-sm"
            :disabled="submitting"
          />
          <!-- Show spinner while submitting -->
          <q-spinner v-if="submitting" color="primary" size="2.5em" />
        </q-card-section>
      </q-form>
    </q-card>
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";

export default {
  name: "StudentAddNewCoop",
  components: {
    BasePage
  },
  data() {
    return {
      offerLetterFile: null,
      courseTerm: "",
      courseTermOptions: ["Winter", "Summer", "Fall"],
      courseYear: new Date().getFullYear(),
      courseName: "",
      courseNameOptions: [],
      submitting: false,
      loading: true
    };
  },
  created: function() {
    this.$axios.get("/courses/names").then(resp => {
      resp.data.forEach(name => {
        this.courseNameOptions.push(name);
      });
      this.loading = false;
    });
  },
  methods: {
    onSubmit: function() {
      // first create the coop, then the report
      const coop = {
        status: "UNDER_REVIEW",
        courseOffering: {
          year: this.courseYear,
          season: this.courseTerm.toUpperCase(),
          course: {
            name: this.courseName
          }
        },
        student: this.$store.state.currentUser
      };

      this.submitting = true;
      // POST request to create coop
      this.$axios
        .post("/coops", coop)
        .then(resp => {
          // after coop is created, POST request for offer letter
          const formData = new FormData();
          formData.append("file", this.offerLetterFile);
          formData.append("status", "UNDER_REVIEW");
          formData.append("title", "Offer Letter");
          formData.append("coopId", resp.data.id);
          formData.append("authorId", this.$store.state.currentUser.id);

          this.$axios
            .post("/reports", formData, {
              headers: { "Content-Type": "multipart/form-data" }
            })
            .then(resp => {
              this.submitting = false;

              this.$q.notify({
                color: "green-4",
                position: "top",
                textColor: "white",
                icon: "cloud_done",
                message: "Co-op Submitted Successfully"
              });
              this.$router.push({ path: "/student/home" });
            })
            .catch(err => {
              this.submitting = false;
              this.$q.notify({
                color: "red-4",
                position: "top",
                textColor: "white",
                icon: "error",
                message: "Something went wrong, please try again"
              });
            });
        })
        .catch(err => {
          this.submitting = false;
          this.$q.notify({
            color: "red-4",
            position: "top",
            textColor: "white",
            icon: "error",
            message: "Something went wrong, please try again"
          });
        });
    },
    onReset: function() {
      this.courseTerm = "";
      this.courseYear = null;
      this.courseName = "";
      this.offerLetterFile = null;
    }
  }
};
</script>

<style lang="scss" scoped>
#container {
  width: 75%;
}

.center-item {
  text-align: center;
}

.section {
  max-width: 250px;
}
</style>
