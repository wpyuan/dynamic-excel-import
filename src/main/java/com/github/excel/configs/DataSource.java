package com.github.excel.configs;

import com.github.excel.enums.DBTypeEnum;
import lombok.Data;

/**
 * 数据源配置
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/10/30 14:02
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 */
@Data
public class DataSource {
    private Long dataSourceId;
    /**
     * 数据源名称
     */
    private String dataSourceName;
    /**
     * 数据库类型
     */
    private DBTypeEnum dbType;
    /**
     * 数据库驱动名
     */
    private String driver;
    /**
     * 地址
     */
    private String url;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
}
