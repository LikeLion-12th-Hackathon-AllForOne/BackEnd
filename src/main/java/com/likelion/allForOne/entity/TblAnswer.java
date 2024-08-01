package com.likelion.allForOne.entity;

import com.likelion.allForOne.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TblAnswer extends BaseEntity {
    @Id
    @Comment(value="구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerSeq;         //구분자
    @Column(columnDefinition = "varchar(500) null comment '답변'")
    private String answerContents;  //답변
    @Column(columnDefinition = "tinyint not null comment '임시저장 여부'")
    private int answerTmpYn;  //임시저장 여부(저장:0 / 임시저장:1)


    @Comment(value="질문 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_question_seq", nullable = false)
    private TblUsedQuestion usedQuestion;    //질문 구분자

    @Comment(value="답변 생성자 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_answer", nullable = false)
    private TblGroupMember memberAnswer;        //답변 생성자 구분자

    @Comment(value="질문 대상자 구분자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_target", nullable = false)
    private TblGroupMember memberTarget;        //질문 대상자 구분자


    /* ============================================================================
     * update
     * ============================================================================ */
    // 답변 내용 변경
    public void updateAnswerContents(String answerContents, int answerTmpYn){
        this.answerContents = answerContents;
        this.answerTmpYn = answerTmpYn;
    }

}
