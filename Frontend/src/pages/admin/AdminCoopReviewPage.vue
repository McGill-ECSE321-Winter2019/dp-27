<template>
  <q-page class="flex justify-center">
    <div id="container" class="row">
      <div class="col">
        <h4>Co-op Review</h4>

        <q-card flat bordered class="q-mb-md">
          <q-tabs
            v-model="currentTab"
            dense
            class="text-grey"
            active-color="primary"
            indicator-color="primary"
            align="justify"
            narrow-indicator
          >
            <q-tab name="new_coops" label="New Co-ops"></q-tab>
            <q-tab name="completed_coops" label="Completed Co-ops"></q-tab>
          </q-tabs>
        </q-card>

        <!-- Show spinner while loading -->
        <div v-if="loading" class="center-item">
          <q-spinner color="primary" size="3em" />
        </div>
        <!-- Show actual data -->
        <div v-else>
          <q-tab-panels v-model="currentTab" animated>
            <q-tab-panel name="new_coops">
              <div v-if="newCoops.length > 0">
                <div class="text-subtitle1 q-mb-lg">
                  <span class="text-weight-medium"
                    >Number of new co-ops to review:
                  </span>
                  <q-badge align="middle" color="black">
                    {{ newCoops.length }}
                  </q-badge>
                </div>
                <CoopReviewPageNewCoopItem
                  v-for="coop in newCoops"
                  :key="coop.id"
                  :coop="coop"
                />
              </div>
              <div v-else class="center-item">
                No new co-ops to review at this time.
              </div>
            </q-tab-panel>
            <q-tab-panel name="completed_coops">
              <div v-if="completedCoops.length > 0">
                <!-- TODO: implement this -->
              </div>
              <div v-else class="center-item">
                No completed co-ops to review at this time.
              </div>
            </q-tab-panel>
          </q-tab-panels>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import CoopReviewPageNewCoopItem from "../../components/admin/CoopReviewPageNewCoopItem.vue";

export default {
  name: "AdminCoopReviewPage",
  components: {
    CoopReviewPageNewCoopItem
  },
  data: function() {
    return {
      newCoopsLoading: true,
      completedCoopsLoading: true,
      currentTab: "new_coops",
      newCoops: [],
      completedCoops: []
    };
  },
  created: function() {
    this.$axios
      .get("/coops", { params: { status: "UNDER_REVIEW" } })
      .then(resp => {
        this.newCoops = resp.data;
        this.newCoopsLoading = false;
      });
    this.completedCoopsLoading = false;
  },
  computed: {
    loading: function() {
      return this.newCoopsLoading || this.completedCoopsLoading;
    }
  }
};
</script>

<style lang="scss" scoped>
#container {
  width: 75%;
}

.center-item {
  text-align: center;
}
</style>
