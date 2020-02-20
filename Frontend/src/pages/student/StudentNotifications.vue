<template>
  <q-page class="flex justify-center">
    <div id="container">
      <NotifsList />
    </div>
  </q-page>
</template>

<script>
import NotifsList from "components/student/NotificationsList.vue";

export default {
  components: {
    NotifsList
  }
};
</script>

<style lang="scss" scoped>
#container {
  width: 85%;
}
</style>
