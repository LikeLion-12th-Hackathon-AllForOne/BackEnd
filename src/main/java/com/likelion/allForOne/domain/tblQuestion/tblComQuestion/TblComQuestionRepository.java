package com.likelion.allForOne.domain.tblQuestion.tblComQuestion;

import com.likelion.allForOne.domain.tblQuestion.dto.QuestionDto;
import com.likelion.allForOne.entity.TblComQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TblComQuestionRepository extends JpaRepository<TblComQuestion, Long> {

    @Query(value="select " +
            "null as add_question_seq, " +
            "tcq.com_question_seq, " +
            "tcq.code_question_type, " +
            "tcq.code_question_class, " +
            "null as member_target " +
            "from tbl_com_question tcq " +
            "inner join tbl_code tc1 on tc1.code_seq = tcq.code_question_type and tc1.code_name = 'comAll' " +
            "inner join tbl_code tc2 on tc2.code_seq = tcq.code_question_class " +
            "inner join tbl_group tg on tg.code_category = tc2.code_val and tg.group_seq = :groupSeq " +
            "left join tbl_used_question tuq on tuq.com_question_seq = tcq.com_question_seq and tuq.group_seq = tg.group_seq " +
            "where tuq.used_question_seq is null " +
            "union " +
            "select " +
            "null as add_question_seq, " +
            "tcq.com_question_seq, " +
            "tcq.code_question_type, " +
            "tcq.code_question_class, " +
            "tgm.member_seq as member_target " +
            "from tbl_com_question tcq " +
            "inner join tbl_code tc1 on tc1.code_seq = tcq.code_question_type and tc1.code_name = 'comTarget' " +
            "inner join tbl_code tc2 on tc2.code_seq = tcq.code_question_class " +
            "inner join tbl_group tg on tg.code_category = tc2.code_val and tg.group_seq = :groupSeq " +
            "join tbl_group_member tgm on tgm.group_seq = tg.group_seq " +
            "inner join tbl_user tu on tu.user_seq = tgm.user_seq " +
            "left join tbl_used_question tuq on tuq.com_question_seq = tcq.com_question_seq and tuq.member_target = tgm.member_seq " +
            "where tuq.used_question_seq is null " +
            "order by RAND() limit 1", nativeQuery = true)
    Object[] findByGroup(@Param("groupSeq") Long groupSeq);  // 사용되지 않은 추가질문 랜덤으로 한개 조회
}
