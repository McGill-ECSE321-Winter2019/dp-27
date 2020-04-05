<!-- Represents a summary of an upcoming Coop on a Student's homepage.

Parent: HomeUpcomingCoopsSection.vue -->
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
              @click="goToCoopDetailsPage"
            />
          </template>
        </q-banner>
      </div>

      <!-- Co-op status -->
      <div class="text-subtitle1">
        <div class="row">
          <div class="col-6">
            <span class="text-subtitle1 text-weight-medium">
              Status:
            </span>
            <q-badge :color="statusColor">
              {{
                this.$common.convertEnumTextToReadableString(this.coop.status)
              }}
            </q-badge>
            <!-- Allow editing of offer letter if Coop is still under review -->
            <div v-if="underReview">
              <q-btn
                color="primary"
                class="q-mt-md"
                flat
                label="Edit Offer Letter"
                @click="showEditOfferLetterPopup = true"
              />
            </div>
          </div>
          <div v-if="underReview" class="col-6 text-body2">
            <q-btn
              color="primary"
              class="float-right"
              flat
              icon-right="open_in_new"
              label="View Offer Letter"
              @click="openPDF"
            />
          </div>
        </div>
      </div>

      <div v-if="!underReview && !coopDetailsRequired">
        <!-- Show co-op details -->
        <div class="text-body2 q-mb-md">
          {{ coop.coopDetails.employerContact.company.name }}
        </div>
      </div>

      <q-dialog v-model="showEditOfferLetterPopup">
        <EditOfferLetterPopup
          :reportId="this.coop.studentReports[0].id"
          @refresh-upcoming-coops="notifyParentToRefreshUpcomingCoops"
        />
      </q-dialog>
    </q-card-section>
  </q-card>
</template>

<script>
import EditOfferLetterPopup from "./EditOfferLetterPopup.vue";

export default {
  name: "HomeUpcomingCoopsSectionItem",
  components: {
    EditOfferLetterPopup
  },
  props: {
    coop: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      offerLetterURL: null,
      showCoopDetailsPopup: false,
      showEditOfferLetterPopup: false,
      statusColor: this.$common.getCoopStatusColor(this.coop.status)
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
    }
  },
  created: function() {
    var bytes = this.coop.studentReports[0].data;

    let blob = this.$common.b64toBlob(bytes, "application/pdf");
    this.offerLetterURL = window.URL.createObjectURL(blob);
  },
  methods: {
    openPDF: function() {
      window.open(this.offerLetterURL);
    },
    notifyParentToRefreshUpcomingCoops: function() {
      this.showEditOfferLetterPopup = false;
      this.$emit("refresh-upcoming-coops");
    },
    goToCoopDetailsPage: function() {
      // go to the Coop Details page for this specific Coop
      this.$router.push({
        path: `/student/coop-details/${this.coop.id}`
      });
    }
  }
};
</script>

<style lang="scss" scoped></style>
