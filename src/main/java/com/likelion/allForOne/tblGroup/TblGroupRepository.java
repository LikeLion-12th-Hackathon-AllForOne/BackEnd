package com.likelion.allForOne.tblGroup;

import com.likelion.allForOne.entity.TblGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TblGroupRepository extends JpaRepository<TblGroup, Long> {
    Optional<TblGroup> findByGroupInviteCode(String groupInviteCode);   //초대코드
    Optional<TblGroup> findByGroupSeqAndUserOwner_UserSeq(Long groupSeq, Long userSeq);   //그룹구분자, 방장 사용자(로그인 사용자)

}
