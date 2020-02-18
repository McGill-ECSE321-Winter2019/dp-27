import Vue from "vue";
import Vuex from "vuex";

// import example from './module-example'

Vue.use(Vuex);

/*
 * If not building with SSR mode, you can
 * directly export the Store instantiation
 */

const Store = new Vuex.Store({
  state: {
    status: "",
    token: localStorage.getItem("token") || "", // not used currently
    userId: localStorage.getItem("userId") || "",
    currentUser: {},
    userExists: false,
    userType: localStorage.getItem("userType") || "" // admin or student
  },
  getters: {
    isLoggedIn: state => !!state.userId,
    authStatus: state => state.status
  },
  mutations: {
    auth_request(state) {
      state.status = "loading";
    },
    auth_success(state, authObj) {
      state.status = "success";
      state.token = authObj.token;
      state.currentUser = authObj.user;
      state.userExists = true;
    },
    auth_error(state) {
      state.status = "error";
    },
    logout(state) {
      state.status = "";
      state.token = "";
      state.currentUser = {};
      state.userExists = false;
      state.userId = "";
      state.userType = "";
    },
    set_user(state, user) {
      state.currentUser = user;
      state.userExists = true;
    }
  },
  actions: {
    // TODO: these are unimplemented
    login({ commit }, user) {
      return new Promise((resolve, reject) => {
        resolve();
      });
    },
    logout({ commit }) {
      return new Promise((resolve, reject) => {
        resolve();
      });
    }
  },
  modules: {
    // example
  },

  // enable strict mode (adds overhead!)
  // for dev mode only
  strict: process.env.DEV
});

export default Store;
