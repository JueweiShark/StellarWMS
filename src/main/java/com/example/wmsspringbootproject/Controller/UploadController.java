package com.example.wmsspringbootproject.Controller;

import com.example.wmsspringbootproject.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "06.上传")
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/upload")
@Transactional
public class UploadController {
    @PostMapping("/uploadPicture")
    @Operation(summary = "上传图片")
    public Result uploadPicture(
            @RequestParam("file") MultipartFile file
            ){
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println(file.getOriginalFilename());
        return Result.success("http://qiniuyun.ktshark.icu/01e4cc13-b23e-4d4b-83de-08033f0ea4bd.jpg");
    }
}
