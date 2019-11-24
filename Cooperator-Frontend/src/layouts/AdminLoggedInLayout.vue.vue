<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-toolbar-title @click="selectedTab=''">
          <router-link to="/admin/home">Co-operator</router-link>
        </q-toolbar-title>

        <q-tabs
          v-model="selectedTab"
          shrink
        >
          <q-route-tab
            name="tab1"
            label="All Co-ops"
            to="/admin/coops"
          />
          <q-route-tab
            name="tab2"
            label="All Students"
            to="/admin/students"
          />
          <q-route-tab
            name="tab3"
            label="All Companies"
            to="/admin/companies"
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
  name: 'AdminLoggedInLayout',
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
