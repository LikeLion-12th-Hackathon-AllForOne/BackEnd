package com.likelion.allForOne.tblCode.service;

import com.likelion.allForOne.global.response.ApiResponse;

public interface CodeService {
    ApiResponse<?> findListSelectList(String codeName);  // 공통코드 select box 리스트 조회
}
