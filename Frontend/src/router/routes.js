const routes = [
  {
    path: "/student",
    component: () => import("layouts/StudentLoggedInLayout.vue"),
    meta: { requiresStudentAuth: true },
    children: [
      { path: "", redirect: "home" },
      {
        path: "home",
        component: () => import("pages/student/StudentHome.vue")
      },
      {
        path: "profile",
        component: () => import("pages/student/StudentProfile.vue")
      },
      {
        path: "coops/:id",
        component: () => import("pages/student/StudentSpecificCoop.vue"),
        props: true
      },
      {
        path: "coops",
        component: () => import("pages/student/StudentCoops.vue")
      },
      {
        path: "reports",
        component: () => import("pages/student/StudentReports.vue")
      }
    ]
  },
  {
    path: "/admin",
    component: () => import("layouts/AdminLoggedInLayout.vue"),
    meta: { requiresAdminAuth: true },
    children: [
      { path: "", redirect: "home" },
      { path: "home", component: () => import("pages/admin/AdminHome.vue") },
      {
        path: "profile",
        component: () => import("pages/admin/AdminProfile.vue")
      },
      {
        path: "coops/view",
        component: () => import("pages/admin/AdminCoops.vue")
      },
      {
        path: "notifications",
        component: () => import("pages/admin/AdminNotifications.vue")
      }
    ]
  }
];

// Always leave this as last one
if (process.env.MODE !== "ssr") {
  routes.push({
    path: "*",
    component: () => import("pages/Error404.vue")
  });
}

export default routes;
