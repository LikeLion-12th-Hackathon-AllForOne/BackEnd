package com.likelion.allForOne.domain.tblLetterPackage;

import com.likelion.allForOne.entity.TblLetterPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TblLetterPackageRepository extends JpaRepository<TblLetterPackage, Long> {
    Optional<TblLetterPackage> findByGroup_GroupSeq(Long groupSeq);
}
