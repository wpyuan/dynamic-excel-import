package com.github.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.fastjson.JSON;
import com.github.excel.configs.ImportConfig;

/**
 * 自行处理数据入库
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/11/4 14:21
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 */
public class SelfProcessDataReadListener extends DefaultDynamicReadListener{


    @Override
    public int batchUpdate(ImportConfig config) {
        return 0;
    }

    @Override
    public void doAfterRead(AnalysisContext analysisContext) {
        System.out.println(analysisContext.readWorkbookHolder().getFile().getName() + "所有数据解析完成！");
        System.out.println("错误信息：" + JSON.toJSONString(getErrorMsgs()));
        System.out.println(analysisContext.readSheetHolder().getSheetName() + "总共导入：" + (getCurrentRowIndex() - analysisContext.readSheetHolder().getHeadRowNumber()));
        System.out.println("成功条数：" + getCurrentSuccessCount());
    }
}
