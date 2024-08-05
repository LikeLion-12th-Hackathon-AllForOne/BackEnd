package com.likelion.allForOne.domain.tblLetter.service;

import com.likelion.allForOne.domain.tblCode.TblCodeRepository;
import com.likelion.allForOne.domain.tblGroup.TblGroupRepository;
import com.likelion.allForOne.domain.tblGroupMember.TblGroupMemberRepository;
import com.likelion.allForOne.domain.tblLetter.TblLetterRepository;
import com.likelion.allForOne.domain.tblLetter.dto.LetterRequestDto;
import com.likelion.allForOne.domain.tblLetter.dto.LetterRequestDto.*;
import com.likelion.allForOne.domain.tblLetter.dto.LetterResponseDto;
import com.likelion.allForOne.domain.tblLetterPackage.TblLetterPackageRepository;
import com.likelion.allForOne.domain.tblLetterPaper.LetterPaperResponseDto;
import com.likelion.allForOne.domain.tblLetterPaper.TblLetterPaperRepository;
import com.likelion.allForOne.domain.tblUser.TblUserRepository;
import com.likelion.allForOne.entity.*;
import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.global.response.resEnum.ErrorCode;
import com.likelion.allForOne.global.response.resEnum.SuccessCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService{
    private final TblUserRepository userRepository;
    private final TblLetterRepository letterRepository;
    private final TblLetterPaperRepository letterPaperRepository;
    private final TblGroupMemberRepository groupMemberRepository;
    private final TblCodeRepository codeRepository;
    private final TblGroupRepository groupRepository;
    private final TblLetterPackageRepository letterPackageRepository;

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

                    // 그룹 멤버 조회 1 - memberTo
                    Optional<TblGroupMember> memberTo = groupMemberRepository.findByMemberSeq(Long.valueOf(createLetterDto.getMember_to()));
                    if (!memberTo.isPresent()) {
                        log.info("그룹 멤버 (To) 조회 실패, 사용자 Seq {}", createLetterDto.getMember_to());
                        return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
                    }

                    // 그룹 멤버 조회 2 - memberFrom
                    Optional<TblGroupMember> memberFrom = groupMemberRepository.findByMemberSeq(Long.valueOf(createLetterDto.getMember_from()));
                    if (!memberFrom.isPresent()) {
                        log.info("그룹 멤버 (From) 조회 실패, 사용자 Seq {}", createLetterDto.getMember_from());
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

                        // 편지 보따리 추가
                        Long groupSeq = Long.valueOf(createLetterDto.getGroup_seq());
                        Optional<TblLetterPackage> letterPackageInfo = letterPackageRepository.findByGroup_GroupSeq(groupSeq);

                        int packageCnt = letterPackageInfo.get().getPackageCnt() + 1; // 편지 보따리 개수 +1

                        TblLetterPackage letterPackage = TblLetterPackage.builder()
                                .packageSeq(letterPackageInfo.get().getPackageSeq())
                                .packageObjective(letterPackageInfo.get().getPackageObjective())
                                .packageCnt(packageCnt)
                                .packageStartDate(letterPackageInfo.get().getPackageStartDate())
                                .group(letterPackageInfo.get().getGroup())
                                .build();
                        letterPackageRepository.save(letterPackage);
                        log.info("편지보따리 추가 완료");
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

    /**
     * 편지 정보 조회
     * @param searchLetterInfo
     * @param session
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> searchLetterInfo(SearchLetterInfo searchLetterInfo, HttpSession session) {
        if (session != null) {
            if (session.getAttribute("userId") != null) {
                String letterFrom = session.getAttribute("userId").toString(); // 보내는 사람 ID
                String letterTo = searchLetterInfo.getLetter_to();                // 받는 사람 ID
                String code_paper = searchLetterInfo.getCode_paper();

                // 코드 값 조회 (유료 / 무료 코드 값)
                TblCode codePaper = codeRepository.findByCodeSeq(Long.valueOf(code_paper));
                if (codePaper == null) return ApiResponse.ERROR(ErrorCode.CODE_NOT_FOUND);

                // 무료 편지지만 조회
                List<LetterPaperResponseDto.searchLetterPaperInfo> letterInfo = new ArrayList<>();
                List<TblLetterPaper> letterPaper = letterPaperRepository.findByCodePaper(codePaper);
                if (letterPaper == null) return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
                for (TblLetterPaper entity : letterPaper) {
                    letterInfo.add(LetterPaperResponseDto.searchLetterPaperInfo.builder()
                            .paperSeq(entity.getPaperSeq())
                            .paperFileName(entity.getPaperFileName())
                            .build());
                }

                LetterResponseDto.searchLetterInfo result = LetterResponseDto.searchLetterInfo.builder()
                        .letterFrom(letterFrom)
                        .letterTo(letterTo)
                        .letterPaper(letterInfo)
                        .build();

                log.info("편지 정보 조회 완료, To > {}, From > {}", letterTo, letterFrom);

                // 조회 결과 리턴
                return ApiResponse.SUCCESS(SuccessCode.FOUND_IT, result);
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
     * 편지함 조회
     * @param searchLetterList
     * @param session
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> searchLetterList(SearchLetterList searchLetterList, HttpSession session) {
        if (session != null) {
            if (session.getAttribute("userId") != null) {
                String userId = session.getAttribute("userId").toString();
                List<TblLetter> letterInfo;
                int memberFrom = 0;
                int memberTo = 0;

                // 보낸 사람 값 여부 확인
                if (searchLetterList.getMember_from() != null && searchLetterList.getMember_from() != "") memberFrom = Integer.parseInt(searchLetterList.getMember_from());

                // 받는 사람 값 여부 확인
                if (searchLetterList.getMember_to() != null && searchLetterList.getMember_to() != "") memberTo = Integer.parseInt(searchLetterList.getMember_to());

                // 편지함 조회
                if (memberFrom == 0 && memberTo != 0) {
                    // 받은 편지함 조회
                    log.info("받은 편지함 조회");
                    letterInfo = letterRepository.searchTblLetterByMemberToAndLetterTo(memberTo, userId);
                } else if (memberFrom != 0 && memberTo == 0){
                    // 보낸 편지함 조회
                    log.info("보낸 편지함 조회");
                    letterInfo = letterRepository.searchTblLetterByMemberFromAndLetterFrom(memberFrom, userId);
                } else {
                    log.info("memberFrom, memberTo 값 모두 없음, 편지함 조회 실패");
                    return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
                }

                if (letterInfo == null) {
                    log.info("letterInfo null, 편지함 조회 실패");
                    return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
                }

                // 편지함에서 조회한 값 dto로 변경
                List<SearchLetter> letterList = new ArrayList<>();
                for (TblLetter letter : letterInfo) {
                    letterList.add(SearchLetter.builder()
                            .letter_seq(letter.getLetterSeq())
                            .letter_to(letter.getLetterTo())
                            .letter_from(letter.getLetterFrom())
                            .letter_contents(letter.getLetterContents())
                            .letter_read(String.valueOf(letter.getLetterRead()))
                            .member_to(letter.getMemberTo().getMemberSeq().toString())
                            .member_from(letter.getMemberFrom().getMemberSeq().toString())
                            .paper_seq(letter.getPaperSeq().getPaperSeq().toString())
                            .build());
                }

                LetterResponseDto.searchLetterList result = LetterResponseDto.searchLetterList.builder()
                        .letterList(letterList)
                        .build();

                log.info("편지함 조회 완료, 사용자 ID {}", userId);

                // 조회 결과 리턴
                return ApiResponse.SUCCESS(SuccessCode.FOUND_IT, result);
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
     * 읽음 처리
     * @param updateReadLetter
     * @param session
     * @return ApiResponse<?>
     */
    @Override
    @Transactional
    public ApiResponse<?> updateReadLetter(UpdateReadLetter updateReadLetter, HttpSession session) {
        if (session != null) {
            if (session.getAttribute("userId") != null) {
                Long letterSeq = updateReadLetter.getLetter_seq();

                // 읽음 처리
                Optional<TblLetter> letterEntity = letterRepository.findById(letterSeq);

                TblLetter letter = TblLetter.builder()
                        .letterSeq(letterEntity.get().getLetterSeq())
                        .letterTo(letterEntity.get().getLetterTo())
                        .letterFrom(letterEntity.get().getLetterFrom())
                        .letterContents(letterEntity.get().getLetterContents())
                        .letterRead(1)
                        .memberTo(letterEntity.get().getMemberTo())
                        .memberFrom(letterEntity.get().getMemberFrom())
                        .paperSeq(letterEntity.get().getPaperSeq())
                        .build();
                letterRepository.save(letter);

                log.info("읽음 처리 완료, letterSeq {}", letterSeq);
                return ApiResponse.SUCCESS(SuccessCode.UPDATE_READ_LETTER);
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