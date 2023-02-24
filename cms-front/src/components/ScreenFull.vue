<template>
  <div @click="onToggle">
    <template v-if="!isFullscreen">
      <fullscreen-outlined style="font-size: 16px" />
    </template>
    <template v-else>
      <fullscreen-exit-outlined style="font-size: 16px" />
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import screenfull from "screenfull";

// 是否全屏
const isFullscreen = ref(false);

// 监听变化
const change = () => {
  isFullscreen.value = screenfull.isFullscreen;
};

// 切换事件
const onToggle = () => {
  screenfull.toggle();
};

// 设置侦听器
onMounted(() => {
  screenfull.on("change", change);
});

// 删除侦听器
onUnmounted(() => {
  screenfull.off("change", change);
});
</script>

<style lang="less" scoped>
div {
  width: 100%;
  height: 100%;
}
</style>
