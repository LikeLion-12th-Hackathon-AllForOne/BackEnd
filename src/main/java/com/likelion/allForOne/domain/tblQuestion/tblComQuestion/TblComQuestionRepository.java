package com.likelion.allForOne.domain.tblQuestion.tblComQuestion;

import com.likelion.allForOne.domain.tblQuestion.dto.QuestionDto;
import com.likelion.allForOne.entity.TblComQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TblComQuestionRepository extends JpaRepository<TblComQuestion, Long> {

    @Query(value="select  " +
            "null as addQuestionSeq, " +
            "tcq.com_question_seq as comQuestionSeq, " +
            "tcq.code_question_type as codeQuestionType, " +
            "tcq.code_question_class as codeQuestionClass, " +
            "tgm.member_seq as memberTarget " +
            "from tbl_com_question tcq  " +
            "inner join tbl_code tc  " +
            "on tc.code_seq = tcq.code_question_type " +
            "and tc.code_name = 'comAll' " +
            "cross join tbl_group_member tgm   " +
            "inner join tbl_user tu on tu.user_seq = tgm.user_seq  " +
            "left join tbl_used_question tuq  " +
            "on tuq.com_question_seq = tcq.com_question_seq  " +
            "and tuq.member_target = tgm.member_seq  " +
            "where tgm.group_seq = :groupSeq " +
            "and tuq.used_question_seq is null " +
            "union all " +
            "select  " +
            "null as addQuestionSeq, " +
            "tcq.com_question_seq as comQuestionSeq, " +
            "tcq.code_question_type as codeQuestionType, " +
            "tcq.code_question_class as codeQuestionClass, " +
            "null as memberTarget  " +
            "from tbl_com_question tcq  " +
            "inner join tbl_code tc  " +
            "on tc.code_seq = tcq.code_question_type " +
            "and tc.code_name = 'comTarget'  " +
            "left join tbl_used_question tuq  " +
            "on tuq.com_question_seq = tcq.com_question_seq " +
            "and tuq.group_seq = :groupSeq " +
            "where tuq.used_question_seq is null " +
            "order by RAND() limit 1", nativeQuery = true)
    Object[] findByGroup(@Param("groupSeq") Long groupSeq);  // 사용되지 않은 추가질문 랜덤으로 한개 조회
}
