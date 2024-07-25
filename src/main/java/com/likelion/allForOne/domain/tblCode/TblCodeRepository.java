package com.likelion.allForOne.domain.tblCode;

import com.likelion.allForOne.entity.TblCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TblCodeRepository extends JpaRepository<TblCode, Long> {
    Optional<TblCode> findByCodeUnitAndCodeName(int codeUnit, String codeName);
    List<TblCode> findByCodeParent_CodeSeq(Long codeParentSeq);
    TblCode findByCodeSeq(Long codeSeq);
}
