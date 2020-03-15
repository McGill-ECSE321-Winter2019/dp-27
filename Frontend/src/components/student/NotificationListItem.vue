<template>
  <div>
    <div class="row items-center">
      <div class="col">
        <q-separator inset />
        <br />
        <div>
          <b v-if="!notification.seen" style="color:red">
            NEW &nbsp;
          </b>
          <b
            >From {{ notification.sender.firstName }}
            {{ notification.sender.lastName }}</b
          >
        </div>
        <div>{{ notification.timeStamp }}</div>
        <div>
          <b>{{ notification.title }}</b>
        </div>
        <div>{{ notification.body }}</div>
      </div>
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
  created() {}
};
</script>

<style lang="scss" scoped>
.section {
  margin-top: 15px;
}
</style>
