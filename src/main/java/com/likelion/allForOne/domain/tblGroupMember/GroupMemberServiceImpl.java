package com.likelion.allForOne.domain.tblGroupMember;

import com.likelion.allForOne.domain.tblCode.service.CodeServiceImpl;
import com.likelion.allForOne.entity.TblCode;
import com.likelion.allForOne.entity.TblGroup;
import com.likelion.allForOne.entity.TblGroupMember;
import com.likelion.allForOne.entity.TblUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupMemberServiceImpl {
    private final TblGroupMemberRepository groupMemberRepository;

    private final CodeServiceImpl codeService;

    /**
     * 그룹 참가 회원 튜플 생성
     * @param group TblGroup:생성될 회원이 참가하고 있는 그룹 정보
     * @return Long:생성된 튜플의 구분자
     */
    public Long saveGroupMember(TblGroup group, TblUser user){
        TblCode code = codeService.findCodes(group.getCodeCategory().getCodeSeq());
        TblGroupMember groupMember = TblGroupMember.builder()
                .group(group)
                .user(user)
                .codeCategoryRole(
                        "가족".equals(code.getCodeName()) ? null
                                : codeService.findCodeByUnitAndParentCode(3, code.getCodeSeq()))
                .codePackage(TblCode.builder()
                        .codeSeq(30L)
                        .build()) //codeSeq: 30=확인, 31=미확인
                .build();
        return groupMemberRepository.save(groupMember).getMemberSeq();
    }

    /**
     * 특정 사용자의 그룹회원 리스트 조회
     * @param userSeq Long: 사용자 구분자
     * @return List<TblGroupMember>:특정 사용자의 그룹회원 리스트
     */
    public List<TblGroupMember> findListGroupMember(Long userSeq){
        return groupMemberRepository.findByUser_UserSeq(userSeq);
    }

    /**
     * 특정 그룹의 그룹회원 리스트 조회
     * @param groupSeq Long: 그룹 구분자
     * @return List<TblGroupMember>:특정 사용자의 그룹회원 리스트
     */
    public List<TblGroupMember> findListGroupMemberByGroup(Long groupSeq){
        return groupMemberRepository.findByGroup_GroupSeqOrderByUser_UserName(groupSeq);
    }

    /**
     * 특정 사용자의 특정 그룹 가입 여부 확인
     * @param groupSeq Long:그룹 구분자
     * @param userSeq Long: 사용자 구분자
     * @return boolean:특정 사용자의 특정 그룹 가입 여부
     */
    public boolean checkGroupMember(Long groupSeq, Long userSeq){
        Optional<TblGroupMember> check = groupMemberRepository.findByGroup_GroupSeqAndUser_UserSeq(groupSeq, userSeq);
        return check.isPresent();
    }

    /**
     * 그룹에 실제 참가중인 회원수 조회
     * @param groupSeq Long: 그룹 구분자
     * @return long: 그룹에 실제 참가중인 회원수
     */
    public long cntJoinGroupMember(Long groupSeq){
        return groupMemberRepository.countByGroup_GroupSeq(groupSeq);
    }

    /**
     * 사용자와 그룹으로 멤버 조회 및 역할 update
     * @param groupSeq Long: 그룹 구분자
     * @param userSeq Long: 사용자 구분자
     * @return boolean
     */
    public boolean updateRole(Long groupSeq, Long userSeq, Long codeCategoryRoleSeq){
        //1. 사용자와 그룹으로 멤버 조회
        Optional<TblGroupMember> memberOpt = groupMemberRepository.findByGroup_GroupSeqAndUser_UserSeq(groupSeq, userSeq);
        if (memberOpt.isEmpty()) return false;

        //2. 역할 update
        TblCode codeEntity = codeService.findCodes(codeCategoryRoleSeq);
        if (codeEntity == null) return false;

        TblGroupMember memberEntity = memberOpt.get();
        memberEntity.updateRole(codeEntity);
        return true;
    }

    /**
     * 멤버 구분자로 멤버조회
     * @param memberSeq Long: 멤버 구분자
     * @return TblGroupMember: 회원 엔티티
     */
    public TblGroupMember findByGroupMemberSeq(Long memberSeq){
        return groupMemberRepository.findById(memberSeq).orElse(null);
    }

    /**
     * 방(그룹) 역할에 맞는 멤버 이름 출력하기
     * @param memberTarget TblGroupMember: 멤버 entity
     * @return String 출력될 이름
     */
    public String findMemberTargetName(TblGroupMember memberTarget){
        TblCode codeCategoryRole = memberTarget.getCodeCategoryRole();
        if (codeCategoryRole == null) {
            return memberTarget.getUser().getUserName()+"(역할 미선택)";
        }else if (codeCategoryRole.getCodeSeq() == 38
                    || codeCategoryRole.getCodeSeq() == 39)
            return codeCategoryRole.getCodeName();
        else return memberTarget.getUser().getUserName();
    }
}
