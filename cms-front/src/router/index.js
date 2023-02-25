import {
  createRouter,
  createWebHistory
} from 'vue-router';
import {
  getToken,
  removeToken
} from '@/utils/auth';
import useStore from '../store/index';
import layoutRouters from './modules/layout';
import defaultSettings from '@/utils/setting'
const constantRoutes = [...layoutRouters];


const router = createRouter({
  history: createWebHistory(
    import.meta.env.BASE_URL),
  scrollBehavior() {
    return {
      el: '#app',
      top: 0,
      behavior: 'smooth',
    };
  },
  routes: constantRoutes
});


// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  router.matcher = router.matcher
}


router.beforeEach(async (to, from, next) => {
  if (to.name === 'login') {
    document.title = defaultSettings.title;
  } else {
    document.title = to.meta.title;
  }
  //根据字段判断是否路由过滤
  const {
    user,
    permissions
  } = useStore();
  if (to.matched.some(record => record.meta.auth)) {
    if (getToken() !== null && getToken() !== "") {
      if (to.path === '/login') {
        next({
          path: '/'
        })
      }
      next();
    } else {
      if (to.path !== '/login') {
        next({
          path: '/login'
        })
      } else {
        next()
      }
    }
  } else {
    if (to.name === undefined) {
      resetRouter();
      await permissions.generateRoutes(user.userinfo.id);
      next({
        ...to,
        replace: true
      });
    } else {
      next();
    }
  }
});

export default router
