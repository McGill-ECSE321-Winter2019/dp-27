import Vue from "vue";
import VueRouter from "vue-router";

import axios from "axios";
import routes from "./routes";
import Store from "../store";

import { Loading } from "quasar";

Vue.use(VueRouter);

var config = require("../config");

// Axios config
const frontendUrl = config.build.host + ":" + config.build.port;
const backendUrl = config.build.backendHost + ":" + config.build.backendPort;

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: {
    "Access-Control-Allow-Origin": frontendUrl,
    "Content-Type": "application/json"
  }
});

/*
 * If not building with SSR mode, you can
 * directly export the Router instantiation
 */

export default function(/* { store, ssrContext } */) {
  const Router = new VueRouter({
    scrollBehavior: () => ({ x: 0, y: 0 }),
    routes,

    // Leave these as is and change from quasar.conf.js instead!
    // quasar.conf.js -> build -> vueRouterMode
    // quasar.conf.js -> build -> publicPath
    mode: process.env.VUE_ROUTER_MODE,
    base: process.env.VUE_ROUTER_BASE
  });

  Router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresStudentAuth)) {
      Loading.show();

      // this route requires Student auth, check if logged in
      if (!Store.getters.isLoggedIn || Store.state.userType !== "student") {
        console.log("Not logged in or wrong user type for this page");
        Loading.hide();
        next({
          path: "/login",
          query: { redirect: to.fullPath }
        });
      } else if (Store.getters.isLoggedIn && !Store.state.userExists) {
        console.log("User ID specified, but no current user in Store");
        // get user with stored ID
        AXIOS.get("/students/id/" + Store.state.userId)
          .then(resp => {
            Store.commit("set_user", resp.data);
            Loading.hide();
            next();
          })
          .catch(() => {
            // user not found
            Loading.hide();
            Store.dispatch("logout").then(
              next({
                path: "/login",
                query: { redirect: to.fullPath }
              })
            );
            next();
          });
      } else {
        console.log("Logged in properly");
        Loading.hide();
        next();
      }
    } else if (to.matched.some(record => record.meta.requiresAdminAuth)) {
      Loading.show();
      // this route requires Admin auth, check if logged in
      if (!Store.getters.isLoggedIn || Store.state.userType !== "admin") {
        console.log("Not logged in or wrong user type for this page");
        Loading.hide();
        next({
          path: "/login",
          query: { redirect: to.fullPath }
        });
      } else if (Store.getters.isLoggedIn && !Store.state.userExists) {
        console.log("User ID specified, but no current user in Store");
        // get user with stored ID
        AXIOS.get("/admins/" + Store.state.userId)
          .then(resp => {
            Store.commit("set_user", resp.data);
            Loading.hide();
            next();
          })
          .catch(() => {
            // user not found
            Loading.hide();
            Store.dispatch("logout").then(
              next({
                path: "/login",
                query: { redirect: to.fullPath }
              })
            );
            next();
          });
      } else {
        console.log("Logged in properly");
        Loading.hide();
        next();
      }
    } else {
      Loading.hide();
      next(); // always call next()
    }
  });

  return Router;
}
