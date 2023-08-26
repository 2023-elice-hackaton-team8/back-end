package com.elice.hackathon.domain.exam.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadImageResDto {
    private String imgUrl;

    public static UploadImageResDto of(String imgUrl){
        return UploadImageResDto.builder()
                .imgUrl(imgUrl)
                .build();
    }
}
