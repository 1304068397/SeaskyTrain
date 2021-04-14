package com.king.train.utils;

import com.alibaba.fastjson.JSON;
import com.king.train.dto.Dto;
import com.king.train.dto.JsonDetailDto;
import com.king.train.dto.JsonResDto;
import com.king.train.dto.template_list;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class OCRUtil {

    private static String appcode;

    @Value("${ocr.appcode}")
    public void setAppcode(String appKey) {
        OCRUtil.appcode = appKey;
    }

    @Autowired
    public HttpServletResponse response;

    @Value("${ocr.templatecode}")
    public void setTemplatecode(String appKey) {
        OCRUtil.templatecode = appKey;
    }

    private static String templatecode;

    public static void main(String[] args) {
        //testJson();

//        List<File> allFile = getAllFile("D:\\pic");
//        List<String> jsonList = new ArrayList<>();
//        for (File file:allFile) {
//            String s = callMain(file);
//            jsonList.add(s);
//        }

        List<String> a = new ArrayList<>();
        a.add("A");
        a.add("B");
        a.add("C");
        a.add("D");
        a.add("E");
        System.out.println(a);
        a.remove("B");
        System.out.println(a);
        a.remove("Q");
        System.out.println(a);
        //导出
        //writeExcel(null,"20201成绩");
    }

    public static void testJson(){
        List<String> strings1 = new ArrayList<>();
        strings1.add("1");
        strings1.add("3");
        strings1.add("5");
        List<String> strings2 = new ArrayList<>();
        strings2.add("2");
        strings2.add("4");
        strings2.add("6");
        List<List<String>> strings3 = new ArrayList<>();
        strings3.add(strings1);
        strings3.add(strings2);
        JsonDetailDto build1 = JsonDetailDto.builder()
                .教师("tony")
                .上课教室("203")
                .校区("延长校区")
                .Table1(strings3)
                .build();
        JsonResDto build = JsonResDto.builder()
                .request_id("1")
                .template_id("2")
                .items(build1)
                .build();
        String s = JSON.toJSONString(build);
        System.out.println(s);
        JsonResDto jsonResDto = JSON.parseObject(s, JsonResDto.class);
        System.out.println(jsonResDto);

    }

    public static String callMain(File file){
        String absolutePath = file.getAbsolutePath();
        String base64Str = ImageToBase64(absolutePath);

        String host = "https://ocrdiy.market.alicloudapi.com";
        String path = "/api/predict/ocr_sdt";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        //String bodys = "输入格式(多模板自动匹配):基于关键字可以输入一组template_id，每组template_id有对应的条件(cond),满足条件即使用该template_id基本条件包含include，exclude，分别表示包含/不包含某个关键字组合条件使用and,or表示，详见下面的示例{\"image\":\"图片二进制数据的base64编码\",\"configure\":{\"template_list\":[{\"template_id\":\"fc7b375c-8e98-48ef-81fb-ec3843b2fb371534902155\",#星巴克的模板id\"cond\":{\"and\":[{\"or\":[{\"include\":\"星巴克\"},{\"include\":\"starbuck\"}]},{\"exclude\":\"Costa\"}]}},{\"template_id\":\"7bc26bd7-41ad-4145-9f5a-4af21b26de9a\",#costa的模板id\"cond\":{\"include\":\"costa\"}}]}}自动匹配{\"image\":\"图片二进制数据的base64编码\",\"configure\":{\"template_list\":[\"fc7b375c-8e98-48ef-81fb-ec3843b2fb371534902155\",#星巴克的模板id\"7bc26bd7-41ad-4145-9f5a-4af21b26de9a\",#costa的模板id]}}输出格式(不带表格的):{\"config_str\":\"{\\\"template_id\\\":\\\"95d551ee-8b2b-4ad0-89a9-ed13417f4a781536146904\\\"}\",\"items\":{\"cash_id\":\":11535001\",#key-value组合，key是用户在界面上填写的\"cash_name\":\":李伟\",\"change\":\"￥0.00\",\"date\":\":2018.03.1616:46\",\"drop\":\"￥-0.10\",\"num\":\":2\",\"pay\":\"￥65.50\",\"sell_type\":\"外带\",\"seller\":\"24662\",\"shop_name\":\"TEL:0571-5697229\",\"total\":\"￥65.60\"},\"request_id\":\"20180723151536_fa0edbe7408fc589b93119f7a53a1260\",\"success\":true}输出格式(带表格的输出)：{\"config_str\":\"{\\\"template_id\\\":\\\"dd3ada93-c6ed-4427-8e0b-fbdebad614061532347130\\\"}\",\"items\":{\"item_no\":\"1313131313\",\"mail\":\"daigou@gmail.com\",\"name\":\"代购小哥\",\"nation\":\"美国\",\"phone\":\"987654321\",\"table0\":{#table0是用户在界面上填写的表格名称#labels是用户在界面上填写的，表格每一列的名称,values是每一行的值#一行的数组和labels是一一对应的关系“labels\":[\"brand\",\"model\",\"name\",\"quantity\",\"standard\",\"total\"]\"values\":[[\"Iphone\",\"8p\",\"手机\",\"3\",\"128G\",\"15000rmb\"],[\"戴森\",\"V8\",\"吸尘器\",\"1\",\"Absolute\",\"3500元\"],[\"\",\"\",\"\",\"\",\"\",\"\"],[\"\",\"\",\"\",\"\",\"\",\"\"],[\"\",\"\",\"\",\"\",\"\",\"\"]]}}}";
        String bodys = getParam2(base64Str);
        //System.out.println("bodys:"+bodys);
        String jsonStr = null;
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            //System.out.println("准备打印response");
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            // System.out.println(response.toString());
            //System.out.println(EntityUtils.toString(response.getEntity()));
            jsonStr = EntityUtils.toString(response.getEntity());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            System.out.println("----------异常----------");
        }
        return jsonStr;
    }

    private static String getParam2(String imagePath){
        List<String> list = new ArrayList<>();
        //模板id，现在是陈奇的账号
        list.add(templatecode);
        template_list templatelist = template_list.builder()
                .template_list(list)
                .build();

        Dto dto = Dto.builder().image(imagePath)
                .configure(templatelist)
                .build();
        String s = JSON.toJSONString(dto);
        return s;

    }

    //1. java遍历文件夹下的所有文件，并且返回一个集合
    public static List<File> getAllFile(String path){
        File file = new File(path);
        List<File> list = new ArrayList<File>();
        func(file,list);
        System.out.println(list.size());
        return list;
    }

    //递归种不能使用return，在外面new一个集合，传入即可
    public  static void func(File file,List list){
        File[] fs = file.listFiles();
        for(File f:fs){
            if(f.isDirectory()){
                func(f,list);//如果是文件夹就递归
            }
            if(f.isFile()){
                list.add(f);//如果是文件就添加到list
            }
        }
    }

    /**
     * 本地图片转换Base64的方法
     * @param imgPath
     */
    public static String ImageToBase64(String imgPath) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(Objects.requireNonNull(data));
    }

}
