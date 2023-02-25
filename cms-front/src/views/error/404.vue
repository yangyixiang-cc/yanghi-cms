<template>
  <a-result status="404" title="404" sub-title="抱歉, 你访问的页面不存在" :style="width">
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
