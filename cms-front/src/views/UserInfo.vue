<template>
  <div class="main">
    <div class="main-header">
      个人中心
    </div>
    <div class="main-container">
      <div class="main-container-content">
        <div class="main-container-content-left">
          <div class="main-container-content-left-user">
            <div class="main-container-content-left-user-title">
              个人信息
            </div>
            <div class="main-container-content-left-user-avatar">
              <a href="#" @click="showImgMobel">
                <img :src="user.userinfo.avatar" alt="用户头像">
                <div class="text">
                  <upload-outlined />
                </div>
              </a>
              <YUploadModel title="修改头像" :url="user.userinfo.avatar" :visible="uploadImgVisible"
                @handleOk="uploadHandleOk" @overCancel="uploadOverCancel" />
            </div>
            <div class="main-container-content-left-user-item">
              <div><user-outlined />&nbsp;用户名称</div>
              <div>{{ user.userinfo.userName }}</div>
            </div>
            <div class="main-container-content-left-user-item">
              <div><mobile-outlined />&nbsp;手机号码</div>
              <div>{{ user.userinfo.phonenumber }}</div>
            </div>
            <div class="main-container-content-left-user-item">
              <div><mail-outlined />&nbsp;用户邮箱</div>
              <div>{{ user.userinfo.email }}</div>
            </div>
            <div class="main-container-content-left-user-item">
              <div><experiment-outlined />&nbsp;创建日期</div>
              <div>{{ user.userinfo.createTime }}</div>
            </div>
          </div>
        </div>
        <div class="main-container-content-right">
          <div class="main-container-content-right-update">
            <div class="main-container-content-right-update-title">
              基本资料
            </div>
            <div class="main-container-content-right-update-content">
              <a-tabs v-model:activeKey="activeKey">
                <a-tab-pane key="1">
                  <template #tab>
                    <span>
                      基本资料
                    </span>
                  </template>
                  <a-form :model="userState" v-bind="layout" name="user-from" @finish="onFinish">
                    <a-form-item v-show="false" name="id" label="ID">
                      <a-input :value="userState.id">
                      </a-input>
                    </a-form-item>
                    <a-form-item name="nickName" label="用户昵称" :rules="[{ required: true, message: '请输入用户昵称！' }]">
                      <a-input v-model:value="userState.nickName" />
                    </a-form-item>
                    <a-form-item name="phonenumber" label="手机号码"
                      :rules="[{ required: true, message: '请输入正确的手机号码格式！', pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/ }]">
                      <a-input v-model:value="userState.phonenumber" />
                    </a-form-item>
                    <a-form-item name="email" label="邮箱"
                      :rules="[{ required: true, type: 'email', message: '请输入正确的邮箱格式！' }]">
                      <a-input v-model:value="userState.email" />
                    </a-form-item>
                    <a-form-item name="sex" label="性别">
                      <a-select ref="select" v-model:value="userState.sex" style="width: 120px">
                        <a-select-option value="男">男</a-select-option>
                        <a-select-option value="女">女</a-select-option>
                        <a-select-option value="未知">未知</a-select-option>
                      </a-select>
                    </a-form-item>
                    <a-form-item :wrapper-col="{ offset: 4, span: 10 }">
                      <a-button type="primary" html-type="submit">保存</a-button>
                      &nbsp;&nbsp;
                      <a-button type="primary" danger @click="() => { router.back() }">关闭</a-button>
                    </a-form-item>
                  </a-form>
                </a-tab-pane>
                <a-tab-pane key="2">
                  <template #tab>
                    <span>
                      修改密码
                    </span>
                  </template>
                  <a-form :model="passwordUpdateState" :rules="rules" v-bind="layout" name="password-from"
                    @finish="onPasswordFinish" ref="passwordFormRef">
                    <a-form-item name="beforePassword" label="旧密码">
                      <a-input-password v-model:value="passwordUpdateState.beforePassword" placeholder="请输入旧密码">
                      </a-input-password>
                    </a-form-item>
                    <a-form-item name="newPassword" label="新密码">
                      <a-input-password v-model:value="passwordUpdateState.newPassword" placeholder="请输入新密码">
                      </a-input-password>
                    </a-form-item>
                    <a-form-item name="reNewPassword" label="确认密码">
                      <a-input-password v-model:value="passwordUpdateState.reNewPassword" placeholder="请输入确认密码">
                      </a-input-password>
                    </a-form-item>
                    <a-form-item :wrapper-col="{ offset: 4, span: 10 }">
                      <a-button type="primary" html-type="submit">保存</a-button>
                      &nbsp;&nbsp;
                      <a-button type="primary" danger @click="() => { router.back() }">关闭</a-button>
                    </a-form-item>
                  </a-form>
                </a-tab-pane>
              </a-tabs>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import User from '@/api/user';
import useStore from '../store/index';
import { useRouter } from 'vue-router';
import YUploadModel from "@/components/YUploadModel.vue";
const router = useRouter();
const { user } = useStore();
const activeKey = ref('1');
//表单布局
const layout = {
  labelCol: {
    span: 3,
  },
  wrapperCol: {
    span: 21,
  },
};
//收集表单 修改基本信息

const userState = reactive({
  ...user.userinfo
});
//表单校验成功后函数
const onFinish = async values => {
  let flog = user.userinfo.nickName != values.nickName
    || user.userinfo.email != values.email
    || user.userinfo.phonenumber != values.phonenumber
    || user.userinfo.sex != values.sex;
  console.log(flog);
  if (flog) {
    const { data } = await User.updateUserOne(values);
    if (data.code === 200) {
      user.updateUserInfo(values.nickName, values.email, values.phonenumber, values.sex);
      message.success("更新用户信息成功");
    } else {
      message.error("更新用户信息失败");
    }
  } else {
    message.error("你没有修改个人信息");
  }

};


const passwordFormRef = ref();

//收集表单 修改密码-----------------------------------------
const passwordUpdateState = reactive({
  beforePassword: '',
  newPassword: '',
  reNewPassword: ''
});

const passwordPatt = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{5,15}$/;

const getValidatePassword = async (password, encodePassword) => {
  const { data } = await User.validatePassword(password, encodePassword);
  console.log(data.data);
  return data.data;
}

let validateBeforePassword = async (_rule, value) => {
  if (value === '') {
    return Promise.reject('请输入旧密码');
  } else if (!await getValidatePassword(value, user.userinfo.password)) {
    return Promise.reject('输入的旧密码不正确');
  } else {
    return Promise.resolve();
  }
}
let validatePass = async (_rule, value) => {
  if (value === '') {
    return Promise.reject('请输入密码');
  } else if (!passwordPatt.test(value)) {
    return Promise.reject('请输入6-18位包含大小写字母数字的密码');
  } else if (value == passwordUpdateState.beforePassword) {
    return Promise.reject('新密码不能和旧密码一致');
  } else {
    return Promise.resolve();
  }
};
let validatePass2 = async (_rule, value) => {
  if (value === '') {
    return Promise.reject('请再一次输入新密码');
  } else if (value !== passwordUpdateState.newPassword) {
    return Promise.reject("两次密码不匹配，请重新输入");
  } else {
    return Promise.resolve();
  }
};

const rules = {
  beforePassword: [{
    required: true,
    validator: validateBeforePassword,
    trigger: 'change',
  }],
  newPassword: [{
    required: true,
    validator: validatePass,
    trigger: 'change',
  }],
  reNewPassword: [{
    required: true,
    validator: validatePass2,
    trigger: 'change',
  }]
};

//提交表单校验成功后函数
const onPasswordFinish = async values => {
  const temp = { id: '', password: '' };
  temp.id = user.userinfo.id;
  temp.password = values.newPassword;
  const { data } = await User.updateUserOne(temp);
  if (data.code === 200) {
    passwordUpdateState.beforePassword = '';
    passwordUpdateState.newPassword = '';
    passwordUpdateState.reNewPassword = '';
    user.updatePassword(await User.getPasswordById(temp.id).data.data);
    message.success("更新密码成功");
  } else {
    message.error("更新密码失败");
  }
};

//头像上传
let uploadImgVisible = ref(false);
const showImgMobel = () => {
  uploadImgVisible.value = !uploadImgVisible.value;
}

const uploadHandleOk = async (uploadUrl) => {
  user.userinfo.avatar = uploadUrl;
  await User.updateUserOne({
    id: user.userinfo.id,
    avatar: uploadUrl
  });
  uploadImgVisible.value = false;

}
const uploadOverCancel = () => {
  uploadImgVisible.value = false;
}


</script>
<style lang="less" scoped>
.img {
  width: 100%;
  height: 100%;
}

.main {
  width: 100%;
  height: 100%;

  &-header {
    display: flex;
    align-items: center;
    width: 100%;
    height: 48px;
    padding-left: 10px;
    font-size: 18px;
    font-weight: bold;
    background-color: #fff;
  }

  &-container {
    width: 100%;
    height: 100%;
    padding: 10px;

    &-content {
      display: flex;
      justify-content: space-between;
      height: 100%;
      padding: 10px;
      overflow: hidden;
      background-color: #fff;
      box-shadow: 0 2px 4px rgb(0 0 0 / 12%), 0 0 6px rgb(0 0 0 / 4%);

      &-left {
        flex: 2.5;
        height: 100%;
        padding: 10px;
        margin-right: 10px;

        &-user {
          width: 100%;
          padding: 10px;
          border-radius: 4px;
          box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);

          >div {
            height: 50px;
            font-size: 14px;
            line-height: 50px;
            border-bottom: 1px solid #ccc;
          }

          &-title {
            font-size: 18px !important;
          }

          &-avatar {
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
            width: 100%;
            height: 160px !important;
            padding: 20px auto;


            img {
              position: relative;
              width: 100px;
              height: 100px;
              border: 1px solid #ccc;
              border-radius: 50%;
            }

            //设置提示文字的样式
            .text {
              position: absolute;
              top: 0;
              right: 0;
              bottom: 0;
              left: 0;
              display: flex;
              align-items: center;
              justify-content: center;
              font-size: 24px;
              text-align: center;
              background: rgb(0 0 0 / 30%); //设置背景的透明度
              border-radius: 50%;
              opacity: 0; //ease-in-out设置提示信息以慢速度开始和结束的过渡效果
              transition: all 0.35s ease-in-out;
              backface-visibility: hidden; //这里还是子绝父相，使用最多//隐藏旋转元素的背面//加浏览器的私有前缀是考虑到兼容性问题
            }

            a {
              display: inline-block;
              width: 100px;
              height: 100px;
              overflow: hidden;
              border-radius: 50%;
            }

            a:hover .text {
              opacity: 1; //opacity从0变成1，从透明到不透明
            }

          }

          &-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
          }

        }
      }

      &-right {
        flex: 6.5;
        height: 100%;
        padding: 10px;
        margin-left: 10px;

        &-update {
          width: 100%;
          padding: 10px;
          border-radius: 4px;
          box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);

          &-title {
            height: 50px;
            font-size: 18px !important;
            line-height: 50px;
            border-bottom: 1px solid #ccc;
          }

        }
      }
    }
  }
}
</style>

