<!-- This component is a popup for both creating and editing a report section
config. It is rendered slightly differently depending on whether it is in
editing mode or not.

Parent: ReportSectionConfigurationItem.vue -->
<template>
  <q-card id="card">
    <q-card-section class="row items-center">
      <div class="text-h6">{{ popupTitle }}</div>
      <q-space />
      <q-btn icon="close" flat round dense v-close-popup />
    </q-card-section>

    <q-card-section>
      <q-form @submit="onSubmit">
        <q-input
          outlined
          v-model="promptData"
          type="textarea"
          label="Section Prompt (e.g. How was your co-op?)"
          lazy-rules
          :rules="[val => (val && val.length > 0) || 'Please enter a prompt']"
        />

        <q-select
          outlined
          v-model="responseTypeData"
          label="Response Type"
          :options="options"
        />

        <q-btn
          class="q-mt-md"
          color="primary"
          type="submit"
          :label="buttonLabel"
          :disabled="submitting"
        />
        <q-btn
          v-if="isEditing"
          class="q-mt-md q-ml-sm"
          color="primary"
          label="Delete"
          flat
          :disabled="submitting"
          @click="deleteReportSectionConfig"
        />
        <span v-if="submitting" class="q-ml-sm">
          <q-spinner color="primary" size="2.5em"></q-spinner>
        </span>
      </q-form>
    </q-card-section>
  </q-card>
</template>

<script>
export default {
  data: function() {
    return {
      popupTitle: "Add New Report Section Configuration",
      promptData: "",
      responseTypeData: "",
      buttonLabel: "Create",
      options: [],
      submitting: false
    };
  },
  props: {
    reportSectionConfig: Object,
    reportConfigId: Number,
    numberOfQuestions: Number,
    isEditing: Boolean
  },
  created: function() {
    this.$axios.get("/report-section-configs/response-types").then(resp => {
      this.options = resp.data;
    });

    if (this.isEditing) {
      this.popupTitle = "Edit Report Section Configuration";
      this.promptData = this.reportSectionConfig.sectionPrompt;
      this.responseTypeData = this.reportSectionConfig.responseType;
      this.buttonLabel = "Update";
    }
  },
  methods: {
    onSubmit: function() {
      if (this.isEditing) {
        this.updateReportSectionConfig();
      } else {
        this.createReportSectionConfig();
      }
    },
    createReportSectionConfig: function() {
      const body = {
        sectionPrompt: this.promptData,
        responseType: this.responseTypeData,
        questionNumber: this.numberOfQuestions + 1,
        reportConfig: {
          id: this.reportConfigId
        }
      };
      this.submitting = true;

      this.$axios
        .post("/report-section-configs", body)
        .then(_resp => {
          this.$q.notify({
            color: "green-4",
            position: "top",
            textColor: "white",
            icon: "cloud_done",
            message: "Created Successfully"
          });
          this.submitting = false;
          // let parent know to close the dialog and refresh its list of report configs
          this.$emit("refresh");
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
    },
    updateReportSectionConfig: function() {
      const body = {
        id: this.reportSectionConfig.id,
        sectionPrompt: this.promptData,
        responseType: this.responseTypeData
      };
      this.submitting = true;

      this.$axios
        .put(`/report-section-configs/${this.reportSectionConfig.id}`, body)
        .then(_resp => {
          this.$q.notify({
            color: "green-4",
            position: "top",
            textColor: "white",
            icon: "cloud_done",
            message: "Updated Successfully"
          });
          this.submitting = false;
          // let parent know to close the dialog and refresh its list
          // of report configs
          this.$emit("refresh");
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
    },
    deleteReportSectionConfig: function() {
      this.submitting = true;
      this.$axios
        .delete("/report-section-configs/" + this.reportSectionConfig.id)
        .then(_resp => {
          this.$q.notify({
            color: "green-4",
            position: "top",
            textColor: "white",
            icon: "cloud_done",
            message: "Deleted Successfully"
          });
          this.submitting = false;
          // let parent know to close the dialog and refresh its list of report configs
          this.$emit("refresh");
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

<style lang="scss" scoped></style>
