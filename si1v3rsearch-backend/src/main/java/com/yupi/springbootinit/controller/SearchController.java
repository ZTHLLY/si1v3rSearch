package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.dto.search.SearchAllRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


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


        //找出搜索类型
        String type = searchAllRequest.getType();
        SearchTypeEnum enumByValue = SearchTypeEnum.getEnumByValue(type);
        //ThrowUtils.throwIf(StringUtils.isBlank(type),ErrorCode.PARAMS_ERROR);

        //取出搜索词
        String searchText=searchAllRequest.getSearchText();

        if(enumByValue==null){
            //新建返回对象
            SearchVO searchVO=new SearchVO();
            //并发
            CompletableFuture<Page<Picture>> pictureTask= CompletableFuture.supplyAsync(()->{
                //图片搜索
                Page<Picture> pictureList = pictureService.PictureSearch(searchText, 1, 10);
                return pictureList;
            });

            CompletableFuture<Page<UserVO>> userTask= CompletableFuture.supplyAsync(()->{
                //用户搜索
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userList = userService.listUserVOByPage(userQueryRequest);
                return userList;
            });

            //帖子搜索 这个没有并发
            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            Page<PostVO> postList = postService.listPostVOByPage(postQueryRequest, request);


            CompletableFuture.allOf(userTask,pictureTask).join();

            try {

                Page<Picture> pictureList = pictureTask.get();
                Page<UserVO> userList = userTask.get();
                searchVO.setPictureList(pictureList.getRecords());
                searchVO.setPostVOList(postList.getRecords());
                searchVO.setUserVOList(userList.getRecords());
                return ResultUtils.success(searchVO);
            }catch (Exception e){
                log.error("查询异常",e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"查询异常");
            }
        }else {
            //新建返回对象
            SearchVO searchVO=new SearchVO();
            searchVO.setPostVOList(new ArrayList<>());
            searchVO.setUserVOList(new ArrayList<>());
            searchVO.setPictureList(new ArrayList<>());

            switch (enumByValue) {
                case POST:
                    PostQueryRequest postQueryRequest = new PostQueryRequest();
                    postQueryRequest.setSearchText(searchText);
                    Page<PostVO> postList = postService.listPostVOByPage(postQueryRequest, request);
                    searchVO.setPostVOList(postList.getRecords());
                    break;
                case USER:
                    UserQueryRequest userQueryRequest = new UserQueryRequest();
                    userQueryRequest.setUserName(searchText);
                    Page<UserVO> userList = userService.listUserVOByPage(userQueryRequest);
                    searchVO.setUserVOList(userList.getRecords());
                    break;
                case PICTURE:
                    Page<Picture> pictureList = pictureService.PictureSearch(searchText, 1, 10);
                    searchVO.setPictureList(pictureList.getRecords());
                    break;
                default:
            }

            return ResultUtils.success(searchVO);
        }




    }


}
