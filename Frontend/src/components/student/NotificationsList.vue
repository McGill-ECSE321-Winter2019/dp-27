<template>
  <div>
    <q-card flat bordered>
      <q-card-section>
        <!-- Show spinner while loading -->
        <div v-if="loading" class="center-item q-mb-md">
          <q-spinner color="primary" size="3em" />
        </div>

        <!-- Show notifications -->
        <div v-else>
          <div v-if="notifications.length > 0">
            <NotificationListItem
              v-for="notification in notifications"
              :key="notification.id"
              :notification="notification"
            />
          </div>
          <div v-else>
            You have no notifications!
          </div>
        </div>
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
import NotificationListItem from "components/student/NotificationListItem.vue";

export default {
  components: {
    NotificationListItem
  },
  data() {
    return {
      notifications: [],
      loading: true
    };
  },
  created: function() {
    const user = this.$store.state.currentUser;
    this.$axios
      .get("/notifications/student/" + user.id, {
        headers: {
          Authorization: this.$store.state.token
        }
      })
      .then(resp => {
        // sort notifications by timestamp
        this.notifications = resp.data.sort(
          this.$common.compareNotificationTimestamps
        );
        this.loading = false;
      });
    this.$axios.put(`/notifications/${user.id}/mark-as-read`, {
      headers: {
        Authorization: this.$store.state.token
      }
    });
  }
};
</script>

<style lang="scss" scoped>
.center-item {
  text-align: center;
}
</style>
