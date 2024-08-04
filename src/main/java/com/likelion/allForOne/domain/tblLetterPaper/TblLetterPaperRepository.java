package com.likelion.allForOne.domain.tblLetterPaper;

import com.likelion.allForOne.entity.TblCode;
import com.likelion.allForOne.entity.TblLetterPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TblLetterPaperRepository extends JpaRepository<TblLetterPaper, Long> {
    List<TblLetterPaper> findByCodePaper(TblCode code);
}