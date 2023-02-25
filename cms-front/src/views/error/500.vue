<template>
  <a-result status="500" title="500" sub-title="抱歉, 服务器发生了错误." :style="width">
    <template #extra>
      <a-button type="primary" @click="() => router.back()">返回</a-button>
    </template>
  </a-result>
</template>
<script setup>
import { useRouter } from 'vue-router'

const router = useRouter();
const width = {
  width: window.innerWidth + 'px'
};
</script>
