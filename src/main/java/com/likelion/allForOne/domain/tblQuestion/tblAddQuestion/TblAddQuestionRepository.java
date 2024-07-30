package com.likelion.allForOne.domain.tblQuestion.tblAddQuestion;

import com.likelion.allForOne.domain.tblQuestion.dto.QuestionDto;
import com.likelion.allForOne.entity.TblAddQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TblAddQuestionRepository extends JpaRepository<TblAddQuestion, Long> {
    @Query(value = "select " +
            "taq.add_question_seq as addQuestionSeq, " +
            "null as comQuestionSeq, " +
            "(select code_seq from tbl_code tc where code_parent_seq = 4 and code_name='addTarget' ) as codeQuestionType, " +
            "taq.code_question_class as codeQuestionClass, " +
            "taq.member_create as memberTarget " +
            "from tbl_add_question taq " +
            "inner join tbl_group_member tgm " +
            "on tgm.member_seq = taq.member_create " +
            "and tgm.group_seq = :groupSeq " +
            "inner join tbl_user tu on tu.user_seq = tgm.user_seq " +
            "left join tbl_used_question tuq " +
            "on tuq.add_question_seq = taq.add_question_seq " +
            "where tuq.used_question_seq is null " +
            "order by RAND() limit 1", nativeQuery = true)
    Object[] findByGroup(@Param("groupSeq") Long groupSeq);  // 사용되지 않은 추가질문 랜덤으로 한개 조회
}
