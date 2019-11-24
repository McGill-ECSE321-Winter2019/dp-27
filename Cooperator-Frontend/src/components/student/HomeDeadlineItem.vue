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
      <div>Due on {{ deadline }}</div>
    </div>
    <div class="col">
      <span style="float: right;">
        <q-btn
          flat
          color="primary"
          label="Upload"
          :size="'md'"
        />
      </span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomeDeadlineItem',
  props: {
    itemName: String,
    deadline: String
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
