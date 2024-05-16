package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.dto.search.SearchAllRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private PostService postService;

    @Resource
    private PictureService pictureService;

    @Resource
    private UserService userService;

//    @PostMapping("/all")
//    public List<SearchVO> searchAll(@RequestBody SearchAllRequest searchAllRequest){
//        String searchText=searchAllRequest.getSearchText();
//        Page<Picture> pictureList = pictureService.PictureSearch(searchText, 1, 10);
//
//
//    }


}
