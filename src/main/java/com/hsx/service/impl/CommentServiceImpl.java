package com.hsx.service.impl;

import com.hsx.mapper.CommentMapper;
import com.hsx.pojo.Comment;
import com.hsx.pojo.User;
import com.hsx.pojo.UserInfo;
import com.hsx.service.CommentService;
import com.hsx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

    @Override
    public List<Comment> getAllCommentByVideoId(Long videoId) {
        //blogId    文章id   accountId     评论者    parentId    上级评论   sons     下集评论

        //找视频的所有一级评论的
        List<Comment> commentVos = commentMapper.getCommentByVideoId(videoId);

        //获取所有子评论
        List<Comment> sons = getSons(commentVos);

        return sons;
    }

    //根据上级评论来找下级评论
    private List<Comment> getSons(Long parentId) {
        return commentMapper.getCommentByParentId(parentId);
    }

    //根据下级来找上级评论
    private Comment getParent(Long id) {return commentMapper.getCommentById(id);}

    // 获取下级评论总数，包括子评论的子评论
    private Long getTotalSonsCount(Long parentId) {
        List<Comment> sons = getSons(parentId);
        Long sonsCount = 0L;
        if (sons != null) {
            sonsCount += sons.size();
            for (Comment son : sons) {
                sonsCount += getTotalSonsCount(son.getId());
            }
        }
        return sonsCount;
    }

    //通过视频的一级评论查找所有子评论
    private List<Comment> getSons(List<Comment> parents) {

        if (parents == null || parents.size() == 0) {
            return null;
        }

        for (Comment parent : parents) {

            Long parentId = parent.getId(); //获取评论的id

            //通过id查询评论的子评论
            List<Comment> sonCommentVos = getSons(parentId);

            // 获取下级评论总数
            Long totalSonsCount = getTotalSonsCount(parentId);
            parent.setSonsCount(totalSonsCount);




            // 获取当前时间的 java.util.Date 对象
            Date currentTime = new Date();


            // 计算时间差
            long timeDifferenceInMillis = currentTime.getTime() - parent.getCreateTime().getTime();


            // 将时间差转换为距离当前时间的时间差字符串
            String timeDifference = formatTimeDifference(timeDifferenceInMillis);

            // 将 timeDifference 返回给前端
            parent.setTimeDifference(timeDifference);


            //查找评论发布者/回复者的信息
            UserInfo userInfo =userService.getUserInfoByUserId(parent.getUserId());
            parent.setUserInfo(userInfo);

            //查找被回复者的信息
            if(parent.getParentId()!=0){
                Comment comment = getParent(parent.getParentId());
                UserInfo replyUserInfo = userService.getUserInfoByUserId(comment.getUserId());
                parent.setReplyUserInfo(replyUserInfo);
            }

            //递归找子评论
            parent.setSons(getSons(sonCommentVos));
        }
        return parents;
    }

    public  String formatTimeDifference(long timeDifference) {
        long SECOND = 1000; // 1秒 = 1000毫秒
        long MINUTE = 60 * SECOND; // 1分钟 = 60秒
        long HOUR = 60 * MINUTE; // 1小时 = 60分钟
        long DAY = 24 * HOUR; // 1天 = 24小时
        long WEEK = 7 * DAY; // 1周 = 7天

        if (timeDifference < MINUTE) {
            // 不足1分钟
            return "刚刚";
        } else if (timeDifference < HOUR) {
            // 不足1小时
            long minutes = timeDifference / MINUTE;
            return minutes + "分钟前";
        } else if (timeDifference < DAY) {
            // 不足1天
            long hours = timeDifference / HOUR;
            return hours + "小时前";
        } else if (timeDifference < WEEK) {
            // 不足1周
            long days = timeDifference / DAY;
            return days + "天前";
        } else {
            // 大于等于1周，直接显示日期
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(new Date());
        }
    }


    /**
     *  发布/回复评论
     * */
    @Override
    @Transactional
    public void commitComment(Comment comment) {


        if (comment.getParentId() == 0) { //根评论
            comment.setCommentStatus(0);
        }else{
            comment.setCommentStatus(1);
        }
        comment.setCreateTime(new Date());
        commentMapper.insertComment(comment);

        return;
    }


}

