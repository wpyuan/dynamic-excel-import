package com.github.excel.bean;

import lombok.Data;

import java.util.Date;

/**
 * 导入结果信息
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/11/5 9:23
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 * @modified by:
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
@Data
public class ImportInfo {
    private Long batchId;
    private String batchNumber;
    private String fileName;
    private Integer fileSize;
    private String fileUrl;
    private Long successCount;
    private Long total;
    private Long userId;
    private Date importDate;

}
