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

        <q-btn dense round flat class="q-mr-sm" icon="notifications">
          <q-badge color="white" text-color="red" floating transparent>
            {unseen.length}
          </q-badge>
        </q-btn>

        <q-btn flat dense round icon="settings" aria-label="Settings">
          <q-menu>
            <q-list style="min-width: 125px">
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

    <q-drawer v-model="leftDrawerOpen" bordered content-class="bg-grey-1">
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
import SidebarLink from "../components/SidebarLink.vue";

export default {
  name: "LoggedInLayout",
  components: {
    SidebarLink
  },
  data() {
    return {
      leftDrawerOpen: false,
      unseen: []
    };
  },
  created: function (){
    const user = this.$store.state.currentUser;
    this.$axios.get("/notifications/" + user.id + "/unread",{
          headers: {
            Authorization: this.$store.state.token
          }
        }).then(resp => {
      this.unseen = resp.data;
    });
  },
  methods: {
    //open notification popup?? 
  }
};
</script>

<style lang="scss" scoped>
a {
  color: inherit;
  text-decoration: inherit;
}
</style>
