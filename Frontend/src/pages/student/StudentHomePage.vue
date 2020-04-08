<template>
  <BasePage>
    <HomeCurrentCoopSection />
    <HomeUpcomingCoopsSection />
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";
import HomeCurrentCoopSection from "components/student/HomeCurrentCoopSection.vue";
import HomeUpcomingCoopsSection from "components/student/HomeUpcomingCoopsSection.vue";

export default {
  name: "StudentHomePage",
  components: {
    BasePage,
    HomeCurrentCoopSection,
    HomeUpcomingCoopsSection
  }
};
</script>

<style scoped lang="scss"></style>
