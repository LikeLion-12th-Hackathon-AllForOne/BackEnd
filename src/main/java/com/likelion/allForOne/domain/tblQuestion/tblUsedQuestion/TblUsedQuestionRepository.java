package com.likelion.allForOne.domain.tblQuestion.tblUsedQuestion;

import com.likelion.allForOne.entity.TblUsedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TblUsedQuestionRepository extends JpaRepository<TblUsedQuestion, Long> {

    @Query(value="select " +
            "tuq.used_question_seq, " +
            "tuq.code_question_type, " +
            "case " +
            "when (tuq.member_target is null or tuq.code_question_class = 31) then '' " +
            "when tuq.code_question_class = 32 then (select tc.code_name from tbl_group_member tgm inner join tbl_code tc on tc.code_seq = tgm.code_category_role where tgm.member_seq = tuq.member_target) " +
            "else (select tu.user_name from tbl_user tu inner join tbl_group_member tgm on  tgm.user_seq = tu.user_seq and tgm.member_seq = tuq.member_target) " +
            "end as target, " +
            "case " +
            "when tuq.code_question_type = 28 then (select tcq.com_question from tbl_com_question tcq where tcq.com_question_seq = tuq.com_question_seq) " +
            "when tuq.code_question_type = 29 then (select tcq.com_question from tbl_com_question tcq where tcq.com_question_seq = tuq.com_question_seq) " +
            "when tuq.code_question_type = 30 then (select taq.add_question from tbl_add_question taq where taq.add_question_seq = tuq.add_question_seq) " +
            "else '' " +
            "end as question " +
            "from tbl_used_question tuq " +
            "where tuq.group_seq = :groupSeq " +
            "and inp_date = curdate()", nativeQuery = true)
    Object[] findByInpDateAndGroup_GroupSeq(@Param("groupSeq") Long groupSeq);
    @Query(value="select " +
            "tuq.used_question_seq, " +
            "tuq.code_question_type, " +
            "date_format(tuq.inp_date, '%Y년 %m월 %d일') as inp_date, " +
            "case " +
            "when (tuq.member_target is null or tuq.code_question_class = 31) then '' " +
            "when tuq.code_question_class = 32 then (select tc.code_name from tbl_group_member tgm inner join tbl_code tc on tc.code_seq = tgm.code_category_role where tgm.member_seq = tuq.member_target) " +
            "else (select tu.user_name from tbl_user tu inner join tbl_group_member tgm on  tgm.user_seq = tu.user_seq and tgm.member_seq = tuq.member_target) " +
            "end as target, " +
            "case " +
            "when tuq.code_question_type = 28 then (select tcq.com_question from tbl_com_question tcq where tcq.com_question_seq = tuq.com_question_seq) " +
            "when tuq.code_question_type = 29 then (select tcq.com_question from tbl_com_question tcq where tcq.com_question_seq = tuq.com_question_seq) " +
            "when tuq.code_question_type = 30 then (select taq.add_question from tbl_add_question taq where taq.add_question_seq = tuq.add_question_seq) " +
            "else '' " +
            "end as question " +
            "from tbl_used_question tuq " +
            "where tuq.used_question_seq = :usedQuestionSeq", nativeQuery = true)
    Object[] findByUsedQuestionSeq(@Param("usedQuestionSeq") Long usedQuestionSeq);
    List<TblUsedQuestion> findTop7ByInpDateBeforeAndGroup_GroupSeqOrderByInpDateDesc(LocalDate inpDate, Long groupSeq);
    Optional<TblUsedQuestion> findByInpDateAndGroup_GroupSeq(LocalDate inpDate, Long groupSeq);
}
