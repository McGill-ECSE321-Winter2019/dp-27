<template>
  <q-card>
    <q-card-section>
      <div class="text-h6">Confirm Delete</div>
    </q-card-section>

    <q-card-section class="q-pt-none">
      Are you sure you want to remove this report configuration? This will
      affect all students currently doing a co-op term and all students with
      upcoming co-op terms.
    </q-card-section>

    <q-card-actions align="right">
      <q-btn flat label="Cancel" color="primary" v-close-popup />
      <q-btn
        flat
        label="Yes, delete"
        color="primary"
        @click="deleteReportConfig"
      />
    </q-card-actions>
  </q-card>
</template>

<script>
export default {
  name: "ReportConfigurationDeletePopup",
  props: {
    id: {
      type: Number,
      required: true
    }
  },
  methods: {
    deleteReportConfig: function() {
      this.$axios
        .delete("/report-configs/" + this.id)
        .then(_resp => {
          this.$q.notify({
            color: "green-4",
            position: "top",
            textColor: "white",
            icon: "cloud_done",
            message: "Deleted Successfully"
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

<style lang="scss" scoped></style>
