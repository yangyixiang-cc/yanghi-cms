<template>
  <YTable :columns="columns" :data="zData" :pagination="pagination" title="系统用户管理" @deleteDataById="deleteDataById"
    @handleUpdate="updateUserById" @handleAdd="addUser" @handleDelete="batchDeleteUser"
    @handlePageInfoUpdate="handlePageInfoUpdate" :reloadColumns="reloadColumns" :formSearchState="formSearchState"
    @onSearchFinish="onSearchFinish" :tableLoading="tableLoading" />
  <a-modal ok-text="确认" cancel-text="取消" v-model:visible="visible" :title="modelTitle" :confirm-loading="confirmLoading"
    @ok="handleOk" @cancel="overCancel">
    <a-form ref="userFormRef" :model="formState" v-bind="layout" name="user-from" @finish="onFinish">
      <a-form-item v-show="false" :name="['user', 'id']" label="ID">
        <a-input v-model:value="formState.user.id" disabled>
        </a-input>
      </a-form-item>
      <template v-if="modelTitle == '添加用户'">
        <a-form-item :name="['user', 'userName']" label="用户名"
          :rules="[{ required: true, message: '请输入以字母开头6-16位用户名！', pattern: /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/ }]">
          <a-input v-model:value="formState.user.userName" placeholder="用户名">
          </a-input>
        </a-form-item>
        <a-form-item :name="['user', 'password']" label="密码"
          :rules="[{ required: true, message: '请输入6-18位包含大小写字母数字的密码！', pattern: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{5,15}$/ }]">
          <a-input-password v-model:value="formState.user.password" placeholder="密码">
          </a-input-password>
        </a-form-item>
      </template>
      <a-form-item :name="['user', 'nickName']" label="昵称" :rules="[{ required: true, message: '请输入用户名！' }]">
        <a-input v-model:value="formState.user.nickName" />
      </a-form-item>
      <a-form-item :name="['user', 'phonenumber']" label="手机号"
        :rules="[{ message: '请输入正确的手机号码格式！', pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/ }]">
        <a-input v-model:value="formState.user.phonenumber" />
      </a-form-item>
      <a-form-item :name="['user', 'email']" label="邮箱" :rules="[{ type: 'email', message: '请输入正确的邮箱格式！' }]">
        <a-input v-model:value="formState.user.email" />
      </a-form-item>
      <a-form-item :name="['user', 'sex']" label="性别">
        <a-select ref="select" v-model:value="formState.user.sex" style="width: 120px">
          <a-select-option value="男">男</a-select-option>
          <a-select-option value="女">女</a-select-option>
          <a-select-option value="未知">未知</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item :name="['user', 'status']" label="状态">
        <a-radio-group v-model:value="formState.user.status">
          <a-radio :value="0">正常</a-radio>
          <a-radio :value="1">停用</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item :name="['user', 'avatar']" label="头像">
        <div v-if="formState.user.avatar" class="show-img">
          <div class="show-img-delete" @click="imgDel">
            <close-circle-outlined />
          </div>
          <img :src="formState.user.avatar" alt="avatar" />
        </div>
        <a-upload v-model:file-list="fileList" name="avatar" list-type="picture-card" :customRequest="uploadImg"
          :before-upload="beforeUpload" @change="handleImgChange" v-else>
          <div>
            <plus-outlined></plus-outlined>
            <div class="ant-upload-text">上传</div>
          </div>
        </a-upload>
      </a-form-item>
      <a-form-item :name="['user', 'rules']" label="角色">
        <a-select v-model:value="formState.user.roles" mode="multiple" style="width: 100%" placeholder="选择角色"
          :options="ruleOptions" @change="handleRuleChange" :fieldNames="ruleFieldNames"></a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import YTable from "@/components/YTable.vue";
import { reactive, onMounted, ref, shallowReactive, computed } from "vue";
import User from '@/api/user';
import Tool from "@/api/tool";
import Role from "@/api/role";
import { message } from 'ant-design-vue';
import dayjs from 'dayjs';
const columns = [{
  title: 'ID',
  width: 100,
  dataIndex: 'id',
  key: 'id',
  fixed: 'left',
},
{
  title: '账号',
  width: 100,
  dataIndex: 'userName',
  key: 'userName',
  fixed: 'left',
}, {
  title: '昵称',
  dataIndex: 'nickName',
  key: 'nickName',
  width: 150,
}, {
  title: '头像',
  width: 100,
  dataIndex: 'avatar',
  key: 'avatar',
  type: 'pic'
}, {
  title: '邮箱',
  dataIndex: 'email',
  key: 'email',
  type: 'email',
  width: 150,
}, {
  title: '手机号',
  dataIndex: 'phonenumber',
  key: 'phonenumber',
  type: 'tel',
  width: 150,
}, {
  title: '性别',
  dataIndex: 'sex',
  key: 'sex',
  width: 150,
}, {
  title: '状态',
  dataIndex: 'status',
  key: 'status',
  width: 150,
}, {
  title: '创建时间',
  dataIndex: 'createTime',
  key: 'createTime',
  type: 'time',
  width: 150,
}, {
  title: '操作',
  key: 'operation',
  fixed: 'right',
  width: 140,
  align: 'center'
}];
const zData = shallowReactive([]);

const modelTitle = ref('');

const pageInfo = ref({});
const tableLoading = ref(false);
//实现 分页请求函数
const getUserPage = async (current = 1, pageSize = 5) => {
  tableLoading.value = true;
  const { data } = await User.getUserPage(current, pageSize);
  if (data.code === 200) {
    let res = data.data;
    pageInfo.value = { ...res }
    zData.length = 0;
    zData.push(...res.records);
    tableLoading.value = false;
  } else {
    tableLoading.value = false;
    message.success("获取分页数据失败！");
  }
}

//分页实现
const pagination = computed(() => {
  return {
    size: 'large',
    current: pageInfo.value.current,
    pageSize: pageInfo.value.size,
    total: pageInfo.value.total,
    pageSizeOptions: ['5', '10', '15'],//可选的页面显示条数
    showTotal: (total, range) => {
      return range[0] + "-" + range[1] + " 共" + total + "条"
    }, //展示每页第几条至第几条和总数
    hideOnSinglePage: false, // 只有一页时是否隐藏分页器
    showQuickJumper: true, //是否可以快速跳转至某页
    showSizeChanger: true, //是否可以改变pageSize
  }
});



//修改数据
const visible = ref(false);
const confirmLoading = ref(false);

//表单布局
const layout = {
  labelCol: {
    span: 4,
  },
  wrapperCol: {
    span: 20,
  },
};

//收集表单
const formState = reactive({
  user: {
    id: '',
    userName: '',
    password: '',
    nickName: '',
    sex: '未知',
    email: '',
    phonenumber: '',
    status: 0,
    avatar: '',
    roles: [],
  },
});

//对接后端角色表-----------------------
const ruleFieldNames = { label: 'name', value: 'id', options: 'options' };
//加载角色信息
const ruleOptions = reactive([]);

//获取角色信息
const getRolesOptions = async () => {
  const { data } = await Role.getRolesAndStatusIsNormal();
  if (data.code === 200) {
    ruleOptions.length = 0;
    console.log(...data.data);
    ruleOptions.push(...data.data);
  } else {
    message.error("获取角色信息失败！");
  }
}

//用户改变角色信息后，更新方法
const handleRuleChange = (value, option) => {
  console.log(value, option);
}


//-----------------------------------文件上传
const fileList = ref([]);

//上传图片之前处理方法
const beforeUpload = file => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJpgOrPng) {
    message.error('只能上传JPEG或JPG格式图片！');
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    message.error('图片大小应小于2M！');
  }
  return isJpgOrPng && isLt2M;
};

// 删除图片
const imgDel = async () => {
  const { data } = await Tool.deleteFile(formState.user.avatar);
  if (data.code === 200) {
    message.success("删除图片成功！");
    formState.user.avatar = "";
    fileList.value.length = 0;
  } else {
    message.error("删除图片失败！");
  }
}
// 修改文件？没用到
const handleImgChange = (fileList) => {
  if (formState.user.avatar !== '' && formState.user.avatar !== null) {
    message.error("只能上传一张图片！");
    return
  }
  fileList.value = fileList;
}

const uploadImg = async (info) => {
  if (formState.user.avatar !== '' && formState.user.avatar !== null) {
    message.error("只能上传一张图片！");
    return
  }
  const formData = new FormData();
  formData.append('file', info.file);
  //后端接口
  const { data } = await Tool.uploadFile(formData);
  if (data.code === 200) {
    message.success(data.msg);
    info.onSuccess(data.data);
    let imgPath = data.data.path;
    formState.user.avatar = imgPath;
  } else {
    info.onError();
    message.error(data.msg);
  }
}

//退出模态框，清空信息
const overCancel = async () => {
  if (formState.user.avatar !== '' && modelTitle.value == '添加用户') {
    console.log(1);
    await imgDel();
  }
  formState.user = {};
}
//-----------------------------------
//表单完成后函数
const onFinish = values => {
  console.log('Success:', values);
};

const userFormRef = ref();
//提交表单
const handleOk = () => {
  confirmLoading.value = true;
  userFormRef.value.validateFields().then(async values => {
    //表单验证通过
    const res = await executeMethod(values.user);
    console.log(res);
    if (res.code === 200) {
      confirmLoading.value = false;
      visible.value = false;
      // triggerRef(zData);
      message.success(res.msg);
      userFormRef.value.resetFields();
      formState.user = {};
      zData.length = 0;
      getUserPage(pageInfo.value.current, pageInfo.value.pageSize);
    } else {
      message.error(res.msg);
      confirmLoading.value = false;
    }
  }).catch(info => {
    //表单验证失败
    console.log(info);
    confirmLoading.value = false;
    message.error("请检查，填写必选项！");
  });
};


//执行增删改查方法 请求修改删除方法（单个）
const executeMethod = async (userinfo) => {
  let res = null;
  if (modelTitle.value == '修改用户') {
    const { data } = await User.updateUserOne(userinfo);
    res = data;
  } else {
    const { data } = await User.addUserOne(userinfo);
    res = data;
  }
  return res;
}


//添加用户
const addUser = () => {
  modelTitle.value = '添加用户';
  visible.value = true;
}

//更新用户
const updateUserById = async (index, userInfo) => {
  modelTitle.value = '修改用户';
  visible.value = true;
  const { data } = await Role.getUserRolesById(userInfo.id);
  if (data.code !== 200) {
    message.error("对应用户角色信息加载失败！");
    visible.value = false;
  }
  userInfo = shallowReactive(userInfo);
  userInfo.status = Number(userInfo.status);
  formState.user = userInfo;
  let tempRules = [];
  data.data.forEach(element => {
    tempRules.push(element.name);
  });
  formState.user.roles = tempRules;
}


//删除一条数据
const deleteDataById = async (index, id) => {
  const { data } = await User.deleteUserOneById(id);
  if (data.code === 200) {
    zData.splice(index, 1);
    message.success(data.msg);
  } else {
    message.success(data.msg);
  }
}

//批量删除用户
const batchDeleteUser = async (selectKeys) => {
  if (selectKeys.length > 0) {
    const { data } = await User.betchDeleteUser(selectKeys);
    if (data.code === 200) {
      message.success(data.msg);
      zData.length = 0;
      if (isSearch.value) {
        getSearchUserPageInfo(pageInfo.value.current, pageInfo.value.pageSize,
          searchValues.nickName, searchValues.status, searchValues.sex, searchValues.email, searchValues.phonenumber, searchValues.beginTime, searchValues.lastTime);
      } else {
        getUserPage(pageInfo.value.current, pageInfo.value.pageSize);
      }
    } else {
      message.error(data.msg);
    }
  } else {
    message.error("没有选择任何项！");
  }
}



//-搜索
const reloadColumns = computed(() => {
  return columns.filter(e => {
    if (e.key != 'avatar' && e.key != 'operation' && e.key != 'id' && e.key != 'userName') {
      return true;
    }
  });
})

const formSearchState = computed(() => {
  const temp = {};
  columns.forEach(e => {
    if (e.key != 'avatar' && e.key != 'operation' && e.key != 'id' && e.key != 'userName') {
      temp[e.key] = '';
    }
  });
  return temp;
});
const isSearch = ref(false);



//搜索请求服务端
const searchValues = reactive({
  nickName: "",
  sex: "",
  status: "",
  email: "",
  phonenumber: "",
  beginTime: "",
  lastTime: ""
})

const onSearchFinish = async (values) => {
  if (values.reset) {
    getUserPage();

  } else {
    let beginTime = '';
    let lastTime = '';
    if (values.createTime) {
      beginTime = dayjs(new Date(values.createTime[0].$d)).format('YYYY-MM-DD HH:mm:ss');
      lastTime = dayjs(new Date(values.createTime[1].$d)).format('YYYY-MM-DD HH:mm:ss');
    }
    if (values.nickName == '' && values.status == '' && values.sex == '' && values.email == '' && values.phonenumber == '' && beginTime == '' && lastTime == '') {
      message.error("请至少填写一项内容！");
      return;
    }
    isSearch.value = true;
    searchValues.nickName = values.nickName;
    searchValues.status = values.status;
    searchValues.sex = values.sex;
    searchValues.email = values.email;
    searchValues.phonenumber = values.nickName;
    searchValues.beginTime = values.beginTime;
    searchValues.lastTime = values.lastTime;
    getSearchUserPageInfo(1, 5,
      values.nickName, values.sex, values.status, values.email, values.phonenumber, beginTime, lastTime);
  }

}

const getSearchUserPageInfo = async (current = 1, pageSize = 5, ...values) => {

  tableLoading.value = true;
  const { data } = await User.getSearchUserPage(1, 5, ...values);
  if (data.code === 200) {
    let res = data.data;
    pageInfo.value = { ...res }
    zData.length = 0;
    zData.push(...res.records);
    tableLoading.value = false;
  } else {
    tableLoading.value = false;
    message.success("获取模糊查询分页数据失败！");
  }

}

//处理分页数据变化
const handlePageInfoUpdate = (page) => {
  if (isSearch.value) {
    getSearchUserPageInfo(page.current, page.pageSize,
      searchValues.nickName, searchValues.sex, searchValues.status, searchValues.email, searchValues.phonenumber, searchValues.beginTime, searchValues.lastTime);
  } else {
    getUserPage(page.current, page.pageSize);
  }
}
onMounted(() => {
  getRolesOptions();
  getUserPage();
});

</script>
<style scoped lang="less">
.show-img {
  position: relative;
  width: 120px;
  height: 120px;
  background-color: #ccc;
  border-radius: 4px;

  img {
    width: 100%;
    height: 100%;
  }

  &-delete {
    position: absolute;
    top: -10px;
    right: -20px;
    z-index: 99;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 50%;

    :hover {
      opacity: 0.8;
    }
  }
}
</style>
