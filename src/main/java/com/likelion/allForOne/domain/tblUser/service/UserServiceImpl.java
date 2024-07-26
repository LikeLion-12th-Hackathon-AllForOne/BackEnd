package com.likelion.allForOne.domain.tblUser.service;

import com.likelion.allForOne.domain.tblCode.TblCodeRepository;
import com.likelion.allForOne.domain.tblUser.TblUserRepository;
import com.likelion.allForOne.entity.TblCode;
import com.likelion.allForOne.entity.TblUser;
import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.global.response.resEnum.ErrorCode;
import com.likelion.allForOne.global.response.resEnum.SuccessCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.likelion.allForOne.domain.tblUser.dto.UserRequestDto.*;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final TblUserRepository userRepository;
    private final TblCodeRepository codeRepository;

    /**
     * 회원가입
     * @param joinDto
     * @return ApiResponse<?>
     */
    @Override
    @Transactional
    public ApiResponse<?> join(JoinDto joinDto) {
        if (!joinDto.getCodeMbti().isEmpty()) {
            TblCode codeMbti = codeRepository.findByCodeSeq(Long.valueOf(joinDto.getCodeMbti()));
            if (codeMbti == null) return ApiResponse.ERROR(ErrorCode.CODE_NOT_FOUND);

            // 회원 생성
            TblUser user = TblUser.builder()
                    .userId(joinDto.getUserId())
                    .userPwd(joinDto.getUserPwd())
                    .userName(joinDto.getUserName())
                    .userBirth(joinDto.getUserBirth())
                    .userPhone(joinDto.getUserPhone())
                    .userImg(joinDto.getUserImg())
                    .codeMbti(codeMbti)
                    .build();
            userRepository.save(user);

            return ApiResponse.SUCCESS(SuccessCode.CREATE_USER);
        } else return ApiResponse.ERROR(ErrorCode.INVALID_PARAMETER);
    }

    /**
     * ID 중복체크
     * @param userId
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> checkIdDuplicate(String userId) {
        TblUser user = userRepository.findByUserId(userId);
        if (user != null) return ApiResponse.ERROR(ErrorCode.ALREADY_EXISTING);
        else return ApiResponse.SUCCESS(SuccessCode.ID_AVAILABLE);
    }

    /**
     * 로그인
     * @param loginDto
     * @param session
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> login(LoginDto loginDto, HttpSession session) {
        if (loginDto != null) {
            // 사용자 조회
            TblUser user = userRepository.findByUserId(loginDto.getUserId());
            if (user == null) {
                log.error("로그인 실패 : 존재하지 않는 사용자, 사용자 ID {}", loginDto.getUserId());
                return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);  // 존재하지 않는 사용자인 경우 Error
            }

            // 비밀번호 일치하는지 확인
            if (user.getUserPwd().equals(loginDto.getUserPwd())) {
                // 세션값 저장
                session.setAttribute("userSeq", user.getUserSeq());
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userName", user.getUserName());
                session.setAttribute("userImg", user.getUserImg());
                session.setMaxInactiveInterval(30*60); // 30분 동안 세션 유지, 이후 자동 로그아웃

                log.info("로그인 성공, 사용자 ID {}", loginDto.getUserId());
                return ApiResponse.SUCCESS(SuccessCode.LOGIN_SUCCESS);
            } else {
                log.error("로그인 실패 : 아이디, 비밀번호 불일치, 사용자 ID {}", loginDto.getUserId());
                return ApiResponse.ERROR(ErrorCode.LOGIN_FAIL); // 비밀번호 불일치 시 로그인 실패
            }
        } else {
            log.error("로그인 실패 : LoginDto가 비어 있습니다.");
            return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND); // LoginDto 값 비어 있을 때
        }
    }

    /**
     * 로그아웃
     * @param session
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> logout(HttpSession session) {
        // 세션 무효화
        session.invalidate();
        return ApiResponse.SUCCESS(SuccessCode.LOGOUT_SUCCESS);
    }
}
