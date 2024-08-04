package com.likelion.allForOne.domain.tblLetter;

import com.likelion.allForOne.entity.TblLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TblLetterRepository extends JpaRepository<TblLetter, Long> {
    @Query(value="select * " +
            "from tbl_letter " +
            "where member_to = :memberTo " +
            "and letter_to = :letterTo ", nativeQuery = true)
    List<TblLetter> searchTblLetterByMemberToAndLetterTo(@Param("memberTo") int memberTo, @Param("letterTo") String letterTo);
}
