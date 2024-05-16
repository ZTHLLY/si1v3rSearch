package com.yupi.springbootinit.model.vo;
import com.yupi.springbootinit.model.entity.Picture;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 聚合搜索
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class SearchVO implements Serializable {


    //展示给前端的用户列表
    private List<UserVO> userVOList;
    //帖子列表
    private List<PostVO> postVOList;
    //图片列表
    private List<Picture> pictureList;


}
