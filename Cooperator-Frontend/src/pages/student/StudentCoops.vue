<template>
  <q-page class="flex justify-center">
    <div id="container">
      <CoopsList />
    </div>
  </q-page>
</template>

<script>
import CoopsList from 'components/student/CoopsList.vue'

export default {
  name: 'StudentCoops',
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
