import {
  post,
  get,
  put,
  _delete
} from '@/utils/request';

export default class Menu {

  static async getMenuTree() {
    return get('/menu/menu_tree');
  }

  static async getCheckedMenuTree(roleId) {
    return get('/menu/chance_menus', {
      roleId
    });
  }

  static async getAllRules() {
    return get("/menu/allmenus");
  }

  /**
   *获取菜单分页数据
   * @param {Number} current  当前页
   * @param {Number} pageSize 条数
   * @returns
   */
  static async getMenuPage(current, pageSize) {
    return get("/menu/page", {
      current,
      pageSize
    })
  }

  /**
   * 根据菜单ID删除菜单
   * @param {Long} id  菜单ID
   * @returns
   */

  static async deleteMenuByRoleId(id) {
    return _delete('/menu/' + id);
  }


  static async addMenuOne(menuInfo) {
    return post('/menu/add', menuInfo);
  }

  static async updateMenuOne(menuInfo) {
    return put('/menu/update',
      menuInfo,

    );
  }

  static async betchDeleteMenu(ids) {
    return post("/menu/betch_delete", {
      ids: ids
    })
  }

  static async getSearchMenuPage(current,
    pageSize,
    menuName,
    name,
    path,
    redirect,
    component,
    perms,
    status,
    visible,
    beginTime,
    lastTime) {
    return post("/menu/return_search_page", {
      current,
      pageSize,
      menuName,
      name,
      path,
      redirect,
      component,
      perms,
      status,
      visible,
      beginTime,
      lastTime
    })
  }


  static async getFatherMenus() {
    return get("/menu/father_menus")
  }


  static async getSubMenus() {
    return get("/menu/sub_menus")
  }

  static async getFatherMenuIdByMenuId(id) {
    return get("/menu/father_id", {
      id
    })
  }
}
