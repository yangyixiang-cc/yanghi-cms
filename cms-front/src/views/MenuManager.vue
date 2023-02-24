<template>
  <YTable :columns="columns" :data="zData" :pagination="pagination" title="菜单管理" @deleteDataById="deleteDataById"
    @handleUpdate="updateMenuById" @handleAdd="addMenu" @handleDelete="batchDeleteMenu"
    @handlePageInfoUpdate="handlePageInfoUpdate" @onSearchFinish="onSearchFinish" :reloadColumns="reloadColumns"
    :formSearchState="formSearchState" :tableLoading="tableLoading" />
  <a-modal ok-text=" 确认" cancel-text="取消" v-model:visible="visible" :title="modelTitle" :confirm-loading="confirmLoading"
    @ok="handleOk" @cancel="overCancel">
    <a-form ref="menuFormRef" :model="formState" v-bind="layout" name="Menu-from" @finish="onFinish">
      <a-form-item v-show="false" :name="['menu', 'id']">
        <a-input v-model:value="formState.menu.id">
        </a-input>
      </a-form-item>
      <a-form-item v-show="false" :name="['menu', 'pId']">
        <a-input v-model:value="formState.menu.pId">
        </a-input>
      </a-form-item>
      <a-form-item placeholder="没有菜单数据" :name="['menu', 'menuRules']" label="上级菜单" v-show="formState.menu.type != 1">
        <a-select v-model:value="chanceMenu" show-search placeholder="请选择上级菜单" style="width: 200px" :options="menuData"
          :filter-option="filterOption" @focus="handleFocus" @blur="handleBlur" @change="handleSelectChange"
          :fieldNames="menuFieldNames">
          <template #tagRender="{ value: val, label, closable, onClose, option }">
            <a-tag :closable="closable" :color="option.color" style="margin-right: 3px" @close="onClose">
              {{ option.name }}&nbsp;&nbsp;
            </a-tag>
          </template>
        </a-select>
      </a-form-item>
      <a-form-item :name="['menu', 'type']" label="菜单类型" :rules="[{ required: true, message: '请选择菜单类型' }]">
        <a-radio-group v-model:value="formState.menu.type">
          <a-radio :value="1">目录</a-radio>
          <a-radio :value="2">菜单</a-radio>
          <a-radio :value="3">按钮</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item :name="['menu', 'menuName']" label="菜单名称"
        :rules="[{ required: true, message: '请输入中文！', pattern: /^[\u4e00-\u9fa5]{0,}$/ }]">
        <a-input v-model:value="formState.menu.menuName" placeholder="请输入菜单名称">
        </a-input>
      </a-form-item>
      <a-form-item :name="['menu', 'icon']" label="菜单图标"
        :rules="[{ required: formState.menu.type == 1, message: '请选择菜单图标' }]">
        <icon-picker v-model:icon="formState.menu.icon">
          <template #iconSelect>
            <a-input v-model:value="formState.menu.icon" />
          </template>
        </icon-picker>
      </a-form-item>
      <a-form-item v-if="formState.menu.type != 3" :name="['menu', 'sort']" label="菜单排序">
        <a-input-number v-model:value="formState.menu.sort" size="large" :min="0" :max="100" />
      </a-form-item>
      <a-form-item v-if="formState.menu.type != 3" :name="['menu', 'name']" label="路由名称"
        :rules="[{ required: true, message: '请输入以小写字母开头，允许包含下划线的编码，至少4位！', }]">
        <a-input v-model:value="formState.menu.name" placeholder="请输入路由名称">
          <template #prefix>
            <a-tooltip title="访问的路由名字，如：`login`">
              <QuestionCircleOutlined />
            </a-tooltip>
          </template>
        </a-input>
      </a-form-item>
      <a-form-item v-if="formState.menu.type != 3" :name="['menu', 'path']" label="路由路径"
        :rules="[{ required: true, message: '请输入以小写字母开头，允许包含下划线的编码，至少4位！', pattern: /^[a-z][a-z_]{3,}$/ }]">
        <a-input v-model:value="formState.menu.path" placeholder="请输入路由路径">
          <template #prefix>
            <a-tooltip title="访问的路由地址，如：`user`">
              <QuestionCircleOutlined />
            </a-tooltip>
          </template>
        </a-input>
      </a-form-item>
      <a-form-item v-if="formState.menu.type == 2" :name="['menu', 'redirect']" label="重定向地址"
        :rules="[{ message: '请输入以小写字母开头，允许包含下划线的编码，至少4位！', pattern: /^[a-z][a-z_]{3,}$/ }]">
        <a-input v-model:value="formState.menu.redirect" placeholder="请输入重定向地址">
        </a-input>
      </a-form-item>
      <a-form-item v-if="formState.menu.type != 3" :name="['menu', 'component']" label="组件名称"
        :rules="[{ required: true, message: '请输入以大写字母开头，只有大小写的编码，至少4位！' }]">
        <a-input v-model:value="formState.menu.component">
          <template #prefix>
            <a-tooltip title="访问的组件路径，如：`Overview`，默认在`views`目录下">
              <QuestionCircleOutlined />
            </a-tooltip>
          </template>
        </a-input>
      </a-form-item>
      <a-form-item :name="['menu', 'perms']" label="权限字符"
        :rules="[{ message: '请输入以小写字母开头，允许包含:的编码，至少4位！', pattern: /^[a-z][a-z:;]{3,}$/ }]">
        <a-input v-model:value="formState.menu.perms">
          <template #prefix>
            <a-tooltip title="权限字符（标识后请求某些接口时具有该字符的可以访问）如system:dept:list">
              <QuestionCircleOutlined />
            </a-tooltip>
          </template>
        </a-input>
      </a-form-item>
      <a-form-item v-if="formState.menu.type != 3" :name="['menu', 'status']" label="状态">
        <a-radio-group v-model:value="formState.menu.status">
          <a-radio :value="0">正常</a-radio>
          <a-radio :value="1">停用</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item v-if="formState.menu.type != 3" :name="['menu', 'visible']" label="显示状态">
        <a-radio-group v-model:value="formState.menu.visible">
          <a-radio :value="1">显示</a-radio>
          <a-radio :value="0">隐藏</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item v-if="formState.menu.type != 3" :name="['menu', 'auth']" label="前端认证">
        <a-radio-group v-model:value="formState.menu.auth">
          <a-radio :value="true">认证</a-radio>
          <a-radio :value="false">暴露</a-radio>
        </a-radio-group>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import YTable from "@/components/YTable.vue";
import IconPicker from "@/components/IconPicker.vue";
import { reactive, onMounted, ref, shallowReactive, computed, watch } from "vue";
import Menu from "@/api/menu";
import { message } from 'ant-design-vue';

//表格
const columns = [{
  title: '菜单编号',
  width: 100,
  dataIndex: 'id',
  key: 'id',
  fixed: 'left',
  ellipsis: true,
},
{
  title: '菜单名称',
  width: 100,
  dataIndex: 'menuName',
  key: 'menuName',
  fixed: 'left',
}, {
  title: '图标',
  dataIndex: 'icon',
  key: 'icon',
  width: 150,
}, {
  title: '路由名称',
  dataIndex: 'name',
  key: 'name',
  width: 150
}, {
  title: '路由地址',
  dataIndex: 'path',
  key: 'path',
  width: 150
}, {
  title: '重定向地址',
  dataIndex: 'redirect',
  key: 'redirect',
  width: 150
}, {
  title: '组件名称',
  dataIndex: 'component',
  key: 'component',
  width: 150
}, {
  title: '权限标识',
  dataIndex: 'perms',
  key: 'perms',
  width: 150
},
{
  title: '状态',
  dataIndex: 'status',
  key: 'status',
  width: 150,
}, {
  title: '可见',
  dataIndex: 'visible',
  key: 'visible',
  width: 150,
}, {
  title: '排序',
  dataIndex: 'sort',
  key: 'sort',
  width: 150
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
const getMenuPage = async (current = 1, pageSize = 5) => {
  tableLoading.value = true;
  const { data } = await Menu.getMenuPage(current, pageSize);
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
  menu: {
    id: '',
    menuName: '',
    name: '',
    path: '',
    icon: '',
    redirect: '',
    component: '',
    perms: '',
    status: 0,
    visible: 1,
    sort: 0,
    auth: true,
    pid: '',
    pId: '',
    type: 1,//类型
  },
});



//-----------------------------------
//表单完成后函数
const onFinish = values => {
  console.log('Success:', values);
};

const menuFormRef = ref();
//提交表单
const handleOk = () => {
  confirmLoading.value = true;
  menuFormRef.value.validateFields().then(async values => {
    //表单验证通过
    const res = await executeMethod(values.menu);
    console.log(res);
    if (res.code === 200) {
      confirmLoading.value = false;
      visible.value = false;
      // triggerRef(zData);
      message.success(res.msg);
      menuFormRef.value.resetFields();
      formState.menu = {};
      zData.length = 0;
      getMenuPage(pageInfo.value.current, pageInfo.value.pageSize);
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

//菜单权限相关---------------------

const chanceMenu = ref('');
const menuFieldNames = {
  label: 'name',
  value: 'id',
}

const menuData = reactive([]);
const handleSelectChange = (value, option) => {
  chanceMenu.value = option.id;
  formState.menu.pid = option.id;
  formState.menu.pId = option.id;
};
const handleBlur = () => {
  menuData.length = 0;
};
const handleFocus = async () => {
  await loadMenusSelect();
};
const filterOption = (input, option) => {
  return menuData.filter(el => {
    return el.name.indexOf(input) >= 0
  })
};
const loadMenusSelect = async () => {
  if (formState.menu.type == 2) {
    let { data } = await Menu.getFatherMenus();
    if (data.code === 200) {
      menuData.length = 0;
      menuData.push(...data.data);
    } else {
      message.error("加载菜单数据失败")
    }
  } else if (formState.menu.type == 3) {
    let { data } = await Menu.getSubMenus();
    if (data.code === 200) {
      menuData.length = 0;
      menuData.push(...data.data);
    } else {
      message.error("加载菜单数据失败")
    }
  }
}


//退出模态框，清空信息
const overCancel = async () => {
  formState.menu = {};
  chanceMenu.value = '';
}

//执行增删改查方法 请求修改删除方法（单个）
const executeMethod = async (menuinfo) => {
  let res = null;
  if (modelTitle.value == '修改菜单') {
    const { data } = await Menu.updateMenuOne(menuinfo);
    res = data;
  } else {
    const { data } = await Menu.addMenuOne(menuinfo);
    res = data;
  }
  return res;
}


//添加角色
const addMenu = () => {
  modelTitle.value = '添加菜单';
  visible.value = true;
}


const setCheckedMenu = async (id) => {
  if (formState.menu.type != 1) {
    const { data } = await Menu.getFatherMenuIdByMenuId(id);
    if (data.code === 200) {
      if (data.data != null) {
        chanceMenu.value = data.data.menuName;
        formState.menu.pid = data.data.id;
        formState.menu.pId = data.data.id;
      }

    } else {
      message.error(data.msg);
    }
  }
}


//更新角色
const updateMenuById = async (index, menuInfo) => {
  modelTitle.value = '修改菜单';
  visible.value = true;
  setCheckedMenu(menuInfo.id);
  menuInfo = shallowReactive(menuInfo);
  menuInfo.status = Number(menuInfo.status);
  menuInfo.visible = Number(menuInfo.visible);
  if (JSON.stringify(menuInfo.pid) == 'null') {
    menuInfo.type = 1;
  } else if (JSON.stringify(menuInfo.pid) != 'null') {
    menuInfo.pId = menuInfo.pid;
    menuInfo.type = 2;
  } else {
    menuInfo.type = 3;
  }
  formState.menu = menuInfo;
}


//删除一条数据
const deleteDataById = async (index, id) => {
  const { data } = await Menu.deleteMenuByMenuId(id);
  if (data.code === 200) {
    zData.splice(index, 1);
    message.success(data.msg);
  } else {
    message.success(data.msg);
  }
}

//批量删除角色
const batchDeleteMenu = async (selectKeys) => {
  if (selectKeys.length > 0) {
    const { data } = await Menu.betchDeleteMenu(selectKeys);
    if (data.code === 200) {
      message.success(data.msg);
      zData.length = 0;
      getMenuPage(pageInfo.value.current, pageInfo.value.pageSize);
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
    if (e.key != 'operation' && e.key != 'icon' && e.key != 'sort' && e.key != 'id') {
      return true;
    }
  });
})

const formSearchState = computed(() => {
  const temp = {};
  columns.forEach(e => {
    if (e.key != 'operation' && e.key != 'icon' && e.key != 'sort' && e.key != 'id') {
      temp[e.key] = '';
    }
  });
  return temp;
});

const isSearch = ref(false);

//搜索请求服务端
const searchValues = reactive({
  menuName: "",
  name: "",
  path: "",
  redirect: "",
  component: "",
  perms: "",
  status: "",
  visible: "",
  beginTime: "",
  lastTime: ""
})

const onSearchFinish = async (values) => {
  if (values.reset) {
    getMenuPage();
  } else {
    let beginTime = '';
    let lastTime = '';
    if (values.createTime) {
      beginTime = dayjs(new Date(values.createTime[0].$d)).format('YYYY-MM-DD HH:mm:ss');
      lastTime = dayjs(new Date(values.createTime[1].$d)).format('YYYY-MM-DD HH:mm:ss');
    }
    console.log(values);
    if (values.name == '' && values.component == '' && values.perms == '' && values.menuName == '' && values.path == '' && values.redirect == '' && values.status == '' && values.visible == '' && beginTime == '' && lastTime == '') {
      message.error("请至少填写一项内容！");
      return;
    }

    isSearch.value = true;
    searchValues.menuName = values.menuName;
    searchValues.name = values.name;
    searchValues.redirect = values.redirect;
    searchValues.component = values.component;
    searchValues.perms = values.perms;
    searchValues.status = values.status;
    searchValues.visible = values.visible;
    searchValues.beginTime = values.beginTime;
    searchValues.lastTime = values.lastTime;
    getSearchMenuPageInfo(1, 5,
      values.menuName,
      values.name,
      values.path,
      values.redirect,
      values.component,
      values.perms,
      values.status,
      values.visible,
      beginTime,
      lastTime);
  }

}

const getSearchMenuPageInfo = async (current = 1, pageSize = 5, ...values) => {

  tableLoading.value = true;
  const { data } = await Menu.getSearchMenuPage(1, 5, ...values);
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
    getSearchMenuPageInfo(page.current, page.pageSize,
      searchValues.name,
      searchValues.path,
      searchValues.redirect,
      searchValues.component,
      searchValues.perms,
      searchValues.status,
      searchValues.visible,
      beginTime,
      lastTime);
  } else {
    getMenuPage(page.current, page.pageSize);
  }
}

onMounted(() => {
  getMenuPage();
});

</script>
<style scoped lang="less">
.scroll-y {
  overflow-y: auto;
}

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
