<template>
  <div>
    <q-card
      flat
      bordered
      class="card"
    >
      <q-card-section>
        <div class="text-h6">Notifications</div>
      </q-card-section>
        <q-card-section v-if="notifsLoaded">
          <NotificationListItem
            v-for="notif in notifications"
            :key="notif.id"
            :notif="notif"
            @child-clicked="handleSelect"
          />
        </q-card-section>
        <q-card-section v-else>
          You have no notifications!
        </q-card-section>
    </q-card>
  </div>
</template>

<script>
import NotificationListItem from 'components/student/NotificationListItem.vue'

export default {
  components: {
    NotificationListItem
  },
  data() {
    return {
      notfications: [],
      notifsLoaded: false
    };
  },
  created: function (){
    const user = this.$store.state.currentUser;
    this.$axios.get("/notifications/all/" + user.id ,{
          headers: {
            Authorization: this.$store.state.token
          }
        }).then(resp => {
      this.notifications = resp.data;
      if(notifications.length != 0) {
        this.notifsLoaded = true;
      }
    });
  },
}
</script>

<style lang="scss" scoped>
.card {
  width: 100%;
  margin-top: 25px;
  margin-bottom: 25px;
}
</style>
