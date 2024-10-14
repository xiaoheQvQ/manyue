package com.hsx.service.impl;

import com.hsx.mapper.VideoMapper;
import com.hsx.pojo.Video;
import com.hsx.service.FileService;
import com.hsx.utils.AliyunOSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
@Service
public class FileServiceImpl implements FileService {

    public Video Upload(Long userId,MultipartFile file, String id) {

        //获取文件名
        String filename = file.getOriginalFilename();
        //给文件拼接时间戳
        String newName = new Date().getTime() + "-" + filename;
        String fileNames = userId + "/" + "video/" + newName;
        //文件上传
        AliyunOSSUtil.uploadFileByte(file,  fileNames);

        //截取视频封面
        //拆分字符串
        String[] split = newName.split("\\.");
        String coverName = split[0];
        //拼接封面名
        String coverNames = userId + "/" + "cover/" + coverName + ".jpg";
        /*截取视频封面
         * 视频截取帧
         * */
        URL url = AliyunOSSUtil.videoInterceptCover(fileNames);
        /*
         * 上传视频封面至阿里云
         * 参数：
         *   netPath:网络路径
         * */
        try {
            AliyunOSSUtil.uploadNetFile(url.toString(), coverNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //拼接视频网络地址
        String videoUrl = "https://hsx-hb.oss-cn-beijing.aliyuncs.com/" + fileNames;
        String imgUrl =  "https://hsx-hb.oss-cn-beijing.aliyuncs.com/" + coverNames;
        Video video = new Video();
        video.setVideoUrl(videoUrl);
        video.setImgUrl(imgUrl);

       return video;
    }


}
