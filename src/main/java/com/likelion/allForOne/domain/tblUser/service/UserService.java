package com.likelion.allForOne.domain.tblUser.service;

import com.likelion.allForOne.domain.tblUser.dto.UserRequestDto.*;
import com.likelion.allForOne.global.response.ApiResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;

public interface UserService {
    ApiResponse<?> join(JoinDto joinDto); // 회원가입

    ApiResponse<?> checkIdDuplicate(String userId); // ID 중복체크

    ApiResponse<?> checkPwd(CheckPwdDto checkPwdDto, Authentication authentication); // 비밀번호 확인

    ApiResponse<?> searchUserInfo(Authentication authentication); // 내 정보 조회
    
    ApiResponse<?> updateUserInfo(UpdateUserInfo updateUserInfo, Authentication authentication); // 내 정보 수정
    
    ApiResponse<?> deleteUser(Authentication authentication); // 회원 탈퇴

    ApiResponse<?> updateUserImage(UpdateUserImage updateUserImage, Authentication authentication); // 사용자 프로필 변경
}
