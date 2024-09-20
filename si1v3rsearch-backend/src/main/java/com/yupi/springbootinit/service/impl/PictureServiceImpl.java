package com.yupi.springbootinit.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.service.PictureService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Service
public class PictureServiceImpl implements PictureService {
    //判断图片是否正常
    public boolean isValidImageUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");  // 使用 HEAD 请求
            connection.setConnectTimeout(1000);   // 设置超时时间
            connection.setReadTimeout(1000);
            int responseCode = connection.getResponseCode();
            return (responseCode == HttpURLConnection.HTTP_OK);  // 如果状态码是200，说明链接有效
        } catch (Exception e) {
            return false;  // 如果出现异常，认为URL无效
        }
    }


    @Override
    public Page<Picture> PictureSearch(String searchText, long pageNum, long pageSize) {
        List<Picture> pictureList = new ArrayList<>();

        long current=pageNum*pageSize;

        String url = String.format("https://www.bing.com/images/search?q=%s&first=%s",searchText,current);
        Document doc = null;
        try {

            doc = Jsoup.connect(url).header("Referer","https://www.bing.com").get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        System.out.println(doc);
        Elements elements = doc.select(".iuscp.isv");

        ExecutorService executor = Executors.newFixedThreadPool(15);
        List<Picture> pictureList1 = null;

        List<CompletableFuture<Picture>> futures = new ArrayList<>();

        for (Element element : elements) {
            //图片地址murl
            String m = element.select(".iusc").get(0).attr("m");
            Map<String,String> map= JSONUtil.toBean(m,Map.class);
            String murl= map.get("murl");
            //图片标题title
            String title=element.select(".inflnk").get(0).attr("aria-label");
            //Picture picture = new Picture();

            CompletableFuture<Picture> future=CompletableFuture.supplyAsync(()->{
                if (isValidImageUrl(murl)) {
                    Picture picture = new Picture();
                    picture.setTitle(title);
                    picture.setImgUrl(murl);
                    return picture;
            }
                return null;
                },executor);
            futures.add(future);


            if(futures.size()>=current+2){
                break;
            }

        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.join();

        pictureList1 = futures.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList());

        Page<Picture> pagePicture=new Page<>(pageNum,pageSize);
        pagePicture.setRecords(pictureList1);
        return pagePicture;
    }
}
