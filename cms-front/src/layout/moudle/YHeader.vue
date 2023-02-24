<template>
  <header class="header">
    <div class="header-left">
      <img src="/public/favicon.ico" :alt="defaultSetting.title" class="header-left-img">
      <span class="header-left-text">{{ defaultSetting.title }}</span>
    </div>
    <div class="header-right">
      <a-popover placement="bottom">
        <template #content>
          <div class="person-info">
            <div @click="() => {
              router.push({
                name: 'userinfo'
              })
            }">个人中心</div>
            <div @click="showExitConfirm">退出登录</div>
          </div>
        </template>
        <template #title>
          <span>{{ userinfo.nickName }}</span>
        </template>
        <a-avatar :size="36" :alt="userinfo.nickName" :src="userinfo.avatar" shape="square" class="header-right-img">
          <template #icon>
            <UserOutlined />
          </template>
        </a-avatar>
      </a-popover>
      <!-- <a-tooltip title="消息" placement="bottom">
        <a-badge dot class="header-right-msg">
          <MailOutlined style="font-size: 16px" />
        </a-badge>
      </a-tooltip> -->
      <a-tooltip title="全屏" placement="bottom" class="header-right-msg">
        <ScreenFull />
      </a-tooltip>
      <a-dropdown class="header-right-more">
        <a class="ant-dropdown-link" @click.prevent>
          <EllipsisOutlined style="font-size: 16px " />
        </a>
        <template #overlay>
          <a-menu>
            <a-menu-item>
              <a href="javascript:;">帮助建议</a>
            </a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </div>
  </header>
</template>
<script setup>
import { computed, createVNode } from 'vue';
import User from '@/api/user';
import { message, Modal } from 'ant-design-vue';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import ScreenFull from "@/components/ScreenFull.vue";
import {
  removeToken
} from '@/utils/auth';
import useStore from '@/store/index';
import { useRouter } from 'vue-router'
import defaultSetting from "@/utils/setting";
const { user, permissions } = useStore();

const router = useRouter();

const handleButtonClick = e => {
  console.log('click left button', e);
};
const handleMenuClick = e => {
  console.log('click', e);
};
const userinfo = computed(() => {
  return user.userinfo;
});

const showExitConfirm = () => {
  Modal.confirm({
    title: '提示',
    icon: createVNode(ExclamationCircleOutlined),
    content: '确定注销并退出系统吗？',
    cancelText: '取消',
    okText: '确定',
    onOk() {
      logout();
    },
    onCancel() { },
  });
};

const logout = async () => {
  const { data } = await User.logout();
  if (data.code === 200 || data.code === 401) {
    user.removeUserInfo();
    user.removeRoles();
    user.removePermissions();
    permissions.removeRouters();
    removeToken();
    message.success("退出成功");
    router.push({
      path: '/', replace: true
    })
  } else {
    message.error(data.msg);

  }
}
</script>
<style scoped lang="less">
.header {
  display: flex;
  justify-content: first baseline;
  width: 100%;
  height: 100%;
  overflow: hidden;
  background: #606c88 !important;

  /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, #3f4c6b, #606c88) !important;

  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right, #3f4c6b, #606c88) !important;

  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  &-left {
    display: flex;
    align-items: center;
    justify-content: space-evenly;
    width: 240px;
    height: 100%;

    &-img {
      width: 80px;
      height: 80%;
    }

    &-text {
      font-size: 14px;
      color: #fff;
    }
  }

  &-right {
    display: flex;
    flex: 1;
    align-items: center;
    justify-content: flex-end;

    &-img {
      margin-right: 10px;
    }

    &-msg {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 40px;
      height: 36px;
      margin-right: 10px;
      line-height: 1.5;
      text-align: center;
      background-color: #fff;
      border-radius: 2px;
    }

    &-question {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 40px;
      height: 36px;
      margin-right: 10px;
      line-height: 1.5;
      text-align: center;
      background-color: #fff;
      border-radius: 2px;
    }

    &-more {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 40px;
      height: 36px;
      margin-right: 10px;
      line-height: 1.5;
      text-align: center;
      background-color: #fff;
      border-radius: 2px;
    }
  }
}

.demo-dropdown-wrap :deep(.ant-dropdown-button) {
  margin-right: 8px;
  margin-bottom: 8px;
}

.person-info {
  >div {
    width: 100%;
    height: 50px;
    margin: 10px auto;
    line-height: 50px;
    text-align: center;
    cursor: pointer;
    border-radius: 4px;
  }

  /* stylelint-disable-next-line rule-empty-line-before */
  >div:hover {
    background-color: rgb(145 213 255 / 50%);
  }
}
</style>
