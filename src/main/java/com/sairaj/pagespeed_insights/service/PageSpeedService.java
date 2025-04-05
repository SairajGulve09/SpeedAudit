package com.sairaj.pagespeed_insights.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sairaj.pagespeed_insights.model.PageSpeedResult;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PageSpeedService {

    private static final String API_KEY = "AIzaSyC3R2NHZLFqJkFSA4eiy25RcBEKW1OOwZU"; 
    private static final String API_URL = "https://www.googleapis.com/pagespeedonline/v5/runPagespeed?url=%s&strategy=mobile&key=" + API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();
    
    private List<PageSpeedResult> recentResults = new ArrayList<>();

    public List<PageSpeedResult> analyzeUrls(List<String> urls) {
        List<PageSpeedResult> results = new ArrayList<>();

        for (String url : urls) {
            try {
                var response = restTemplate.getForObject(String.format(API_URL, url), Object.class);
                var json = (Map<?, ?>) response;
                var lighthouse = (Map<?, ?>) json.get("lighthouseResult");
                var audits = (Map<?, ?>) lighthouse.get("audits");
                var categories = (Map<?, ?>) lighthouse.get("categories");
                var performanceCategory = (Map<?, ?>) categories.get("performance");

                double performance = ((Number) performanceCategory.get("score")).doubleValue() * 100;
                double fcp = ((Number) ((Map<?, ?>) audits.get("first-contentful-paint")).get("numericValue")).doubleValue();
                double lcp = ((Number) ((Map<?, ?>) audits.get("largest-contentful-paint")).get("numericValue")).doubleValue();
                double tbt = ((Number) ((Map<?, ?>) audits.get("total-blocking-time")).get("numericValue")).doubleValue();
                double cls = ((Number) ((Map<?, ?>) audits.get("cumulative-layout-shift")).get("numericValue")).doubleValue();

                results.add(new PageSpeedResult(url, performance, fcp, lcp, tbt, cls));
            } catch (Exception e) {
                // Default to zeros if API fails
                results.add(new PageSpeedResult(url, 0, 0, 0, 0, 0));
            }
        }
        
        recentResults = results;

        return results;
    }

    
    public byte[] generateExcelReport() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("PageSpeed Results");

            // Header row
            Row headerRow = sheet.createRow(0);
            String[] headers = { "URL", "Performance", "FCP", "LCP", "TBT", "CLS" };
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Data rows
            List<PageSpeedResult> results = recentResults;
            for (int i = 0; i < results.size(); i++) {
                PageSpeedResult result = results.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(result.getUrl());
                row.createCell(1).setCellValue(result.getPerformance());
                row.createCell(2).setCellValue(result.getFcp());
                row.createCell(3).setCellValue(result.getLcp());
                row.createCell(4).setCellValue(result.getTbt());
                row.createCell(5).setCellValue(result.getCls());
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }
    
}