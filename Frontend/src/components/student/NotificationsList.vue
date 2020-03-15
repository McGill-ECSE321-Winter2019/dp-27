<template>
  <div>
    <q-card flat bordered class="card">
      <q-card-section>
        <div class="text-h6">Notifications</div>
      </q-card-section>
      <q-card-section v-if="notifsLoaded">
        <NotificationListItem
          v-for="notification in notifications"
          :key="notification.id"
          :notification="notification"
        />
      </q-card-section>
      <q-card-section v-else>
        You have no notifications!
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
      notifsLoaded: false
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
        this.notifsLoaded = true;
        if (this.notifications.length == 0) {
          this.notifsLoaded = false;
        }
      });
    this.$axios
      .put("/notifications/" + user.id + "/mark-as-read", {
        headers: {
          Authorization: this.$store.state.token
        }
      })
      .then(resp => {});
  }
};
</script>

<style lang="scss" scoped>
.card {
  width: 100%;
  margin-top: 25px;
  margin-bottom: 25px;
}
</style>
