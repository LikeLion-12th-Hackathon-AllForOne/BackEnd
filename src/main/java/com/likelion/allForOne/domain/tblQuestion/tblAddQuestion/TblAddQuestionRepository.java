package com.likelion.allForOne.domain.tblQuestion.tblAddQuestion;

import com.likelion.allForOne.entity.TblAddQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TblAddQuestionRepository extends JpaRepository<TblAddQuestion, Long> {
    @Query(value = "select taq.* " +
            "from tbl_add_question taq " +
            "inner join tbl_group_member tgm " +
            "on tgm.member_seq = taq.member_create " +
            "left join tbl_used_question tuq " +
            "on tuq.add_question_seq = taq.add_question_seq " +
            "where tuq.used_question_seq is null " +
            "and tgm.group_seq = :groupSeq " +
            "order by RAND() limit 1", nativeQuery = true)
    Optional<TblAddQuestion> findByGroup(@Param("groupSeq") Long groupSeq);  // 사용되지 않은 추가질문 랜덤으로 한개 조회
}
