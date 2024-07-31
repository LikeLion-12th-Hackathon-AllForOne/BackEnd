package com.likelion.allForOne.domain.tblQuestion.tblAnswer;

import com.likelion.allForOne.entity.TblAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TblAnswerRepository extends JpaRepository<TblAnswer,Long> {
    Optional<TblAnswer> findByUsedQuestion_UsedQuestionSeqAndMemberAnswer_MemberSeqAndMemberTarget_MemberSeq(Long groupSeq, Long memberAnswer, Long memberTarget);
}
