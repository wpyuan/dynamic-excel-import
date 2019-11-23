package com.github.excel.utils;

import com.alibaba.excel.EasyExcel;
import com.github.excel.configs.ImportConfig;
import com.github.excel.listener.DefaultDynamicReadListener;
import com.github.excel.listener.SelfProcessDataReadListener;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 动态Excel导入
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/11/4 14:12
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 * @modified by:
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
public class DynamicExcelUtils {

    public static void read(String fileName, List<ImportConfig> configs) {
        long start = System.currentTimeMillis();
        configs.forEach(config -> {
            EasyExcel.read(fileName, config.getSheetConfig().getAutoUpdate() ? new DefaultDynamicReadListener() : new SelfProcessDataReadListener()).customObject(config).sheet(Math.toIntExact(config.getSheetConfig().getSheetNo())).headRowNumber(Math.toIntExact((config.getSheetConfig().getHeadRowNumber()))).doRead();
        });
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end-start) + "ms");
    }

    public static void read(File file, List<ImportConfig> configs) {
        long start = System.currentTimeMillis();
        configs.forEach(config -> {
            EasyExcel.read(file, config.getSheetConfig().getAutoUpdate() ? new DefaultDynamicReadListener() : new SelfProcessDataReadListener()).customObject(config).sheet(Math.toIntExact(config.getSheetConfig().getSheetNo())).headRowNumber(Math.toIntExact(config.getSheetConfig().getHeadRowNumber())).doRead();
        });
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end-start) + "ms");
    }

    public static void read(InputStream in, List<ImportConfig> configs) {
        long start = System.currentTimeMillis();
        configs.forEach(config -> {
            EasyExcel.read(in, config.getSheetConfig().getAutoUpdate() ? new DefaultDynamicReadListener() : new SelfProcessDataReadListener()).customObject(config).sheet(Math.toIntExact(config.getSheetConfig().getSheetNo())).headRowNumber(Math.toIntExact(config.getSheetConfig().getHeadRowNumber())).doRead();
        });
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end-start) + "ms");
    }
}
