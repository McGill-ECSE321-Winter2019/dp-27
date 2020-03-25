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

    let blob = this.b64toBlob(bytes, "application/pdf");
    this.offerLetterURL = window.URL.createObjectURL(blob);
  },
  methods: {
    openPDF: function() {
      window.open(this.offerLetterURL);
    },
    // found on SO: https://stackoverflow.com/a/57767153
    b64toBlob: function(content, contentType) {
      contentType = contentType || "";
      const sliceSize = 512;
      // method which converts base64 to binary
      const byteCharacters = window.atob(content);

      const byteArrays = [];
      for (
        let offset = 0;
        offset < byteCharacters.length;
        offset += sliceSize
      ) {
        const slice = byteCharacters.slice(offset, offset + sliceSize);
        const byteNumbers = new Array(slice.length);
        for (let i = 0; i < slice.length; i++) {
          byteNumbers[i] = slice.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
      }

      // statement which creates the blob
      return new Blob(byteArrays, {
        type: contentType
      });
    }
  }
};
</script>

<style lang="scss" scoped></style>
