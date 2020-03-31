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
              {{ term }}
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
          <q-btn label="Approve" color="primary" @click="approveCoop" />
          <q-btn label="Reject" color="primary" flat @click="rejectCoop" />
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
  computed: {
    term: function() {
      return `${this.coop.courseOffering.season} ${this.coop.courseOffering.year}`;
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
    approveCoop: function() {
      // set the status of the coop to FUTURE
      const coopBody = {
        id: this.coop.id,
        status: "FUTURE"
      };
      // update the coop
      this.$axios.put("/coops", coopBody);

      // set the status of the offer letter to COMPLETED
      // assuming the offer letter is the only StudentReport, which it should be
      const studentReportBody = {
        id: this.coop.studentReports[0].id,
        status: "COMPLETED"
      };
      // update the student report status
      this.$axios.put("/student-reports", studentReportBody);

      // also send the student a notification that their coop has been approved
      const notifBody = {
        title: "Co-op Approved",
        body: `Your co-op for ${this.term} has been approved!`,
        student: {
          id: this.coop.student.id
        },
        sender: {
          id: this.$store.state.currentUser.id
        }
      };
      // create the notification
      this.$axios
        .post("/notifications", notifBody)
        .then(_resp => {
          this.$q.notify({
            color: "green-4",
            position: "top",
            textColor: "white",
            icon: "cloud_done",
            message: "Co-op Approved Successfully"
          });
        })
        .catch(_err => {
          this.$q.notify({
            color: "red-4",
            position: "top",
            textColor: "white",
            icon: "error",
            message: "Something went wrong, please try again"
          });
        });
    },
    rejectCoop: function() {
      // TODO: implement This
      // the notification body should be customizable, i.e. the admin should
      // enter why they rejected the co-op
    }
  }
};
</script>

<style lang="scss" scoped></style>
