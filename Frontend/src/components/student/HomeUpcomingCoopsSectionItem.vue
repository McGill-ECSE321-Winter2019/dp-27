<template>
  <q-card bordered square class="q-mb-md">
    <q-card-section>
      <div class="text-h6">{{ term }}</div>
    </q-card-section>

    <q-separator inset />

    <q-card-section>
      <!-- Show a warning message if co-op details are missing -->
      <div v-if="coopDetailsRequired">
        <q-banner
          inline-actions
          rounded
          class="bg-orange-12 text-white q-mb-sm"
        >
          You have not yet added required details for this co-op.
          <template v-slot:avatar>
            <q-icon name="warning" color="white" />
          </template>
          <template v-slot:action>
            <q-btn
              flat
              label="Add Co-op Details"
              @click="showCoopDetailsPopup = true"
            />
          </template>
        </q-banner>
      </div>

      <!-- Co-op status -->
      <div class="text-subtitle1">
        <span class="text-subtitle1 text-weight-medium">
          Status:
        </span>
        <q-badge :color="statusColor">
          {{ this.$common.convertEnumTextToReadableString(this.coop.status) }}
        </q-badge>
      </div>

      <div v-if="!underReview && !coopDetailsRequired">
        <!-- Show co-op details -->
        <div class="text-body2 q-mb-md">
          {{ coop.coopDetails.employerContact.company.name }}
        </div>
      </div>

      <q-dialog v-model="showCoopDetailsPopup">
        <CoopDetailsPopup :coopId="this.coop.id" />
      </q-dialog>
    </q-card-section>
  </q-card>
</template>

<script>
import CoopDetailsPopup from "./CoopDetailsPopup.vue";

export default {
  name: "HomeUpcomingCoopsSectionItem",
  components: {
    CoopDetailsPopup
  },
  props: {
    coop: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      showCoopDetailsPopup: false
    };
  },
  computed: {
    underReview: function() {
      return this.coop.status === "UNDER_REVIEW";
    },
    coopDetailsRequired: function() {
      return (
        this.coop.coopDetails === null && this.coop.status != "UNDER_REVIEW"
      );
    },
    term: function() {
      const season = this.$common.convertEnumTextToReadableString(
        this.coop.courseOffering.season
      );
      return `${season} ${this.coop.courseOffering.year}`;
    },
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

<style lang="scss" scoped></style>
