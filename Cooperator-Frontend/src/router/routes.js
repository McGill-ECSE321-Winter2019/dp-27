
const routes = [
  {
    path: '/student',
    component: () => import('layouts/MyLayout.vue'),
    children: [
      { path: 'home', component: () => import('pages/student/StudentHome.vue') },
      { path: 'profile', component: () => import('pages/student/StudentProfile.vue') },
      { path: 'coops/:id', component: () => import('pages/student/StudentSpecificCoop.vue'), props: true },
      { path: 'coops', component: () => import('pages/student/StudentCoops.vue') }
    ]
  }
]

// Always leave this as last one
if (process.env.MODE !== 'ssr') {
  routes.push({
    path: '*',
    component: () => import('pages/Error404.vue')
  })
}

export default routes
