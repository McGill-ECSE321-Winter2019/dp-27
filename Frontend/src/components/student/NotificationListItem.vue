<template>
  <div class="row items-center">
    <div class="col">
      <div>
        <span
          v-if="!notification.seen"
          class="text-weight-medium"
          style="color:red"
        >
          NEW &nbsp;
        </span>

        <span class="text-h6 text-weight-medium">
          {{ notification.title }}
        </span>

        <div>{{ notification.body }}</div>

        <div class="row text-caption">
          From: {{ senderName }}
          <q-space />
          <span>{{ timestamp }}</span>
        </div>
      </div>

      <q-separator class="q-mb-md q-mt-md" />
    </div>
  </div>
</template>

<script>
export default {
  props: {
    notification: {
      type: Object,
      required: true
    }
  },
  computed: {
    timestamp: function() {
      return this.$common.convertTimestampLongToString(
        this.notification.timeStamp
      );
    },
    senderName: function() {
      return `${this.notification.sender.firstName} ${this.notification.sender.lastName}`;
    }
  }
};
</script>

<style lang="scss" scoped>
.section {
  margin-top: 15px;
}
</style>
