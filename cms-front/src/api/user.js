import {
  post,
  get,
  _delete,
  put
} from '@/utils/request';

export default class User {
  /**
   * 登录
   * @param {String} userName 用户名
   * @param {String} password 密码
   * @returns
   */
  static async login(userName, password) {
    return post('/user/login', {
      userName: userName,
      password: password,
    }, {}, {
      isToken: false
    });
  }
  /**
   * 退出登录
   */
  static async logout() {
    return get('/user/logout');
  }

  /**
   *
   * @returns 获取全部用户
   */
  static async getAllUser() {
    return get('/user/alluser');
  }

  /**
   * 根据用户ID删除用户
   * @param {Long} id  用户ID
   * @returns
   */

  static async deleteUserOneById(id) {
    return _delete('/user/' + id);
  }

  /**
   * 根据用户ID获取动态路由即用户的相关权限
   * 和能操作的菜单
   * @param {Long} id 用户ID
   */
  static async getUserPermission(id) {
    return get('/user/permission/' + id)
  }

  static async addUserOne(userInfo) {
    return post('/user/add', userInfo);
  }

  static async updateUserOne(userInfo) {
    return put('/user/update', userInfo);
  }

  static async betchDeleteUser(ids) {
    return post("/user/betch_delete", {
      ids: ids
    })
  }

  /**
   *获取用户分页数据
   * @param {Number} current  当前页
   * @param {Number} pageSize 条数
   * @returns
   */
  static async getUserPage(current, pageSize) {
    return get("/user/page", {
      current,
      pageSize
    })
  }

  /**
   *获取用户搜索分页数据
   * @param {Number} current  当前页
   * @param {Number} pageSize 条数
   * @returns
   */

  static async getSearchUserPage(current,
    pageSize,
    nickName,
    sex,
    status,
    email,
    phonenumber,
    beginTime,
    lastTime) {
    return post("/user/return_search_page", {
      current,
      pageSize,
      nickName,
      sex,
      status,
      email,
      phonenumber,
      beginTime,
      lastTime
    })
  }

  static async validatePassword(password, encodePassword) {
    return get("/user/validate_password", {
      password,
      encodePassword
    })
  }
  static async getPasswordById(userId) {
    return get("/user/return_encode_password", {
      userId
    })
  }
  //获取验证码
  static async getVersifyCode() {
    return get('/user/captcha')
  }

  //验证验证码
  static async versifyCode(token,
    inputCode) {
    return get('/user/verify_code', {
      token,
      inputCode
    })
  }
}
