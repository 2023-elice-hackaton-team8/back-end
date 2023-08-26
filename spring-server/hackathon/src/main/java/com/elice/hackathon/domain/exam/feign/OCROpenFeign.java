package com.elice.hackathon.domain.exam.feign;

import com.elice.hackathon.domain.exam.dto.request.OcrReqDto;
import com.elice.hackathon.domain.exam.dto.response.OcrResDto;
import com.elice.hackathon.domain.exam.dto.response.OpenFeignTestResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name="OCROpenFeign",
        url = "${exchange.currency.api.uri}"
)
public interface OCROpenFeign {

    @PostMapping("/ocr")
    public String ocrFeign(@RequestBody OcrReqDto ocrReqDto);
}
