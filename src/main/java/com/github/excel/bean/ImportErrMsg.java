package com.github.excel.bean;

import lombok.Data;

import java.util.Date;

/**
 * 导入异常信息
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/10/29 12:48
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 * @modified by:
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
@Data
public class ImportErrMsg {
    /**
     * 发生错误的批次号
     */
    private Integer batchNumber;
    /**
     * 发生错误的行号
     */
    private Integer rowIndex;
    /**
     * 发生错误的列号
     */
    private Integer colIndex;
    /**
     * 错误的描述
     */
    private String errorMsg;
    /**
     * 错误的堆栈
     */
    private String stackTrace;
    /**
     * 发生错误的异常Sql
     */
    private String exceptionSql;
    /**
     * 发生错误的时间
     */
    private Date happenDataTime;

    public ImportErrMsg(Integer rowIndex, Integer colIndex, String errorMsg) {
        super();
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.errorMsg = errorMsg;
        this.happenDataTime = new Date();
    }

    public ImportErrMsg(Integer batchNumber, String exceptionSql, String errorMsg, String stackTrace) {
        super();
        this.batchNumber = batchNumber;
        this.exceptionSql = exceptionSql;
        this.errorMsg = errorMsg;
        this.stackTrace = stackTrace;
        this.happenDataTime = new Date();
    }

    public ImportErrMsg(Integer batchNumber, String errorMsg) {
        super();
        this.batchNumber = batchNumber;
        this.errorMsg = errorMsg;
        this.happenDataTime = new Date();
    }
}
