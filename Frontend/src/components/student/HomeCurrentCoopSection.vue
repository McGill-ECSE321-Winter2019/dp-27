<!-- This component represents a Student's current Coop on their homepage. -->
<template>
  <q-card flat bordered class="q-mt-md">
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
        <div v-if="coop">
          <div class="text-subtitle1 q-mb-sm">
            <span class="text-subtitle1 text-weight-medium">
              Status:
            </span>
            <q-badge :color="statusColor">
              {{
                this.$common.convertEnumTextToReadableString(this.coop.status)
              }}
            </q-badge>
          </div>

          <CoopDetailsListView :coop="coop" />

          <q-btn color="primary" label="Edit Co-op Details" flat />
          <q-btn color="primary" label="View Reports" flat />
        </div>
        <div v-else class="text-subtitle1 center-item">
          No current co-op to show.
        </div>
      </div>
    </q-card-section>
  </q-card>
</template>

<script>
import CoopDetailsListView from "./CoopDetailsListView.vue";

export default {
  name: "HomeCurrentCoopSection",
  components: {
    CoopDetailsListView
  },
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
  },
  computed: {
    statusColor: function() {
      if (this.coop.status === "UNDER_REVIEW") return "orange";
      if (this.coop.status === "IN_PROGRESS") return "blue";
      if (this.coop.status === "FUTURE") return "light-blue";
      if (this.coop.status === "INCOMPLETE") return "primary";
      else return "black";
    }
  }
};
</script>

<style lang="scss" scoped>
.center-item {
  text-align: center;
}
</style>
