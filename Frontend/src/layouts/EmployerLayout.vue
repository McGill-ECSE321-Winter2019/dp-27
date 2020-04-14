<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-toolbar-title>
          <router-link to="/employer/upload">Co-operator</router-link>
          <span class="text-caption q-ml-sm">EMPLOYER</span>
        </q-toolbar-title>

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
import SidebarLink from "../components/SidebarLink.vue";

export default {
  name: "EmployerLayout",

  data() {
    return {
      darkModeOn: this.$q.localStorage.getItem("darkMode")
    };
  },
  created: function() {
    this.setDarkMode();
  },
  methods: {
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
</style>
