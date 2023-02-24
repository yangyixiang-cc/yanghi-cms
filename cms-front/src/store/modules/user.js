import {
  defineStore
} from 'pinia';

const userStore = defineStore('user', {
  state: () => {
    return {
      userinfo: {},
      roles: [],
      permissions: []
    }
  },
  actions: {
    updateUserInfo(nickName, email, phonenumber, sex) {
      this.userinfo.nickName = nickName;
      this.userinfo.email = email;
      this.userinfo.phonenumber = phonenumber;
      this.userinfo.sex = sex;
    },
    updatePassword(password) {
      this.userinfo.password = password;
    },
    setUserInfo(userinfo) {
      this.userinfo = userinfo;
    },
    setUserAvatar(url) {
      this.userinfo.avatar = url;
    },
    removeUserInfo() {
      this.userinfo = {};
    },
    setRoles(roles) {
      this.roles = roles;
    },
    removeRoles() {
      this.roles = [];
    },
    setPermissions(permissions) {
      this.permissions = permissions;
    },
    removePermissions() {
      this.permissions = [];
    }
  },
  persist: true
});
export default userStore;
