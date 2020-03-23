<template>
  <q-page class="flex justify-center">
    <div id="container" class="row">
      <div class="col">
        <h4>Frequently Asked Questions</h4>

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
            </q-expansion-item></q-card-section
          >
        </q-card>
      </div>
    </div>
  </q-page>
</template>

<script>
import studentFaqData from "../../data/student-faq";

export default {
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
