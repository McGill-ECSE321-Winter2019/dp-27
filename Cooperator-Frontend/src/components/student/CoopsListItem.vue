<template>
  <div>
    <p><strong>Info:</strong></p>

    <div class="row items-center">
      <div class="col-2">
        <div>Company:</div>
        <div>Location:</div>
        <div>Start Date:</div>
        <div>End Date:</div>
      </div>
      <div class="col">
        <div>Microsoft</div>
        <div>Redmond, WA</div>
        <div>May 11, 2020</div>
        <div>June 28, 2020</div>
      </div>
    </div>

    <div class="row section items-center">
      <div class="col-1"><strong>Progress:</strong></div>
      <div class="col">
        <q-linear-progress
          rounded
          style="height: 25px"
          :value="0.40"
          color="yellow-8"
          class="q-mt-sm"
        >
          <div class="absolute-full flex flex-center">
            <q-badge
              color="white"
              text-color="yellow-8"
              :label="'0.40%'"
            />
          </div>
        </q-linear-progress>
      </div>
    </div>

    <div class="row section items-center">
      <div class="col-1"><strong>Reports:</strong></div>
      <CoopsListReportItem
        itemName="Offer Letter"
        status="Approved"
      />
      <CoopsListReportItem
        itemName="Employer Evaluation"
        status="In Review"
      />
      <CoopsListReportItem
        itemName="Other Report"
        status="Not Submitted"
      />
    </div>
  </div>
</template>

<script>
import CoopsListReportItem from 'components/student/CoopsListReportItem.vue'

export default {
  name: 'CoopsListItem',
  components: {
    CoopsListReportItem
  }
}
</script>

<style lang="scss" scoped>
.section {
  margin-top: 15px;
}
</style>
