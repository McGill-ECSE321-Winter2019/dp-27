<template>
  <q-card id="card">
    <q-card-section class="row items-center">
      <div class="text-h6">{{ popupTitle }}</div>
      <q-space />
      <q-btn icon="close" flat round dense v-close-popup />
    </q-card-section>

    <q-card-section>
      <q-form @submit="onSubmit" class="q-gutter-sm">
        <q-btn color="primary" type="submit" :label="buttonLabel" />
      </q-form>
    </q-card-section>
  </q-card>
</template>

<script>
export default {
  name: "CoopDetailsPopup",
  props: {
    coopId: {
      type: Number,
      required: true
    }
  },
  data: function() {
    return {
      popupTitle: "Add Co-op Details",
      buttonLabel: "Add"
    };
  },
  methods: {
    onSubmit: function() {}
  }
};
</script>

<style lang="scss" scoped>
#card {
  min-width: 400px;
}
</style>
