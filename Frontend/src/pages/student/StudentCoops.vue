<template>
  <BasePage title="My Co-ops">
    <CoopsList />
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";
import CoopsList from "components/student/CoopsList.vue";

export default {
  name: "StudentCoops",
  components: {
    BasePage,
    CoopsList
  }
};
</script>

<style lang="scss" scoped></style>
