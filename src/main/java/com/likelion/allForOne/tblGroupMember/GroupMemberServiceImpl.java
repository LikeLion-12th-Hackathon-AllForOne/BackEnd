package com.likelion.allForOne.tblGroupMember;

import com.likelion.allForOne.entity.TblCode;
import com.likelion.allForOne.entity.TblGroup;
import com.likelion.allForOne.entity.TblGroupMember;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupMemberServiceImpl {
    private final TblGroupMemberRepository groupMemberRepository;

    /**
     * 그룹 참가 회원 튜플 생성
     * @param group TblGroup:생성될 회원이 참가하고 있는 그룹 정보
     * @return Long:생성된 튜플의 구분자
     */
    public Long saveGroupMember(TblGroup group){
        TblGroupMember groupMember = TblGroupMember.builder()
                .group(group)
                .user(group.getUserOwner())
                .codePackage(TblCode.builder()
                        .codeSeq(30L)
                        .build()) //codeSeq: 30=확인, 31=미확인
                .build();
        return groupMemberRepository.save(groupMember).getMemberSeq();
    }
}
