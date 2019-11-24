<template>
  <q-page class="flex justify-center">
    <h4>Emma Eagles</h4>
    <div id="container">
      <CoopsList />
    </div>
  </q-page>
</template>

<script>
import CoopsList from 'components/student/CoopsList.vue'

export default {
  name: 'AdminStudentCoops',
  components: {
    CoopsList
  }
}
</script>

<style lang="scss" scoped>
#container {
  width: 85%;
}
</style>
