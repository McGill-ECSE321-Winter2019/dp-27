<template>
  <q-card
    flat
    bordered
    id="card"
  >
    <q-card-section>
      <div class="text-h6">Current Co-op Info</div>
    </q-card-section>

    <q-separator inset />

    <q-card-section>
      <div class="space text">
        Status:
        <span style="float: right;">
          <q-badge color="yellow-8">
            In Progress
          </q-badge>
        </span>
      </div>
      <div class="space text">
        Employer:
        <span style="float: right;">
          Microsoft
        </span>
      </div>
      <div class="space text">
        Start Date:
        <span style="float: right;">
          May 11, 2020
        </span>
      </div>
      <div class="space text">
        End Date:
        <span style="float: right;">
          July 28, 2020
        </span>
      </div>
      <div class="row justify-center">
        <q-btn
          flat
          color="primary"
          label="More Info"
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
  margin-top: 5px;
  margin-bottom: 5px;
}

#card {
  width: 100%;
  min-width: 225px;
  margin-top: 25px;
  margin-left: 10px;
}
</style>
