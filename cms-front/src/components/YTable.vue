<template>
  <div class="main">
    <div class="main-header">
      {{ title }}
    </div>
    <div class="main-container">
      <div class="main-container-content">
        <div class="main-container-content-serarch">
          <a-form ref="formSearchRef" name="advanced_search" class="ant-advanced-search-form" :model="formSearchState"
            @finish="onSearchFinish" style="margin-top: 10px;">
            <a-row :gutter="24">
              <template v-for="(column, index) in state.reloadColumns">
                <a-col v-show="expand || index <= 2" :span="8">
                  <a-form-item :name="column.key" :label="column.title" :rules="[{ message: `请输入${column.title}` }]"
                    v-if="column.type == 'time'">
                    <a-space direction="vertical" :size="12">
                      <a-range-picker v-model:value="state.formSearchState[column.key]" format="YYYY/MM/DD" />
                    </a-space>
                  </a-form-item>
                  <a-form-item :name="column.key" :label="column.title" :rules="[{ message: `请输入${column.title}` }]"
                    v-else-if="column.key != 'status' && column.key != 'sex' && column.key != 'visible'">
                    <a-input v-model:value="state.formSearchState[column.key]" :placeholder="`请输入${column.title}`"
                      :type="column.type ? column.type : 'text'"></a-input>
                  </a-form-item>
                  <a-form-item :name="column.key" :label="column.title" :rules="[{ message: `请输入${column.title}` }]"
                    v-else-if="column.key == 'status'">
                    <a-radio-group v-model:value="state.formSearchState[column.key]">
                      <a-radio value="0">正常</a-radio>
                      <a-radio value="1">停用</a-radio>
                    </a-radio-group>
                  </a-form-item>
                  <a-form-item :name="column.key" :label="column.title" :rules="[{ message: `请输入${column.title}` }]"
                    v-else-if="column.key == 'visible'">
                    <a-radio-group v-model:value="state.formSearchState[column.key]">
                      <a-radio value="1">显示</a-radio>
                      <a-radio value="0">隐藏</a-radio>
                    </a-radio-group>
                  </a-form-item>
                  <a-form-item :name="column.key" :label="column.title" :rules="[{ message: `请输入${column.title}` }]"
                    v-else>
                    <a-select ref="select" v-model:value="state.formSearchState[column.key]" style="width: 120px">
                      <a-select-option value="男">男</a-select-option>
                      <a-select-option value="女">女</a-select-option>
                      <a-select-option value="未知">未知</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
              </template>
            </a-row>
            <a-row>
              <a-col :span="24" style="text-align: right">
                <a-button type="primary" html-type="submit">搜索</a-button>
                <a-button style="margin: 0 8px" @click="resetFrom">重置</a-button>
                <a style="font-size: 12px" @click="expand = !expand">
                  <template v-if="expand">
                    <UpOutlined />
                  </template>
                  <template v-else>
                    <DownOutlined />
                  </template>
                  折叠
                </a>
              </a-col>
            </a-row>
          </a-form>
        </div>
        <div class="main-container-content-btn">
          <a-button type="primary" @click="handleAdd" ghost style="margin-right: 10px;">
            <template #icon>
              <plus-outlined />
            </template>
            添加
          </a-button>
          <a-popconfirm title="确认删除吗？" cancelText="取消" okText="确认" @confirm="handleDelete"
            :disabled="rowKeys.length <= 0">
            <template #icon><question-circle-outlined style="color: red" /></template>
            <a-button type="primary" danger ghost :disabled="rowKeys.length <= 0">
              <template #icon>
                <delete-outlined />
              </template>
              删除
            </a-button>
          </a-popconfirm>
        </div>
        <a-table sticky :columns="columns" :data-source="data" :scroll="{
          x: 1500, y: 210
        }" :rowSelection="rowSelection
" :rowKey="(record) => record.id" :pagination="pagination" @change="handleTableChange" :loading="tableLoading">
          <template #bodyCell="{ text, record, index, column }">
            <!-- 当前单元格的值：{{ text }} ~ 当前行数据：{{ record }} ~ 当前行的索引：{{index}} ~ 当前列相关属性：{{column}} -->
            <template v-if="column.key === 'operation'">
              <span class="btns">
                <a-button type="primary" size="small" @click="handleUpdate(index, record)">编辑</a-button>
                <a-popconfirm title="确认删除吗？" cancelText="取消" okText="确认" @confirm="deleteDataOnebyId(index, record)">
                  <template #icon><question-circle-outlined style="color: red" /></template>
                  <a-button type="primary" size="small" danger>删除</a-button>
                </a-popconfirm>
              </span>
            </template>
            <template v-if="column.type === 'pic'">
              <a-image class="img" :src="record.avatar" />
            </template>
            <template v-if="column.key === 'status'">
              <a-tag color="success" v-if="record.status == 0">正常</a-tag>
              <a-tag color="error" v-else>停用</a-tag>
            </template>
            <template v-if="column.key === 'visible'">
              <a-tag color="success" v-if="record.visible == 1">显示</a-tag>
              <a-tag color="error" v-else>隐藏</a-tag>
            </template>
            <template v-if="column.key === 'icon'">
              <component :is="$antIcons[record.icon]" />
            </template>
          </template>
        </a-table>
      </div>
    </div>
  </div>
</template>
<script setup>
import { reactive, ref } from 'vue';
import { DownOutlined, UpOutlined } from '@ant-design/icons-vue';
const state = defineProps({
  columns: Array,
  data: Array,
  title: String,
  pagination: Object,
  reloadColumns: Object,
  formSearchState: Object,
  tableLoading: Boolean
});

const emits = defineEmits(['deleteDatabyId', 'handleAdd', 'handleDelete', 'handleUpdate', 'handlePageInfoUpdate', 'onSearchFinish']);
const rowKeys = reactive([]);
const rowSelection = ref({
  checkStrictly: false,
  onChange: (selectedRowKeys, selectedRows) => {
    rowKeys.length = 0;
    rowKeys.push(...selectedRowKeys);
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
  },
  onSelect: (record, selected, selectedRows) => {
    console.log(record, selected, selectedRows);
  },
  onSelectAll: (selected, selectedRows, changeRows) => {
    console.log(selected, selectedRows, changeRows);
  },
});

//表格改变后触发
const handleTableChange = (page) => {
  // this.ipagination = pagination;
  emits("handlePageInfoUpdate", page);
  //后端请求方法
}

//删除单条数据
const deleteDataOnebyId = (index, record) => {
  emits("deleteDataById", index, record.id);
}

//添加
const handleAdd = () => {
  emits("handleAdd");
}
//批量删除
const handleDelete = () => {
  emits("handleDelete", rowKeys);
}
//编辑
const handleUpdate = (index, record) => {
  emits("handleUpdate", index, record);
}

// 表单搜索
const expand = ref(false);
const formSearchRef = ref();

const onSearchFinish = values => {
  emits("onSearchFinish", values);
};
const resetFrom = () => {
  formSearchRef.value.resetFields();
  emits("onSearchFinish", { reset: true });
}

</script>
<style lang="less" scoped>
#components-table-demo-summary tfoot th,
#components-table-demo-summary tfoot td {
  background: #fafafa;
}

[data-theme='dark'] #components-table-demo-summary tfoot th,
[data-theme='dark'] #components-table-demo-summary tfoot td {
  background: #1d1d1d;
}

.btns {
  display: flex;
  justify-content: space-evenly;
}

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
      height: 100%;
      padding: 10px;
      overflow: hidden;
      background-color: #fff;
      box-shadow: 0 2px 4px rgb(0 0 0 / 12%),
        0 0 6px rgb(0 0 0 / 4%);

      &-btn {
        width: 100%;
        margin: 10px;
      }
    }
  }
}
</style>
