package com.likelion.allForOne.domain;

import com.likelion.allForOne.entity.TblCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<TblCode, Long> {
    TblCode findByCodeSeq(Long codeSeq);
}
