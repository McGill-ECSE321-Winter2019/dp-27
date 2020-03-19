<template>
  <q-card id="card">
    <q-card-section class="row items-center">
      <div class="text-h6">Add New Report Configuration</div>
      <q-space />
      <q-btn icon="close" flat round dense v-close-popup />
    </q-card-section>

    <q-card-section>
      <q-form @submit="createReportConfig" class="q-gutter-sm">
        <q-input
          filled
          v-model="type"
          label="Report Type (e.g. Final Evaluation)"
          lazy-rules
          :rules="[
            val => (val && val.length > 0) || 'Please enter a report type'
          ]"
        />

        <q-checkbox
          left-label
          v-model="requiresFile"
          label="Requires file (PDF)?"
        />

        <div class="text-subtitle2">Deadline:</div>
        <div class="row items-start">
          <q-input
            filled
            v-model.number="deadline"
            type="number"
            label="Number of Days"
            lazy-rules
            class="col-4"
            :rules="[val => val >= 0 || 'Number of days cannot be negative']"
          />

          <span class="text-body2 col-2 center-item q-mt-md">from</span>

          <q-select
            filled
            v-model="isDeadlineFromStart"
            :options="options"
            class="col-6"
          />
        </div>

        <div class="text-caption q-mb-md">
          Report sections can be configured after the initial report
          configuration is created.
        </div>

        <q-btn color="primary" type="submit" label="Create" />
      </q-form>
    </q-card-section> </q-card
></template>

<script>
export default {
  name: "ReportConfigurationPopup",
  data: function() {
    return {
      type: "",
      requiresFile: false,
      deadline: null,
      isDeadlineFromStart: "",
      options: ["Start of co-op", "End of co-op"]
    };
  },
  methods: {
    createReportConfig: function() {
      const body = {
        requiresFile: this.requiresFile,
        deadline: this.deadline,
        isDeadlineFromStart:
          this.isDeadlineFromStart === this.options[0] ? true : false,
        type: this.type
      };

      this.$axios
        .post("/report-configs", body)
        .then(_resp => {
          this.$q.notify({
            color: "green-4",
            position: "top",
            textColor: "white",
            icon: "cloud_done",
            message: "Created Successfully"
          });
          // let parent know to close the dialog and refresh its list of report configs
          this.$emit("refresh");
        })
        .catch(_err => {
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

<style lang="scss" scoped>
#card {
  width: 100%;
  min-width: 250px;
  margin-top: 25px;
  margin-bottom: 25px;
  margin-right: 10px;
}

.center-item {
  text-align: center;
}
</style>
