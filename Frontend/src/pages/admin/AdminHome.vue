<template>
  <q-page class="flex justify-center">
    <div id="container" class="row">
      <HomeMainInfo />
    </div>
  </q-page>
</template>

<script>
import HomeMainInfo from "components/admin/HomeMainInfo.vue";

export default {
  name: "AdminHome",
  components: {
    HomeMainInfo
  }
};
</script>

<style lang="scss">
#container {
}
</style>
