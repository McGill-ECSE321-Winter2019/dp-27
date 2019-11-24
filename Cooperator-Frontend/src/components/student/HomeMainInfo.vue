<template>
  <q-card
    flat
    bordered
    id="card"
  >
    <q-card-section>
      <div class="text-h6">Welcome, Student!</div>
    </q-card-section>

    <q-separator inset />

    <q-card-section>
      <strong>Upcoming Deadlines:</strong>
      <HomeDeadlineItem
        itemName="Offer Letter"
        deadline="May 1, 2020"
      />
      <HomeDeadlineItem
        itemName="Employer Evaluation"
        deadline="August 1, 2020"
      />
      <HomeDeadlineItem
        itemName="Other Report"
        deadline="August 28, 2020"
      />
    </q-card-section>
  </q-card>
</template>

<script>
import HomeDeadlineItem from 'components/student/HomeDeadlineItem.vue'

export default {
  name: 'HomeMainInfo',
  components: {
    HomeDeadlineItem
  }
}
</script>

<style scoped lang="scss">
h6 {
  margin: 10px;
}

#card {
  width: 100%;
  margin-top: 25px;
  margin-right: 10px;
}
</style>
