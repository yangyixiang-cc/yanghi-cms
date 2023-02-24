<template>
  <a-modal :visible="visible" width="500px" :title="title" @ok="handleOk" class="model" ok-text="替换" @cancel="overCancel">
    <a-row type="flex">
      <a-col :flex="2" :offset="2">
        <img :src="url" alt="avatar" width="100" />
      </a-col>
      <a-col :flex="3" :offset="2">
        <a-upload v-model:file-list="fileList" name="avatar" list-type="picture-card" :customRequest="uploadImg"
          :before-upload="beforeUpload" @change="handleImgChange" @remove="imgDel">
          <div>
            <loading-outlined v-if="loading"></loading-outlined>
            <plus-outlined v-else></plus-outlined>
            <div class="ant-upload-text">上传</div>
          </div>
        </a-upload>
      </a-col>
    </a-row>

  </a-modal>
</template>
<script setup>
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { createVNode, ref } from 'vue';
import { message, Modal } from 'ant-design-vue';
import Tool from "@/api/tool";
defineProps({
  title: String,
  url: String,
  visible: Boolean
});
const emits = defineEmits(['overCancel', 'handleOk']);

//-----------------------------------文件上传
const fileList = ref([]);
const uploadUrl = ref('');
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
  const { data } = await Tool.deleteFile(uploadUrl.value);
  if (data.code === 200) {
    message.success("删除图片成功！");
    uploadUrl.value = "";
    fileList.value.length = 0;
  } else {
    message.error("删除图片失败！");
  }
}
// 修改文件？没用到
const handleImgChange = (fileList) => {
  if (fileList.length > 1) {
    message.error("只能上传一张图片！");
    return
  }
  fileList.value = fileList;
}

const uploadImg = async (info) => {
  if (fileList.length > 1) {
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
    uploadUrl.value = imgPath;
  } else {
    info.onError();
    message.error(data.msg);
  }
}

//退出模态框，清空信息
const overCancel = () => {
  if (uploadUrl.value != '') {
    imgDel();
  }
  emits('overCancel');
}
const handleOk = () => {
  if (uploadUrl.value == '') {
    message.error("你没有上传图片！");
    return;
  }
  Modal.confirm({
    title: '你确认要更换头像吗?',
    icon: createVNode(ExclamationCircleOutlined),
    onOk() {
      return new Promise((resolve, reject) => {
        emits('handleOk', uploadUrl.value);
        resolve();
      }).catch(() => console.log('更换头像失败！'));
    },
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    onCancel() {
      if (uploadUrl.value != '') {
        imgDel();
      }
      emits('overCancel');
    },
  });
};


</script>
<style lang="less" scoped>
.model {
  display: flex;
  flex-direction: row;
  justify-content: space-between;

  img {
    display: flex;
  }

}
</style>

