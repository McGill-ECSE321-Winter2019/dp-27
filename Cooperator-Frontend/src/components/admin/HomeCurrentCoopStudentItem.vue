<template>
  <div
    id="container"
    class="row"
  >
    <div class="col">
      <div><strong>{{ srt }}</strong></div>
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
  name: 'HomeCurrentCoopStudentItem',
  props: {
    studentName: String
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