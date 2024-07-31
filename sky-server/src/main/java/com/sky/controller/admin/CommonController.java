package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口 [CommonController 類上方]")
@Slf4j
public class CommonController {

    /**
     * 文件上傳
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上傳 [CommonController 類方法 upload]")
    public Result<String> upload(MultipartFile file) {
        log.info("[Logger] 文件上傳: {}", file);

        // 獲取原始文件名
        String originalFilename = file.getOriginalFilename();

        // 構造唯一的文件名(不能重複) - uuid(通用唯一識別碼)
        int index = originalFilename.lastIndexOf(".");
        String extname = originalFilename.substring(index);
        String newFileName = UUID.randomUUID().toString() + extname;

        log.info("[Logger] 新的文件名: {}", newFileName);


        // 將文件儲存在 local 端 (/Users/david11yf29/Desktop/JavaTraining/project/projectImage/)
        try {
            file.transferTo(new File("/Users/david11yf29/Desktop/JavaTraining/project/projectImage/"+newFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
