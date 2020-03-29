<template>
  <q-card flat bordered id="card">
    <q-card-section>
      <div class="text-h6">Current Co-op</div>
    </q-card-section>

    <q-separator inset />

    <q-card-section>
      <!-- Show spinner while loading -->
      <div v-if="loading" class="center-item">
        <q-spinner color="primary" size="3em" />
      </div>
      <!-- Show actual data -->
      <div v-else>
        <div v-if="coop"></div>
        <div v-else class="text-subtitle1 center-item">
          No current co-op to show.
        </div>
      </div>
    </q-card-section>
  </q-card>
</template>

<script>
export default {
  name: "HomeCurrentCoopSection",
  data: function() {
    return {
      loading: true,
      coop: null
    };
  },
  created: function() {
    const id = this.$store.state.currentUser.id;
    this.$axios.get(`/students/${id}/current-coop`).then(resp => {
      this.coop = resp.data;
      this.loading = false;
    });
  }
};
</script>

<style lang="scss" scoped>
#card {
  width: 100%;
  margin-top: 25px;
  margin-right: 10px;
}

.center-item {
  text-align: center;
}
</style>
