package com.yupi.springbootinit;


import cn.hutool.http.HttpRequest;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class crawlerTest {

    @Resource
    private PostService postService;
    @Test
    void crawlImg() throws IOException {

        String url = "https://www.bing.com/images/search?q=星穹铁道黄泉&first=1";
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc);
        Elements elements = doc.select(".iuscp.isv");
        for (Element element : elements) {
            //图片地址murl
            String m = element.select(".iusc").get(0).attr("m");
            Map<String,String> map=JSONUtil.toBean(m,Map.class);
            String murl= map.get("murl");
            //图片标题
            String title=element.select(".inflnk").get(0).attr("aria-label");
            System.out.println(title);
            //System.out.println(murl);
        }
    }

    @Test
     void httpTest(){
        //1.获取数据
        String json ="{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"tags\":[],\"reviewStatus\":1}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest.post(url)
                .body(json)
                .execute()
                .body();
        //System.out.println(result);
        //2.处理数据，首先转成对象
        Map<String, Object> map= JSONUtil.toBean(result, Map.class);
        //System.out.println(map);
        JSONObject data =  (JSONObject) map.get("data");
        //System.out.println(data);
        JSONArray records = (JSONArray) data.get("records");
        //System.out.println(records);

        List<Post> postList=new ArrayList<>();
        for ( Object record :records){
            JSONObject tempRecord=(JSONObject) record;
            Post post = new Post();

            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            System.out.println(tags);
            List<String> tagLists=tags.toList(String.class);
            System.out.println(tagLists);
            String t = JSONUtil.toJsonStr(tagLists);
            //System.out.println(t);
            post.setTags(t);
            post.setUserId(1782712950386180098L);

            postList.add(post);

        }
        //System.out.println(postList);
        //3.数据存入数据库
        boolean ans = postService.saveBatch(postList);
        Assertions.assertTrue(ans);
    }
}
