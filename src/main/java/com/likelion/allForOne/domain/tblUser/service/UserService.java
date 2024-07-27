package com.likelion.allForOne.domain.tblUser.service;

import com.likelion.allForOne.domain.tblUser.dto.UserRequestDto.*;
import com.likelion.allForOne.global.response.ApiResponse;
import jakarta.servlet.http.HttpSession;

public interface UserService {
    ApiResponse<?> join(JoinDto joinDto); // 회원가입

    ApiResponse<?> checkIdDuplicate(String userId); // ID 중복체크

    ApiResponse<?> login(LoginDto loginDto, HttpSession session); // 로그인

    ApiResponse<?> logout(HttpSession session); // 로그아웃

    ApiResponse<?> checkPwd(CheckPwdDto checkPwdDto, HttpSession session); // 비밀번호 확인

    ApiResponse<?> searchUserInfo(HttpSession session); // 내 정보 조회
    
    ApiResponse<?> updateUserInfo(UpdateUserInfo updateUserInfo, HttpSession session); // 내 정보 수정
    
    ApiResponse<?> deleteUser(HttpSession session); // 회원 탈퇴
}
