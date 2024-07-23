package com.likelion.allForOne.tblGroupMember;

import com.likelion.allForOne.entity.TblGroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblGroupMemberRepository extends JpaRepository<TblGroupMember, Long> {
    List<TblGroupMember> findByUser_UserSeq(Long userSeq);
}
