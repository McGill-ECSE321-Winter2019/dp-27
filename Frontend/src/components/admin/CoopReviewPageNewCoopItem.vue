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
            <div class="text-subtitle1">
              <span class="text-subtitle1 text-weight-medium">
                Course:
              </span>
              {{ coop.courseOffering.course.name }}
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
        </div>

        <!-- Disable buttons while submitting -->
        <q-btn
          label="Approve"
          color="primary"
          @click="confirm = true"
          :disabled="submitting"
        />
        <q-btn
          label="Reject"
          color="primary"
          flat
          @click="rejectCoop(coop.student)"
          :disabled="submitting"
        />
        <!-- Show spinner while submitting -->
        <q-spinner v-if="submitting" color="primary" size="2.5em" />
      </div>
      <q-dialog v-model="showPopup">
        <NotificationPopup
          :title="title"
          :body="message"
          :students="s"
          @sent="confirmReject"
        />
      </q-dialog>
      <!-- Popup for an Admin to confirm that they want to approve the Coop -->
      <q-dialog v-model="confirm">
        <q-card>
          <q-card-section>
            <div class="text-h6 primary">
              Are you sure you would like to confirm this Coop?
            </div>
          </q-card-section>

          <q-card-actions align="right" class="text-primary">
            <q-btn flat label="Cancel" v-close-popup />
            <q-btn color="primary" label="Confirm Co-op" @click="approveCoop" />
          </q-card-actions>
        </q-card>
      </q-dialog>
    </q-card-section>
  </q-card>
</template>

<script>
import NotificationPopup from "./NotificationPopup.vue";

export default {
  name: "CoopReviewPageNewCoopItem",
  components: {
    NotificationPopup
  },
  data: function() {
    return {
      offerLetterURL: null,
      submitting: false,
      showPopup: false,
      confirm: false,
      s: [],
      message: "",
      title: ""
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
      const term = `${this.coop.courseOffering.season} ${this.coop.courseOffering.year}`;
      return this.$common.convertEnumTextToReadableString(term);
    }
  },
  created: function() {
    var bytes = this.coop.reports[0].data;

    let blob = this.$common.b64toBlob(bytes, "application/pdf");
    this.offerLetterURL = window.URL.createObjectURL(blob);
  },
  methods: {
    openPDF: function() {
      window.open(this.offerLetterURL);
    },
    approveCoop: function() {
      this.submitting = true;
      this.confirm = false;
      // set the status of the coop to FUTURE
      const coopBody = {
        status: "FUTURE"
      };
      // update the coop
      this.$axios.put(`/coops/${this.coop.id}`, coopBody).then(() => {
        this.$emit("refresh-new-coops");

        this.$q.notify({
          color: "green-4",
          position: "top",
          textColor: "white",
          icon: "cloud_done",
          message: "Co-op Approved Successfully"
        });
        this.submitting = false;
      });

      // set the status of the offer letter to COMPLETED
      // assuming the offer letter is the only Report, which it should be
      const reportBody = {
        status: "COMPLETED"
      };
      // update the student report status
      this.$axios.put(`/reports/${this.coop.reports[0].id}`, reportBody);

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
      this.$axios.post("/notifications", notifBody).catch(_err => {
        this.$q.notify({
          color: "red-4",
          position: "top",
          textColor: "white",
          icon: "error",
          message: "Something went wrong, please try again"
        });
      });
    },

    rejectCoop: function(student) {
      this.title = "Your Co-op request has been rejected";
      this.message =
        "After reviewing the contents of the Co-op offer letter, it has been deemed unsatisfactory as relevant experience.";
      this.showPopup = true;
      this.s = [student];
    },

    confirmReject: function() {
      const coopBody = {
        status: "REJECTED"
      };

      //update the coop
      this.$axios
        .put("/coops/" + this.coop.id, coopBody)
        .then(_resp => {
          this.showPopup = false;
          this.$emit("refresh-new-coops");
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
    }
  }
};
</script>

<style lang="scss" scoped></style>
