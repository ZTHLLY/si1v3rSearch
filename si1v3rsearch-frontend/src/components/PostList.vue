<template>
  <div>帖子列表</div>
  <a-list item-layout="horizontal" :data-source="props.postList">
    <template #renderItem="{ item }">
      <a-list-item>
        <a-list-item-meta>
          <template #description>
            <p
              v-html="item.content"
              style="color: black; font-weight: lighter; font-family: Cursive"
            ></p>
          </template>
          <template #title>
            <a v-html="item.title" style="font-size: 16px"></a>
          </template>
          <template #avatar>
            <a-avatar
              src="https://pic.imgdb.cn/item/657c196cc458853aefc75d25.png"
            />
          </template>
        </a-list-item-meta>
      </a-list-item>
    </template>
  </a-list>
</template>
<script setup lang="ts">
import { withDefaults, defineProps } from "vue";
interface Props {
  postList: any[];
}
const props = withDefaults(defineProps<Props>(), {
  postList: () => [],
});
</script>

<style scoped></style>
