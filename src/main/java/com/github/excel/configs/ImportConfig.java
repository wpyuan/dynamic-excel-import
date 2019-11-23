package com.github.excel.configs;

import lombok.Data;

import java.util.List;

/**
 * 导入全部配置
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/10/30 14:28
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 */
@Data
public class ImportConfig {

    /**
     * sheet页配置
     */
    private TemplateSheetConfig sheetConfig;
    /**
     * 列配置
     */
    private List<TemplateColConfig> templateColConfigs;
    /**
     * 数据源
     */
    private DataSource dataSource;

    /**
     * 预留属性
     */
    private Object attribute;
}
