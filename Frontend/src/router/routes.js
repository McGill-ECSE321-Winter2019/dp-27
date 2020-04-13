const routes = [
  {
    path: "/login",
    component: () => import("layouts/LoginLayout.vue")
  },
  {
    path: "/student",
    component: () => import("layouts/StudentLoggedInLayout.vue"),
    meta: { requiresStudentAuth: true },
    children: [
      { path: "", redirect: "home" },
      {
        path: "home",
        component: () => import("pages/student/StudentHomePage.vue")
      },
      {
        path: "profile",
        component: () => import("pages/student/StudentProfile.vue")
      },
      {
        path: "coops",
        component: () => import("pages/student/StudentCoops.vue")
      },
      {
        name: "StudentCoopDetailsPage",
        path: "coop-details/:coopId",
        component: () => import("pages/student/StudentCoopDetailsPage.vue")
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
        path: "help",
        component: () => import("pages/student/StudentFAQ.vue")
      },
      {
        path: "notifications",
        component: () => import("pages/student/StudentNotifications.vue")
      }
    ]
  },
  {
    path: "/admin",
    component: () => import("layouts/AdminLoggedInLayout.vue"),
    meta: { requiresAdminAuth: true },
    children: [
      {
        path: "",
        redirect: "home"
      },
      {
        path: "home",
        component: () => import("pages/admin/AdminHomePage.vue")
      },
      {
        path: "student-coops",
        component: () => import("pages/admin/AdminStudentCoops.vue")
      },
      {
        path: "profile",
        component: () => import("pages/admin/AdminProfile.vue")
      },
      {
        path: "coops",
        component: () => import("pages/admin/AdminCoops.vue")
      },
      {
        name: "Review",
        path: "coops/review",
        component: () => import("pages/admin/AdminCoopReviewPage.vue"),
        props: true
      },
      {
        name: "AdminViewStudents",
        path: "students",
        component: () => import("pages/admin/AdminStudentsPage.vue"),
        props: true
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
        path: "report-config",
        component: () => import("pages/admin/AdminReportConfigPage.vue")
      },
      {
        path: "csv-parse",
        component: () => import("pages/admin/CsvParse.vue")
      },
      {
        path: "create-notification",
        component: () => import("pages/admin/AdminCreateNotificationPage.vue"),
        props: true,
        name: "CreateNotif"
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
