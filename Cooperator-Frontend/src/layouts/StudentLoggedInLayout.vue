<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-toolbar-title @click="selectedTab=''">
          <router-link to="/student/home">Co-operator</router-link>
        </q-toolbar-title>

        <q-tabs
          v-model="selectedTab"
          shrink
        >
          <q-route-tab
            name="tab1"
            label="My Co-ops"
            to="/student/coops"
          />
          <q-route-tab
            name="tab2"
            label="My Reports"
            to="/student/reports"
          />
        </q-tabs>

        <q-btn
          dense
          round
          flat
          class="q-mr-sm"
          icon="notifications"
        >
          <q-badge
            color="white"
            text-color="red"
            floating
            transparent
          >
            4
          </q-badge>
        </q-btn>

        <q-btn
          flat
          dense
          round
          @click="leftDrawerOpen = !leftDrawerOpen"
          icon="settings"
          aria-label="Settings"
        >
          <q-menu>
            <q-list style="min-width: 100px">
              <q-item
                clickable
                v-close-popup
              >
                <q-item-section>username</q-item-section>
              </q-item>
              <q-item
                clickable
                v-close-popup
              >
                <q-item-section>Help</q-item-section>
              </q-item>
              <q-separator />
              <q-item
                clickable
                v-close-popup
              >
                <q-item-section>Settings</q-item-section>
              </q-item>
              <q-item
                clickable
                v-close-popup
              >
                <q-item-section>Log Out</q-item-section>
              </q-item>
            </q-list>
          </q-menu>
        </q-btn>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
export default {
  name: 'StudentLoggedInLayout',
  data () {
    return {
      selectedTab: ''
    }
  }
}
</script>

<style lang="scss" scoped>
a {
  color: inherit;
  text-decoration: inherit;
}
</style>
