package com.likelion.allForOne.domain.tblLetter.service;

import com.likelion.allForOne.domain.tblGroupMember.TblGroupMemberRepository;
import com.likelion.allForOne.domain.tblLetter.TblLetterRepository;
import com.likelion.allForOne.domain.tblLetter.dto.LetterRequestDto.*;
import com.likelion.allForOne.domain.tblLetterPaper.TblLetterPaperRepository;
import com.likelion.allForOne.domain.tblUser.TblUserRepository;
import com.likelion.allForOne.entity.TblGroupMember;
import com.likelion.allForOne.entity.TblLetter;
import com.likelion.allForOne.entity.TblLetterPaper;
import com.likelion.allForOne.entity.TblUser;
import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.global.response.resEnum.ErrorCode;
import com.likelion.allForOne.global.response.resEnum.SuccessCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService{
    private final TblUserRepository userRepository;
    private final TblLetterRepository letterRepository;
    private final TblLetterPaperRepository letterPaperRepository;
    private final TblGroupMemberRepository groupMemberRepository;

    /**
     * 편지 등록
     * @param createLetterDto
     * @return ApiResponse<?>
     */
    @Override
    @Transactional
    public ApiResponse<?> createLetter(CreateLetterDto createLetterDto, HttpSession session) {
        if (session != null) {
            if (session.getAttribute("userId") != null) {
                Long userSeq = (Long) session.getAttribute("userSeq");
                String userId = session.getAttribute("userId").toString();

                if (!userId.isEmpty() || userId != null) {
                    // 사용자 조회
                    Optional<TblUser> user = Optional.ofNullable(userRepository.findByUserId(userId));

                    // 편지지 코드 조회
                    Optional<TblLetterPaper> letterPaper = letterPaperRepository.findById(Long.valueOf(createLetterDto.getPaper_seq()));
                    if (!letterPaper.isPresent()) {
                        log.info("편지지 코드 조회 실패, 편지지 코드 {}", createLetterDto.getLetter_seq());
                        return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
                    }

                    // memberTo 사용자 조회
                    Optional<TblUser> userTo = Optional.ofNullable(userRepository.findByUserId(createLetterDto.getLetter_to()));
                    if (!userTo.isPresent()) {
                        log.info("사용자 조회 실패, 사용자 ID {}", createLetterDto.getLetter_to());
                        return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
                    }

                    // 그룹 멤버 조회 1 - memberTo
                    Optional<TblGroupMember> memberTo = groupMemberRepository.findById(userTo.get().getUserSeq());
                    if (!memberTo.isPresent()) {
                        log.info("그룹 멤버 (To) 조회 실패, 사용자 Seq {}", userTo.get().getUserSeq());
                        return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
                    }

                    // 그룹 멤버 조회 2 - memberFrom
                    Optional<TblGroupMember> memberFrom = groupMemberRepository.findById(userSeq);
                    if (!memberFrom.isPresent()) {
                        log.info("그룹 멤버 (From) 조회 실패, 사용자 Seq {}", userSeq);
                        return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
                    }

                    if (user.isPresent()) {
                        // 편지 등록
                        TblLetter letter = TblLetter.builder()
                                .letterTo(createLetterDto.getLetter_to())
                                .letterFrom(userId)
                                .letterContents(createLetterDto.getLetter_contents())
                                .letterRead(0) // 안 읽음(0), 읽음(1)
                                .memberTo(memberTo.get())
                                .memberFrom(memberFrom.get())
                                .paperSeq(letterPaper.get())
                                .build();
                        letterRepository.save(letter);

                        log.info("편지 등록 완료, 사용자 ID {}", userId);
                        return ApiResponse.SUCCESS(SuccessCode.CREATE_LETTER);
                    } else return ApiResponse.ERROR(ErrorCode.CREATE_LETTER_FAIL);
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
}