package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.dto.search.SearchAllRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchAllRequest searchAllRequest, HttpServletRequest request){
        //图片搜索
        String searchText=searchAllRequest.getSearchText();
        Page<Picture> pictureList = pictureService.PictureSearch(searchText, 1, 10);

        //用户搜索
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        Page<UserVO> userList = userService.listUserVOByPage(userQueryRequest);

        //帖子搜索
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        Page<PostVO> postList = postService.listPostVOByPage(postQueryRequest, request);

        SearchVO searchVO=new SearchVO();
        searchVO.setPictureList(pictureList.getRecords());
        searchVO.setPostVOList(postList.getRecords());
        searchVO.setUserVOList(userList.getRecords());


        return ResultUtils.success(searchVO);
    }


}
