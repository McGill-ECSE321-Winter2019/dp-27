<!-- This component represents an individual report section config within a
report configuration.

Parent: ReportConfigurationItem.vue -->
<template>
  <div class="row">
    <div class="col-7 text-body2">{{ prompt }}</div>
    <div class="col-4 text-body2">{{ responseType }}</div>
    <div class="col-1">
      <q-btn label="Edit" color="primary" flat />
    </div>
  </div>
</template>

<script>
export default {
  name: "ReportSectionConfigurationItem",
  props: {
    prompt: {
      type: String,
      required: true
    },
    responseType: {
      type: String,
      required: true
    }
  }
};
</script>

<style lang="scss" scoped></style>
