<template>
  <div>
    <q-card
      flat
      bordered
      class="card"
    >
      <q-card-section>
        <div class="text-h6">Current Co-op</div>
      </q-card-section>

      <q-separator inset />

      <q-card-section>
        <CoopsListItem />
      </q-card-section>
    </q-card>

    <q-card
      flat
      bordered
      class="card"
    >
      <q-card-section>
        <div class="text-h6">Past Co-ops</div>
      </q-card-section>

      <q-separator inset />

      <q-card-section>None</q-card-section>
    </q-card>
  </div>
</template>

<script>
import NotificationListItem from 'components/student/NotificationListItem.vue'

export default {
  name: 'CoopsList',
  components: {
    CoopsListItem
  }
}
</script>

<style lang="scss" scoped>
.card {
  width: 100%;
  margin-top: 25px;
  margin-bottom: 25px;
}
</style>
