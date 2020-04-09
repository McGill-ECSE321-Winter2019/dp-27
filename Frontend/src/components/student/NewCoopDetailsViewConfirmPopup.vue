<template>
  <q-card>
    <q-card-section>
      <div class="text-h6">Confirmation</div>
    </q-card-section>

    <q-card-section class="q-pt-none">
      Make sure to review the information you have entered, as you will not be
      able to change it later.
    </q-card-section>

    <q-card-actions align="right">
      <q-btn flat label="Take Me Back" color="primary" v-close-popup />
      <q-btn label="Submit" color="primary" @click="notifyParent" />
    </q-card-actions>
  </q-card>
</template>

<script>
export default {
  name: "NewCoopDetailsConfirmPopup",
  methods: {
    notifyParent: function() {
      // let parent know that it can create the CoopDetails
      this.$emit("create-coop-details");
    }
  }
};
</script>

<style lang="scss" scoped></style>
