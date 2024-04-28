package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;



/**
 * 帖子
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@TableName(value = "post")
@Data
public class Picture{

    /**
     * 标题
     */
    private String title;
    /**
     * 地址
     */
    private String imgUrl;


}