<template>
  <a-layout :style="screen">
    <a-layout-header class="reset">
      <YHeader />
    </a-layout-header>
    <a-layout>
      <a-layout-sider class="reset" width="240" collapsible theme="light" v-model:collapsed="collapsed">
        <YSider />
      </a-layout-sider>
      <a-layout>
        <a-layout-content>
          <YContent />
        </a-layout-content>
        <a-layout-footer>
          <YFooter />
        </a-layout-footer>
      </a-layout>
    </a-layout>
  </a-layout>
</template>
<script setup>
import { ref, onMounted, watch, computed } from 'vue';
import YHeader from "./moudle/YHeader.vue";
import YSider from "./moudle/YSider.vue";
import YContent from "./moudle/YContent.vue";
import YFooter from "./moudle/YFooter.vue";

const screenWidth = ref(window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth);

onMounted(() => {
  window.onresize = () => {
    return (() => {
      screenWidth.value = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth
    })()
  }
})
watch(screenWidth, (newX) => {
  if (newX < 1024) {
    screenWidth.value = 1024;
  }
  screenWidth.value = newX;
})
const screen = computed(() => {
  return {
    width: screenWidth.value + "px",
    height: "100vh"
  }
});
const collapsed = ref(false);
</script>
<style scoped lang="less">
.reset {
  padding: 0 !important;
  margin: 0 !important;
  background-color: #f0f2f5;
}
</style>
