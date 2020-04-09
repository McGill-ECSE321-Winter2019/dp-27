<template>
  <BasePage title="Notifications">
    <NotificationsList />
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";
import NotificationsList from "components/student/NotificationsList.vue";

export default {
  components: {
    BasePage,
    NotificationsList
  }
};
</script>

<style lang="scss" scoped></style>
