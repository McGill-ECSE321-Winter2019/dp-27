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
          <router-link to="/admin/home">Co-operator</router-link>
          <span class="text-caption q-ml-sm">ADMIN</span>
        </q-toolbar-title>

        <q-btn dense round flat class="q-mr-sm" icon="notifications">
          <q-badge color="white" text-color="red" floating transparent>
            4
          </q-badge>
        </q-btn>

        <q-btn flat dense round icon="settings" aria-label="Settings">
          <q-menu>
            <q-list style="min-width: 100px">
              <q-item clickable v-close-popup>
                <q-item-section>username</q-item-section>
              </q-item>
              <q-item clickable v-close-popup>
                <q-item-section>Help</q-item-section>
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

    <q-drawer v-model="leftDrawerOpen" bordered content-class="bg-grey-1">
      <q-list>
        <q-item-label header class="text-grey-8">Navigation</q-item-label>
        <SidebarLink
          title="Co-ops"
          caption="View all co-op information"
          link="/admin/coops"
          icon="work_outline"
        />
        <SidebarLink
          title="Students"
          caption="View all student information"
          link="/admin/students"
          icon="accessibility_new"
        />
        <SidebarLink
          title="Companies"
          caption="View all company information"
          link="/admin/companies"
          icon="domain"
        />
        <SidebarLink
          title="Report Configuration"
          caption="Configure reports to be submitted for co-op terms"
          link="/admin/report-config"
          icon="assignment"
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
      leftDrawerOpen: false
    };
  }
};
</script>

<style lang="scss" scoped>
a {
  color: inherit;
  text-decoration: inherit;
}
</style>
