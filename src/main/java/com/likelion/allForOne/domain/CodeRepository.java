package com.likelion.allForOne.domain;

import com.likelion.allForOne.entity.tblCode;
import com.likelion.allForOne.entity.tblUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<tblCode, Long> {
    tblCode findByCodeSeq(Long codeSeq);
}
