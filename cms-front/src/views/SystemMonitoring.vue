<template>
  <div class="main">
    <div class="main-header">
      系统监控
    </div>
    <div class="main-container">
      <div class="main-container-content">
        <div class="main-container-content-aricle">

          <a-spin :spinning="spinning" tip="数据加载中">
            <div class="reset">
              <a-button type="primary" :loading="shuffle" @click="reset">刷新</a-button>
            </div>
            <a-row :gutter="[16, 16]" justify="center">
              <a-col :span="6" class="col">
                <a-progress type="circle" :percent="systemData.cupInfo.usePercent" :stroke-color="{
                  from: '#108ee9',
                  to: '#87d068',
                }" />
                CPU使用率
              </a-col>
              <a-col :span="6" class="col">
                <a-progress type="circle" :percent="systemData.memoryInfo.usePercent" :stroke-color="{
                  from: '#108ee9',
                  to: '#87d068',
                }" />
                内存使用率
              </a-col>
              <a-col :span="6" class="col">
                <a-progress type="circle" :percent="systemData.diskUsePercent" :stroke-color="{
                  '0%': '#108ee9',
                  '100%': '#87d068',
                }" />

                磁盘使用率
              </a-col>
            </a-row>

            <br />
            <br />
            <a-descriptions bordered title="JVM虚拟机信息" :size="size"
              :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
              <a-descriptions-item label="jdk版本">{{ systemData.jvmInfo.jdkVersion }}</a-descriptions-item>
              <a-descriptions-item label="jdk安装目录">{{ systemData.jvmInfo.jdkHome }}</a-descriptions-item>
              <a-descriptions-item label="jdk名称">{{ systemData.jvmInfo.jdkName }}</a-descriptions-item>
              <a-descriptions-item label="总内存">{{ systemData.jvmInfo.jvmTotalMemory }}</a-descriptions-item>
              <a-descriptions-item label="Java虚拟机将尝试使用的最大内存量">{{ systemData.jvmInfo.maxMemory }}</a-descriptions-item>
              <a-descriptions-item label="空闲内存">{{ systemData.jvmInfo.freeMemory }}</a-descriptions-item>
              <a-descriptions-item label="已使用内存">{{ systemData.jvmInfo.usedMemory }}</a-descriptions-item>
              <a-descriptions-item label="内存使用率">{{ systemData.jvmInfo.usePercent }}</a-descriptions-item>
              <a-descriptions-item label="分区大小（字符串类型，例如 1GB）">{{ systemData.jvmInfo.size }}</a-descriptions-item>
              <a-descriptions-item label="返回Java虚拟机的正常运行时间（毫秒）">{{ systemData.jvmInfo.uptime }}</a-descriptions-item>
              <a-descriptions-item label="返回Java虚拟机的启动时间（毫秒）。">{{ systemData.jvmInfo.startTime
              }}</a-descriptions-item>
            </a-descriptions>
            <br />
            <br />
            <a-descriptions bordered title="服务器信息" :size="size" :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
              <a-descriptions-item label="服务器名称">{{ systemData.sysInfo.name }}</a-descriptions-item>
              <a-descriptions-item label="IP地址">{{ systemData.sysInfo.ip }}</a-descriptions-item>
              <a-descriptions-item label="操作系统">{{ systemData.sysInfo.osName }}</a-descriptions-item>
              <a-descriptions-item label="系统架构">{{ systemData.sysInfo.osArch }}</a-descriptions-item>
              <a-descriptions-item label="项目路径">{{ systemData.sysInfo.userDir }}</a-descriptions-item>
              <a-descriptions-item label="内核">{{ systemData.cupInfo.physicalProcessorCount }}</a-descriptions-item>
              <a-descriptions-item label="逻辑处理器">{{ systemData.cupInfo.logicalProcessorCount }}</a-descriptions-item>
            </a-descriptions>
          </a-spin>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { reactive, onMounted, ref } from "vue";
import Tool from "@/api/tool";
import { message } from 'ant-design-vue';

const systemData = reactive({
  cupInfo: {},
  sysInfo: {},
  memoryInfo: {},
  diskUsePercent: {},
  diskInfos: [],
  sysInfo: {},
  jvmInfo: {}
});

const spinning = ref(false);

const getSystemInfo = async () => {
  spinning.value = true;
  const { data } = await Tool.serverMonitor();
  if (data.code == 200) {
    systemData.cupInfo = data.data.cupInfo;
    systemData.sysInfo = data.data.sysInfo;
    systemData.memoryInfo = data.data.memoryInfo;
    systemData.diskUsePercent = data.data.diskUsePercent;
    systemData.diskInfos = data.data.diskInfos;
    systemData.sysInfo = data.data.sysInfo;
    systemData.jvmInfo = data.data.jvmInfo;
    spinning.value = false;
  } else {
    message.error("获取系统监控信息失败");
    spinning.value = false;
  }
}

const shuffle = ref(false);

const reset = async () => {
  shuffle.value = true;
  await getSystemInfo();
  shuffle.value = false;
}

onMounted(async () => {
  await getSystemInfo();
})
</script>
<style lang="less" scoped>
.reset {
  display: flex;
  justify-content: end;
  width: 100%;
  padding-right: 40px;
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
    overflow-y: auto;

    &-content {
      padding: 20px 10px;
      padding-bottom: 100px;
      background-color: #fff;
      box-shadow: 0 2px 4px rgb(0 0 0 / 12%), 0 0 6px rgb(0 0 0 / 4%);
    }
  }
}

.col {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
  height: 230px;
  font-size: 18px;
}
</style>

