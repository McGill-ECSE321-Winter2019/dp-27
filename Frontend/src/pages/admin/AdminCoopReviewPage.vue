<template>
  <BasePage title="Co-op Review">
    <q-card flat bordered class="q-mb-md">
      <q-tabs
        v-model="currentTabData"
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
      <q-tab-panels v-model="currentTabData" animated>
        <q-tab-panel name="new_coops">
          <div v-if="newCoops.length > 0">
            <div class="text-subtitle1 q-mb-md">
              <span class="text-weight-medium"
                >Number of new co-ops to review:
              </span>
              <q-badge align="middle" color="black">
                {{ newCoops.length }}
              </q-badge>
            </div>
            <div class="text-caption q-mb-lg">
              Note that students will enter their co-op details (e.g. company
              name, salary) after the co-op is approved.
            </div>
            <CoopReviewPageNewCoopItem
              v-for="coop in newCoops"
              :key="coop.id"
              :coop="coop"
              @refresh-new-coops="getNewCoops"
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
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";
import CoopReviewPageNewCoopItem from "../../components/admin/CoopReviewPageNewCoopItem.vue";

export default {
  name: "AdminCoopReviewPage",
  props: {
    currentTab: String
  },
  components: {
    BasePage,
    CoopReviewPageNewCoopItem
  },
  data: function() {
    return {
      newCoopsLoading: true,
      completedCoopsLoading: true,
      currentTabData: "new_coops",
      newCoops: [],
      completedCoops: []
    };
  },
  created: function() {
    if (this.currentTab) this.currentTabData = this.currentTab;

    this.getNewCoops();
    this.completedCoopsLoading = false;
  },
  computed: {
    loading: function() {
      return this.newCoopsLoading || this.completedCoopsLoading;
    }
  },
  methods: {
    getNewCoops: function() {
      this.newCoopsLoading = true;
      this.$axios
        .get("/coops", { params: { status: "UNDER_REVIEW" } })
        .then(resp => {
          // sort in order of soonest terms first
          this.newCoops = resp.data.sort(this.$common.compareCoopTerms);
          this.newCoopsLoading = false;
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.center-item {
  text-align: center;
}
</style>
