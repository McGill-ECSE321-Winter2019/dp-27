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
        path: "new-coop",
        component: () => import("pages/student/StudentAddNewCoop.vue")
      },
      {
        path: "reports",
        component: () => import("pages/student/StudentReports.vue")
      },
      {
<<<<<<< HEAD
<<<<<<< HEAD
        path: "help",
        component: () => import("pages/student/StudentFAQ.vue")
=======
        path: "notifications",
        component: () => import("pages/student/StudentNotifications.vue")
>>>>>>> 58362456... i think it works
=======
        path: "notifications",
        component: () => import("pages/student/StudentNotifications.vue")
>>>>>>> d256c4d0a1386f2d865cd5e49053ac68f02e3b1e
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
        path: "studentCoops",
        component: () => import("pages/admin/AdminStudentCoops.vue")
      },
      {
        path: "profile",
        component: () => import("pages/admin/AdminProfile.vue")
      },
      { path: "coops", component: () => import("pages/admin/AdminCoops.vue") },
      {
        path: "students",
        component: () => import("pages/admin/AdminStudents.vue")
      },
      {
        path: "student",
        component: () => import("pages/admin/AdminStudent.vue")
      },
      {
        path: "companies",
        component: () => import("pages/admin/AdminCompanies.vue")
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
