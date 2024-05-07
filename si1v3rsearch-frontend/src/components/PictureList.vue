<template>
  <a-list
    item-layout="horizontal"
    :grid="{ gutter: 0, xs: 1, sm: 2, md: 3, lg: 3, xl: 4, xxl: 3 }"
    :data-source="props.pictureList"
  >
    <template #renderItem="{ item }">
      <a-list-item>
        <a-card hoverable>
          <template #cover>
            <img :src="item.imgUrl" />
          </template>
          <a-card-meta :title="item.title">
            <template #description>{{ item.title }}</template>
          </a-card-meta>
        </a-card>
      </a-list-item>
    </template>
  </a-list>
</template>
<script setup lang="ts">
import { withDefaults, defineProps } from "vue";
interface Props {
  pictureList: any[];
}
const props = withDefaults(defineProps<Props>(), {
  pictureList: () => [],
});
</script>

<style scoped></style>
