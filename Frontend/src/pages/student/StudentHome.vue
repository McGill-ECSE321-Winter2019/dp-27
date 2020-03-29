<template>
  <q-page class="flex justify-center">
    <div id="container" class="row">
      <div class="col">
        <HomeCoopSection />
      </div>
    </div>
  </q-page>
</template>

<script>
import HomeCoopSection from "components/student/HomeCoopSection.vue";

export default {
  name: "StudentHome",
  components: {
    HomeCoopSection
  }
};
</script>

<style scoped lang="scss">
#container {
  width: 75%;
}
</style>
