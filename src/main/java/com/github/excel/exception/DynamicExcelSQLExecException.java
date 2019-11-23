package com.github.excel.exception;

import lombok.Data;

/**
 * SQL执行异常
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/10/29 15:41
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 */
@Data
public class DynamicExcelSQLExecException extends DynamiceExcelException {

    private Integer bathchNumber;
    private String exceptionSql;

    public DynamicExcelSQLExecException(Integer bathchNumber, String exceptionSql, String message, Throwable throwable) {
        super(message, throwable);
        this.bathchNumber = bathchNumber;
        this.exceptionSql = exceptionSql;
    }
}
