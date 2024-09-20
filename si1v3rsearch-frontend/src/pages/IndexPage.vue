<template>
  <div class="index-page">
    <a-input-search
      v-model:value="searchText"
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
        <a-skeleton :loading="loading" active :paragraph="{ rows: 6 }">
          <PostList :post-list="postList" />
        </a-skeleton>
      </a-tab-pane>
      <a-tab-pane key="picture" tab="图片">
        <a-skeleton :loading="loading" active :paragraph="{ rows: 6 }">
          <PictureList :picture-list="pictureList" />
        </a-skeleton>
      </a-tab-pane>
      <a-tab-pane key="user" tab="用户">
        <a-skeleton :loading="loading" active :paragraph="{ rows: 6 }">
          <UserList :user-list="userList" />
        </a-skeleton>
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
const loading = ref<boolean>(true);

const route = useRoute();
const activeKey = route.params.category ? route.params.category : "post";
const initSearchParams = {
  text: "",
  pageSize: 10,
  pageNum: 1,
  type: "post",
};
const searchParams = ref(initSearchParams);
const searchText = ref(route.params.text || "");

// const loadDataOld = (params: any) => {
//   const postParams = {
//     ...params,
//     searchText: params.text,
//   };
//   myAxios.post("/post/list/page/vo", postParams).then((res: any) => {
//     console.log(res);
//     postList.value = res.records;
//   });
//   const userParams = {
//     ...params,
//     userName: params.text,
//   };
//   myAxios.post("/user/list/page/vo", userParams).then((res: any) => {
//     console.log(res);
//     userList.value = res.records;
//   });
//   myAxios.post("/picture/list/page/vo", postParams).then((res: any) => {
//     console.log("picture接口返回的数据=>", res);
//     pictureList.value = res.records;
//   });
// };

const loadData = (params: any) => {
  const postParams = {
    ...params,
    searchText: params.text,
  };
  //console.log("postParams=>", postParams);
  //alert(postParams.type);
  loading.value = true;
  myAxios.post("/search/all", postParams).then((res: any) => {
    //console.log(res);
    if (postParams.type === "user") {
      loading.value = false;
      userList.value = res.userVOList;
    } else if (postParams.type === "post") {
      loading.value = false;
      postList.value = res.postVOList;
    } else if (postParams.type === "picture") {
      loading.value = false;
      pictureList.value = res.pictureList;
    }
  });
};

watchEffect(() => {
  searchParams.value = {
    ...initSearchParams,
    text: route.query.text,
    type: route.params.category,
  } as any;
  console.log("watchEffect searchParams.value=>", searchParams.value);
  loadData(searchParams.value);
});

loadData(initSearchParams);
//点击搜索按钮
const onSearch = (value: string) => {
  //alert(value);
  console.log(value);
  router.push({
    query: {
      ...searchParams.value,
      text: value,
    },
  });
};
//切换标签栏
const handleTabChange = (key: string) => {
  router.push({
    path: `/${key}`,
    query: searchParams.value.text ? searchParams.value : undefined,
  });
};
</script>
