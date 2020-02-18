<template>
  <q-page class="flex justify-center">
    <div
      id="container"
      class="row"
    >
      <div class="col-7">
        <HomeMainInfo />
      </div>
      <div class="col-5">
        <HomeDeadlineInfo />
      </div>
    </div>
  </q-page>
</template>

<script>
import HomeMainInfo from 'components/admin/HomeMainInfo.vue'
import HomeDeadlineInfo from 'components/admin/HomeCurrentCoopDeadlines.vue'

export default {
  name: 'AdminHome',
  components: {
    HomeMainInfo,
    HomeDeadlineInfo
  }
}
</script>

<style lang="scss">
#container {
  width: 92%;
}
</style>
