package com.elice.hackathon.domain.exam.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetSubTitleResDto {

    String[] titles;
    public static GetSubTitleResDto of(String[] titles){
        return GetSubTitleResDto.builder()
                .titles(titles)
                .build();
    }
}
