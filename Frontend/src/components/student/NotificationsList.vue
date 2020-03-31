<template>
  <div>
    <q-card flat bordered class="card">
      <q-card-section>
        <div class="text-h6">Notifications</div>
      </q-card-section>
      <!-- Show spinner while loading -->
      <div v-if="loading" class="center-item q-mb-md">
        <q-spinner color="primary" size="3em" />
      </div>
      <!-- Show notifications -->
      <div v-else>
        <q-card-section v-if="notifications.length > 0">
          <NotificationListItem
            v-for="notification in notifications"
            :key="notification.id"
            :notification="notification"
          />
        </q-card-section>
        <q-card-section v-else>
          You have no notifications!
        </q-card-section>
      </div>
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
        this.notifications = resp.data;
        this.loading = false;
      });
    this.$axios.put("/notifications/" + user.id + "/mark-as-read", {
      headers: {
        Authorization: this.$store.state.token
      }
    });
  }
};
</script>

<style lang="scss" scoped>
.card {
  width: 100%;
  margin-top: 25px;
  margin-bottom: 25px;
}

.center-item {
  text-align: center;
}
</style>
