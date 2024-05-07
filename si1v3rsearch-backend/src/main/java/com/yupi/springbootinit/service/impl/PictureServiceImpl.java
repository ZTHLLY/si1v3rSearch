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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class PictureServiceImpl implements PictureService {
    @Override
    public Page<Picture> PictureSearch(String searchText, long pageNum, long pageSize) {
        List<Picture> pictureList = new ArrayList<>();

        long current=pageNum*pageSize;

        String url = String.format("https://www.bing.com/images/search?q=%s&first=%s",searchText,current);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        System.out.println(doc);
        Elements elements = doc.select(".iuscp.isv");
        for (Element element : elements) {
            //图片地址murl
            String m = element.select(".iusc").get(0).attr("m");
            Map<String,String> map= JSONUtil.toBean(m,Map.class);
            String murl= map.get("murl");
            //图片标题title
            String title=element.select(".inflnk").get(0).attr("aria-label");
            Picture picture = new Picture();

            picture.setTitle(title);
            picture.setImgUrl(murl);
            pictureList.add(picture);

            if(pictureList.size()>=current){
                break;
            }

        }
        Page<Picture> pagePicture=new Page<>(pageNum,pageSize);
        pagePicture.setRecords(pictureList);
        return pagePicture;
    }
}
