<!-- This component sets a standard width and title style for every
page to use. Note that the title is optional.

This is how it can be used in page components:

<BasePage title="This is a title">
    ...whatever content you want on the page...
</BasePage>
-->
<template>
  <q-page class="flex justify-center">
    <div id="container" class="row">
      <div class="col">
        <h4 v-if="title">{{ title }}</h4>
        <!-- Insert main page information here -->
        <slot></slot>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
  name: "BasePage",
  props: {
    title: String
  }
};
</script>

<style lang="scss" scoped>
#container {
  width: 75%;
}
</style>
