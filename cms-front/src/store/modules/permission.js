import {
  defineStore
} from 'pinia';
import layoutRouters from '@/router/modules/layout';
import User from '@/api/user';
const constantRoutes = [...layoutRouters];
import
router
from '@/router';
// 处理路由
export const filterAsyncRouter = (routers) => { // 遍历后台传来的路由字符串，转换为组件对象
  const temp = routers.filter(router => {
    if (router.component) {
      if (router.component === 'Layout') { // Layout组件特殊处理
        router.redirect = router.redirect;
      } else {
        const component = router.component
        router.component = loadView(component)
      }
    }
    router.meta = {
      title: router.menuName,
      icon: router.icon,
      noCache: !router.cache,
      visible: router.visible,
      status: router.status,
      perms: router.perms,
      remark: router.remark,
      pid: router.pid,
      id: router.id,
      auth: router.auth,
      sort: router.sort
    }
    if (router.children && router.children.length) {
      router.children = filterAsyncRouter(router.children)
    }
    return true
  });

  const resArray = []
  temp.forEach(element => {
    if ((element.children == "" || element.children == null) && (element.redirect == "" || element.redirect == null)) {
      resArray.push({
        name: element.name,
        path: element.path,
        component: element.component,
        meta: element.meta
      });
    } else if (element.redirect == "" || element.redirect == null) {
      resArray.push({
        name: element.name,
        path: element.path,
        component: element.component,
        meta: element.meta,
        children: element.children
      });
    } else if (element.children == "" || element.children == null) {
      resArray.push({
        name: element.name,
        path: element.path,
        component: element.component,
        meta: element.meta,
        redirect: element.redirect
      });
    } else {
      resArray.push({
        name: element.name,
        path: element.path,
        component: element.component,
        meta: element.meta,
        children: element.children,
        redirect: element.redirect
      });
    }
  });
  return resArray;
}

export const loadView = (view) => {
  return () => import(`../../views/${view}.vue`)
}

function ArrayToTreeData(data) {
  const cloneData = JSON.parse(JSON.stringify(data)) // 对源数据深度克隆
  return cloneData.filter(father => {
    const branchArr = cloneData.filter(child => father.id === child.pid) // 返回每一项的子级数组
    branchArr.length > 0 ? father.children = branchArr : '' // 如果存在子级，则给父级添加一个children属性，并赋值
    return father.pid === null // 返回第一层
  })
}

const routerPackag = routers => {
  routers.filter(itemRouter => {
    if (itemRouter.component != "Layout") {
      router.addRoute("layout", itemRouter);
    }
    // 是否存在子集
    if (itemRouter.children && itemRouter.children.length) {
      routerPackag(itemRouter.children);
    }
    return true;
  });
};

const permissionsStore = defineStore('permissions', {
  state: () => {
    return {
      routes: [],
      addRoutes: [],
      menuLoaded: false
    }
  },
  actions: {
    removeRouters() {
      this.routes = [];
      this.addRoutes = [];
      this.menuLoaded = false;
    },
    setRouters(routes) {
      this.addRoutes = routes;
      this.routes = constantRoutes.concat(routes);
      console.log('routes', this.routes);
    },
    setMenuLoaded(menuLoaded) {
      this.menuLoaded = menuLoaded
    },
    resetRouter() {
      routerPackag(this.addRoutes);
    },
    async generateRoutes(userid) {
      const {
        data
      } = await User.getUserPermission(userid);
      let accessedRoutes = ArrayToTreeData(data.data);
      if (accessedRoutes && accessedRoutes.length) {
        const asyncRouter = filterAsyncRouter(accessedRoutes);
        this.setRouters(asyncRouter);
        this.setMenuLoaded(true);
        routerPackag(this.addRoutes);
      }
    },
  },
  persist: true
});
export default permissionsStore;
