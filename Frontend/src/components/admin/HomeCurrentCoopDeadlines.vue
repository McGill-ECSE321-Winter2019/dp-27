<template>
  <q-card
    flat
    bordered
    id="card"
  >
    <q-card-section>
      <div class="text-h6">Current Co-op Deadlines</div>
    </q-card-section>

    <q-separator inset />

    <q-card-section>
      <div class="space text">
        <b>Offer Letter: </b> May 1st, 2020
        <span style="float: right;">
          <q-btn
            color="primary"
            label="Send Reminder"
            :size="'sm'"
          />
        </span>
      </div>
      <div class="space text">
        <b>Employer Evaluation: </b> August 1st, 2020
        <span style="float: right;">
          <q-btn
            color="primary"
            label="Send Reminder"
            :size="'sm'"
          />
        </span>
      </div>
       <div class="space text">
        <b>Final Report: </b> September 1st, 2020
        <span style="float: right;">
          <q-btn
            color="primary"
            label="Send Reminder"
            :size="'sm'"
          />
        </span>
      </div>
      <div class="row justify-center">
        <q-btn
          flat
          color="primary"
          label="Edit Dates"
          :size="'sm'"
        />
      </div>

    </q-card-section>
  </q-card>
</template>

<script>
export default {
  name: 'HomeCurrentCoopInfoWidget'
}
</script>

<style scoped lang="scss">
h6 {
  margin: 10px;
}

.space {
  margin-top: 15px;
  margin-bottom: 15px;
  padding: 1%;
}

#card {
  width: 100%;
  min-width: 225px;
  margin-top: 25px;
  margin-left: 10px;
}
</style>
