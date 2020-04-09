<template>
  <q-card flat bordered id="card">
    <q-card-section>
      <div class="text-h6">Upcoming Co-ops</div>
    </q-card-section>

    <q-separator inset />

    <q-card-section>
      <!-- Show spinner while loading -->
      <div v-if="loading" class="center-item">
        <q-spinner color="primary" size="3em" />
      </div>
      <!-- Show actual data -->
      <div v-else>
        <div v-if="coops.length > 0">
          <HomeUpcomingCoopsSectionItem
            v-for="coop in coops"
            :key="coop.id"
            :coop="coop"
            @refresh-upcoming-coops="refreshUpcomingCoops"
          />
        </div>
        <div v-else class="text-subtitle1 center-item">
          No upcoming co-ops to show.
        </div>
      </div>
    </q-card-section>
  </q-card>
</template>

<script>
import HomeUpcomingCoopsSectionItem from "./HomeUpcomingCoopsSectionItem.vue";

export default {
  name: "HomeUpcomingCoopsSection",
  components: {
    HomeUpcomingCoopsSectionItem
  },
  data: function() {
    return {
      loading: true,
      coops: []
    };
  },
  props: {},
  created: function() {
    this.refreshUpcomingCoops();
  },
  methods: {
    refreshUpcomingCoops: function() {
      this.loading = true;
      const studentId = this.$store.state.currentUser.id;
      // get all upcoming coops for the currently logged in student
      this.$axios.get(`/students/${studentId}/upcoming-coops`).then(resp => {
        this.coops = resp.data;
        this.loading = false;
      });
    }
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
