<template>
  <YTable :columns="columns" :data="zData" :pagination="pagination" title="角色管理" @deleteDataById="deleteDataById"
    @handleUpdate="updateRoleById" @handleAdd="addRole" @handleDelete="batchDeleteRole"
    @handlePageInfoUpdate="handlePageInfoUpdate" @onSearchFinish="onSearchFinish" :reloadColumns="reloadColumns"
    :formSearchState="formSearchState" :tableLoading="tableLoading" />
  <a-modal ok-text=" 确认" cancel-text="取消" v-model:visible="visible" :title="modelTitle" :confirm-loading="confirmLoading"
    @ok="handleOk" @cancel="overCancel">
    <a-form ref="roleFormRef" :model="formState" v-bind="layout" name="role-from" @finish="onFinish">
      <a-form-item v-show="false" :name="['role', 'id']">
        <a-input v-model:value="formState.role.id">
        </a-input>
      </a-form-item>
      <a-form-item :name="['role', 'name']" label="角色名称"
        :rules="[{ required: true, message: '请输入中文！', pattern: /^[\u4e00-\u9fa5]{0,}$/ }]">
        <a-input v-model:value="formState.role.name" placeholder="用户名">
        </a-input>
      </a-form-item>
      <a-form-item :name="['role', 'roleKey']" label="权限字符"
        :rules="[{ required: true, message: '请输入以小写字母开头，允许包含下划线的编码，至少4位！', pattern: /^[a-z][a-z_]{3,}$/ }]">
        <a-input v-model:value="formState.role.roleKey" />
      </a-form-item>
      <a-form-item :name="['role', 'status']" label="状态">
        <a-radio-group v-model:value="formState.role.status">
          <a-radio :value="0">正常</a-radio>
          <a-radio :value="1">停用</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item :name="['role', 'remark']" label="备注">
        <a-textarea v-model:value="formState.role.remark" placeholder="请输入备注内容" allow-clear show-count :maxlength="200" />
      </a-form-item>
      <a-form-item placeholder="没有菜单数据" :name="['role', 'menuRules']" label="菜单权限">
        <a-tree-select v-model:value="chanceMenus" style="width: 100%"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }" placeholder="请选择角色能管理的菜单" allow-clear multiple
          :show-checked-strategy="showCheckedStrategy" tree-checkable tree-default-expand-all :tree-data="treeData"
          :fieldNames="menuFieldNames">
          <template #tagRender="{ label, closable, onClose, option }">
            <a-tag :closable="closable" :color="option.color" style="margin-right: 3px" @close="onClose">
              {{ label }}&nbsp;&nbsp;
            </a-tag>
          </template>
        </a-tree-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import YTable from "@/components/YTable.vue";
import { reactive, onMounted, ref, shallowReactive, computed, watch } from "vue";
import Role from "@/api/role";
import Menu from "@/api/menu";
import { message, TreeSelect } from 'ant-design-vue';

//表格
const columns = [{
  title: '角色编号',
  width: 100,
  dataIndex: 'id',
  key: 'id',
  fixed: 'left',
},
{
  title: '角色名称',
  width: 100,
  dataIndex: 'name',
  key: 'name',
  fixed: 'left',
}, {
  title: '权限字符',
  dataIndex: 'roleKey',
  key: 'roleKey',
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
const getRolePage = async (current = 1, pageSize = 5) => {
  tableLoading.value = true;
  const { data } = await Role.getRolePage(current, pageSize);
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
  role: {
    id: '',
    name: '',
    roleKey: '',
    status: 0,
    remark: '',
    menuRules: {}
  },
});


//-----------------------------------
//表单完成后函数
const onFinish = values => {
  console.log('Success:', values);
};

const roleFormRef = ref();
//提交表单
const handleOk = () => {
  confirmLoading.value = true;
  roleFormRef.value.validateFields().then(async values => {
    //表单验证通过
    const res = await executeMethod(values.role);
    console.log(res);
    if (res.code === 200) {
      confirmLoading.value = false;
      visible.value = false;
      // triggerRef(zData);
      message.success(res.msg);
      roleFormRef.value.resetFields();
      formState.role = {};
      zData.length = 0;
      getRolePage(pageInfo.value.current, pageInfo.value.pageSize);
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

const showCheckedStrategy = TreeSelect.SHOW_ALL;
const chanceMenus = ref([]);
const treeData = reactive([]);
const menuFieldNames = {
  children: 'children',
  label: 'name',
  value: 'id',
}

const getCheckMenuTree = async (roleId) => {
  const { data } = await Menu.getCheckedMenuTree(roleId);
  if (data.code === 200) {
    chanceMenus.value.length = 0;
    chanceMenus.value.push(...data.data);
  } else {
    message.error("获取角色已选菜单树失败");
  }
}


const getMenuTree = async () => {
  const { data } = await Menu.getMenuTree();
  if (data.code === 200) {
    treeData.length = 0;
    treeData.push(...data.data);
  } else {
    message.error("获取菜单树失败");
  }
}

watch(chanceMenus, () => {
  console.log('select', chanceMenus.value);
});

watch(visible, async (newValue, oldValue) => {
  if (newValue == true) {
    await getMenuTree();
  }
})

//退出模态框，清空信息
const overCancel = async () => {
  formState.role = {};
  chanceMenus.value.length = 0;
}


//执行增删改查方法 请求修改删除方法（单个）
const executeMethod = async (roleinfo) => {
  let res = null;
  if (modelTitle.value == '修改角色') {
    const { data } = await Role.updateRoleOne(roleinfo, chanceMenus.value);
    res = data;
  } else {
    const { data } = await Role.addRoleOne(roleinfo);
    res = data;
  }
  return res;
}


//添加角色
const addRole = () => {
  modelTitle.value = '添加角色';
  visible.value = true;
}

//更新角色
const updateRoleById = async (index, roleInfo) => {
  modelTitle.value = '修改角色';
  visible.value = true;
  await getCheckMenuTree(roleInfo.id);
  roleInfo = shallowReactive(roleInfo);
  roleInfo.status = Number(roleInfo.status);
  formState.role = roleInfo;
}


//删除一条数据
const deleteDataById = async (index, id) => {
  const { data } = await Role.deleteRoleByRoleId(id);
  if (data.code === 200) {
    zData.splice(index, 1);
    message.success(data.msg);
  } else {
    message.success(data.msg);
  }
}

//批量删除角色
const batchDeleteRole = async (selectKeys) => {
  if (selectKeys.length > 0) {
    const { data } = await Role.betchDeleteRole(selectKeys);
    if (data.code === 200) {
      message.success(data.msg);
      zData.length = 0;
      getRolePage(pageInfo.value.current, pageInfo.value.pageSize);
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
    if (e.key != 'operation' && e.key != 'id') {
      return true;
    }
  });
})

const formSearchState = computed(() => {
  const temp = {};
  columns.forEach(e => {
    if (e.key != 'operation' && e.key != 'id') {
      temp[e.key] = '';
    }
  });
  return temp;
});

const isSearch = ref(false);




//搜索请求服务端
const searchValues = reactive({
  name: "",
  roleKey: "",
  status: "",
  beginTime: "",
  lastTime: ""
})

const onSearchFinish = async (values) => {
  if (values.reset) {
    getRolePage();
  } else {
    let beginTime = '';
    let lastTime = '';
    if (values.createTime) {
      beginTime = dayjs(new Date(values.createTime[0].$d)).format('YYYY-MM-DD HH:mm:ss');
      lastTime = dayjs(new Date(values.createTime[1].$d)).format('YYYY-MM-DD HH:mm:ss');
    }
    console.log(values);
    if (values.name == '' && values.status == '' && values.roleKey == '' && beginTime == '' && lastTime == '') {
      message.error("请至少填写一项内容！");
      return;
    }
    isSearch.value = true;
    searchValues.name = values.name;
    searchValues.status = values.status;
    searchValues.roleKey = values.roleKey;
    searchValues.beginTime = values.beginTime;
    searchValues.lastTime = values.lastTime;
    getSearchRolePageInfo(1, 5,
      values.name, values.roleKey, values.status, beginTime, lastTime);
  }

}

const getSearchRolePageInfo = async (current = 1, pageSize = 5, ...values) => {

  tableLoading.value = true;
  const { data } = await Role.getSearchRolePage(1, 5, ...values);
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
    getSearchRolePageInfo(page.current, page.pageSize,
      searchValues.name, searchValues.roleKey, searchValues.status, searchValues.beginTime, searchValues.lastTime);
  } else {
    getRolePage(page.current, page.pageSize);
  }
}

onMounted(() => {
  getRolePage();
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
