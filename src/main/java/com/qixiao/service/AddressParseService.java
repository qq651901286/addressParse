package com.qixiao.service;

import com.qixiao.entity.AreaDataEntity;

import java.util.List;

/**
 * @BelongsProject: addressParse
 * @BelongsPackage: com.qixiao.service
 * @Author: qixiao
 * @Email：651901286@qq.com
 * @CreateTime: 2022-11-14  10:43
 * @Description:
 * @Version: 1.0
 */
public interface AddressParseService {
    /**
     * 加载地址数据
     * @return
     */
    List<AreaDataEntity> loadData();

    /**
     * 通过地址来解析
     * @param address
     * @return
     */
    AreaDataEntity parseByAddress(String address);
}
