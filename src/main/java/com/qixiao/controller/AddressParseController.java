package com.qixiao.controller;

import com.qixiao.entity.AreaDataEntity;
import com.qixiao.service.AddressParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: addressParse
 * @BelongsPackage: com.qixiao.controller
 * @Author: qixiao
 * @Email：651901286@qq.com
 * @CreateTime: 2022-11-14  13:57
 * @Description:
 * @Version: 1.0
 */
@RestController
public class AddressParseController {

    @Autowired
    private AddressParseService addressParseService;

    @GetMapping("test")
    public AreaDataEntity test(@RequestParam String text) {
        AreaDataEntity areaDataEntity = addressParseService.parseByAddress(text);
        return areaDataEntity;
    }
}
