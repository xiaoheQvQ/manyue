package com.hsx.controller.Comment;

import com.hsx.pojo.Comment;
import com.hsx.pojo.JsonResponse;
import com.hsx.service.impl.CommentServiceImpl;
import com.hsx.utils.UserUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;


    /**
     *  获取指定某条评论下的所有评论
     * */
    @GetMapping("/Comment")
    public JsonResponse<List<Comment>> getBlogById(@Param("videoId") String videoId){
        Long vid = Long.valueOf(videoId);
        return new JsonResponse<>(commentService.getAllCommentByVideoId(vid));
    }


    /**
     *  发布评论/回复评论
     * */
    @PostMapping("/CommitComment")
    public JsonResponse<String> commitComment(@RequestBody Comment comment){

        Long userId = UserUtils.getCurrentUserId();
        comment.setUserId(userId);
        commentService.commitComment(comment);
        return JsonResponse.success("发送成功");

    }



}
