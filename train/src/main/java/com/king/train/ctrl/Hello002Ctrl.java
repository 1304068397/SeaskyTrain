package com.king.train.ctrl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.king.train.dao.JkFailMapper;
import com.king.train.dao.JkSuccessMapper;
import com.king.train.dto.ExportOut;
import com.king.train.dto.JsonDetailDto;
import com.king.train.entities.JkSuccessPo;
import com.king.train.utils.OCRUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @PackageName:com.king.train.ctrl
 * @ClassName:Hello002Ctrl
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2021/3/26 10:46
 */
@RestController
public class Hello002Ctrl {

    @Autowired
    JkSuccessMapper successMapper;

    @Autowired
    JkFailMapper failMapper;

    @PostMapping("/failHandle")
    public void ocr(HttpServletResponse response) throws Exception {
        List<String> fileNameList = fileNameList();
        List<String> jsonStrList = jsonStrList();
        List<ExportOut> outList = new ArrayList<>();
        for (int i = 0; i < fileNameList.size(); i++) {
            String filename = fileNameList.get(i);
            String jsonStr = jsonStrList.get(i);
            List<ExportOut> exportOuts = handleData(jsonStr,filename);
            outList.addAll(exportOuts);
        }
        System.out.println("准备导出...");
        writeExcel(response,outList,"20201成绩");
    }

    public void writeExcel(HttpServletResponse response, List<ExportOut> exportList, String exportName) {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");
        try {
            String fileName = URLEncoder.encode(exportName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), ExportOut.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("20201成绩").doWrite(exportList);
        } catch (IOException e) {
            System.out.println("导出文件出错！");
        }
    }

    private List<String> fileNameList(){
        List<File> allFile = OCRUtil.getAllFile("C:\\Users\\13040\\Desktop\\fail\\不完整，必须人工处理");
        List<String> list = new ArrayList<>();
        for (File file:allFile) {
            list.add(file.getName());
        }
        return list;
    }

    private List<String> jsonStrList(){
        QueryWrapper<JkSuccessPo> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        List<JkSuccessPo> jkSuccessPos = successMapper.selectList(wrapper);
        List<String> collect = jkSuccessPos.stream().map(JkSuccessPo::getDetail).collect(Collectors.toList());
        return collect;
    }

    private List<ExportOut> handleData(String jsonStr,String fileName) throws Exception {
        List<ExportOut> exportOutList = new ArrayList<>();
        //json转对象，麻烦
        JsonDetailDto items = null;
        List<List> lists = new ArrayList<>();
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            items = JSON.toJavaObject(jsonObject, JsonDetailDto.class);
            String table1 = jsonObject.getString("Table1");
            if (!Objects.isNull(JSON.parseArray(table1, List.class))){
                lists = JSON.parseArray(table1, List.class);
            }
        } catch (Exception e) {
//            System.out.println("------------------------>>>>>>>>"+jsonStr);
//            throw new DataFormatException("出错啦 "+jsonStr);
        }
        List<String> temp = new ArrayList<>();
        temp.add(fileName);
        temp.add("-");
        lists.add(0, temp);

        for (int i = 0; i < lists.size(); i++) {
            List<String> d = lists.get(i);
            if (d.size()<2){
                continue;
            }
            if (StringUtils.isEmpty(d.get(0)) || StringUtils.isEmpty(d.get(1))){
                continue;
            }
            if (i==0){
                ExportOut build = ExportOut.builder()
                        .xq(fileName)
                        .kch("-")
                        .build();
                exportOutList.add(build);
            } else {
                ExportOut build = ExportOut.builder()
                        .xq("20201")
                        .kch("t课程号")
                        .bh("班号")
                        .xh(d.get(0))
                        .xsxm(d.get(1))
                        .pscj(d.get(2))
                        .qmcj(d.get(3))
                        .zpcj(d.get(4))
                        .cjzs("百分制")
                        .pscjbl("50%")
                        .qmcjbl("50%")
                        .build();
                exportOutList.add(build);
            }
        }
        ExportOut build1 = ExportOut.builder()
                .xq("")
                .kch("")
                .bh("")
                .xh("")
                .xsxm("")
                .pscj("")
                .qmcj("")
                .zpcj("")
                .cjzs("")
                .pscjbl("")
                .qmcjbl("")
                .build();
        exportOutList.add(build1);
        return exportOutList;
    }
}
