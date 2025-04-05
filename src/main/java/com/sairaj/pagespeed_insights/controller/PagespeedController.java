package com.sairaj.pagespeed_insights.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sairaj.pagespeed_insights.model.PageSpeedResult;
import com.sairaj.pagespeed_insights.model.PageSpeedRequest;
import com.sairaj.pagespeed_insights.service.PageSpeedService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;
import java.util.List;

@RestController
@RequestMapping("api/pagespeed")
@CrossOrigin(origins = "http://localhost:3000")
public class PagespeedController {

    @Autowired
    private PageSpeedService pageSpeedService;

    @PostMapping("/analyze")
    public List<PageSpeedResult> analyzeUrls(@RequestBody PageSpeedRequest request) {
        return pageSpeedService.analyzeUrls(request.getUrls());
    }
    
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel() {
        byte[] excelData = pageSpeedService.generateExcelReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("pagespeed_results.xlsx").build());

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }

}