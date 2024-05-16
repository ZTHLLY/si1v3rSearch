<template>
  <div class="index-page">
    <a-input-search
      v-model:value="searchParams.text"
      placeholder="input search text"
      enter-button="Search"
      size="large"
      @search="onSearch"
    />
    <!-- {{ JSON.stringify(searchParams) }}
    {{ JSON.stringify(userList) }} -->
    <Mydivider />
    <a-tabs v-model:activeKey="activeKey" @change="handleTabChange">
      <a-tab-pane key="post" tab="文章">
        <PostList :post-list="postList" />
      </a-tab-pane>
      <a-tab-pane key="picture" tab="图片">
        <PictureList :picture-list="pictureList" />
      </a-tab-pane>
      <a-tab-pane key="user" tab="用户">
        <UserList :user-list="userList" />
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
import myAxios from "../plugins/myAxios";

const postList = ref([]);
const userList = ref([]);
const pictureList = ref([]);

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

const loadDataOld = (params: any) => {
  const postParams = {
    ...params,
    searchText: params.text,
  };
  myAxios.post("/post/list/page/vo", postParams).then((res: any) => {
    console.log(res);
    postList.value = res.records;
  });
  const userParams = {
    ...params,
    userName: params.text,
  };
  myAxios.post("/user/list/page/vo", userParams).then((res: any) => {
    console.log(res);
    userList.value = res.records;
  });
  myAxios.post("/picture/list/page/vo", postParams).then((res: any) => {
    console.log("picture接口返回的数据=>", res);
    pictureList.value = res.records;
  });
};
const loadData = (params: any) => {
  const postParams = {
    ...params,
    searchText: params.text,
  };
  myAxios.post("/search/all", postParams).then((res: any) => {
    console.log(res);
    postList.value = res.postVOList;
    userList.value = res.userVOList;
    pictureList.value = res.pictureList;
  });
};

loadData(initSearchParams);

const onSearch = (value: string) => {
  //alert(value);
  console.log(value);
  router.push({
    query: searchParams.value,
  });
  loadData(searchParams.value);
};

const handleTabChange = (key: string) => {
  router.push({
    path: `/${key}`,
    query: searchParams.value.text ? searchParams.value : undefined,
  });
};
</script>
