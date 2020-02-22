<template>
  <q-page class="flex justify-center">
    <div id="container">
      <CoopsList />
    </div>
  </q-page>
</template>

<script>
import NotifList from 'components/student/NotificationList.vue'

export default {
  name: 'StudentNotifs',
  components: {
    NotifList
  }
}
</script>

<style lang="scss" scoped>
#container {
  width: 85%;
}
</style>
