<template>
  <BasePage title="Submit Evaluations">
    <q-card flat bordered class="q-mb-md">
      <q-card-section>
        <div class="text-h6 q-mb-sm">Select Student</div>
        <div v-if="coopsLoaded">
          <q-select
            emit-value
            map-options
            outlined
            v-model="chosenStudent"
            option-value="id"
            option-label="name"
            :options="students"
            label="Student"
            :rules="[val => val != null || 'Please upload a file']"
          >
          </q-select>
          <div class="text-h6 q-mb-sm">Upload Evaluation (PDF)</div>
          <q-file
            outlined
            v-model="evaluationFile"
            label="Upload Evaluation"
            class="q-mb-md"
            lazy-rules
            accept=".pdf"
            :rules="[val => val != null || 'Please upload a file']"
            ><template v-slot:prepend>
              <q-icon name="attach_file" />
            </template>
          </q-file>
          <!-- Disable buttons while submitting -->
          <q-btn
            label="Submit"
            type="submit"
            color="primary"
            :disabled="submitting"
            @click="onSubmit"
          />
          <!-- Show spinner while submitting -->
          <q-spinner v-if="submitting" color="primary" size="2.5em" />
        </div>
      </q-card-section>
    </q-card>
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";

export default {
  name: "EmployerUploadPage",
  components: {
    BasePage
  },
  data() {
    return {
      coops: [],
      students: [],
      coopsAndStudents: [],
      evaluationFile: null,
      employerContact: null,
      coopsLoaded: false,
      chosenStudent: null,
      submitting: false
    };
  },
  created: function() {
    var url = $route.params.url;
    this.$axios.get("/employer-contacts/url/" + url).then(resp => {
      this.employerContact = resp.data;
      this.$axios
        .get("/coops/employer-contact/" + this.employerContact.id)
        .then(resp => {
          this.coops = resp.data;
          this.coopsLoaded = true;
          for (var i = 0; i < this.coops.length; i++) {
            var stuId = this.coops[i].student.id;
            var name = this.coops[i].student.firstName.concat(
              " ",
              this.coops[i].student.lastName
            );
            this.students.push({ name: name, id: stuId });
            this.coopsAndStudents.push({
              coopId: this.coops[i].id,
              stuId: stuId
            });
          }
        });
    });
  },
  methods: {
    onSubmit: function() {
      console.log(this.coopsAndStudents);

      let coopStuObj = this.coopsAndStudents.find(
        o => o.stuId === this.chosenStudent
      );

      const formData = new FormData();
      formData.append("file", this.evaluationFile);
      formData.append("status", "UNDER_REVIEW");
      formData.append("title", "Employer Evaluation");
      formData.append("coopId", coopStuObj.coopId);
      formData.append("authorId", this.employerContact.id);

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
            message: "Report Submitted Successfully"
          });
          this.evaluationFile = null;
          this.chosenStudent = null;
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
    }
  }
};
</script>

<style lang="scss">
#container {
}
</style>
