export default [{
    path: '/',
    name: 'app',
    meta: {
      auth: false, // 添加该字段，表示进入这个路由是需要登录的
    },
    redirect: "/login",
  }, {
    path: '/login',
    name: 'login',
    meta: {
      auth: false, // 添加该字段，表示进入这个路由是需要登录的
    },
    component: () => import('@/views/Login.vue'),
  },
  {
    path: '/layout',
    name: 'layout',
    meta: {
      auth: true, // 添加该字段，表示进入这个路由是需要登录的
      title: '欢迎'
    },
    component: () => import('@/layout/Layout.vue'),
    children: [{
      path: 'userinfo',
      name: 'userinfo',
      meta: {
        auth: true, // 添加该字段，表示进入这个路由是需要登录的
        title: '个人中心'
      },
      component: () => import('@/views/UserInfo.vue'),
    }]
  }, {
    path: "/404",
    component: () => import("@/views/error/404.vue"),
  },
];
//{
//   path: '/layout',
//   name: 'layout',
//   meta: {
//     auth: true, // 添加该字段，表示进入这个路由是需要登录的
//   },
//   component: () => import('@/layout/Layout.vue'),
//   children: [{
//       path: 'overview',
//       name: 'overview',
//       meta: {
//         auth: true, // 添加该字段，表示进入这个路由是需要登录的
//       },
//       component: () => import('@/views/Overview.vue')
//     },
//     {
//       path: 'content_model',
//       name: 'contentModel',
//       meta: {
//         auth: true, // 添加该字段，表示进入这个路由是需要登录的
//       },
//       component: () => import('@/views/ContentModel.vue')
//     },

//     {
//       path: 'content_collections',
//       name: 'contentCollections',
//       meta: {
//         auth: true, // 添加该字段，表示进入这个路由是需要登录的
//       },
//       redirect: "/app"
//     },
//     {
//       path: 'permission_manager',
//       name: 'permissionManager',
//       meta: {
//         auth: true, // 添加该字段，表示进入这个路由是需要登录的
//       },
//       redirect: "/app",
//       children: [{
//           path: 'role_manager',
//           name: 'roleManager',
//           meta: {
//             auth: true, // 添加该字段，表示进入这个路由是需要登录的
//           },
//           component: () => import('@/views/RoleManager.vue')
//         },

//         {
//           path: 'system_user',
//           name: 'systemUser',
//           meta: {
//             auth: true, // 添加该字段，表示进入这个路由是需要登录的
//           },
//           component: () => import('@/views/SystemUser.vue')
//         },
//       ]
//     },
//     {
//       path: 'system_setting',
//       name: 'systemSetting',
//       meta: {
//         auth: true, // 添加该字段，表示进入这个路由是需要登录的
//       },
//       component: () => import('@/views/SystemSetting.vue')
//     }
//   ],
//   },
// {
//   path: "/:pathMatch(.*)*",
//   redirect: "/404",
//}
