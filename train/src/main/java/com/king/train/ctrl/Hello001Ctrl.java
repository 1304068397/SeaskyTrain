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
import com.king.train.entities.CommonResult;
import com.king.train.entities.JkFailPo;
import com.king.train.entities.JkSuccessPo;
import com.king.train.enums.ResponseCode;
import com.king.train.service.Ocrtest;
import com.king.train.utils.OCRUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

/**
 * @PackageName:com.example.demo.controller
 * @ClassName:Hello001Ctrl
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2021/3/5 15:51
 */
@RestController
public class Hello001Ctrl {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    Ocrtest Ocrtest;

    @Autowired
    JkSuccessMapper successMapper;

    @Autowired
    JkFailMapper failMapper;

    private String getStr(){
        String a=
                "105002121-《医学检验导论》成绩记载表-金磊-19医检3班_3.png,105007211-2- 《免疫学及免疫检验技术（2）》成绩记载表-郑红-18级医学检验技术本科1班_4.png,105008111-2-《微生物学及微生物检验技术2》成绩记载表--聂志妍-18级医学检验技术本科1班_4.png,105010111-1-《临床血液学及血液学检验技术（1）》成绩记载表-盛跃颖-18级医检本科1班_4.png,105025111-1-成绩记载表-病理检验技术-李雪梅-B20050301_2.png,105100111-《生物化学》-成绩记载表-马琳琳-19医检3班_3.png,105304011-《口腔材料学》成绩记载表-陈荣荣-B19-2_2.png,117001111-3--成绩记载表-大学英语(3)-19级临床医学（本科）4班--郑德虎_2.png,19级中药学（高职）班(218001021 文献检索 彭骏等)_1.png,19级中药学（高职）班(218001021 文献检索 彭骏等)_2.png,19级药品生物技术（生物制药方向）（中高贯通）班(209108011 文献检索与利用B 彭骏等)_1.png,300001261-《做自己的口腔医生》成绩记载表-陈凤贞_4.png,300001294-成绩记载表-科技论文写作-彭南求-0001_2.png,300001305《牙痛五千年：口腔医学技术史》成绩记载表-董晛_4.png,Scan1_1.png,Scan2_1.png,Scan3_1.png,Scan4_1.png,医学统计学3班_3.png,康复1班(118001111 文献检索A 彭骏等)_1.png,康复1班(118001111 文献检索A 彭骏等)_2.png,康复2班(118001111 文献检索A 彭骏等)_1.png,康复2班(118001111 文献检索A 彭骏等)_2.png,药学本科1班(118001111 文献检索A 彭骏等)_1.png,药学本科2班(118001111 文献检索A 彭骏等)_1.png,药学本科3班(118001111 文献检索A 彭骏等)_1.png,药学本科4班(118001111 文献检索A 彭骏等)_1.png,药学本科5班(118001111 文献检索A 彭骏等)_1.png,药学本科6班(118001111 文献检索A 彭骏等)_1.png,课程号：300001040 课程名称：钢琴（初级）主讲教师：陈纯-0001（2）_1.png,课程号：300001253 课程名称：艺术设计鉴赏 主讲教师：王晓琦-0001（2）_1.png,课程号：300001277 课程名称：形体与健康 主讲老师：张麟，胡悦-0002（2）_1.png,课程号：300001278 课程名称：综合材料设计 主讲老师：戴竞宇、谢雨彤-0001（2）_1.png";return a;
    }

    //复制指定文件名到指定文件夹
    @PostMapping("allfileToPath")
    public void allfileToPath() {
        total=0;
        String str =getStr();
        List<String> list = Stream.of(str.split(",")).collect(Collectors.toList());
        Map<String,String> map = new HashMap<>();
        map.put("path", "C:\\Users\\13040\\Desktop\\ocrtemp");
        for (int i = 0; i < list.size(); i++) {
            String filename = list.get(i);
            map.put("filename", filename);
            copyFileToPath(map);
        }
        System.out.println("复制指定文件名到指定文件夹SUCCESS!应复制"+list.size()+"张图片,实际复制"+total+"张图片！");
    }

    private int total= 0;

    //复制指定文件名到指定文件夹
    @PostMapping("copyFileToPath")
    public void copyFileToPath(@RequestBody Map<String,String> map) {
        //要复制的文件名
        String filename = map.get("filename");
        //文件终点的指定文件夹
        String topath = map.get("path");
        String pathname = "D:\\pic\\allPng\\"+filename;
        File file = new File(pathname);
        //复制到的位置
        String topathname = topath;
        File toFile = new File(topathname);
        try {
            copy(file, toFile);
        } catch (Exception e) {
            System.out.println("异常！");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("完成！");
        total++;
    }

    public static void copy(File file, File toFile) throws Exception {
        byte[] b = new byte[1024];
        int a;
        FileInputStream fis;
        FileOutputStream fos;
        if (file.isDirectory()) {
            String filepath = file.getAbsolutePath();
            filepath=filepath.replaceAll("\\\\", "/");
            String toFilepath = toFile.getAbsolutePath();
            toFilepath=toFilepath.replaceAll("\\\\", "/");
            int lastIndexOf = filepath.lastIndexOf("/");
            toFilepath = toFilepath + filepath.substring(lastIndexOf ,filepath.length());
            File copy=new File(toFilepath);
            //复制文件夹
            if (!copy.exists()) {
                copy.mkdir();
            }
            //遍历文件夹
            for (File f : file.listFiles()) {
                copy(f, copy);
            }
        } else {
            if (toFile.isDirectory()) {
                String filepath = file.getAbsolutePath();
                filepath=filepath.replaceAll("\\\\", "/");
                String toFilepath = toFile.getAbsolutePath();
                toFilepath=toFilepath.replaceAll("\\\\", "/");
                int lastIndexOf = filepath.lastIndexOf("/");
                toFilepath = toFilepath + filepath.substring(lastIndexOf ,filepath.length());

                //写文件
                File newFile = new File(toFilepath);
                fis = new FileInputStream(file);
                fos = new FileOutputStream(newFile);
                while ((a = fis.read(b)) != -1) {
                    fos.write(b, 0, a);
                }
            } else {
                //写文件
                fis = new FileInputStream(file);
                fos = new FileOutputStream(toFile);
                while ((a = fis.read(b)) != -1) {
                    fos.write(b, 0, a);
                }
            }

        }
    }


    @PostMapping("testInsert")
    public void testInsert() {
        //数据写到数据库
        String fileName = "haha";
        List<ExportOut> exportOutList = new ArrayList<>();
        ExportOut build = ExportOut.builder()
                .xq(fileName)
                .kch("-")
                .build();
        exportOutList.add(build);
        int save = Ocrtest.save(fileName, exportOutList);
        System.out.println("成功：：准备写入数据库：："+fileName+"，结果："+save);
    }

    @PostMapping("testQuery")
    public CommonResult testQuery() {
        List<JkSuccessPo> jkSuccessPos = successMapper.selectList(null);
        List<ExportOut> out = new ArrayList<>();
        for (JkSuccessPo po:jkSuccessPos) {
            List<ExportOut> exportOuts = JSON.parseArray(po.getDetail(), ExportOut.class);
            out.addAll(exportOuts);
        }
        return new CommonResult(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message(),out);
    }

    //查找失败的成绩单名称信息
    @PostMapping("testQueryFail")
    public CommonResult testQueryFail() {
        List<JkFailPo> jkFailPos = failMapper.selectList(null);
        List<String> collect = jkFailPos.stream().map(JkFailPo::getName).collect(Collectors.toList());
        return new CommonResult(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message(),collect);
    }

    //处理失败信息
    @PostMapping("handleFail")
    public CommonResult handleFail() {
        List<JkFailPo> jkFailPos = failMapper.selectList(null);
        List<String> collect = jkFailPos.stream().map(JkFailPo::getName).collect(Collectors.toList());
        //去成功表查询成绩单名字，如果查到了，就删除，没查到就不处理
        for (String name:collect) {
            List<JkSuccessPo> jkSuccessPos = successMapper.selectList(new QueryWrapper<>(JkSuccessPo.builder().name(name).build()));
            if (jkSuccessPos.size()>0){
                Map<String, Object> map = new HashMap<>();
                map.put("name", name);
                int i = failMapper.deleteByMap(map);
            }
        }
        return new CommonResult(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message());
    }

    //物理删除
    @PostMapping("testDelete")
    public CommonResult testDelete(@RequestBody Map<String,Long> map) {
        Long id = map.get("id");
        if (Objects.isNull(id)){
            return new CommonResult(ResponseCode.FAILURE.value(),ResponseCode.FAILURE.message());
        }
        int i = successMapper.deleteById(id);
        return new CommonResult(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message());
    }

    @GetMapping("/hello")
    public String hello(){
        Long views = redisTemplate.opsForValue().increment("views");
        return "谢谢你的陪伴，爱的次数+1："+views+"!";
    }

    private final static Executor executor = Executors.newSingleThreadExecutor();//启用多线程
    private static AtomicInteger count = new AtomicInteger(0);
    @PostMapping("/ocr")
    public void ocr( HttpServletResponse response,@RequestParam("path") String path) throws Exception {
        path= StringUtils.isEmpty(path)?"D:\\pic":path;
        List<File> allFile = OCRUtil.getAllFile(path);
        List<String> jsonList = new ArrayList<>();
        List<ExportOut> allList = new ArrayList<>();
        int allSize = 0;
        final CountDownLatch latch = new CountDownLatch(allFile.size());
        for (int i = 0; i < allFile.size(); i++) {
            final int j=i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        File file = allFile.get(j);
                        String fileName = file.getName();
//                        if (fileName.substring(0,1).equals("x")){
//                            continue;
//                        }
                        String s = OCRUtil.callMain(file);
                        jsonList.add(s);
                        //处理数据
                        List<ExportOut> exportOutList = null;
                        try {
                            exportOutList = handleData(s,fileName);
                        } catch (Exception e) {
                            System.out.println("错误文件名------》》》》》》"+file.getName());
                        }
                        if (exportOutList.size()>0){
                            count.incrementAndGet();
                            allList.addAll(exportOutList);
                        }
                        if (exportOutList.size()>2){
                            //数据写到数据库
                            //int insert = successMapper.insert(JkSuccessPo.builder().name(fileName).detail(JSON.toJSONString(exportOutList)).build());
                            //System.out.println("成功：：准备写入数据库：："+file.getName()+"，结果："+insert);
                        }else {
                            //数据写到数据库
                            //int insert = failMapper.insert(JkFailPo.builder().name(fileName).detail(JSON.toJSONString(exportOutList)).build());
                            //System.out.println("失败：：准备写入数据库：："+file.getName()+"，结果："+insert);
                        }
                        System.out.println("---------->>>>>>>>------->>>>>>>"+fileName+"处理完成！ 剩余"+(allFile.size()-count.get())+"条待处理！");
                    }catch(Exception e){

                    }finally {
                        latch.countDown();
                    }
                }
            });
        }
        //导出
        latch.await();
        String res = allFile.size()-count.get()==0?"结果正确！":"结果失败！";
        ExportOut build = ExportOut.builder()
                .xq("共有图片"+allFile.size()+"张,实际导出"+count.get()+"张,"+res)
                .build();
        allList.add(build);
        System.out.println("多线程结束");
        count.set(0);
        if (allList.size()>0){
            writeExcel(response,allList,"20201成绩");
        }
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

    private List<ExportOut> handleData(String jsonStr,String fileName) throws Exception {
        List<ExportOut> exportOutList = new ArrayList<>();
        //json转对象，麻烦
        JsonDetailDto items = null;
        List<List> lists = new ArrayList<>();
        try {
            Map map = JSON.parseObject(jsonStr, HashMap.class);
            String itemStr = JSON.toJSONString(map.get("items"));
            JSONObject jsonObject = JSONObject.parseObject(itemStr);
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
//        if (ObjectUtils.isEmpty(lists)){
//            return new ArrayList<>();
//        }
        //构建集合
        String cjbl = Objects.isNull(items) || Objects.isNull(items.get成绩比例()) ? "":items.get成绩比例();
        String pscjbl = null;
        String qmcjbl = null;
        if (!StringUtils.isEmpty(cjbl)){
            try {
                pscjbl = cjbl.substring(cjbl.indexOf("平时成绩")+4, cjbl.indexOf("%")+1);
                qmcjbl = cjbl.substring(cjbl.indexOf("期末成绩")+4);
                pscjbl = StringUtils.isEmpty(pscjbl)?"":pscjbl;
                qmcjbl = StringUtils.isEmpty(qmcjbl)?"":qmcjbl;
            } catch (Exception e) {
                throw new DataFormatException(items.get课程号()+"课程号出错");
            }
        }
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
            } else if (i==1){
                continue;
            } else if (i>1){
                ExportOut build = ExportOut.builder()
                        .xq("20201")
                        .kch(items.get课程号())
                        .bh(items.get班号())
                        .xh(d.get(0))
                        .xsxm(d.get(1))
                        .pscj(d.get(2))
                        .qmcj(d.get(3))
                        .zpcj(d.get(4))
                        .cjzs(items.get成绩制式())
                        .pscjbl(pscjbl)
                        .qmcjbl(qmcjbl)
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
