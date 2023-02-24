<template>
  <div>
    <a-result status="403" title="403" sub-title="抱歉, 你没有该页面的访问权限." :style="width">
      <template #extra>
        <a-button type="primary" @click="() => router.back()">返回</a-button>
      </template>
    </a-result>
  </div>
</template>
<script setup>
import { useRouter } from 'vue-router'

const router = useRouter();
const width = {
  width: window.innerWidth + 'px'
};
</script>

