<template>
  <div class="index-page">
    <a-input-search
      v-model:value="searchParams.text"
      placeholder="input search text"
      enter-button="Search"
      size="large"
      @search="onSearch"
    />
    {{ JSON.stringify(searchParams) }}
    <Mydivider />
    <a-tabs v-model:activeKey="activeKey" @change="handleTabChange">
      <a-tab-pane key="post" tab="文章">
        <PostList />
      </a-tab-pane>
      <a-tab-pane key="picture" tab="图片">
        <PictureList />
      </a-tab-pane>
      <a-tab-pane key="user" tab="用户">
        <UserList />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import PostList from "../components/PostList.vue";
import PictureList from "../components/PictureList.vue";
import UserList from "../components/UserList.vue";
import Mydivider from "../components/MyDivider.vue";
import router from "@/router";
import { useRoute } from "vue-router";

const route = useRoute();
const activeKey = route.params.category;
const initSearchParams = {
  text: "",
  pageSize: 10,
  pageNum: 1,
};
const searchParams = ref(initSearchParams);

watchEffect(() => {
  searchParams.value = {
    ...initSearchParams,
    text: route.query.text,
  } as any;
});

const onSearch = (value: string) => {
  alert(value);
  router.push({
    query: searchParams.value,
  });
};

const handleTabChange = (key: string) => {
  router.push({
    path: `/${key}`,
    query: searchParams.value.text ? searchParams.value : undefined,
  });
};
</script>
