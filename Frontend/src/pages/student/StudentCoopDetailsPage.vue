<!-- This page allows a Student to either submit their CoopDetails or view the
CoopDetails they have already submitted. -->
<template>
  <BasePage title="Co-op Details">
    <q-card flat bordered class="q-mb-md">
      <!-- Show spinner while submitting -->
      <div v-if="loading" class="center-item q-ma-md">
        <q-spinner color="primary" size="3em" />
      </div>
      <div v-else>
        <!-- Allow Student to add their CoopDetails if it doesn't exist -->
        <CoopDetailsPageNewCoopDetails
          v-if="coop.coopDetails === null"
          :coop="coop"
        />
        <!-- Allow viewing only otherwise -->
        <CoopDetailsPageExistingCoopDetails v-else :coop="coop" />
      </div>
    </q-card>
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";
import CoopDetailsPageNewCoopDetails from "../../components/student/CoopDetailsPageNewCoopDetails.vue";
import CoopDetailsPageExistingCoopDetails from "../../components/student/CoopDetailsPageExistingCoopDetails.vue";

export default {
  name: "StudentCoopDetailsPage",
  components: {
    BasePage,
    CoopDetailsPageNewCoopDetails,
    CoopDetailsPageExistingCoopDetails
  },
  data: function() {
    return {
      coop: null,
      loading: true,
      coopId: this.$route.params.coopId
    };
  },
  created: function() {
    this.getCoop();
  },
  methods: {
    getCoop: function() {
      this.loading = true;
      this.$axios.get(`/coops/${this.coopId}`).then(resp => {
        this.coop = resp.data;
        this.loading = false;
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
