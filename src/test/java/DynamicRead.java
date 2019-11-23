import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import com.github.excel.bean.ImportErrMsg;
import com.github.excel.configs.TemplateSheetConfig;
import com.github.excel.listener.DefaultDynamicReadListener;
import com.github.excel.configs.ImportConfig;
import com.github.excel.listener.SelfProcessDataReadListener;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态读
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/10/29 15:10
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 */
@Ignore
public class DynamicRead {

    @Test
    public void DynamicReadByTemplate() {
        String fileName = "D:\\my-opensource-utils\\dynamic-excel-import\\src\\main\\resources\\template\\DemoDataExtraColumn.xlsx";
        ImportConfig config = new ImportConfig();

        config.setTemplateColConfigs(TestDataSource.selectTemplateColConfigByTemplateConfigId(2340L));
        config.setDataSource(TestDataSource.selectDataSourceById(1L));
        config.setSheetConfig(TestDataSource.selectSheetConfigById(0L));
        long start = System.currentTimeMillis();
        EasyExcel.read(fileName, new DefaultDynamicReadListener()).customObject(config).sheet(1).headRowNumber(6).doRead();
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end-start) + "ms");

    }

    /**
     * 头标题不是顶行
     * @param 
     * @throws 
     * @return void
     * @author peiyuan.wang@hand-china.com
     * @date  
     */
    @Test
    @Deprecated
    public void headTopIndexNo0() {
        String fileName = "D:\\my-opensource-utils\\dynamic-excel-import\\src\\main\\resources\\template\\DemoDataTitleNoTop.xlsx";
        ImportConfig config = new ImportConfig();

        config.setTemplateColConfigs(TestDataSource.selectTemplateColConfigByTemplateConfigId(123L));
        config.setDataSource(TestDataSource.selectDataSourceById(1L));
        config.setSheetConfig(TestDataSource.selectSheetConfigById(0L));
        long start = System.currentTimeMillis();
        EasyExcel.read(fileName, new DefaultDynamicReadListener()).customObject(config).sheet().headRowNumber(4).doRead();
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end-start) + "ms");
    }
    
    /**
     * 关闭自动持久化
     * @param
     * @throws
     * @return
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    @Test
    public void closeAutoUpdate() {
        String fileName = "D:\\my-opensource-utils\\dynamic-excel-import\\src\\main\\resources\\template\\DemoDataTitleNoTop.xlsx";
        ImportConfig config = new ImportConfig();

        config.setTemplateColConfigs(TestDataSource.selectTemplateColConfigByTemplateConfigId(123L));
        config.setDataSource(TestDataSource.selectDataSourceById(1L));
        config.setSheetConfig(TestDataSource.selectCloseAutoUpdate());
        long start = System.currentTimeMillis();
        EasyExcel.read(fileName, new DefaultDynamicReadListener()).customObject(config).sheet().headRowNumber(4).doRead();
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end-start) + "ms");
    }

    /**
     * 每行的返回自动转换为java模型并存储至list，在invoke方法里获取
     * @param
     * @throws
     * @return
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    @Test
    public void useJavaModel() {
        String fileName = "D:\\my-opensource-utils\\dynamic-excel-import\\src\\main\\resources\\template\\DemoDataTitleNoTop.xlsx";
        ImportConfig config = new ImportConfig();

        config.setTemplateColConfigs(TestDataSource.selectTemplateColConfigByTemplateConfigId(234L));
        config.setDataSource(TestDataSource.selectDataSourceById(1L));
        config.setSheetConfig(TestDataSource.selectSaveJaveModelList());
        long start = System.currentTimeMillis();
        EasyExcel.read(fileName, new SelfProcessDataReadListener()).customObject(config).sheet().headRowNumber(4).doRead();
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end-start) + "ms");

    }

    /**
     * 更新方式是合并
     * @param
     * @throws
     * @return void
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    @Test
    public void updateWayIsMargeMysql() {

    }

    /**
     * 更新方式插入 oracle
     * @param
     * @throws
     * @return void
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    @Test
    public void updateWayIsInsertOra() {

    }

    /**
     * 更新方式是合并
     * @param
     * @throws
     * @return void
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    @Test
    public void updateWayIsMargeOra() {

    }
    
    /**
     * 多sheet页导入
     * @param 
     * @throws 
     * @return void
     * @author peiyuan.wang@hand-china.com
     * @date  
     */
    @Test
    public void moreSheetImport() {
        String fileName = "D:\\my-opensource-utils\\dynamic-excel-import\\src\\main\\resources\\template\\DemoData多Sheet.xlsx";
        ImportConfig config = new ImportConfig();

        config.setTemplateColConfigs(TestDataSource.selectTemplateColConfigByTemplateConfigId(234L));
        config.setDataSource(TestDataSource.selectDataSourceById(1L));
        config.setSheetConfig(TestDataSource.selectSheetConfigById(0L));
        long start = System.currentTimeMillis();

        ExcelReader excelReader = EasyExcel.read(fileName).customObject(config).build();
        // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
        ReadSheet readSheet1 =
                EasyExcel.readSheet(0).registerReadListener(new DefaultDynamicReadListener()).build();
        readSheet1.setHeadRowNumber(4);
        ReadSheet readSheet2 =
                EasyExcel.readSheet(1).registerReadListener(new DefaultDynamicReadListener()).build();
        readSheet2.setHeadRowNumber(4);
        ReadSheet readSheet3 =
                EasyExcel.readSheet(2).registerReadListener(new DefaultDynamicReadListener()).build();
        readSheet3.setHeadRowNumber(4);
        // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
        excelReader.read(readSheet1, readSheet2, readSheet3);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();

        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end-start) + "ms");
    }

}
