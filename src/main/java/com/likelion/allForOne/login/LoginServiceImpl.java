package com.likelion.allForOne.login;

import com.likelion.allForOne.domain.tblCode.TblCodeRepository;
import com.likelion.allForOne.domain.tblUser.TblUserRepository;
import com.likelion.allForOne.domain.tblUser.dto.UserRequestDto;
import com.likelion.allForOne.entity.TblUser;
import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.global.response.resEnum.ErrorCode;
import com.likelion.allForOne.global.response.resEnum.SuccessCode;
import com.likelion.allForOne.login.dto.TokenDto;
import com.likelion.allForOne.login.dto.UserDto;
import com.likelion.allForOne.login.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl {
    private final TblUserRepository userRepository;
    private final TblCodeRepository codeRepository;
    @Value("${jwt.secret.ACCESS_TOKEN_KEY}")
    private String accessTokenKey;
    @Value("${jwt.secret.REFRESH_TOKEN_KEY}")
    private String refreshTokenKey;
    private Long accessExpireTimeMs = 1000*60*60*24L; // 24시간
    private Long refreshExpireTimeMs = 1000*60*60*24*30L; // 30일

    /**
     * 카카오 로그인 (최초)
     * @param loginDto LoginDto
     * @return ApiResponse<TokenDto.responseDto>
     */
    @Transactional
    public ApiResponse<?> firstLogin(UserRequestDto.LoginDto loginDto){
        if (loginDto != null) {
            // 1. 사용자 조회
            Optional<TblUser> userOpt = Optional.ofNullable(userRepository.findByUserId(loginDto.getUserId()));
            if(userOpt.isPresent()) {
                TblUser login = userOpt.get();
                // 2. 비밀번호 일치하는지 확인
                if (login.getUserPwd().equals(loginDto.getUserPwd())) {
                    // 3. entity -> dto 로 데이터 담기
                    UserDto userDto = UserDto.builder()
                            .userSeq(login.getUserSeq())
                            .userName(login.getUserName())
                            .userId(login.getUserId())
                            .userImg(login.getUserImg())
                            .build();

                    // 4. 1 또는 2에서 발급받은 데이터를 통해 JWT 생성 및 반환
                    String accessToken = JwtUtil.createAccessToken(userDto, accessTokenKey, accessExpireTimeMs);
                    String refreshToken = JwtUtil.createRefreshToken(userDto, refreshTokenKey, refreshExpireTimeMs);
                    login.updateRefreshToken(refreshToken);

                    // TokenDto 데이터 담아서 전달
                    return ApiResponse.SUCCESS(SuccessCode.LOGIN_SUCCESS,
                            TokenDto.responseDto.builder()
                                    .nickname(userDto.getUserName())
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .build());
                } else {
                    log.error("로그인 실패 : 아이디, 비밀번호 불일치, 사용자 ID {}", loginDto.getUserId());
                    return ApiResponse.ERROR(ErrorCode.LOGIN_FAIL); // 비밀번호 불일치 시 로그인 실패
                }
            } else {
                log.error("로그인 실패 : 존재하지 않는 사용자, 사용자 ID {}", loginDto.getUserId());
                return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);  // 존재하지 않는 사용자인 경우 Error
            }
        } else {
            log.error("로그인 실패 : LoginDto가 비어 있습니다.");
            return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND); // LoginDto 값 비어 있을 때
        }
    }
}
