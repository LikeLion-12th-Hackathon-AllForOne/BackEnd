package com.likelion.allForOne.tblGroupMember;

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

    /**
     * 그룹 참가 회원 튜플 생성
     * @param group TblGroup:생성될 회원이 참가하고 있는 그룹 정보
     * @return Long:생성된 튜플의 구분자
     */
    public Long saveGroupMember(TblGroup group, TblUser user){
        TblGroupMember groupMember = TblGroupMember.builder()
                .group(group)
                .user(user)
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
}
