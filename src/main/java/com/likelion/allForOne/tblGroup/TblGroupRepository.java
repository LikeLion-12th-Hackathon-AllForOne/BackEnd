package com.likelion.allForOne.tblGroup;

import com.likelion.allForOne.entity.TblGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TblGroupRepository extends JpaRepository<TblGroup, Long> {
    Optional<TblGroup> findByGroupInviteCode(String groupInviteCode);   //초대코드로 그룹 찾기

}
