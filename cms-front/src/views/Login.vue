<template>
  <div class="container" :style="screen">
    <div class="container-top">
      <div class="container-top-main">
        <div class="container-top-main-text">
          <h1>内容管理（CMS）</h1>
          <p class="p1">打造云端一体化数据运营平台</p>
        </div>
        <div class="container-top-main-input">
          <p class="p2">账户密码登录</p>
          <a-form :model="formState" name="normal_login" class="login-form" @finish="onFinish"
            @finishFailed="onFinishFailed">
            <a-form-item name="username"
              :rules="[{ required: true, message: '请输入以字母开头6-16位用户名！', pattern: /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/ }]">
              <a-input v-model:value="formState.username" size="large" placeholder="用户名">
                <template #prefix>
                  <UserOutlined class="site-form-item-icon" />
                </template>
              </a-input>
            </a-form-item>

            <a-form-item name="password"
              :rules="[{ required: true, message: '请输入6-18位包含大小写字母数字的密码！', pattern: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{5,15}$/ }]">
              <a-input-password v-model:value="formState.password" size="large" placeholder="密码">
                <template #prefix>
                  <LockOutlined class="site-form-item-icon" />
                </template>
              </a-input-password>
            </a-form-item>
            <a-form-item name="versify" :rules="[{ required: true, message: '请输入验证码！' }]">
              <a-input placeholder="请输入验证码" v-model:value="formState.versify" size="large" maxlength="4">
                <template #prefix>
                  <SafetyOutlined />
                </template>
                <template #suffix>
                  <a-tooltip title="刷新验证码">
                    <img :src="captcha.img" alt="刷新验证码" style="width: 100px;height: 100%;" @click="getCaptchaCode">
                  </a-tooltip>
                </template>
              </a-input>
            </a-form-item>
            <a-form-item>
              <a-form-item name="remember" no-style>
                <a-checkbox v-model:checked="formState.remember">记住密码</a-checkbox>
              </a-form-item>
              <a class="login-form-forgot" href="">忘记密码</a>
            </a-form-item>

            <a-form-item>
              <a-button :disabled="disabled" type="primary" html-type="submit" class="login-form-button" size="large"
                block>
                登录
              </a-button>
              <br />
              <br />
              <a href="">现在注册!</a>
            </a-form-item>
          </a-form>
        </div>
      </div>
    </div>
    <div class="container-bot">
      无头内容管理系统（CMS） - 2.13.10<br /><br />
      Copyright 2020-2023 by yanghi
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, watch, computed, reactive } from 'vue';
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import User from '@/api/user';
import useStore from '../store/index';
import {
  setToken,
  removeToken
} from '@/utils/auth';
import { useRouter, useRoute } from 'vue-router'
const router = useRouter()
const route = useRoute()

const { user, permissions } = useStore();

const screenWidth = ref(window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth);

const captcha = reactive({
  token: '',
  expire: '',
  img: ''
});


const getCaptchaCode = async () => {
  const { data } = await User.getVersifyCode();
  if (data.code == 200) {
    captcha.token = data.data.token;
    captcha.expire = data.data.expire;
    captcha.img = "data:image/jpeg;base64," + data.data.img;
  } else {
    message.error("验证码获取失败，请刷新验证码！");
  }
}


onMounted(async () => {

  window.onresize = () => {
    return (() => {
      screenWidth.value = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth
    })()
  }
  await getCaptchaCode();
})


watch(screenWidth, (newX) => {
  if (newX < 1024) {
    screenWidth.value = 1024;
  }
  screenWidth.value = newX;
})
const screen = computed(() => {
  return {
    width: screenWidth.value + "px",
    height: "100vh"
  }
});

const formState = reactive({
  username: '',
  password: '',
  remember: true,
  versify: '',
});
const onFinish = async (values) => {
  const { data: res } = await User.versifyCode(captcha.token,
    values.versify);
  if (res.code == 200) {
    const { data } = await User.login(values.username, values.password);
    if (data.code === 200) {
      if (data.data.token) {
        setToken(data.data.token);
      }
      user.setUserInfo(data.data.user);
      user.setRoles(data.data.roles);
      user.setPermissions(data.data.permissions);
      await permissions.generateRoutes(data.data.user.id);
      message.success(data.msg);
      router.push({
        path: '/layout/overview', replace: true, query: {
          ...route.query,
        },
      })
    } else {
      message.error(data.msg);
      user.removeUserInfo();
      user.removeRoles();
      user.removePermissions();
      removeToken();
    }
  } else {
    message.error(res.msg);
  }
};
const onFinishFailed = errorInfo => {
  console.log('Failed:', errorInfo);
};
const disabled = computed(() => {
  return !(formState.username && formState.password);
});

</script>

<style scoped lang="less">
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-right: auto;
  margin-left: auto;
  text-align: center;
  background-color: #f0f2f5;

  &-top {
    display: flex;
    flex: 9;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;

    &-main {
      display: flex;
      flex-direction: column;
      justify-content: start;
      width: 420px;
      height: 550px;
      padding: 20px;
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgb(0 0 0 / 6%), 0 0 6px rgb(0 0 0 / 2%);

      &-text {
        >h1 {
          font-size: 32px;
          font-weight: bold;
        }
      }



      &-input {
        margin-top: 30px;

      }


    }


  }

  &-bot {
    display: flex;
    flex: 1;
    align-items: center;
    justify-content: center;
    width: 100%;
    font-size: 14px;
    color: #8b8d87;
  }
}

.p1 {
  font-size: 16px;
  color: #909399;
}

.p2 {
  font-size: 28px;
}

.pd {
  margin: 15px auto;
}

.btn {
  margin-top: 40px;
}

.input {
  width: 100% !important;
}
</style>
