import {
  post,
  get,
  put,
  _delete
} from '@/utils/request';

export default class Role {

  static async getRolesAndStatusIsNormal() {
    return get('/role/status_normal');
  }

  /**
   * 根据用户ID获取角色信息
   * @param {Number} userId
   * @returns
   */
  static async getUserRolesById(userId) {
    return get('/role/user_roles', {
      userId
    });
  }

  static async getAllRules() {
    return get("/role/allroles");
  }

  /**
   *获取角色分页数据
   * @param {Number} current  当前页
   * @param {Number} pageSize 条数
   * @returns
   */
  static async getRolePage(current, pageSize) {
    return get("/role/page", {
      current,
      pageSize
    })
  }

  /**
   * 根据角色ID删除角色
   * @param {Long} id  角色ID
   * @returns
   */

  static async deleteRoleByRoleId(id) {
    return _delete('/role/' + id);
  }


  static async addRoleOne(roleInfo) {
    return post('/role/add', roleInfo);
  }

  static async updateRoleOne(roleInfo, chanceMenus) {
    return post('/role/update', {
      ...roleInfo,
      chanceMenus
    });
  }

  static async betchDeleteRole(ids) {
    return post("/role/betch_delete", {
      ids: ids
    })
  }

  static async getSearchRolePage(current,
    pageSize,
    name,
    roleKey,
    status,
    beginTime,
    lastTime) {
    return post("/role/return_search_page", {
      current,
      pageSize,
      name,
      roleKey,
      status,
      beginTime,
      lastTime
    })
  }
}
