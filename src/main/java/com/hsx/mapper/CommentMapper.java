package com.hsx.mapper;



import com.hsx.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> getCommentByVideoId(Long videoId);

    List<Comment> getCommentByParentId(Long parentId);

    void insertComment(Comment commentDto);

    Comment getCommentById(Long id);
}
