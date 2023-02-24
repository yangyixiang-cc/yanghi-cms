<template>
  <a-config-provider :locale="locale">
    <router-view />
  </a-config-provider>
</template>
<script setup>
import { ref } from "vue";
import zhCN from 'ant-design-vue/lib/locale-provider/zh_CN';
const locale = ref(zhCN);

</script>
<style scoped lang="less"></style>
