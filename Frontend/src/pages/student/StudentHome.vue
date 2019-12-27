<template>
  <q-page class="flex justify-center">
    <div
      id="container"
      class="row"
    >
      <div class="col-9">
        <HomeMainInfo />
      </div>
      <div class="col-3">
        <HomeCurrentCoopInfoWidget />
      </div>
    </div>
  </q-page>
</template>

<script>
import HomeCurrentCoopInfoWidget from 'components/student/HomeCurrentCoopInfoWidget.vue'
import HomeMainInfo from 'components/student/HomeMainInfo.vue'

export default {
  name: 'StudentHome',
  components: {
    HomeCurrentCoopInfoWidget,
    HomeMainInfo
  }
}
</script>

<style scoped lang="scss">
#container {
  width: 85%;
}
</style>
