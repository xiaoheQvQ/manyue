package com.hsx.service;

import com.hsx.pojo.Video;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

     Video Upload(Long userId,MultipartFile file, String id);

}
