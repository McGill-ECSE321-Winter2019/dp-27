<template>
  <q-card class="q-mb-md">
    <q-card-section>
      <div class="q-gutter-md">
        <div class="text-h6">
          {{ `${coop.student.firstName} ${coop.student.lastName}` }}
        </div>

        <div class="row">
          <div class="col-6">
            <div class="text-subtitle1">
              <span class="text-subtitle1 text-weight-medium">
                Term:
              </span>
              {{ `${coop.courseOffering.season} ${coop.courseOffering.year}` }}
            </div>
          </div>
          <div class="col-6 text-body2">
            <q-btn
              color="primary"
              class="float-right"
              flat
              icon-right="open_in_new"
              label="View Offer Letter"
              @click="openPDF"
            />
          </div>
          <q-btn label="Approve" color="primary" />
          <q-btn label="Reject" color="primary" flat />
        </div>
      </div>
    </q-card-section>
  </q-card>
</template>

<script>
export default {
  name: "CoopReviewPageNewCoopItem",
  data: function() {
    return {
      offerLetterURL: null
    };
  },
  props: {
    coop: {
      type: Object,
      required: true
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
    }
  }
};
</script>

<style lang="scss" scoped></style>
