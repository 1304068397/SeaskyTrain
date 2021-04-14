package com.king.train.service;

import com.king.train.dto.ExportOut;

import java.util.List;

public interface Ocrtest {
    int save(String fileName , List<ExportOut> exportOutList);
}
