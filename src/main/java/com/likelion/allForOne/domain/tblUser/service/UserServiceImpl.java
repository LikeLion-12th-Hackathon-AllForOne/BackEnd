package com.likelion.allForOne.domain.tblUser.service;

import com.likelion.allForOne.domain.tblCode.TblCodeRepository;
import com.likelion.allForOne.domain.tblGroupMember.TblGroupMemberRepository;
import com.likelion.allForOne.domain.tblUser.TblUserRepository;
import com.likelion.allForOne.domain.tblUser.dto.UserResponseDto;
import com.likelion.allForOne.entity.TblCode;
import com.likelion.allForOne.entity.TblUser;
import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.global.response.resEnum.ErrorCode;
import com.likelion.allForOne.global.response.resEnum.SuccessCode;
import com.likelion.allForOne.login.utils.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.likelion.allForOne.domain.tblUser.dto.UserRequestDto.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final TblUserRepository userRepository;
    private final TblCodeRepository codeRepository;
    private final TblGroupMemberRepository groupMemberRepository;

    /**
     * 회원가입
     * @param joinDto
     * @return ApiResponse<?>
     */
    @Override
    @Transactional
    public ApiResponse<?> join(JoinDto joinDto) {
        if (!joinDto.getCodeMbti().isEmpty()) {
            // 코드 값 조회
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

            log.info("회원가입 완료, 사용자 ID {}", user.getUserId());
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
        // 사용자 조회
        Optional<TblUser> user = Optional.ofNullable(userRepository.findByUserId(userId));

        if (user.isPresent()) {
            log.info("이미 존재하는 ID, 사용자 ID {}", user.get().getUserId());
            return ApiResponse.ERROR(ErrorCode.ALREADY_EXISTING);
        } else {
            log.info("사용 가능한 ID 입니다.");
            return ApiResponse.SUCCESS(SuccessCode.ID_AVAILABLE);
        }
    }

    /**
     * 비밀번호 확인
     * @param checkPwdDto
     * @param authentication
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> checkPwd(CheckPwdDto checkPwdDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (userDetails != null) {
            if (userDetails.getUserId() != null) {
                // 사용자 조회
                Optional<TblUser> user = Optional.ofNullable(userRepository.findByUserId(userDetails.getUserId()));

                if (user.isPresent()) {
                    // 비밀번호 일치 확인
                    if (user.get().getUserPwd().equals(checkPwdDto.getUserPwd())) {
                        log.info("비밀번호가 일치합니다.");
                        return ApiResponse.SUCCESS(SuccessCode.PASSWORD_CORRECT);
                    } else {
                        log.error("비밀번호가 일치하지 않습니다.");
                        return ApiResponse.ERROR(ErrorCode.PASSWORD_INCORRECT);
                    }
                } else return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
            } else {
                log.error("세션이 만료되었습니다.");
                return ApiResponse.ERROR(ErrorCode.SESSION_EXPIRED);
            }
        } else {
            log.error("세션이 만료되었습니다.");
            return ApiResponse.ERROR(ErrorCode.SESSION_EXPIRED);
        }
    }

    /**
     * 내 정보 조회
     * @param authentication
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> searchUserInfo(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (userDetails != null) {
            if (userDetails.getUserId() != null) {
                // 사용자 조회
                Optional<TblUser> user = Optional.ofNullable(userRepository.findByUserId(userDetails.getUserId()));
                if (user.isPresent()) {
                    log.info("회원정보 조회 완료, 사용자 ID {}", user.get().getUserId());

                    // 조회 결과 리턴
                    return ApiResponse.SUCCESS(
                            SuccessCode.FOUND_IT,
                            UserResponseDto.searchUserInfo.builder()
                                    .userSeq(user.get().getUserSeq())
                                    .userId(user.get().getUserId())
                                    .userName(user.get().getUserName())
                                    .userBirth(user.get().getUserBirth())
                                    .userPhone(user.get().getUserPhone())
                                    .userImg(user.get().getUserImg())
                                    .codeMbti(user.get().getCodeMbti().getCodeName())
                                    .build()
                    );
                } else return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
            } else {
                log.error("세션이 만료되었습니다.");
                return ApiResponse.ERROR(ErrorCode.SESSION_EXPIRED);
            }
        } else {
            log.error("세션이 만료되었습니다.");
            return ApiResponse.ERROR(ErrorCode.SESSION_EXPIRED);
        }
    }

    /**
     * 내 정보 수정
     * @param updateUserInfo
     * @param authentication
     * @return ApiResponse<?>
     */
    @Override
    @Transactional
    public ApiResponse<?> updateUserInfo(@RequestBody UpdateUserInfo updateUserInfo, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (userDetails != null) {
            if (userDetails.getUserId() != null) {
                // 사용자 조회
                Optional<TblUser> user = Optional.ofNullable(userRepository.findByUserId(userDetails.getUserId()));

                if (user.isPresent()) {
                    // 코드 값 조회
                    TblCode codeMbti = codeRepository.findByCodeSeq(Long.valueOf(updateUserInfo.getCodeMbti()));
                    if (codeMbti == null) return ApiResponse.ERROR(ErrorCode.CODE_NOT_FOUND);

                    // 변경된 사용자 정보 수정
                    TblUser updateUser = TblUser.builder()
                            .userSeq(user.get().getUserSeq())
                            .userId(user.get().getUserId())
                            .userPwd(updateUserInfo.getUserPwd())
                            .userName(updateUserInfo.getUserName())
                            .userBirth(updateUserInfo.getUserBirth())
                            .userPhone(updateUserInfo.getUserPhone())
                            .userImg(updateUserInfo.getUserImg())
                            .codeMbti(codeMbti)
                            .build();
                    userRepository.save(updateUser);

                    log.info("회원정보 수정 완료, 사용자 ID {}", user.get().getUserId());
                    return ApiResponse.SUCCESS(SuccessCode.UPDATE_USER_INFO);
                } else return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
            } else {
                log.error("세션이 만료되었습니다.");
                return ApiResponse.ERROR(ErrorCode.SESSION_EXPIRED);
            }
        } else {
            log.error("세션이 만료되었습니다.");
            return ApiResponse.ERROR(ErrorCode.SESSION_EXPIRED);
        }
    }

    /**
     * 회원 탈퇴
     * @param authentication
     * @return ApiResponse<?>
     */
    @Override
    @Transactional
    public ApiResponse<?> deleteUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (userDetails != null) {
            if (userDetails.getUserId() != null) {
                // 사용자 조회
                Optional<TblUser> user = Optional.ofNullable(userRepository.findByUserId(userDetails.getUserId()));

                if (user.isPresent()) {
                    // 사용자가 속해있는 그룹 멤버 삭제
                    groupMemberRepository.deleteByUser_UserSeq(user.get().getUserSeq());
                    log.info("사용자가 속해있는 그룹 멤버 삭제");

                    // 사용자 삭제
                    userRepository.deleteById(user.get().getUserSeq());
                    
                    log.info("회원 탈퇴 완료, 사용자 ID {}", userDetails.getUserId());
                    return ApiResponse.SUCCESS(SuccessCode.DELETE_USER);
                } else return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
            } else {
                log.error("세션이 만료되었습니다.");
                return ApiResponse.ERROR(ErrorCode.SESSION_EXPIRED);
            }
        } else {
            log.error("세션이 만료되었습니다.");
            return ApiResponse.ERROR(ErrorCode.SESSION_EXPIRED);
        }
    }

    /**
     * 사용자 프로필 변경
     * @param updateUserImage
     * @param authentication
     * @return
     */
    @Override
    @Transactional
    public ApiResponse<?> updateUserImage(UpdateUserImage updateUserImage, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (userDetails != null) {
            if (userDetails.getUserId() != null) {
                String userImg = updateUserImage.getUserImg();

                // 사용자 조회
                Optional<TblUser> user = Optional.ofNullable(userRepository.findByUserId(userDetails.getUserId()));

                // 사용자 프로필 변경
                TblUser updateUser = TblUser.builder()
                        .userSeq(user.get().getUserSeq())
                        .userId(user.get().getUserId())
                        .userPwd(user.get().getUserPwd())
                        .userName(user.get().getUserName())
                        .userBirth(user.get().getUserBirth())
                        .userPhone(user.get().getUserPhone())
                        .userImg(userImg)
                        .codeMbti(user.get().getCodeMbti())
                        .build();
                userRepository.save(updateUser);

                log.info("사용자 프로필 변경되었습니다. IMG 명 {}", userImg);
                return ApiResponse.SUCCESS(SuccessCode.UPDATE_USER_IMG);
            } else {
                log.error("세션이 만료되었습니다.");
                return ApiResponse.ERROR(ErrorCode.SESSION_EXPIRED);
            }
        } else {
            log.error("세션이 만료되었습니다.");
            return ApiResponse.ERROR(ErrorCode.SESSION_EXPIRED);
        }
    }
}
