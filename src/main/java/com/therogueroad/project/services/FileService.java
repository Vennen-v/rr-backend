package com.therogueroad.project.services;


import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String saveFileToAWSS3Bucket(MultipartFile file);
}
