<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          @click="leftDrawerOpen = !leftDrawerOpen"
          icon="menu"
          aria-label="Menu"
        />

        <q-toolbar-title>
          <router-link to="/student/home">Co-operator</router-link>
        </q-toolbar-title>

        <!-- Mark all notifications as read when the bell is clicked -->
        <q-btn
          dense
          round
          flat
          class="q-mr-sm"
          icon="notifications"
          @click="markAsRead"
        >
          <q-menu v-if="recentNotifications === null">
            <q-list>
              <q-item>
                <q-item-section>
                  <div class="center-item" id="notifications-menu">
                    <q-spinner color="primary" size="3em" class="q-ma-md" />
                  </div>
                </q-item-section>
              </q-item>
            </q-list>
          </q-menu>

          <q-menu v-else>
            <q-list>
              <HomeNotificationsMenuItem
                v-for="n in recentNotifications"
                :key="n.id"
                :notification="n"
              />
            </q-list>
            <q-btn
              flat
              v-close-popup
              color="primary"
              label="View All"
              class="q-ma-sm"
              @click="goToNotifPage"
            />
          </q-menu>

          <q-badge
            v-if="unseen.length > 0"
            color="white"
            text-color="red"
            floating
            transparent
          >
            {{ unseen.length }}
          </q-badge>
        </q-btn>

        <q-btn flat dense round icon="settings" aria-label="Settings">
          <q-menu>
            <q-list style="min-width: 200px">
              <q-item>
                <q-item-section>
                  <q-item-label>Dark Mode</q-item-label>
                </q-item-section>
                <q-item-section side top>
                  <q-toggle
                    color="white"
                    v-model="darkModeOn"
                    val="picture"
                    @input="setDarkMode"
                  />
                </q-item-section>
              </q-item>
              <q-separator />
              <q-item clickable v-close-popup>
                <q-item-section>Settings</q-item-section>
              </q-item>
              <q-item clickable v-close-popup>
                <q-item-section>Log Out</q-item-section>
              </q-item>
            </q-list>
          </q-menu>
        </q-btn>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawerOpen" bordered>
      <q-list>
        <q-item-label header class="text-grey-8">Navigation</q-item-label>
        <SidebarLink
          title="Add New Co-op"
          caption="Add a new co-op term"
          link="/student/new-coop"
          icon="add"
        />
        <SidebarLink
          title="My Co-ops"
          caption="View current and past co-ops"
          link="/student/coops"
          icon="work_outline"
        />
        <SidebarLink
          title="My Reports"
          caption="View submitted and pending reports"
          link="/student/reports"
          icon="assignment"
        />
        <SidebarLink
          title="Help"
          caption="View frequently asked questions"
          link="/student/help"
          icon="help_outline"
        />
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
import HomeNotificationsMenuItem from "../components/student/HomeNotificationsMenuItem.vue";
import SidebarLink from "../components/SidebarLink.vue";

export default {
  name: "StudentLoggedInLayout",
  components: {
    HomeNotificationsMenuItem,
    SidebarLink
  },
  data() {
    return {
      leftDrawerOpen: false,
      darkModeOn: this.$q.localStorage.getItem("darkMode"),
      unseen: [],
      recentNotifications: null
    };
  },
  created: function() {
    this.setDarkMode();
    this.refreshNotifications();
  },
  methods: {
    goToNotifPage() {
      if (this.$route.path !== "/student/notifications") {
        this.$router.push("/student/notifications");
      }
    },
    refreshNotifications: function() {
      const user = this.$store.state.currentUser;
      // get all unread notifications
      this.$axios
        .get(`/notifications/student/${user.id}/unread`, {
          headers: {
            Authorization: this.$store.state.token
          }
        })
        .then(resp => {
          this.unseen = resp.data;
        });
      // get 5 most recent notifications
      this.$axios
        .get(`/notifications/student/${user.id}/recent`, {
          headers: {
            Authorization: this.$store.state.token
          },
          params: {
            fetchSize: 5
          }
        })
        .then(resp => {
          this.recentNotifications = resp.data;
        });
    },
    markAsRead: function() {
      const user = this.$store.state.currentUser;
      this.$axios.put(`/notifications/${user.id}/mark-as-read`, {
        headers: {
          Authorization: this.$store.state.token
        }
      });
    },
    setDarkMode: function() {
      if (this.darkModeOn === null) this.darkModeOn = false;

      this.$q.dark.set(this.darkModeOn);
      this.$q.localStorage.set("darkMode", this.darkModeOn);
    }
  }
};
</script>

<style lang="scss" scoped>
a {
  color: inherit;
  text-decoration: inherit;
}

#notifications-menu {
  width: 300px;
}

.center-item {
  text-align: center;
}
</style>
