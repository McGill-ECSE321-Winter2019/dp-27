<!-- This layout renders the login page for both Students and Admins. -->
<template>
  <q-layout>
    <q-page-container>
      <q-page class="flex column flex-center">
        <div class="text-h4 q-mb-md">Co-operator</div>
        <q-card id="card">
          <!-- Show buttons for Student and Admin logins -->
          <q-card-section v-if="!studentSelected && !adminSelected">
            <div class="text-caption center-item">
              Log in to McGill's Software Engineering Co-op system.
            </div>
            <div class="center-item">
              <q-btn
                label="Student Login"
                @click="studentSelected = true"
                color="primary"
                rounded
                class="nav-button q-pa-xs q-ma-md"
              />
            </div>
            <div class="center-item">
              <q-btn
                label="Admin Login"
                @click="adminSelected = true"
                color="primary"
                rounded
                class="nav-button q-pa-xs q-ma-md"
              />
            </div>
          </q-card-section>
          <!-- Student login -->
          <q-card-section v-if="studentSelected">
            <q-form @submit="onStudentLogIn" class="q-gutter-md">
              <div class="center-item login-heading">
                <q-btn
                  icon="arrow_back"
                  flat
                  round
                  dense
                  class="left-button"
                  @click="studentSelected = false"
                />
                <div class="text-body1 text-weight-medium">
                  Student Login
                </div>
              </div>
              <div class="text-caption center-item">
                Log in to Co-operator using your McGill email and password.
              </div>
              <q-input
                outlined
                v-model="username"
                hint="Enter your email"
                suffix="@mail.mcgill.ca"
              />

              <q-input
                outlined
                v-model="password"
                type="password"
                hint="Enter your password"
              />
              <div>
                <q-btn label="Log In" type="submit" color="primary" />
              </div>
            </q-form>
          </q-card-section>
          <!-- Admin login -->
          <q-card-section v-if="adminSelected">
            <q-form @submit="onAdminLogIn" class="q-gutter-md">
              <div class="center-item login-heading">
                <q-btn
                  icon="arrow_back"
                  flat
                  round
                  dense
                  class="left-button"
                  @click="adminSelected = false"
                />
                <div class="text-body1 text-weight-medium">
                  Admin Login
                </div>
              </div>
              <div class="text-caption center-item">
                Log in to Co-operator using your McGill employee email and
                password.
              </div>
              <q-input
                outlined
                v-model="username"
                hint="Enter your email"
                suffix="@mcgill.ca"
              >
              </q-input>

              <q-input
                outlined
                v-model="password"
                type="password"
                hint="Enter your password"
              />

              <div>
                <q-btn label="Log In" type="submit" color="primary" />
              </div>
            </q-form>
          </q-card-section>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script>
export default {
  name: "LoginLayout",
  data: function() {
    return {
      studentSelected: false,
      adminSelected: false,
      username: "",
      password: ""
    };
  },
  methods: {
    onStudentLogIn: function() {},
    onAdminLogIn: function() {}
  }
};
</script>

<style lang="scss" scoped>
#card {
  width: 33%;
  min-width: 350px;
}

.login-heading {
  position: relative;
}

.center-item {
  text-align: center;
}

.nav-button {
  width: 50%;
}

.left-button {
  position: absolute;
  left: 0;
}
</style>
