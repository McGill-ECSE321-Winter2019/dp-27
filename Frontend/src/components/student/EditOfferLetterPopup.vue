<!-- This popup allows a Student to update their offer letter for a Coop
that is under review.

Parent: HomeUpcomingCoopsSectionItem.vue -->
<template>
  <q-card id="card">
    <q-card-section class="row items-center">
      <div class="text-h6">Edit Offer Letter</div>
      <q-space />
      <q-btn icon="close" flat round dense v-close-popup />
    </q-card-section>

    <q-card-section>
      <q-form @submit="updateOfferLetter" class="q-gutter-md">
        <div class="text-caption">
          Attach a new PDF here to overwrite your previous offer letter
          submission for this co-op.
        </div>
        <q-file
          outlined
          v-model="offerLetterFile"
          label="Attach offer letter"
          class="file-input"
          lazy-rules
          accept=".pdf"
          :rules="[val => val != null || 'Please upload a file']"
        >
          <template v-slot:prepend>
            <q-icon name="attach_file" />
          </template>
        </q-file>

        <!-- Disable button while submitting -->
        <q-btn
          color="primary"
          type="submit"
          label="Update"
          :disabled="submitting"
        />
        <!-- Show spinner while submitting -->
        <q-spinner v-if="submitting" color="primary" size="2.5em" />
      </q-form>
    </q-card-section>
  </q-card>
</template>

<script>
export default {
  name: "EditOfferLetterPopup",
  props: {
    reportId: {
      type: Number,
      required: true
    }
  },
  data: function() {
    return {
      offerLetterFile: null,
      submitting: false
    };
  },
  methods: {
    updateOfferLetter: function() {
      const formData = new FormData();
      formData.append("file", this.offerLetterFile);

      this.submitting = true;

      this.$axios
        .put(`/student-reports/${this.reportId}`, formData, {
          headers: { "Content-Type": "multipart/form-data" }
        })
        .then(_resp => {
          this.$q.notify({
            color: "green-4",
            position: "top",
            textColor: "white",
            icon: "cloud_done",
            message: "Offer Letter Updated Successfully"
          });
          this.submitting = false;

          this.$emit("refresh-upcoming-coops");
        })
        .catch(_err => {
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

<style>
.file-input {
  max-width: 250px;
}
</style>
