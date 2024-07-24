package com.likelion.allForOne.tblGroup.service;

import com.likelion.allForOne.entity.TblCode;
import com.likelion.allForOne.entity.TblGroup;
import com.likelion.allForOne.entity.TblGroupMember;
import com.likelion.allForOne.entity.TblUser;
import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.global.response.CustomException;
import com.likelion.allForOne.global.response.resEnum.ErrorCode;
import com.likelion.allForOne.global.response.resEnum.SuccessCode;
import com.likelion.allForOne.tblCode.service.CodeServiceImpl;
import com.likelion.allForOne.tblGroup.TblGroupRepository;
import com.likelion.allForOne.tblGroup.dto.GroupDto;
import com.likelion.allForOne.tblGroup.dto.GroupRequestDto;
import com.likelion.allForOne.tblGroup.dto.GroupResponseDto;
import com.likelion.allForOne.tblGroupMember.GroupMemberServiceImpl;
import com.likelion.allForOne.tblGroupMember.TblGroupMemberRepository;
import com.likelion.allForOne.tblLetterPackage.LetterPackageServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {
    private final TblGroupRepository groupRepository;
    private final TblGroupMemberRepository groupMemberRepository;
    private final CodeServiceImpl codeService;
    private final LetterPackageServiceImpl letterPackageService;
    private final GroupMemberServiceImpl groupMemberService;

    /**
     * 방(그룹) 생성
     * @param data GroupRequestDto.saveOneGroup:방(그룹) 생성시 필요한 데이터
     * @param userSeq Long:로그인 사용자 Seq
     * @return ApiResponse<?>
     */
    @Override
    @Transactional
    public ApiResponse<?> saveOneGroup(GroupRequestDto.saveOneGroup data, Long userSeq) {
        //1. 유저 정보 확인 (로그인 기능 완료 후 추가) (수정필요)

        //2. 카테고리 코드 구분자 유효성 확인
        boolean codeValidation = codeService.codeValidationCheck("category", data.getCodeCategorySeq());
        if (!codeValidation) return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);

        //3. 인원수 1이상 6이하
        if (data.getGroupMemberCnt() < 1 || data.getGroupMemberCnt() > 6)
            return ApiResponse.ERROR(ErrorCode.INVALID_PARAMETER);

        //4. 사용자의 프리미엄 구독 여부에 따라 참가한 방(그룹)이 1개를 넘지 못함. (해당 부분은 추후 개발 예정)

        //5. 초대코드 생성
        String inviteCode = createInviteCode();
        Optional<TblGroup> bfGroup = groupRepository.findByGroupInviteCode(inviteCode);
        while(bfGroup.isPresent()){
            inviteCode = createInviteCode();
            bfGroup = groupRepository.findByGroupInviteCode(inviteCode);
        }

        //6. 방(그룹) 생성
        TblGroup entity = TblGroup.builder()
                .groupMemberCnt(data.getGroupMemberCnt())
                .groupName(data.getGroupName())
                .groupInviteCode(inviteCode)
                .codeCategory(TblCode.builder().codeSeq(data.getCodeCategorySeq()).build())
                .userOwner(TblUser.builder().userSeq(userSeq).build())
                .build();
        TblGroup tblGroup = groupRepository.save(entity);

        //7. 선물보따리 생성
        Long packageSeq = letterPackageService.saveLetterPackage(tblGroup);
        if(packageSeq == null) throw new CustomException(ErrorCode.CREATE_FAIL);

        //8. 회원 등록
        Long memberSeq = groupMemberService.saveGroupMember(tblGroup, tblGroup.getUserOwner());
        if(memberSeq == null) throw new CustomException(ErrorCode.CREATE_FAIL);

        //9. return
        return ApiResponse.SUCCESS(SuccessCode.CREATE_GROUP);
    }

    /**
     * 참여 방(그룹) 리스트
     * @param userSeq Long:로그인 사용자 구분자
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> findListJoinGroup(Long userSeq) {
        //1. 사용자 정보 조회 + 결과 값에 userImg, userName 값 변경 작업 필요. (수정필요)

        //2. 사용자가 참가하고 있는 방 리스트 찾기
        List<TblGroupMember> groupListEntity = groupMemberRepository.findByUser_UserSeq(userSeq);
        if (groupListEntity.isEmpty())
            return ApiResponse.SUCCESS(SuccessCode.FOUND_NO_SEARCH_RESULT,
                    GroupResponseDto.findListJoinGroup.builder()
                            .userImg("imgfile.jpg")
                            .userName("관리자")
                            .build());

        //3. entity -> dto 변환
        List<GroupDto.participateInfo> list = new ArrayList<>();
        for(TblGroupMember entity : groupListEntity) {
            //3-1. group entity 조회(지연 로딩으로 추가 검색 필요.)
            Optional<TblGroup> groupOpt = groupRepository.findById(entity.getGroup().getGroupSeq());
            if (groupOpt.isEmpty()) continue;

            TblGroup group = groupOpt.get();
            list.add(GroupDto.participateInfo.builder()
                            .memberSeq(entity.getMemberSeq())
                            .groupName(group.getGroupName())
                            .categoryName(group.getCodeCategory().getCodeName())
                            .ownerName(group.getUserOwner().getUserName())
                            .ownerYn(userSeq.equals(group.getUserOwner().getUserSeq()))
                            .fullPackageYn("미확인".equals(entity.getCodePackage().getCodeName()))
                            .build());
        }

        //4. 조회 결과 반환
        return ApiResponse.SUCCESS(
                SuccessCode.FOUND_IT,
                GroupResponseDto.findListJoinGroup.builder()
                        .userImg("imgfile.jpg")
                        .userName("관리자")
                        .list(list)
                        .build());
    }

    /**
     * 초대코드 조회
     * @param groupSeq Long:그룹 구분자
     * @param userSeq Long:로그인 사용자 구분자
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> findGroupInviteCode(Long groupSeq, Long userSeq) {
        //1. 그룹의 방장 사용자가 로그인 사용자와 일치하는지 확인
        Optional<TblGroup> tblGroup = groupRepository.findByGroupSeqAndUserOwner_UserSeq(groupSeq, userSeq);
        if (tblGroup.isEmpty()) return ApiResponse.ERROR(ErrorCode.UNAUTHORIZED);

        //2. 일치하는 경우, 초대코드 return
        return ApiResponse.SUCCESS(SuccessCode.FOUND_LIST, tblGroup.get().getGroupInviteCode());
    }

    /**
     * 초대코드 입장-회원 등록
     * @param data GroupRequestDto.saveGroupMemberByInviteCode
     * @param userSeq Long:로그인 사용자 구분자
     * @return ApiResponse<?>
     */
    @Override
    @Transactional
    public ApiResponse<?> saveGroupMemberByInviteCode(GroupRequestDto.saveGroupMemberByInviteCode data, Long userSeq) {
        //1. 로그인 사용자 정보 확인 (수정필요)
        TblUser user = TblUser.builder().userSeq(userSeq).build();

        //2. 초대코드로 그룹 조회
        Optional<TblGroup> joinGroupOpt = groupRepository.findByGroupInviteCode(data.getGroupInviteCode());
        if (joinGroupOpt.isEmpty()) return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
        TblGroup joinGroup = joinGroupOpt.get();

        //3. 해당 그룹에 이미 가입된 경우 확인
        boolean checkGroupMember = groupMemberService.checkGroupMember(joinGroup.getGroupSeq(), userSeq);
        if (checkGroupMember) return ApiResponse.ERROR(ErrorCode.ALREADY_EXISTING);

        //4. 그룹 인원수와 멤버 등록된 인원수 확인
        long cntJoinMember = groupMemberService.cntJoinGroupMember(joinGroup.getGroupSeq());
        if (cntJoinMember >= joinGroup.getGroupMemberCnt()) return ApiResponse.ERROR(ErrorCode.ALREADY_FULL);

        //5. 멤버 등록
        Long memberSeq = groupMemberService.saveGroupMember(joinGroup, user);
        if(memberSeq == null) throw new CustomException(ErrorCode.CREATE_FAIL);

        //6. 결과 반환
        return ApiResponse.SUCCESS(SuccessCode.JOIN_MEMBER);
    }

    /**
     * 초대코드 생성
     * @return String:랜덤으로 생성된 초대코드
     */
    private String createInviteCode(){
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int length = 8;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
