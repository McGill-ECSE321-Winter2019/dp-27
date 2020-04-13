<!-- This component represents an individual report section config within a
report configuration.

Parent: ReportConfigurationItem.vue -->
<template>
  <div class="row">
    <div class="col-7 text-body2">{{ reportSectionConfig.sectionPrompt }}</div>
    <div class="col-4 text-body2">{{ reportSectionConfig.responseType }}</div>
    <div class="col-1">
      <q-btn
        label="Edit"
        color="primary"
        flat
        class="float-right"
        @click="showPopup = true"
      />
    </div>

    <q-dialog v-model="showPopup">
      <ReportSectionConfigurationPopup
        :isEditing="true"
        :reportSectionConfig="this.reportSectionConfig"
        @refresh="notifyParent"
      />
    </q-dialog>
  </div>
</template>

<script>
import ReportSectionConfigurationPopup from "./ReportSectionConfigurationPopup.vue";

export default {
  name: "ReportSectionConfigurationItem",
  components: {
    ReportSectionConfigurationPopup
  },
  data: function() {
    return {
      showPopup: false
    };
  },
  props: {
    reportSectionConfig: {
      type: Object,
      required: true
    }
  },
  methods: {
    notifyParent: function() {
      this.$emit("refresh");
    }
  }
};
</script>

<style lang="scss" scoped></style>
