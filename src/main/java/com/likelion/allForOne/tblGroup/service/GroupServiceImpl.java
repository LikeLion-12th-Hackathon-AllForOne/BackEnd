package com.likelion.allForOne.tblGroup.service;

import com.likelion.allForOne.entity.TblCode;
import com.likelion.allForOne.entity.TblGroup;
import com.likelion.allForOne.entity.TblLetterPackage;
import com.likelion.allForOne.entity.TblUser;
import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.global.response.CustomException;
import com.likelion.allForOne.global.response.resEnum.ErrorCode;
import com.likelion.allForOne.global.response.resEnum.SuccessCode;
import com.likelion.allForOne.tblCode.service.CodeServiceImpl;
import com.likelion.allForOne.tblGroup.TblGroupRepository;
import com.likelion.allForOne.tblGroup.dto.GroupRequestDto;
import com.likelion.allForOne.tblLetterPackage.LetterPackageServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {
    private final TblGroupRepository groupRepository;
    private final CodeServiceImpl codeService;
    private final LetterPackageServiceImpl letterPackageService;

    /**
     * 방(그룹) 생성
     * @param data GroupRequestDto.saveOneGroup:방(그룹) 생성시 필요한 데이터
     * @param userSeq Long:로그인 사용자 Seq
     * @return ApiResponse<?>
     */
    @Override
    @Transactional
    public ApiResponse<?> saveOneGroup(GroupRequestDto.saveOneGroup data, Long userSeq) {
        //1. 유저 정보 확인 (로그인 기능 완료 후 추가)

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

        //8. return
        if(packageSeq == null) throw new CustomException(ErrorCode.CREATE_FAIL);
        return ApiResponse.SUCCESS(SuccessCode.CREATE_GROUP);
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
