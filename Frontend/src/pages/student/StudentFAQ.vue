<template>
  <BasePage title="Frequently Asked Questions">
    <q-card flat bordered class="q-mb-md">
      <q-card-section>
        <q-expansion-item
          expand-separator
          v-for="item in faqData"
          :key="item.question"
          :label="item.question"
          header-class="text-subtitle1"
        >
          <q-card>
            <q-card-section>
              {{ item.answer }}
            </q-card-section>
          </q-card>
        </q-expansion-item>
      </q-card-section>
    </q-card>
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";
import studentFaqData from "../../data/student-faq";

export default {
  name: "StudentFAQ",
  components: {
    BasePage
  },
  data() {
    return {
      faqData: studentFaqData
    };
  }
};
</script>

<style lang="scss" scoped>
#container {
  width: 75%;
}
</style>
