<template>
  <div>
    <q-item>
      <q-item-section>
        <div class="col">
          <div>
            <span class="text-subtitle1 text-weight-medium">
              {{ notification.title }}
            </span>

            <q-badge v-if="!notification.seen" color="red">
              NEW
            </q-badge>

            <div class="q-mb-md">{{ notification.body }}</div>

            <div class="row text-caption">
              From: {{ senderName }}
              <q-space />
              <span>{{ timestamp }}</span>
            </div>
          </div>
        </div>
      </q-item-section>
    </q-item>
    <q-separator />
  </div>
</template>

<script>
export default {
  name: "HomeNotificationsMenuItem",
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

<style lang="scss" scoped></style>
