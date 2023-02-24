<template>
  <div class="main">
    <div class="main-header">
      概览
    </div>
    <div class="main-container">
      <div class="main-container-content">
        欢迎使用{{ setting.title }}
      </div>
    </div>
  </div>
</template>
<script setup>
import setting from "@/utils/setting";

</script>
<style lang="less" scoped>
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
      padding: 20px 10px;
      overflow: hidden;
      background-color: #fff;
      box-shadow: 0 2px 4px rgb(0 0 0 / 12%), 0 0 6px rgb(0 0 0 / 4%)
    }
  }
}
</style>

