<template>
  <q-page class="flex justify-center">
    <div id="container" class="row">
      <div class="col">
        <h4>Submit a New Co-op</h4>

        <q-card flat bordered class="q-mb-md">
          <q-form @submit="onSubmit" @reset="onReset">
            <q-card-section>
              <div class="text-h6 q-mb-sm">Course Information</div>
              <div class="text-caption q-mb-md">
                This is the school term during which your co-op will take place.
              </div>

              <div class="section">
                <q-select
                  outlined
                  v-model="courseTerm"
                  :options="courseTermOptions"
                  label="Course term"
                  :rules="[
                    val =>
                      (val && val.length > 0) || 'Please select a course term'
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
              </div>
            </q-card-section>

            <q-separator inset />

            <q-card-section>
              <div class="text-h6 q-mb-sm">Offer Letter</div>
              <div class="text-caption q-mb-md">
                Please attach your signed offer letter indicating you have
                accepted a co-op position. The offer letter will be reviewed by
                a co-op administrator, who will then approve or reject your
                application.
              </div>

              <div class="section">
                <q-file
                  outlined
                  v-model="offerLetterFile"
                  label="Attach offer letter"
                  class="q-mb-md"
                >
                  <template v-slot:prepend>
                    <q-icon name="attach_file" />
                  </template>
                </q-file>
              </div>

              <q-btn label="Submit" type="submit" color="primary" />
              <q-btn
                label="Reset"
                type="reset"
                color="primary"
                flat
                class="q-ml-sm"
              />
              <q-spinner v-if="submitting" color="primary" size="3em" />
            </q-card-section>
          </q-form>
        </q-card>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
  name: "StudentAddNewCoop",
  data() {
    return {
      offerLetterFile: null,
      courseTerm: "",
      courseTermOptions: ["Winter", "Summer", "Fall"],
      courseYear: new Date().getFullYear(),
      submitting: false
    };
  },
  methods: {
    onSubmit: function() {
      // first create the coop, then the report
      const coop = {
        status: "UNDER_REVIEW",
        courseOffering: {
          year: this.courseYear,
          season: this.courseTerm.toUpperCase()
        },
        student: this.$store.state.currentUser
      };

      this.submitting = true;
      // POST request to create coop
      this.$axios
        .post("/coops", coop)
        .then(resp => {
          console.log(resp);
          console.log("Coop created");

          // after coop is created, POST request for offer letter
          const formData = new FormData();
          formData.append("file", this.offerLetterFile);
          formData.append("status", "UNDER_REVIEW");
          formData.append("title", "Offer Letter");
          formData.append("coop_id", resp.data.id.toString());

          this.$axios
            .post("/student-reports", formData, {
              headers: { "Content-Type": "multipart/form-data" }
            })
            .then(resp => {
              console.log(resp);
              console.log("Report (offer letter) created");
              this.submitting = false;

              this.$router.push({ path: "/student/home" });
            })
            .catch(err => console.log(err));
        })
        .catch(err => console.log(err));
    },
    onReset: function() {
      this.courseTerm = "";
      this.courseYear = null;
    }
  }
};
</script>

<style scoped lang="scss">
#container {
  width: 75%;
}

.section {
  max-width: 250px;
}
</style>
