package com.hsx.constant;

import java.util.ArrayList;
import java.util.List;


public interface UserConstant {

    String GENDER_MALE = "0";

    String GENDER_FEMALE = "1";

    String GENDER_UNKNOW = "2";

    String DEFAULT_BIRTH = "2000-01-01";

    String DEFAULT_NICK = "萌新";

    String DEFAULT_AVATAR = "https://mp-a29d2492-cdac-4c3b-89f4-f4d0e8bd7b62.cdn.bspapp.com/仿b站/默认配置/默认头像.jpg";

    String DEFAULT_SIGN = "暂未设置签名";

    String USER_FOLLOWING_GROUP_TYPE_DEFAULT = "2";

    String USER_FOLLOWING_GROUP_ALL_NAME = "全部分组";

    String USER_FOLLOWING_GROUP_TYPE_USER = "3";


    /** 头像文件大小的上限值(10MB) */
    int AVATAR_MAX_SIZE = 10 * 1024 * 1024;
    /** 允许上传的头像的文件类型 */
     List<String> AVATAR_TYPES = new ArrayList<String>(){{
            add("png");
            add("jpg");
        }};




}
