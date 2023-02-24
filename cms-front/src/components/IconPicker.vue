<template>
  <a-popover :trigger="trigger" v-model:visible="visible">
    <template #title>
      <a-input-search v-model:value="searchValue" placeholder="输入英文关键词进行搜索" @change="filterIcon" />
    </template>
    <template #content>
      <div class="icon-box">
        <div v-for="(item, index) in iconArr" :key="index" @click="handleClick(item)" class="icon-content"
          :style="{ background: icon === item ? '#268961' : '' }">
          <component :is="$antIcons[item]" />
        </div>
      </div>
    </template>
    <slot name="iconSelect"></slot>
  </a-popover>
</template>

<script>
import { defineComponent, ref, watch } from "vue";
import icons from '../utils/icons';
export default defineComponent({
  name: "IconPicker",
  props: {
    icon: {
      type: String,
      required: true
    },
    //自定义触发方式
    trigger: {
      type: String,
      default: 'click',
      validator: function (value) {
        return ['click', 'hover', 'focus'].indexOf(value) !== -1
      }
    }
  },
  setup(props, context) {
    const iconArr = ref(icons)
    const visible = ref(false)
    const searchValue = ref('')

    const handleClick = (icon) => {
      context.emit('update:icon', icon)
      visible.value = false;
    }

    /**
     * 进行搜索过滤
     */
    const filterIcon = () => {
      if (searchValue.value) {
        iconArr.value = icons.filter(item => item.toLowerCase().includes(searchValue.value.toLowerCase()))
      } else {
        iconArr.value = icons;
      }
    }

    watch(visible, () => {
      searchValue.value = ''
      iconArr.value = icons;
    })

    return {
      visible,
      iconArr,
      handleClick,
      searchValue,
      filterIcon
    }
  }
})
</script>

<style lang="less" scoped>
.icon-box {
  display: flex;
  flex-direction: row;
  /* stylelint-disable-next-line declaration-block-no-redundant-longhand-properties */
  flex-wrap: wrap;
  align-content: flex-start;
  justify-content: center;
  width: 250px;
  height: 230px;
  overflow: auto;
  font-size: 20px;
}

.icon-content {
  width: 45px;
  height: 40px;
  margin: 5px;
  text-align: center;
  cursor: pointer;
  border: 1px solid #ccc;
  border-radius: 6px
}

.icon-content:hover {
  background: #1890ff;
}
</style>
