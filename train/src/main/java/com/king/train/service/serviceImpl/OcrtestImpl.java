package com.king.train.service.serviceImpl;


import com.alibaba.fastjson.JSON;
import com.king.train.dao.JkFailMapper;
import com.king.train.dao.JkSuccessMapper;
import com.king.train.dto.ExportOut;
import com.king.train.entities.JkSuccessPo;
import com.king.train.service.Ocrtest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @PackageName:com.example.demo.service
 * @ClassName:testImpl
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2021/3/25 10:44
 */
@Service
public class OcrtestImpl implements Ocrtest {

    @Autowired
    JkSuccessMapper successMapper;

    @Autowired
    JkFailMapper failMapper;

    @Override
    public int save(String fileName , List<ExportOut> exportOutList) {
       return successMapper.insert(JkSuccessPo.builder().name(fileName).detail(JSON.toJSONString(exportOutList)).build());
    }
}
