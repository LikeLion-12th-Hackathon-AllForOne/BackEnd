package com.likelion.allForOne.domain.tblGroupMember;

import com.likelion.allForOne.entity.TblGroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TblGroupMemberRepository extends JpaRepository<TblGroupMember, Long> {
    List<TblGroupMember> findByUser_UserSeq(Long userSeq);
    List<TblGroupMember> findByGroup_GroupSeqOrderByUser_UserName(Long groupSeq);
    Optional<TblGroupMember> findByGroup_GroupSeqAndUser_UserSeq(Long groupSeq, Long userSeq);
    Long countByGroup_GroupSeq(Long groupSeq);
    Optional<TblGroupMember> findByMemberSeq(Long memberSeq);
    int deleteByUser_UserSeq(Long userSeq);
}
