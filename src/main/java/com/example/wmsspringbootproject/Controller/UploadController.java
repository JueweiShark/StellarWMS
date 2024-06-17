package com.example.wmsspringbootproject.Controller;

import com.example.wmsspringbootproject.Utils.QiniuUtil;
import com.example.wmsspringbootproject.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "06.上传")
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/upload")
@Transactional
public class UploadController {
    @Autowired
    private QiniuUtil qiniuUtil;
    @PostMapping("/uploadPicture")
    @Operation(summary = "上传图片")
    public Result uploadPicture(
            @RequestParam("file") MultipartFile file
            ){
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println(file.getOriginalFilename());
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString()+"."+ StringUtils.substringAfterLast(originalFilename,".");
//        上传到七牛云
        boolean upload = qiniuUtil.upload(file, filename);

        if (!upload) {
            return Result.failed("上传失败");
        }
        String pictureUrl=qiniuUtil.url+"/"+filename;
        System.out.println("上传成功:"+pictureUrl);
        return Result.success(pictureUrl);
    }
}
