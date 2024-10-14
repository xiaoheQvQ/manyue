package com.hsx.service;

import com.hsx.pojo.Comment;

import java.util.List;

public interface CommentService {


     List<Comment> getAllCommentByVideoId(Long videoId);

    void commitComment(Comment comment);
}
