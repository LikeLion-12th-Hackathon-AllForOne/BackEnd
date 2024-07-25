package com.likelion.allForOne.domain.tblUser.service;

import com.likelion.allForOne.domain.tblCode.TblCodeRepository;
import com.likelion.allForOne.domain.tblUser.TblUserRepository;
import com.likelion.allForOne.domain.tblUser.dto.UserResponseDto;
import com.likelion.allForOne.entity.TblCode;
import com.likelion.allForOne.entity.TblUser;
import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.global.response.CustomException;
import com.likelion.allForOne.global.response.resEnum.ErrorCode;
import com.likelion.allForOne.global.response.resEnum.SuccessCode;
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
     * @param userJoinRequestDto
     * @return ApiResponse<?>
     */
    @Override
    @Transactional
    public ApiResponse<?> join(UserJoinRequestDto userJoinRequestDto) {
        if (!userJoinRequestDto.getCodeMbti().isEmpty()) {
            TblCode codeMbti = codeRepository.findByCodeSeq(Long.valueOf(userJoinRequestDto.getCodeMbti()));
            if (codeMbti == null) return ApiResponse.ERROR(ErrorCode.CODE_NOT_FOUND);

            // 회원 생성
            TblUser user = TblUser.builder()
                    .userId(userJoinRequestDto.getUserId())
                    .userPwd(userJoinRequestDto.getUserPwd())
                    .userName(userJoinRequestDto.getUserName())
                    .userBirth(userJoinRequestDto.getUserBirth())
                    .userPhone(userJoinRequestDto.getUserPhone())
                    .userImg(userJoinRequestDto.getUserImg())
                    .codeMbti(codeMbti)
                    .build();
            userRepository.save(user);

            return ApiResponse.SUCCESS(SuccessCode.CREATE_USER);
        } else return ApiResponse.ERROR(ErrorCode.INVALID_PARAMETER);
    }
}
