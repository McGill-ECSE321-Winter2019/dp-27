<template>
  <q-page class="flex justify-center">
    <div id="container" class="row">
      <div class="col">
        <HomeCurrentCoopSection />
        <HomeUpcomingCoopsSection />
      </div>
    </div>
  </q-page>
</template>

<script>
import HomeCurrentCoopSection from "components/student/HomeCurrentCoopSection.vue";
import HomeUpcomingCoopsSection from "components/student/HomeUpcomingCoopsSection.vue";

export default {
  name: "StudentHomePage",
  components: {
    HomeCurrentCoopSection,
    HomeUpcomingCoopsSection
  }
};
</script>

<style scoped lang="scss">
#container {
  width: 75%;
}
</style>
