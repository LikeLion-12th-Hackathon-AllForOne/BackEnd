package com.likelion.allForOne.domain.tblUser.service;

import com.likelion.allForOne.domain.tblUser.dto.UserRequestDto;
import com.likelion.allForOne.global.response.ApiResponse;

public interface UserService {
    ApiResponse<?> join(UserRequestDto.UserJoinRequestDto userJoinRequestDto); // 회원가입
}
