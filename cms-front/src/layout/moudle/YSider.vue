<template>
  <aside class="aside">
    <a-menu id="dddddd" v-model:openKeys="openKeys" v-model:selectedKeys="selectedKeys" style="width: 100%" mode="inline"
      @click="handleClick">
      <template v-for="menu in menuList" :key="index">
        <a-sub-menu :key="menu.name" v-if="menu.isSub && menu.visible">
          <template #icon v-if="menu.icon != '#' && menu.visible">
            <component :is="$antIcons[menu.icon]" />
          </template>
          <template #title>{{ menu.title }}</template>
          <a-menu-item :key="sub.name" v-for="sub in menu
            .children">
            <template #icon v-if="sub.icon != '#'">
              <component :is="$antIcons[sub.icon]" />
            </template>
            <span>{{ sub.title }}</span>
          </a-menu-item>
        </a-sub-menu>
        <a-menu-item :key="menu.name" v-if="!menu.isSub && menu.visible">
          <template #icon v-if="menu.icon != '#'">
            <component :is="$antIcons[menu.icon]" />
          </template>
          <span>{{ menu.title }}</span>
        </a-menu-item>
      </template>
    </a-menu>
  </aside>
</template>
<style scoped lang="less">
.aside {
  width: 100%;
  height: 100%;
  overflow-y: auto;
  background: #fff;
}
</style>

<script setup>
import { ref, watch, computed } from 'vue';
import { useRouter } from 'vue-router'
import useStore from '@/store/index';
const { permissions, user } = useStore();
const router = useRouter()
const selectedKeys = ref(['overview']);
const openKeys = ref(['overview']);
const handleClick = async e => {
  try {
    router.push({
      name: e.key
    });
  } catch (error) {
  }

};
const titleClick = e => {
  console.log('titleClick', e);
};



watch(() => openKeys, val => {
  console.log('openKeys', val);
});
const routerPackag = routers => {
  const temp = [];
  routers.filter(itemRouter => {
    let len = 0;
    if (itemRouter.children) {
      len = itemRouter.children.length;
    }
    // 是否存在子集
    if (len > 0) {
      let c = routerPackag(itemRouter.children);
      temp.push({
        name: itemRouter.name + itemRouter.path,
        title: itemRouter.meta.title,
        icon: itemRouter.meta.icon,
        visible: itemRouter.meta.visible,
        isSub: true,
        children: c
      })
    } else {
      temp.push({
        name: itemRouter.name,
        title: itemRouter.meta.title,
        visible: itemRouter.meta.visible,
        isSub: false,
        icon: itemRouter.meta.icon
      })
    }
    return true;
  });
  temp.sort((a, b) => a.sort - b.sort);
  return temp;
};
const menuList = computed(() => {
  const routes = permissions.addRoutes;
  return routerPackag(routes);
})
</script>
