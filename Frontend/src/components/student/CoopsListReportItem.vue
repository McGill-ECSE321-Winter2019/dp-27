<template>
  <div
    id="container"
    class="row"
  >
    <div
      class="col-1"
      style="min-width: 45px"
    >
      <center>
        <q-icon
          color="primary"
          name="assignment"
          :size="'md'"
        />
      </center>
    </div>

    <div class="col">
      <div><strong>{{ itemName }}</strong></div>
    </div>

    <div class="col">
      <q-badge color="secondary">
        {{ status }}
      </q-badge>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CoopsListReportItem',
  props: {
    itemName: String,
    status: String
  }
}
</script>

<style scoped lang="scss">
h6 {
  margin: 10px;
}

#container {
  width: 100%;
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>
