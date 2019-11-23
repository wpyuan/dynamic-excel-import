package com.github.excel.configs;

import lombok.Data;

/**
 * 模板头
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/10/28 15:46
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 */
@Data
public class TemplateConfig {
    private Long templateConfigId;

    /**
     * 模板代码
     */
    private String templateConfigCode;

    /**
     * 模板名称
     */
    private String templateConfigName;

    /**
     * 模板文件地址
     */
    private String url;

    /**
     * 是否启用
     */
    private String enableFlag;

    /**
     * 未启用原因
     */
    private String notEnabledReason;
}
