<template>
  <BasePage>
    <HomeMainInfo />
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";
import HomeMainInfo from "components/admin/HomeMainInfo.vue";

export default {
  name: "AdminHome",
  components: {
    BasePage,
    HomeMainInfo
  }
};
</script>

<style lang="scss">
#container {
}
</style>
