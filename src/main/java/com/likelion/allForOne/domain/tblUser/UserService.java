package com.likelion.allForOne.domain.tblUser;

import com.likelion.allForOne.domain.CodeRepository;
import com.likelion.allForOne.entity.tblCode;
import com.likelion.allForOne.entity.tblUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.likelion.allForOne.domain.tblUser.UserRequestDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CodeRepository codeRepository;

    // 회원가입
    public UserResponseDto join(UserJoinRequestDto userJoinRequestDto) {
        // Todo.예외처리 수정 예정
        try {
            tblCode codeMbti = codeRepository.findByCodeSeq(Long.valueOf(userJoinRequestDto.getCodeMbti()));

            // 회원 객체 생성
            tblUser user = tblUser.builder()
                    .userId(userJoinRequestDto.getUserId())
                    .userPwd(userJoinRequestDto.getUserPwd())
                    .userName(userJoinRequestDto.getUserName())
                    .userBirth(userJoinRequestDto.getUserBirth())
                    .userPhone(userJoinRequestDto.getUserPhone())
                    .userImg(userJoinRequestDto.getUserImg())
                    .codeMbti(codeMbti)
                    .build();
            userRepository.save(user);

            return UserResponseDto.UserJoinResponseDto(user);
        } catch (Exception e) {
            log.error("join Exception : ", e);
            throw  e;
        }
    }
}
